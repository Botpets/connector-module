package com.gm.botpets.chatconnector.manger;

import org.springframework.stereotype.Component;

import com.gm.botpets.chatconnector.exception.ChatConnectorException;
import com.gm.botpets.chatconnector.model.BPNLPResponse;
import com.gm.botpets.chatconnector.model.User;
import com.gm.botpets.chatconnector.service.ITargetService;

@Component
public class TargetServiceManager {

	private ITargetService targetService;

	public void setTargetService(ITargetService targetService) {
		this.targetService = targetService;
	}

	public String processIntent(BPNLPResponse bpNLPResponse, User user) throws ChatConnectorException {
		return targetService.processIntent(bpNLPResponse, user);
	}
}
