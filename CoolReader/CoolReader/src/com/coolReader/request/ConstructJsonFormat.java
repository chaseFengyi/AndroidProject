package com.coolReader.request;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 构造json格式
 * @author FengYi~
 *2015年8月13日16:07:45
 */
public class ConstructJsonFormat {
	/**
	 * 构造添加数据json格式
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
	
	//构造删除数据格式串
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
	 * 构造查询json格式
	 * @param mailAddr 用户邮箱
	 * @param keywords 获取查询关键字
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
