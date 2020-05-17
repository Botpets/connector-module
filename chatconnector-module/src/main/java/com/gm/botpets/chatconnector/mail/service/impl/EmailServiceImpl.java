package com.gm.botpets.chatconnector.mail.service.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.gm.botpets.chatconnector.exception.ChatConnectorException;
import com.gm.botpets.chatconnector.mail.service.IEmailService;
import com.gm.botpets.chatconnector.model.EmailDTO;

@Component
public class EmailServiceImpl implements IEmailService {
	private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private Environment env;

	@Override
	public void sendMail(Object response, EmailDTO emailDTO) throws ChatConnectorException {
		String serverUsername = env.getProperty("email.server.username");
		String serverPassword = env.getProperty("email.server.password");

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(serverUsername, serverPassword);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(serverUsername));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDTO.getRecipient()));
			message.setSubject(emailDTO.getSubject());
			message.setText(response.toString());
			message.setHeader("In-Reply-To", emailDTO.getMessageId());
			Transport.send(message);

			logger.info("Sent Email response");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

}
