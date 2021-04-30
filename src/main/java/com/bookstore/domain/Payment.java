package com.bookstore.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "paymentInformation")
public class Payment {

	@Id
	@Column (name = "payment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column (name = "user_email")
	private String user_email;
	
	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	@Column(name = "card_number", nullable = false, length = 60)
	private String cardnumber;
	
	@Column(name = "card_holder", nullable = false, length = 45)
	private String cardholder;
	
	
	@Column(name = "exp_month", nullable = false, length = 20)
	private String expmonth;
	
	@Column(name = "exp_year", nullable = false, length = 20)
	private String expyear;
	
	private String type;
	
	private Integer cvc;
	

	public String getType() {
		return type;
	}

	@OneToOne
	private Order order;

	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "userPayment")
	private UserBilling userBilling;

	@Column(name = "LAST_FOUR_DIGITS", nullable = true)
	private String lastFourDigits;

	
	public String getCardnumber() {
		return cardnumber;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCardholder() {
		return cardholder;
	}

	public void setCardholder(String cardholder) {
		this.cardholder = cardholder;
	}

	
	public String getExpmonth() {
		return expmonth;
	}

	public void setExpmonth(String month) {
		this.expmonth = month;
	}

	public String getExpyear() {
		return expyear;
	}

	public void setExpyear(String expyear) {
		this.expyear = expyear;
	}


	public UserBilling getUserBilling() {
		return userBilling;
	}

	public void setUserBilling(UserBilling userBilling) {
		this.userBilling = userBilling;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public int getCvc() {
		return 0;
	}

	public void setCvc(int cvc) {
		this.cvc = cvc;
	}

	public String getLastFourDigits() {
		return lastFourDigits;
	}

	public void setLastFourDigits(String lastFourDigits) {
		this.lastFourDigits = lastFourDigits;
	}




}
