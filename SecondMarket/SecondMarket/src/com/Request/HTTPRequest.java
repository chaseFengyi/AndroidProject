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
	// ��½
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

	// �ϴ�
	public static String uploadFile(File file) {
		final String TAG = "uploadFile";
		final int TIME_OUT = 10 * 10000000; // ��ʱʱ��
		final String CHARSET = "utf-8"; // ���ñ���
		final String SUCCESS = "1";
		final String FAILURE = "0";
		// �ϴ�ͷ��򷢲���Ʒ,ͷ��Ϊ1��������ƷͼƬΪ2.
		String UPLOAD_TYPE = "upLoadType";
		String upLoadType = "1";

		// user�ļ�ֵ��
		String USER_ID = "UserId";
		String UserId = "1";

		// ֻ�е�������Ʒ�������¼�ֵ��
		// ��Ʒ����
		String GOODS_NAME = "goodsName";
		String goodsName = "aaaa";

		// ������Ʒʱ����Ʒ����
		String GOODS_DESCRIBE = "goodsDescribe";
		String goodsDescribe = "XXXXX����������������������������������������";
		// ��Ʒ����
		String GOODS_TYPE = "goodsType";
		String goodsType = "1";

		// ͼƬ
		String BOUNDARY = UUID.randomUUID().toString(); // �߽��ʶ �������
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // ��������
//		String RequestURL = "http://localhost:8080/XuptMarket/uploadimagecl";
		String RequestURL = Address.SENDPICTURE_ADDRESS;
		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // ����������
			conn.setDoOutput(true); // ���������
			conn.setUseCaches(false); // ������ʹ�û���
			conn.setRequestMethod("POST"); // ����ʽ
			conn.setRequestProperty("Charset", CHARSET); // ���ñ���
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
				 * ���ļ���Ϊ�գ����ļ���װ�����ϴ�
				 */
				OutputStream outputSteam = conn.getOutputStream();

				DataOutputStream dos = new DataOutputStream(outputSteam);
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * �����ص�ע�⣺ name�����ֵΪ����������Ҫkey ֻ�����key �ſ��Եõ���Ӧ���ļ�
				 * filename���ļ������֣�������׺���� ����:abc.png
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

				// ��ȡ���������ص��ֽ���
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
	 * ����POST���������ַ����ķ���
	 * 
	 * @param url
	 *            :����ĵ�ַ
	 * @param params
	 *            :POST�������
	 * @return ������
	 */
	public static String queryStringForPost(String url,
			ArrayList<NameValuePair> params,String SSID) {
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
			if (true) {
				// �����Ӧ
				result = EntityUtils.toString(response.getEntity(), "utf-8");// ��ֹ��������
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

	// �û����������Ϣ
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

	// ��������
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
	
	// 7.���·���
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
