package com.example.sendlist;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.publicInfo.BackInfo;
import com.request.HttpObtain;
import com.request.MD5StringUtil;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class UserRegisterActivity extends Activity {
	private EditText userPhoneEdit;
	private EditText userPwdEdit;
	private EditText workNumEdit;
	private Button ok;
	private Button reset;
	private Button exit;
	private static final String url = "http://121.42.8.50/CS1/customer/User_register";
	private static boolean sign = false;

	// 用户注册成功的异步处理Handler
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			// 表示注册成功（Register_Ok）
			if (msg.what == 0x123) {
				
			}
		}
			
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registe);
		
		userPhoneEdit = (EditText) findViewById(R.id.userPhoneEdit);
		userPwdEdit = (EditText) findViewById(R.id.userPwdEdit);
		workNumEdit = (EditText) findViewById(R.id.userWorkEdit);
		ok = (Button) findViewById(R.id.ok);
		
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取注册表中的所有数据
				
				// 上传注册信息
				new Thread() {
					public void run() {
						// http://222.24.63.101/Ordering/customerregcl?account=zzzzzz&passwd=123456&phone=13512347896&collegeId=1&apartId=1&dormNum=123
						try {
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
							/*String string1 = url+
									"?userPhone"+getPhone+"&userPasswd"+getPwd+"&workerNum"+getWorkNum+"&action"+action;*/
							String string = HttpObtain.sendDate(url, params);
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
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}.start();
			}
		});
	}

}
