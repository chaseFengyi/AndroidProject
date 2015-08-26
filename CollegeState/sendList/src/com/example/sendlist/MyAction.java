package com.example.sendlist;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.request.MD5StringUtil;

public class MyAction {

	public boolean judgeAction(String clientAction) {
		Date date = new Date();
		SimpleDateFormat mydate = new SimpleDateFormat("yyyy-MM-dd");

		String serviceActiion = MD5StringUtil.MD5Encode(mydate.format(date));

		System.out.println("服务端：" + serviceActiion);
		System.out.println("客户端：" + clientAction);

		if (clientAction != null
				&& clientAction.equalsIgnoreCase(serviceActiion))
			return true;
		else
			return false;

	}
	
	

}
