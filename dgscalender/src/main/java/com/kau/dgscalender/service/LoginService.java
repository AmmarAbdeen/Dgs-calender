package com.kau.dgscalender.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kau.dgscalender.dao.PrivilegesDAO;
import com.kau.dgscalender.dao.UserDAO;
import com.kau.dgscalender.dto.PrivilegesDTO;
import com.kau.dgscalender.dto.UserDTO;
import com.kau.dgscalender.entity.Privileges;
import com.kau.dgscalender.entity.User;
import com.kau.dgscalender.exception.BusinessException;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Service
public class LoginService extends BaseService {
	
	@Autowired
	private UserDAO userDAO; 
	@Autowired
	private JWTService jwtservice;
	@Autowired
	private PrivilegesDAO privilegesDAO; 
	
	public UserDTO login(UserDTO request) throws BusinessException{
		try {
			log.info("Enter login Function...with request :" + request);
			User user = userDAO.findByUsernameAndPassword(request.getUsername(),
					request.getPassword());
			
			if (user == null) {
				throw new BusinessException("Invalid  UserName or password ");
			}
			Map<String, String> claims = new HashMap<>();
			claims.put("userName", request.getUsername());
			Map<String, Object> headers = new HashMap<>();
			String token = jwtservice.createJWT(headers, claims);
			List<Privileges> privileges = new ArrayList<>();
			for (Privileges privilege : privilegesDAO.findAllByUserIdOrderByPrivilegeorder(user.getId())) {
				if (privilege.isMainPrivilege() || privilege.isMenuItem())
					privileges.add(privilege);
			}
			UserDTO userDTO = mapToDTO(user,false);
			userDTO.setPrivileges(mapToDTOPrivilegesList(privileges));
			userDTO.setSessionToken(token);
			return userDTO;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		
	}
	
	public UserDTO mapToDTO(User user ,boolean showSesensitiveData) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setFullName(user.getFullName());
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		userDTO.setAdmin(user.isAdmin());
		if(showSesensitiveData) {
			userDTO.setPassword(user.getPassword());
		}				
		return userDTO;
	}
	
	public List<PrivilegesDTO> mapToDTOPrivilegesList(List<Privileges> privileges){
		List<PrivilegesDTO> privilegesDTOs = new ArrayList<>();
		for(Privileges privilege : privileges) {
			privilegesDTOs.add(mapPrivilegToDTO(privilege));
		}
		return privilegesDTOs;
	}
	
	public PrivilegesDTO mapPrivilegToDTO(Privileges privilege ) {
		PrivilegesDTO privilegeDTO = new PrivilegesDTO();
		privilegeDTO.setId(privilege.getId());
		privilegeDTO.setCode(privilege.getCode());
		privilegeDTO.setArabicName(privilege.getArabicName());
		privilegeDTO.setEnglishName(privilege.getEnglishName());
		privilegeDTO.setIconPath(privilege.getIconPath());
		privilegeDTO.setRouterLink(privilege.getRouterLink());
		privilegeDTO.setBackendUrl(privilege.getBackendUrl());
		privilegeDTO.setMainPrivilege(privilege.isMainPrivilege());
		privilegeDTO.setMenuItem(privilege.isMenuItem());
		privilegeDTO.setAdminPrivilege(privilege.isAdminPrivilege());
		privilegeDTO.setPrivilegeorder(privilege.getPrivilegeorder());
		privilegeDTO.setParentPrivilegeId(privilege.getParentPrivilege() != null ?privilege.getParentPrivilege().getId():null);
		return privilegeDTO;
	}
	

}
