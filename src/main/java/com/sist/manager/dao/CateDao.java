package com.sist.manager.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sist.manager.model.FilterOption;
import com.sist.manager.model.FilterOptionValue;
import com.sist.manager.model.ProdMainCate;
import com.sist.manager.model.ProdSubCate;

@Repository
public interface CateDao {
	public abstract List<ProdMainCate> prodMainCateList();
	public abstract List<ProdSubCate> prodSubCateList();
	public abstract List<FilterOption> filterOptionList();
	public abstract List<FilterOptionValue> filterOptionValueList();
}