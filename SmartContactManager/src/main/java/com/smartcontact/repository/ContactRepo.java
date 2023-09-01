package com.smartcontact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;
import com.smartcontact.entities.Contact;
import com.smartcontact.entities.User;

public interface ContactRepo extends JpaRepository<Contact, Integer> {
	
	@Query("from Contact as c where c.user.id= :userId")
	public List<Contact> findContactsByUserId(@Param("userId") int userId);
	
	
	//there LOWER() is used to change String case into lower case
	//we can also use UPPER()
	//this is my search bar
	/*
	 * @Query("from Contact c where LOWER(c.name)= LOWER(:username) AND c.user.id= :userId"
	 * ) public List<Contact> getContactByUsername(@Param("username") String
	 * username, @Param("userId") int userId);
	 */
	//tutorial search bar
	public List<Contact> findByNameContainingAndUser(String query, User user);
	
}
