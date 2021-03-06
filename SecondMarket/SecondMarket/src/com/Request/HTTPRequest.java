package com.Request;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import android.util.Log;

import com.publicInfo.Address;

public class HTTPRequest {
	// 登陆
	public static String getStringFromHttpOfLoad(String jsonString,
			List<NameValuePair> params) {
		HttpPost httpRequest = new HttpPost(jsonString);
		String result = null;
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils
						.toString(httpResponse.getEntity(), "utf_8");
				result = decode(result);
				if (result.startsWith("{\"Response")) {
					JSONObject object = new JSONObject(result);
					result = object.getString("Response");
					/*JSONArray jsonArray = new JSONArray(result);
					System.out.println(")))))");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						System.out.println("(((((())))))((((");
						System.out.println("response:"+jsonObject.getString("Response"));
						if (!jsonObject.getString("Response").equals(null)) {
							result = jsonObject.getString("Response");
							System.out.println("http------="+result);
						}
					}*/
				}
			}
			
			return result;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 上传
	public static String uploadFile(File file) {
		final String TAG = "uploadFile";
		final int TIME_OUT = 10 * 10000000; // 超时时间
		final String CHARSET = "utf-8"; // 设置编码
		final String SUCCESS = "1";
		final String FAILURE = "0";
		// 上传头像或发布物品,头像为1，发布物品图片为2.
		String UPLOAD_TYPE = "upLoadType";
		String upLoadType = "1";

		// user的键值对
		String USER_ID = "UserId";
		String UserId = "1";

		// 只有当发布物品才有以下键值对
		// 物品名称
		String GOODS_NAME = "goodsName";
		String goodsName = "aaaa";

		// 发布物品时，物品描述
		String GOODS_DESCRIBE = "goodsDescribe";
		String goodsDescribe = "XXXXX啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊";
		// 物品类型
		String GOODS_TYPE = "goodsType";
		String goodsType = "1";

		// 图片
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
//		String RequestURL = "http://localhost:8080/XuptMarket/uploadimagecl";
		String RequestURL = Address.SENDPICTURE_ADDRESS;
		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			conn.setRequestProperty(UPLOAD_TYPE, upLoadType);
			conn.setRequestProperty(GOODS_NAME, goodsName);
			conn.setRequestProperty(GOODS_DESCRIBE, goodsDescribe);
			conn.setRequestProperty(USER_ID, UserId);
			conn.setRequestProperty(GOODS_TYPE, goodsType);

			if (file != null) {
				/**
				 * 当文件不为空，把文件包装并且上传
				 */
				OutputStream outputSteam = conn.getOutputStream();

				DataOutputStream dos = new DataOutputStream(outputSteam);
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名的 比如:abc.png
				 */

				sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
						+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();

				// 获取服务器返回的字节流
				InputStream in = conn.getInputStream();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String str = "";
				while ((str = br.readLine()) != null) {
					System.out.println(str);
				}
				in.close();
				dos.close();
				return SUCCESS;

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return FAILURE;
	}

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
			ArrayList<NameValuePair> params,String SSID) {
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
			if (true) {
				// 获得响应
				result = EntityUtils.toString(response.getEntity(), "utf-8");// 防止中文乱码
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();

			return result;
		} catch (IOException e) {
			e.printStackTrace();

			return result;
		}
		return null;
	}

	// 用户有意向的信息
	public static String getUserIdeaAndDeleteStringFromHttp(
			String jsonString, List<NameValuePair> params) {
		String url = jsonString + "?goodsId=1";
		HttpPost httpRequest = new HttpPost(url);
		String result = null;
		Log.e("HTTPREQUEST", Thread.currentThread().getName());
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils
						.toString(httpResponse.getEntity(), "utf_8");
				if (result.startsWith("[\"Response")) {
					JSONArray jsonArray = new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						if (!jsonObject.getString("Response").equals(null)) {
							result = jsonObject.getString("Response");
						}
					}
				}
			}
			result = decode(result);
			return result;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 分类搜索
	public static String getSortSearchFromHttp(String jsonString,
			List<NameValuePair> params) {
		String url = jsonString + "?goodsTypeId=1&page=1";
		HttpPost httpRequest = new HttpPost(url);
		String result = null;
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils
						.toString(httpResponse.getEntity(), "utf_8");
				if (result.startsWith("[\"Response")) {
					JSONArray jsonArray = new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						if (!jsonObject.getString("Response").equals(null)) {
							result = jsonObject.getString("Response");
						}
					}
				}
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	// 7.最新发布
		public static String getNewReleaseFromHttp(String jsonString,
				List<NameValuePair> params) {
			HttpPost httpRequest = new HttpPost(jsonString);
			String result = null;
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					result = EntityUtils
							.toString(httpResponse.getEntity(), "utf_8");
					System.out.println("resultresult"+result);
					/*if (result.startsWith("[\"Response")) {
						JSONArray jsonArray = new JSONArray(result);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							if (!jsonObject.getString("Response").equals(null)) {
								result = jsonObject.getString("Response");
							}
						}
					}*/
				}
				return result;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return result;
		}
	
	/*public static String decode(String str) throws IOException {
		String string;
		System.out.println("decode--->"+str);
		string = StringUtils.base64EnCode(str);
		System.out.println("decodeing--->"+string);
		return string;
	}*/
	public static String decode(String str) throws IOException {
		String string;	
//		System.out.println("string---->"+str);
//		BASE64Decoder decoder = new BASE64Decoder();
//		string = new String(decoder.decodeBuffer(str),"UTF-8");
		Decoder.BASE64Decoder a = new Decoder.BASE64Decoder();
		byte[] b = a.decodeBuffer(str);
		string = new String(b); 
		return string;
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
