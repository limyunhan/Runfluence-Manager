package com.sist.manager.dao;

import org.springframework.stereotype.Repository;

import com.sist.manager.model.Admin;

@Repository
public interface AdminDao {
	// 관리자 조회 (로그인 시 사용)
	public abstract Admin adminSelect(String admId);
}