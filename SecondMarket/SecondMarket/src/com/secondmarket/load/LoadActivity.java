package com.secondmarket.load;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import com.Request.HTTPRequest;
import com.Request.JSONResolve;
import com.publicInfo.Address;
import com.publicInfo.BackInfo;
import com.secondmarket.DB.DBCreateWord;
import com.secondmarket.DB.DBPerform;
import com.secondmarket.bean.UserInfoDemo;
import com.secondmarket.information.MainPageActivity;

public class LoadActivity extends Activity {
	private AutoCompleteTextView userEdit;
	private EditText passEdit;
	private Button enter;
	private CheckBox checkBox;

	private static boolean isChecked = true;
	private SharedPreferences sp;
	public static List<UserInfoDemo> list1 = new ArrayList<UserInfoDemo>();
	private static String user;
	private static String pass;
	public static String USERSNUMBER = "";
	public static String USERPASSWD = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);

		findView();
		save();
		getListener();
	}

	private void findView() {
		userEdit = (AutoCompleteTextView) findViewById(R.id.accountEdit);
		passEdit = (EditText) findViewById(R.id.passEdit);
		enter = (Button) findViewById(R.id.enter);
		checkBox = (CheckBox) findViewById(R.id.checkBox);
	}

	// ��������û������뱣�����ֻ���
	private void save() {
		sp = this.getSharedPreferences("SecondMarketFile", MODE_PRIVATE);
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
						LoadActivity.this,
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

	// ������
	private void getListener() {
		enter.setOnClickListener(new EnterListener());
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

	// ��½����
	class EnterListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			user = userEdit.getText().toString();
			pass = passEdit.getText().toString();
			if (HTTPRequest.isNetWorkEnable(LoadActivity.this)) {
				if (user.equals("") || pass.equals("")) {
					Toast.makeText(LoadActivity.this, "�û��������벻��Ϊ��",
							Toast.LENGTH_SHORT).show();
				} else {
					new MyRunnable().start();
				}
			} else {
				Toast.makeText(LoadActivity.this, "�Բ���������δ���ӣ���������",
						Toast.LENGTH_SHORT).show();
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
			params.add(new BasicNameValuePair("userNum", user));
			params.add(new BasicNameValuePair("userPassword", pass));
			System.out.println("pass=" + pass);
			System.out.println("user=" + user);
			String string = HTTPRequest.getStringFromHttpOfLoad(
					Address.LOAD_ADDRESS, params);
			Message msg = handler.obtainMessage();
			System.out.println("stringstring--->" + string);
			if (string.equals(BackInfo.RESPONSE_USER_NOT_EXIST)) {
				msg.what = 0x02;
			} else if (string.equals(BackInfo.RESPONSE_PASSWORD_ERROR)) {
				msg.what = 0x03;
			} else {
				msg.obj = JSONResolve.getLoadInfoAfterJson(string);
				System.out.println("OBJ:"
						+ JSONResolve.getLoadInfoAfterJson(string));
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
				list1 = (List<UserInfoDemo>) msg.obj;
				System.out.println("list.size()=" + list1.size());
				for (int i = 0; i < list1.size(); i++) {
					if (user.equals(list1.get(i).getUserSchoolNum())) {
						USERSNUMBER = list1.get(i).getUserSchoolNum();
						USERPASSWD = list1.get(i).getUserPassword();
					}
				}
				String usered = USERSNUMBER;
				String passed = USERPASSWD;
				if (judgeInfo(usered, passed, user, pass) == 0) {
					// ��½�ɹ�
					DBPerform.createUserTable(LoadActivity.this);
					DBPerform.insertUserQuery(LoadActivity.this, list1.get(0), DBCreateWord.TB_USER);
					if (!isChecked) {
						passEdit.setText("");
					}
					sp.edit().putString(usered, passed).commit();
					Intent intent = new Intent();
					intent.setClass(LoadActivity.this, MainPageActivity.class);
					startActivity(intent);
				} else if (judgeInfo(usered, passed, user, pass) == -1) {
					// ����û�������
					Toast.makeText(LoadActivity.this, "�Բ����û������������µ�¼",
							Toast.LENGTH_SHORT).show();
					userEdit.setText("");
					passEdit.setText("");
				} else {
					// �������
					Toast.makeText(LoadActivity.this, "�Բ���������������µ�¼",
							Toast.LENGTH_SHORT).show();
					passEdit.setText("");
				}
				Toast.makeText(LoadActivity.this, "��½�ɹ�", Toast.LENGTH_SHORT)
						.show();
				break;

			case 0x02:
				Toast.makeText(LoadActivity.this, "û�û����������",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}

	};

}
