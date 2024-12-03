package com.sist.manager.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sist.common.util.HttpUtil;
import com.sist.common.util.StringUtil;
import com.sist.manager.model.Paging;
import com.sist.manager.model.Response;
import com.sist.manager.model.User;
import com.sist.manager.service.UserService;

@Controller("userController")
public class UserController {
	public static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	private static final int LIST_COUNT = 3;
	private static final int PAGE_COUNT = 5;
	
	@RequestMapping(value = "/user/list")
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
		int curPage = HttpUtil.get(request, "curPage", 1);
		String searchType = HttpUtil.get(request, "searchType", ""); // 1은 아이디, 2는 이름
		String searchValue = HttpUtil.get(request, "searchValue", "");
		String status = HttpUtil.get(request, "status", "");
		String gubun = "";
		
		User search = new User();
		search.setStatus(status);
		gubun = (!StringUtil.isEmpty(status)) ? "Y" : "";
		
		if (!StringUtil.isEmpty(searchType) && !StringUtil.isEmpty(searchValue)) {
			if (StringUtil.equals(searchType, "1")) {
				search.setUserId(searchValue);
				gubun = "Y";
			} else if (StringUtil.equals(searchType, "2")) {
				search.setUserName(searchValue);
				gubun = "Y";
				
			} else {
				searchType = "";
				searchValue = "";
				gubun = "";
			}
			
		} else {
			searchType = "";
			searchValue = "";
			gubun = "";
		}
		
		int totalCnt = userService.userListCount(search);
		List<User> userList = null;
		Paging paging = null;
		
		if (totalCnt > 0) {
			paging = new Paging("/user/list", totalCnt, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
			search.setStartRow(paging.getStartRow());
			search.setEndRow(paging.getEndRow());
			userList = userService.userList(search);
		}
		
		model.addAttribute("userList", userList);
		model.addAttribute("paging", paging);
		model.addAttribute("searchType", searchType);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage", curPage);
		model.addAttribute("status", status);
		model.addAttribute("gubun", gubun);
	
		return "/user/list";
	}
	
	@RequestMapping(value = "/user/update")
	public String update(Model model, HttpServletRequest request, HttpServletResponse response) {
		String userId = HttpUtil.get(request, "userId", "");
		
		User user = null;
		
		if (!StringUtil.isEmpty(userId)) {
			user = userService.userSelect(userId);
		}
		
		if (user != null) model.addAttribute("user", user);
		
		return "/user/update";
	}
	
	@RequestMapping(value = "/user/updateProc", method = RequestMethod.POST)
	@ResponseBody
	public Response<Object> updateProc(HttpServletRequest request, HttpServletResponse response) {
		Response<Object> ajaxResponse = new Response<>();
		
		String userId = HttpUtil.get(request, "userId", "");
		String userPwd = HttpUtil.get(request, "userPwd", "");
		String userName = HttpUtil.get(request, "userName", "");
		String userEmail = HttpUtil.get(request, "userEmail", "");
		String status = HttpUtil.get(request, "status", "");
		
		if (!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(userPwd) && !StringUtil.isEmpty(userName) && !StringUtil.isEmpty(userEmail) && !StringUtil.isEmpty(status)) {
			User user = userService.userSelect(userId);
			
			if (user != null) {
				user.setUserPwd(userPwd);
				user.setUserName(userName);
				user.setUserEmail(userEmail);
				user.setStatus(status);
				
				if (userService.userUpdate(user)) {
					ajaxResponse.setResponse(200, "유저 정보 수정 완료");
				} else {
					ajaxResponse.setResponse(500, "DB 정합성 오류");
				}
				
			} else {
				ajaxResponse.setResponse(404, "사용자가 존재하지 않음");
			}
			
		} else {
			ajaxResponse.setResponse(400, "비정상적인 접근");
		}
			
		return ajaxResponse;
	}
}