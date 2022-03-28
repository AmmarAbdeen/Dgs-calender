package com.kau.dgscalender.exception;

import com.google.gson.JsonElement;

public class ExternalSystemException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5679895905371601689L;

	public ExternalSystemException(String message) {
		super(message);
	}

	public ExternalSystemException(String message, JsonElement externalSystemResponse) {
		super(message, externalSystemResponse);
	}

	public ExternalSystemException(String message, JsonElement externalSystemResponse, Throwable cause) {
		super(message, externalSystemResponse, cause);
	}

	public ExternalSystemException(String message, Throwable cause) {
		super(message, cause);
	}

}
