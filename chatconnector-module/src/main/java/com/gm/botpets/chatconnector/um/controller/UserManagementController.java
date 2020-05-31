package com.gm.botpets.chatconnector.um.controller;

import static com.gm.botpets.chatconnector.um.constants.ControllerConstants.USER_CONTROLLER_DESCRIPTION;
import static com.gm.botpets.chatconnector.um.constants.ControllerConstants.USER_CONTROLLER_PATH;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gm.botpets.chatconnector.um.dto.LoginDTO;
import com.gm.botpets.chatconnector.um.dto.UserDTO;
import com.gm.botpets.chatconnector.um.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(path = USER_CONTROLLER_PATH)
@Api(value = USER_CONTROLLER_DESCRIPTION)
public class UserManagementController {

	private Logger logger = LoggerFactory.getLogger(UserManagementController.class);

	@Autowired
	private UserService userService;

	@ApiOperation(value = "query based on Query params", response = UserDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Submit information to register") })
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST, produces = "application/json")
	public Object registerUser(@RequestBody UserDTO queryDTO) {
		logger.info("registerUser Started() ");
		return userService.saveUserDetails(queryDTO);
	}
	
	@ApiOperation(value = "query based on Query params", response = UserDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Submit information to register") })
	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST, produces = "application/json")
	public Object updateProfile(@RequestBody UserDTO queryDTO) {
		logger.info("updateProfile Started() ");
		return userService.saveProfileDetails(queryDTO);
	}

	@ApiOperation(value = "query based on Query params", response = LoginDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "login information to login to the service") })
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public Object loginUser(@RequestBody LoginDTO loginDTO) {
		logger.info("loginUser Started() ");
		return userService.loginAndValidateUser(loginDTO);
	}

	@GetMapping(value = "/ping")
	public String ping() {
		logger.info("ping Started() ");
		return "I am responding from User management controller";
	}
}
