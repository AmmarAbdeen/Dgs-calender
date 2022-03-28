package com.kau.dgscalender.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kau.dgscalender.entity.Sectors;

@Repository
public interface SectorsDAO extends JpaRepository<Sectors,Long>{
	
	Sectors findByName(String name);

}
