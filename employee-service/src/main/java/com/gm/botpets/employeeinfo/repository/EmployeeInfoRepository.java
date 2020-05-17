package com.gm.botpets.employeeinfo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gm.botpets.employeeinfo.model.EmployeeInfo;


@Repository
public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, Integer> {
	
	@Query(value = "SELECT * FROM employee WHERE emp_id=:empId", nativeQuery = true)	
	public EmployeeInfo findEmployeeInfobyId(@Param("empId") Integer empId );
	
	@Query(value = "SELECT * FROM emp_leave WHERE emp_id=:empId", nativeQuery = true)	
	public Object findLeaveEmployeeInfobyId(@Param("empId") Integer empId );
	
	@Query(value = "SELECT * FROM employee WHERE emp_name=:empName", nativeQuery = true)	
	public EmployeeInfo findEmployeeInfobyName(@Param("empName") String empName );
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "Update employee e set e.emp_addresses=:empAddresses WHERE e.emp_Name=:empName", nativeQuery = true)	
	public int updateEmployeeAddressByName(@Param("empAddresses") String empAddresses,@Param("empName") String empName );
	
	@Transactional
	@Modifying
	@Query(value = "Insert into emp_leave(emp_id,emp_name,timeoff_starttime,timeoff_endtime,leave_reason) values(:empId,:empName,:timeoffStartTime,:timeoffEndTime,:leaveReason)", nativeQuery = true)	
	public int saveTimeOffRequestById(@Param("empId") Integer empId,@Param("empName") String empName,@Param("timeoffStartTime") String timeoffStartTime ,@Param("timeoffEndTime") String timeoffEndTime,@Param("leaveReason") String leaveReason);

	@Query(value="Select * from emp_contact where emp_email=:empEmail OR emp_phoneno=:empPhoneNo",nativeQuery = true)
	public Object findEmployeeInfoByEmailOrPhoneNo(@Param("empEmail") String empEmail,@Param("empPhoneNo") String empPhoneNo  );
}
