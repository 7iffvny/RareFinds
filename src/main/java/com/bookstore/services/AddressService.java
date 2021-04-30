package com.bookstore.services;

import com.bookstore.domain.Address;
import com.bookstore.domain.UserShipping;

public interface AddressService {
	Address setByUserShipping(UserShipping userShipping, Address shippingAddress);
}
