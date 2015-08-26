package com.market.utils;

import java.util.HashMap;
import java.util.Map;

import com.market.model.HttpResponsBean;
import com.market.model.StringBean;
import com.market.model.UserInfoBean;

public class getInfoFromServer {
	static String SId = null;
	static String result = null;

	public static UserInfoBean getNameFromServer(UserInfoBean userInfo) {
		// 登陆获得sessionID和html信息
		Map<String, String> param = new HashMap<String, String>();
		param.put("__VIEWSTATE",
				"dDw1MjQ2ODMxNzY7Oz4AG9yzNM0K25AybGLl5cV%2F3NvN4g%3D%3D");
		param.put("TextBox1", userInfo.getUserSchoolNum());
		param.put("TextBox2", userInfo.getUserPassword());
		param.put("RadioButtonList1", "%D1%A7%C9%FA");
		param.put("Button1", "++%B5%C7%C2%BC++");

		try {
			HttpRequester request = new HttpRequester();
			HttpResponsBean hr = request.sendGet(
					"http://222.24.19.201/default_ysdx.aspx", param, null);
			result = hr.getContent();
			SId = hr.getSessionId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 通过html判断登陆是否成功
		if (result.contains(StringBean.USER_PASSWD_ERROR)) {
			userInfo.setUserName(StringBean.USER_PASSWD_ERROR);
			return userInfo;
		} else if (result.contains(StringBean.USER_NOT_EXIST)) {
			userInfo.setUserName(StringBean.USER_NOT_EXIST);
			return userInfo;
		}

		// 登陆成功后获取用户姓名
		try {
			HttpRequester request = new HttpRequester();
			HttpResponsBean hr = request.sendGet(
					"http://222.24.19.201/xscjcx.aspx?xh="
							+ userInfo.getUserSchoolNum(), SId);
			String temp = hr.getContent();
			userInfo.setUserName(temp.substring(temp.indexOf("姓名：") + 3,
					temp.indexOf("姓名：") + 6));
			return userInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
