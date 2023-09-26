package com.example.myapp.user.mber.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MberUserDetails extends User {

	private static final long serialVersionUID = 1L;

	private String mberGender;
		
	private boolean enabled;

	public MberUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String mberGender, boolean enabled) {
		super(username, password, authorities);
		this.mberGender = mberGender;
		this.enabled = enabled;
	}

	public String getMberGender() {
		return this.mberGender;
	}
	
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}