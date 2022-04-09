package com.kau.dgscalender.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends BaseDTO  {
	
private Long id;
	
	private String username;
	
	private String password;

	private String newPassword;

	private String confirmPassword;

	private String email;

	private String fullName;
	
	private String sessionToken;
	
	private int notificationNumber;
	
	private boolean admin;
	
	private String sector;
	
	private List<PrivilegesDTO> privileges = new ArrayList<>();


}
