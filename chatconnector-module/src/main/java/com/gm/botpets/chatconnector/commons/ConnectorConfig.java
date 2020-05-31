package com.gm.botpets.chatconnector.commons;

import javax.annotation.PostConstruct;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.botpets.chatconnector.admin.model.ConnectorInfo;
import com.gm.botpets.chatconnector.admin.model.ConnectorType;
import com.gm.botpets.chatconnector.admin.model.EmailConntectorDetails;
import com.gm.botpets.chatconnector.admin.model.ImapInfo;
import com.gm.botpets.chatconnector.admin.model.SlackConnetorDetails;
import com.gm.botpets.chatconnector.admin.model.SmtpInfo;
import com.gm.botpets.chatconnector.service.IAdminService;

@Component
public class ConnectorConfig {

	private static EmailConntectorDetails emailConntectorDetails = null;
	private static SlackConnetorDetails slackConnectorDetails = null;

	@Autowired
	private IAdminService adminService;

	public static EmailConntectorDetails getEmailConntectorDetails() {
		return emailConntectorDetails;
	}

	public static void setEmailConntectorDetails(EmailConntectorDetails emailConntectorDetails) {
		ConnectorConfig.emailConntectorDetails = emailConntectorDetails;
	}

	@PostConstruct
	public void init() {
		loadEmail();
		loadSlackConfigurations();

	}

	private void loadSlackConfigurations() {
		ConnectorInfo connectorInfo = null;
		try {
			connectorInfo = (ConnectorInfo) adminService.getConnDetailsByName(ConnectorType.SLACK.toString());
			if (connectorInfo != null && connectorInfo.getIsDefault() != null
					&& connectorInfo.getIsDefault().equals("Y")) {
				String slackDetails = connectorInfo.getConnectorDetails();
				if (slackDetails != null) {
					ObjectMapper mapper = new ObjectMapper();
					slackConnectorDetails = mapper.readValue(slackDetails, SlackConnetorDetails.class);
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	private void loadEmail() {
		ConnectorInfo connectorInfo = null;
		try {
			connectorInfo = (ConnectorInfo) adminService.getConnDetailsByName(ConnectorType.GMAIL.toString());
			if (connectorInfo != null && connectorInfo.getIsDefault() != null
					&& connectorInfo.getIsDefault().equals("Y")) {
				String emailDetails = connectorInfo.getConnectorDetails();
				if (emailDetails != null) {
					ObjectMapper mapper = new ObjectMapper();
					EmailConntectorDetails connectorDetails = mapper.readValue(emailDetails,
							EmailConntectorDetails.class);
					if (emailConntectorDetails == null) {
						emailConntectorDetails = new EmailConntectorDetails();
					}
					ConnectorConfig.setEmailConntectorDetails(connectorDetails);
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static SlackConnetorDetails getSlackConnectorDetails() {
		return slackConnectorDetails;
	}

	public static void setSlackConnectorDetails(SlackConnetorDetails slackConnectorDetails) {
		ConnectorConfig.slackConnectorDetails = slackConnectorDetails;
	}

}
