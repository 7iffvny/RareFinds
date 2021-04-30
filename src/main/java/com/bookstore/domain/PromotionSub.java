package com.bookstore.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "promoInformation")
public class PromotionSub {

	private static final long serialVersionUID = 1L;
	@Id
	@Column (name = "promotion_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "PROMO_NAME")
    private String promoName;

    @Column(name = "PROMO_CODE")
    private String promoCode;

    @Column(name = "START_DATE")
    private String startDate;

    @Column(name = "END_DATE")
    private String endDate;

    @Column(name = "DISCOUNT")
    private BigDecimal discount;
    
    private Boolean is_used;
	
	public Boolean getIs_used() {
		return is_used;
	}

	public void setIs_used(Boolean is_used) {
		this.is_used = is_used;
	}

	public String getPromoName() {
		return promoName;
	}

	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal bigDecimal) {
		this.discount = bigDecimal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column (name = "user_phonenumber")
	private String user_phonenumber;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser_phonenumber() {
		return user_phonenumber;
	}

	public void setUser_phonenumber(String user_phonenumber) {
		this.user_phonenumber = user_phonenumber;
	}

	@Column (name = "user_email")
	private String user_email;
	
	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
}
