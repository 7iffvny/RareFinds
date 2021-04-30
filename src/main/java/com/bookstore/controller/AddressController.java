//package com.bookstore.controller;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import com.bookstore.domain.Address;
//import com.bookstore.services.UserServices;
//
//@Controller
//public class AddressController {
//	
//	
//	@Autowired
//	private UserServices service;
//	
//	@PostMapping("/add_address")
//	public String AddAddress(HttpServletRequest request, Address add, Model model) {
//		
//		String emailaddress = request.getParameter("emailaddress");
//		
//		String street = request.getParameter("street");
//		String zipcode = request.getParameter("zipcode");
//		String city = request.getParameter("city");
//		String country = request.getParameter("country");
//		String state = request.getParameter("state");
//		System.out.println(country);
//		
//		service.addAddress(add, street, zipcode, city, country, emailaddress, state);
//
//		return "addressInfo";
//	
//	
//		
//	
//	}
//}

