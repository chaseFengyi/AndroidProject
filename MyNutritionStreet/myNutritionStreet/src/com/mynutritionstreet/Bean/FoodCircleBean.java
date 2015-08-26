package com.mynutritionstreet.Bean;

public class FoodCircleBean {
	private int id;
	private String publishContent;
	private String publishPicture;
	private String publishTime;
	private UserInfoBean user;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPublishContent() {
		return publishContent;
	}
	public void setPublishContent(String publishContent) {
		this.publishContent = publishContent;
	}
	public String getPublishPicture() {
		return publishPicture;
	}
	public void setPublishPicture(String publishPicture) {
		this.publishPicture = publishPicture;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public UserInfoBean getUser() {
		return user;
	}
	public void setUser(UserInfoBean user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "FoodCircleBean [id=" + id + ", publishContent="
				+ publishContent + ", publishPicture=" + publishPicture
				+ ", publishTime=" + publishTime + ", user=" + user + "]";
	}
	
}
