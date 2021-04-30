package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstore.domain.User;

public interface UserRepo extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.id = ?1")
	public Optional<User> findById(Integer id);
	
	@Query("SELECT u FROM User u WHERE u.emailaddress = ?1")
	public User findByEmail(String emailaddress);
	
	@Query("SELECT u FROM User u WHERE u.username = ?1")
	public User findByUsername(String username);
	
	@Query("UPDATE User u SET u.enabled = true WHERE u.id = ?1")
	@Modifying
	public void enable (Integer id);
	
	@Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
	public User findByVerificationCode(String code);
	
	@Query("SELECT u FROM User u WHERE u.emailaddress = :emailaddress")
	public User getUserByEmail(@Param("emailaddress") String emailaddress);
	
	public User findByResetPasswordToken(String token);
	
	@Query("SELECT u FROM User u WHERE u.user_type = ?1")
	public User findByUserType(String user_type);

	@Query("SELECT u FROM User u WHERE u.username = ?1")
	public User findUser(String username);
	
	
	
}
