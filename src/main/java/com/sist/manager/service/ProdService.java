package com.sist.manager.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sist.manager.dao.ProdDao;
import com.sist.manager.model.Prod;
import com.sist.manager.model.VariantsOption;
import com.sist.manager.model.VariantsOptionValue;
import com.sist.manager.model.VariantsOptionValueComb;

@Service
public class ProdService {
	public static Logger logger = LoggerFactory.getLogger(ProdService.class);
	
	
	@Autowired
	private ProdDao prodDao;
	
	// 제품 삽입
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean prodInsert(Prod prod, List<String> prodFilterOptionValueIdList, List<VariantsOptionValueComb> variantsOptionValueCombList) {
		int cnt = 0;
		
		cnt = prodDao.prodInsert(prod);
		
		if (cnt == 1) {
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("prodSubCateCombinedId", prod.getProdSubCateCombinedId());
			hashMap.put("prodFilterOptionValueIdList", prodFilterOptionValueIdList);
			prodDao.prodFilterOptionValueInsert(hashMap);
			
			hashMap.clear();
			
			hashMap.put("prodId", prod.getProdId()); // 상품 ID
	        hashMap.put("variantsOptionValueCombList", variantsOptionValueCombList);
	        prodDao.variantsOptionValueCombInsert(hashMap);
		}
				
		return (cnt == 1);
	}
	
	public List<VariantsOption> variantsOptionList() {
		List<VariantsOption> list = null;
		
		try {
			list = prodDao.variantsOptionList();
		
		} catch (Exception e) {
			logger.error("[ProdService] variantsOptionList Exception", e);
		}

		return list;
	}
	
	public List<VariantsOptionValue> variantsOptionValueList() {
		List<VariantsOptionValue> list = null;
		
		try {
			list = prodDao.variantsOptionValueList();
		} catch (Exception e) {
			logger.error("[ProdService] variantsOptionValueList Exception", e);
		}
		
		return list;
	}
}