package com.bookstore.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bookstore.domain.Book;

public interface BookRepository extends CrudRepository<Book, Long>{

	@Query("SELECT b FROM Book b WHERE b.title = ?1")
	public Book findByTitle(String username);
	
	@Query("SELECT b FROM Book b WHERE b.Bookid = ?1")
	public Book findById(Integer id);

	@Query("SELECT b FROM Book b ORDER BY b.Bookid DESC")
	public List<Book> getDescending();

	@Query("SELECT b FROM Book b WHERE b.price < 100")
	public List<Book> getUnder100();

	@Query("SELECT b FROM Book b WHERE b.isFeatured = 1")
	public List<Book> getFeatured();


	@Query("SELECT b FROM Book b ORDER BY b.title")
	public List<Book> getSortedByTitle();

	@Query("SELECT b FROM Book b ORDER BY b.categories")
	public List<Book> getSortedByCategories();

	@Query("SELECT b FROM Book b ORDER BY b.isbn")
	public List<Book> getSortedByISBN();

	@Query("SELECT b FROM Book b ORDER BY b.name")
	public List<Book> getSortedByAuthor();

	@Query("SELECT b FROM Book b ORDER BY b.price")
	public List<Book> getSortedByPriceLowToHigh();

	@Query("SELECT b FROM Book b ORDER BY b.price DESC")
	public List<Book> getSortedByPriceHighToLow();

	@Query("SELECT b FROM Book b ORDER BY b.rating DESC")
	public List<Book> getSortedByRating();



	@Query("SELECT b FROM Book b WHERE b.title LIKE %?1%")
	public List<Book> searchByTitle(String term);

	@Query("SELECT b FROM Book b WHERE b.isbn LIKE %?1%")
	public List<Book> searchByISBN(String term);

	@Query("SELECT b FROM Book b WHERE b.name LIKE %?1%")
	public List<Book> searchByAuthor(String term);

	@Query("SELECT b FROM Book b WHERE b.categories LIKE %?1%")
	public List<Book> searchByCategory(String term);




	
	
}
