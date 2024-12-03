package com.sist.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.manager.dao.AdminDao;
import com.sist.manager.model.Admin;

@Service("adminService")
public class AdminService {
	public static Logger logger = LoggerFactory.getLogger(AdminService.class);
	
	@Autowired
	private AdminDao adminDao;
	
	
	public Admin adminSelect(String admId) {
		Admin admin = null;
		
		try {
			admin = adminDao.adminSelect(admId);
		} catch (Exception e) {
			logger.error("[AdminService] adminSelect Exception", e);
		}
		
		return admin;
	}
}