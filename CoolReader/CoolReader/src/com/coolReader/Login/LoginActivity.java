package com.coolReader.Login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.coolReader.R;
import com.coolReader.Application.SysApplication;
import com.coolReader.Register.RegisterActivity;
import com.coolReader.Util.AddressUtil;
import com.coolReader.Util.BackTagUtils;
import com.coolReader.Util.DialogUtils;
import com.coolReader.Util.ToastUtils;
import com.coolReader.mainPage.MainPageActivity;
import com.coolReader.request.JsonResolve;
import com.coolReader.request.TCPRequest;

/**
 * 登录
 * 
 * @author Xuptljw 2015年7月22日15:07:58
 */
public class LoginActivity extends Activity implements OnClickListener {

	private EditText uname = null;
	private EditText upass = null;
	private Button loginButton = null;
	private Button registerButton = null;
	private Dialog loginDialog = null;

	private final int OK = 1;
	private final int USER_NOT_EXIST = 2;
	private final int SERVER_ERROR = 3;
	private final int WRONG_PASSWD = 4;

	public static String USER_EMAIL_STR = "";
	private CheckBox mCheckBoxIsSavePassword = null;
	private CheckBox mCheckBoxAutoLogin = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		SysApplication.getInstance().addActivity(this);
		// 得到页面布局控件
		findViews();
		initLoginInfo();
//		exit();
	}
	
	/**
	 * 当用户点击注销，返回到登录界面
	 */
	public void exit(){
		Intent intent = getIntent();
		if(intent == null)
			return;
		String set = intent.getStringExtra("setting");
		Log.i("login-set", set);
		if(set.equals("setting")){//通过注销进入登陆界面
			upass.setText("");
			mCheckBoxAutoLogin.setChecked(false);
			mCheckBoxIsSavePassword.setChecked(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void findViews() {
		// 得到页面的空间
		loginButton = (Button) this.findViewById(R.id.login_button);
		registerButton = (Button) this.findViewById(R.id.register_button);
		uname = (EditText) this.findViewById(R.id.uname);
		upass = (EditText) this.findViewById(R.id.upass);
		// 是否记住密码
		mCheckBoxIsSavePassword = (CheckBox) findViewById(R.id.ck_activity_login_is_save_password);
		// 是否自动登录
		mCheckBoxAutoLogin = (CheckBox) findViewById(R.id.ck_activity_login_is_auto_login);

		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
	}
	

	/**
	 * 初始化用户登录信息
	 */
	private void initLoginInfo() {
		SharedPreferences sp = getSharedPreferences("Cool-Reader",
				Context.MODE_PRIVATE);
		uname.setText(sp.getString("email", ""));
		upass.setText(sp.getString("password", ""));
		mCheckBoxIsSavePassword.setChecked(sp
				.getBoolean("SAVE_PASSWORD", false));
		boolean autoLogin = sp.getBoolean("auto_login", false);
		mCheckBoxAutoLogin.setChecked(autoLogin);
		if (autoLogin && validate()) {
			// 登录
			login();
		}
	}


	/**
	 * 保存密码
	 */
	private void savePassword(String email, String password,
			boolean save_password, boolean auto_login) {
		SharedPreferences sp = getSharedPreferences("Cool-Reader",
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putString("email", email);
		ed.putString("password", password);
		ed.putBoolean("SAVE_PASSWORD", save_password);
		ed.putBoolean("auto_login", auto_login);
		ed.commit();
	}

	
	// 校验登录
	public boolean validate() {
		String name = uname.getText().toString();
		String pass = upass.getText().toString();
		if (TextUtils.isEmpty(name)) {
			ToastUtils.makeToast(this, "用户名不能为空");
			return false;
		}
		if (TextUtils.isEmpty(pass)) {
			ToastUtils.makeToast(this, "密码不能为空");
			return false;
		}
		return true;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 获取到请求响应信息后关闭等待端口
			loginDialog.cancel();
			switch (msg.what) {
			case SERVER_ERROR:
				ToastUtils.makeToast(LoginActivity.this, "网络错误");
				break;
			case USER_NOT_EXIST:
				ToastUtils.makeToast(LoginActivity.this, "用户不存在");
				break;
			case OK:
				ToastUtils.makeToast(LoginActivity.this, "登录成功");
				// 登陆成功，保存用户信息
				// 是否保存密码
				if (mCheckBoxIsSavePassword.isChecked()) {
					savePassword(uname.getText().toString(),
							upass.getText().toString(), true,
							mCheckBoxAutoLogin.isChecked());
				} else {
					savePassword(uname.getText().toString(), "",
							false, mCheckBoxAutoLogin.isChecked());
				}
				// 在日志中查看得到的信息
				Log.i("success", "登录成功" + (String) msg.obj);
				Intent intent = new Intent(LoginActivity.this,
						MainPageActivity.class);
				startActivity(intent);
				finish();
				break;
			case WRONG_PASSWD:
				ToastUtils.makeToast(LoginActivity.this, "密码错误");
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 登陆
	 */
	private void login(){
		// 创建对话框显示正在登陆
		loginDialog = DialogUtils.createProgressDialog(this,
				"CodingReader", "isLoading");
		loginDialog.show();
		String name = uname.getText().toString().trim();
		String pass = upass.getText().toString().trim();
		USER_EMAIL_STR = name;

		JSONObject object = new JSONObject();
		try {
			object.put("funType", 10).put(
					"funContent",
					new JSONObject().put("userInfo", new JSONObject()
							.put("mailAddr", name)
							.put("password", pass)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String string = object.toString();
		new clientThread(string, AddressUtil.TCT_ADDRESS,
				AddressUtil.TCP_PORT).start();

	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_button:
			if (validate()) {
				login();
				// startActivity(new Intent(LoginActivity.this,
				// MainPageActivity.class));
			}
			break;
		case R.id.register_button:
			// 直接跳转到注册页面
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		default:
			break;
		}
	}

	class clientThread extends Thread {
		String string;
		String dstAddress;
		int dstPort;

		public clientThread(String string, String dstAddress, int dstPort) {
			this.string = string;
			this.dstAddress = dstAddress;
			this.dstPort = dstPort;
		}

		@Override
		public void run() {
			String receive = TCPRequest.tcpRequest(string, dstAddress, dstPort);
			String describe = JsonResolve.fun_des_resolve(receive);
			Message message = new Message();
			if (describe.equals(BackTagUtils.OK)) {
				message.what = OK;
			} else if (describe.equals(BackTagUtils.USER_NOT_EXIST)) {
				message.what = USER_NOT_EXIST;
			} else if (describe.equals(BackTagUtils.SERVER_ERROR)) {
				message.what = SERVER_ERROR;
			} else {
				message.what = WRONG_PASSWD;
			}
			// message.obj = receive;
			handler.sendMessage(message);
		}
	}

}
