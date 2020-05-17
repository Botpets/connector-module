package com.gm.botpets.chatconnector.listener;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.riversun.slacklet.Slacklet;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;
import org.riversun.slacklet.SlackletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gm.botpets.chatconnector.admin.model.SlackConnetorDetails;
import com.gm.botpets.chatconnector.commons.ConnectorConfig;
import com.gm.botpets.chatconnector.model.ChannelType;
import com.gm.botpets.chatconnector.model.ChatRequest;
import com.gm.botpets.chatconnector.model.NLPRequest;
import com.gm.botpets.chatconnector.model.User;
import com.gm.botpets.chatconnector.service.IMessageProcessor;
import com.gm.botpets.chatconnector.util.ChatConnectorUtil;

@Component
public class SlackListener {

	private Logger logger = LoggerFactory.getLogger(SlackListener.class);

	@Autowired
	private IMessageProcessor messageProcessor;

	@Autowired
	private ChatConnectorUtil chatConnectorUtil;

	@Autowired
	ConnectorConfig connectorConfig;

	private static SlackletService slackService;

	@PostConstruct
	public void startSlackService() {
		// Load bot token from the database
		String botToken = loadBotDetails();
		if (botToken != null) {
			slackService = new SlackletService(botToken);
			processRequest();
			try {
				slackService.start();
				logger.info("**************** Slack listener started **************");
			} catch (IOException e1) {
				logger.info("Error in Slack Listener");
			}
			
		} else {
			logger.warn("!!!!!!!!! Slack listener not started !!!!!!!!!!!");
		}
	}

	private String loadBotDetails() {
		SlackConnetorDetails slackConnetorDetails = ConnectorConfig.getSlackConnectorDetails();
		String token = null;
		if (slackConnetorDetails != null) {
			token = slackConnetorDetails.getSlackBotToken();
		} else {
			logger.warn("Slack configurations not found");
		}
		return token;
	}

	private void processRequest() {
		long starttime = System.currentTimeMillis();
		ChatRequest chatRequest = new ChatRequest();
		NLPRequest nlpRequest = new NLPRequest();
		User user = new User();

		slackService.addSlacklet(new Slacklet() {
			String userEmail = null;
			String question = null;
			Object response = null;

			@Override
			public void onDirectMessagePosted(SlackletRequest req, SlackletResponse resp) {
				userEmail = req.getSender().getUserMail();
				question = req.getContent();
				String sessionId = chatConnectorUtil.base64EncodedSessionId(ChannelType.SLACK + userEmail);
				nlpRequest.setQuestion(question);
				user.setEmail(userEmail);
				nlpRequest.setUser(user);
				nlpRequest.setSessionId(sessionId);
				try {
					response = messageProcessor.processMessageRequest(nlpRequest);
					chatRequest.setMessage(response);
					long endtime = System.currentTimeMillis();
					long difftime = endtime - starttime;
					logger.info("Time taken to process in Sec's:" + difftime / 1000);
				} catch (Exception e) {
					chatRequest.setMessage("Apologies, something went wrong");
					logger.error("Failed to process slack request ", e);
				}
				resp.reply(chatRequest.getMessage().toString());
			}
		});

	}

}