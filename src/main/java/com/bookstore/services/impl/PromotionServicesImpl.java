package com.bookstore.services.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bookstore.domain.Promotion;
import com.bookstore.domain.PromotionSub;
import com.bookstore.repository.PromoSubRepository;
import com.bookstore.repository.PromotionRepo;
import com.bookstore.services.PromotionService;

import net.bytebuddy.utility.RandomString;

@Service
public class PromotionServicesImpl implements PromotionService{

    @Autowired
    private PromotionRepo promoRepo;

    @Autowired
    private PromoSubRepository promoInfoRepository;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
   	PromotionServicesImpl promoService;




    public List<Promotion> findAll() {
        return (List<Promotion>) promoRepo.findAll();
    }



    public void sendPromo( Integer id)
    {
    	Promotion promo = promoRepo.findId(id);
    	List<PromotionSub> promoInfoList = promoInfoRepository.findAll();

        for (PromotionSub promotionInfo : promoInfoList) {
            try {
            	promotionInfo.setPromoName(promo.getPromoName());
            	promotionInfo.setPromoCode(promo.getPromoCode());
            	promotionInfo.setDiscount(promo.getDiscount());
            	promotionInfo.setStartDate(promo.getStartDate());
            	promotionInfo.setEndDate(promo.getEndDate());
            	promoInfoRepository.save(promotionInfo);
            	sendSuccessAddPromo(promotionInfo.getUser_email(), promo.getPromoCode(), promo.getPromoName());
            	
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        promo.setPromoSent("True");
        promoRepo.save(promo);
    }
    public void savePromotion(Promotion promo, String name, String code, String startDate,
                             String endDate, BigDecimal discount) {
        promo.setPromoName(name);
        promo.setPromoCode(code);
        promo.setStartDate(startDate);
        promo.setEndDate(endDate);
        promo.setDiscount(discount);
        promo.setPromoSent("False");

        promoRepo.save(promo);

        
    }

    public void sendSuccessAddPromo(String emailAddress, String promoCode, String promoName) throws UnsupportedEncodingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom("rarefinds.noreply@gmail.com", "RareFinds Support");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            helper.setTo(emailAddress);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        String subject = "New Promo Code";

        String content = "<p>Hello Valued Customer,</p>"
                + "<p>Your savings are right around the corner! Next time you visit the checkout page, make sure to apply this new promo code to get massive savings!</p>"
                + "<p> Promo Name: " + promoName + "</p>"
                + "<p> Promo Code: " + promoCode +  "</p>"
                + "<p>Enjoy the amazing discount!</p>";

        try {
            helper.setSubject(subject);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            helper.setText(content, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);
    }



	public Promotion findById(Integer id) {
		return promoRepo.findById(id).orElse(null);
	}



	@Override
	public void delete(Integer id) {
		promoRepo.deleteById(id);
	}



	public void deletePromo(Integer id, Model model) {
		Promotion promo = promoRepo.findId(id);
		String is_sent = promo.getPromoSent();
		if(is_sent.equals("True"))
		{
				model.addAttribute("is_sent", true);
		}
		else
		{
			promoService.delete(id);		

		}
	}
		
	
	public Promotion findByCode(String code) {
        return promoRepo.findByCode(code);
    }
	
	public PromotionSub findByUserCode(String code) {
        return ((PromotionRepo) promoRepo).findByUserCode(code);
    }
		
		public boolean isPromoValid(String code) {

	        Promotion p = this.findByCode(code);

	        if (p == null) {
	            return false;
	        }

	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        Date start, end;
	        Date todaysDate = new Date();

	        try {
	            start = df.parse(p.getStartDate());
	            end = df.parse(p.getEndDate());
	            // check if start date is before today
	            if (todaysDate.after(end) || todaysDate.before(start)) {
	                return false;
	            }
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        return true;
	}
	
	



}
