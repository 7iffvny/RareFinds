package com.bookstore.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.domain.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.services.BookService;


@Controller
@ControllerAdvice
public class BookController {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private BookService bookService;
	
	@GetMapping("/add_books")
	public String AddBooks(Model model)
	{
		Book book = new Book();
		model.addAttribute("book", book);
		return "addBooks";

	}
	
	@PostMapping("/add_books")
	public String addBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {
		
		String title = request.getParameter("title");
		String isbn = request.getParameter("isbn");
		String author = request.getParameter("author");
		Double price = Double.parseDouble(request.getParameter("price"));
		String description = request.getParameter("description");
		Integer edition = Integer.parseInt(request.getParameter("edition"));
		String publisherName = request.getParameter("publisherName");
		String publisherYear = request.getParameter("publisherYear");
		Integer quantity = Integer.parseInt(request.getParameter("quantity"));
		String categories = request.getParameter("categories");
		book.setCategories(categories);
		book.setDescription(description);
		book.setEdition(edition);
		book.setIsbn(isbn);
		book.setName(author);
		book.setPrice(price);
		book.setPublisherName(publisherName);
		book.setPublisheYear(publisherYear);
		book.setTitle(title);
		book.setQuantity(quantity);
		book.setInStockNumber(book.getQuantity());
		bookService.save(book);

		MultipartFile bookImage = book.getBookImage();
		book.setBookImage(bookImage);

		String bookPath = "";
		String newPath = "";

		try {
			byte[] bytes = bookImage.getBytes();
			String name = book.getBookid() + ".png";
			bookPath = "src/main/resources/static/image/book/" + name;
			newPath = "image/book/" + name;
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(bookPath)));
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		book.setCover_pic(newPath);
		bookService.save(book);
		return "redirect:manage_Books";
	}

	@PostMapping("/increase_quantity")
	public String increaseQuantity(HttpServletRequest request, Book book, Model model)
	{
		Long id = Long.parseLong(request.getParameter("bookID"));
		Book books = bookService.findById(id);
		Integer quantity = books.getQuantity();

		Integer newQuantity = quantity + 1;
		books.setQuantity(newQuantity);
		//book.setInStockNumber(book.getQuantity());
		bookService.save(books);

		return bookList(model);
	}

	@PostMapping("/toggle_featured")
	public String toggleFeatured(HttpServletRequest request, Book book, Model model)
	{
		Long id = Long.parseLong(request.getParameter("bookID"));
		Book books = bookService.findById(id);
		System.out.println(books.isFeatured());
		Integer isFeatured = books.isFeatured();

		if (isFeatured == 0) {
			isFeatured = 1;
		} else {
			isFeatured = 0;
		}

		books.setFeatured(isFeatured);
		bookService.save(books);

		return bookList(model);
	}

	@PostMapping("/decrease_quantity")
	public String decreaseQuantity(HttpServletRequest request, Book book, Model model)
	{
		Long id = Long.parseLong(request.getParameter("bookID"));
		Book books = bookService.findById(id);
		Integer quantity = books.getQuantity();

		Integer newQuantity = quantity - 1;
		books.setQuantity(newQuantity);
		//book.setInStockNumber(book.getQuantity());
		bookService.save(books);

		return bookList(model);
	}

	@PostMapping("/delete_book")
	public String deleteBook(HttpServletRequest request, Book book, Model model)
	{
		Long id = Long.parseLong(request.getParameter("bookID"));
		Book books = bookService.findById(id);
		bookRepository.delete(books);

		return bookList(model);
	}

	@GetMapping("/manage_Books")
	public String bookList(Model model) {
		List<Book> bookList = bookService.findAll();
		model.addAttribute("bookList", bookList);
		return "manageBooks";
		
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleFileUploadError(RedirectAttributes ra) {
		
		ra.addFlashAttribute("error", "File too large, Cannot upload file larger than 5MB");
		return "redirect:add_books";
		
	}
}
