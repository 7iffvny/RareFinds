package com.bookstore.services;

import com.bookstore.domain.BillingAddress;
import com.bookstore.domain.Payment;
import com.bookstore.domain.UserPayment;

public interface PaymentService {

	
	Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
