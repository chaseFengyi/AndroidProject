package com.mynutritionstreet.register;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mynutritionstreet.R;
import com.mynutritionstreet.json.JsonResolve;
import com.mynutritionstreet.login.LoginActivity;
import com.mynutritionstreet.mainpage.MainPageActivity;
import com.mynutritionstreet.mixinfo.SysApplication;
import com.mynutritionstreet.myinfo.MyInfoActivity;
import com.mynutritionstreet.util.BackTagUtils;
import com.mynutritionstreet.util.DialogUtils;
import com.mynutritionstreet.util.HttpUtil;
import com.mynutritionstreet.util.ToastUtil;
import com.mynutritionstreet.util.UrlUtils;
import com.mynutritionstreet.util.VolleyUtil;

public class RegisterActivity extends Activity implements OnClickListener,OnCheckedChangeListener {
	private LinearLayout linearLayout;
	private ImageView back;
	private EditText userName, userPass, userCheckPass, userAge;
	private RadioGroup sex;
	private Button ok, reset;
	// tip表示男或女
	private String tip = "";
	static Dialog dialog = null;
	
	final int USER_NAME_HAS_EXIST = 1;
	final int SUCCESS = 2;
	final int FALSE = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		SysApplication.getInstance().addActivity(this);

		findView();
		setAdapt();
		onclick();
	}

	private void findView() {
		linearLayout = (LinearLayout) findViewById(R.id.registerLinear);
		back = (ImageView) findViewById(R.id.registerBack);
		userName = (EditText) findViewById(R.id.registerName);
		userPass = (EditText) findViewById(R.id.registerPass);
		userCheckPass = (EditText) findViewById(R.id.registerCheckPass);
		sex = (RadioGroup) findViewById(R.id.registerSex);
		userAge = (EditText) findViewById(R.id.registerAge);
		ok = (Button) findViewById(R.id.registerOK);
		reset = (Button) findViewById(R.id.registerReset);
	}

	private void setAdapt() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int height = metrics.heightPixels;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, height / 6, 0, 0);
		linearLayout.setLayoutParams(params);
	}

	private void onclick() {
		back.setOnClickListener(this);
		sex.setOnCheckedChangeListener(this);
		ok.setOnClickListener(this);
		reset.setOnClickListener(this);
	}

	private boolean register() {
		boolean flag = false;
		if (userName.getText().toString().equals("")) {
			Toast.makeText(RegisterActivity.this, "账号不能为空", Toast.LENGTH_SHORT)
					.show();
			return flag;
		} else if (userPass.getText().toString().equals("") || userCheckPass.getText().toString().equals("")) {
			Toast.makeText(RegisterActivity.this, "输入密码不能为空",
					Toast.LENGTH_SHORT).show();
			return flag;
		} else if (!userPass.getText().toString().equals(userCheckPass.getText().toString())) {
			Toast.makeText(RegisterActivity.this, "两次输入的密码不匹配",
					Toast.LENGTH_SHORT).show();
			return flag;
		} else {
			flag = true;
			return flag;
		}
	}

	/**
	 * 向服务器上传注册信息
	 */
	public void uploadRegistInfo(){
		String str_count = userName.getText().toString().trim();
		String str_pass = userPass.getText().toString().trim();
		String str_sex = tip;
		String age = userAge.getText().toString().trim();
		
		dialog = DialogUtils.createProgressDialog(RegisterActivity.this,"注册","注册中，请稍后....");
		dialog.show();
		
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("userName", str_count);
		rawParams.put("password", str_pass);
		rawParams.put("sex", str_sex);
		rawParams.put("age", age);
		Log.i("regist-url=", UrlUtils.USER_REGISTER);
		HttpUtil registerRequest = new HttpUtil(UrlUtils.USER_REGISTER, rawParams,
				new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d("TAG", response);
				ToastUtil.showToast(RegisterActivity.this, "注册成功", 2000);
				// 验证登陆结果
				checkReturnResult(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				byte[] htmlBodyBytes = error.networkResponse.data;
				Log.e("LOGIN-ERROR", new String(htmlBodyBytes), error);
				ToastUtil.showToast(RegisterActivity.this, "注册失败，网络错误", 2000);
				// 登录失败，网络错误
			}
		});
		
		VolleyUtil.getRequestQueue().add(registerRequest);
	}
	
	/**
	 * 查看返回的结果
	 * @param response
	 */
	public void checkReturnResult(String response){
		dialog.dismiss();
		String result = JsonResolve.get_result(response);
		if(result.equals(BackTagUtils.USER_NAME_HAS_EXIST)){
			ToastUtil.showToast(RegisterActivity.this, "该用户名已存在，不能注册", 2000);
		}else if(result.equals(BackTagUtils.SUCCESS)){
			ToastUtil.showToast(RegisterActivity.this, "注册成功", 2000);
			startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
			finish();
		}else{
			ToastUtil.showToast(RegisterActivity.this, "注册失败", 2000);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.registerBack:
			startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
			finish();
			break;
		case R.id.registerOK:
			if (register()) {
//				Toast.makeText(RegisterActivity.this, "注册成功，返回登陆！",
//						Toast.LENGTH_SHORT).show();
//				startActivity(new Intent(RegisterActivity.this,
//						LoginActivity.class));
//				finish();
				uploadRegistInfo();
			}
			break;
		case R.id.registerReset:
			userName.setText("");
			userPass.setText("");
			userCheckPass.setText("");
			userAge.setText("");
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {
			startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }
        return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		tip = checkedId == R.id.male ? "男" : "女";
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		VolleyUtil.getRequestQueue().cancelAll(this);
	}

}
