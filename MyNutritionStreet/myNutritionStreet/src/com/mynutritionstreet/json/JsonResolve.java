package com.mynutritionstreet.json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mynutritionstreet.Bean.FoodCircleBean;
import com.mynutritionstreet.Bean.GoodInfoBean;
import com.mynutritionstreet.Bean.TypeInfoBean;
import com.mynutritionstreet.Bean.UserInfoBean;
import com.mynutritionstreet.util.StrinAndDate;

public class JsonResolve {
	/**
	 * ��������{"result":"success"}���ֶ�
	 * @param response
	 * @return  ����result��Ӧ������
	 */
	public static String get_result(String response){
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(response);
			return jsonObject.getString("result");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * �����û���½�ɹ��󷵻ص��û���Ϣ
	 * @param result
	 * @return
	 */
	public static UserInfoBean json_userinfo_result_login(String result){
		JSONObject jsonObject = null;
		UserInfoBean userInfoDemo = null;
		try {
			jsonObject = new JSONObject(result);
			userInfoDemo = new UserInfoBean();
			userInfoDemo.setUserAge(jsonObject.getInt("age"));
			userInfoDemo.setUserId(jsonObject.getInt("id"));
			userInfoDemo.setUserPassword(jsonObject.getString("password"));
			userInfoDemo.setQuestionStatus(jsonObject.getInt("questionStatus"));
			userInfoDemo.setUserSex(jsonObject.getString("sex"));
			userInfoDemo.setUserName(jsonObject.getString("userName"));
			userInfoDemo.setUserPhoto(jsonObject.getString("userPhoto"));
			userInfoDemo.setUserWeight(jsonObject.getDouble("weight"));
			return userInfoDemo;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ������½�ɹ��󷵻ص���Ʒ��Ϣ
	 * @param result
	 * @return
	 */
	public static List<GoodInfoBean> json_goodsinfo_result_login(String result){
		JSONObject jsonObject = null;
		List<GoodInfoBean> goods = null;
		try {
			jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("goods");
			if(jsonArray.length() > 0){
				goods = new ArrayList<GoodInfoBean>();
				for(int i = 0; i < jsonArray.length(); i++){
					JSONObject object = jsonArray.getJSONObject(i);
					GoodInfoBean goodInfoBean = new GoodInfoBean();
					goodInfoBean.setGoodsName(object.getString("goodsName"));
					goodInfoBean.setGoodsPhoto(object.getString("goodsPhoto"));
					goodInfoBean.setId(object.getInt("id"));
					goodInfoBean.setNutritionEffects(object.getString("nutritionalEffects"));
					goodInfoBean.setPublishTime(object.getString("publishTime"));
					goodInfoBean.setStep(object.getString("step"));
					goodInfoBean.setSuitablePerson(object.getString("suitablPerson"));
					List<TypeInfoBean> types = null;
					JSONArray array = object.getJSONArray("types");
					if(array.length() > 0){
						types = new ArrayList<TypeInfoBean>();
						for(int j = 0;  j< array.length(); j++){
							JSONObject obj_type = array.getJSONObject(j);
							TypeInfoBean typeInfoBean = new TypeInfoBean();
							typeInfoBean.setId(obj_type.getInt("id"));
							typeInfoBean.setTypeName(obj_type.getString("typeName"));
							types.add(typeInfoBean);
						}
					}
					goods.add(goodInfoBean);
					
				}
			}
			
			return goods;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * �����������Ͳ�ѯgoods��������
	 * @param response
	 * @return
	 */
	public static List<GoodInfoBean> json_goods_query_by_type(String response){
		JSONObject jsonObject = null;
		List<GoodInfoBean> goods = null;
		try {
			jsonObject = new JSONObject(response);
			JSONArray array = jsonObject.getJSONArray("result");
			if(array.length() > 0){
				goods = new ArrayList<GoodInfoBean>();
				for(int i = 0; i < array.length(); i++){
					JSONObject object = array.getJSONObject(i);
					GoodInfoBean goodInfoBean = new GoodInfoBean();
					goodInfoBean.setGoodsName(object.getString("goodsName"));
					goodInfoBean.setGoodsPhoto(object.getString("goodsPhoto"));
					goodInfoBean.setId(object.getInt("id"));
					goodInfoBean.setNutritionEffects(object.getString("nutritionalEffects"));
					goodInfoBean.setPublishTime(object.getString("publishTime"));
					goodInfoBean.setStep(object.getString("step"));
					goodInfoBean.setSuitablePerson(object.getString("suitablPerson"));
					goods.add(goodInfoBean);
				}
			}
			return goods;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return goods;
	}
	
	/**
	 * ������ѯ��ʳȦ��Ϣ
	 * @param response
	 */
	public static List<FoodCircleBean> json_query_good_food(String response){
		JSONObject jsonObject = null;
		List<FoodCircleBean> foods = null;
		try {
			jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("result");
			if(jsonArray.length() > 0){
				foods = new ArrayList<FoodCircleBean>();
				for(int i = 0; i < jsonArray.length(); i++){
					JSONObject object = jsonArray.getJSONObject(i);
					FoodCircleBean bean = new FoodCircleBean();
					bean.setId(object.getInt("id"));
					bean.setPublishContent(object.getString("publishContent"));
					bean.setPublishPicture(object.getString("publishPicture"));
					bean.setPublishTime(object.getString("publishTime"));
//					JSONObject user = object.getJSONObject("user");
					Log.i("json", (object.get("user") == null)+"");
					System.out.println("hhh="+(object.get("user")));
					if(object.get("user") == null || object.get("user").equals(null)){
						bean.setUser(null);
					}else{
						JSONObject user = object.getJSONObject("user");
						UserInfoBean infoBean = new UserInfoBean();
						infoBean.setUserId(user.getInt("id"));
						infoBean.setUserAge(user.getInt("age"));
						infoBean.setUserPassword(user.getString("password"));
						infoBean.setQuestionStatus(user.getInt("questionStatus"));
						infoBean.setUserSex(user.getString("sex"));
						infoBean.setUserName(user.getString("userName"));
						infoBean.setUserPhoto(user.getString("userPhoto"));
						infoBean.setUserWeight(user.getDouble("weight"));
						bean.setUser(infoBean);
					}
					foods.add(bean);
				}
			}
			return foods;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
