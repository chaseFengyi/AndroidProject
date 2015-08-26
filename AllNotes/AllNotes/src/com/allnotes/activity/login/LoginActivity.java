package com.allnotes.activity.login;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.allnotes.R;
import com.allnotes.activity.main.MainActivity;
import com.allnotes.activity.regist.RegisterActivity;
import com.allnotes.app.App;
import com.allnotes.customtoast.CustomToast;
import com.allnotes.utils.HttpUtils;
import com.allnotes.utils.UrlUtils;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Administrator 登录
 */
public class LoginActivity extends Activity {

	private RequestQueue mRequestQueue = null;
	private ProgressDialog mProgressDialog = null;
	private App mApp;
	private EditText mEditTextUserName = null;
	private EditText mEditTextPassword = null;
	private CheckBox mCheckBoxIsSavePassword = null;
	private CheckBox mCheckBoxAutoLogin = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		init();
		// 初始化View
		findViews();
		// 初始化用户登录信息
		initLoginInfo();
	}

	private void init() {
		mApp = (App) getApplication();
		// 初始化提示框
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("提示");
		mProgressDialog.setMessage("登陆中,请稍候...");
		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(this);
	}

	/**
	 * 初始化控件
	 */
	private void findViews() {
		// 设置左上角图标不可见
		findViewById(R.id.topButton).setVisibility(View.INVISIBLE);
		// 设置标题
		((TextView) findViewById(R.id.topTv)).setText("登录ALL笔记");
		// 用户名
		mEditTextUserName = (EditText) findViewById(R.id.et_activity_login_username);
		// 密码
		mEditTextPassword = (EditText) findViewById(R.id.et_activity_login_password);
		// 是否记住密码
		mCheckBoxIsSavePassword = (CheckBox) findViewById(R.id.ck_activity_login_is_save_password);
		// 是否自动登录
		mCheckBoxAutoLogin = (CheckBox) findViewById(R.id.ck_activity_login_is_auto_login);
		// 登录
		((Button) findViewById(R.id.bt_activity_login_login))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (checkUserNameAndPassword()) {
							// 登录
							Login();
						}
					}
				});
		// 注册
		((Button) findViewById(R.id.bt_activity_login_regist))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 跳转到注册界面
						startActivity(new Intent(LoginActivity.this,
								RegisterActivity.class));
					}
				});
	}

	/**
	 * 登录
	 */
	private void Login() {
		mProgressDialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("email", mEditTextUserName.getText().toString());
		rawParams.put("password", mEditTextPassword.getText().toString());
		HttpUtils loginRequest = new HttpUtils(UrlUtils.Login, rawParams,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						mProgressDialog.dismiss();
						Log.d("TAG", response);
						// 验证登陆结果
						checkResult(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						// 登录失败，网络错误
						loginFalure();
					}
				});
		mRequestQueue.add(loginRequest);
	}

	protected void loginFalure() {
		mProgressDialog.dismiss();
		CustomToast.showToast(this, "网络连接错误", 2000);
	}

	/**
	 * 验证登陆结果
	 * 
	 * @param response
	 *            请求返回的json字符串
	 */
	protected void checkResult(String response) {

		try {
			JSONObject obj = new JSONObject(response);
			String code = (String) obj.get("code");
			if (code.equals("1")) {
				// 登陆成功，保存用户信息
				mApp.setUser(LoginJsonUtil.saveUserInfo(response,
						mEditTextUserName.getText().toString()));
				// 是否保存密码
				if (mCheckBoxIsSavePassword.isChecked()) {
					savePassword(mEditTextUserName.getText().toString(),
							mEditTextPassword.getText().toString(), true,
							mCheckBoxAutoLogin.isChecked());
				} else {
					savePassword(mEditTextUserName.getText().toString(), "",
							false, mCheckBoxAutoLogin.isChecked());
				}
				CustomToast.showToast(this, "登录成功", 2000);
				// 登陆成功，跳转到MainActivity
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				finish();
			} else {
				CustomToast.showToast(this, "登录失败,账户或密码不正确", 2000);
			}
		} catch (JSONException e) {
			Log.e("TAG", e.getMessage(), e);
		}
	}

	/**
	 * 保存密码
	 */
	private void savePassword(String email, String password,
			boolean save_password, boolean auto_login) {
		SharedPreferences sp = getSharedPreferences("ALL_NOTE",
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putString("email", email);
		ed.putString("password", password);
		ed.putBoolean("SAVE_PASSWORD", save_password);
		ed.putBoolean("auto_login", auto_login);
		ed.commit();
	}

	/**
	 * 初始化用户登录信息
	 */
	private void initLoginInfo() {
		SharedPreferences sp = getSharedPreferences("ALL_NOTE",
				Context.MODE_PRIVATE);
		mEditTextUserName.setText(sp.getString("email", ""));
		mEditTextPassword.setText(sp.getString("password", ""));
		mCheckBoxIsSavePassword.setChecked(sp
				.getBoolean("SAVE_PASSWORD", false));
		boolean autoLogin = sp.getBoolean("auto_login", false);
		mCheckBoxAutoLogin.setChecked(autoLogin);
		if (autoLogin && checkUserNameAndPassword()) {
			// 登录
			Login();
		}
	}

	/**
	 * 验证用户输入
	 * 
	 * @return
	 */
	protected boolean checkUserNameAndPassword() {
		String userName = mEditTextUserName.getText().toString();
		String passWord = mEditTextPassword.getText().toString();
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		if (userName == null || userName.equals("")) {
			CustomToast.showToast(this, "请填写账户", 2000);
			mEditTextUserName.requestFocus();
			return false;
		} else {
			Matcher matcher = pattern.matcher(userName);
			if (!matcher.matches()) {
				CustomToast.showToast(this, "请填写正确的Email", 2000);
				mEditTextUserName.requestFocus();
				return false;
			}
		}
		if (passWord == null || passWord.equals("")) {
			CustomToast.showToast(this, "请填写密码", 2000);
			mEditTextPassword.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		mRequestQueue.cancelAll(this);
	}

}
