package com.sist.manager.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.manager.dao.UserDao;
import com.sist.manager.model.User;

@Service("userService")
public class UserService {
	public static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserDao userDao;
	
	public List<User> userList(User search) {
		List<User> list = null;
		
		try {
			list = userDao.userList(search);
		
		} catch (Exception e) {
			logger.error("[UserService] userList Exception", e);
		}
		
		return list;
	}
	
	public int userListCount(User search) {
		int cnt = 0;
		
		try {
			cnt = userDao.userListCount(search);
		
		} catch (Exception e) {
			logger.error("[UserService] userListCnt Exception", e);
		}
		
		return cnt;
	}
	
	public User userSelect(String userId) {
		User user = null;
		
		try {
			user = userDao.userSelect(userId);
		} catch (Exception e) {
			logger.error("[UserService] userSelect Exception", e);
		}
		
		return user;
	}
	
	public boolean userUpdate(User user) {
		int cnt = 0;
		
		try {
			cnt = userDao.userUpdate(user);
		} catch (Exception e) {
			logger.error("[UserService] userUpdate Excetpion", e);
		}
		
		return (cnt == 1);
	}
}