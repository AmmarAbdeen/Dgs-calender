package com.kau.dgscalender.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "EVENT")
public class Event extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "TITLE", nullable = false)
	private String title;
	
	@Column(name = "ALL_DAY")
	private boolean allDay;
	
	@Column(name = "START")
	private LocalDateTime start;
	
	@Column(name = "END_DATE")
	private LocalDateTime endDate;
	
	@ManyToOne
	@JoinColumn(name = "CREATED_BY")
	private User createdBy;
	
	@ManyToOne
	@JoinColumn(name = "MODIFIED_BY")
	private User modifiedBy;
	
	@ManyToOne
	@JoinColumn(name = "SECTOR_ID")
	private Sectors sector;

}
