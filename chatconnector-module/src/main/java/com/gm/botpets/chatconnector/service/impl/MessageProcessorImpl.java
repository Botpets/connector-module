package com.gm.botpets.chatconnector.service.impl;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.botpets.chatconnector.exception.ChatConnectorException;
import com.gm.botpets.chatconnector.manger.TargetServiceManager;
import com.gm.botpets.chatconnector.model.BPNLPResponse;
import com.gm.botpets.chatconnector.model.NLPRequest;
import com.gm.botpets.chatconnector.model.SmsDTO;
import com.gm.botpets.chatconnector.model.User;
import com.gm.botpets.chatconnector.service.IMessageProcessor;
import com.gm.botpets.chatconnector.service.ITargetService;
import com.gm.botpets.nlp.model.BPNLPRequest;
import com.gm.botpets.nlp.model.BPUser;
import com.gm.botpets.nlp.service.IBPNLPService;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class MessageProcessorImpl implements IMessageProcessor {

	private Logger logger = LoggerFactory.getLogger(MessageProcessorImpl.class);

	private static final String POST_METHOD = "POST";
	private static final String APPLICATION_JSON = "application/json";
	public static final String ACCOUNT_SID = "ACCOUNT.SID";
	public static final String AUTH_TOKEN = "AUTH.TOKEN";
	public static final String TO_PHONE_NUMBER = "TO.PHONE.NUMBER";
	public static final String FROM_PHONE_NUMBER = "FROM.PHONE.NUMBER";

	@Autowired
	private Environment env;

	@Autowired
	@Qualifier("employeeTargetService")
	private ITargetService employeeTargetService;

	@Autowired
	private TargetServiceManager targetServiceManager;

	@Autowired
	private IBPNLPService bpNLPService;

	@Override
	public Object processMessageRequest(NLPRequest bpNLPRequest) throws Exception {
		String sourceLanguage = null;
		if (bpNLPRequest.getUser() != null) {
			sourceLanguage = bpNLPRequest.getUser().getPreferredLanguage();
		}
		if (sourceLanguage == null) {
			sourceLanguage = bpNLPService.detectLanguage(bpNLPRequest.getQuestion().trim());
		}
		//If nothing found Set default language as "en"
		if (sourceLanguage == null) {
			sourceLanguage = "en";
		}

		bpNLPRequest.getUser().setPreferredLanguage(sourceLanguage);

		BPNLPResponse bpNLPResponse = processRequestWithNLPEngine(bpNLPRequest);

		String targetLanguage = "en";
		boolean isTranslationRequired = translationRequired(sourceLanguage, targetLanguage);
		String response = null;
		if (bpNLPResponse != null) {
			targetServiceManager.setTargetService(employeeTargetService);
			if (isTranslationRequired) {
				response = bpNLPService.translateMessage(
						targetServiceManager.processIntent(bpNLPResponse, bpNLPRequest.getUser()),
						targetLanguage, sourceLanguage);
			} else {
				response = targetServiceManager.processIntent(bpNLPResponse, bpNLPRequest.getUser());
			}

		} else {
			if (response == null) {
				if (isTranslationRequired) {
					response = bpNLPService.translateMessage("Please rephrase you question.", targetLanguage,
							sourceLanguage);
				} else {
					response = "Please rephrase you question.";
				}

			}
		}

		return response;
	}

	private boolean translationRequired(String source, String target) {
		boolean flag = false;
		if (!source.equalsIgnoreCase(target)) {
			flag = true;
		}
		return flag;
	}

	private BPNLPResponse processRequestWithNLPEngine(NLPRequest nlpRequest) {

		BPNLPRequest bpNLPRequest = new BPNLPRequest();
		bpNLPRequest.setQuestion(nlpRequest.getQuestion());
		bpNLPRequest.setSessionId(nlpRequest.getSessionId());
		User user = nlpRequest.getUser();

		BPUser bpUser = new BPUser();
		bpUser.setEmail(user.getEmail());
		bpUser.setPhoneNo(user.getPhoneNo());
		bpUser.setPreferredLanguage(user.getPreferredLanguage());

		bpNLPRequest.setUser(bpUser);

		Object nlpResponse = null;
		BPNLPResponse response = null;
		try {
			nlpResponse = bpNLPService.processBPNLPRequest(bpNLPRequest);
			ObjectMapper mapper = new ObjectMapper();
			response = (BPNLPResponse) mapper.convertValue(nlpResponse, BPNLPResponse.class);
		} catch (Exception e) {
			logger.info("processRequestWithNLPEngine:error in processing nlp request");
		}

		return response;
	}

	@Override
	public Message sendSms(Object nlpResponse, SmsDTO smslDTO) throws ChatConnectorException {
		logger.info("sendSms started()");
		String accountSid = env.getProperty(ACCOUNT_SID);
		String authToken = env.getProperty(AUTH_TOKEN);
		Message message = null;

		try {
			Twilio.init(accountSid, authToken);
			message = Message.creator(
	                new com.twilio.type.PhoneNumber(smslDTO.getFromnumber().trim()),
	                new com.twilio.type.PhoneNumber(smslDTO.getTonumber().trim()),
	                nlpResponse.toString().trim())
	            .setPersistentAction(
	                Arrays.asList(""))
	            .create();

	        System.out.println(message.getSid());
			logger.info("sendSms ended()");
		} catch (ApiException e) {
			if (e.getCode() == 21614) {
				System.out.println("Uh oh, looks like this caller can't receive SMS messages.");
			}
		}
		return message;

	}
}
