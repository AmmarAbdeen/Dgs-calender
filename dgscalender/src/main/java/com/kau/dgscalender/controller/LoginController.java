package com.kau.dgscalender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.kau.dgscalender.dto.BaseDTO;
import com.kau.dgscalender.dto.CalenderResponse;
import com.kau.dgscalender.dto.UserDTO;
import com.kau.dgscalender.service.LoginService;
import com.kau.dgscalender.service.UserService;

import io.jsonwebtoken.Claims;
import lombok.extern.apachecommons.CommonsLog;

@RestController
@CommonsLog
@RequestMapping("/api/calender")
public class LoginController extends BaseController{
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody BaseDTO encodedRequest) {
		try {
			log.info("Enter Login API...with request :" + encodedRequest.toString());
			encodedRequest.setEncryptedDataType(UserDTO.class);
			UserDTO user = getDecodedrequest(encodedRequest);
			return success(loginService.login(user));

		} catch (Exception e) {
			return wrapException(e, "login");

		}
	}
	
	@PostMapping(value = "/signuser")
	public ResponseEntity<?> signUser(@RequestHeader("session-token") String sessionToken,@RequestBody UserDTO userRequest ){
		try {
			log.info("Enter signUser API...with request :" + userRequest.toString());
			UserDTO userResponse = userService.addUser(userRequest);
			
			return success(new Gson().toJson(userResponse));

		} catch (Exception e) {
			return wrapException(e, "signUser");

		}
	}
	
	@PostMapping(value = "/verifytosendemail")
	public ResponseEntity<?> verifyToSendEmail(@RequestBody UserDTO userRequest ){
		try {
			log.info("Enter verifyToSendEmail API...with request :" + userRequest.toString());
			
			userService.verifyToSendEmail(userRequest);
			
			return success(new CalenderResponse("0","Success"));

		} catch (Exception e) {
			return wrapException(e, "verifyToSendEmail");

		}
	}

}
