package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstore.domain.Address;

public interface AddressRepo extends JpaRepository<Address, Integer> {

	@Query("SELECT a FROM Address a WHERE a.user_email = ?1")
	public Address findByEmail(String email);
}
