package com.gm.botpets.employeeinfo.service;

import com.gm.botpets.employeeinfo.dto.EmployeeContactDTO;
import com.gm.botpets.employeeinfo.dto.EmployeeInfoDTO;
import com.gm.botpets.employeeinfo.dto.RequestTimeInfoDTO;
import com.gm.botpets.employeeinfo.model.LeaveInfo;

public interface EmployeeInfoService {
	public  Object  executeEmployeeInfoById(EmployeeInfoDTO queryDTO);
	public  Object  updateAdressesforEmployee(EmployeeInfoDTO queryDTO);
	public Object executeEmployeeInfoByName(EmployeeInfoDTO queryDTO);
	public Object requestTimeOffById(RequestTimeInfoDTO queryDTO);
	public Object getEmployeeInfoByEmailOrPhoneNo(EmployeeContactDTO contactDTO);
	public LeaveInfo getTimeoffStatus(RequestTimeInfoDTO requestTimeInfoDTO);
	
}
