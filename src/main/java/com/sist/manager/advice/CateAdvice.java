package com.sist.manager.advice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sist.manager.model.FilterOption;
import com.sist.manager.model.FilterOptionValue;
import com.sist.manager.model.ProdMainCate;
import com.sist.manager.model.ProdSubCate;
import com.sist.manager.service.CateService;

@ControllerAdvice
public class CateAdvice {
    
    @Autowired
    private CateService cateService;

    // 메인 카테고리 리스트
    @ModelAttribute("prodMainCateList")
    public List<ProdMainCate> getProdMainCateList() {
        return cateService.prodMainCateList();
    }
    
    // 메인 카테고리에 해당하는 서브 카테고리 리스트 맵
    @ModelAttribute("prodSubCateListMap") 
    public Map<String, List<ProdSubCate>> getProdSubCateListMap() {
        List<ProdSubCate> prodSubCateList = cateService.prodSubCateList();
        Map<String, List<ProdSubCate>> prodSubCateListMap = new HashMap<>();

        for (ProdSubCate prodSubCate : prodSubCateList) {
            prodSubCateListMap
                .computeIfAbsent(prodSubCate.getProdMainCateId(), k -> new ArrayList<>())
                .add(prodSubCate);
        }

        return prodSubCateListMap;
    }
    
    // 메인 카테고리 번호로 메인 카테고리 객체를 얻는 맵 (이름을 조회할 수 있음)
    @ModelAttribute("prodMainCateMap")
    public Map<String, ProdMainCate> getProdMainCateMap() {
        List<ProdMainCate> prodMainCateList = cateService.prodMainCateList();
        Map<String, ProdMainCate> prodMainCateMap = new HashMap<>();

        for (ProdMainCate prodMainCate : prodMainCateList) {
            prodMainCateMap.put(prodMainCate.getProdMainCateId(), prodMainCate);
        }

        return prodMainCateMap;
    }
    
    // 서브 카테고리 번호로 서브 카테고리 객체를 얻는 맵 (이름을 조회할 수 있음)
    @ModelAttribute("prodSubCateMap") 
    public Map<String, ProdSubCate> getProdSubCateMap() {
        List<ProdSubCate> prodSubCateList = cateService.prodSubCateList();
        Map<String, ProdSubCate> prodSubCateMap = new HashMap<>();

        for (ProdSubCate prodSubCate : prodSubCateList) {
            prodSubCateMap.put(prodSubCate.getProdSubCateCombinedId(), prodSubCate);
        }

        return prodSubCateMap;
    }
    
    // 필터 리스트
    @ModelAttribute("filterOptionList")
    public List<FilterOption> getFilterOptionList() {
        return cateService.filterOptionList();
    }
    
    // 필터에 해당하는 필터 옵션 값 리스트 맵
    @ModelAttribute("filterOptionValueListMap") 
    public Map<String, List<FilterOptionValue>> getFilterOptionValueListMap() {
        List<FilterOptionValue> filterOptionValueList = cateService.filterOptionValueList();
        Map<String, List<FilterOptionValue>> filterOptionValueListMap = new HashMap<>();

        for (FilterOptionValue filterOptionValue : filterOptionValueList) {
            filterOptionValueListMap
                .computeIfAbsent(filterOptionValue.getFilterOptionId(), k -> new ArrayList<>())
                .add(filterOptionValue);
        }

        return filterOptionValueListMap;
    }
}