//package com.bookstore.controller;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import com.bookstore.domain.BillingAddress;
//import com.bookstore.domain.Payment;
//import com.bookstore.services.UserServices;
//
//@Controller
//public class PaymentController {
//
//	private Integer count = 0;
//	
//	
//	@Autowired
//	private UserServices service;
//	
////	@GetMapping("/add_payment")
////    public String showPayment() {
////    	return "paymentInformation";
////    }
//	
//	@PostMapping("/add_payment")
//	public String AddPayment(HttpServletRequest request,BillingAddress bill, Payment payment, Model model) {
//		
//		String emailaddress = request.getParameter("emailaddress");
//		
//		String cardnumber = request.getParameter("cardnumber");
//		String cardholder = request.getParameter("cardholder");
//		String month = request.getParameter("month");
//		String year = request.getParameter("year");
//		String billingstreet1 = request.getParameter("street1");
//		String billingstreet2 = request.getParameter("street2");
//		String zipcode = request.getParameter("zipcode");
//		String city = request.getParameter("city");
//		String country = request.getParameter("country");
//		String state = request.getParameter("state");
//		System.out.println(country);
//		if(count < 3)
//		{
//			service.addPayment(bill, payment,cardnumber, cardholder, month, year,billingstreet1, billingstreet2, zipcode, city, country, emailaddress, state);
//			count += 1;
//
//			return "paymentInformation";
//		}
//		else
//		{
//			model.addAttribute("message", "cannot add more than 3 cards");
//			return "paymentInformation";
//		}
//
//		
//	}
//}
