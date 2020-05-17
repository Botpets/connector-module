package com.gm.botpets.chatconnector.sendmail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gm.botpets.chatconnector.admin.model.EmailConntectorDetails;
import com.gm.botpets.chatconnector.admin.model.SmtpInfo;
import com.gm.botpets.chatconnector.commons.ConnectorConfig;

@Component
public class SendMailTLS {
	@Autowired
	private ConnectorConfig connectorConfig;

	public void sendMail(Object response, String sender) {
		EmailConntectorDetails connectorDetails = ConnectorConfig.getEmailConntectorDetails();
		SmtpInfo smtpInfo = connectorDetails.getSmtpInfo();
		
		final String username = connectorDetails.getUsername();
		final String password = connectorDetails.getPassword();

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtpInfo.getHost());
		props.put("mail.smtp.port", smtpInfo.getPort());

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sender));
			message.setSubject("Question Response");
			message.setText("Question Response," + response.toString());

			Transport.send(message);


		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
