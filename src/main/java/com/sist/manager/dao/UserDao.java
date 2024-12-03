package com.sist.manager.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sist.manager.model.User;

@Repository
public interface UserDao {
	public abstract List<User> userList(User search);
	public abstract int userListCount(User search);
	public abstract User userSelect(String userId);
	public abstract int userUpdate(User user);
}