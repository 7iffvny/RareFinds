package com.bookstore.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "promotion")
public class Promotion {

    @Id
    @Column (name = "PROMO_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    
    
    @Column(name = "PROMO_SENT", nullable = true)
    private String promoSent;
    
    public String getPromoSent() {
		return promoSent;
	}
	public void setPromoSent(String promoSent) {
		this.promoSent = promoSent;
	}
	public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
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
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
		// TODO Auto-generated method stub
		
	

    public String getDiscountFormatted() {

    	BigDecimal hun = new BigDecimal(100);
        BigDecimal wholeNum = discount.multiply(hun);

        return wholeNum + "%";
    }

}
