package com.allnotes.activity.login;

import org.json.JSONException;
import org.json.JSONObject;

import com.allnotes.bean.User;

public class LoginJsonUtil {
	/**
	 * 保存用户信息
	 * 
	 * @param response
	 */
	public static User saveUserInfo(String response, String email) {
		User user = null;
		try {
			user = new User();
			JSONObject obj = new JSONObject(response);
			JSONObject data = obj.getJSONObject("data");
			user.setUserId(data.getString("userid"));
			if (data.getString("username").equals("null")) {
				user.setUserName("用户" + data.getString("userid"));
			} else {
				user.setUserName(data.getString("username"));
			}
			user.setSex(data.getString("sex"));
			user.setShareNote(data.getString("sharenote"));
			user.setShareRead(data.getString("shareread"));
			user.setTel(data.getString("tel"));
			user.setDesc(data.getString("desc"));
			user.setCareer(data.getString("career"));
			user.setEdu(data.getString("edu"));
			user.setLocal(data.getString("local"));
			user.setUserimg(data.getString("userimg"));
			user.setCheck(data.getString("check"));
			user.setEmail(email);
			return user;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}
}
