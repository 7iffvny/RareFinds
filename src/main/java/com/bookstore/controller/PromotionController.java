package com.bookstore.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.bookstore.domain.PromotionSub;
import com.bookstore.repository.PromoSubRepository;
import com.bookstore.services.UserServices;

@Controller
public class PromotionController {

	@Autowired
	private UserServices service;
	
	@Autowired
	private PromoSubRepository promoRepo;
	@PostMapping("/add_promotion")
	public String updateUser(HttpServletRequest request, Model model, PromotionSub promotion) {
		String emailaddress = request.getParameter("emailaddress");
		String phonenumber = request.getParameter("phonenumber");
		

    	service.addPromo(promotion, phonenumber, emailaddress);
    	
    	
    	return "userdashboard";
    }
	
	@PostMapping("/unsubscribe")
	public String Unsubscribe(HttpServletRequest request, Model model, PromotionSub promotion) {
		String emailaddress = request.getParameter("userEmail");
		service.removePromo(promotion, emailaddress);
		return "userdashboard";
	}

	
	public void removePromo(PromotionSub promotion, String emailaddress) {
		List<PromotionSub> list = promoRepo.findAll();

		for (PromotionSub ps : list) {
			if (ps.getUser_email().equals(emailaddress)) {
				System.out.println("Promo email = " + ps.getUser_email());
				promoRepo.delete(ps);
			}
		}
	}
}
