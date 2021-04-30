package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstore.domain.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {

	@Query("SELECT p FROM Payment p WHERE p.id = ?1")
	public Payment findByPaymentId(Integer id);
	
	@Query("SELECT p FROM Payment p WHERE p.user_email = ?1")
	public Payment findByEmail(String email);
}
