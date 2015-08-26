package com.coolReader.net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;

import com.coolReader.Util.LoginTcpUtils;

/**
 * 子线程处理登录请求
 * @author Xuptljw
 * 2015年7月24日15:29:18
 */
public class LoginActivityTask extends Thread{
	
	private Handler handler;
	private String name;
	private String pass;
	private Socket socket;
	public static final int NETWORK_ERROR = 0;
	public static final int LOGIN_FAILED = 1;
	public static final int LOGIN_SUCCESS = 2;
	
	public LoginActivityTask(Handler handler, String name, String pass) {
		super();
		this.handler = handler;
		this.name = name;
		this.pass = pass;
	}

	//开启线程处理请求
	public void run() {
	    //利用Map来封装参数
	    Map<String,String> params = new HashMap<String, String>();
	    params.put("username", name);
	    params.put("password", pass);
	    //利用TCP协议进行处理请求,创建TCP链接得到链接套接字
	    try {
		socket = new Socket(LoginTcpUtils.IP, LoginTcpUtils.port);
	    } catch (UnknownHostException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    
	    String result = LoginTcpUtils.doTcpRequest(socket, params);
	    System.out.println("result="+result);
	    if(result.equals("ok")) {
		//登录成功
		handler.sendEmptyMessage(LOGIN_SUCCESS);
	    }else {
		int txt2 = 2;
		System.out.println(txt2);
	    }
	}
}
