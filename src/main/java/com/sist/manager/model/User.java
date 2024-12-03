package com.sist.manager.model;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private String userId;
    private String userPwd;
    private String userName;
    private String userEmail;
    private String status;
    private String regDate;
    
    private int startRow;
    private int endRow;
    
    public User() {
    	userId = "";
    	userPwd = "";
    	userName = "";
    	userEmail = "";
    	status = ""; // 관리자 페이지에서 검색 조건에 사용하므로 초기 값 없이 설정
    	regDate = "";
    	startRow = 0;
    	endRow = 0;
    }

	public String getUserId() {return userId;}
	public void setUserId(String userId) {this.userId = userId;}
	public String getUserPwd() {return userPwd;}
	public void setUserPwd(String userPwd) {this.userPwd = userPwd;}
	public String getUserName() {return userName;}
	public void setUserName(String userName) {this.userName = userName;}
	public String getUserEmail() {return userEmail;}
	public void setUserEmail(String userEmail) {this.userEmail = userEmail;}
	public String getStatus() {return status;}
	public void setStatus(String status) {this.status = status;}
	public String getRegDate() {return regDate;}
	public void setRegDate(String regDate) {this.regDate = regDate;}
	public int getStartRow() {return startRow;}
	public void setStartRow(int startRow) {this.startRow = startRow;}
	public int getEndRow() {return endRow;}
	public void setEndRow(int endRow) {this.endRow = endRow;}
}