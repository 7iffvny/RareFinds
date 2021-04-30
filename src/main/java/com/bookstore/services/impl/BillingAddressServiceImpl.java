package com.bookstore.services.impl;

import org.springframework.stereotype.Service;

import com.bookstore.domain.BillingAddress;
import com.bookstore.domain.UserBilling;
import com.bookstore.services.BillingAddressService;

@Service
public class BillingAddressServiceImpl implements BillingAddressService{
	
	public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {
		billingAddress.setBillingName(userBilling.getUserBillingName());
		billingAddress.setBillingstreet1(userBilling.getUserBillingStreet1());
		billingAddress.setBillingstreet2(userBilling.getUserBillingStreet2());
		billingAddress.setCity(userBilling.getUserBillingCity());
		billingAddress.setState(userBilling.getUserBillingState());
		billingAddress.setCountry(userBilling.getUserBillingCountry());
		billingAddress.setBillingzipcode(userBilling.getUserBillingZipcode());
		
		return billingAddress;
	}

}