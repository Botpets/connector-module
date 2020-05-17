package com.gm.botpets.chatconnector.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gm.botpets.chatconnector.admin.model.ConnectorInfo;

@Repository
public interface AdminRepository extends CrudRepository<ConnectorInfo,Integer> {

	@Query(value = "SELECT * FROM connector_info WHERE connector_type=:connectorType", nativeQuery = true)	
	public ConnectorInfo getConnDetailsByName(@Param("connectorType") String connectorType );
	
	@Transactional
	@Modifying
	@Query(value = "Insert into connector_info(id,connector_type,connector_details, is_default) values(:id,:connectorType,:connectorDetails,:isDefault)", nativeQuery = true)	
	public int saveConnectorDetails(@Param("id") Integer id,@Param("connectorType") String connectorType,@Param("connectorDetails") String connectorDetails, @Param("isDefault") String isDefault);

	@Query(value = "SELECT * FROM connector_info WHERE id=:id", nativeQuery = true)
	public ConnectorInfo getConnDetailsById(@Param("id") Integer id );

	@Transactional
	@Modifying
	@Query(value = "update connector_info c set c.connector_type=:connectorType,c.connector_details=:connectorDetails,c.is_default=:isDefault where c.id=:id", nativeQuery = true)	
	public int updateConnectorDetails( @Param("id") Integer id,@Param("connectorType") String connectorType,@Param("connectorDetails") String connectorDetails,@Param("isDefault") String isDefault);
}
