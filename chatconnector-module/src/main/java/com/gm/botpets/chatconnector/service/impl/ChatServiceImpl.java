package com.gm.botpets.chatconnector.service.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.gm.botpets.chatconnector.exception.ChatConnectorException;
import com.gm.botpets.chatconnector.mail.service.IEmailService;
import com.gm.botpets.chatconnector.model.ChannelType;
import com.gm.botpets.chatconnector.model.ChatRequest;
import com.gm.botpets.chatconnector.model.EmailDTO;
import com.gm.botpets.chatconnector.model.NLPRequest;
import com.gm.botpets.chatconnector.model.SmsDTO;
import com.gm.botpets.chatconnector.model.User;
import com.gm.botpets.chatconnector.service.IChatService;
import com.gm.botpets.chatconnector.service.IMessageProcessor;
import com.twilio.rest.api.v2010.account.Message;


@Service
public class ChatServiceImpl implements IChatService {

	private Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

	@Autowired
	private Environment env;

	@Autowired
	private IEmailService emailService;

	@Autowired
	private IMessageProcessor messageProcessor;
	
	@SuppressWarnings("null")
	@Override
	public Object processChatRequest(ChatRequest chatRequest) throws ChatConnectorException, IOException {
		logger.info("processChatRequest started():");
		HttpServletResponse response = null;
		ChannelType channelType = null;
		NLPRequest nlpRequest = null;
		Object nlpResponse = null;
		Message message = null;

		if (chatRequest != null) {
			channelType = chatRequest.getChannelType();
		}
		switch (channelType) {

		case SMS:
			break;
		case EMAIL:
			emailService.sendMail(chatRequest.getMessage(), (EmailDTO) chatRequest.getChannelDTO());
			break;
		case SLACK:
			break;

		case WHATSAPP:
			break;
		case CHATBOT:
			break;
		case TWILIOSMS:
			nlpRequest = new NLPRequest();
			nlpRequest.setQuestion(chatRequest.getMessage().toString().trim());
			nlpRequest.setSessionId(chatRequest.getSessionId());
			User user = new User();
			user.setSms(((SmsDTO) chatRequest.getChannelDTO()).getSId());
			nlpRequest.setUser(user);
			try {
				nlpResponse = messageProcessor.processMessageRequest(nlpRequest);
				chatRequest.setMessage(nlpResponse);
				message = messageProcessor.sendSms(chatRequest.getMessage(), (SmsDTO) chatRequest.getChannelDTO());
			} catch (Exception ex) {
				logger.error("Error downloading content", ex);
			}
			break;
		default:

		}
		logger.info("processChatRequest ended():");
		return message;

	}
}
