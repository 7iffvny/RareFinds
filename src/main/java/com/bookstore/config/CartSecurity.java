package com.bookstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class CartSecurity extends WebSecurityConfigurerAdapter {


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/cartView").authenticated()
		.anyRequest().permitAll()
		.and()
		.csrf().disable().cors().disable()
		.formLogin()
		.loginPage("/user_login")
			.usernameParameter("emailaddress")
			.defaultSuccessUrl("/user_login")
			.permitAll()
		.and()
		.logout().logoutSuccessUrl("/").permitAll()
		.and().csrf().disable();
	}
}
