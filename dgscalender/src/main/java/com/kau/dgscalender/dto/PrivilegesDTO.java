package com.kau.dgscalender.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PrivilegesDTO extends BaseDTO{
	
    private Long id;
	
	private String code;

	private String arabicName;

	private String englishName;

	private String iconPath;

	private String routerLink;

	private String backendUrl;
	
	private boolean mainPrivilege;

	private boolean adminPrivilege;

	private boolean menuItem;

	private List<PrivilegesDTO> childrenPrivilege;
	
	private Long privilegeorder;

	private Long parentPrivilegeId;

}
