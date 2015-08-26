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
		// ��ʼ��View
		findViews();
		// ��ʼ����������
		mRequestQueue = Volley.newRequestQueue(this);
		// ��ʼ����ʾ��
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("��ʾ");
		mProgressDialog.setMessage("ע����,���Ժ�...");
	}

	/**
	 * ��ʼ��View
	 */
	private void findViews() {
		// �������Ͻ�ͼ�귵��
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
					// ע��
					register();
				}
			}
		});
	}

	/**
	 * ע��
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
						// ��֤���ؽ��
						checkResult(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						// �������Ӵ���
						registerFalure();
					}
				});
		mRequestQueue.add(registRequest);
	}

	/**
	 * ��֤���ؽ��
	 * 
	 * @param response
	 *            ���󷵻ص��ַ���
	 */
	protected void checkResult(String response) {
		mProgressDialog.dismiss();
		try {
			JSONObject obj = new JSONObject(response);
			String code = (String) obj.get("code");
			if (code.equals("1")) {
				CustomToast.showToast(this, "ע��ɹ�", 2000);
				finish();
			} else {
				JSONArray msgs = obj.getJSONArray("msg");
				String msg = msgs.getString(0);
				System.out.println("msg--->" + msg);
				if (msg.equals(EMAIL_EXIST)) {
					CustomToast.showToast(this, "�������Ѿ�ע��", 2000);
					mEditTextEmail.requestFocus();
				}
			}
		} catch (JSONException e) {
			Log.e("TAG", e.getMessage(), e);
		}
	}

	/**
	 * �������Ӵ���
	 */
	protected void registerFalure() {
		mProgressDialog.dismiss();
		CustomToast.showToast(this, "�������Ӵ���", 2000);
	}

	/**
	 * ��֤�û�����
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
			CustomToast.showToast(this, "����д�û���", 2000);
			mEditTextUserName.requestFocus();
			return false;
		}
		if (email == null || email.equals("")) {
			CustomToast.showToast(this, "��������", 2000);
			mEditTextUserName.requestFocus();
			return false;
		} else {
			Matcher matcher = pattern.matcher(email);
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
		if (againPassWord == null || againPassWord.equals("")) {
			CustomToast.showToast(this, "���ٴ���������", 2000);
			mEditTextPassword.requestFocus();
			return false;
		}
		if (!passWord.equals(againPassWord)) {
			CustomToast.showToast(this, "�����������벻һ��", 2000);
			mEditTextAgainPassword.setText("");
			mEditTextAgainPassword.requestFocus();
			return false;
		}
		return true;
	}
}
