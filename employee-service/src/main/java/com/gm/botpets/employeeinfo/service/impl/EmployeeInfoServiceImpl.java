package com.gm.botpets.employeeinfo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.gm.botpets.employeeinfo.dto.EmployeeContactDTO;
import com.gm.botpets.employeeinfo.dto.EmployeeInfoDTO;
import com.gm.botpets.employeeinfo.dto.RequestTimeInfoDTO;
import com.gm.botpets.employeeinfo.model.EmployeeContact;
import com.gm.botpets.employeeinfo.model.EmployeeInfo;
import com.gm.botpets.employeeinfo.model.LeaveInfo;
import com.gm.botpets.employeeinfo.repository.EmployeeContactRepository;
import com.gm.botpets.employeeinfo.repository.EmployeeInfoRepository;
import com.gm.botpets.employeeinfo.repository.EmployeeLeaveRepository;
import com.gm.botpets.employeeinfo.service.EmployeeInfoService;

@Service
@PropertySource("classpath:/application.properties")
public class EmployeeInfoServiceImpl implements EmployeeInfoService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EmployeeInfoRepository queryRepository;

	@Autowired
	EmployeeContactRepository employeeContactRepository;

	@Autowired
	EmployeeLeaveRepository employeeLeaveRepository;

	@Override
	public Object executeEmployeeInfoById(EmployeeInfoDTO queryDTO) {
		logger.info("executeEmployeeInfoById Started() ");
		EmployeeInfo empQuery = null;
		if (null != queryDTO) {
			empQuery = queryRepository.findEmployeeInfobyId(queryDTO.getEmpId());
		}
		logger.info("executeEmployeeInfoById ended() ");
		return empQuery;

	}

	@Override
	public Object executeEmployeeInfoByName(EmployeeInfoDTO queryDTO) {
		logger.info("executeEmployeeInfoByName Started() ");
		EmployeeInfo empQuery = null;
		if (null != queryDTO) {
			empQuery = queryRepository.findEmployeeInfobyName(queryDTO.getEmpName());
		}

		logger.info("executeEmployeeInfoByName ended() ");
		return empQuery;
	}

	@Override
	public Object updateAdressesforEmployee(EmployeeInfoDTO queryDTO) {
		logger.info("updateAdressesforEmployee Started() ");
		List<Object> resultArray = new ArrayList<Object>();
		int status = 0;
		if (null != queryDTO) {
			status = queryRepository.updateEmployeeAddressByName(queryDTO.getEmpAddresses(), queryDTO.getEmpName());
		}

		if (status == 1) {
			resultArray.add("Address Updated Successfully!");
		}
		logger.info("updateAdressesforEmployee ended() ");

		return resultArray;
	}

	@Override
	public Object requestTimeOffById(RequestTimeInfoDTO leaveQueryDTO) {
		logger.info("requestTimeOffById Started() ");
		EmployeeInfo empQuery = null;
		Object leaveQuery = null;
		int leaveStatus = 0;
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (null != leaveQueryDTO) {
			empQuery = queryRepository.findEmployeeInfobyId(leaveQueryDTO.getEmpId());
			if (empQuery != null) {
				leaveQuery = queryRepository.findLeaveEmployeeInfobyId(leaveQueryDTO.getEmpId());
				if (leaveQuery == null) {
					leaveStatus = queryRepository.saveTimeOffRequestById(leaveQueryDTO.getEmpId(),
							empQuery.getEmpName(), leaveQueryDTO.getTimeoffStartTime(),
							leaveQueryDTO.getTimeoffEndTime(), leaveQueryDTO.getLeaveReason());
					if (leaveStatus == 1) {
						result.put("statusvalue", leaveStatus);
						result.put("statusFlag", "T");
						result.put("status", "Success");
					} else {
						result.put("statusvalue", leaveStatus);
						result.put("statusFlag", "F");
						result.put("status", "Failed");
					}
				} else {
					result.put("message", "Leave Application Already Exists");
					result.put("status", "Failed");
					result.put("statusvalue", leaveStatus);
					result.put("statusFlag", "F");
				}
			} else {
				result.put("status", "Failed");
				result.put("statusvalue", leaveStatus);
				result.put("statusFlag", "F");
				result.put("message", "Employee doesnot exists");
			}

		}

		logger.info("requestTimeOffById ended() ");
		return result;
	}

	@Override
	public LeaveInfo getTimeoffStatus(RequestTimeInfoDTO requestTimeInfoDTO) {
		logger.info("getTimeoffStatus Started() ");
		LeaveInfo leaveInfo = employeeLeaveRepository.findEmployeeLeaveStatus(requestTimeInfoDTO.getEmpId(),
				requestTimeInfoDTO.getTimeoffStartTime(), requestTimeInfoDTO.getTimeoffEndTime());
		return leaveInfo;
	}

	@Override
	public Object getEmployeeInfoByEmailOrPhoneNo(EmployeeContactDTO contactDTO) {
		logger.debug("getEmployeeInfoByEmailOrPhoneNo started()");
		EmployeeContact employeeContact = null;
		if (contactDTO != null) {
			employeeContact = employeeContactRepository.findEmployeeInfoByEmailOrPhoneNo(contactDTO.getEmployeeEmail(),
					contactDTO.getEmployeePhoneNo());
		}
		logger.debug("getEmployeeInfoByEmailOrPhoneNo ended()");
		return employeeContact;
	}
}
