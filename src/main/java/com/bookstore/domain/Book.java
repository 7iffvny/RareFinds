package com.bookstore.domain;




import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "books")
public class Book {

	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BOOK_ID", nullable = false, unique = true)
	private Long Bookid;
	
	
	@Column(name = "TITLE", nullable = false)
	private String title;
	
	@Column(name = "ISBN", nullable = false)
	private String isbn;
	
	@Column(name = "AUTHOR_NAME", nullable = false)
	private String name;
	
	@Column(name = "CATEGORIES", nullable = false)
	private String categories;	
	
	@Column(name = "DESCRIPTION", nullable = false, length = 1000)
	private String description;	
	
	@Column(name = "COVER_PICTURE", nullable = true)
	private String cover_pic;	
	
    @Column(name = "EDITION", nullable = false)
    private Integer edition;
    
    @Column(name = "PUBLISHER_NAME", nullable = false)
    private String publisherName;
    
    @Column(name = "PUBLISHER_YEAR", nullable = false)
    private String publisherYear;
    
    @Column(name = "PRICE",nullable = false, precision = 10, scale = 2)
    private double price;
    
    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;
    
    @Column(name = "KEYWORDS", nullable = true)
    private String keywords;
    
    @Column(name = "STATUS", nullable = true)
    private Integer status;
    
    @Column(name = "QUANTITY_ON_HAND", nullable = true)
    private Integer quantityHand;
    
    @Column(name = "MINIMUM_THRESHOLD", nullable = true)
    private Integer minimum;

	@Column(name = "RATING", nullable = true)
	private Integer rating;

	@Column(name = "isFeatured", nullable = true)
	private Integer isFeatured;


    private int inStockNumber;
    
    @Transient
	private MultipartFile bookImage;
    
	public Long getBookid() {
		return Bookid;
	}
	public void setBookid(Long bookid) {
		Bookid = bookid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCover_pic() {
		return cover_pic;
	}
	public void setCover_pic(String cover_pic) {
		this.cover_pic = cover_pic;
	}
	public Integer getEdition() {
		return edition;
	}
	public void setEdition(Integer edition) {
		this.edition = edition;
	}
	public String getPublisherName() {
		return publisherName;
	}
	public void setPublisherName(String publisheName) {
		this.publisherName = publisheName;
	}
	public String getPublisherYear() {
		return publisherYear;
	}
	public void setPublisheYear(String publisheYear) {
		this.publisherYear = publisheYear;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
		this.inStockNumber = quantity;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getQuantityHand() {
		return quantityHand;
	}
	public void setQuantityHand(Integer quantityHand) {
		this.quantityHand = quantityHand;
	}
	public Integer getMinimum() {
		return minimum;
	}
	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}
    
	public MultipartFile getBookImage() {
		return bookImage;
	}

	public void setBookImage(MultipartFile bookImage) {
		this.bookImage = bookImage;
	}
    
	public String getFormattedPrice() {
		return "$" + this.price;
	}

	public String getFormattedEdition() {
		if (edition == 1)
			return this.edition + "st";
		else if (edition == 2)
			return this.edition + "nd";
		else if (edition == 3)
			return this.edition + "rd";
		else
			return this.edition + "th";
	}
	public int getInStockNumber() {
		return inStockNumber;
	}
	public void setInStockNumber(int inStockNumber) {
		this.inStockNumber = inStockNumber;
		this.quantity = inStockNumber;
	}

	public String getFormattedRating() {
		return rating + " of 5";
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer isFeatured() {
		return isFeatured;
	}
	public void setFeatured(Integer featured) {
		isFeatured = featured;
	}

	public List<Integer> getRatingAsList() {
		List<Integer> ratingList = new ArrayList<Integer>();
		for (int i = 0; i < 5; i++) {
			if (i < rating)
				ratingList.add(1);
			else
				ratingList.add(0);
		}
		return ratingList;
	}
   
}
