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
 * @author Administrator ��¼
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
		// ��ʼ��View
		findViews();
		// ��ʼ���û���¼��Ϣ
		initLoginInfo();
	}

	private void init() {
		mApp = (App) getApplication();
		// ��ʼ����ʾ��
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("��ʾ");
		mProgressDialog.setMessage("��½��,���Ժ�...");
		// ��ʼ����������
		mRequestQueue = Volley.newRequestQueue(this);
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void findViews() {
		// �������Ͻ�ͼ�겻�ɼ�
		findViewById(R.id.topButton).setVisibility(View.INVISIBLE);
		// ���ñ���
		((TextView) findViewById(R.id.topTv)).setText("��¼ALL�ʼ�");
		// �û���
		mEditTextUserName = (EditText) findViewById(R.id.et_activity_login_username);
		// ����
		mEditTextPassword = (EditText) findViewById(R.id.et_activity_login_password);
		// �Ƿ��ס����
		mCheckBoxIsSavePassword = (CheckBox) findViewById(R.id.ck_activity_login_is_save_password);
		// �Ƿ��Զ���¼
		mCheckBoxAutoLogin = (CheckBox) findViewById(R.id.ck_activity_login_is_auto_login);
		// ��¼
		((Button) findViewById(R.id.bt_activity_login_login))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (checkUserNameAndPassword()) {
							// ��¼
							Login();
						}
					}
				});
		// ע��
		((Button) findViewById(R.id.bt_activity_login_regist))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// ��ת��ע�����
						startActivity(new Intent(LoginActivity.this,
								RegisterActivity.class));
					}
				});
	}

	/**
	 * ��¼
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
						// ��֤��½���
						checkResult(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						// ��¼ʧ�ܣ��������
						loginFalure();
					}
				});
		mRequestQueue.add(loginRequest);
	}

	protected void loginFalure() {
		mProgressDialog.dismiss();
		CustomToast.showToast(this, "�������Ӵ���", 2000);
	}

	/**
	 * ��֤��½���
	 * 
	 * @param response
	 *            ���󷵻ص�json�ַ���
	 */
	protected void checkResult(String response) {

		try {
			JSONObject obj = new JSONObject(response);
			String code = (String) obj.get("code");
			if (code.equals("1")) {
				// ��½�ɹ��������û���Ϣ
				mApp.setUser(LoginJsonUtil.saveUserInfo(response,
						mEditTextUserName.getText().toString()));
				// �Ƿ񱣴�����
				if (mCheckBoxIsSavePassword.isChecked()) {
					savePassword(mEditTextUserName.getText().toString(),
							mEditTextPassword.getText().toString(), true,
							mCheckBoxAutoLogin.isChecked());
				} else {
					savePassword(mEditTextUserName.getText().toString(), "",
							false, mCheckBoxAutoLogin.isChecked());
				}
				CustomToast.showToast(this, "��¼�ɹ�", 2000);
				// ��½�ɹ�����ת��MainActivity
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				finish();
			} else {
				CustomToast.showToast(this, "��¼ʧ��,�˻������벻��ȷ", 2000);
			}
		} catch (JSONException e) {
			Log.e("TAG", e.getMessage(), e);
		}
	}

	/**
	 * ��������
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
	 * ��ʼ���û���¼��Ϣ
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
			// ��¼
			Login();
		}
	}

	/**
	 * ��֤�û�����
	 * 
	 * @return
	 */
	protected boolean checkUserNameAndPassword() {
		String userName = mEditTextUserName.getText().toString();
		String passWord = mEditTextPassword.getText().toString();
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		if (userName == null || userName.equals("")) {
			CustomToast.showToast(this, "����д�˻�", 2000);
			mEditTextUserName.requestFocus();
			return false;
		} else {
			Matcher matcher = pattern.matcher(userName);
			if (!matcher.matches()) {
				CustomToast.showToast(this, "����д��ȷ��Email", 2000);
				mEditTextUserName.requestFocus();
				return false;
			}
		}
		if (passWord == null || passWord.equals("")) {
			CustomToast.showToast(this, "����д����", 2000);
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
