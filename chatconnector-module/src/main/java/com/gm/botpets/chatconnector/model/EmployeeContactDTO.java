package com.gm.botpets.chatconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown=true)
public class EmployeeContactDTO {
	private String employeeEmail;
	private String employeePhoneNo;

}
