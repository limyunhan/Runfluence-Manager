package com.sist.manager.model;

import java.io.Serializable;

public class VariantsOptionValue implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String variantsOptionValueId;
	private String variantsOptionId;
	private String variantsOptionValueName;
	
	public VariantsOptionValue() {
		variantsOptionValueId = "";
		variantsOptionId = "";
		variantsOptionValueName = "";
	}
	
	public String getVariantsOptionId() {return variantsOptionId;}
	public void setVariantsOptionId(String variantsOptionId) {this.variantsOptionId = variantsOptionId;}
	public String getVariantsOptionValueName() {return variantsOptionValueName;}
	public void setVariantsOptionValueName(String variantsOptionValueName) {this.variantsOptionValueName = variantsOptionValueName;}
	public String getVariantsOptionValueId() {return variantsOptionValueId;}
	public void setVariantsOptionValueId(String variantsOptionValueId) {this.variantsOptionValueId = variantsOptionValueId;}
}