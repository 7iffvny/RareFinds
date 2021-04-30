package com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstore.domain.Promotion;
import com.bookstore.domain.PromotionSub;

public interface PromoSubRepository extends JpaRepository<PromotionSub, Integer> {
	
	@Query("SELECT pr FROM PromotionSub pr WHERE pr.user_email = ?1")
	public PromotionSub findByEmail(String email);
	
	@Query("SELECT pr FROM PromotionSub pr WHERE pr.user_email = ?1")
	List <PromotionSub> findByEmails(String email);

	@Query("SELECT p FROM PromotionSub p WHERE p.promoCode = ?1")
    public PromotionSub findByCode(String code);
	
	public PromotionSub findById(Long promoId);
}
