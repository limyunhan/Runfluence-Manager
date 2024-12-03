package com.sist.manager.model;

import java.io.Serializable;

public class Prod implements Serializable {
	private static final long serialVersionUID = 1L;
	private String prodId;
	private String prodSubCateCombinedId;
	private String prodName;
	private long prodPrice;
	private double prodDiscountPercent;
	private String prodInfo;
	private String prodStatus;
	private String regDate;
	
	public Prod() {
		prodId = "";
		prodSubCateCombinedId = "";
		prodName = "";
		prodPrice = 0L;
		prodDiscountPercent = 0.0;
		prodInfo = "";
		prodStatus = "I";
		regDate = "";
	}
	
	public String getProdId() {return prodId;}
	public void setProdId(String prodId) {this.prodId = prodId;}
	public String getProdSubCateCombinedId() {return prodSubCateCombinedId;}
	public void setProdSubCateCombinedId(String prodSubCateCombinedId) {this.prodSubCateCombinedId = prodSubCateCombinedId;}
	public String getProdName() {return prodName;}
	public void setProdName(String prodName) {this.prodName = prodName;}
	public long getProdPrice() {return prodPrice;}
	public void setProdPrice(long prodPrice) {this.prodPrice = prodPrice;}
	public double getProdDiscountPercent() {return prodDiscountPercent;}
	public void setProdDiscountPercent(double prodDiscountPercent) {this.prodDiscountPercent = prodDiscountPercent;}
	public String getProdInfo() {return prodInfo;}
	public void setProdInfo(String prodInfo) {this.prodInfo = prodInfo;}
	public String getProdStatus() {return prodStatus;}
	public void setProdStatus(String prodStatus) {this.prodStatus = prodStatus;}
	public String getRegDate() {return regDate;}
	public void setRegDate(String regDate) {this.regDate = regDate;}
}