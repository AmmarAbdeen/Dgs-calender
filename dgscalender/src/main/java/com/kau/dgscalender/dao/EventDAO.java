package com.kau.dgscalender.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kau.dgscalender.entity.Event;

@Repository
public interface EventDAO extends JpaRepository<Event, Long> {

}
