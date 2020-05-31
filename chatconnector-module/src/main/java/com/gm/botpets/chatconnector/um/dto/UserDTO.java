package com.gm.botpets.chatconnector.um.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
private Integer id;
private String firstName;
private String lastName;
private String email;
private String password;
private String phoneNo;
}
