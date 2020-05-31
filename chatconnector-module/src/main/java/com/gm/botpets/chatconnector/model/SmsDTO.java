package com.gm.botpets.chatconnector.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsDTO {

	private String tonumber;
	private String fromnumber;
	private String message;
	private String sId;
}
