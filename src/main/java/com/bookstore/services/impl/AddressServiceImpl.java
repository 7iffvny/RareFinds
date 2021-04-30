package com.bookstore.services.impl;

import org.springframework.stereotype.Service;

import com.bookstore.domain.Address;
import com.bookstore.domain.UserShipping;
import com.bookstore.services.AddressService;

@Service
public class AddressServiceImpl  implements AddressService {
	public Address setByUserShipping(UserShipping userShipping, Address shippingAddress) {
		shippingAddress.setShippingname(userShipping.getUserShippingName());
		shippingAddress.setShippingstreet1(userShipping.getUserShippingStreet1());
		shippingAddress.setShippingstreet2(userShipping.getUserShippingStreet2());
		shippingAddress.setCity(userShipping.getUserShippingCity());
		shippingAddress.setState(userShipping.getUserShippingState());
		shippingAddress.setCountry(userShipping.getUserShippingCountry());
		shippingAddress.setZipcode(userShipping.getUserShippingZipcode());
		
		return shippingAddress;
	}
}