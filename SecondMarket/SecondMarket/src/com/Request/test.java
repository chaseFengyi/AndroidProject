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
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class test {

	public static void main(String args[]) {

		Login();

		File file = new File("H:\\aa.jpg");
		System.out.println(uploadFile(file));
	}

	// ��½
	public static void Login() {
		final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("UserNum", "03121212"));
		param.add(new BasicNameValuePair("UserPassword", "f82210578"));
		String temp = queryStringForPost(
				"http://localhost:8080/XuptMarket/userlogincl", param, null);
	}

	// ������
	public static void Wanted() {
		final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("goodsId", "1"));
		String temp = queryStringForPost(
				"http://localhost:8080/XuptMarket/goodswantedcl", param, null);
	}

	// ɾ��
	public static void Del() {
		final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("goodsId", "1"));
		String temp = queryStringForPost(
				"http://localhost:8080/XuptMarket/delgoodscl", param, null);
	}

	// ��������
	public static void ClassifiedSearch() {
		final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("goodsTypeId", "1"));
		param.add(new BasicNameValuePair("page", "1"));
		String temp = queryStringForPost(
				"http://localhost:8080/XuptMarket/classifiedsearchcl", param,
				null);
	}

	// ģ������
	public static void IndistinctSearch() {
		final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("page", "1"));
		param.add(new BasicNameValuePair("keywords", "a"));
		String temp = queryStringForPost(
				"http://localhost:8080/XuptMarket/indistinctsearchcl", param,
				null);
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
		String upLoadType = "2";

		// user�ļ�ֵ��
		String USER_ID = "UserId";
		String UserId = "12344";

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
		String RequestURL = "http://localhost:8080/XuptMarket/uploadimagecl";
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
			ArrayList<NameValuePair> params, String SSID) {
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
				System.out.println(result);
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

}
