package com.example.sendlist;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.publicInfo.BackInfo;
import com.request.HttpObtain;
import com.request.JsonResolve;

public class Login extends Activity {
	private AutoCompleteTextView userEdit;
	private EditText passEdit;
	private Button enter;
	private Button registe;
	private CheckBox checkBox;

	public static String WORKERNUM = "";
	public static String ACTION = "";
	public static String USERPHONE = "";
	public static String USERPASSWD = "";
	private static String user;
	private static String pass;
	public static String url = "http://121.42.8.50/CS1/customer/User_login";
	private static boolean isChecked = true;
	private SharedPreferences sp;
	private static List<UserInfo> list1 = new ArrayList<UserInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		onClick();
		save();
		click();
	}

	private void onClick() {
		userEdit = (AutoCompleteTextView) findViewById(R.id.accountEdit);
		passEdit = (EditText) findViewById(R.id.passEdit);
		enter = (Button) findViewById(R.id.enter);
		registe = (Button) findViewById(R.id.registe);
		checkBox = (CheckBox) findViewById(R.id.checkBox);
	}

	// 将填入的用户名密码保存在手机上
	private void save() {
		sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE);
		userEdit.setThreshold(1);// 输入1个字母就开始自动提示
		passEdit.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		// 隐藏密码为InputType.TYPE_TEXT_VARIATION_PASSWORD，也就是0x81
		// 显示密码为InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD，也就是0x91
		userEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				String[] allUserName = new String[sp.getAll().size()];// sp.getAll().size()返回的是有多少个键值对
				allUserName = sp.getAll().keySet().toArray(new String[0]);
				// sp.getAll()返回一张hash map
				// keySet()得到的是a set of the keys.
				// hash map是由key-value组成的

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						Login.this,
						android.R.layout.simple_dropdown_item_1line,
						allUserName);

				userEdit.setAdapter(adapter);// 设置数据适配器
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				passEdit.setText(sp
						.getString(userEdit.getText().toString(), ""));// 自动输入密码
			}
		});
	}

	// 点击监听
	private void click() {
		enter.setOnClickListener(new EnterListener());
		registe.setOnClickListener(new RegisteListener());
		checkBox.setOnCheckedChangeListener(new CheckBoxListener());
	}

	private class CheckBoxListener implements
			android.widget.CompoundButton.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if (arg1) {// 选中
				isChecked = true;
			} else {
				isChecked = false;
			}
		}

	}

	// 判断username与password是否与已经注册的信息吻合
	private int judgeInfo(String usered, String passed, String user, String pass) {
		if (usered.equals(user)) {
			if (passed.equals(pass)) {
				return 0;
			}
			return 1;
		}
		return -1;
	}

	public class MyRunnable extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userPhone", user));
			params.add(new BasicNameValuePair("userPasswd", pass));
			String string = HttpObtain.getStringFromHttp(url, params);
			Message msg = handler.obtainMessage();
			if (string.equals(BackInfo.RESPONSE_NOUSER_OR_PASSWDERROR)) {
				msg.what = 0x02;
			} else {
//				System.out.println("STRING:" + string);
				msg.obj = JsonResolve.getLoginInfo(string);
//				System.out.println("OBJ:" + JsonResolve.getLoginInfo(string));
				msg.what = 0x01;
			}
			handler.sendMessage(msg);
		}

	}

	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x01:
				list1 = (List<UserInfo>) msg.obj;
//				System.out.println("LIST:" + list1.size());
				for (int i = 0; i < list1.size(); i++) {
					if (user.equals(list1.get(i).getUserPhone())) {
						USERPHONE = list1.get(i).getUserPhone();
						USERPASSWD = list1.get(i).getUserPasswd();
						ACTION = list1.get(i).getAction();
//						System.out.println("ACTION:" + ACTION);
						WORKERNUM = list1.get(i).getWorkerNum();
					}
				}

				String usered = USERPHONE;
				String passed = USERPASSWD;
				if (judgeInfo(usered, passed, user, pass) == 0) {
					// 登陆成功
					if (!isChecked) {
						passEdit.setText("");
					}
					sp.edit().putString(usered, passed).commit();
					Intent intent = new Intent();
					intent.setClass(Login.this, Logined.class);
					startActivity(intent);
				} else if (judgeInfo(usered, passed, user, pass) == -1) {
					// 如果用户名错误
					Toast.makeText(Login.this, "对不起，用户名错误，请重新登录",
							Toast.LENGTH_SHORT).show();
					userEdit.setText("");
					passEdit.setText("");
				} else {
					// 密码错误
					Toast.makeText(Login.this, "对不起，密码错误，请重新登录",
							Toast.LENGTH_SHORT).show();
					passEdit.setText("");
				}
				break;

			case 0x02:
				Toast.makeText(Login.this, "没用户或密码错误", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}

	};

	// 当用户点击登录按钮所触发的事件
	class EnterListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			user = userEdit.getText().toString();
			pass = passEdit.getText().toString();

			// 判断网络连接是否成功
			if (HttpObtain.isNetWorkEnable(Login.this)) {
				if (user.equals("") || pass.equals("")) {
					Toast.makeText(Login.this, "用户名或密码不能为空", Toast.LENGTH_SHORT)
							.show();
				}
				new MyRunnable().start();
			} else {
				Toast.makeText(Login.this, "对不起，网络尚未连接，情检查网络",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	// 当用户点击注册按钮
	class RegisteListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(Login.this, Register.class);
			startActivity(intent);
			finish();
		}

	}

}
