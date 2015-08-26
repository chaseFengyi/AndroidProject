package com.coolReader.request;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ����json��ʽ
 * @author FengYi~
 *2015��8��13��16:07:45
 */
public class ConstructJsonFormat {
	/**
	 * �����������json��ʽ
	 * @param url
	 * @param mailAddr
	 * @return
	 */
	public static String json_add(String url, String mailAddr){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("funType", 50).put("funContent", new JSONObject().put("mailAddr", mailAddr).put("deviceType", "android").put("url", url).put("tag", ""));
			return jsonObject.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	//����ɾ�����ݸ�ʽ��
	public static String json_delete(String url,String mailAddr){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("funType", 40).put("funContent", new JSONObject().put("mailAddr", mailAddr).put("deviceType", "android").put("url", url));
			return jsonObject.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * �����ѯjson��ʽ
	 * @param mailAddr �û�����
	 * @param keywords ��ȡ��ѯ�ؼ���
	 * @return
	 */
	public static String json_query(String mailAddr, String keywords){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("funType", 60).put("funContent", new JSONObject().put("mailAddr", mailAddr).put("keywords", keywords));
			
			return jsonObject.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static String json_synchroData(String mailAddr,List<String> urls){
		JSONObject jsonObject = new JSONObject();
		try {
			JSONArray jsonArray = null;
			if(urls != null){
				jsonArray = new JSONArray();
				for(int i = 0; i < urls.size(); i++){
					jsonArray.put(urls.get(i));
				}
			}
			
			jsonObject.put("funType", 30).put("funContent", new JSONObject().put("mailAddr", mailAddr).put("deviceType", "android").put("url", jsonArray));
			return jsonObject.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
