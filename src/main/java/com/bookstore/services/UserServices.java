package com.bookstore.services;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.domain.Address;
import com.bookstore.domain.BillingAddress;
import com.bookstore.domain.Payment;
import com.bookstore.domain.PromotionSub;
import com.bookstore.domain.ShoppingCart;
import com.bookstore.domain.User;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.domain.UserShipping;
import com.bookstore.repository.AddressRepo;
import com.bookstore.repository.PaymentRepo;
import com.bookstore.repository.PromoSubRepository;
import com.bookstore.repository.UserPaymentRepository;
import com.bookstore.repository.UserRepo;
import com.bookstore.repository.UserShippingRepository;
import com.bookstore.utility.UserNotFoundException;

import net.bytebuddy.utility.RandomString;

@Service
public class UserServices {

	@Autowired
	private UserRepo repo;

	
	
	@Autowired
	private UserPaymentRepository userPaymentRepository;
	
	@Autowired
	private UserShippingRepository userShippingRepository;
	@Autowired
	private PromoSubRepository promoRepo;
	
	@Autowired
	private AddressRepo addRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JavaMailSender mailSender;
	
	public User listDetails(String emailaddress) {
		return repo.findByEmail(emailaddress);
	}
	
	public List<User> findAll() {
		return (List<User>) repo.findAll();
	}
	
	@Transactional
	public void register(User user, String siteURL ) 
	
		
			throws UnsupportedEncodingException, MessagingException {
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setEnabled(false);
		user.setUser_type("user");
		user.setUser_type("notenabled");
		
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setUser(user);
		user.setShoppingCart(shoppingCart);
		
		user.setUserShippingList(new ArrayList<UserShipping>());
		user.setUserPaymentList(new ArrayList<UserPayment>());
		repo.save(user);
		
		sendVerificationEmail(user, siteURL);
	}
	
	private void sendVerificationEmail(User user, String siteURL) 
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmailaddress();
		String fromAddress = "rarefinds.noreply@gmail.com";
		String senderName = "RareFinds";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>"
				+ "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
				+ "Thank you,<br>"
				+ "Rare Finds Online BookStore.";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", user.getFullName());
		String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
		
		content = content.replace("[[URL]]", verifyURL);
		
		helper.setText(content, true);
		
		mailSender.send(message);
		
		System.out.println("Email has been sent");
	}
	
	public boolean verify(String verificationCode) {
		User user = repo.findByVerificationCode(verificationCode);
		
		if (user == null || user.isEnabled()) {
			return false;
		} else {
			user.setVerificationCode(null);
			user.setEnabled(true);
			user.setUser_type("user");
			user.setUser_status("enabled");
			
			repo.save(user);
			
			return true;
		}
		
	}
	
	
	public void updateResetPasswordToken(String token, String emailaddress) throws UserNotFoundException
	{
		User user = repo.findByEmail(emailaddress);
		
		if(user != null)
		{
			user.setResetPasswordToken(token);
			repo.save(user);
		}else {
			throw new UserNotFoundException("could not find any User with email  " + emailaddress);
		}
	}
	
	public User get(String resetPasswordToken)
	{
		return repo.findByResetPasswordToken(resetPasswordToken);
	}
	
	public User getEmail(String email)
	{
		return repo.findByEmail(email);
	}
	public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
         
        user.setResetPasswordToken(null);
        repo.save(user);
    }
	
	public User updateUserDetails(String username, String phonenumber, String firstname, String lastname, String emailaddress)
	{
		User user = repo.findByEmail(emailaddress);
		if(user != null)
		{
			user.setFirstname(firstname);
			user.setLastname(lastname);
			user.setPhonenumber(phonenumber);
			user.setUsername(username);
			repo.save(user);
			try {
				sendSuccessChangeProfile(emailaddress);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		}
		else
		{
			System.out.println("User not found"  + emailaddress);
		
		}
		return user;
	}
	public void sendSuccessChangeProfile(String emailaddress) throws UnsupportedEncodingException{
   	 
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
			helper.setTo(emailaddress);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		String subject = "Your Profile was sucessfully updated";
	     
	    String content = "<p>Hello,</p>"
	            + "<p>Thanks for visiting RareFinds! Per your requst, we have successfully updated ypur profile information.</p>"
	            + "<p>Visit Your Account at RareFinds.com to view your orders, make changes to any order that hasn't yet entered the shipping process, update your subscriptions, and much more.</p>"
	            + "<p>Should you need to contact us for any reason, please know that we can give  "
	            + "out order information only to the name and e-mail address associated with your account. Thanks again for shopping with us.</p>";
	     
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
	
	public void sendSuccessAdd(String emailaddress) throws UnsupportedEncodingException{
	   	 
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
			helper.setTo(emailaddress);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		String subject = "Your Address was added sucessfully";
	     
	    String content = "<p>Hello,</p>"
	            + "<p>Thanks for visiting RareFinds! Your address has been added successfully.</p>"
	            + "<p>Visit Your Account at RareFinds.com to view your orders, make changes to any order that hasn't yet entered the shipping process, update your subscriptions, and much more.</p>"
	            + "<p>Should you need to contact us for any reason, please know that we can give  "
	            + "out order information only to the name and e-mail address associated with your account. Thanks again for shopping with us.</p>";
	     
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

//	public void addAddress(Address address, String street, String zipcode, String city, String country, String emailaddress, String state) {
//		
//		address.setUser_email(emailaddress);
//		address.setAddress(street);
//		address.setZipcode(zipcode);
//		address.setState(state);
//		address.setCountry(country);
//		address.setCity(city);
//		addRepo.save(address);
//		try {
//			sendSuccessAdd(emailaddress);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	}
	
	

	public void addPromo(PromotionSub promotion, String phonenumber, String emailaddress) {
		promotion.setUser_email(emailaddress);
		promotion.setUser_phonenumber(phonenumber);
		promoRepo.save(promotion);
		try {
			sendSuccessPromo(emailaddress);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendSuccessPromo(String emailaddress) throws UnsupportedEncodingException{
	   	 
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
			helper.setTo(emailaddress);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		String subject = "You have successfully subscribed for promotions";
	     
	    String content = "<p>Hello,</p>"
	            + "<p>Buckel up, because you are about to be mind blown with the amazing discounts. Infacts your friends are going to be jealous :)</p>"
	            + "<p>Visit Your Account at RareFinds.com to make order and apply all promo codes.</p>"
	            + "<p>Should you need to contact us for any reason, please know that we can give  "
	            + "out order information only to the name and e-mail address associated with your account. Thanks again for shopping with us.</p>";
	     
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
	
	 public void changeUserStatus(String email) {

			User user = repo.findByEmail(email);

			if (user.getUser_status().equals("enabled") ) {
				user.setUser_status("Suspended");
			} else if (user.getUser_status().equals("Suspended")) {
				user.setUser_status("enabled");
			}

			repo.save(user);

		}
	 
		public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
			
			userPayment.setUser(user);
			userPayment.setUserBilling(userBilling);
			userPayment.setDefaultPayment(true);
			userBilling.setUserPayment(userPayment);
			user.getUserPaymentList().add(userPayment);
			repo.save(user);
			try {
				sendSuccessPay(user.getEmailaddress());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		public void sendSuccessPay(String emailaddress) throws UnsupportedEncodingException{
		   	 
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
				helper.setTo(emailaddress);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			String subject = "Your Payment method was added sucessfully";
		     
		    String content = "<p>Hello,</p>"
		            + "<p>Thanks for visiting RareFinds! Your Payment method has been added successfully.</p>"
		            + "<p>Visit Your Account at RareFinds.com to view your orders, make changes to any order that hasn't yet entered the shipping process, update your subscriptions, and much more.</p>"
		            + "<p>Should you need to contact us for any reason, please know that we can give  "
		            + "out order information only to the name and e-mail address associated with your account. Thanks again for shopping with us.</p>";
		     
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
		
		public void updateUserShipping(UserShipping userShipping, User user){
			userShipping.setUser(user);
			userShipping.setUserShippingDefault(true);
			user.getUserShippingList().add(userShipping);
			repo.save(user);
			try {
				sendSuccessAdd(user.getEmailaddress());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		public void setUserDefaultPayment(Long userPaymentId, User user) {
			List<UserPayment> userPaymentList = (List<UserPayment>) userPaymentRepository.findAll();
			
			for (UserPayment userPayment : userPaymentList) {
				if(userPayment.getId() == userPaymentId) {
					userPayment.setDefaultPayment(true);
					userPaymentRepository.save(userPayment);
				} else {
					userPayment.setDefaultPayment(false);
					userPaymentRepository.save(userPayment);
				}
			}
		}
		
		public void setUserDefaultShipping(Long userShippingId, User user) {
			List<UserShipping> userShippingList = (List<UserShipping>) userShippingRepository.findAll();
			
			for (UserShipping userShipping : userShippingList) {
				if(userShipping.getId() == userShippingId) {
					userShipping.setUserShippingDefault(true);
					userShippingRepository.save(userShipping);
				} else {
					userShipping.setUserShippingDefault(false);
					userShippingRepository.save(userShipping);
				}
			}
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
