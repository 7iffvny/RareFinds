package com.bookstore.services;

import com.bookstore.domain.BillingAddress;
import com.bookstore.domain.Order;
import com.bookstore.domain.Payment;
import com.bookstore.domain.Address;
import com.bookstore.domain.ShoppingCart;
import com.bookstore.domain.User;

public interface OrderService {
	Order createOrder(ShoppingCart shoppingCart,
			Address shippingAddress,
			BillingAddress billingAddress,
			Payment payment,
			String shippingMethod,
			User user);
	
	Order findById(Long id);
}
