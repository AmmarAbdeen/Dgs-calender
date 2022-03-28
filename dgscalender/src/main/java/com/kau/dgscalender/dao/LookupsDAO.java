package com.kau.dgscalender.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kau.dgscalender.entity.Lookups;


@Repository
public interface LookupsDAO extends JpaRepository<Lookups, Long> {
	
	List<Lookups> findAllByType(String type);

}
