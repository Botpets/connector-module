package com.gm.botpets.chatconnector.mail.service;

import com.gm.botpets.chatconnector.exception.ChatConnectorException;
import com.gm.botpets.chatconnector.model.EmailDTO;

public interface IEmailService {
	public void sendMail(Object response, EmailDTO emailDTO) throws ChatConnectorException;
}
