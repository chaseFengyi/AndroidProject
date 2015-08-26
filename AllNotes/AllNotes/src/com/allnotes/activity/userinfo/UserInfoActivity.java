package com.allnotes.activity.userinfo;

import java.util.HashMap;
import java.util.Map;


import org.json.JSONException;
import org.json.JSONObject;

import com.allnotes.R;
import com.allnotes.app.App;
import com.allnotes.bean.User;
import com.allnotes.customtoast.CustomToast;
import com.allnotes.utils.HttpUtils;
import com.allnotes.utils.UrlUtils;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class UserInfoActivity extends Activity {

	private RequestQueue mRequestQueue = null;
	private ProgressDialog mProgressDialog = null;
	private App mApp;
	private EditText mEditTextUserName;
	private EditText mEditTextTel;
	private EditText mEditTextCareer;
	private EditText mEditTextEdu;
	private EditText mEditTextDesc;
	private RadioButton mRadioButtonMale;
	private RadioButton mRadioButtonFeMale;
	private CheckBox mCheckBoxShareNote;
	private CheckBox mCheckBoxShareRead;
	private TextView mTextViewEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_info);
		init();
		// 初始化控件
		findViews();
	}

	private void findViews() {
		((TextView) findViewById(R.id.activity_topTv)).setText("修改信息");
		mEditTextUserName = (EditText) findViewById(R.id.et_activity_user_ionf_username);
		mEditTextTel = (EditText) findViewById(R.id.et_activity_user_ionf_tel);
		mEditTextCareer = (EditText) findViewById(R.id.et_activity_user_ionf_career);
		mEditTextEdu = (EditText) findViewById(R.id.et_activity_user_ionf_edu);
		mEditTextDesc = (EditText) findViewById(R.id.et_activity_user_ionf_desc);
		mRadioButtonMale = (RadioButton) findViewById(R.id.rb_activity_user_info_male);
		mRadioButtonFeMale = (RadioButton) findViewById(R.id.rb_activity_user_info_Female);
		mCheckBoxShareNote = (CheckBox) findViewById(R.id.ck_activity_user_info_sharenote);
		mCheckBoxShareRead = (CheckBox) findViewById(R.id.ck_activity_user_info_shareread);
		mTextViewEmail = (TextView) findViewById(R.id.et_activity_user_ionf_email);

		User user = mApp.getUser();
		if (user != null) {
			mEditTextUserName.setText(user.getUserName());
			mTextViewEmail.setText(user.getEmail());
			mEditTextTel.setText(user.getTel());
			mEditTextCareer.setText(user.getCareer());
			mEditTextEdu.setText(user.getEdu());
			mEditTextDesc.setText(user.getDesc());
			if (user.getSex().equals("1")) {
				mRadioButtonMale.setChecked(false);
				mRadioButtonFeMale.setChecked(true);
			}
			if (user.getShareNote().equals("0"))
				mCheckBoxShareNote.setChecked(false);
			if (user.getShareRead().equals("0"))
				mCheckBoxShareRead.setChecked(false);
		}

		// 设置左上角图标返回
		((ImageView) findViewById(R.id.activity_topButton))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});

		// 确认修改
		((Button) findViewById(R.id.bt_activity_user_info_changeinfo))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (checkInput()) {
							showTipDialog();
						}
					}
				});
	}

	/**
	 * 修改用户信息
	 */
	protected void changeUserInfo() {
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("_method", "put");
		rawParams.put("username", mEditTextUserName.getText().toString());
		if (!mEditTextTel.getText().toString().equals(""))
			rawParams.put("tel", mEditTextTel.getText().toString());
		if (!mEditTextDesc.getText().toString().equals(""))
			rawParams.put("desc", mEditTextDesc.getText().toString());
		if (!mEditTextCareer.getText().toString().equals(""))
			rawParams.put("career", mEditTextCareer.getText().toString());
		if (!mEditTextEdu.getText().toString().equals(""))
			rawParams.put("edu", mEditTextEdu.getText().toString());
		if (mRadioButtonMale.isChecked()) {
			rawParams.put("sex", "0");
		} else {
			rawParams.put("sex", "1");
		}

		if (mCheckBoxShareNote.isChecked()) {
			rawParams.put("sharenote", "1");
		} else {
			rawParams.put("sharenote", "0");
		}
		if (mCheckBoxShareRead.isChecked()) {
			rawParams.put("sharerread", "1");
		} else {
			rawParams.put("shareread", "0");
		}
		HttpUtils changeInfoHttp = new HttpUtils(UrlUtils.ChangeUserInfo
				+ mApp.getUser().getCheck() + "/", rawParams,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						mProgressDialog.dismiss();
						Log.d("TAG", response);
						try {
							JSONObject obj = new JSONObject(response);
							if (obj.getString("code").equals("1")) {
								CustomToast.showToast(UserInfoActivity.this,
										"修改成功", 2000);
								// 修改成功后保存用户信息
								saveUserInfo();
								finish();
							} else {
								CustomToast.showToast(UserInfoActivity.this,
										"修改失败", 2000);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						mProgressDialog.dismiss();
						Log.e("TAG", error.getMessage(), error);
						CustomToast.showToast(UserInfoActivity.this, "修改失败",
								2000);
					}
				});
		mRequestQueue.add(changeInfoHttp);
	}

	/**
	 * 保存用户信息
	 */
	protected void saveUserInfo() {
		User user = mApp.getUser();
		user.setUserName(mEditTextUserName.getText().toString());
		if (!mEditTextTel.getText().toString().equals(""))
			user.setTel(mEditTextTel.getText().toString());
		if (!mEditTextDesc.getText().toString().equals(""))
			user.setDesc(mEditTextDesc.getText().toString());
		if (!mEditTextCareer.getText().toString().equals(""))
			user.setCareer(mEditTextCareer.getText().toString());
		if (!mEditTextEdu.getText().toString().equals(""))
			user.setEdu(mEditTextEdu.getText().toString());
		if (mRadioButtonMale.isChecked()) {
			user.setSex("0");
		} else {
			user.setSex("1");
		}
		if (mCheckBoxShareNote.isChecked()) {
			user.setShareNote("1");
		} else {
			user.setShareNote("0");
		}
		if (mCheckBoxShareRead.isChecked()) {
			user.setShareRead("1");
		} else {
			user.setShareRead("0");
		}
	}

	/**
	 * 验证用户输入
	 * 
	 * @return
	 */
	protected boolean checkInput() {
		if (mEditTextUserName.getText().toString().equals("")) {
			CustomToast.showToast(UserInfoActivity.this, "昵称不能为空", 2000);
			return false;
		}
		return true;
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
	 * 谢该信息确认狂
	 */
	protected void showTipDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				UserInfoActivity.this);
		builder.setTitle("提示");
		builder.setMessage("确认修改？");
		builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 修改用户信息
				changeUserInfo();
			}
		});
		builder.show();
	}
}
