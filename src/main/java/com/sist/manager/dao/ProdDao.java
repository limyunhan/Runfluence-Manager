package com.sist.manager.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sist.manager.model.Prod;
import com.sist.manager.model.VariantsOption;
import com.sist.manager.model.VariantsOptionValue;

@Repository
public interface ProdDao {
	public abstract int prodInsert(Prod prod);
	public abstract int prodFilterOptionValueInsert(HashMap<String, Object> hashMap);
	public abstract int variantsOptionValueCombInsert(HashMap<String, Object> hashMap);
	public abstract List<VariantsOption> variantsOptionList();
	public abstract List<VariantsOptionValue> variantsOptionValueList();	
	public abstract String getExpectedProdId(String prodSubCateCombinedId);
	public abstract String getActualProdId(String prodSubCateCombinedId);
	public abstract String getProdMainCateName(String prodSubCateCombinedId);
}