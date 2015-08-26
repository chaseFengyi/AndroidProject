package com.allnotes.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 验证服务器返回的结果
 * @author FengYi~
 *2015年7月29日14:35:56
 */
public class CheckResultRequest {
	/**
	 * 验证分类信息上传结果 /**
	 * 
	 * @param response
	 * @return 返回json中data值的内容
	 */
	public static String checkResult(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			String code = (String) jsonObject.get("code");
			if (code.equals("1")) {
				return jsonObject.getString("data");
			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
