package com.gm.botpets.chatconnector.admin.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class SmtpInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String host;
	private String port;
}
