package com.gm.botpets.chatconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class LeaveInfo {

	private Integer leaveId;

	private Integer empId;

	private String empName;

	private String timeoffStartTime;

	private String timeoffEndTime;

	private String leaveReason;

	private String leaveStatus;

}
