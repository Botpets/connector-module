package com.gm.botpets.chatconnector.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BPNLPResponse {

	private String intent;
	private String outputMessage;
	List<String> requestInputs;

}
