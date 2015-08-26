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
	// IPǰ׺
	public static String IP_PREFIX = "http://121.42.8.50/CS/";
	// �û�ע���URL
	public static String USER_REGISTER_URL = "http://121.42.8.50/CS/customer/Customer_register";
	// �û���¼��URL
	public static String USER_LOGIN_URL = "http://121.42.8.50/CS/customer/Customer_login";
	// �û�������URl
	public static String USER_SEARCH_URL = "http://121.42.8.50/CS/customer/Goods_getGoodsListByStr";
	// ת��������URL
	public static String USER_FANCY_URL = "http://121.42.8.50/CS/customer/Shop_getShopListByCate";
	// ��ȡ������Ϣ��URL
	public static String SHOP_LIST_URL = "http://121.42.8.50/CS/customer/Shop_getShopListByArea";
	// ��ȡ�˵���URL
	public static String MENU_ITEM_URL = "http://121.42.8.50/CS/customer/Goods_getGoodsList";
	// �û��޸���Ϣ��URl
	public static String USER_INFO_CHANGE = "http://121.42.8.50/Ordering/customerupdateinfocl";
	// �û��ύ����
	public static String USER_SUBMIT_ORDER = "http://121.42.8.50/CS/customer/MyOrder_add";
	// �û���ȡ������URL
	public static String USER_ORDER_FORM = "http://121.42.8.50/CS/customer/OrderItem_cOrderItemList";
	// �û��޸Ķ�����״̬��URL
	public static String CHANGE_ORDER_ITEM_STATE = "http://121.42.8.50/CS/customer/OrderItem_updateOrderState";
	// �û����۶�����URL
	public static String USER_ASSESS = "http://121.42.8.50/CS/customer/Assess_add";
	// �û��޸������URL
	public static String USER_CHANGE_PASSWD = "http://121.42.8.50/CS/customer/Customer_update";
	// �鿴������Ʒ��URl
	public static String CHECk_PROMOTIONAL_PRODUCTS = "http://121.42.8.50/CS/customer/ProGoods_getProGoodsList";
	// ҡһҡ������URL
	public static String USER_GRAB_MEAL = "http://121.42.8.50/CS/customer/Wealth_add";
	// �鿴�Ƹ���URL
	public static String USER_CKECK_WEALTH = "http://121.42.8.50/CS/customer/Wealth_getWealthList";
	// ɾ���ҵĲƸ�ֵ�����ѣ�
	public static String DEL_WEALTH = "http://121.42.8.50/CS/customer/Wealth_delete";
	// ��Ҫ���̵�URl
	public static String MY_SUGGESTION = "http://121.42.8.50/CS/customer/Suggestion_add";
	// ��ʳ�Ƽ���URL
	public static String RECOMMENDED = "http://121.42.8.50/CS/customer/Shop_getRecShopListByCity";
	// ��ȡ�����URL
	public static String AREA_LIST = "http://121.42.8.50/CS/customer/Area_getAreaList";
	// ��ȡ���ͼƬ��ַ
	public static String GET_AD_IMAGE = "http://121.42.8.50/CS/customer/Ad_getAdList";
	// �����쳣
	public static String NETWORK_ERROR = "network error";

	/**
	 * ����POST���������ַ����ķ���
	 * 
	 * @param url
	 *            :����ĵ�ַ
	 * @param params
	 *            :POST�������
	 * @return ������
	 */
	public static String queryStringForPost(String url,
			ArrayList<NameValuePair> params) {
		// ����url���HttpPost����
		HttpPost request = new HttpPost(url);
		// ��Ӳ���
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String result = null;
		try {
			// �����Ӧ����
			HttpResponse response = new DefaultHttpClient().execute(request);
			// �ж��Ƿ�����ɹ�
			if (response.getStatusLine().getStatusCode() == 200) {
				// �����Ӧ
				result = EntityUtils.toString(response.getEntity(), "utf-8");// ��ֹ��������
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
	 * �ж��Ƿ��������� ע�⣺�˴���Ҫ���Ȩ�� <uses-permission
	 * android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 * 
	 * @param context
	 *            �����Ķ���
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
