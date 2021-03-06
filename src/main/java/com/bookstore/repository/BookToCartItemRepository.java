package com.bookstore.repository;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.domain.BookToCartItem;
import com.bookstore.domain.CartItem;

@Transactional
public interface BookToCartItemRepository extends CrudRepository<BookToCartItem, Long> {

	void deleteByCartItem(CartItem cartItem);
	
}
