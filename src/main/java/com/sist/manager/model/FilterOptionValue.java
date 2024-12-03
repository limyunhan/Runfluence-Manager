package com.sist.manager.model;

import java.io.Serializable;

public class FilterOptionValue implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String filterOptionValueId;
	private String filterOptionId;
	private String filterOptionValueName;
	
	public FilterOptionValue() {
		filterOptionValueId = "";
		filterOptionId = "";
	}
	
	public String getFilterOptionValueId() {return filterOptionValueId;}
	public void setFilterOptionValueId(String filterOptionValueId) {this.filterOptionValueId = filterOptionValueId;}
	public String getFilterOptionId() {return filterOptionId;}
	public void setFilterOptionId(String filterOptionId) {this.filterOptionId = filterOptionId;}
	public String getFilterOptionValueName() {return filterOptionValueName;}
	public void setFilterOptionValueName(String filterOptionValueName) {this.filterOptionValueName = filterOptionValueName;}
}