package com.coolReader.request;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

public class TCPRequest {

	public static String tcpRequest(String string, String dstAddress,
			int dstPort) {
		Socket socket = null;
		PrintWriter writer = null;
		int timeout = 10000;
		InputStream inputStream = null;
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		String receive = null;
		StringBuilder sb = null;
		int count = 0;
		try {

			Log.i("tag", "开始链接socket");
			socket = new Socket(dstAddress, dstPort);
			socket.setSoTimeout(timeout);// 设置阻塞时间,避免丢包
			Log.i("tag", "连接成功");
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			inputStream = socket.getInputStream();
			dataInputStream = new DataInputStream(inputStream);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			// 发送数据
			Log.i("tag",
					"发送" + string + "至"
							+ socket.getInetAddress().getHostAddress() + ":"
							+ String.valueOf(socket.getPort()));
			writer.write(string);
			writer.flush();
			Log.i("tag", "发送成功");
			// 接收数据
			Log.i("tag", "检测数据");
			receive = "";
			int readNum = 0;
			sb = new StringBuilder();
			while (count == 0) {
				count = inputStream.available();
			}
			char[] buffer = new char[count];
			while(true){
				readNum = bufferedReader.read(buffer);
				if(readNum == -1)
					break;
				sb.append(new String(buffer));
			}
			
//			byte buffer[] = new byte[count];
//			List<byte[]> list_byte = new ArrayList<byte[]>();
//			while (true) {
//				readNum = dataInputStream.read(buffer);
//				if (readNum == -1) {
//					break;
//				}
//				list_byte.add(buffer);
////				String str = new String(buffer, "utf-8");
////				sb.append(str);
////				receive += str;
//			}
//			
//			byte[] bytes = new byte[count * list_byte.size()];
//			System.out.println("SIZE="+count * list_byte.size());
//			if(list_byte != null){
//				System.out.println("11111111");
//				int k = 0;
//				for(int i = 0; i < list_byte.size(); i++){
//					System.out.println("22222222------"+list_byte.size());
//					for(int j = 0; j < list_byte.get(i).length; j++){
//						bytes[k++] = list_byte.get(i)[j];
//						System.out.print(bytes[k-1]);
//					}
//				}
//			}
//			
			receive = sb.toString();
//			receive = new String(bytes,"utf-8");
			Log.i("tcp-receive=", receive);
			
		} catch (IOException exception) {
			exception.printStackTrace();
			Log.e("error", "error");
		} finally {
			try {
				Log.i("tag", "close writer");
				writer.close();
				Log.i("tag", "close reader");
				dataInputStream.close();
				bufferedReader.close();
				Log.i("tag", "close socket");
				inputStream.close();
				Log.i("tag", "close inputStream");
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return receive;
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
	
	/**
	 * 用base64将图片进行解码转为bitmap
	 * @param str
	 * @return
	 */
//	public static String decode(String str){
//		String string;
//		Decoder.BASE64Decoder a = new Decoder.BASE64Decoder();
//		byte[] b = null;
//		try {
//			b = a.decodeBuffer(str);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		string = new String(b);
//		return string;
//	}
	public static Bitmap decode(String str){
		Bitmap bitmap = null;
		
		byte[] decode = Base64.decode(str, Base64.DEFAULT);
		bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
		
		return bitmap;
	}

}
