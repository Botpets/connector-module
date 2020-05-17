package com.gm.botpets.chatconnector.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class NLPRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String question;
	private User user;
	private Channel channel;
	private NLPEngine nlpEngine; // optional
	private String sessionId;
}
