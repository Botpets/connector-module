package com.gm.botpets.chatconnector.um.service;

import com.gm.botpets.chatconnector.um.dto.LoginDTO;
import com.gm.botpets.chatconnector.um.dto.UserDTO;

public interface UserService {

	public Object saveUserDetails(UserDTO queryDTO);

	public Object loginAndValidateUser(LoginDTO loginDTO);

	public Object saveProfileDetails(UserDTO queryDTO);

}
