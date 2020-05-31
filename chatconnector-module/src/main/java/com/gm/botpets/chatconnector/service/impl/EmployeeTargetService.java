package com.gm.botpets.chatconnector.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.botpets.chatconnector.exception.ChatConnectorException;
import com.gm.botpets.chatconnector.model.BPNLPResponse;
import com.gm.botpets.chatconnector.model.EmployeeContact;
import com.gm.botpets.chatconnector.model.EmployeeContactDTO;
import com.gm.botpets.chatconnector.model.EmployeeInfo;
import com.gm.botpets.chatconnector.model.EmployeeInfoDTO;
import com.gm.botpets.chatconnector.model.LeaveInfo;
import com.gm.botpets.chatconnector.model.RequestTimeInfoDTO;
import com.gm.botpets.chatconnector.model.User;
import com.gm.botpets.chatconnector.service.ITargetService;

@Component
@Qualifier("employeeTargetService")
public class EmployeeTargetService implements ITargetService {

	public static final String EMPLOYEE_INFO_SERVICE_URL = "EMPLOYEE.INFO.SERVICE.URL";
	public static final String GET_EMPLOYEE_NAME_URL = "GET.EMPLOYEE.NAME.URL";
	public static final String GET_EMPLOYEE_TIMEOFF_STATUS = "GET.EMPLOYEE.TIMEOFF.STATUS";

	@Autowired
	private Environment env;

	@Autowired
	@Lazy
	private RestTemplate restTemplate;

	@Override
	public String processIntent(BPNLPResponse bpNLPResponse, User user) throws ChatConnectorException {
		EmployeeContact employeeContact = getEmployeeContactDetails(user);

		EmployeeInfo employeeInfo = null;
		String intent = bpNLPResponse.getIntent();
		String output = bpNLPResponse.getOutputMessage();
		List<String> inputs = bpNLPResponse.getRequestInputs();

		String response = null;
		if (employeeContact == null) {
			response = "User details does not exist";
		} else {
			employeeInfo = getEmployeeInfo(employeeContact.getEmpId());
			if (employeeInfo != null && intent != null) {
				switch (intent) {
				case "Cost_Center":
					response = "Your cost center is $" + employeeInfo.getEmpCostCenter();
					break;
				case "RSU":
					response = "As of today, your RSU is " + employeeInfo.getEmpRsu();
					break;
				case "YTD":
					response = "Your YTD is $" + employeeInfo.getEmpTotalYtd();
					break;
				case "Request_for_timeoff":
					if (inputs != null) {
						response = "Your leave status: " + getRequestTimeOffById(inputs, employeeContact);
					} else {
						response = "I couldn't answer your question. Try rephrasing.";
					}
					break;
				case "Benefits":
					response = "As per the records your Benefits are " + employeeInfo.getEmpBenefit();
					break;
				case "Paycheck":
					response = "Your paycheck for the current month is $" + employeeInfo.getEmpPaycheck();
					break;
				case "Peer_Feedback":
					response = "The feedback obtained from your peer is " + employeeInfo.getEmpPeerFeedback();
					break;
				case "Manager_Feedback":
					response = "The feedback obtained from your Manager is " + employeeInfo.getEmpManagerFeedback();
					break;
				case "Home_Address":
					response = "Your House Address is " + employeeInfo.getEmpAddresses();
					break;
				case "Visa_Expires":
					response = "Your Visa expires on " + employeeInfo.getEmpVisaExpiryData();
					break;
				case "Change_Address":
					response = "You have changed your address to "  + employeeInfo.getEmpAddresses();
					break;
				case "job_code":
					response = employeeInfo.getEmpJobcode() + " is your Job code";
					break;
				case "Compensation_range":
					response = "Your Compensation Range is in between ($)" + employeeInfo.getEmpCompRange();
					break;
				default:
					response = "I couldn't answer your question. Try rephrasing.";
					break;
				}
			} else {
				response = output;
			}
		}
		return response;
	}

	private String getRequestTimeOffById(List<String> inputs, EmployeeContact employeeContact) {
		RequestTimeInfoDTO requestTimeInfoDTO = new RequestTimeInfoDTO();
		requestTimeInfoDTO.setEmpId(employeeContact.getEmpId());
		requestTimeInfoDTO.setTimeoffStartTime(inputs.get(0));
		requestTimeInfoDTO.setTimeoffEndTime(inputs.get(1));

		LeaveInfo leaveInfo = restTemplate.postForObject(env.getProperty(GET_EMPLOYEE_TIMEOFF_STATUS),
				requestTimeInfoDTO, LeaveInfo.class);

		return leaveInfo.getLeaveStatus();

	}

	private EmployeeContact getEmployeeContactDetails(User user) {
		EmployeeContactDTO employeeContactDTO = null;
		String input = "";
		EmployeeContact employeeContact = null;
		try {

			employeeContactDTO = new EmployeeContactDTO();
			employeeContactDTO.setEmployeeEmail(user.getEmail());
			employeeContactDTO.setEmployeePhoneNo(user.getPhoneNo());
			ObjectMapper objectMapper = new ObjectMapper();
			input = objectMapper.writeValueAsString(employeeContactDTO);

			URL url = new URL(env.getProperty(GET_EMPLOYEE_NAME_URL));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			ObjectMapper mapper = new ObjectMapper();
			employeeContact = mapper.readValue(conn.getInputStream(), EmployeeContact.class);

			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
		return employeeContact;

	}

	private EmployeeInfo getEmployeeInfo(Integer empId) {
		EmployeeInfoDTO employeeInfoDTO = null;
		String input = "";
		EmployeeInfo employeeInfo = null;
		try {

			employeeInfoDTO = new EmployeeInfoDTO();
			employeeInfoDTO.setEmpId(empId);
			ObjectMapper objectMapper = new ObjectMapper();
			input = objectMapper.writeValueAsString(employeeInfoDTO);

			URL url = new URL(env.getProperty(EMPLOYEE_INFO_SERVICE_URL));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			ObjectMapper mapper = new ObjectMapper();
			employeeInfo = mapper.readValue(conn.getInputStream(), EmployeeInfo.class);

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return employeeInfo;

	}

}
