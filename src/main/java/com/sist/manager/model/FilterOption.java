package com.sist.manager.model;

import java.io.Serializable;

public class FilterOption implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String filterOptionId;
	private String filterOptionName;
	
	public FilterOption() {
		filterOptionId = "";
		filterOptionName = "";
	}
	
	public String getFilterOptionId() {return filterOptionId;}
	public void setFilterOptionId(String filterOptionId) {this.filterOptionId = filterOptionId;}
	public String getFilterOptionName() {return filterOptionName;}
	public void setFilterOptionName(String filterOptionName) {this.filterOptionName = filterOptionName;}
}