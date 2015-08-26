package com.example.sendlist;

public class UserInfo {
	private String action;
	private int areaId;
	private String orderAddress;
	private String userAuthority;
	private int userCredit;
	private long userId;
	private String userIdCardNum;
	private String userName;
	private String userPasswd;
	private String userPhone;
	private String userRegiTimeTime;
	private String workerNum;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public String getOrderAddress() {
		return orderAddress;
	}
	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}
	public String getUserAuthority() {
		return userAuthority;
	}
	public void setUserAuthority(String userAuthority) {
		this.userAuthority = userAuthority;
	}
	public int getUserCredit() {
		return userCredit;
	}
	public void setUserCredit(int userCredit) {
		this.userCredit = userCredit;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserIdCardNum() {
		return userIdCardNum;
	}
	public void setUserIdCardNum(String userIdCardNum) {
		this.userIdCardNum = userIdCardNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPasswd() {
		return userPasswd;
	}
	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserRegiTimeTime() {
		return userRegiTimeTime;
	}
	public void setUserRegiTimeTime(String userRegiTimeTime) {
		this.userRegiTimeTime = userRegiTimeTime;
	}
	public String getWorkerNum() {
		return workerNum;
	}
	public void setWorkerNum(String workerNum) {
		this.workerNum = workerNum;
	}
	@Override
	public String toString() {
		return "{\"action\":"+action+",\"areaId\":"+areaId+"," +
				"\"orderAddress\":"+orderAddress+",\"userAuthority" +
				"\":"+userAuthority+"\",\"userCredit\":"+userCredit+
				"\",\"userId\":"+userId+",\"userIdCardNum" +"\":"+
				userIdCardNum+",\"userName\":"+userName+"," +"\"" +
				"userPasswd\":\""+userPasswd+",\"userPhone\":\""+
				userPhone+",\"userRegiTimeTime\":\""+userRegiTimeTime+
				"\",\"workerNum\":"+workerNum+"}";
	}
	
	
}
