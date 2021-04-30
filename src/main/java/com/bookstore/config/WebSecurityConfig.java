package com.bookstore.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.bookstore.services.CustomerUserDetailService;

@Order(1)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
		@Autowired
		private DataSource dataSource;
		
		@Bean
		public UserDetailsService userDetailsService() {
			return new CustomerUserDetailService();
		}
		
		@Bean
		public BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
			authProvider.setUserDetailsService(userDetailsService());
			authProvider.setPasswordEncoder(passwordEncoder());
			
			return authProvider;
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(authenticationProvider());
		}

		
//		@Override
//		public void configure(AuthenticationManagerBuilder auth) throws Exception {
//			auth.inMemoryAuthentication()
//				.passwordEncoder(new BCryptPasswordEncoder())
//				.withUser("janeodum41@gmail.com").password("$2a$10$LWJ1e/x1aBM/AWNVOMQ9aeID0iQz27fFyJ1WkNfQJVcjZUt9Rq57i")
//				.roles("ADMIN");
//				
//		}
		
		

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
				.antMatchers("/user_login").authenticated()
				.and()
				.formLogin()
					.loginPage("/user_login")
					.usernameParameter("emailaddress")
					.defaultSuccessUrl("/user_login")
					.permitAll()
				
				.and()
				.logout()
					.permitAll()
					.logoutSuccessUrl("/")
					
				
				.and()
					.rememberMe().tokenRepository(persistentTokenRepository())
				.and().csrf().disable();
			
			
		}

		@Bean
		public PersistentTokenRepository persistentTokenRepository() {
			JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
			tokenRepository.setDataSource(dataSource);
			return tokenRepository;
		}

}
