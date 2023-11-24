package com.smart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;
import com.smart.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
	
	@Query("from Contact as c where c.user.id =:userId")
	public List<Contact> findContactsByUser(@Param("userId") int userId);
	
//	@Query("SELECT c FROM Contact c WHERE c.user_id = :userid AND c.name LIKE %:query%")
//	public List<Contact> findByNameContainingAndUser(@Param("query")String query,@Param("userid")int userid);
	
	public List<Contact> findByNameContainingAndUser(String keywords, User user);
}
