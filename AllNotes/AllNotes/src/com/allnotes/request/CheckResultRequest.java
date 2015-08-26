package com.allnotes.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ��֤���������صĽ��
 * @author FengYi~
 *2015��7��29��14:35:56
 */
public class CheckResultRequest {
	/**
	 * ��֤������Ϣ�ϴ���� /**
	 * 
	 * @param response
	 * @return ����json��dataֵ������
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
