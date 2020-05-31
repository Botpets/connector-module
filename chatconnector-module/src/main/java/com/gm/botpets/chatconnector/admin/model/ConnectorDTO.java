package com.gm.botpets.chatconnector.admin.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConnectorDTO {

	private Integer id;
	private String connectorType;
	private Object connectorDetails;
	private String isDefault;
	private String connectorAlias;
}
