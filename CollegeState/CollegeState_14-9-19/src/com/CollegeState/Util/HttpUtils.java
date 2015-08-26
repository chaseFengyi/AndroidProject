package com.CollegeState.Util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author zc 2014.5.6
 */
public class HttpUtils {
	// 222.24.63.101
	// IP前缀
	public static String IP_PREFIX = "http://121.42.8.50/CS/";
	// 用户注册的URL
	public static String USER_REGISTER_URL = "http://121.42.8.50/CS/customer/Customer_register";
	// 用户登录的URL
	public static String USER_LOGIN_URL = "http://121.42.8.50/CS/customer/Customer_login";
	// 用户搜索的URl
	public static String USER_SEARCH_URL = "http://121.42.8.50/CS/customer/Goods_getGoodsListByStr";
	// 转盘搜索的URL
	public static String USER_FANCY_URL = "http://121.42.8.50/CS/customer/Shop_getShopListByCate";
	// 获取商铺信息的URL
	public static String SHOP_LIST_URL = "http://121.42.8.50/CS/customer/Shop_getShopListByArea";
	// 获取菜单的URL
	public static String MENU_ITEM_URL = "http://121.42.8.50/CS/customer/Goods_getGoodsList";
	// 用户修改信息的URl
	public static String USER_INFO_CHANGE = "http://121.42.8.50/Ordering/customerupdateinfocl";
	// 用户提交订单
	public static String USER_SUBMIT_ORDER = "http://121.42.8.50/CS/customer/MyOrder_add";
	// 用户获取订单的URL
	public static String USER_ORDER_FORM = "http://121.42.8.50/CS/customer/OrderItem_cOrderItemList";
	// 用户修改订单项状态的URL
	public static String CHANGE_ORDER_ITEM_STATE = "http://121.42.8.50/CS/customer/OrderItem_updateOrderState";
	// 用户评价订单的URL
	public static String USER_ASSESS = "http://121.42.8.50/CS/customer/Assess_add";
	// 用户修改密码的URL
	public static String USER_CHANGE_PASSWD = "http://121.42.8.50/CS/customer/Customer_update";
	// 查看促销产品的URl
	public static String CHECk_PROMOTIONAL_PRODUCTS = "http://121.42.8.50/CS/customer/ProGoods_getProGoodsList";
	// 摇一摇抢饭的URL
	public static String USER_GRAB_MEAL = "http://121.42.8.50/CS/customer/Wealth_add";
	// 查看财富的URL
	public static String USER_CKECK_WEALTH = "http://121.42.8.50/CS/customer/Wealth_getWealthList";
	// 删除我的财富值（消费）
	public static String DEL_WEALTH = "http://121.42.8.50/CS/customer/Wealth_delete";
	// 我要挑刺的URl
	public static String MY_SUGGESTION = "http://121.42.8.50/CS/customer/Suggestion_add";
	// 美食推荐的URL
	public static String RECOMMENDED = "http://121.42.8.50/CS/customer/Shop_getRecShopListByCity";
	// 获取区域的URL
	public static String AREA_LIST = "http://121.42.8.50/CS/customer/Area_getAreaList";
	// 获取广告图片地址
	public static String GET_AD_IMAGE = "http://121.42.8.50/CS/customer/Ad_getAdList";
	// 网络异常
	public static String NETWORK_ERROR = "network error";

	/**
	 * 用于POST请求生成字符串的方法
	 * 
	 * @param url
	 *            :请求的地址
	 * @param params
	 *            :POST请求参数
	 * @return 请求结果
	 */
	public static String queryStringForPost(String url,
			ArrayList<NameValuePair> params) {
		// 根据url获得HttpPost对象
		HttpPost request = new HttpPost(url);
		// 添加参数
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String result = null;
		try {
			// 获得响应对象
			HttpResponse response = new DefaultHttpClient().execute(request);
			// 判断是否请求成功
			if (response.getStatusLine().getStatusCode() == 200) {
				// 获得响应
				result = EntityUtils.toString(response.getEntity(), "utf-8");// 防止中文乱码
				try {
					if (result.startsWith("{\"response")) {
						JSONObject obj = new JSONObject(result);
						if (!obj.getString("response").equals(null))
							result = obj.getString("response") + "";
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = HttpUtils.NETWORK_ERROR;
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = HttpUtils.NETWORK_ERROR;
			return result;
		}
		return null;
	}

	/**
	 * 判断是否连接网络 注意：此处需要添加权限 <uses-permission
	 * android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 * 
	 * @param context
	 *            上下文对象
	 * @return
	 */
	public static boolean isNetWorkEnable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo == null) {
			return false;
		} else {
			return netInfo.isConnectedOrConnecting();
		}
	}

}
