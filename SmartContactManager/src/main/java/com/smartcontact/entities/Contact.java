package com.smartcontact.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
                     
@Entity
public class Contact {
    

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int cid;
    private String name;
    private String nickname;
    private String phone;
    private String imageUrl;
    private String work;
    @Column(length=5000)
    private String description;
    private String email;
    @ManyToOne()
    @JsonIgnore //remove the circular dependency
    private User user;
    
    
    
    public Contact() {
		super();
	}

	public Contact(int cid, String name, String nickname, String phone, String imageUrl, String work,
            String description, String email,User user) {
        this.cid = cid;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.work = work;
        this.description = description;
        this.email = email;
        this.user=user;
    }
    
    public int getCid() {
        return cid;
    }
    public void setCid(int cid) {
        this.cid = cid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getWork() {
        return work;
    }
    public void setWork(String work) {
        this.work = work;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    

}
