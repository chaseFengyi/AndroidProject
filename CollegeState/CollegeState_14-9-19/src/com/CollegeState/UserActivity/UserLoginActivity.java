package com.CollegeState.UserActivity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.JSONUtils;

/**
 * @author zc 用户登录的activity
 */
public class UserLoginActivity extends Activity {
	// 登录界面用户输入的账号和密码
	private EditText accountLoginEditText = null;
	private EditText passwordLoginEditText = null;
	// 是否保存用户账号信息的单选按钮
	private CheckBox isSaveDataCheckBox = null;
	//忘记密码按钮
	private Button ForgetPasswdButton = null;
	// 用户登录的按钮
	private Button loginButton = null;
	// 注册按钮
	private Button userRegister = null;
	private ImageButton back;
	private TextView titleText;// back右边显示activity功能的text
	// 保存登录信息的SharedPreference
	private SharedPreferences sharedPreference = null;
	private SharedPreferences.Editor editor = null;

	private String SHARED_PREF_NAME = "USER_LOGIN_INFO";
	private String SHARED_PREF_USER_ACCOUNT = "account";
	private String SHARED_PREF_USER_PASSWORD = "password";
	// 与服务器通信相关参数名
	private String USER_PHONE = "userPhone";
	private String USER_PASSWD = "userPasswd";
	private String LOGIN_ERROR = "noUser_or_passwdError";

	// 开始登录时的进度条
	private ProgressDialog loginDialog = null;
	// 处理登录的Handler
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				// 用户密码错误
				loginDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserLoginActivity.this)
						.setTitle("温馨提示")
						.setMessage("用户名或密码错误")
						.setPositiveButton("请重新输入",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 要求用户重新输入密码
										dialog.dismiss();
										accountLoginEditText.requestFocus();
									}
								});
				builder.create().show();
			} else if (msg.what == 0x345) {
				// 登录成功，创建用户信息的单例，并进行跳转或显示登录成功状态
				loginDialog.dismiss();
				String jsonString = msg.obj.toString();
				// 判断保存用户信息是否成功
				JSONUtils.setUserInfoAfterLogin(jsonString);
				// 标记登录成功状态
				UserInfoBean.setLogin(true);
				Toast.makeText(UserLoginActivity.this, "登陆成功",
						Toast.LENGTH_SHORT).show();
				finish();
			}// 网络错误
			else if (msg.what == 0x456) {
				loginDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserLoginActivity.this)
						.setTitle("温馨提示")
						.setMessage("网络错误")
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								})
						.setPositiveButton("去更改网络状态",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 跳转到系统的activity去更改网络
										dialog.dismiss();
										Intent intent = new Intent();
										intent.setAction(android.provider.Settings.ACTION_SETTINGS);
										startActivity(intent);
									}

								});
				builder.create().show();
			}// 服务器错误
			if (msg.what == 0x999) {
				loginDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserLoginActivity.this)
						.setTitle("溫馨提示")
						.setMessage("系统正在升级，稍后再试")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder.create().show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// activity动画
		overridePendingTransition(R.anim.anim_in_from_right,
				R.anim.anim_out_to_left);
		accountLoginEditText = (EditText) findViewById(R.id.EditTextUserLoginAccount);
		passwordLoginEditText = (EditText) findViewById(R.id.EditTextUserLoginPassword);
		isSaveDataCheckBox = (CheckBox) findViewById(R.id.CheckBoxIsSavePasswd);
		ForgetPasswdButton = (Button)findViewById(R.id.ButtonForgetPasswd);
		// 记住密码自动勾选
		isSaveDataCheckBox.setChecked(true);
		loginButton = (Button) findViewById(R.id.ButtonUserLoginConfirme);
		userRegister = (Button) findViewById(R.id.ButtonUserRegister);
		initActionbar();// 自定义标题栏
		// 第一步判断是否是注册之后传来Intent
		Intent receiveIntent = getIntent();
		if (receiveIntent.getStringExtra(UserRegisterActivity.USER_ACCOUNT) != null) {
			if (receiveIntent
					.getStringExtra(UserRegisterActivity.USER_PASSWORD) != null) {
				accountLoginEditText.setText(receiveIntent
						.getStringExtra(UserRegisterActivity.USER_ACCOUNT));
				passwordLoginEditText.setText(receiveIntent
						.getStringExtra(UserRegisterActivity.USER_PASSWORD));
			}
		} else {
			// 显示保存的信息
			getSavedData();
		}
		//忘记密码按钮监听
		ForgetPasswdButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("http://121.42.8.50/CS/customer/Customer_resetpasswd_input.jsp");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		// 登录按钮事件监听
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String userAccount = accountLoginEditText.getText()
						.toString();
				final String userPassword = passwordLoginEditText.getText()
						.toString();
				if (userAccount.equals("")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserLoginActivity.this)
							.setTitle("温馨提示!")
							.setMessage("请输入您的账号!")
							.setPositiveButton("重新输入",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 账号输入框获取焦点
											dialog.dismiss();
											accountLoginEditText.requestFocus();
										}
									});
					builder.create().show();
					return;
				}
				if (userPassword.equals("")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserLoginActivity.this)
							.setTitle("温馨提示!")
							.setMessage("请输入您的密码!")
							.setPositiveButton("重新输入",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 密码输入框获取焦点
											dialog.dismiss();
											passwordLoginEditText
													.requestFocus();
										}
									});
					builder.create().show();
					return;
				}
				// 判断网络状态
				if (!HttpUtils.isNetWorkEnable(UserLoginActivity.this)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserLoginActivity.this)
							.setTitle("温馨提示")
							.setMessage("网络错误")
							.setNegativeButton("返回",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									})
							.setPositiveButton("去更改网络状态",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 跳转到系统的activity去更改网络
											dialog.dismiss();
											Intent intent = new Intent();
											intent.setAction(android.provider.Settings.ACTION_SETTINGS);
											startActivity(intent);
										}

									});
					builder.create().show();
				}
				// 开始登录(创建进度条)
				loginDialog = new ProgressDialog(UserLoginActivity.this);
				loginDialog.setTitle("溫馨提示");
				loginDialog.setMessage("正在登录");
				loginDialog.setCancelable(true);
				loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				loginDialog.show();
				new Thread() {
					public void run() {
						try {
							// 发送请求

							ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair(USER_PHONE,
									userAccount));
							params.add(new BasicNameValuePair(USER_PASSWD,
									userPassword));

							String result = HttpUtils.queryStringForPost(
									HttpUtils.USER_LOGIN_URL, params);
							// 用户名或密码错误
							if (result.equals(LOGIN_ERROR)) {
								handler.sendEmptyMessage(0x123);
							}
							// 网络错误
							else if (result.equals(HttpUtils.NETWORK_ERROR)) {
								handler.sendEmptyMessage(0x456);
							} else if (TextUtils.isEmpty(result)) {
								handler.sendEmptyMessage(0x999);
							}
							// 登录成功创建user对象
							else {
								Message msg = new Message();
								msg.what = 0x345;
								// 使用obj来封装这个JSONString
								msg.obj = result;
								handler.sendMessage(msg);
								saveUserLoginData();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();
			}

		});
		// 注册按钮事件监听
		userRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(UserLoginActivity.this,
						UserRegisterActivity.class);
				finish();
				UserLoginActivity.this.startActivity(intent);
			}
		});
	}

	/**
	 * 当activity启动时自动从SharedPreference中读取用户信息
	 */
	private void getSavedData() {
		sharedPreference = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
		// 从手机中获取SharedPreference，获取失败时返回""字符串,并显示在控件上
		String userAccount = sharedPreference.getString(
				SHARED_PREF_USER_ACCOUNT, "");
		String userPassword = sharedPreference.getString(
				SHARED_PREF_USER_PASSWORD, "");
		// 读取到界面
		accountLoginEditText.setText(userAccount);
		passwordLoginEditText.setText(userPassword);
	}

	/**
	 * 当登录成功时使用SharedPreference存取用户信息
	 * 
	 * @return
	 */
	private boolean saveUserLoginData() {
		// 当用户选择不自动保存时返回
		if (!shouldAutoSaveData()) {
			sharedPreference = getSharedPreferences(SHARED_PREF_NAME,
					Context.MODE_PRIVATE);
			editor = sharedPreference.edit();
			editor.clear();
			editor.commit();
			return false;
		} else {
			String userAccount = accountLoginEditText.getText().toString();
			String password = passwordLoginEditText.getText().toString();
			sharedPreference = getSharedPreferences(SHARED_PREF_NAME,
					Context.MODE_PRIVATE);
			editor = sharedPreference.edit();
			editor.putString(SHARED_PREF_USER_ACCOUNT, userAccount);
			editor.putString(SHARED_PREF_USER_PASSWORD, password);
			editor.commit();
			return true;
		}
	}

	/**
	 * 用于判断用户是否需要保存登录信息
	 * 
	 * @return
	 */
	private boolean shouldAutoSaveData() {
		if (isSaveDataCheckBox.isChecked()) {
			return true;
		}
		return false;
	}

	/**
	 * 这个接口用于以后再菜单中添加新功能：修改信息，找回密码等
	 * 
	 * @param menu
	 * @return
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	private void initActionbar() {
		// 自定义标题栏
		getActionBar().setDisplayShowHomeEnabled(false); // 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
		getActionBar().setDisplayShowTitleEnabled(false); // 对应ActionBar.DISPLAY_SHOW_TITLE。
		getActionBar().setDisplayShowCustomEnabled(true);// 使自定义的普通View能在title栏显示
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mTitleView = mInflater.inflate(
				R.layout.custom_action_bar_onlyback, null);
		getActionBar().setCustomView(
				mTitleView,
				new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		titleText = (TextView) findViewById(R.id.title_name);
		titleText.setText("登陆");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
