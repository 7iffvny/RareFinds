package com.bookstore.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bookstore.domain.UserPayment;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long>{
	@Query("SELECT COUNT(u) FROM UserPayment u WHERE u.user.id=?1")
    long countById(Long id);

}
