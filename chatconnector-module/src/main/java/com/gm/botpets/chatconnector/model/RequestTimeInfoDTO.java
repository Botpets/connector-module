package com.gm.botpets.chatconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestTimeInfoDTO {
	private Integer empId;
	private String empName;
	private String timeoffStartTime;
	private String timeoffEndTime;
	private String leaveReason;

}
