package com.gm.botpets.chatconnector.um.service.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.botpets.chatconnector.um.dto.LoginDTO;
import com.gm.botpets.chatconnector.um.dto.UserDTO;
import com.gm.botpets.chatconnector.um.model.User;
import com.gm.botpets.chatconnector.um.repository.UserRepository;
import com.gm.botpets.chatconnector.um.service.UserService;

@Service
//@PropertySource("classpath:/application.properties")
public class UserServiceImpl implements UserService {

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepository userRepository;

	@Override
	public Object saveUserDetails(UserDTO queryDTO) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		String userPassword = userRepository.findPassword(queryDTO.getEmail());
		
		if (userPassword == null) {
			Integer status = userRepository.saveUserDetails(queryDTO.getId(), queryDTO.getFirstName(),
					queryDTO.getLastName(), queryDTO.getEmail(), queryDTO.getPassword());
			User user = userRepository.getUserDetails(queryDTO.getEmail());
			result.put("status", status);
			result.put("userDetails", user);
		} else {
			result.put("status", 0);
		}
		return result;
	}

	@Override
	public Object loginAndValidateUser(LoginDTO loginDTO) {
		logger.info("Entered validate user");
		HashMap<String, Object> result = new HashMap<String, Object>();
		User user = userRepository.getUserDetails(loginDTO.getUsername());
		String userPassword = userRepository.findPassword(loginDTO.getUsername());
		if (userPassword != null) {
			if (userPassword.equalsIgnoreCase(loginDTO.getPassword())) {
				logger.info("Valid user");
				result.put("response", "Login Successful");
				result.put("userDetails", user);
			} else {
				logger.info("Invalid user");
				result.put("response", "Login Failed");
			}

		} else {
			result.put("response", "User Doesnot exists");
		}
		logger.info("Exiting validation");
		return result;
	}

	@Override
	public Object saveProfileDetails(UserDTO queryDTO) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Integer status = null;
		if(queryDTO!=null) {
			status = userRepository.updateUserDetails(queryDTO.getId(), queryDTO.getFirstName(),
					queryDTO.getLastName(), queryDTO.getPhoneNo());
		}
		if(status>0) {
			result.put("status", "success");
			}else {
				result.put("status", "failure");
			}
		
		return result;
	}
}
