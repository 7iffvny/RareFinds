package com.bookstore.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.UserPayment;
import com.bookstore.repository.UserPaymentRepository;
import com.bookstore.services.UserPaymentService;

@Service
public class UserPaymentServiceImpl implements UserPaymentService{

	@Autowired
	private UserPaymentRepository userPaymentRepository;

	@Override
	public UserPayment findById(Long id) {
		return userPaymentRepository.findById(id).orElse(null);
	}

	@Override
	public void removeById(Long id) {
		userPaymentRepository.deleteById(id);		
	}
		
//	public UserPayment findById(Long id) {
//		return userPaymentRepository.findOne(id);
//	}
//	
//	public void removeById(Long id) {
//		userPaymentRepository.delete(id);
//	}
} 