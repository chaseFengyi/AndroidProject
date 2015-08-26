package com.coolReader.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * TCP���ӹ���
 * 
 * @author Xuptljw 2015��7��26��17:49:56
 */
public class LoginTcpUtils {
	// �����IP���Ӽ��˿�
	public static final String IP = "192.168.1.148";
	public static final int port = 3333;

	public static final String NETWORK_ERROR = "network_error";

	public static String doTcpRequest(Socket socket, Map<String, String> params) {
		InputStream in = null;// ���������������socket�е�����
		OutputStream os = null;// ������������������ݵ�socket��
		InputStream convertString = null;// ���ô�socket�ж�ȡ����
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] byt = null;
		String result = null;
		if (socket != null) {
			Log.i("test", "socket��Ϊ�գ�");
		} else {
			Log.i("test", "socketΪ�գ�");
		}
		try {
			// in = new BufferedReader(new
			// InputStreamReader(socket.getInputStream()));
			in = socket.getInputStream();

			os = socket.getOutputStream();
			JSONObject js = new JSONObject();

			// ���û���Ϣƴ�ӳ�json��ʽ���ַ����������̨����
			/*
			 * String loginInfoJson = js.put("funType", 10).put("funContent",
			 * new JSONObject().put("userInfo",params.get("username"))
			 * .put("password", params.get("password"))).toString();
			 */
			// Fy�޸�loginInfoJson
			String loginInfoJson = js
					.put("funType", 10)
					.put("funContent",
							new JSONObject().put("userInfo", new JSONObject()
									.put("mailAddr", params.get("username"))
									.put("password", params.get("password"))))
					.toString();

			Log.i("json", loginInfoJson);

			// ���ͻ��˴��͵�json�ַ���ת��Ϊ�����д���
			convertString = new ByteArrayInputStream(loginInfoJson.getBytes());
			byte[] buffer = new byte[1024];
			int end = 0;
			while ((end = convertString.read(buffer)) != -1) {
				os.write(buffer, 0, end);
			}
			// ˢ�·�������
			os.flush();

			String txt = "";

			// int end1 = in.read();
			// while(end1 != -1) {//��������
			// baos.write(end1);
			// end1 = in.read();
			// }
			// byt = baos.toByteArray();

			result = getInputStream(in);
			// while(txt != null) {
			// result += txt;
			// txt = in.readLine(); //����
			//
			// }
			// result = in.readLine();
			// txt = new String(byt, "UTF-8");
			// �õ���������Ӧ������
			// System.out.println(result);

			// Log.i("sermess", in.readLine());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			result = NETWORK_ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			result = NETWORK_ERROR;
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			try {
				convertString.close();
				in.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// ���շ������˷��͵�����
	public static String getInputStream(InputStream in) throws IOException {
		// FileOutputStream fl = new FileOutputStream("G:\\jj.txt");
		String test = null;
		try {
			while (true) {
				int valiable = in.available(); // ���Ƚ��Զ�ȡ���ֽ�ȡ��,����ʵ�ʿɶ��ֽ���
				byte[] bs = new byte[valiable];
				if (valiable == 0) { // ���û�����ݾ��˳�
					break;
				} else {
					in.read(bs, 0, valiable);
					test = new String(bs, 0, valiable);
					// fl.write(bs);
					Log.i("content", test);
				}
				
				// while(true) {
				// int valiable = in.available(); //���Ƚ��Զ�ȡ���ֽ�ȡ��
				// if(valiable == 0) { //���û�����ݾ��˳�
				// break;
				// }else if(valiable > 1024){
				// while(valiable > 1024) {
				// valiable -= 1024;
				// in.read(bs,0,valiable);
				// test += new String(bs,0,valiable);
				// Log.i("content", test);
				// }
				// }else {
				//
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return test;
	}
}
