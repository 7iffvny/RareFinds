package com.bookstore.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "addressInformation")
public class Address {

	@Id
	@Column (name = "address_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column (name = "user_email")
	private String user_email;
	
	@Column (name = "shipping_name")
	private String shippingname;
	@Column(name = "shipping_street1", nullable = false, length = 64)
	private String shippingstreet1;
	
	
	@Column(name = "shipping_street2", nullable = true, length = 64)
	private String shippingstreet2;
	
	@Column(name = "zipcode", nullable = false, length = 64)
	private String zipcode;
	
	@Column(name = "country", nullable = true, length = 64)
	private String country;
	
	@Column(name = "state", nullable = false, length = 45)
	private String state;
	
	@Column(name = "city", nullable = false, length = 45)
	private String city;
	
	@OneToOne
	private Order order;
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public String getShippingstreet1() {
		return shippingstreet1;
	}

	public void setShippingstreet1(String shippingstreet1) {
		this.shippingstreet1 = shippingstreet1;
	}

	public String getShippingstreet2() {
		return shippingstreet2;
	}

	public void setShippingstreet2(String shippingstreet2) {
		this.shippingstreet2 = shippingstreet2;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
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

	
	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

	public String getShippingname() {
		return shippingname;
	}

	public void setShippingname(String shippingname) {
		this.shippingname = shippingname;
	}


}
	

		
