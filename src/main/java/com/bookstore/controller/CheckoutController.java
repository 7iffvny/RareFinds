package com.bookstore.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.NonUniqueResultException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.domain.BillingAddress;
import com.bookstore.domain.CartItem;
import com.bookstore.domain.CustomerUserDetails;
import com.bookstore.domain.Order;
import com.bookstore.domain.Payment;
import com.bookstore.domain.Promotion;
import com.bookstore.domain.PromotionSub;
import com.bookstore.domain.Address;
import com.bookstore.domain.ShoppingCart;
import com.bookstore.domain.User;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.domain.UserShipping;
import com.bookstore.repository.PromoSubRepository;
import com.bookstore.repository.ShoppingCartRepository;
import com.bookstore.repository.UserRepo;
import com.bookstore.services.BillingAddressService;
import com.bookstore.services.CartItemService;
import com.bookstore.services.OrderService;
import com.bookstore.services.PaymentService;
import com.bookstore.services.PromotionService;
import com.bookstore.services.ShoppingCartService;
import com.bookstore.services.AddressService;
import com.bookstore.services.UserPaymentService;
import com.bookstore.services.UserShippingService;
import com.bookstore.utility.MailConstructor;
import com.bookstore.utility.USConstants;

@Controller
public class CheckoutController {

	private Address shippingAddress = new Address();
	private BillingAddress billingAddress = new BillingAddress();
	private Payment payment = new Payment();
	
	private String usedPromoCode = null;
	
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;

	
	@Autowired
	private MailConstructor mailConstructor;
	
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private AddressService shippingAddressService;
	
	@Autowired
	private BillingAddressService billingAddressService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private UserShippingService userShippingService;
	
	@Autowired
	private UserPaymentService userPaymentService;
	
	@Autowired
	private PromotionService promoService;
	
	@Autowired
	private ShoppingCartRepository shopRepo;
	
	@Autowired
    private PromoSubRepository promoRepo;
	
	
	@RequestMapping("/checkout")
	public String checkout(
			@RequestParam("id") Long cartId,
			@RequestParam(value="missingRequiredField", required=false) boolean missingRequiredField,
			Model model
			){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "userLogin";
		}

		
		else if(principal != null )
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		if(cartId != user.getShoppingCart().getId()) {
			return "badRequestPage";
		}
		List<PromotionSub> promoList = promoRepo.findByEmails(emailaddress);
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		ShoppingCart shoppingCart = user.getShoppingCart();
		if(cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppintCart/cart";
		}
		
		for (CartItem cartItem : cartItemList) {
			if(cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}
//		if(shoppingCart.getDiscount() == null)
//		{
//			shoppingCart.setDiscount(setPromo);
//			
//		}
		shoppingCart.setPromoAdded("False");
		shopRepo.save(shoppingCart);
		if (promoList.size() == 0) {
			model.addAttribute("emptyPromotList", true);
		} else {
			model.addAttribute("emptyPromoList", false);
		}
		
		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		
		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);
		
		if (userPaymentList.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		} else {
			model.addAttribute("emptyPaymentList", false);
		}
		
		if (userShippingList.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}
		
		
		
		for(UserShipping userShipping : userShippingList) {
			if(userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}
		
		for (UserPayment userPayment : userPaymentList) {
			if(userPayment.isDefaultPayment()) {
				paymentService.setByUserPayment(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}
		
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());
		model.addAttribute("promoList", promoList);
		model.addAttribute("promoadded", false);
		

		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		
		model.addAttribute("classActiveShipping", true);
		
		if(missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}
		for(PromotionSub pm : promoList)
		{
			pm.setIs_used(false);
			promoRepo.save(pm);
		}
		return "checkout";
		
	}
	
	
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutPost(@ModelAttribute("shippingAddress") Address shippingAddress,
			@ModelAttribute("billingAddress") BillingAddress billingAddress, @ModelAttribute("payment") Payment payment,
			@ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
			@ModelAttribute("shippingMethod") String shippingMethod, Model model) {
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		ShoppingCart shoppingCart = repo.findByEmail((emailaddress)).getShoppingCart();

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);

		if (billingSameAsShipping.equals("true")) {
			billingAddress.setBillingName(shippingAddress.getShippingname());
			billingAddress.setBillingstreet1(shippingAddress.getShippingstreet1());
			billingAddress.setBillingstreet2(shippingAddress.getShippingstreet2());
			billingAddress.setCity(shippingAddress.getCity());
			billingAddress.setState(shippingAddress.getState());
			billingAddress.setCountry(shippingAddress.getCountry());
			billingAddress.setBillingzipcode(shippingAddress.getZipcode());
		}

		if (shippingAddress.getShippingstreet1().isEmpty() || shippingAddress.getCity().isEmpty()
				|| shippingAddress.getState().isEmpty()
				|| shippingAddress.getShippingname().isEmpty()
				|| shippingAddress.getZipcode().isEmpty() || payment.getCardnumber().isEmpty()
				|| billingAddress.getBillingstreet1().isEmpty()
				|| billingAddress.getCity().isEmpty() || billingAddress.getState().isEmpty()
				|| billingAddress.getBillingName().isEmpty()
				|| billingAddress.getBillingzipcode().isEmpty())
			return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";
		
		
		User user = repo.findByEmail(emailaddress);
		
		Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, payment, shippingMethod, user);
		
		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));
		
		shoppingCartService.clearShoppingCart(shoppingCart);
		List<PromotionSub> promoList = promoRepo.findByEmails(emailaddress);
		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;
		
		if (shippingMethod.equals("groundShipping")) {
			estimatedDeliveryDate = today.plusDays(5);
		} else {
			estimatedDeliveryDate = today.plusDays(3);
		}
		
		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);
		
		model.addAttribute("emptyShippingList", false);
		
		for(PromotionSub pm : promoList)
		{
			if (pm.getIs_used() == true);
			{
				promoRepo.deleteById(pm.getId());
			}
		}
		
		return "orderSubmittedPage";
	}
	
	@RequestMapping("/addPromo")
	public String addPromo(
			@RequestParam("id") Long cartId,
			HttpServletRequest request, Model model)
	{
		
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		List<PromotionSub> promoList = promoRepo.findByEmails(emailaddress);

		String promoCode = request.getParameter("promoCode");
		PromotionSub promoSub = promoService.findByUserCode(promoCode);
		Promotion promosub = promoService.findByCode(promoCode);
		ShoppingCart shoppingCart = user.getShoppingCart();
		if(shoppingCart.getPromoAdded().equals("True"))
		{
			
			model.addAttribute("cannotApply", "cannot apply more than one promotion");
			
			return "checkout";
		}
		try{
				if(promosub.getPromoCode().equals(promoCode) && promoList.size() != 0)
		
				{
					shoppingCart.setGrandTotal(shoppingCart.getGrandTotal().subtract(shoppingCart.getGrandTotal().multiply(promosub.getDiscount())));
					shopRepo.save(shoppingCart);
					promoSub.setIs_used(true);
					promoRepo.save(promoSub);
					model.addAttribute("message", "Promotion has been applied to cart");
					model.addAttribute("promoadded", true);
				}
				else if(promoList.size() == 0 && promosub.getPromoCode().equals(promoCode)) {
					shoppingCart.setGrandTotal(shoppingCart.getGrandTotal().subtract(shoppingCart.getGrandTotal().multiply(promosub.getDiscount())));
					shopRepo.save(shoppingCart);
					PromotionSub sub =new PromotionSub();
					sub.setDiscount(promosub.getDiscount());
					sub.setEndDate(promosub.getEndDate());
					sub.setPromoCode(promosub.getPromoCode());
					sub.setPromoName(promosub.getPromoName());
					sub.setIs_used(true);
					sub.setUser_email(user.getEmailaddress());
					sub.setUser_phonenumber(user.getPhonenumber());
					promoRepo.save(sub);
					model.addAttribute("message", "Promotion has been applied to cart");
					model.addAttribute("promoadded", true);
					
				}
				else
				{
					model.addAttribute("invalid", "promo Code is invalid");
					model.addAttribute("promoadded", false);
				}
		}catch (NonUniqueResultException nre)
		{
			model.addAttribute("cannotApply", "cannot apply already applied promo");
			model.addAttribute("promoadded", false);
		}catch (NullPointerException Np)
		{
			model.addAttribute("invalid", "PromoCode is invalid");
			model.addAttribute("promoadded", false);
		}
		
		//!promosub.getPromoCode().equals(promoCode) ||
		
		
		if(cartId != user.getShoppingCart().getId()) {
			return "badRequestPage";
		}
		//bigdecimal setPromo =  1.0;
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		if(cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppintCart/cart";
		}
		
		for (CartItem cartItem : cartItemList) {
			if(cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}
//		if(shoppingCart.getDiscount() == null)
//		{
//			shoppingCart.setDiscount(setPromo);
//			
//		}
		shoppingCart.setPromoAdded("False");
		shopRepo.save(shoppingCart);
		if (promoList.size() == 0) {
			model.addAttribute("emptyPromotList", true);
		} else {
			model.addAttribute("emptyPromoList", false);
		}
		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		
		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);
		
		if (userPaymentList.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		} else {
			model.addAttribute("emptyPaymentList", false);
		}
		
		if (userShippingList.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}
		
		
		
		for(UserShipping userShipping : userShippingList) {
			if(userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}
		
		for (UserPayment userPayment : userPaymentList) {
			if(userPayment.isDefaultPayment()) {
				paymentService.setByUserPayment(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}
		
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());
		model.addAttribute("promoList", promoList);
		
		

		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		
		model.addAttribute("classActiveShipping", true);
		
		
		return "checkout";
	}
	
	
	
	
	
	
	@RequestMapping("/setPromo")
	public String setPromo(
			@RequestParam("promoCode") String promoCode,
			@RequestParam(value="missingRequiredField", required=false) boolean missingRequiredField,
			 Model model
			) {
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		List<PromotionSub> promoList = promoRepo.findByEmails(emailaddress);
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		
		if(cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppintCart/cart";
		}
		PromotionSub promoSub = promoService.findByUserCode(promoCode);
		Promotion promosub = promoService.findByCode(promoCode);
		ShoppingCart shoppingCart = user.getShoppingCart();
		if(shoppingCart.getPromoAdded().equals("True"))
		{
			
			model.addAttribute("cannotApply", "cannot apply more than one promotion");
			
			return "checkout";
		}
		
		if(promosub.getPromoCode().equals(promoCode) && shoppingCart.getPromoAdded().equals("False"))
		{
			shoppingCart.setGrandTotal(shoppingCart.getGrandTotal().subtract(shoppingCart.getGrandTotal().multiply(promosub.getDiscount())));
			shoppingCart.setPromoAdded("True");
			shopRepo.save(shoppingCart);
			model.addAttribute("promoadded", true);
			promoSub.setIs_used(true);
			promoRepo.save(promoSub);
			model.addAttribute("message", "Promotion has been applied to cart");
			
		}
		else {
			promoSub.setIs_used(false);
			promoRepo.save(promoSub);
			model.addAttribute("promoadded", false);
			model.addAttribute("invalid", "promo Code is invalid");
						
		}
		
		
		for(PromotionSub pm : promoList)
		{
			if (promoService.isPromoValid(pm.getPromoCode()) == false);
			{
				promoRepo.deleteById(pm.getId());
				promoRepo.save(pm);
			}
		}
		
		
		for (CartItem cartItem : cartItemList) {
			if(cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}
		
	
		if (promoList.size() == 0) {
			model.addAttribute("emptyPromotList", true);
		} else {
			model.addAttribute("emptyPromoList", false);
		}
		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		
		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);
		
		if (userPaymentList.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		} else {
			model.addAttribute("emptyPaymentList", false);
		}
		
		if (userShippingList.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}
		
		
		for(UserShipping userShipping : userShippingList) {
			if(userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}
		
		for (UserPayment userPayment : userPaymentList) {
			if(userPayment.isDefaultPayment()) {
				paymentService.setByUserPayment(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}
		shoppingCart.setDiscount(promosub.getDiscount());
		
		model.addAttribute("promo", promosub);
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());
		model.addAttribute("promoList", promoList);
		shoppingCart.setPromoAdded("True");
		shopRepo.save(shoppingCart);
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		
		model.addAttribute("classActiveShipping", true);
		
		

		return "checkout";
	}
	
	
	
	
	
	
	
	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(
			@RequestParam("userShippingId") Long userShippingId,
			 Model model
			) {
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		UserShipping userShipping = userShippingService.findById(userShippingId);
		
		if(userShipping.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
			
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();
			
			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);
			
			model.addAttribute("shippingAddress", shippingAddress);
			
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("promoadded", false);
			
			if (userPaymentList.size() == 0) {
				model.addAttribute("emptyPaymentList", true);
			} else {
				model.addAttribute("emptyPaymentList", false);
			}
			
			
			
			return "checkout";
		}
	}
	
	
	
	
	
	
	
	
	@RequestMapping("/setPaymentMethod")
	public String setPaymentMethod(
			@RequestParam("userPaymentId") Long userPaymentId,
			 Model model
			) {
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		UserBilling userBilling = userPayment.getUserBilling();
		
		if(userPayment.getUser().getId() != user.getId()){
			return "badRequestPage";
		} else {
			paymentService.setByUserPayment(userPayment, payment);
			
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
			
			billingAddressService.setByUserBilling(userBilling, billingAddress);
			
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();
			
			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);
			
			model.addAttribute("shippingAddress", shippingAddress);
			
			model.addAttribute("classActivePayment", true);
			
			
			model.addAttribute("emptyPaymentList", false);
			model.addAttribute("promoadded", false);
			
			
			if (userShippingList.size() == 0) {
				model.addAttribute("emptyShippingList", true);
			} else {
				model.addAttribute("emptyShippingList", false);
			}
			
			return "checkout";
		}
	}
	
}
