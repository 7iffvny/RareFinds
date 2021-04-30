package com.bookstore.domain;

import java.util.Collection;



import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomerUserDetails implements UserDetails {

	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	
	public User getUser() {
		return user;
	}
	
	public CustomerUserDetails(User user) {
		this.user = user;
	}
	
	

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public String getFullname() {
		return user.getFullName();
	}

	public String getFirstname() {
		return user.getFirstname();
	}
	
	public String getLastname() {
		return user.getLastname();
	}
	
	public String getPhonenumber() {
		return user.getPhonenumber();
	}
	
	public String getEmailaddress() {
		return user.getEmailaddress();
	}
	
	public Long getId() {
		return user.getId();
	}
	public String getUserType() {
		return user.getUser_type();
	}
	public String getUserStatus() {
		return user.getUser_status();
	}
	
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getUserEnabled() {
		return user.isEnabled();
	}
}
