package com.gm.botpets.chatconnector.admin.controller;

import static com.gm.botpets.chatconnector.um.constants.ControllerConstants.ADMIN_CONTROLLER_DESCRIPTION;
import static com.gm.botpets.chatconnector.um.constants.ControllerConstants.ADMIN_CONTROLLER_PATH;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gm.botpets.chatconnector.admin.model.ConnectorDTO;
import com.gm.botpets.chatconnector.service.IAdminService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(path = ADMIN_CONTROLLER_PATH)
@Api(value = ADMIN_CONTROLLER_DESCRIPTION)
public class AdminController {

	private Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private IAdminService adminService;

	@ApiOperation(value = "query based on Query params", response = ConnectorDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "save connector details for connector information") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public Object saveDetails(@RequestBody ConnectorDTO connectorDTO) throws Exception {
		logger.info("saveDetails Started() ");

		Object response = null;

		try {
			response = adminService.saveConnectorDetails(connectorDTO);
		} catch (Exception e) {
			logger.info("Error in saveDetails");
		}
		return response;
	}
	
	@ApiOperation(value = "query based on Query params", response = ConnectorDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "update connector details for connector information") })
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
	public Object updateDetails(@RequestBody ConnectorDTO connectorDTO) throws Exception {
		logger.info("updateDetails Started() ");

		Object response = null;

		try {
			response = adminService.updateConnectorDetails(connectorDTO);
		} catch (Exception e) {
			logger.info("Error in updateDetails");
		}
		return response;
	}

	@ApiOperation(value = "query based on Query params", response = String.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "get connector details by Name") })
	@GetMapping(value = "/name/{name}", produces = "application/json")
	public Object getConnDetailsByName(@PathVariable String name) {
		logger.info("getConnDetailsByName Started() ");

		Object response = null;
		try {
			response = adminService.getConnDetailsByName(name);
		} catch (Exception e) {
			logger.info("Error in getConnDetailsByName");
		}
		return response;
	}

	@ApiOperation(value = "query based on Query params", response = Integer.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "get connector details by id") })
	@GetMapping(value = "/id/{id}", produces = "application/json")
	public Object getConnDetailsById(@PathVariable Integer id) {
		logger.info("getConnDetailsById Started() ");

		Object response = null;
		try {
			response = adminService.getConnDetailsById(id);
		} catch (Exception e) {
			logger.info("Error in getConnDetailsById");
		}
		return response;
	}

	@ApiOperation(value = "query based on Query params", response = Integer.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "get all connectors") })
	@GetMapping(value = "/all", produces = "application/json")
	public Object findAll() {
		logger.info("findAll Started() ");

		Object response = null;
		try {
			response = adminService.findAllDetails();
		} catch (Exception e) {
			logger.info("Error in findAll");
		}
		return response;
	}

	@ApiOperation(value = "query based on Query params", response = Integer.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "delete connector details by id") })
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public Object deleteConnDetailsById(@PathVariable Integer id) {
		logger.info("deleteConnDetailsById Started() ");

		Object response = null;
		try {
			response = adminService.deleteConnDetailsById(id);
		} catch (Exception e) {
			logger.info("Error in deleteConnDetailsById");
		}
		return response;
	}

	@GetMapping(value = "/ping")
	public String ping() {
		logger.info("ping Started() ");
		return "I am responding from AdminController";
	}
}
