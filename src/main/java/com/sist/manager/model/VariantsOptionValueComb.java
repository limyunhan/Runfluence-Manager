package com.sist.manager.model;

import java.io.Serializable;

public class VariantsOptionValueComb implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String variantsOptionValueCombId;
	private String prodId;
	private long variantsOptionValueCombStock;
	private String variantsOptionValueCombText;
	
	public VariantsOptionValueComb() {
		variantsOptionValueCombId = "";
		prodId = "";
		variantsOptionValueCombStock = 0L;
		variantsOptionValueCombText = "";
	}
	
	public String getVariantsOptionValueCombId() {return variantsOptionValueCombId;}
	public void setVariantsOptionValueCombId(String variantsOptionValueCombId) {this.variantsOptionValueCombId = variantsOptionValueCombId;}
	public String getProdId() {return prodId;}
	public void setProdId(String prodId) {this.prodId = prodId;}
	public long getVariantsOptionValueCombStock() {return variantsOptionValueCombStock;}
	public void setVariantsOptionValueCombStock(long variantsOptionValueCombStock) {this.variantsOptionValueCombStock = variantsOptionValueCombStock;}
	public String getVariantsOptionValueCombText() {return variantsOptionValueCombText;}
	public void setVariantsOptionValueCombText(String variantsOptionValueCombText) {this.variantsOptionValueCombText = variantsOptionValueCombText;}
}