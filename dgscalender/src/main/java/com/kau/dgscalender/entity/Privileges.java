package com.kau.dgscalender.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@Table(name = "PRIVILEGES")
public class Privileges extends BaseEntity{
	@Expose
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "CODE")
	private String code;

	@Column(name = "ARABIC_NAME")
	private String arabicName;

	@Column(name = "ENGLISH_NAME")
	private String englishName;

	@Column(name = "ICON_PATH")
	private String iconPath;

	@Column(name = "ROUTER_LINK")
	private String routerLink;

	@Column(name = "BACKEND_URL")
	private String backendUrl;
	
	@Column(name = "MAIN_PRIVILEGE")
	private boolean mainPrivilege;

	@Column(name = "ADMIN_PRIVILEGE")
	private boolean adminPrivilege;

	@Column(name = "MENU_ITEM")
	private boolean menuItem;

	@Column(name = "PRIVILEGE_ORDER")
	private Long privilegeorder;

	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private Privileges parentPrivilege;
	
	@ManyToMany(mappedBy = "privilege", fetch = FetchType.LAZY)
	private List<User> user;

}
