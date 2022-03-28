package com.kau.dgscalender.exception;

import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JsonElement externalSystemResponse;

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message, JsonElement externalSystemResponse, Throwable cause) {
		super(message, cause);
		this.externalSystemResponse = externalSystemResponse;
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, JsonElement externalsystemResponse) {
		super(message);
		this.externalSystemResponse = externalsystemResponse;
	}

}

