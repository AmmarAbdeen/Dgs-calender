package com.kau.dgscalender.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LookupsDTO extends BaseDTO{
	
	private Long id;
	private String code;
	private String type;
	private String nameEN;
	private String nameAR;


}
