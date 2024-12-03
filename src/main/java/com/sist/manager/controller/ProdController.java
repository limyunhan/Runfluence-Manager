package com.sist.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sist.manager.model.VariantsOption;
import com.sist.manager.model.VariantsOptionValue;
import com.sist.manager.service.ProdService;

@Controller
public class ProdController {
	public static Logger logger = LoggerFactory.getLogger(ProdController.class);

	@Autowired
	private ProdService prodService;

	@RequestMapping(value = "/prod/register")
	public String register(Model model, HttpServletRequest request) {

		List<VariantsOption> variantsOptionList = prodService.variantsOptionList();
		List<VariantsOptionValue> variantsOptionValueList = prodService.variantsOptionValueList();
		Map<String, List<VariantsOptionValue>> variantsOptionValueListMap = new HashMap<>();

		for (VariantsOptionValue variantsOptionValue : variantsOptionValueList) {
			variantsOptionValueListMap
					.computeIfAbsent(variantsOptionValue.getVariantsOptionId(), k -> new ArrayList<>())
					.add(variantsOptionValue);
		}

		model.addAttribute("variantsOptionList", variantsOptionList);
		model.addAttribute("variantsOptionValueListMap", variantsOptionValueListMap);

		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			String variantsOptionValueListMapJsonString = objectMapper.writeValueAsString(variantsOptionValueListMap);
			model.addAttribute("variantsOptionValueListMapJsonString", variantsOptionValueListMapJsonString);
		
		} catch (JsonProcessingException e) {
			logger.error("[ProdController] register Exception", e);
		}
		
		return "/prod/register";
	}

}