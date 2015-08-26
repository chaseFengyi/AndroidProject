package com.coolReader.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coolReader.Bean.URLInfoBean;

public class JsonResolve {
	/**
	 * 解析形如
	 * {
		"funType":202,
		"describe":"SERVER_ERROR"
		}
		的数据
	 * @param string
	 * @return  返回字段describe的内容
	 */
	public static String fun_des_resolve(String string){
		String describe = null;
		try {
			JSONObject jsonObject = new JSONObject(string);
			describe = jsonObject.getString("describe");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return describe;
	}
	
	/**
	 * @param string
	 * @return  返回字段funType的内容
	 */
	public static int getFunType_resolve(String string){
		int funType = 0;
		try {
			JSONObject jsonObject = new JSONObject(string);
			funType = jsonObject.getInt("funType");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return funType;
	} 
	
	/**
	 * 解析形如
	 * {
		"funType":37,
		"content":{"url":"" , "title":"" , "info":"","urlCount":5}
		}
		格式的内容
	 * @param string
	 * @return 返回content的内容
	 */
	public static URLInfoBean getUrlContent(String string){
		URLInfoBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject(string);
			bean = new URLInfoBean();
			JSONObject object = jsonObject.getJSONObject("content");
			bean.setUrlLink(object.getString("url"));
			bean.setUrlTitle(object.getString("title"));
			bean.setUrlContent(object.getString("info"));
			bean.setUrlTag(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 解析同步数据返回
	 * @param receive
	 * @return
	 */
	public static List<Map<String, Object>> json_synchro(String receive){
		List<Map<String, Object>> data = null;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(receive);
			JSONObject pcDeleted = jsonObject.getJSONObject("return1");
			//存储pc删除的url
			List<String> pc = null ;
			if(pcDeleted.get("content") == null || pcDeleted.get("content").equals(null)){
				pc = null;
			}else{
				pc = new ArrayList<String>();
				JSONArray pcDeleted_url = pcDeleted.getJSONArray("content");
				for(int i = 0; i < pcDeleted_url.length(); i++){
					pc.add(pcDeleted_url.getString(i));
				}
			}
			//存储服务器删除Android端要删除的标志
			String describe = jsonObject.getJSONObject("return2").getString("describe");
			//存储要同步添加的url
			List<URLInfoBean> add = null;
			JSONObject addObject = jsonObject.getJSONObject("return3");
			if(addObject.get("content") == null || addObject.get("content").equals(null)){
				add = null;
			}else{
				add = new ArrayList<URLInfoBean>();
				JSONArray addArray = addObject.getJSONArray("content");
				for(int i = 0; i < addArray.length(); i++){
					JSONObject object = addArray.getJSONObject(i);
					URLInfoBean bean = new URLInfoBean();
					bean.setUrlLink(object.getString("url"));
					bean.setUrlTitle(object.getString("title"));
					bean.setUrlContent(object.getString("info"));
					bean.setUrlTag(0);
					add.add(bean);
				}
			}
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("return1", pc);
			Map<String, Object> map2 = new HashMap<String, Object>();
			map1.put("return2", describe);
			Map<String, Object> map3 = new HashMap<String, Object>();
			map1.put("return3", add);
			data.add(map1);
			data.add(map2);
			data.add(map3);
			return data;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
