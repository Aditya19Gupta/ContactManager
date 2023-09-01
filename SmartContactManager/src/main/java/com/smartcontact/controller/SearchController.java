package com.smartcontact.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontact.entities.Contact;
import com.smartcontact.entities.User;
import com.smartcontact.repository.ContactRepo;
import com.smartcontact.repository.UserRepo;

@RestController
public class SearchController {
	
	@Autowired
	private ContactRepo contactRepo;
	@Autowired
	private UserRepo repo;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal p){
		
		User user=repo.getUserByUsername(p.getName());
		List<Contact> list = contactRepo.findByNameContainingAndUser(query, user);
		
		//result will send in the form of json
		//data will serialised so remove the circular dependency, use @JsonIgnore on user field which is in contact entities class
		return ResponseEntity.ok(list);
	}
	
}
