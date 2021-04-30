package com.bookstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.domain.Book;
import com.bookstore.domain.CartItem;
import com.bookstore.domain.CustomerUserDetails;
import com.bookstore.domain.ShoppingCart;
import com.bookstore.domain.User;
import com.bookstore.repository.UserRepo;
import com.bookstore.services.BookService;
import com.bookstore.services.CartItemService;
import com.bookstore.services.ShoppingCartService;
import com.bookstore.services.UserServices;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	
	@Autowired
	private UserServices userService;
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShoppingCartService ShoppingCartService;
	
	@RequestMapping("/cart")
	public String shoppingCart(Model model) {
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
		ShoppingCart shoppingCart = (ShoppingCart) user.getShoppingCart();
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		ShoppingCartService.updateShoppingCart(shoppingCart);
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		
		return "cartView";
	}
	
	
	
	@RequestMapping(value = "/addItem", method = RequestMethod.POST)
	public String addItem(
			@ModelAttribute("book") Book book,
			@ModelAttribute("qty") String qty,
			Model model
			) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal =   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailaddress = null;
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "userLogin";
		}
		else if(principal != null)
		{
			emailaddress = ((CustomerUserDetails)principal).getEmailaddress();
		}
		User user = repo.findByEmail(emailaddress);
		book = bookService.findById(book.getBookid());
		
		if (Integer.parseInt(qty) > book.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/bookDetail?id="+book.getBookid();
		}
		
		CartItem cartItem = cartItemService.addBookToCartItem(book, user, Integer.parseInt(qty));
		model.addAttribute("addBookSuccess", true);
		
		return "forward:/bookDetail?id="+book.getBookid();
	}

	@RequestMapping("/updateCartItem")
	public String updateShoppingCart(
			@ModelAttribute("id") Long cartItemId,
			@ModelAttribute("qty") int qty
			) {
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQty(qty);
		cartItemService.updateCartItem(cartItem);
		System.out.println("true");
		return "forward:/shoppingCart/cart";
	}
	
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("id") Long id) {
		cartItemService.removeCartItem(cartItemService.findById(id));
		
		return "forward:/shoppingCart/cart";
	}
}