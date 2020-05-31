package com.gm.botpets.chatconnector.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.botpets.chatconnector.admin.model.ConnectorDTO;
import com.gm.botpets.chatconnector.admin.model.ConnectorInfo;
import com.gm.botpets.chatconnector.admin.model.ConnectorType;
import com.gm.botpets.chatconnector.admin.model.EmailConntectorDetails;
import com.gm.botpets.chatconnector.admin.model.SlackConnetorDetails;
import com.gm.botpets.chatconnector.admin.model.TwilioSmsConnectorDetails;
import com.gm.botpets.chatconnector.repository.AdminRepository;
import com.gm.botpets.chatconnector.service.IAdminService;

@Service
public class AdminServiceImpl implements IAdminService {

	private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	private Environment env;

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public Object saveConnectorDetails(ConnectorDTO connectorDTO) throws Exception {
		logger.info("saveConnectorDetails started()");
		Map<String, Object> response = new HashMap<String, Object>();
		String message = null;
		int status = 0;
		Object details = adminRepository.getConnDetailsByName(connectorDTO.getConnectorType());
		if (details == null) {
			ObjectMapper mapper = new ObjectMapper();

			String connectorDetails = mapper.writeValueAsString(connectorDTO.getConnectorDetails());
			status = adminRepository.saveConnectorDetails(connectorDTO.getId(), connectorDTO.getConnectorType(),
					connectorDetails, connectorDTO.getIsDefault());
			if (status == 1) {
				message = "success";
				response.put("message", message);
			} else {
				message = "failure";
				response.put("message", message);
			}
		} else {
			response.put("message", "duplicate record");
		}
		logger.info("saveConnectorDetails ended()");
		return response;
	}

	@Override
	public Object getConnDetailsByName(String connectorType) throws Exception {
		logger.info("getConnDetailsByName started()");
		ConnectorInfo connectorInfo = adminRepository.getConnDetailsByName(connectorType);
		logger.info("getConnDetailsByName ended()");
		return connectorInfo;
	}

	@Override
	public Object getConnDetailsById(Integer id) throws Exception {
		logger.info("getConnDetailsById started()");
		ConnectorInfo connectorInfo = adminRepository.getConnDetailsById(id);
		ConnectorDTO connectorDTO = prepareConnectorDTO(connectorInfo);

		logger.info("getConnDetailsById ended()");
		return connectorDTO;
	}

	@Override
	public Object findAllDetails() throws Exception {
		List<ConnectorInfo> allDetails = (List<ConnectorInfo>) adminRepository.findAll();
		List<ConnectorDTO> connectorDTOs = new ArrayList<>();
		if (allDetails != null && !allDetails.isEmpty()) {
			connectorDTOs = new ArrayList<>();
			for (ConnectorInfo connectorInfo : allDetails) {
				ConnectorDTO connectorDTO = prepareConnectorDTO(connectorInfo);
				connectorDTOs.add(connectorDTO);
			}
		}
		return connectorDTOs;
	}

	private ConnectorDTO prepareConnectorDTO(ConnectorInfo connectorInfo)
			throws IOException, JsonParseException, JsonMappingException {
		ConnectorDTO connectorDTO = new ConnectorDTO();
		ObjectMapper mapper = new ObjectMapper();
		connectorDTO.setId(connectorInfo.getId());
		connectorDTO.setConnectorType(connectorInfo.getConnectorType());
		connectorDTO.setIsDefault(connectorInfo.getIsDefault());
		if (connectorInfo.getConnectorType().equals(ConnectorType.GMAIL.toString())) {
			EmailConntectorDetails emailConntectorDetails = mapper.readValue(connectorInfo.getConnectorDetails(),
					EmailConntectorDetails.class);
			connectorDTO.setConnectorDetails(emailConntectorDetails);
		} else if (connectorInfo.getConnectorType().equals(ConnectorType.SLACK.toString())) {
			SlackConnetorDetails slackInfo = mapper.readValue(connectorInfo.getConnectorDetails(),
					SlackConnetorDetails.class);
			connectorDTO.setConnectorDetails(slackInfo);
		} else if (connectorInfo.getConnectorType().equals(ConnectorType.TWILIO_SMS.toString())) {
			TwilioSmsConnectorDetails twilioSmsConnectorDetails = mapper.readValue(connectorInfo.getConnectorDetails(),
					TwilioSmsConnectorDetails.class);
			connectorDTO.setConnectorDetails(twilioSmsConnectorDetails);
		}
		return connectorDTO;
	}

	@Override
	public Object deleteConnDetailsById(Integer id) throws Exception {
		logger.info("deleteConnDetailsById started()");
		Map<String, Object> response = new HashMap<String, Object>();
		ConnectorInfo connectorInfo = adminRepository.getConnDetailsById(id);
		if (connectorInfo != null && connectorInfo.getIsDefault().equalsIgnoreCase("N")) {
			adminRepository.deleteById(id);
			response.put("message", "deleted");
		} else {
			response.put("message", "record not found");
		}
		logger.info("deleteConnDetailsById ended()");
		return response;
	}

	@Override
	public Object updateConnectorDetails(ConnectorDTO connectorDTO) throws Exception {
		logger.info("updateConnectorDetails started()");
		Map<String, Object> response = new HashMap<String, Object>();
		String message = null;
		int status = 0;
		Object details = adminRepository.getConnDetailsById(connectorDTO.getId());
		if (details != null) {
			ObjectMapper mapper = new ObjectMapper();

			String connectorDetails = mapper.writeValueAsString(connectorDTO.getConnectorDetails());
			status = adminRepository.updateConnectorDetails(connectorDTO.getId(), connectorDTO.getConnectorType(),
					connectorDetails, connectorDTO.getIsDefault());
			if (status > 0) {
				message = "success";
				response.put("message", message);
			} else {
				message = "failure";
				response.put("message", message);
			}
		} else {
			response.put("message", "record not found");
		}
		logger.info("updateConnectorDetails ended()");
		return response;
	}

}
