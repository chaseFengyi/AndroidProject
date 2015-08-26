package com.request;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.sendlist.Login;
import com.get.SeeDetailsOfGet;
import com.send.SeeDetailsOfSend;

public class HttpObtain {
	//ע��
	public static String sendDate(String url,List<NameValuePair> params){
		//����httppost����
				HttpPost httpRequest = new HttpPost(url);
				String result = null;
				try{
					//����httpRequest����
					httpRequest.setEntity(
							new UrlEncodedFormEntity(params,HTTP.UTF_8));
					//ȡ��httpresponse
					HttpResponse httpResponse = 
							new DefaultHttpClient().execute(httpRequest);
					//��״̬��Ϊ200 
					if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
						//ȥ����Ӧ�ִ�
						result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
						if(result.startsWith("{\"object\":null,\"response")){
							JSONObject object = new JSONObject(result);
							if(!object.getString("response").equals(null)){
								result = object.getString("response");
							}
						}
					}
					return result;
				}catch(UnsupportedEncodingException e){
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
				return result;	
	}
	//��½
	public static String getStringFromHttp(String url,List<NameValuePair> params){
		//����httppost����
		HttpPost httpRequest = new HttpPost(url);
		String result = null;
		try{
			//����httpRequest����
			httpRequest.setEntity(
					new UrlEncodedFormEntity(params,HTTP.UTF_8));
			//ȡ��httpresponse
			HttpResponse httpResponse = 
					new DefaultHttpClient().execute(httpRequest);
			//��״̬��Ϊ200 
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				//ȥ����Ӧ�ִ�
				result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
				try{
					if(result.startsWith("{\"object\":null,\"response")){
						JSONObject obj = new JSONObject(result);
						if(!obj.getString("response").equals(null)){
							result = obj.getString("response");
						}
					}
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
			return result;
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//ȡ��
		public static String getOrderFromHttp(String url,List<NameValuePair> params){
			//����http post����
			String path = url+
					"?workerNum="+Login.WORKERNUM+"&method=order&orderState=0&pageNo=1";
			System.out.println("urlurlurl:"+path);
			HttpPost httpRequest = new HttpPost(path);
			String result = null;
			try{
				//����httpRequest����
				httpRequest.setEntity(
						new UrlEncodedFormEntity(params,HTTP.UTF_8));
				//ȡ��httpresponse
				HttpResponse httpResponse = 
						new DefaultHttpClient().execute(httpRequest);
				//��״̬��Ϊ200 
				if(httpResponse.getStatusLine().getStatusCode() == 200){
					//ȥ����Ӧ�ִ�
					result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
					if(result.startsWith("{\"object\":null,\"response")){
						JSONObject object = new JSONObject(result);
						result = object.getString("response");
					}
				}
				return result;
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		//��ʾȡ����Ϣ
				public static String afterGetOrderFromHttp(String url,List<NameValuePair> params){
					//����http post����
					String path = url+
							"?action="+Login.ACTION+"&shopId="+SeeDetailsOfGet.SHOPID+"&orderItemState=1&pageNo=1";
					HttpPost httpRequest = new HttpPost(path);
					String result = null;
					try{
						//����httpRequest����
						httpRequest.setEntity(
								new UrlEncodedFormEntity(params,HTTP.UTF_8));
						//ȡ��httpresponse
						HttpResponse httpResponse = 
								new DefaultHttpClient().execute(httpRequest);
						//��״̬��Ϊ200 
						if(httpResponse.getStatusLine().getStatusCode() == 200){
							//ȥ����Ӧ�ִ�
							result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
							if(result.startsWith("{\"object\":null,\"response")){
								JSONObject object = new JSONObject(result);
								result = object.getString("response");
							}
						}
						return result;
					}catch(UnsupportedEncodingException e){
						e.printStackTrace();
					}catch (Exception e) {
						e.printStackTrace();
					}
					return result;
				}
	
	//�͵�
	public static String sendOrderFromHttp(String url,List<NameValuePair> params){
		System.out.println("action:"+Login.ACTION);
		//����http post����
		String path = url+
				"?action="+Login.ACTION+"&method=send&key=000001&orderState=1&pageNo=1";
		HttpPost httpRequest = new HttpPost(path);
		String result = null;
		try{
			//����httpRequest����
			httpRequest.setEntity(
					new UrlEncodedFormEntity(params,HTTP.UTF_8));
			//ȡ��httpresponse
			HttpResponse httpResponse = 
					new DefaultHttpClient().execute(httpRequest);
			//��״̬��Ϊ200 
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				//ȥ����Ӧ�ִ�
				result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
				if(result.startsWith("{\"object\":null,\"response")){
					JSONObject object = new JSONObject(result);
					result = object.getString("response");
				}
			}
			return result;
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//��ʾ�͵���Ϣ
	public static String afterSendOrderFromHttp(String url,List<NameValuePair> params){
		//����http post����
		String path = url+
				"?action="+Login.ACTION+"&orderId="+SeeDetailsOfSend.ORDERID+"&orderItemState=1&pageNo=1&method=order";
		HttpPost httpRequest = new HttpPost(path);
		System.out.println("path:"+path);
		String result = null;
		try{
			//����httpRequest����
			httpRequest.setEntity(
					new UrlEncodedFormEntity(params,HTTP.UTF_8));
			//ȡ��httpresponse
			HttpResponse httpResponse = 
					new DefaultHttpClient().execute(httpRequest);
			//��״̬��Ϊ200 
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				//ȥ����Ӧ�ִ�
				result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
				if(result.startsWith("{\"object\":null,\"response")){
					JSONObject object = new JSONObject(result);
					result = object.getString("response");
				}
			}
			return result;
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
