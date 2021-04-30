package com.bookstore.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class BillingAddress {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "billing_name", nullable = false, length = 64)
	private String BillingName;
	
	@Column(name = "billing_street1", nullable = false, length = 64)
	private String billingstreet1;
	
	
	@Column(name = "billing_street2", nullable = true, length = 64)
	private String billingstreet2;
	
	@Column(name = "billing_zipcode", nullable = false, length = 64)
	private String billingzipcode;
	
	@Column(name = "country", nullable = true, length = 64)
	private String country;
	
	@Column(name = "state", nullable = false, length = 20)
	private String state;
	
	@Column(name = "city", nullable = false, length = 20)
	private String city;
	
	@OneToOne
	private Order order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBillingName() {
		return BillingName;
	}

	public void setBillingName(String billingName) {
		BillingName = billingName;
	}

	public String getBillingstreet1() {
		return billingstreet1;
	}

	public void setBillingstreet1(String billingstreet1) {
		this.billingstreet1 = billingstreet1;
	}

	public String getBillingstreet2() {
		return billingstreet2;
	}

	public void setBillingstreet2(String billingstreet2) {
		this.billingstreet2 = billingstreet2;
	}

	public String getBillingzipcode() {
		return billingzipcode;
	}

	public void setBillingzipcode(String billingzipcode) {
		this.billingzipcode = billingzipcode;
	}

	
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	
}
