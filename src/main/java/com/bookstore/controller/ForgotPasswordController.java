package com.bookstore.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.bookstore.domain.User;
import com.bookstore.repository.UserRepo;
import com.bookstore.services.UserServices;
import com.bookstore.utility.UserNotFoundException;
import com.bookstore.utility.Utility;

import net.bytebuddy.utility.RandomString;

@Controller
public class ForgotPasswordController {

	@Autowired
    private JavaMailSender mailSender;
     
	@Autowired
	private UserRepo repo;
	
    @Autowired
    private UserServices userService;
    
    @Autowired
	private PasswordEncoder passwordEncoder;
     
    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
    	return "forgotPassword";
    }
 
    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model)  {
    	String emailaddress = request.getParameter("emailaddress");
    	
    	String token = RandomString.make(45);
    	try {
			userService.updateResetPasswordToken(token, emailaddress);
			String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token="  + token;
			
			sendEmail(emailaddress, resetPasswordLink);
			model.addAttribute("message", "we have sent a reset link to yor email");
			
		} catch (UserNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		}catch (UnsupportedEncodingException e) {
			model.addAttribute("error", "Error while sending email" );
		}
    	
    	return "forgotPassword"; 
    }
    
     
    public void sendEmail(String emailaddress, String resetPasswordLink) throws UnsupportedEncodingException{
 
    	MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		try {
			helper.setFrom("Janeodum41@gmail.com", "RareFinds Support");
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
		String subject = "Here's the link to reset your password";
	     
	    String content = "<p>Hello,</p>"
	            + "<p>You have requested to reset your password.</p>"
	            + "<p>Click the link below to change your password:</p>"
	            + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
	            + "<br>"
	            + "<p>Ignore this email if you do remember your password, "
	            + "or you have not made the request.</p>";
	     
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
     
     
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
    	User user = userService.get(token);
    	
    	if (user == null)
    	{
    		model.addAttribute("message", "Invalid Token");
    		
    		return "message";
    	}
    	model.addAttribute("token", token);
    	return "resetPassword";
    }
     
    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
    	String token = request.getParameter("token");
    	String password = request.getParameter("password");
    	User user = userService.get(token);
    	model.addAttribute("title", "Reset Password");
    	if (user == null)
    	{
    		
    		model.addAttribute("message", "Invalid Token");
    		
    		return "message";
    	}else {
    		userService.updatePassword(user, password);
    		return "passwordMessageSuccess";
    	}
    }
    
    @PostMapping("/change_password")
    public String processChangePassword(HttpServletRequest request, Model model) {
    	String password = request.getParameter("password");
    	String emailaddress = request.getParameter("emailaddress");
    	String oldPassword = request.getParameter("oldpassword");
    	User user = repo.findByEmail(emailaddress);
    	
    	
    	model.addAttribute("title", "Reset Password");
    	if (user == null)
    	{
    		
    		model.addAttribute("message", "Invalid");
    		
    		return "message";
    	}
    	if (passwordEncoder.matches(oldPassword, user.getPassword()) )
    	{
    		userService.updatePassword(user, password);
    		try {
				sendSuccessChangeEmail(emailaddress);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
    	}
    	else
    	{
    		model.addAttribute("dbPass", true);
    	}
    		return "changePassword";
    	
    }
    
    public void sendSuccessChangeEmail(String emailaddress) throws UnsupportedEncodingException{
    	 
    	MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		try {
			helper.setFrom("Janeodum41@gmail.com", "RareFinds Support");
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
	            + "<p>Thanks for visiting RareFinds! Per your requst, we have successfully changed your password.</p>"
	            + "<p>Visit Your Account at RareFinds.com to view your orders, make changes to any order that hasn't yet entered the shipping process, update your subscriptions, and much more.</p>"
	            
	            + "<br>"
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
}
