package com.gm.botpets.employeeinfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gm.botpets.employeeinfo.model.LeaveInfo;

public interface EmployeeLeaveRepository extends JpaRepository<LeaveInfo, Integer>{
	@Query(value="Select * from emp_leave where emp_id=:empId AND timeoff_starttime=:timeoffStartTime AND timeoff_endtime=:timeoffEndTime",nativeQuery = true)
	public LeaveInfo findEmployeeLeaveStatus(@Param("empId") Integer empId,@Param("timeoffStartTime") String timeoffStartTime, @Param("timeoffEndTime") String timeoffEndTime);
}
