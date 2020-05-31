package com.gm.botpets.nlp.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BPNLPRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private String question;
	private BPUser user;
	private Channel channel;
	private NLPEngine nlpEngine; // optional
	private String sessionId;
	
}
