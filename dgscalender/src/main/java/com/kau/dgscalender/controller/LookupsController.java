package com.kau.dgscalender.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kau.dgscalender.service.JWTService;
import com.kau.dgscalender.service.LookupsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.apachecommons.CommonsLog;

@RestController
@CommonsLog
@RequestMapping("/api/calender/lookups")
public class LookupsController extends BaseController{
	
	@Autowired
	protected LookupsService lookupsService;
	@Autowired
	protected JWTService jwtservice;
	
	@GetMapping(value = "/alllookups/{type}")
	public ResponseEntity<?> getAllLookupsByType(HttpServletRequest request, @RequestHeader("session-token") String sessionToken,@PathVariable("type") String type) {
		try {
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			request.setAttribute("tokenData", tokenInfo.getBody());
			Map<String, String> tokenData = (Map<String, String>) request.getAttribute("tokenData");
			String username = tokenData.get("userName");
			jwtservice.checkPrivilegesAccess(request, username);
			return success(lookupsService.getAllLookupsByType(type));

		} catch (Exception e) {
			return wrapException(e, "searchSheeps");

		}
	}

}
