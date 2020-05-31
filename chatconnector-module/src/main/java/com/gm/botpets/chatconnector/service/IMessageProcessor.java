package com.gm.botpets.chatconnector.service;

import com.gm.botpets.chatconnector.exception.ChatConnectorException;
import com.gm.botpets.chatconnector.model.NLPRequest;
import com.gm.botpets.chatconnector.model.SmsDTO;
import com.twilio.rest.api.v2010.account.Message;

public interface IMessageProcessor {
	public Object processMessageRequest(NLPRequest bpNLPRequest) throws Exception;
	public Message sendSms(Object object, SmsDTO smslDTO) throws ChatConnectorException;
}
