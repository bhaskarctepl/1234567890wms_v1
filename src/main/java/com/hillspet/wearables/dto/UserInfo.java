package com.hillspet.wearables.dto;

public class UserInfo {
	private Integer userId;
	private String userName;
	private String email;
	private String password;
	private String phoneNumber;
	private Integer needToChangePassword;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getNeedToChangePassword() {
		return needToChangePassword;
	}

	public void setNeedToChangePassword(Integer needToChangePassword) {
		this.needToChangePassword = needToChangePassword;
	}

}
