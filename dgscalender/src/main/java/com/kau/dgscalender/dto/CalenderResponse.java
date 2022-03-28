package com.kau.dgscalender.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.JsonElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalenderResponse extends BaseDTO implements Serializable {
	
	private static final long serialVersionUID = 4629513827289686725L;
	public static final String SUCCESS = "Success";
	private String status;
	private String description;
	private JsonElement externalSystemResponse;
	
	public CalenderResponse(String status, String description) {
		this.status = status;
		this.description = description;
	}

	public CalenderResponse() {

	}

	public CalenderResponse(String status, String description, JsonElement externalSystemResponse) {
		super();
		this.status = status;
		this.description = description;
		this.externalSystemResponse = externalSystemResponse;
		// this.externalSystemResponse = Utility.gson.toJson(externalSystemResponse);

	}


}
