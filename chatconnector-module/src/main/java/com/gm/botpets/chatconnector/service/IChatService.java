package com.gm.botpets.chatconnector.service;

import java.io.IOException;

import com.gm.botpets.chatconnector.exception.ChatConnectorException;
import com.gm.botpets.chatconnector.model.ChatRequest;
import com.twilio.rest.api.v2010.account.Message;

public interface IChatService {

	Object processChatRequest(ChatRequest chatRequest) throws ChatConnectorException, IOException;

}
