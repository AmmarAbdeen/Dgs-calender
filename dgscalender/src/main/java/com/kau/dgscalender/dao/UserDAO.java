package com.kau.dgscalender.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kau.dgscalender.entity.User;

@Repository
public interface UserDAO extends JpaRepository<User,Long>{
	
	@Query("select m from User m where m.password= :password and lower(m.username) like lower(concat(:username))")
	User findByUserNameContainingIgnoreCaseAndPassword(@Param("username") String username,
			@Param("password") String password);
	
	User findByUsernameContainingIgnoreCase ( String username);
	
	User findByUsernameContainingIgnoreCaseAndIdNot( String username,Long id);
	
	User findByUsernameAndIdNot( String username,Long id);
	
	User findByUsername ( String username);
	
	User findByUsernameContainingIgnoreCaseAndPassword( String username,String password);
	
	User findByUsernameAndPassword ( String username,String password);
	
	User findByUsernameAndEmail ( String username,String email);
	
	User findByEmail (String email);
	
	User findByEmailAndIdNot (String email,Long id);

}
