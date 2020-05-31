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
@Table(name = "emp_contact")
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class EmployeeContact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_id")
	private Integer empId;

	@Column(name = "emp_name")
	private String empName;
	
	@Column(name = "emp_email")
	private String empEmail;
	
	@Column(name = "emp_phoneno")
	private String empPhoneNo;
	
	@Column(name = "emp_addresses")
	private String empAddresses;
	
}
