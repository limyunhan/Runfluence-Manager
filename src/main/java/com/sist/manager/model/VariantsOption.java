package com.sist.manager.model;

import java.io.Serializable;


public class VariantsOption implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String variantsOptionId;
	private String variantsOptionName;
	
	public VariantsOption() {
		variantsOptionId = "";
		variantsOptionName = "";
	}
	
	public String getVariantsOptionId() {return variantsOptionId;}
	public void setVariantsOptionId(String variantsOptionId) {this.variantsOptionId = variantsOptionId;}
	public String getVariantsOptionName() {return variantsOptionName;}
	public void setVariantsOptionName(String variantsOptionName) {this.variantsOptionName = variantsOptionName;}
}
