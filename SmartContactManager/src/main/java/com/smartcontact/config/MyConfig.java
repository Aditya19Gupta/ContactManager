package com.smartcontact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig{
	
	@Bean
	public UserDetailsService getUserDetails() {
		
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider daoAuthProvider = new 		DaoAuthenticationProvider();
		daoAuthProvider.setPasswordEncoder(this.passwordEncoder());
		daoAuthProvider.setUserDetailsService(this.getUserDetails());
		
		return daoAuthProvider;
	}
	
	@Bean
	public SecurityFilterChain getConfigure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeHttpRequests()
		.requestMatchers("/user/**").hasRole("USER")
		.requestMatchers("/admin/**").hasRole("ADMIN")
		.requestMatchers("/**")
		.permitAll()
		.anyRequest().authenticated().and()
		.formLogin()
		.loginPage("/signin")
		.defaultSuccessUrl("/user/index")
		.loginProcessingUrl("/dologin");
//		;
		http.authenticationProvider(authProvider());
		return http.build();
	}
}








