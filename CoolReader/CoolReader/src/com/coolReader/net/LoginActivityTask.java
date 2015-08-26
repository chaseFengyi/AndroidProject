package com.coolReader.net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;

import com.coolReader.Util.LoginTcpUtils;

/**
 * ���̴߳����¼����
 * @author Xuptljw
 * 2015��7��24��15:29:18
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

	//�����̴߳�������
	public void run() {
	    //����Map����װ����
	    Map<String,String> params = new HashMap<String, String>();
	    params.put("username", name);
	    params.put("password", pass);
	    //����TCPЭ����д�������,����TCP���ӵõ������׽���
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
		//��¼�ɹ�
		handler.sendEmptyMessage(LOGIN_SUCCESS);
	    }else {
		int txt2 = 2;
		System.out.println(txt2);
	    }
	}
}
