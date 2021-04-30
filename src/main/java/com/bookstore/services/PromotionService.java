package com.bookstore.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.bookstore.domain.Promotion;
import com.bookstore.domain.PromotionSub;

import java.util.List;
import java.util.Optional;

public interface PromotionService {

    List<Promotion> findAll();
    
    Promotion findByCode(String code);

    boolean isPromoValid(String code);

//    Promotion findByName(String name);
    Promotion findById (Integer id);
    
    void delete(Integer id);

	PromotionSub findByUserCode(String promoCode);
}
