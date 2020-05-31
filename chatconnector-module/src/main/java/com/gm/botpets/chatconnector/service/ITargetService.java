package com.gm.botpets.chatconnector.service;

import com.gm.botpets.chatconnector.exception.ChatConnectorException;
import com.gm.botpets.chatconnector.model.BPNLPResponse;
import com.gm.botpets.chatconnector.model.User;

public interface ITargetService {

	public String processIntent(BPNLPResponse bpNLPResponse, User user) throws ChatConnectorException;
}
