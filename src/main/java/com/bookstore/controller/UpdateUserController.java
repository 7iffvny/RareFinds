package com.bookstore.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.domain.CartItem;
import com.bookstore.domain.CustomerUserDetails;
import com.bookstore.domain.Order;
import com.bookstore.domain.User;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.domain.UserShipping;
import com.bookstore.repository.UserPaymentRepository;
import com.bookstore.repository.UserRepo;
import com.bookstore.services.CartItemService;
import com.bookstore.services.OrderService;
import com.bookstore.services.UserPaymentService;
import com.bookstore.services.UserServices;
import com.bookstore.services.UserShippingService;
import com.bookstore.utility.USConstants;


@Controller
public class UpdateUserController {
	
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private UserPaymentRepository payrepo;
	@Autowired
	private UserServices service;
	
	@Autowired
	private UserPaymentService userPaymentService;
	
	@Autowired
	private UserShippingService userShippingService;
	
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/manage_account")
	public String ShowAccount() {
//		String emailaddress = request.getParameter("emailaddress");
//		User details = service.listDetails(emailaddress);
//		model.addAttribute("userprofile", details);
		return "userdashboard";
	}
	
	@GetMapping("/edit_profile")
	public String ShowEditProfile() {
		return "editProfile";
	}
	@GetMapping("/manage_address")
	public String ShowAddress(Model model) {
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "addressInfo";
	}
	@GetMapping("/manage_promotion")
	public String ShowPromotion() {
		return "promotion";
	}
	@GetMapping("/manage_order")
	public String manageorder(Model model)
	{
			
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
				model.addAttribute("user", user);
				model.addAttribute("classActiveOrders", true);
				model.addAttribute("userPaymentList", user.getUserPaymentList());
				model.addAttribute("userShippingList", user.getUserShippingList());
				model.addAttribute("orderList", user.getOrderList());
				
				
				
			
		return "orderHistory";
	}

	@RequestMapping("/orderDetail")
	public String orderDetail(
			@RequestParam("id") Long orderId,
			 Model model
			){
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		Order order = orderService.findById(orderId);
		
		if(order.getUser().getId()!=user.getId()) {
			return "badRequestPage";
		} else {
			List<CartItem> cartItemList = cartItemService.findByOrder(order);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("user", user);
			model.addAttribute("order", order);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			
			UserShipping userShipping = new UserShipping();
			model.addAttribute("userShipping", userShipping);
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveOrders", true);
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("displayOrderDetail", true);
			
			return "orderHistory";
		}
	}

	@GetMapping("/manage_payment")
	public String ShowPayment(Model model) {
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "paymentInformation";
	}
	
	@GetMapping("/manage_password")
	public String ShowPassword() {
		return "changePassword";
	} 
	
	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(
			Model model
			){
		
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		long count = payrepo.countById(user.getId());
		System.out.println(count);
		if (count == 3)
		{
			model.addAttribute("message", "cannot add more than 3 cards, please delete a payment method");
			return "paymentInformation";
		}
		model.addAttribute("user", user);
		
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();
		
		
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		
		return "paymentInformation";
		
	}
	
	
	@RequestMapping("/listOfCreditCards")
	public String listOfCreditCards(
			Model model, HttpServletRequest request
			) {
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		/*model.addAttribute("orderList", user.orderList());*/
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "paymentInformation";
	}
	
	
	@RequestMapping(value="/addNewCreditCard", method=RequestMethod.POST)
	public String addNewCreditCard(
			@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling,
			Model model
			){
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		String number = userPayment.getCardNumber();

		String lastFourDigits = "";
		lastFourDigits = number.substring(number.length() - 4);
		userPayment.setLastFourDigits("**** **** **** " + lastFourDigits);

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPayment = passwordEncoder.encode(number);
		userPayment.setCardNumber(encodedPayment);
		payrepo.save(userPayment);
		User user = repo.findByEmail(emailaddress);
		
		
		service.updateUserBilling(userBilling, userPayment, user);
		
		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "paymentInformation";
	}
	
	@RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(
			Model model, HttpServletRequest request
			) {
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		/*model.addAttribute("orderList", user.orderList());*/
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "addressInfo";
	}
		
	
	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(
			Model model
			){
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		model.addAttribute("user", user);
		
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		
		UserShipping userShipping = new UserShipping();
		
		model.addAttribute("userShipping", userShipping);
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		return "addressInfo";
	}
	
	@RequestMapping(value="/addNewShippingAddress", method=RequestMethod.POST)
	public String addNewShippingAddressPost(
			@ModelAttribute("userShipping") UserShipping userShipping,
			 Model model
			){
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		service.updateUserShipping(userShipping, user);
		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		return "addressInfo";
	}
	
	@RequestMapping("/updateCreditCard")
	public String updateCreditCard(
			@ModelAttribute("id") Long creditCardId, Model model
			) {
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		
		if(user.getId() != userPayment.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			UserBilling userBilling = userPayment.getUserBilling();
			model.addAttribute("userPayment", userPayment);
			model.addAttribute("userBilling", userBilling);
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("addNewCreditCard", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			
			return "paymentInformation";
		}
	}
	
	@RequestMapping("/updateUserShipping")
	public String updateUserShipping(
			@ModelAttribute("id") Long shippingAddressId, Model model
			) {
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		UserShipping userShipping = userShippingService.findById(shippingAddressId);
		
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			
			model.addAttribute("userShipping", userShipping);
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("listOfCreditCards", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			
			return "addressInfo";
		}
	}
	
	@RequestMapping(value="/setDefaultPayment", method=RequestMethod.POST)
	public String setDefaultPayment(
			@ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Model model
			) {
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		service.setUserDefaultPayment(defaultPaymentId, user);
		
		model.addAttribute("user", user);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		
		return "paymentInformation";
	}
	
	@RequestMapping(value="/setDefaultShippingAddress", method=RequestMethod.POST)
	public String setDefaultShippingAddress(
			@ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Model model
			) {
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		service.setUserDefaultShipping(defaultShippingId, user);
		
		model.addAttribute("user", user);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		
		return "addressInfo";
	}
	
	@RequestMapping("/removeCreditCard")
	public String removeCreditCard(
			@ModelAttribute("id") Long creditCardId, Model model
			){
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		
		if(user.getId().equals(userPayment.getUser().getId())) {
			model.addAttribute("user", user);
			userPaymentService.removeById(creditCardId);
			
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			return "paymentInformation";
		} else {
			return "badRequestPage";
			
			
			
		}
	}
	
	@RequestMapping("/removeUserShipping")
	public String removeUserShipping(
			@ModelAttribute("id") Long userShippingId, Model model
			){
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		UserShipping userShipping = userShippingService.findById(userShippingId);
		
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			
			userShippingService.removeById(userShippingId);
			
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveShipping", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			
			return "addressInfo";
		}
	}
	@PostMapping("/updateUser")
	public String updateUser(HttpServletRequest request, Model model) {
		String emailaddress = request.getParameter("emailaddress");
		String username = request.getParameter("username");
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String phonenumber = request.getParameter("phonenumber");

    	service.updateUserDetails(username, phonenumber, firstname, lastname, emailaddress);
    	
    	
    	return "userdashboard";
    }
	
	
}
