package com.kau.dgscalender.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SectorsDTO extends BaseDTO {

	private Long id;
	
	private String name;

	private String color;
	
	private String createdBy;

}
