package com.gm.botpets.nlp.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BPNLPResponse {

	private String intent;
	List<String> requestInputs;	
	private String outputMessage;

}
