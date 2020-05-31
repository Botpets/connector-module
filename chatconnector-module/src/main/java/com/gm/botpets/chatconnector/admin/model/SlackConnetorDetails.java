package com.gm.botpets.chatconnector.admin.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SlackConnetorDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	private String slackBotToken;
	private String slackApi;
	private String slackIncomingWebhookUrl;
}
