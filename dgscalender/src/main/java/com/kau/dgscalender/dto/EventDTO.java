package com.kau.dgscalender.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDTO extends BaseDTO {
	
	private Long id;

	private String title;
	
	private String start;
	
	private String end;
	
	private String createdBy;
	
	private String modifiedBy;
	
	private String color;
	
	private boolean allDay;
	
	private String sector;

}
