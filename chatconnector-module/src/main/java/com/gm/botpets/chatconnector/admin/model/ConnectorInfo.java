package com.gm.botpets.chatconnector.admin.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "connector_info")
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class ConnectorInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
 
	@Column(name = "connector_type")
	private String connectorType;
	
	@Column(name = "connector_details")
	private String connectorDetails;
	
	@Column(name = "is_default")
	private String isDefault;
	
	@Column(name = "connector_alias")
	private String connectorAlias;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(String connectorType) {
		this.connectorType = connectorType;
	}

	public String getConnectorDetails() {
		return connectorDetails;
	}

	public void setConnectorDetails(String connectorDetails) {
		this.connectorDetails = connectorDetails;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getConnectorAlias() {
		return connectorAlias;
	}

	public void setConnectorAlias(String connectorAlias) {
		this.connectorAlias = connectorAlias;
	}
	
}
