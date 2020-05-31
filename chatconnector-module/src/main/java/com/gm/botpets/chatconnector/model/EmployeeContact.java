package com.gm.botpets.chatconnector.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeContact implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer empId;
	private String empName;
	private String empEmail;
	private String empPhoneNo;
	private String empAddresses;

}
