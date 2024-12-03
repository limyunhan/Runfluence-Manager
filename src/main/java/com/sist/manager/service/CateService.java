package com.sist.manager.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.manager.dao.CateDao;
import com.sist.manager.model.FilterOption;
import com.sist.manager.model.FilterOptionValue;
import com.sist.manager.model.ProdMainCate;
import com.sist.manager.model.ProdSubCate;

@Service
public class CateService {
	public static Logger logger = LoggerFactory.getLogger(CateService.class);

	@Autowired
	private CateDao cateDao;

	public List<ProdMainCate> prodMainCateList() {
		List<ProdMainCate> list = null;

		try {
			list = cateDao.prodMainCateList();
		} catch (Exception e) {
			logger.error("[CateService] prodMainCateList Exception", e);
		}

		return list;
	}

	public List<ProdSubCate> prodSubCateList() {
		List<ProdSubCate> list = null;

		try {
			list = cateDao.prodSubCateList();
		} catch (Exception e) {
			logger.error("[CateService] prodSubCateList Exception", e);
		}

		return list;
	}

	public List<FilterOption> filterOptionList() {
		List<FilterOption> list = null;

		try {
			list = cateDao.filterOptionList();
		} catch (Exception e) {
			logger.error("[CateService] filterOptionList Exception", e);
		}

		return list;
	}

	public List<FilterOptionValue> filterOptionValueList() {
		List<FilterOptionValue> list = null;
		
		try {
			list = cateDao.filterOptionValueList();
		} catch (Exception e) {
			logger.error("[CateService] filterOptionValueList Exception", e);
		}

		return list;
		
	}
}
