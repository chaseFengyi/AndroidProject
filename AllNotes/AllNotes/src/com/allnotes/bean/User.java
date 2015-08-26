package com.allnotes.bean;

public class User {
	private String userId;
	private String userName;
	private String sex;
	private String shareNote;// 0或1,0不分享笔记,1分享.默认为1
	private String shareRead;// 0或1,同上.分享阅读
	private String tel;// 电话
	private String desc;// 个人简介
	private String career;// 职业
	private String edu;// 学校
	private String local;// 位置
	private String userimg;// 用户头像
	private String check;// 验证
	private String email;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getShareNote() {
		return shareNote;
	}

	public void setShareNote(String shareNote) {
		this.shareNote = shareNote;
	}

	public String getShareRead() {
		return shareRead;
	}

	public void setShareRead(String shareRead) {
		this.shareRead = shareRead;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getEdu() {
		return edu;
	}

	public void setEdu(String edu) {
		this.edu = edu;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
