package com.gm.botpets.employeeinfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gm.botpets.employeeinfo.model.EmployeeContact;


@Repository
public interface EmployeeContactRepository extends JpaRepository<EmployeeContact, Integer> {
	
	@Query(value="Select * from emp_contact where emp_email=:empEmail OR emp_phoneno=:empPhoneNo",nativeQuery = true)
	public EmployeeContact findEmployeeInfoByEmailOrPhoneNo(@Param("empEmail") String empEmail,@Param("empPhoneNo") String empPhoneNo  );
    
}
