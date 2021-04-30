package com.bookstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bookstore.domain.CustomerUserDetails;
import com.bookstore.domain.User;
import com.bookstore.repository.UserRepo;

import net.bytebuddy.utility.RandomString;

public class CustomerUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String emailaddress) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(emailaddress);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return new CustomerUserDetails(user);
	}
	
}
