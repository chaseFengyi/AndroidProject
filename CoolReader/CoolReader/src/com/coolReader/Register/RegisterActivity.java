package com.coolReader.Register;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coolReader.R;
import com.coolReader.Application.SysApplication;
import com.coolReader.Bean.UserInfoBean;
import com.coolReader.Login.LoginActivity;
import com.coolReader.Util.AddressUtil;
import com.coolReader.Util.BackTagUtils;
import com.coolReader.Util.DialogUtils;
import com.coolReader.Util.PublicAttributeUtil;
import com.coolReader.Util.ToastUtils;
import com.coolReader.dao.DBPerform;
import com.coolReader.request.JsonResolve;
import com.coolReader.request.TCPRequest;

/**
 * @author FengYi~
 * 
 */
public class RegisterActivity extends Activity implements OnClickListener,
		 OnCheckedChangeListener {
	private LinearLayout linearLayout;
	private ImageView back, logoImg;
	private EditText userName, userPass, userCheckPass, email;
	private RadioGroup sex;
	private Button ok, reset;
	private Dialog registerDialog = null;
	// 用户信息
	private UserInfoBean userInfoBean = null;
	// tip表示男或女
	private String tip = "";

	private final int OK = 1;
	private final int USER_ALREADY_EXIST = 2;
	private final int SERVER_ERROR = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_register);

		findView();
		setAdapt();
		onclick();
	}

	private void findView() {
		linearLayout = (LinearLayout) findViewById(R.id.registerLinear);
		back = (ImageView) findViewById(R.id.registerBack);
		logoImg = (ImageView) findViewById(R.id.registerLogo);
		userName = (EditText) findViewById(R.id.registerName);
		userPass = (EditText) findViewById(R.id.registerPass);
		userCheckPass = (EditText) findViewById(R.id.registerCheckPass);
		sex = (RadioGroup) findViewById(R.id.registerSex);
		email = (EditText) findViewById(R.id.registerEmail);
		ok = (Button) findViewById(R.id.registerOk);
		reset = (Button) findViewById(R.id.registerReset);
	}

	private void setAdapt() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int height = metrics.heightPixels;
		int width = metrics.widthPixels;

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		logoImg.measure(w, h);
		int subWidth = logoImg.getMeasuredWidth();

		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		params1.setMargins((width - subWidth) / 3, height / 20,
				(width - subWidth) / 3, 0);
		params1.width = width / 3;
		params1.height = width / 3;
		logoImg.setLayoutParams(params1);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, height / 20, 0, 0);
		linearLayout.setLayoutParams(params);
	}

	private void onclick() {
		back.setOnClickListener(this);
		sex.setOnCheckedChangeListener(this);
		ok.setOnClickListener(this);
		reset.setOnClickListener(this);
	}

	// 验证邮箱输入是否正确
	public boolean isEmail(String str) {
		Pattern regex = Pattern.compile(PublicAttributeUtil.EMAIL_EXPRESS);
		Matcher matcher = regex.matcher(str);
		return matcher.matches();
	}

	private boolean register() {
		boolean flag = false;
		if (userName.getText().toString().equals("")) {
			Toast.makeText(RegisterActivity.this, "账号不能为空", Toast.LENGTH_SHORT)
					.show();
			return flag;
		} else if (userPass.getText().toString().equals("")
				|| userCheckPass.getText().toString().equals("")) {
			Toast.makeText(RegisterActivity.this, "输入密码不能为空",
					Toast.LENGTH_SHORT).show();
			return flag;
		} else if (!userPass.getText().toString()
				.equals(userCheckPass.getText().toString())) {
			Toast.makeText(RegisterActivity.this, "两次输入的密码不匹配",
					Toast.LENGTH_SHORT).show();
			userCheckPass.setText("");
			userCheckPass.requestFocus();
			return flag;
		} else if (!isEmail(email.getText().toString())) {
			Toast.makeText(RegisterActivity.this, "邮箱格式输入错误，请重新输入",
					Toast.LENGTH_SHORT).show();
			return flag;
		} else {
			flag = true;
			return flag;
		}
	}

	// 获取输入的数据，准备上传服务器
	private UserInfoBean getDateFromLocal() {
		userInfoBean = new UserInfoBean();
		userInfoBean.setUserName(userName.getText().toString());
		userInfoBean.setUserPass(userPass.getText().toString());
		userInfoBean.setUserSex(tip);
		userInfoBean.setUserEmail(email.getText().toString());
		return userInfoBean;
	}

	/**
	 * 形成json格式的字符串
	 * 
	 * @return
	 */
	public String jsonForm() {
		UserInfoBean userInfoBean = getDateFromLocal();

		final JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject2 = new JSONObject();
		JSONObject jsonObject3 = new JSONObject();

		try {
			jsonObject.put("funType", 20);
			jsonObject3.put("userName", userInfoBean.getUserName());
			jsonObject3.put("password", userInfoBean.getUserPass());
			jsonObject3.put("mailAddr", userInfoBean.getUserEmail());
			jsonObject3.put("Sex", userInfoBean.getUserSex());
			jsonObject2.put("userInfo", jsonObject3);
			jsonObject.put("funContent", jsonObject2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject.toString();

	}

	// 上传数据
	private void uploadUserInfo() {

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String string  = jsonForm();
				System.out.println("json="+string);
				String receive = TCPRequest.tcpRequest(string, AddressUtil.TCT_ADDRESS, AddressUtil.TCP_PORT);
				String describe = JsonResolve.fun_des_resolve(receive);
				Message message = new Message();
				if(describe.equals(BackTagUtils.OK)){
					message.what = OK;
				}else if(describe.equals(BackTagUtils.SERVER_ERROR)){
					message.what = SERVER_ERROR;
				}else{
					message.what = USER_ALREADY_EXIST;
				}
				message.obj = describe;
				handler.sendMessage(message);
			}
		}).start();
		
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			registerDialog.cancel();
			switch(msg.what){
			case OK:
				//将注册的用户信息存放在本地数据库
				DBPerform.insertUserInfo(RegisterActivity.this, getDateFromLocal());
				List<UserInfoBean> list = DBPerform.queryUserInfo(RegisterActivity.this);
				System.out.println("userinfo="+list);
				ToastUtils.makeToast(RegisterActivity.this, "注册成功");
				startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
				finish();
				break;
			case SERVER_ERROR:
				ToastUtils.makeToast(RegisterActivity.this, "网络错误,请检查网路状况");
				break;
			case USER_ALREADY_EXIST:
				ToastUtils.makeToast(RegisterActivity.this, "该用户已存在，注册失败");
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.registerBack:
			startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
			finish();
			break;
		case R.id.registerOk:
			if (register()) {
				if (TCPRequest.isNetWorkEnable(RegisterActivity.this)) {
					registerDialog = DialogUtils.createProgressDialog(this,
							"CodingReader", "isRegister");
					registerDialog.show();
					uploadUserInfo();
				} else {
					ToastUtils.makeToast(RegisterActivity.this, "网络错误");
				}

			}
			break;
		case R.id.registerReset:
			userName.setText("");
			userPass.setText("");
			userCheckPass.setText("");
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		tip = checkedId == R.id.registerMale ? "男" : "女";
	}
}
