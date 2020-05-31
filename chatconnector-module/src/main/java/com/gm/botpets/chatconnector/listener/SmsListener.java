package com.gm.botpets.chatconnector.listener;

import static spark.Spark.port;
import static spark.Spark.post;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.gm.botpets.chatconnector.model.ChannelType;
import com.gm.botpets.chatconnector.model.ChatRequest;
import com.gm.botpets.chatconnector.model.NLPRequest;
import com.gm.botpets.chatconnector.model.SmsDTO;
import com.gm.botpets.chatconnector.model.User;
import com.gm.botpets.chatconnector.service.IChatService;
import com.gm.botpets.chatconnector.service.IMessageProcessor;
import com.gm.botpets.chatconnector.util.ChatConnectorUtil;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;

@Component
public class SmsListener {

	private Logger logger = LoggerFactory.getLogger(SmsListener.class);

	private static String username;
	private static String password;

	@Autowired
	private Environment env;

	@Autowired
	private IMessageProcessor messageProcessor;

	@Autowired
	private ChatConnectorUtil chatConnectorUtil;

	@Autowired
	private IChatService chatService;

	@PostConstruct
	public void listen() {

		int port = Integer.valueOf(env.getProperty("jetty.server.port"));
		port(port);

		logger.info("retrieveSms started()");
		post("/answer", (req, res) -> {
			Map<String, String> parameters = parseBody(req.body());
			NLPRequest nlpRequest = null;

			String caller = parameters.get("From");
			String twilioNumber = parameters.get("To");
			String message = parameters.get("Body");
			String smsStatus = parameters.get("SmsStatus");
			logger.info("smsListener started()");
			processSMSRequest(message, caller, twilioNumber, smsStatus);
			VoiceResponse twiml = new VoiceResponse.Builder()
					.say(new Say.Builder("Thanks for calling! We just sent you a text with a clue.")
							.voice(Say.Voice.ALICE).build())
					.build();

			return twiml.toXml();
		});

	}

	private Map<String, String> parseBody(String body) throws UnsupportedEncodingException {
		String[] unparsedParams = body.split("&");
		Map<String, String> parsedParams = new HashMap<String, String>();
		for (int i = 0; i < unparsedParams.length; i++) {
			String[] param = unparsedParams[i].split("=");
			if (param.length == 2) {
				parsedParams.put(urlDecode(param[0]), urlDecode(param[1]));
			} else if (param.length == 1) {
				parsedParams.put(urlDecode(param[0]), "");
			}
		}
		return parsedParams;
	}

	public static String urlDecode(String s) throws UnsupportedEncodingException {
		return URLDecoder.decode(s, "utf-8");
	}

	public void processSMSRequest(String message, String toNumber, String fromNumber, String smsStatus) {
		Object response = null;
		SmsDTO smsDTO = new SmsDTO();
		smsDTO.setTonumber(toNumber);
		smsDTO.setFromnumber(fromNumber);
		smsDTO.setMessage(message);
		long starttime = System.currentTimeMillis();
		String sessionId = chatConnectorUtil.base64EncodedSessionId(ChannelType.TWILIOSMS + extractPhoneNo(toNumber));
		ChatRequest chatRequest = new ChatRequest();
		NLPRequest nlpRequest = new NLPRequest();
		nlpRequest.setQuestion(message.trim());
		User user = new User();
		user.setPhoneNo(extractPhoneNo(toNumber));
		logger.info("User details: " + user.toString());
		nlpRequest.setUser(user);
		nlpRequest.setSessionId(sessionId);
		try {
			response = messageProcessor.processMessageRequest(nlpRequest);
			if (smsStatus.equalsIgnoreCase("received")) {
				chatRequest.setChannelType(ChannelType.TWILIOSMS);
			}
			chatRequest.setChannelDTO(smsDTO);
			chatRequest.setMessage(response);
			long endtime = System.currentTimeMillis();
			long difftime = endtime - starttime;
			logger.info("Time taken to process in Sec's:" + difftime / 1000);
			chatService.processChatRequest(chatRequest);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String extractPhoneNo(String number) {
		int indx = number.indexOf("+");
		return number.substring(indx);
	}

}