package com.example.sendlist;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.publicInfo.BackInfo;
import com.request.HttpObtain;
import com.request.JsonResolve;

public class Login extends Activity {
	private AutoCompleteTextView userEdit;
	private EditText passEdit;
	private Button enter;
	private Button registe;
	private CheckBox checkBox;

	public static String WORKERNUM = "";
	public static String ACTION = "";
	public static String USERPHONE = "";
	public static String USERPASSWD = "";
	private static String user;
	private static String pass;
	public static String url = "http://121.42.8.50/CS1/customer/User_login";
	private static boolean isChecked = true;
	private SharedPreferences sp;
	private static List<UserInfo> list1 = new ArrayList<UserInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		onClick();
		save();
		click();
	}

	private void onClick() {
		userEdit = (AutoCompleteTextView) findViewById(R.id.accountEdit);
		passEdit = (EditText) findViewById(R.id.passEdit);
		enter = (Button) findViewById(R.id.enter);
		registe = (Button) findViewById(R.id.registe);
		checkBox = (CheckBox) findViewById(R.id.checkBox);
	}

	// ��������û������뱣�����ֻ���
	private void save() {
		sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE);
		userEdit.setThreshold(1);// ����1����ĸ�Ϳ�ʼ�Զ���ʾ
		passEdit.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		// ��������ΪInputType.TYPE_TEXT_VARIATION_PASSWORD��Ҳ����0x81
		// ��ʾ����ΪInputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD��Ҳ����0x91
		userEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				String[] allUserName = new String[sp.getAll().size()];// sp.getAll().size()���ص����ж��ٸ���ֵ��
				allUserName = sp.getAll().keySet().toArray(new String[0]);
				// sp.getAll()����һ��hash map
				// keySet()�õ�����a set of the keys.
				// hash map����key-value��ɵ�

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						Login.this,
						android.R.layout.simple_dropdown_item_1line,
						allUserName);

				userEdit.setAdapter(adapter);// ��������������
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				passEdit.setText(sp
						.getString(userEdit.getText().toString(), ""));// �Զ���������
			}
		});
	}

	// �������
	private void click() {
		enter.setOnClickListener(new EnterListener());
		registe.setOnClickListener(new RegisteListener());
		checkBox.setOnCheckedChangeListener(new CheckBoxListener());
	}

	private class CheckBoxListener implements
			android.widget.CompoundButton.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if (arg1) {// ѡ��
				isChecked = true;
			} else {
				isChecked = false;
			}
		}

	}

	// �ж�username��password�Ƿ����Ѿ�ע�����Ϣ�Ǻ�
	private int judgeInfo(String usered, String passed, String user, String pass) {
		if (usered.equals(user)) {
			if (passed.equals(pass)) {
				return 0;
			}
			return 1;
		}
		return -1;
	}

	public class MyRunnable extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userPhone", user));
			params.add(new BasicNameValuePair("userPasswd", pass));
			String string = HttpObtain.getStringFromHttp(url, params);
			Message msg = handler.obtainMessage();
			if (string.equals(BackInfo.RESPONSE_NOUSER_OR_PASSWDERROR)) {
				msg.what = 0x02;
			} else {
//				System.out.println("STRING:" + string);
				msg.obj = JsonResolve.getLoginInfo(string);
//				System.out.println("OBJ:" + JsonResolve.getLoginInfo(string));
				msg.what = 0x01;
			}
			handler.sendMessage(msg);
		}

	}

	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x01:
				list1 = (List<UserInfo>) msg.obj;
//				System.out.println("LIST:" + list1.size());
				for (int i = 0; i < list1.size(); i++) {
					if (user.equals(list1.get(i).getUserPhone())) {
						USERPHONE = list1.get(i).getUserPhone();
						USERPASSWD = list1.get(i).getUserPasswd();
						ACTION = list1.get(i).getAction();
//						System.out.println("ACTION:" + ACTION);
						WORKERNUM = list1.get(i).getWorkerNum();
					}
				}

				String usered = USERPHONE;
				String passed = USERPASSWD;
				if (judgeInfo(usered, passed, user, pass) == 0) {
					// ��½�ɹ�
					if (!isChecked) {
						passEdit.setText("");
					}
					sp.edit().putString(usered, passed).commit();
					Intent intent = new Intent();
					intent.setClass(Login.this, Logined.class);
					startActivity(intent);
				} else if (judgeInfo(usered, passed, user, pass) == -1) {
					// ����û�������
					Toast.makeText(Login.this, "�Բ����û������������µ�¼",
							Toast.LENGTH_SHORT).show();
					userEdit.setText("");
					passEdit.setText("");
				} else {
					// �������
					Toast.makeText(Login.this, "�Բ���������������µ�¼",
							Toast.LENGTH_SHORT).show();
					passEdit.setText("");
				}
				break;

			case 0x02:
				Toast.makeText(Login.this, "û�û����������", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}

	};

	// ���û������¼��ť���������¼�
	class EnterListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			user = userEdit.getText().toString();
			pass = passEdit.getText().toString();

			// �ж����������Ƿ�ɹ�
			if (HttpObtain.isNetWorkEnable(Login.this)) {
				if (user.equals("") || pass.equals("")) {
					Toast.makeText(Login.this, "�û��������벻��Ϊ��", Toast.LENGTH_SHORT)
							.show();
				}
				new MyRunnable().start();
			} else {
				Toast.makeText(Login.this, "�Բ���������δ���ӣ���������",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	// ���û����ע�ᰴť
	class RegisteListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(Login.this, Register.class);
			startActivity(intent);
			finish();
		}

	}

}
