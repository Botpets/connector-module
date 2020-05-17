package com.gm.botpets.employeeinfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employee")
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class EmployeeInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_id")
	private Integer empId;
 
	@Column(name = "emp_name")
	private String empName;
	
	@Column(name = "emp_addresses")
	private String empAddresses;

	@Column(name = "emp_cost_center")
	private String empCostCenter;
	
	@Column(name = "emp_total_cost_center")
	private String empTotalCostCenter;
	
	@Column(name = "emp_rsu")
	private String empRsu;
	
	@Column(name = "emp_benefit")
	private String empBenefit;
	
	@Column(name = "emp_paycheck")
	private String empPaycheck;
	
	@Column(name = "emp_total_ytd")
	private String empTotalYtd;
	
	@Column(name = "emp_manager_feedback")
	private String empManagerFeedback;
	
	@Column(name = "emp_visa_expiry_data")
	private String empVisaExpiryData;
	
	@Column(name = "emp_peer_feedback")
	private String empPeerFeedback;
	
	@Column(name = "emp_jobcode")
	private String empJobcode;
	 
	@Column(name = "emp_comp_range")
	private String empCompRange;
	
	@Column(name = "emp_base_pay")
	private String empBasePay;
	
	@Column(name = "emp_over_time")
	private String empOverTime;
	
	@Column(name = "emp_bonus_pay")
	private String empBonusPay;
	
	@Column(name = "emp_total_compensation")
	private String empTotalCompensation;
}
