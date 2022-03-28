package com.kau.dgscalender.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public class BaseEntity implements Serializable{

	@CreationTimestamp
	@Column(name = "CREATION_TIME")
	private LocalDateTime creationTime;
}
