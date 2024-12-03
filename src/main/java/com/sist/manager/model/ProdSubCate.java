package com.sist.manager.model;

import java.io.Serializable;

public class ProdSubCate implements Serializable {
	private static final long serialVersionUID = 1L;
	private String prodSubCateCombinedId;
	private String prodMainCateId;
	private String prodSubCateId;
	private String prodSubCateName;
	
	public ProdSubCate() {
		prodSubCateCombinedId = "";
		prodMainCateId = "";
		prodSubCateId = "";
		prodSubCateName = "";
	}
	
	public String getProdSubCateCombinedId() {return prodSubCateCombinedId;}
	public void setProdSubCateCombinedId(String prodSubCateCombinedId) {this.prodSubCateCombinedId = prodSubCateCombinedId;}
	public String getProdMainCateId() {return prodMainCateId;}
	public void setProdMainCateId(String prodMainCateId) {this.prodMainCateId = prodMainCateId;}
	public String getProdSubCateId() {return prodSubCateId;}
	public void setProdSubCateId(String prodSubCateId) {this.prodSubCateId = prodSubCateId;}
	public String getProdSubCateName() {return prodSubCateName;}
	public void setProdSubCateName(String prodSubCateName) {this.prodSubCateName = prodSubCateName;}
}