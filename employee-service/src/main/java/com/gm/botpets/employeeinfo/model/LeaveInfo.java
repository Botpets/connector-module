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
@Table(name = "emp_leave")
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class LeaveInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "leave_id")
	private Integer leaveId;
	
	@Column(name = "emp_id")
	private Integer empId;
 
	@Column(name = "emp_name")
	private String empName;
	
	@Column(name = "timeoff_starttime")
	private String timeoffStartTime;
	
	@Column(name = "timeoff_endtime")
	private String timeoffEndTime;
	
	@Column(name = "leave_reason")
	private String leaveReason;
	
	@Column(name = "leave_status")
	private String leaveStatus;
	
}
