/**
 * <pre>
 * 프로젝트명 : Manager
 * 패키지명   : com.icia.manager.controller
 * 파일명     : IndexController.java
 * 작성일     : 2021. 7. 30.
 * 작성자     : daekk
 * </pre>
 */
package com.sist.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sist.common.util.CookieUtil;
import com.sist.common.util.HttpUtil;
import com.sist.common.util.StringUtil;
import com.sist.manager.model.Admin;
import com.sist.manager.model.Response;
import com.sist.manager.service.AdminService;

/**
 * <pre>
 * 패키지명   : com.icia.manager.controller
 * 파일명     : IndexController.java
 * 작성일     : 2021. 7. 30.
 * 작성자     : daekk
 * 설명       : 인덱스 컨트롤러
 * </pre>
 */
@Controller("indexController")
public class IndexController {
	public static Logger logger = LoggerFactory.getLogger(IndexController.class);

	// 쿠키명
	@Value("#{env['auth.cookie.name']}")
	private String AUTH_COOKIE_NAME;
	
	@Autowired
	private AdminService adminService;

	/**
	 * <pre>
	 * 메소드명   : index
	 * 작성일     : 2021. 7. 30.
	 * 작성자     : daekk
	 * 설명       : 인덱스
	 * </pre>
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @return String
	 */
	@RequestMapping(value = "/index")
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		if (CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null) {
			return "redirect:/user/list";
		} else {
			return "/index";
		}
	}

	// 관리자 로그인
	@RequestMapping(value = "/loginProc", method = RequestMethod.POST)
	@ResponseBody
	public Response<Object> loginProc(HttpServletRequest request, HttpServletResponse response) {
		Response<Object> ajaxResponse = new Response<>();

		String admId = HttpUtil.get(request, "admId", "");
		String admPwd = HttpUtil.get(request, "admPwd", "");

		if (!StringUtil.isEmpty(admId) && !StringUtil.isEmpty(admPwd)) {
			Admin admin = adminService.adminSelect(admId);
			
			if (admin != null) {
				if (StringUtil.equals(admin.getStatus(), "Y")) {
					if (StringUtil.equals(admin.getAdmPwd(), admPwd)) {
						CookieUtil.addCookie(response, "/", -1, AUTH_COOKIE_NAME, CookieUtil.stringToHex(admId));
						ajaxResponse.setResponse(200, "로그인 성공");
					} else {
						ajaxResponse.setResponse(401, "비밀번호 일치하지 않음");
					}
				} else {
					ajaxResponse.setResponse(403, "정지된 관리자");
				}
			} else {
				ajaxResponse.setResponse(404, "아이디 존재하지 않음");
			}
		} else {
			ajaxResponse.setResponse(400, "비정상적인 접근");
		}

		return ajaxResponse;
	}
	
	@RequestMapping(value = "/loginOut")
	public String loginOut(HttpServletRequest request, HttpServletResponse response) {
		
		if (CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null) {
			CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);
		}
		
		return "redirect:/";
	}
}