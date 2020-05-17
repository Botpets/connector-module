package com.gm.botpets.nlp.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.botpets.nlp.exception.BPNLPConnectorException;
import com.gm.botpets.nlp.model.BPNLPResponse;
import com.gm.botpets.nlp.model.WatsonConfiguration;
import com.gm.botpets.nlp.service.INLPService;
import com.gm.botpets.nlp.util.LanguagueUtil;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.language_translator.v3.util.Language;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
@Qualifier("watsonNLPService")
public class WatsonNLPServiceImpl implements INLPService {

	private static Logger logger = LoggerFactory.getLogger(WatsonNLPServiceImpl.class);
	public static final String THRESHOLD_SESSION_TIME = "threshold.session.time";

	@Autowired
	private Environment env;

	@Autowired
	private LanguagueUtil languagueUtil;

	@Override
	public Object invokeNLPEngine(String message, String sourceLanguage, String sessionId)
			throws BPNLPConnectorException {
		WatsonConfiguration configuration = constructWatsonConfiguration();
		try {
			return invokeNLPEngine(message, sourceLanguage, sessionId, configuration);
		} catch (Exception e) {
			logger.error("Failed to process message on Watson ", e);
			throw new BPNLPConnectorException("Failed to process message on Watson");
		}
	}

	private BPNLPResponse invokeNLPEngine(String message, String sourceLanguage, String sessionId,
			WatsonConfiguration configurtion) throws Exception {
		logger.debug("invokeNLPEngine stated()\n" + "Requested message:" + message);
		Assistant service = new Assistant(configurtion.getVersion(), configurtion.getUsername(),
				configurtion.getPassword());
		service.setEndPoint(configurtion.getUrl());
		String separator = System.lineSeparator();
		MessageResponse response = null;
		if (message.contains("\n")) {
			message = message.substring(0, message.indexOf(separator));
		}
		String translatedMessage = null;
		if (!sourceLanguage.equals(Language.ENGLISH)) {
			translatedMessage = languagueUtil.translate(message, sourceLanguage, Language.ENGLISH);
		} else {
			translatedMessage = message;
		}
		InputData input = new InputData.Builder(translatedMessage).build();

		MessageOptions options = null;

		boolean isSessionExists = findBySessionId(sessionId);
		String reason = null;
		if (!isSessionExists) {
			options = new MessageOptions.Builder(configurtion.getWorkspaceId()).input(input).build();
		} else {
			boolean validSession = isSessionValid(sessionId);
			if (validSession) {
				Context context = getContextBySessionId(sessionId);
				if (context != null) {
					options = new MessageOptions.Builder(configurtion.getWorkspaceId()).context(context).input(input)
							.build();
				}
			} else {
				input = new InputData.Builder("hi").build();
				options = new MessageOptions.Builder(configurtion.getWorkspaceId()).input(input).build();
			}
		}

		response = service.message(options).execute();
		reason = (String) response.getContext().getSystem().get("branch_exited_reason");

		manageSession(isSessionExists, reason, sessionId, response);

		logger.info("Watson response: " + response);

		BPNLPResponse bpNLPResponse = constructResponse(response, reason);
		return bpNLPResponse;
	}

	private BPNLPResponse constructResponse(MessageResponse response, String reason) {
		BPNLPResponse bpNLPResponse = new BPNLPResponse();
		if (response != null) {
			List<String> inputs = null;
			Context context = response.getContext();
			String intent = (String) context.get("intent");

			Map<String, String> contextParams = (Map<String, String>) context.get("context_params");
			if (contextParams != null) {
				// iterate over contextParams and add to "inputs" array list
				inputs = new ArrayList<>(contextParams.values());
				bpNLPResponse.setRequestInputs(inputs);

			}

			if (intent != null && reason != null) {
				bpNLPResponse.setIntent(intent);
			} else {
				bpNLPResponse.setOutputMessage(response.getOutput().getText().get(0));

			}

		}
		return bpNLPResponse;
	}

	private boolean isSessionValid(String sessionId) {
		MongoCollection<Document> collection = invokeMongoDB();
		boolean flag = true;
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		List<Document> nlpDataValues = (List<Document>) collection.find().into(new ArrayList<Document>());

		for (Document timestamp : nlpDataValues) {
			String lastUpTime = (String) timestamp.get("lastUpdatedTimestamp");
			long UpdatedTime = 0;
			Date date;
			try {
				date = sdf.parse(lastUpTime);
				UpdatedTime = date.getTime();
			} catch (ParseException e) {
				logger.info(
						"Error inmiantainSessionWithThresholdTime:Unable to delete session based on threshold time");
			}
			long currentTime = System.currentTimeMillis();
			long diffTime = currentTime - UpdatedTime;
			long thresholdTime = Long.parseLong(env.getProperty(THRESHOLD_SESSION_TIME));
			if (diffTime > thresholdTime) {
				deleteWatsonRes(sessionId);
				flag = false;
			}
		}
		return flag;
	}

	private void updateWatsonRes(String sessionId, MessageResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		MongoCollection<Document> collection = invokeMongoDB();
		try {
			jsonString = mapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Document doc = Document.parse(jsonString);
		doc.append("lastUpdatedTimestamp", getCurrentTime());
		doc.append("sessionId", sessionId);
		Bson bsonDoc = doc;
		Bson filter = new Document("sessionId", sessionId);
		collection.findOneAndReplace(filter, doc);
	}

	private WatsonConfiguration constructWatsonConfiguration() {
		WatsonConfiguration configuration = new WatsonConfiguration();
		configuration.setUsername(env.getProperty("watson.username"));
		configuration.setPassword(env.getProperty("watson.password"));
		configuration.setUrl(env.getProperty("watson.url"));
		configuration.setVersion(env.getProperty("watson.version"));
		configuration.setWorkspaceId(env.getProperty("watson.workspaceId"));
		return configuration;
	}

	public static void insertWatsonRes(String sessionId, MessageResponse response) {

		try {
			System.out.println("invokeMongoDB() started");
			MongoCollection<Document> collection = invokeMongoDB();

			// Show all documents in the collection
			showAllDocuments(collection);
			boolean status = insertIntoCollection(collection, response, sessionId);
			System.out.println("Status of Insert: " + status);
			showAllDocuments(collection);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	private void manageSession(boolean isSessionExists, String reason, String sessionId, MessageResponse response) {
		boolean flag = false;

		if (isSessionExists) {
			// session exists
			if (reason == null) {
				// update
				updateWatsonRes(sessionId, response);
			} else {
				// deleted
				deleteWatsonRes(sessionId);
			}
		} else {
			// does session exists
			if (reason == null) {
				// insert
				insertWatsonRes(sessionId, response);
			} else {
				// do nothing
			}
		}

	}

	public static void deleteWatsonRes(String sessionId) {
		try {
			MongoCollection<Document> collection = invokeMongoDB();
			Bson filter = new Document("sessionId", sessionId);
			collection.deleteOne(filter);
		} catch (Exception e) {
			logger.info("Error in deleteWatsonRes:Unable to delete chat session");
		} finally {
		}
	}

	public static boolean insertIntoCollection(final MongoCollection<Document> collection, MessageResponse response,
			String sessionId) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString;
		boolean status = true;

		try {
			jsonString = mapper.writeValueAsString(response);

			// Insert JSON into MongoDB
			System.out.println(String.format("Item #%s: %s", sessionId, jsonString));
			Document doc = Document.parse(jsonString);
			doc.append("lastUpdatedTimestamp", getCurrentTime());
			doc.append("sessionId", sessionId);
			collection.insertOne(doc);
		} catch (MongoWriteException mwe) {
			status = false;
		} catch (IOException e) {
			status = false;
		}
		return status;
	}

	private static Object getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static void showAllDocuments(final MongoCollection<Document> collection) {
		System.out.println("All Items in the Inventory Collection");
		for (Document doc : collection.find()) {
			System.out.println(doc.toJson());
		}
	}

	private boolean findBySessionId(String sessionId) {
		MongoCollection<Document> collection = invokeMongoDB();
		boolean status = false;

		List<Document> nlpDataValues = (List<Document>) collection.find().into(new ArrayList<Document>());

		for (Document nlpData : nlpDataValues) {
			String sId = (String) nlpData.get("sessionId");
			if (sId.equalsIgnoreCase(sessionId)) {
				status = true;
			}
		}
		return status;
	}

	private Context getContextBySessionId(String sessionId) {
		MongoCollection<Document> collection = invokeMongoDB();
		Context context = null;
		FindIterable<Document> docs = collection.find();
		for (Document doc : docs) {
			if (doc.get("sessionId").toString().equalsIgnoreCase(sessionId)) {

				Document ctxDoc = (Document) doc.get("context");
				ObjectMapper mapper = new ObjectMapper();
				try {
					context = (Context) mapper.readValue(ctxDoc.toJson(), Context.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			logger.info(doc.get("context").toString());
		}

		return context;
	}

	private static MongoCollection<Document> invokeMongoDB() {
		MongoClient client = new MongoClient("localhost", 27017);
		MongoDatabase database = client.getDatabase("nlpdata");
		MongoCollection<Document> collection = database.getCollection("inventory");
		return collection;
	}

}
