package com.kau.dgscalender.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER")
public class User extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "USERNAME", nullable = false)
	private String username;
	
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	
	@Column(name = "EMAIL")
	private String email;

	@Column(name = "FULL_NAME")
	private String fullName;
	
	@Column(name = "NOTIFICATION_NUMBER")
	private int notificationNumber;
		
	@Column(name = "ADMIN", columnDefinition = "boolean default false")
	private boolean admin;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USER_PRIVILEGES", joinColumns = {
			@JoinColumn(referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(referencedColumnName = "ID") })
	private List<Privileges> privilege;
	
	@ManyToOne
	@JoinColumn(name = "SECTOR_ID")
	private Sectors sector;

	

	
	

}
