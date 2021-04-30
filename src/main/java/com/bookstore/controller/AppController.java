package com.bookstore.controller;


import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bookstore.domain.Book;
import com.bookstore.domain.CustomerUserDetails;
import com.bookstore.domain.User;
import com.bookstore.repository.UserRepo;
import com.bookstore.services.BookService;
import com.bookstore.services.UserServices;
import com.bookstore.services.impl.BookServiceImpl;

@Controller
public class AppController {

    @Autowired
    private UserRepo repo;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserServices service;

    @GetMapping("")
    public String viewHomePage(Model model) {
        setHomepageBooksFromDB(model);
        return "index";
    }

    @PostMapping("/search")
    public String Search(HttpServletRequest request, Model model) {
        String searchType = request.getParameter("searchType");
        String searchTerm = request.getParameter("searchTerms");

//        System.out.println("Search for: " + searchTerm + " In " + searchType);

        List<Book> bookList;

        switch (searchType) {
            case "Category":
//                System.out.println("Search for: " + searchTerm + " In Categories...");
                bookList = bookService.searchByCategory(searchTerm);
//                for (Book b : bookList) {
//                    System.out.println(b.getTitle());
//                }
                break;
            case "Title":
//                System.out.println("Search for: " + searchTerm + " in Titles........");
                bookList = bookService.searchByTitle(searchTerm);
//                for (Book b : bookList) {
//                    System.out.println(b.getTitle());
//                }
                break;
            case "ISBN":
//                System.out.println("Search for: " + searchTerm + " in ISBN,,, ");
                bookList = bookService.searchByISBN(searchTerm);
//                for (Book b : bookList) {
//                    System.out.println(b.getTitle());
//                }
                break;
            case "Author":
//                System.out.println("Search for: " + searchTerm + " in Authors... ");
                bookList = bookService.searchByAuthor(searchTerm);
//                for (Book b : bookList) {
//                    System.out.println(b.getTitle());
//                }
                break;
            default:
                bookList = bookService.findAll();
                break;
        }
        if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "bookshelf";
		}

        model.addAttribute("bookList", bookList);
        return "bookshelf";
    }


    @GetMapping("/bookshelf")
    public String bookshelf(Model model) {
        if (!model.containsAttribute("bookList")) {
            List<Book> bookList = bookService.getSortedByTitle();
            model.addAttribute("bookList", bookList);
        }
        
        return "bookshelf";
    }

    @PostMapping("/bookshelf_sorted")
    public String bookshelfSorted(HttpServletRequest request, Model model) {

        String type = request.getParameter("type");
        System.out.println(type);

        List<Book> bookList;


        if (type.equals("Author"))
            bookList = bookService.getSortedByAuthor();
        else if (type.equals("Category"))
            bookList = bookService.getSortedByCategories();
        else if (type.equals("ISBN"))
            bookList = bookService.getSortedByISBN();
        else if (type.equals("PriceHighToLow"))
            bookList = bookService.getSortedByPriceHighToLow();
        else if (type.equals("PriceLowToHigh"))
            bookList = bookService.getSortedByPriceLowToHigh();
        else if (type.equals("Rating"))
            bookList = bookService.getSortedByRating();
        else // type is title
            bookList = bookService.getSortedByTitle();

        model.addAttribute("bookList", bookList);
        return bookshelf(model);
    }


    @RequestMapping(value = "/bookDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public String bookDetail(
            @PathParam("id") Long id, Model model
    ) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String emailaddress = null;
//        if (principal != null) {
//            emailaddress = ((CustomerUserDetails) principal).getEmailaddress();
//            User user = repo.findByEmail(emailaddress);
//            model.addAttribute("user", user);
//        }


        Book book = bookService.findById(id);

        model.addAttribute("book", book);

        List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        model.addAttribute("qtyList", qtyList);
        model.addAttribute("qty", 1);

        return "bookDetail";
    }

    private void setHomepageBooksFromDB(Model model) {
        // get the 6 most recent books
        List<Book> recentlyAddedBooks = bookService.get6MostRecent();
        model.addAttribute("recentBookList", recentlyAddedBooks);

        // get featured books
        List<Book> featuredList = bookService.getFeaturedBooks();
        model.addAttribute("featuredList", featuredList);
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "registerUser";
    }


    @GetMapping("/cart_view")
    public String viewcart() {
        return "cartView";
    }

    @GetMapping("/dashboard")
    public String ShowLoogedout() {
        return "dashboard";
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/admin")
    public String getadmin() {
        return "dashboard";
    }


    @PostMapping("/process_register")
    public String processRegister(User user, HttpServletRequest request, @ModelAttribute("emailaddress") String emailaddress,
                                  @ModelAttribute("username") String username, Model model)
            throws UnsupportedEncodingException, MessagingException {

        model.addAttribute("email", emailaddress);
        model.addAttribute("username", username);

        if (repo.findByUsername(username) != null) {
            model.addAttribute("usernameExists", true);
            return "registerUser";

        }

        if (repo.findByEmail(emailaddress) != null) {
            model.addAttribute("emailExists", true);

            return "registerUser";
        }
        service.register(user, getSiteURL(request));
        return "register_sucesss";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (service.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    @GetMapping("/user_login")
    public String showLoginPage(@ModelAttribute("user_status") String user_status, Model model) {
        model.addAttribute("user_status", user_status);
        String type = null;
        String status = null;
        boolean enabled = true;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomerUserDetails) {
            type = ((CustomerUserDetails) principal).getUserType();
            status = ((CustomerUserDetails) principal).getUserStatus();
            enabled = ((CustomerUserDetails) principal).getUserEnabled();
        }
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "userLogin";
        } else if (enabled == false) {
            model.addAttribute("is_enabled", true);
            return "userLogin";
        } else if (status.equals("Suspended")) {
            model.addAttribute("userSuspended", true);
            return "userLogin";
        } else if (type.equals("admin") && authentication != null) {
            return "dashboard";
        } else {
            setHomepageBooksFromDB(model);
            return "index";
        }
    }

//	@GetMapping("/ju_lius")
//	public String showJulius(Model model) {
//		Book bookList = bookService.findByTitle("Julius Caesar");
//		model.addAttribute("bookList", bookList);
//		return "julius";
//	}
//
//	@GetMapping("/romeo")
//	public String showRomeo(Model model) {
//		Book bookList = bookService.findByTitle("Romeo");
//		model.addAttribute("bookList", bookList);
//		return "romeoandjuliet";
//	}
//	@GetMapping("/mac_beth")
//	public String showMacbeth(Model model) {
//		Book bookList = bookService.findByTitle("The Tragedy of Macbeth");
//		model.addAttribute("bookList", bookList);
//		return "macbeth";
//	}
//	@GetMapping("/emi_ly")
//	public String showEmily(Model model) {
//		Book bookList = bookService.findByTitle("Letters of Emily Dickinson");
//		model.addAttribute("bookList", bookList);
//		return "emily";
//	}
//
//	@GetMapping("/fro_zen")
//	public String showFrozen(Model model) {
//		Book bookList = bookService.findByTitle("frozen");
//		model.addAttribute("bookList", bookList);
//		return "frozen";
//	}
//	@GetMapping("/hamlet")
//	public String showHamlet(Model model) {
//		Book bookList = bookService.findByTitle("Hamlet");
//		model.addAttribute("bookList", bookList);
//		return "Hamlet";
//	}

}
