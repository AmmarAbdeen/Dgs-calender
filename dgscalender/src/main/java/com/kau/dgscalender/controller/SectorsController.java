package com.kau.dgscalender.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.kau.dgscalender.dto.EventDTO;
import com.kau.dgscalender.dto.SectorsDTO;
import com.kau.dgscalender.service.JWTService;
import com.kau.dgscalender.service.SectorsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.apachecommons.CommonsLog;

@RestController
@CommonsLog
@RequestMapping("/api/calender/sectors")
public class SectorsController extends BaseController{
	@Autowired
	private SectorsService sectorsService;
	@Autowired
	protected JWTService jwtservice;
	
	@GetMapping(value = "/allsectors")
	public ResponseEntity<?> getAllSectors(@RequestHeader("session-token") String sessionToken) {
		try {
			log.info("Enter getAllSectors API");
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			return success(sectorsService.getAllSectors());

		} catch (Exception e) {
			return wrapException(e, "getAllSectors");

		}
	}
	
	@PostMapping(value = "/addsector")
	public ResponseEntity<?> addSector(@RequestHeader("session-token") String sessionToken,HttpServletRequest request,@RequestBody SectorsDTO sectorRequest ){
		try {
			log.info("Enter addSector API...with request :" + sectorRequest.toString());
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			request.setAttribute("tokenData", tokenInfo.getBody());
			Map<String, String> tokenData = (Map<String, String>) request.getAttribute("tokenData");
			String username = tokenData.get("userName");
			jwtservice.checkPrivilegesAccess(request, username);
			sectorRequest.setCreatedBy(username);
			SectorsDTO sectorResponse = sectorsService.addSector(sectorRequest);
			
			return success(new Gson().toJson(sectorResponse));

		} catch (Exception e) {
			return wrapException(e, "addSector");

		}
	}

}
