package com.example.sendlist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.publicInfo.BackInfo;
import com.request.HttpObtain;
import com.request.JsonResolve;
import com.request.MD5StringUtil;

public class Register extends Activity {
	private EditText userPhoneEdit;
	private EditText userPwdEdit;
	private EditText workNumEdit;
	private Button ok;
	private Button reset;
	private Button exit;
//	private static String getPhone;
//	private static String getPwd;
//	private static String getWorkNum;
//	private static String action;
	private static final String url = "http://121.42.8.50/CS1/customer/User_register";
	private static boolean sign = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registe);

		onClick();
		click();
	}

	private void onClick() {
		userPhoneEdit = (EditText) findViewById(R.id.userPhoneEdit);
		userPwdEdit = (EditText) findViewById(R.id.userPwdEdit);
		workNumEdit = (EditText) findViewById(R.id.userWorkEdit);
		ok = (Button) findViewById(R.id.ok);
		reset = (Button) findViewById(R.id.reset);
		exit = (Button) findViewById(R.id.exit);
	}

	// 点击监听
	private void click() {
		ok.setOnClickListener(new OKListener());
		reset.setOnClickListener(new ResetListener());
		exit.setOnClickListener(new ExitListener());
	}

	// 判断输入的工作号是否合法
	private boolean judge() {
		// 判断输入的工作号是否在（000000-000017）之间
		String getWorkNum = workNumEdit.getText().toString();
		if (getWorkNum.length() != 6) {
			Toast.makeText(Register.this, "哦!" + "工作号输入错误，请重新输入.",
					Toast.LENGTH_SHORT).show();
			workNumEdit.setText("");
		} else if (getWorkNum.equals("000000")) {
			return true;
		} else {
			String str = "";
			for (int i = 0; i < getWorkNum.length(); i++) {
				if (getWorkNum.charAt(i) != '0') {
					if ((getWorkNum.charAt(i) > '0')
							&& (getWorkNum.charAt(i) <= '9')) {
						str += getWorkNum.charAt(i);
					} else {
						Toast.makeText(Register.this,
								"sorry," + "工作号输入错误，请重新输入.", Toast.LENGTH_SHORT)
								.show();
						str = "";
						workNumEdit.setText("");
						break;
					}
				}
			}
			if (str.length() > 0) {
				long isSure = Long.parseLong(str);
				if ((isSure >= 0) && (isSure <= 17)) {
					return true;
				} else {
					Toast.makeText(Register.this, "" + "工作号输入错误，请重新输入.",
							Toast.LENGTH_SHORT).show();
					workNumEdit.setText("");
				}
			}
		}
		return false;
	}
/*
	public class MyRunnable extends Thread {

		@Override
		public void run() {
			String getPhone = userPhoneEdit.getText().toString();
			String getPwd = userPwdEdit.getText().toString();
			String getWorkNum = workNumEdit.getText().toString();
			SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd");
			String date = t.format(new Date());
			String action = MD5StringUtil.MD5Encode(date);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userPhone", getPhone));
			params.add(new BasicNameValuePair("userPasswd", getPwd));
			params.add(new BasicNameValuePair("workerNum", getWorkNum));
			params.add(new BasicNameValuePair("action", action));
			String string1 = url+
					"?userPhone"+getPhone+"&userPasswd"+getPwd+"&workerNum"+getWorkNum+"&action"+action;
			String string = HttpObtain.sendDate(url, params);
			System.out.println("registerREGIDSTER:"+string);
			Message msg = handler.obtainMessage();
			if (string.equals(BackInfo.RESPONSE_REGISTER_OK)) {
				msg.what = 0x01;
			} else if (string.equals(BackInfo.RESPONSE_REGISTER_REPEAT)) {
				msg.what = 0x02;
			} else if (string.equals(BackInfo.RESPONSE_DATABASE_ERROR)) {
				msg.what = 0x03;
			} else if (string.equals(BackInfo.RESPONSE_ACTION_ERROR)) {
				msg.what = 0x04;
			} else {
				msg.what = 0x05;
			}
			handler.sendMessage(msg);
		}

	}*/

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0x01:
				Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
				sign = true;
				Intent intent = new Intent();
				intent.setClass(Register.this, Login.class);
				startActivity(intent);
				finish();
				break;
			case 0x02:
				sign = false;
				userPhoneEdit.setText("");
				Toast.makeText(Register.this, "手机号码已经被注册", Toast.LENGTH_SHORT).show();
				break;
			case 0x03:
				sign = false;
				Toast.makeText(Register.this, "数据操作错误", Toast.LENGTH_SHORT).show();
				break;
			case 0x04:
				sign = false;
				Toast.makeText(Register.this, "操作错误", Toast.LENGTH_SHORT).show();
				break;
			}
		}

	};

	// 判断该手机号是否存在,如果存在，提示错误，不存在，将信息写入服务器
	// 注册成功，返回true，否则返回false
	/*private boolean judgePhone() {
		String getPhone = userPhoneEdit.getText().toString();
		String getPwd = userPwdEdit.getText().toString();
		String getWorkNum = workNumEdit.getText().toString();
		
		SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd");
		String date = t.format(new Date());
		String action = MD5StringUtil.MD5Encode(date);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userPhone", getPhone));
		params.add(new BasicNameValuePair("userPasswd", getPwd));
		params.add(new BasicNameValuePair("workerNum", getWorkNum));
		params.add(new BasicNameValuePair("action", action));
		String string = HttpObtain.sendDate(url, params);
		System.out.println("sssss:" + string);
		if (string.equals(BackInfo.RESPONSE_REGISTER_OK)) {
			Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
			return true;
		} else if (string.equals(BackInfo.RESPONSE_REGISTER_REPEAT)) {
			Toast.makeText(Register.this, "手机号码已被注册", Toast.LENGTH_SHORT)
					.show();
			userPhoneEdit.setText("");
			return false;
		} else if (string.equals(BackInfo.RESPONSE_DATABASE_ERROR)) {
			Toast.makeText(Register.this, "数据操作错误", Toast.LENGTH_SHORT).show();
			return false;
		} else if (string.equals(BackInfo.RESPONSE_ACTION_ERROR)) {
			Toast.makeText(Register.this, "操作错误", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			Toast.makeText(Register.this, "服务器开小差了，稍后再试", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
	}
*/
	class OKListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (judge()) {
				/*if(judgePhone()){
					System.out.println("out1");
					Intent intent = new Intent();
					intent.setClass(Register.this, Login.class);
					startActivity(intent);
					finish();
				}*/
				Thread thread = new Thread(){
					@Override
					public void run() {
						android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
						String getPhone = userPhoneEdit.getText().toString();
						String getPwd = userPwdEdit.getText().toString();
						String getWorkNum = workNumEdit.getText().toString();
						SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd");
						String date = t.format(new Date());
						System.out.println("daye"+date);
						String action = MD5StringUtil.MD5Encode(date);
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("userPhone", getPhone));
						params.add(new BasicNameValuePair("userPasswd", getPwd));
						params.add(new BasicNameValuePair("workerNum", getWorkNum));
						params.add(new BasicNameValuePair("action", action));
						String string1 = url+
								"?userPhone"+getPhone+"&userPasswd"+getPwd+"&workerNum"+getWorkNum+"&action"+action;
							System.out.println("ssss"+string1);
						String string = HttpObtain.sendDate(url, params);
						System.out.println("registerREGIDSTER:"+string);
						Message msg = handler.obtainMessage();
						if (string.equals(BackInfo.RESPONSE_REGISTER_OK)) {
							msg.what = 0x01;
						} else if (string.equals(BackInfo.RESPONSE_REGISTER_REPEAT)) {
							msg.what = 0x02;
						} else if (string.equals(BackInfo.RESPONSE_DATABASE_ERROR)) {
							msg.what = 0x03;
						} else if (string.equals(BackInfo.RESPONSE_ACTION_ERROR)) {
							msg.what = 0x04;
						} else {
							msg.what = 0x05;
						}
						handler.sendMessage(msg);
					}
				};
				thread.start();
			}
		}

	}

	class ResetListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			userPhoneEdit.setText("");
			userPwdEdit.setText("");
			workNumEdit.setText("");
		}

	}

	class ExitListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			userPhoneEdit.setText("");
			userPwdEdit.setText("");
			workNumEdit.setText("");
			Intent intent = new Intent();
			intent.setClass(Register.this, Login.class);
			startActivity(intent);
			finish();
		}

	}
}
