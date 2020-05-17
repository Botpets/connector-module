package com.gm.botpets.chatconnector.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String apiVersion;
	private String smsSid;
	private String smsStatus;
	private String smsMessageSid;
	private String numSegments;
	private String from;
	private String to;
	private String messageSid;
	private String body;
	private String accountSid;
	private String numMedia;
}
