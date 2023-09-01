package com.smartcontact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontact.entities.User;
import com.smartcontact.repository.UserRepo;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepo repo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user=repo.getUserByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("User not found..");
		}
		
		UserCustomDetails userCustomDetails = new UserCustomDetails(user);
		return userCustomDetails;
	}

}
