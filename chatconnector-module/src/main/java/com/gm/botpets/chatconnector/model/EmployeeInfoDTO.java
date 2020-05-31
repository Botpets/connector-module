package com.gm.botpets.chatconnector.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeInfoDTO {

	private Integer empId;
	private String empName;	
	private String empAddresses;	
	
}
