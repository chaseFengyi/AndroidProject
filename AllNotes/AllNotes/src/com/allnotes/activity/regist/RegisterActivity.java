package com.allnotes.activity.regist;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.allnotes.R;
import com.allnotes.customtoast.CustomToast;
import com.allnotes.utils.HttpUtils;
import com.allnotes.utils.UrlUtils;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RegisterActivity extends Activity {

	private static final String EMAIL_EXIST = "The email has already been taken.";

	private RequestQueue mRequestQueue = null;
	private EditText mEditTextUserName = null;
	private EditText mEditTextEmail = null;
	private EditText mEditTextPassword = null;
	private EditText mEditTextAgainPassword = null;
	private Button mButtonRegister = null;
	private ProgressDialog mProgressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		// 初始化View
		findViews();
		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(this);
		// 初始化提示框
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("提示");
		mProgressDialog.setMessage("注册中,请稍候...");
	}

	/**
	 * 初始化View
	 */
	private void findViews() {
		// 设置左上角图标返回
		((ImageView) findViewById(R.id.activity_topButton))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		mEditTextUserName = (EditText) findViewById(R.id.et_activity_register_username);
		mEditTextEmail = (EditText) findViewById(R.id.et_activity_register_email);
		mEditTextPassword = (EditText) findViewById(R.id.et_activity_register_password);
		mEditTextAgainPassword = (EditText) findViewById(R.id.et_activity_register_again_password);
		mButtonRegister = (Button) findViewById(R.id.bt_activity_register_register);
		mButtonRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkInput()) {
					// 注册
					register();
				}
			}
		});
	}

	/**
	 * 注册
	 */
	protected void register() {
		mProgressDialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("name", mEditTextUserName.getText().toString());
		rawParams.put("email", mEditTextEmail.getText().toString());
		rawParams.put("password", mEditTextPassword.getText().toString());
		HttpUtils registRequest = new HttpUtils(UrlUtils.Register, rawParams,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d("TAG", response);
						// 验证返回结果
						checkResult(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						// 网络连接错误
						registerFalure();
					}
				});
		mRequestQueue.add(registRequest);
	}

	/**
	 * 验证返回结果
	 * 
	 * @param response
	 *            请求返回的字符串
	 */
	protected void checkResult(String response) {
		mProgressDialog.dismiss();
		try {
			JSONObject obj = new JSONObject(response);
			String code = (String) obj.get("code");
			if (code.equals("1")) {
				CustomToast.showToast(this, "注册成功", 2000);
				finish();
			} else {
				JSONArray msgs = obj.getJSONArray("msg");
				String msg = msgs.getString(0);
				System.out.println("msg--->" + msg);
				if (msg.equals(EMAIL_EXIST)) {
					CustomToast.showToast(this, "该邮箱已经注册", 2000);
					mEditTextEmail.requestFocus();
				}
			}
		} catch (JSONException e) {
			Log.e("TAG", e.getMessage(), e);
		}
	}

	/**
	 * 网络连接错误
	 */
	protected void registerFalure() {
		mProgressDialog.dismiss();
		CustomToast.showToast(this, "网络连接错误", 2000);
	}

	/**
	 * 验证用户输入
	 * 
	 * @return
	 */
	protected boolean checkInput() {
		String userName = mEditTextUserName.getText().toString();
		String email = mEditTextEmail.getText().toString();
		String passWord = mEditTextPassword.getText().toString();
		String againPassWord = mEditTextAgainPassword.getText().toString();
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		if (userName == null || userName.equals("")) {
			CustomToast.showToast(this, "请填写用户名", 2000);
			mEditTextUserName.requestFocus();
			return false;
		}
		if (email == null || email.equals("")) {
			CustomToast.showToast(this, "请填邮箱", 2000);
			mEditTextUserName.requestFocus();
			return false;
		} else {
			Matcher matcher = pattern.matcher(email);
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
		if (againPassWord == null || againPassWord.equals("")) {
			CustomToast.showToast(this, "请再次输入密码", 2000);
			mEditTextPassword.requestFocus();
			return false;
		}
		if (!passWord.equals(againPassWord)) {
			CustomToast.showToast(this, "两次密码输入不一致", 2000);
			mEditTextAgainPassword.setText("");
			mEditTextAgainPassword.requestFocus();
			return false;
		}
		return true;
	}
}
