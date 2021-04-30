package com.bookstore.services;


import java.util.List;

import com.bookstore.domain.Book;
import org.springframework.data.jpa.repository.Query;

public interface BookService {
    List<Book> findAll();

    Book findByTitle(String title);

    Book save(Book book);

    Book findById(Long id);

    List<Book> getDescending();

    List<Book> getUnder100();

    List<Book> get6MostRecent();

    List<Book> get6Under100();

    List<Book> getFeaturedBooks();

    List<Book> getSortedByTitle();

    List<Book> getSortedByCategories();

    List<Book> getSortedByISBN();

    List<Book> getSortedByAuthor();

    List<Book> getSortedByPriceLowToHigh();

    List<Book> getSortedByPriceHighToLow();


    List<Book> getSortedByRating();

    List<Book> searchByTitle(String term);

    List<Book> searchByISBN(String term);

    List<Book> searchByAuthor(String term);

    List<Book> searchByCategory(String term);

}
