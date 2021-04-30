package com.bookstore.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.services.BookService;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {
        return (List<Book>) bookRepository.findAll();
    }

    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> getDescending() {
        return bookRepository.getDescending();
    }

    public List<Book> getUnder100() {
        return bookRepository.getUnder100();
    }

    public List<Book> getSortedByTitle() {
        return bookRepository.getSortedByTitle();
    }

    public List<Book> getFeaturedBooks() {
        return bookRepository.getFeatured();
    }

    public List<Book> getSortedByCategories() {
        return bookRepository.getSortedByCategories();
    }

    public List<Book> getSortedByISBN() {
        return bookRepository.getSortedByISBN();
    }

    public List<Book> getSortedByAuthor() {
        return bookRepository.getSortedByAuthor();
    }

    public List<Book> searchByTitle(String term) {
        return bookRepository.searchByTitle(term);
    }

    public List<Book> searchByISBN(String term) {
        return bookRepository.searchByISBN(term);
    }

    public List<Book> searchByAuthor(String term) {
        return bookRepository.searchByAuthor(term);
    }

    public List<Book> searchByCategory(String term) {
        return bookRepository.searchByCategory(term);
    }

    public List<Book> getSortedByPriceLowToHigh(){
        return bookRepository.getSortedByPriceLowToHigh();
    }
    public List<Book> getSortedByPriceHighToLow(){
        return bookRepository.getSortedByPriceHighToLow();
    }

    public List<Book> getSortedByRating() {
        return bookRepository.getSortedByRating();
    }

    // get the 6 most recent books
    public List<Book> get6MostRecent() {
        List<Book> books = getDescending();
        List<Book> recentlyAddedBooks = getDescending();
        recentlyAddedBooks.clear();
        for (int i = 0; i < books.size(); i++) {
            if (i < 6)
                recentlyAddedBooks.add(books.get(i));
        }
        return recentlyAddedBooks;
    }

    // gets a random 6 books from the database whose price is less than 100
    public List<Book> get6Under100() {
        List<Book> books = getUnder100();
        List<Book> under100 = findAll();
        under100.clear();
        int count = 0;
        for (Book book : books) {
            if (count < 6) {
                if (book.getPrice() < 100.0) {
                    under100.add(book);
                    count++;
                }
            }
        }
        return under100;
    }


}