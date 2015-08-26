package com.mynutritionstreet.application;

import java.util.List;

import com.mynutritionstreet.Bean.GoodInfoBean;
import com.mynutritionstreet.Bean.UserInfoBean;
import com.mynutritionstreet.util.VolleyUtil;

import android.app.Application;

public class MyApplication extends Application {
	private UserInfoBean userInfoBean;
	private List<GoodInfoBean> goodInfoBeans;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initRequestQueue();
	}
	 	
	
	//初始化请求队列
	private void initRequestQueue(){
		//初始化volley
		VolleyUtil.initialize(getApplicationContext());
	}


	public UserInfoBean getUserInfoBean() {
		return userInfoBean;
	}


	public void setUserInfoBean(UserInfoBean userInfoBean) {
		this.userInfoBean = userInfoBean;
	}


	public List<GoodInfoBean> getGoodInfoBeans() {
		return goodInfoBeans;
	}


	public void setGoodInfoBeans(List<GoodInfoBean> goodInfoBeans) {
		this.goodInfoBeans = goodInfoBeans;
	}


	@Override
	public String toString() {
		return "MyApplication [userInfoBean=" + userInfoBean
				+ ", goodInfoBeans=" + goodInfoBeans + "]";
	}

	
}
