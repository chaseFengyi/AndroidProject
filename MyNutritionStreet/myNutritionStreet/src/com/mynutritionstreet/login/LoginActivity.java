package com.mynutritionstreet.login;

import java.nio.MappedByteBuffer;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mynutritionstreet.R;
import com.mynutritionstreet.application.MyApplication;
import com.mynutritionstreet.json.JsonResolve;
import com.mynutritionstreet.mainpage.MainPageActivity;
import com.mynutritionstreet.mixinfo.SysApplication;
import com.mynutritionstreet.pictureutil.ImageUtile;
import com.mynutritionstreet.register.RegisterActivity;
import com.mynutritionstreet.util.BackTagUtils;
import com.mynutritionstreet.util.DialogUtils;
import com.mynutritionstreet.util.HttpUtil;
import com.mynutritionstreet.util.ToastUtil;
import com.mynutritionstreet.util.UrlUtils;
import com.mynutritionstreet.util.VolleyUtil;

public class LoginActivity extends Activity implements OnClickListener {
	private ImageView imageView;
	private EditText loginName, loginPass; 
	private Button btnLogin, btnRegister;
	DrawerLayout layout;
	Dialog dialog = null;
	MyApplication myApplication = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SysApplication.getInstance().addActivity(this); 
		
		myApplication = (MyApplication)getApplication();
		
		findView();
		setLoginPhoto();
		setAdapter();
		onclick();
	}

	private void findView() {
		imageView = (ImageView) findViewById(R.id.loginPhoto);
		loginName = (EditText) findViewById(R.id.loginName);
		loginPass = (EditText) findViewById(R.id.loginPass);
		btnLogin = (Button) findViewById(R.id.login);
		btnRegister = (Button) findViewById(R.id.register);
	}
	
	private void setAdapter(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		int height = metrics.heightPixels;
		
		int w = View.MeasureSpec.makeMeasureSpec(0,  
                View.MeasureSpec.UNSPECIFIED);  
        int h = View.MeasureSpec.makeMeasureSpec(0,  
                View.MeasureSpec.UNSPECIFIED);  
		imageView.measure(w, h);
		imageView.setPadding(0, height/10, 0, 30);
	}
	
	/*//设置图片
	private void setLoginPhoto(){
		//获取壁纸返回值是Drawable
		Drawable drawable = getWallpaper();
		//将Drawable转化为Bitmap
		Bitmap bitmap = ImageUtile.drawableToBitmap(drawable);
		//缩放图片
		Bitmap zoomBitmap = ImageUtile.zoomBitmap(bitmap, 200, 150);
		//获取圆角图片
		Bitmap roundBitmap = ImageUtile.getRoundedCornerBitmap(zoomBitmap, 10.0f);
		imageView.setImageBitmap(roundBitmap);
	}*/
	//设置图片
		private void setLoginPhoto(){
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loginpicture);
			//缩放图片
			Bitmap zoomBitmap = ImageUtile.zoomBitmap(bitmap, 230, 180);
			//获取圆角图片
			Bitmap roundBitmap = ImageUtile.getRoundedCornerBitmap(zoomBitmap, 15.0f);
			imageView.setImageBitmap(roundBitmap);
		}
	
		private void onclick(){
			btnLogin.setOnClickListener(this);
			btnRegister.setOnClickListener(this);
		}
		
	
	/**
	 * 用户请求服务器登陆
	 */
	private void login(){
		dialog = DialogUtils.createProgressDialog(LoginActivity.this, "登陆", "正在登陆....");
		dialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("userName", loginName.getText().toString().trim());
		rawParams.put("password", loginPass.getText().toString().trim());
		System.out.println("params="+rawParams);
		System.out.println("ur"+UrlUtils.USER_LOGIN);
		HttpUtil loginRequest = new HttpUtil(UrlUtils.USER_LOGIN, rawParams, 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						checkLoginResult(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.e("TAG", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.e("LOGIN-ERROR", new String(htmlBodyBytes), error);
						ToastUtil.showToast(LoginActivity.this, "网络错误", 2000);
					}
		});
		VolleyUtil.getRequestQueue().add(loginRequest);
	}
	
	/**
	 * 检查用户登陆结果
	 * @param response
	 */
	public void checkLoginResult(String response){
		dialog.dismiss();
		String result = JsonResolve.get_result(response);
		Log.i("login-result", result);
		if(result.equals(BackTagUtils.USERNAME_OR_PASSWORD_ER)){
			ToastUtil.showToast(LoginActivity.this, "用户名或者密码错误", 2000);
		}else{
			myApplication.setUserInfoBean(JsonResolve.json_userinfo_result_login(result));
			myApplication.setGoodInfoBeans(JsonResolve.json_goodsinfo_result_login(result));
			ToastUtil.showToast(LoginActivity.this, "登陆成功", 2000);
			startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
			finish();
		}
	}
	
	/**
	 * 检测用户输入的登录信息是否完整
	 * @return
	 */
	public boolean checkLoginInfo(){
		String uName = loginName.getText().toString().trim();
		String uPasss = loginPass.getText().toString().trim();
		if(uName.equals("")){
			ToastUtil.showToast(LoginActivity.this, "账号不能为空", 2000);
			loginName.requestFocus();
			return false;
		}else if(uPasss.equals("")){
			ToastUtil.showToast(LoginActivity.this,"密码不能为空", 2000);
			loginPass.requestFocus();
			return false;
		}
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.login:
			String uName = loginName.getText().toString();
			String uPass = loginPass.getText().toString();
			if(checkLoginInfo()){
				login();
			}
//			startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
			break;
		case R.id.register:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {
			System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		VolleyUtil.getRequestQueue().cancelAll(this);
	}

}
