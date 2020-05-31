package com.gm.botpets.chatconnector.service;

import com.gm.botpets.chatconnector.admin.model.ConnectorDTO;

public interface IAdminService {

	Object saveConnectorDetails(ConnectorDTO connectorDTO) throws Exception;

	Object getConnDetailsByName(String  connectorType) throws Exception;

	Object getConnDetailsById(Integer id) throws Exception;

	Object findAllDetails() throws Exception;

	Object deleteConnDetailsById(Integer id) throws Exception;

	Object updateConnectorDetails(ConnectorDTO connectorDTO)  throws Exception;

}
