package com.smartcontact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import com.smartcontact.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	@Query("select u from User u where u.email= :email")
	public User getUserByUsername(@Param("email") String email);
	//this is here because of CrudRepository access only Integer related data like getUserById(int id), etc. If we want you access String type data we have to fire these lines.  
}
