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
 * ��¼
 * 
 * @author Xuptljw 2015��7��22��15:07:58
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
		// �õ�ҳ�沼�ֿؼ�
		findViews();
		initLoginInfo();
//		exit();
	}
	
	/**
	 * ���û����ע�������ص���¼����
	 */
	public void exit(){
		Intent intent = getIntent();
		if(intent == null)
			return;
		String set = intent.getStringExtra("setting");
		Log.i("login-set", set);
		if(set.equals("setting")){//ͨ��ע�������½����
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
		// �õ�ҳ��Ŀռ�
		loginButton = (Button) this.findViewById(R.id.login_button);
		registerButton = (Button) this.findViewById(R.id.register_button);
		uname = (EditText) this.findViewById(R.id.uname);
		upass = (EditText) this.findViewById(R.id.upass);
		// �Ƿ��ס����
		mCheckBoxIsSavePassword = (CheckBox) findViewById(R.id.ck_activity_login_is_save_password);
		// �Ƿ��Զ���¼
		mCheckBoxAutoLogin = (CheckBox) findViewById(R.id.ck_activity_login_is_auto_login);

		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
	}
	

	/**
	 * ��ʼ���û���¼��Ϣ
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
			// ��¼
			login();
		}
	}


	/**
	 * ��������
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

	
	// У���¼
	public boolean validate() {
		String name = uname.getText().toString();
		String pass = upass.getText().toString();
		if (TextUtils.isEmpty(name)) {
			ToastUtils.makeToast(this, "�û�������Ϊ��");
			return false;
		}
		if (TextUtils.isEmpty(pass)) {
			ToastUtils.makeToast(this, "���벻��Ϊ��");
			return false;
		}
		return true;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// ��ȡ��������Ӧ��Ϣ��رյȴ��˿�
			loginDialog.cancel();
			switch (msg.what) {
			case SERVER_ERROR:
				ToastUtils.makeToast(LoginActivity.this, "�������");
				break;
			case USER_NOT_EXIST:
				ToastUtils.makeToast(LoginActivity.this, "�û�������");
				break;
			case OK:
				ToastUtils.makeToast(LoginActivity.this, "��¼�ɹ�");
				// ��½�ɹ��������û���Ϣ
				// �Ƿ񱣴�����
				if (mCheckBoxIsSavePassword.isChecked()) {
					savePassword(uname.getText().toString(),
							upass.getText().toString(), true,
							mCheckBoxAutoLogin.isChecked());
				} else {
					savePassword(uname.getText().toString(), "",
							false, mCheckBoxAutoLogin.isChecked());
				}
				// ����־�в鿴�õ�����Ϣ
				Log.i("success", "��¼�ɹ�" + (String) msg.obj);
				Intent intent = new Intent(LoginActivity.this,
						MainPageActivity.class);
				startActivity(intent);
				finish();
				break;
			case WRONG_PASSWD:
				ToastUtils.makeToast(LoginActivity.this, "�������");
				break;
			default:
				break;
			}
		}
	};

	/**
	 * ��½
	 */
	private void login(){
		// �����Ի�����ʾ���ڵ�½
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
			// ֱ����ת��ע��ҳ��
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
