package com.bookstore.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.bookstore.domain.Book;
import com.bookstore.domain.Promotion;
import com.bookstore.domain.User;
import com.bookstore.services.BookService;
import com.bookstore.services.UserServices;
import com.bookstore.services.impl.PromotionServicesImpl;

@Controller
public class AdminController {

	@Autowired
	UserServices userServices;

    @Autowired
    private BookService bookService;
    
    @Autowired
	PromotionServicesImpl promoService;
	@GetMapping("/edit_admin")
	public String EditAdmin()
	{
		return "editAdmin";
	}
	
	@GetMapping("/view_admin")
	public String ViewAdmin()
	{
		return "dashboard";
	}
	
	@GetMapping("/manage_promotions")
	public String ManagePromo(Model model)
	{
		List<Promotion> promoList = promoService.findAll();
		model.addAttribute("promoList", promoList);
		return "managePromotions";
	}

	@GetMapping("/add_promotions")
	public String AddPromo(Model model)
	{
		return "addPromotion";
	}
	
	
	
	@PostMapping("/delete_promo")
	public String DelPromo(HttpServletRequest request, Promotion promo, Model model)
	{
		Integer id = Integer.parseInt(request.getParameter("promoId"));
		promoService.deletePromo(id, model);
		List<Promotion> promoList = promoService.findAll();
		model.addAttribute("promoList", promoList);
		return "managePromotions";
	}

	@GetMapping("/manage_users")
	public String ManageUsers(Model model)
	{
		List<User> userList = userServices.findAll();
		model.addAttribute("userList", userList);
		return "manageUsers";
	}

	@GetMapping("/manage_books")
	public String ManageBooks(Model model)
	{
        List<Book> bookList = bookService.findAll();
//        for (Book book : bookList)
//            System.out.println("Promotion: " + book.getName() + ", " + book.getTitle()
//                    + ", " + book.getEdition() + ", " + book.getPrice());
        model.addAttribute("bookList", bookList);
		return "manageBooks";
	}

	

	@PostMapping("/savePromotion")
	public String savePromotion(HttpServletRequest request, Promotion promo, Model model)
	{
		String name = request.getParameter("name");
		String code = request.getParameter("code");
		String startDateString = request.getParameter("startDate");
		String endDateString = request.getParameter("endDate");
		BigDecimal discount = new BigDecimal(request.getParameter("discount"));

		boolean isDateError = false;
		String errorMessage = "Date is not valid. ";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate, endDate;
		Date todaysDate = new Date();

		try {
			startDate = df.parse(startDateString);
			endDate = df.parse(endDateString);

			// check if start date is before today
			if (startDate.before(todaysDate)) {
				errorMessage += " Start date is Before today. ";
				isDateError = true;
				model.addAttribute("startday", "Error!!! Start date cannot be before today.");
			}
			// check if end date is before today
			if (endDate.before(todaysDate)) {
				errorMessage += " End date is Before today. ";
				isDateError = true;
				model.addAttribute("endday", "Error!!! End date cannot be before today.");
			}

			// check if start is after end date
			if (startDate.after(endDate)) {
				errorMessage += " Start date must be before the End date. ";
				isDateError = true;
				model.addAttribute("dateerror",  "Error!!! Start date must be before the End date.");
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (isDateError) {
			model.addAttribute("isDateError", isDateError);
			model.addAttribute("errorMessage", errorMessage);
			return AddPromo(model);
		} else {
			promoService.savePromotion(promo, name, code, startDateString, endDateString, discount);
			return ManagePromo(model);
		}
	}
	
	@PostMapping("/sendPromotion")
	public String sendPromotion(HttpServletRequest request, Promotion promo, Model model)
	{
		Integer id = Integer.parseInt(request.getParameter("promoId"));
		promoService.sendPromo(id);
		List<Promotion> promoList = promoService.findAll();
		model.addAttribute("promoList", promoList);
		return "managePromotions";
		
	}

	@PostMapping("/changeUserStatus")
	public String changeUserStatus(HttpServletRequest request, User user, Model model)
	{
		String email = request.getParameter("emailaddress");
		
		userServices.changeUserStatus(email);
		List<User> userList = userServices.findAll();
		model.addAttribute("userList", userList);
		return "manageUsers";
	}
	
	
	
}
