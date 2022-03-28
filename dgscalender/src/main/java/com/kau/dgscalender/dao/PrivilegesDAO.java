package com.kau.dgscalender.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kau.dgscalender.entity.Privileges;


@Repository
public interface PrivilegesDAO extends JpaRepository<Privileges, Long> {
	
	List<Privileges> findAllByUserIdOrderByPrivilegeorder(Long userId);
	
	List<Privileges> findAllByAdminPrivilege(boolean AdminPrivilege);
	
	Privileges findByBackendUrl (String backendUrl);
		

}
