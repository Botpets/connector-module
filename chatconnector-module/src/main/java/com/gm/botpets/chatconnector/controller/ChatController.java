package com.gm.botpets.chatconnector.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gm.botpets.chatconnector.model.ChannelType;
import com.gm.botpets.chatconnector.model.ChatRequest;
import com.gm.botpets.chatconnector.model.SmsDTO;
import com.gm.botpets.chatconnector.model.SmsRequest;
import com.gm.botpets.chatconnector.service.IChatService;
import com.twilio.rest.api.v2010.account.Message;

@RestController
@RequestMapping(path = "/chatprocessor")
public class ChatController {

	@Autowired
	private IChatService chatService;
	
	private Logger logger = LoggerFactory.getLogger(ChatController.class);

	
	@PostMapping(value = "/",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public Message  processSmsUserResponse(@Valid SmsRequest smsRequest) {
		logger.info("processSmsUserResponse started()");
		Message message = null;
		ChatRequest chatRequest = null;
	if(smsRequest.getMessageSid()!=null) {
		chatRequest = new ChatRequest();
		chatRequest.setChannelType(ChannelType.TWILIOSMS);
		chatRequest.setMessage(smsRequest.getBody());
		chatRequest.setSessionId(smsRequest.getMessageSid());
		
		SmsDTO smsDTO = new SmsDTO();
		smsDTO.setFromnumber(smsRequest.getFrom());
		smsDTO.setTonumber(smsRequest.getTo());
		smsDTO.setMessage(smsRequest.getBody());
		smsDTO.setSId(smsRequest.getAccountSid());
		chatRequest.setChannelDTO(smsDTO);
	}
		try {
			message =  (Message) chatService.processChatRequest(chatRequest);
		} catch (Exception e) {
			logger.info("Error in processing chat request:"+chatRequest.getChannelType());
		}
		logger.info("processSmsUserResponse ended()");
		return message;
	}

	@PostMapping(value = "/request",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public Object  processUserResponse(@Valid Object object) {
		logger.info("processUserResponse started()");
		logger.info("processUserResponse ended()");
		return "";
	}
	@GetMapping(value = "/ping")
	public String Hello() {
		return "Hello!";
	}
}
