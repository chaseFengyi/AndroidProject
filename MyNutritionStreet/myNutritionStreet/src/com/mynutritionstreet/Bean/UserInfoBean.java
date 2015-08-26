package com.mynutritionstreet.Bean;

public class UserInfoBean {
	private int userId;
	private String userName;
	private String userSex;
	private int userAge;
	private double userWeight;
	private String userPassword;
	private String userPhoto;
	private int questionStatus;
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
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public int getUserAge() {
		return userAge;
	}
	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}
	public double getUserWeight() {
		return userWeight;
	}
	public void setUserWeight(double userWeight) {
		this.userWeight = userWeight;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public int getQuestionStatus() {
		return questionStatus;
	}
	public void setQuestionStatus(int questionStatus) {
		this.questionStatus = questionStatus;
	}
	@Override
	public String toString() {
		return "UserInfoDemo [userId=" + userId + ", userName=" + userName
				+ ", userSex=" + userSex + ", userAge=" + userAge
				+ ", userWeight=" + userWeight + ", userPassword="
				+ userPassword + ", userPhoto=" + userPhoto
				+ ", questionStatus=" + questionStatus + "]";
	}
	
}
