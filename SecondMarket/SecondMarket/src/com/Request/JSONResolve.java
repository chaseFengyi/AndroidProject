package com.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.secondmarket.bean.SortSearchDemo;
import com.secondmarket.bean.UserInfoDemo;

public class JSONResolve {
	//1.µÇÂ½
	public static List<UserInfoDemo> getLoadInfoAfterJson(String jsonString){
		List<UserInfoDemo> list = new ArrayList<UserInfoDemo>();
		System.out.println("----->");
		try{
//			JSONArray jsonArray = new JSONArray(jsonString);
//			for(int i = 0; i < jsonArray.length(); i++){
			JSONObject jsonObject = new JSONObject(jsonString);
				UserInfoDemo userInfoDemo = new UserInfoDemo();
//				JSONObject jsonObject = jsonArray.getJSONObject(i);
				userInfoDemo.setUserId(jsonObject.getInt("userId"));
				System.out.println(userInfoDemo.getUserId());
				userInfoDemo.setUserGrade(jsonObject.getString("userGrade"));
				System.out.println(userInfoDemo.getUserGrade());
				userInfoDemo.setUserName(jsonObject.getString("userName"));
				System.out.println(userInfoDemo.getUserName());
				userInfoDemo.setUserNike(jsonObject.getString("userNike"));
				System.out.println(userInfoDemo.getUserNike());
				userInfoDemo.setUserPassword(jsonObject.getString("userPassword"));
				System.out.println(userInfoDemo.getUserPassword());
				userInfoDemo.setUserPictureAd(jsonObject.getString("userPictureAd"));
				System.out.println(userInfoDemo.getUserPictureAd());
				userInfoDemo.setUserSchoolNum(jsonObject.getString("userSchoolNum"));
				System.out.println(userInfoDemo.getUserSchoolNum());
				userInfoDemo.setUserSex(jsonObject.getString("userSex"));
				System.out.println(userInfoDemo.getUserSex());
				list.add(userInfoDemo);
//			}
			
			return list;
		}catch(JSONException e){
			e.printStackTrace();
		}
		return list;
	}
	
	//·ÖÀà
	public static List<SortSearchDemo> getSortInfoAfterJson(String jsonString){
		List<SortSearchDemo> list = new ArrayList<SortSearchDemo>();
		List<Map<String, String>> list1;
		try{
			JSONArray jsonArray = new JSONArray(jsonString);
			for(int i = 0; i < jsonArray.length(); i++){
				SortSearchDemo sortSearchDemo = new SortSearchDemo();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				sortSearchDemo.setIsSale(jsonObject.getInt("isSale"));
				sortSearchDemo.setGoodsId(jsonObject.getInt("goodsId"));
				sortSearchDemo.setGoodsConnect(jsonObject.getString("goodsConnect"));
				sortSearchDemo.setUserId(jsonObject.getInt("userId"));
				sortSearchDemo.setGoodsDescribe(jsonObject.getString("goodsDescribe"));
				sortSearchDemo.setGoodsTypeId(jsonObject.getString("goodsTypeId"));
				sortSearchDemo.setGoodsPrice(jsonObject.getString("goodsPrice"));
				sortSearchDemo.setGoodsWanted(jsonObject.getInt("goodsWanted"));
				sortSearchDemo.setGoodsPublishTime(jsonObject.getString("goodsPublishTime"));
				sortSearchDemo.setGoodsName(jsonObject.getString("goodsName"));
				JSONArray array = jsonObject.getJSONArray("goodsPictureAD");
				Map<String, String> map = new HashMap<String, String>();
				for(int j=0; j<array.length(); j++){
					map.put("goodsPictureAD"+(j+1), array.getString(j));
				}
				list1 = new ArrayList<Map<String,String>>();
				list1.add(map);
				sortSearchDemo.setList(list1);
				list.add(sortSearchDemo);
			}
			
			return list;
		}catch(JSONException e){
			e.printStackTrace();
		}
		return list;
	}
	
}
