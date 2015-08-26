package com.coolReader.Bean;

public class UserInfoBean {
	private int userId;
	private String userName;
	private String userPass;
	private String userSex;
	private String userEmail;
	private String userRegisterDate;
	private String userPicture;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserRegisterDate() {
		return userRegisterDate;
	}
	public void setUserRegisterDate(String userRegisterDate) {
		this.userRegisterDate = userRegisterDate;
	}
	public String getUserPicture() {
		return userPicture;
	}
	public void setUserPicture(String userPicture) {
		this.userPicture = userPicture;
	}
	@Override
	public String toString() {
		return "UserInfoBean [userId=" + userId + ", userName=" + userName
				+ ", userPass=" + userPass + ", userSex=" + userSex
				+ ", userEmail=" + userEmail + ", userRegisterDate="
				+ userRegisterDate + ", userPicture=" + userPicture + "]";
	}
	

}
