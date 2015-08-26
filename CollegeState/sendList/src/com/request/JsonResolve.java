package com.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.sendlist.Login;
import com.example.sendlist.UserInfo;
import com.get.AfterGetInfo;
import com.get.GetOrderInfo;
import com.send.AfterSendInfo;
import com.send.SendOrderInfo;


public class JsonResolve {
	//获取商铺信息
	public static List<Map<String, Object>> getShopInfoAfterJson(String jsonString){
		List<Map<String, Object>> list = null;
		try{
			list = new ArrayList<Map<String,Object>>();
			JSONArray data = new JSONArray(jsonString);
			for(int i=0; i<data.length(); i++){
				Map<String, Object> map = 
						new HashMap<String, Object>();
				JSONObject json = data.getJSONObject(i);
				map.put("shopName", json.getString("shopName"));
				map.put("shopPhone", json.getString("shopPhone"));
				list.add(map);
			}
			return list;
		}catch(JSONException e){
			e.printStackTrace();
		}
		return list;
	}
	//获取商品信息
	public static List<Map<String, Object>> getGoodsInfoAfterJson(String jsonString){
		List<Map<String, Object>> list = null;
		try{
			list = new ArrayList<Map<String,Object>>();
			JSONArray data = new JSONArray(jsonString);
			for(int i=0; i<data.length(); i++){
				Map<String, Object> map = 
						new HashMap<String, Object>();
				JSONObject json = data.getJSONObject(i);
				map.put("goodsName", json.getString("goodsName"));
				map.put("goodsCount", json.getString("goodsCount"));
				map.put("goodsPrice", json.getString("goodsPrice"));
				list.add(map);
			}
			return list;
		}catch(JSONException e){
			e.printStackTrace();
		}
		return list;
	}
	
	//获取用户登录的信息
	/*{"action":"27B5916F4F3E2663E287D2B69961C26E",
		"areaId":0,
		"orderAddress":null,
		"userAuthority":"00001",
		"userCredit":0,
		"userId":3899418,
		"userIdCardNum":null,
		"userName":null,
		"userPasswd":"123456",
		"userPhone":"13720422675",
		"userRegiTimeTime":"2014-9-19 22:07:17",
		"workerNum":null}*/
	public static List<UserInfo> getLoginInfo(String jsonString){
		List<UserInfo> list = new ArrayList<UserInfo>();
		try {
			JSONObject data = new JSONObject(jsonString);
			UserInfo info = new UserInfo();
			info.setAction(data.getString("action"));
			info.setAreaId(data.getInt("areaId"));
			info.setOrderAddress(data.getString("orderAddress"));
//			info.setUserAuthority(data.getString("userAuthority"));
			info.setUserCredit(data.getInt("userCredit"));
			info.setUserId(data.getLong("userId"));
			info.setUserIdCardNum(data.getString("userIdCardNum"));
			info.setUserName(data.getString("userName"));
			info.setUserPasswd(data.getString("userPasswd"));
			info.setUserPhone(data.getString("userPhone"));
			info.setUserRegiTimeTime(data.getString("userRegiTimeTime"));
			info.setWorkerNum(data.getString("workerNum"));
			list.add(info);
			System.out.println("LISTLIST::::");
		} catch (JSONException e) {
			e.printStackTrace();
			return list;
		}
		System.out.println("LIST::"+list);
		return list;
	}
	
	//获取送单信息
	/*{"endPage":1,
		"len":10,"
		list":[{
			"areaId":1,
			"getMoney":0.0,"
			 handMoney":29.0,"
			orderAddress":"1#559",
			"orderConsumeTime":"18:00-18:30",
			"orderId":393217,
			"orderPhone":"18729354347",
			"orderState":0,
			"orderTime":"2014-10-06 16:01:23",
			"orderWay":0,"userId":98304,
			"workerNum":"0001"}],
		"pageNo":1,
		"startIndex":0,
		"startPage":1,
		"totalPage":1,
		"totalRecord":1}*/
	public static List<SendOrderInfo> sendOrderInfo(String jsonString){
		List<SendOrderInfo> list = new ArrayList<SendOrderInfo>();
		try {
				JSONObject data = new JSONObject(jsonString);
				JSONArray array = data.getJSONArray("list");
				for(int i = 0; i < array.length(); i++){
					JSONObject object = array.getJSONObject(i);
					SendOrderInfo info = new SendOrderInfo();
					info.setOrderId(object.getInt("orderId"));
					info.setOrderPhone(object.getString("orderPhone"));
					info.setOrderConsumeTime(object.getString("orderConsumeTime"));
					info.setHandMoney(object.getDouble("handMoney"));
					info.setOrderAddress(object.getString("orderAddress"));
					info.setPageNo(data.getInt("pageNo"));
					list.add(info);
				}
		} catch (JSONException e) {
			e.printStackTrace();
			return list;
		}
		return list;
	}
	
	//获取取餐详情
	public static List<AfterSendInfo> AfterSendOrderInfo(String jsonString){
		List<AfterSendInfo> list = new ArrayList<AfterSendInfo>();
		try {
				JSONObject data = new JSONObject(jsonString);
				JSONArray array = data.getJSONArray("list");
				for(int i = 0; i < array.length(); i++){
					JSONObject object = array.getJSONObject(i);
					AfterSendInfo info = new AfterSendInfo();
					info.setPageNo(data.getInt("pageNo"));
					info.setGoodsId(object.getInt("goodsId"));
					info.setGoodsName(object.getString("goodsName"));
					info.setGoodsPrice(object.getDouble("goodsPrice"));
					info.setGoodsCount(object.getInt("goodsCount"));
					info.setGoodsMore(object.getString("goodsMore"));
					list.add(info);
				}
		} catch (JSONException e) {
			e.printStackTrace();
			return list;
		}
		return list;
	}
	
	//获取取单信息
		/*{"endPage":1,
			 "len":10,
			 "list":[{"areaId":1,
			 "cityId":0,
			 "serviceCategoty":0,
			 "shopAddress":"西安邮电大学旭日苑一楼",
			 "shopAssess":0.0,
			 "shopCategory":0,
			 "shopContent":null,
			 "takeMoney":88.0,
			
			 "shopCredit":0,
			 "shopId":32768,
			 "shopImg":"\/img\/shop.jpg",
			 "shopName":"测试商家1",
			 "shopPhone":"18729354347",
			 "shopSales":0,
			 "shopState":"营业","userId":2,
			 "workTime":null,
			 "workerNum":null}],
			"pageNo":1,
			"startIndex":0,
			"startPage":1,
			"totalPage":1,
			"totalRecord":1}*/
		public static List<GetOrderInfo> getOrderInfo(String jsonString){
			List<GetOrderInfo> list = new ArrayList<GetOrderInfo>();
			try {
					JSONObject data = new JSONObject(jsonString);
					JSONArray array = data.getJSONArray("list");
					for(int j = 0; j < array.length(); j++){
						JSONObject object = array.getJSONObject(j);
						GetOrderInfo info = new GetOrderInfo();
						info.setShopPhone(object.getString("shopPhone"));
						info.setShopName(object.getString("shopName"));
						info.setTakeMoney(object.getDouble("takeMoney"));
						info.setShopAddress(object.getString("shopAddress"));
						info.setShopId(object.getInt("shopId"));
						list.add(info);
					}
			} catch (JSONException e) {
				e.printStackTrace();
				return list;
			}
			return list;
		}
		
		public static List<AfterGetInfo> afterGetOrderInfo(String jsonString){
			List<AfterGetInfo> list = new ArrayList<AfterGetInfo>();
			try {
					JSONObject data = new JSONObject(jsonString);
					JSONArray array = data.getJSONArray("list");
					for(int j = 0; j < array.length(); j++){
						JSONObject object = array.getJSONObject(j);
						AfterGetInfo info = new AfterGetInfo();
						info.setPageNo(object.getInt("pageNo"));
						info.setGoodsId(object.getInt("goodsId"));
						info.setGoodsName(object.getString("goodsName"));
						info.setGoodsPrice(object.getDouble("goodsPrice"));
						info.setGoodsCount(object.getInt("goodsCount"));
						info.setGoodsMore(object.getString("goodsMore"));
						list.add(info);
					}
			} catch (JSONException e) {
				e.printStackTrace();
				return list;
			}
			return list;
		}
}
