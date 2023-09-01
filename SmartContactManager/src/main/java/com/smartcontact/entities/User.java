package com.smartcontact.entities;

import java.util.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank(message = "Name can't be blank..")
	private String name;
	
	@Column(unique = true)
	@NotBlank(message = "Email can't be blank..")
	@Email
	private String email;
	
	@NotBlank(message = "Password can't be blank..")
	private String password;
	
	private boolean active;
	
	@Column(length = 500)
	private String about;
	
	private String role;
	private String imageUrl;
	 // Cascade.ALL is used for update
																						 	// all related entries and Lazy
																											// use for fetch data when call
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user",orphanRemoval = true)
	//orphanRemoval for, if child entity unlink to parent entity then child enitity will deleted
	List<Contact> contact = new ArrayList<>();
	
	public User() {
		
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", active="
				+ active + ", about=" + about + ", role=" + role + ", imageUrl=" + imageUrl + ", contact=" + contact
				+ "]";
	}

	public User(int id, String name, String email, String password, boolean active, String about, String role,
			String imageUrl) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.active = active;
		this.about = about;
		this.role = role;
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Contact> getContact() {
		return contact;
	}

	public void setContact(List<Contact> contact) {
		this.contact = contact;
	}

}
