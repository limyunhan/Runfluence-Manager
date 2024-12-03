package com.sist.manager.model;

import java.io.Serializable;

public class ProdMainCate implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String prodMainCateId;
	private String prodMainCateName;
	
	public ProdMainCate() {
		prodMainCateId = "";
		prodMainCateName = "";
	}
	
	public String getProdMainCateId() {return prodMainCateId;}
	public void setProdMainCateId(String prodMainCateId) {this.prodMainCateId = prodMainCateId;}
	public String getProdMainCateName() {return prodMainCateName;}
	public void setProdMainCateName(String prodMainCateName) {this.prodMainCateName = prodMainCateName;}
}