package com.example.myapp.user.mber.model;


import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MberUserDetails extends User {

	private static final long serialVersionUID = 1L;

	private String mberEmail;
	private String mberGender;
	private String mberBirth;
	private String mberPhone;
	private String mberRegistDate;

	
	public MberUserDetails(String username, String password, 
			Collection<? extends GrantedAuthority> authorities, String mberEmail, String mberGender, String mberBirth, String mberPhone, String mberRegistDate) {
		super(username, password, authorities);
		this.mberEmail = mberEmail;
		this.mberGender = mberGender;
		this.mberBirth = mberBirth;
		this.mberPhone = mberPhone;
		this.mberRegistDate = mberRegistDate;
	}

	public String getMberEmail() {
		return this.mberEmail;
	}
	public String getMberGender() {
		return this.mberGender;
	}
	public String getMberBirth() {
		return this.mberBirth;
	}
	public String getmberPhone() {
		return this.mberPhone;
	}
	public String getmberRegistDate() {
		return this.mberRegistDate;
	}
}