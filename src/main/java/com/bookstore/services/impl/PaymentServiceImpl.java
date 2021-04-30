package com.bookstore.services.impl;



import org.springframework.stereotype.Service;

import com.bookstore.domain.Payment;
import com.bookstore.domain.UserPayment;
import com.bookstore.services.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService{
	
	public Payment setByUserPayment(UserPayment userPayment, Payment payment) {
		payment.setType(userPayment.getType());
		payment.setCardholder(userPayment.getHolderName());
		payment.setCardnumber(userPayment.getCardNumber());
		payment.setExpmonth(userPayment.getExpiryMonth());
		payment.setExpyear(userPayment.getExpiryYear());
		payment.setLastFourDigits(userPayment.getLastFourDigits());
		return payment;
	}
	
	
}
