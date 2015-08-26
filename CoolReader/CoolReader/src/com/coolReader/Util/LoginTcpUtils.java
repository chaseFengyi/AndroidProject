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
 * TCP链接工具
 * 
 * @author Xuptljw 2015年7月26日17:49:56
 */
public class LoginTcpUtils {
	// 请求的IP链接及端口
	public static final String IP = "192.168.1.148";
	public static final int port = 3333;

	public static final String NETWORK_ERROR = "network_error";

	public static String doTcpRequest(Socket socket, Map<String, String> params) {
		InputStream in = null;// 输入流，用于输出socket中的内容
		OutputStream os = null;// 输出流，用于输入内容到socket中
		InputStream convertString = null;// 利用从socket中读取内容
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] byt = null;
		String result = null;
		if (socket != null) {
			Log.i("test", "socket不为空！");
		} else {
			Log.i("test", "socket为空！");
		}
		try {
			// in = new BufferedReader(new
			// InputStreamReader(socket.getInputStream()));
			in = socket.getInputStream();

			os = socket.getOutputStream();
			JSONObject js = new JSONObject();

			// 将用户信息拼接成json格式的字符串传输给后台处理
			/*
			 * String loginInfoJson = js.put("funType", 10).put("funContent",
			 * new JSONObject().put("userInfo",params.get("username"))
			 * .put("password", params.get("password"))).toString();
			 */
			// Fy修改loginInfoJson
			String loginInfoJson = js
					.put("funType", 10)
					.put("funContent",
							new JSONObject().put("userInfo", new JSONObject()
									.put("mailAddr", params.get("username"))
									.put("password", params.get("password"))))
					.toString();

			Log.i("json", loginInfoJson);

			// 将客户端传送的json字符串转化为流进行传输
			convertString = new ByteArrayInputStream(loginInfoJson.getBytes());
			byte[] buffer = new byte[1024];
			int end = 0;
			while ((end = convertString.read(buffer)) != -1) {
				os.write(buffer, 0, end);
			}
			// 刷新发送数据
			os.flush();

			String txt = "";

			// int end1 = in.read();
			// while(end1 != -1) {//发生阻塞
			// baos.write(end1);
			// end1 = in.read();
			// }
			// byt = baos.toByteArray();

			result = getInputStream(in);
			// while(txt != null) {
			// result += txt;
			// txt = in.readLine(); //阻塞
			//
			// }
			// result = in.readLine();
			// txt = new String(byt, "UTF-8");
			// 得到服务器响应的数据
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

	// 接收服务器端发送的数据
	public static String getInputStream(InputStream in) throws IOException {
		// FileOutputStream fl = new FileOutputStream("G:\\jj.txt");
		String test = null;
		try {
			while (true) {
				int valiable = in.available(); // 首先将以读取的字节取到,返回实际可读字节数
				byte[] bs = new byte[valiable];
				if (valiable == 0) { // 如果没有内容就退出
					break;
				} else {
					in.read(bs, 0, valiable);
					test = new String(bs, 0, valiable);
					// fl.write(bs);
					Log.i("content", test);
				}
				
				// while(true) {
				// int valiable = in.available(); //首先将以读取的字节取到
				// if(valiable == 0) { //如果没有内容就退出
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
