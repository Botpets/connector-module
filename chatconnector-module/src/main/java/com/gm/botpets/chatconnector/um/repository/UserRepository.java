package com.gm.botpets.chatconnector.um.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gm.botpets.chatconnector.um.model.User;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
	
	@Query(value = "SELECT password FROM user WHERE email=:username", nativeQuery = true)	
	public String findPassword(@Param("username") String username );
	
	@Query(value = "SELECT * FROM user WHERE email=:username", nativeQuery = true)	
	public User getUserDetails(@Param("username") String username );
	
	@Transactional
	@Modifying
	@Query(value = "Insert into user(id,firstname,lastname,email,password) values(:id,:firstName,:lastName,:email,:password)", nativeQuery = true)	
	public int saveUserDetails(@Param("id") Integer id,@Param("firstName") String firstName,@Param("lastName") String lastName ,@Param("email") String email,@Param("password") String password);

	@Transactional
	@Modifying
	@Query(value = "update user u set u.firstname=:firstName,u.lastname=:lastName,u.phoneno=:phoneNo where u.id=:id", nativeQuery = true)	
	public Integer updateUserDetails(@Param("id") Integer id,@Param("firstName") String firstName,@Param("lastName") String lastName ,@Param("phoneNo") String phoneNo);
}
