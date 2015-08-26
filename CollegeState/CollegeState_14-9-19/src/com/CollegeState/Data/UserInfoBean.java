package com.CollegeState.Data;

/**
 * 保存用户的单例信息，全部采用静态，方便程序使用
 * 
 * @author zc
 * 
 */
public class UserInfoBean {
	private static String orderAddress = "";
	private static String userAuthority = "";
	private static String userCredit = "";
	private static String userId = "";
	private static String userPasswd = "";
	private static String userPhone = "";
	private static String userChooseAreaId = "1";
	private static String cityId = "1001";
	private static String action = "";
	private static boolean isLogin = false;

	public static String getOrderAddress() {
		return orderAddress;
	}

	public static void setOrderAddress(String orderAddress) {
		UserInfoBean.orderAddress = orderAddress;
	}

	public static String getUserAuthority() {
		return userAuthority;
	}

	public static void setUserAuthority(String userAuthority) {
		UserInfoBean.userAuthority = userAuthority;
	}

	public static String getUserCredit() {
		return userCredit;
	}

	public static void setUserCredit(String userCredit) {
		UserInfoBean.userCredit = userCredit;
	}

	public static String getUserId() {
		return userId;
	}

	public static void setUserId(String userId) {
		UserInfoBean.userId = userId;
	}

	public static String getUserPasswd() {
		return userPasswd;
	}

	public static void setUserPasswd(String userPasswd) {
		UserInfoBean.userPasswd = userPasswd;
	}

	public static String getUserPhone() {
		return userPhone;
	}

	public static void setUserPhone(String userPhone) {
		UserInfoBean.userPhone = userPhone;
	}

	public static boolean isLogin() {
		return isLogin;
	}

	public static void setLogin(boolean isLogin) {
		UserInfoBean.isLogin = isLogin;
	}

	public static String getUserChooseAreaId() {
		return userChooseAreaId;
	}

	public static void setUserChooseAreaId(String userChooseAreaId) {
		UserInfoBean.userChooseAreaId = userChooseAreaId;
	}

	public static String getCityId() {
		return cityId;
	}

	public static void setCityId(String cityId) {
		UserInfoBean.cityId = cityId;
	}

	public static String getAction() {
		return action;
	}

	public static void setAction(String action) {
		UserInfoBean.action = action;
	}
}
