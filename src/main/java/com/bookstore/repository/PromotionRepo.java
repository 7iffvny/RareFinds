package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstore.domain.Promotion;
import com.bookstore.domain.PromotionSub;

import java.util.List;

public interface PromotionRepo extends JpaRepository<Promotion, Integer> {

    @Query("SELECT p FROM Promotion p WHERE p.id = ?1")
    public Promotion findId(Integer id);
    
    @Query("SELECT p FROM Promotion p WHERE p.promoCode = ?1")
    public Promotion findByCode(String code);

    @Query("SELECT p FROM PromotionSub p WHERE p.promoCode = ?1")
	public PromotionSub findByUserCode(String code);

}
