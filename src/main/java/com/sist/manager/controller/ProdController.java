package com.sist.manager.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.sist.common.model.FileData;
import com.sist.common.util.FileUtil;
import com.sist.common.util.HttpUtil;
import com.sist.common.util.StringUtil;
import com.sist.manager.model.Prod;
import com.sist.manager.model.Response;
import com.sist.manager.model.VariantsOption;
import com.sist.manager.model.VariantsOptionValue;
import com.sist.manager.model.VariantsOptionValueComb;
import com.sist.manager.service.ProdService;

@Controller
public class ProdController {
	public static Logger logger = LoggerFactory.getLogger(ProdController.class);
	
	@Value("#{env['prod.img.dir']}")
	private String PROD_IMG_DIR;
	
	@Autowired
	private ProdService prodService;

	@RequestMapping(value = "/prod/register")
	public String register(Model model, HttpServletRequest request) {

		List<VariantsOption> variantsOptionList = prodService.variantsOptionList();
		List<VariantsOptionValue> variantsOptionValueList = prodService.variantsOptionValueList();
		Map<String, List<VariantsOptionValue>> variantsOptionValueListMap = new HashMap<>();
	    Map<String, VariantsOptionValue> variantsOptionValueMap = new HashMap<>();

		for (VariantsOptionValue variantsOptionValue : variantsOptionValueList) {
			variantsOptionValueListMap
					.computeIfAbsent(variantsOptionValue.getVariantsOptionId(), k -> new ArrayList<>())
					.add(variantsOptionValue);
			variantsOptionValueMap.put(variantsOptionValue.getVariantsOptionValueId(), variantsOptionValue);
		}

		model.addAttribute("variantsOptionList", variantsOptionList);
		model.addAttribute("variantsOptionValueListMap", variantsOptionValueListMap);

		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			String variantsOptionValueListMapJsonString = objectMapper.writeValueAsString(variantsOptionValueListMap);
			String variantsOptionValueMapJsonString = objectMapper.writeValueAsString(variantsOptionValueMap);
			
			model.addAttribute("variantsOptionValueListMapJsonString", variantsOptionValueListMapJsonString);
			model.addAttribute("variantsOptionValueMapJsonString", variantsOptionValueMapJsonString);
		
		} catch (JsonProcessingException e) {
			logger.error("[ProdController] register Exception", e);
		}
		
		return "/prod/register";
	}
	
	@RequestMapping(value = "/prod/registerProc", method = RequestMethod.POST)
	@ResponseBody
	public Response<Object> registerProc(MultipartHttpServletRequest request) {
		Response<Object> ajaxResponse = new Response<>();

		String prodSubCateCombinedId = HttpUtil.get(request, "prodSubCateCombinedId", "");
		String prodName = HttpUtil.get(request, "prodName", "");
		long prodPrice = HttpUtil.get(request, "prodPrice", 0L);
		double prodDiscountPercent = HttpUtil.get(request, "prodDiscountPercent", 0.0);
		String prodInfo = HttpUtil.get(request, "prodInfo", "");
		String prodStatus = HttpUtil.get(request, "prodStatus", "");
		
		List<String> prodFilterOptionValueIdList = new ArrayList<>(Arrays.asList(HttpUtil.get(request, "filterOptions", "").split(",")));	
		List<String> prodVariantsOptionIdList = new ArrayList<>(Arrays.asList(HttpUtil.get(request, "varintsOptions", "").split(",")));
		List<String> combinations = new ArrayList<>(Arrays.asList(HttpUtil.get(request, "combinations", "").split(",")));
		List<String> combinationsText = new ArrayList<>(Arrays.asList(HttpUtil.get(request, "combinationsText", "").split(",")));
		List<Integer> combinationsStock = Arrays.stream(HttpUtil.get(request, "combinationsStock", "").split(",")).map(stock -> StringUtil.isEmpty(stock) ? 0 : Integer.parseInt(stock)) .collect(Collectors.toList());    
		List<VariantsOptionValueComb> variantsOptionValueCombList = new ArrayList<>();
		for (int i = 0; i < combinations.size(); i++) {
			VariantsOptionValueComb variantsOptionValueComb = new VariantsOptionValueComb();
			variantsOptionValueComb.setVariantsOptionValueCombId(combinations.get(i));
			variantsOptionValueComb.setVariantsOptionValueCombText(combinations.get(i));
			variantsOptionValueComb.setVariantsOptionValueCombStock(combinationsStock.get(i));
			variantsOptionValueComb.setVariantsOptionValueCombText(combinationsText.get(i));
			variantsOptionValueCombList.add(variantsOptionValueComb);
		}
		
		Prod prod = new Prod();
		prod.setProdSubCateCombinedId(prodSubCateCombinedId);
		prod.setProdName(prodName);
		prod.setProdPrice(prodPrice);
		prod.setProdDiscountPercent(prodDiscountPercent);
		prod.setProdInfo(prodInfo);
		prod.setProdStatus(prodStatus);
		
		try {
			if (prodService.prodInsert(prod, prodFilterOptionValueIdList, variantsOptionValueCombList, prodVariantsOptionIdList)) {
				String actualProdId = prodService.getActualProdId(prodSubCateCombinedId);
				String prodMainCateName = prodService.getProdMainCateName(prodSubCateCombinedId);
				HttpUtil.getFiles(request, "prodImage", PROD_IMG_DIR + FileUtil.getFileSeparator() + prodMainCateName  + FileUtil.getFileSeparator() + "mainImg", actualProdId);
				ajaxResponse.setResponse(200, "제품 등록 완료");
				
			} else {
				ajaxResponse.setResponse(500, "제품 등록 실패");
			}
			
		} catch (Exception e) {
			logger.error("[ProdController] registerProc Exception", e);
			ajaxResponse.setResponse(500, "제품 등록 실패");
		}
	
		return ajaxResponse;
	}
	
	@RequestMapping(value = "/prod/uploadImage", method = RequestMethod.POST)
	@ResponseBody
	public JsonObject uploadImage(MultipartHttpServletRequest request) {
		JsonObject jsonObject = new JsonObject();
		String prodMainCateName = HttpUtil.get(request, "prodMainCateName", "");
		String prodSubCateCombindedId = HttpUtil.get(request, "prodSubCateCombinedId", "");
		String expectedProdId = prodService.getExpectedProdId(prodSubCateCombindedId);
		FileData fileData = HttpUtil.getFile(request, "file", PROD_IMG_DIR + FileUtil.getFileSeparator() + prodMainCateName + FileUtil.getFileSeparator() + "infoImg", expectedProdId);
		
		StringBuilder srcFile = new StringBuilder();
		
		srcFile.append("/resources/prod-img")
			.append(FileUtil.getFileSeparator())
			.append(prodMainCateName)
			.append(FileUtil.getFileSeparator())
			.append("infoImg")
			.append(FileUtil.getFileSeparator())
			.append(fileData.getFileName());
		jsonObject.addProperty("url", srcFile.toString());
		jsonObject.addProperty("orgName", fileData.getFileOrgName());
	
		return jsonObject;
	}
}