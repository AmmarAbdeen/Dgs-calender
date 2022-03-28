package com.kau.dgscalender.controller;

import java.util.Base64;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.google.gson.Gson;
import com.kau.dgscalender.dto.BaseDTO;
import com.kau.dgscalender.dto.CalenderResponse;
import com.kau.dgscalender.exception.ExternalSystemException;

import lombok.extern.apachecommons.CommonsLog;

@Component
@CommonsLog
@CrossOrigin(origins = "*", allowedHeaders = "*")
public abstract class BaseController {
	
	public <D extends BaseDTO> ResponseEntity<CalenderResponse> success() {
		return new ResponseEntity<CalenderResponse>(HttpStatus.OK);
	}

	public <D extends BaseDTO> ResponseEntity<BaseDTO> success(D results) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(results);
	}

	public ResponseEntity<String> success(String results) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(results);
	}

	@Deprecated
	public ResponseEntity<CalenderResponse> wrapException(String msg, String funName) {
		log.error("Returned from " + funName + " API With error: " + msg);
		return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(new CalenderResponse("1", msg));
	}

	public ResponseEntity<String> wrapException(Exception e, String functionName) {
		log.error("Returned from " + functionName + " API With error: " + e.getMessage(), e);
		if (e instanceof ExternalSystemException) {
			return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
					new CalenderResponse("1", e.getMessage(), ((ExternalSystemException) e).getExternalSystemResponse())
							.toString());
		} else if (e.getCause() instanceof JpaSystemException
				|| e.getCause() instanceof IncorrectResultSizeDataAccessException
				|| e.getCause() instanceof ConstraintViolationException) {
			return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
					.body(new CalenderResponse("1", " General Error, try again later").toString());
		}
		return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
				.body(new CalenderResponse("1", e.getMessage()).toString());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseDTO> T getDecodedrequest(BaseDTO encodedRequest) {

		return (T) new Gson().fromJson(decodeBase64(encodedRequest.getEncryptedData()),
				encodedRequest.getEncryptedDataType());

	}
	
	public static String decodeBase64(String body) {
		byte[] decodedBytes = Base64.getDecoder().decode(body);
		return new String(decodedBytes);
	}
	

}
