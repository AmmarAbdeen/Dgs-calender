package com.kau.dgscalender.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.kau.dgscalender.dto.BaseDTO;
import com.kau.dgscalender.dto.CalenderResponse;
import com.kau.dgscalender.dto.UserDTO;
import com.kau.dgscalender.service.JWTService;
import com.kau.dgscalender.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.apachecommons.CommonsLog;

@RestController
@CommonsLog
@RequestMapping("/api/calender/user")
public class UserController extends BaseController{

	@Autowired
	private UserService userService;
	@Autowired
	protected JWTService jwtservice;
	
	@PostMapping(value = "/adduser")
	public ResponseEntity<?> addUser(HttpServletRequest request,@RequestHeader("session-token") String sessionToken,@RequestBody UserDTO userRequest ){
		try {
			log.info("Enter addUser API...with request :" + userRequest.toString());
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			request.setAttribute("tokenData", tokenInfo.getBody());
			Map<String, String> tokenData = (Map<String, String>) request.getAttribute("tokenData");
			String username = tokenData.get("userName");
			jwtservice.checkPrivilegesAccess(request, username);
			UserDTO userResponse = userService.addUser(userRequest);
			
			return success(new Gson().toJson(userResponse));

		} catch (Exception e) {
			return wrapException(e, "addUser");

		}
	}
	
	@PostMapping(value = "/editinfo")
	public ResponseEntity<?> editInfo(HttpServletRequest request,@RequestHeader("session-token") String sessionToken,@RequestBody UserDTO userRequest ){
		try {
			log.info("Enter editInfo API...with request :" + userRequest.toString());
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			request.setAttribute("tokenData", tokenInfo.getBody());
			Map<String, String> tokenData = (Map<String, String>) request.getAttribute("tokenData");
			String username = tokenData.get("userName");
			jwtservice.checkPrivilegesAccess(request, username);
			UserDTO userResponse = userService.editUser(userRequest);
			
			return success(new Gson().toJson(userResponse));

		} catch (Exception e) {
			return wrapException(e, "editInfo");

		}
	}
	

	@PostMapping(value = "/changepassword")
	public ResponseEntity<?> changePassword(HttpServletRequest request,@RequestHeader("session-token") String sessionToken,@RequestBody BaseDTO encodedRequest ){
		try {
			log.info("Enter changePassword API...with request :" + encodedRequest.toString());
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			request.setAttribute("tokenData", tokenInfo.getBody());
			Map<String, String> tokenData = (Map<String, String>) request.getAttribute("tokenData");
			String username = tokenData.get("userName");
			jwtservice.checkPrivilegesAccess(request, username);
			encodedRequest.setEncryptedDataType(UserDTO.class);
			UserDTO userDTO = getDecodedrequest(encodedRequest);
			userService.changePassword(username,userDTO);
			
			return success(new CalenderResponse("0","Success"));

		} catch (Exception e) {
			return wrapException(e, "changePassword");

		}
	}
	
	
	@GetMapping(value = "/getparentprivileges/{userId}")
	public ResponseEntity<?> getParentPrivilegesByUser(HttpServletRequest request,@RequestHeader("session-token") String sessionToken,@PathVariable("userId") Long userId) {
		try {
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			request.setAttribute("tokenData", tokenInfo.getBody());
			Map<String, String> tokenData = (Map<String, String>) request.getAttribute("tokenData");
			String username = tokenData.get("userName");
			jwtservice.checkPrivilegesAccess(request, username);
			return success(userService.getAllPrivilegesParentWithChildren(userId));

		} catch (Exception e) {
			return wrapException(e, "getParentPrivilegesByUser");

		}
	}
	
	@GetMapping(value = "/getallusers")
	public ResponseEntity<?> getAllUsers(HttpServletRequest request,@RequestHeader("session-token") String sessionToken) {
		try {
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			request.setAttribute("tokenData", tokenInfo.getBody());
			Map<String, String> tokenData = (Map<String, String>) request.getAttribute("tokenData");
			String username = tokenData.get("userName");
			jwtservice.checkPrivilegesAccess(request, username);
			return success(userService.getAllUsers());

		} catch (Exception e) {
			return wrapException(e, "getAllUsers");

		}
	}
	
	@GetMapping(value = "/getuserinfo/{name}")
	public ResponseEntity<?> getUserByUsername(HttpServletRequest request,
			@RequestHeader("session-token") String sessionToken, @PathVariable("name") String name) {
		try {
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			request.setAttribute("tokenData", tokenInfo.getBody());
			Map<String, String> tokenData = (Map<String, String>) request.getAttribute("tokenData");
			String username = tokenData.get("userName");
			jwtservice.checkPrivilegesAccess(request, username);
			return success(userService.getUserByUserName(name));

		} catch (Exception e) {
			return wrapException(e, "getUserByUsername");

		}
	}
	
	@GetMapping(value = "/getallprivileges/{userId}")
	public ResponseEntity<?> getAllPrivilegesByUser(HttpServletRequest request,@RequestHeader("session-token") String sessionToken,@PathVariable("userId") Long userId) {
		try {
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			request.setAttribute("tokenData", tokenInfo.getBody());
			Map<String, String> tokenData = (Map<String, String>) request.getAttribute("tokenData");
			String username = tokenData.get("userName");
			jwtservice.checkPrivilegesAccess(request, username);
			return success(userService.getAllPrivileges(userId));

		} catch (Exception e) {
			return wrapException(e, "getAllPrivilegesByUser");

		}
	}
	

}
