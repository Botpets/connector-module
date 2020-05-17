package com.gm.botpets.chatconnector.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer empId;
 
	private String empName;
	
	private String empAddresses;

	private String empCostCenter;
	
	private String empTotalCostCenter;
	
	private String empRsu;
	
	private String empBenefit;
	
	private String empPaycheck;
	
	private String empTotalYtd;
	
	private String empManagerFeedback;
	
	private String empVisaExpiryData;
	
	private String empPeerFeedback;
	
	private String empJobcode;
	 
	private String empCompRange;
	
	private String empBasePay;
	
	private String empOverTime;
	
	private String empBonusPay;
	
	private String empTotalCompensation;
}
