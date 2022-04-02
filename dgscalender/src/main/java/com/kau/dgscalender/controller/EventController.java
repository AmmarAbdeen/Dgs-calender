package com.kau.dgscalender.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.CloseableThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.kau.dgscalender.Notifications;
import com.kau.dgscalender.dto.EventDTO;
import com.kau.dgscalender.service.EventService;
import com.kau.dgscalender.service.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.apachecommons.CommonsLog;

@RestController
@CommonsLog
@RequestMapping("/api/calender/event")
public class EventController extends BaseController {

	@Autowired
	private EventService eventService;
	@Autowired
	protected JWTService jwtservice;
	
	private Notifications notifications = new Notifications(0);

	@GetMapping(value = "/allevents")
	public ResponseEntity<?> getAllEvents(HttpServletRequest request,
			@RequestHeader("session-token") String sessionToken) {
		try {
			log.info("Enter getAllEvents API");
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			request.setAttribute("tokenData", tokenInfo.getBody());
			Map<String, String> tokenData = (Map<String, String>) request.getAttribute("tokenData");
			String username = tokenData.get("userName");
			jwtservice.checkPrivilegesAccess(request, username);
			return success(eventService.getAllEvents());

		} catch (Exception e) {
			return wrapException(e, "getAllEvents");

		}

	}

	@PostMapping(value = "/addevent")
	public ResponseEntity<?> addEvent(HttpServletRequest request,@RequestHeader("session-token") String sessionToken, @RequestBody EventDTO eventRequest) {
		try {
			log.info("Enter addEvent API...with request :" + eventRequest.toString());
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			request.setAttribute("tokenData", tokenInfo.getBody());
			Map<String, String> tokenData = (Map<String, String>) request.getAttribute("tokenData");
			String username = tokenData.get("userName");
			jwtservice.checkPrivilegesAccess(request, username);
			EventDTO eventResponse = eventService.addEvent(eventRequest,username,notifications);

			return success(new Gson().toJson(eventResponse));

		} catch (Exception e) {
			return wrapException(e, "addEvent");

		}
	}

	@PostMapping(value = "/updateevent")
	public ResponseEntity<?> updateEvent(HttpServletRequest request,@RequestHeader("session-token") String sessionToken, @RequestBody EventDTO eventRequest) {
		try {
			log.info("Enter updateEvent API...with request :" + eventRequest.toString());
			Jws<Claims> tokenInfo = jwtservice.decodeJWT(sessionToken);
			request.setAttribute("tokenData", tokenInfo.getBody());
			Map<String, String> tokenData = (Map<String, String>) request.getAttribute("tokenData");
			String username = tokenData.get("userName");
			jwtservice.checkPrivilegesAccess(request, username);
			EventDTO eventResponse = eventService.updateEvent(eventRequest,username);

			return success(new Gson().toJson(eventResponse));

		} catch (Exception e) {
			return wrapException(e, "updateEvent");

		}
	}

}
