package com.gm.botpets.employeeinfo.controller;

import static com.gm.botpets.employeeinfo.constants.EmployeeInfoServiceConstants.EMPLOYEE_INFO_CONTROLLER_DESCRIPTION;
import static com.gm.botpets.employeeinfo.constants.EmployeeInfoServiceConstants.EMPLOYEE_INFO_CONTROLLER_PATH;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gm.botpets.employeeinfo.dto.EmployeeContactDTO;
import com.gm.botpets.employeeinfo.dto.EmployeeInfoDTO;
import com.gm.botpets.employeeinfo.dto.RequestTimeInfoDTO;
import com.gm.botpets.employeeinfo.service.EmployeeInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(EMPLOYEE_INFO_CONTROLLER_PATH)
@Api(value = EMPLOYEE_INFO_CONTROLLER_DESCRIPTION)
public class EmployeeInfoController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EmployeeInfoService employeeInfoService;

	@ApiOperation(value = "Execute query based on query params", response = EmployeeInfoDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Query Employee Information By Id") })
	@RequestMapping(value = "/executeEmployeeInfoById", method = RequestMethod.POST, produces = "application/json")
	public Object executeEmployeeInfoById(@RequestBody EmployeeInfoDTO queryDTO) {
		logger.info("executeEmployeeInfoById Started() ");
		return employeeInfoService.executeEmployeeInfoById(queryDTO);
	}

	@ApiOperation(value = "Execute query based on query params", response = EmployeeInfoDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Query EmployeeInfo By Name") })
	@RequestMapping(value = "/executeEmployeeInfoByName", method = RequestMethod.POST, produces = "application/json")
	public Object executeEmployeeInfoByName(@RequestBody EmployeeInfoDTO queryDTO) {
		logger.info("executeEmployeeInfonByName Started() ");
		return employeeInfoService.executeEmployeeInfoByName(queryDTO);
	}

	@ApiOperation(value = "Update query based on query params", response = EmployeeInfoDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Update Addresses of Employee by name") })
	@RequestMapping(value = "/updateEmployeeAddressByName", method = RequestMethod.POST, produces = "application/json")
	public Object updateEmployeeAddressByName(@RequestBody EmployeeInfoDTO queryDTO) {
		logger.info("updateEmployeeAddressByName Started() ");
		return employeeInfoService.updateAdressesforEmployee(queryDTO);
	}

	@ApiOperation(value = "Insert query based on query params", response = RequestTimeInfoDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "request Employee Time Off request by Id") })
	@RequestMapping(value = "/requestEmployeeTimeOffById", method = RequestMethod.POST, produces = "application/json")
	public Object executeRequestTimeOffById(@RequestBody RequestTimeInfoDTO requestTimeOffDTO) {
		logger.info("executeRequestTimeOffById Started() ");
		return employeeInfoService.requestTimeOffById(requestTimeOffDTO);
	}
	
	@ApiOperation(value = "Get Employee  timeoff staus", response = RequestTimeInfoDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Get Employee  timeoff staus") })
	@RequestMapping(value = "/getTimeOffStatus", method = RequestMethod.POST, produces = "application/json")
	public Object getRequestTimeOffStatus(@RequestBody RequestTimeInfoDTO requestTimeOffDTO) {
		logger.info("getRequestTimeOffStatus Started() ");
		return employeeInfoService.getTimeoffStatus(requestTimeOffDTO);
	}

	@ApiOperation(value = "Execute query based on query params", response = EmployeeContactDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "get Employee name ,Employee id requesting with employee email or employee phone number") })
	@RequestMapping(value = "/getEmployeeInfoByEmailOrPhoneNo", method = RequestMethod.POST, produces = "application/json")
	public Object getEmployeeInfoByEmailOrPhoneNo(@RequestBody EmployeeContactDTO contactDTO) {
		logger.info("getEmployeeInfoByEmailOrPhoneNo Started() ");
		return employeeInfoService.getEmployeeInfoByEmailOrPhoneNo(contactDTO);
	}
	
	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public String ping() {
		return "I am OK";
	}
}
