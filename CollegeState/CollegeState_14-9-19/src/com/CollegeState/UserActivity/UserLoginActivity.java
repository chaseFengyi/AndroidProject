package com.CollegeState.UserActivity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.JSONUtils;

/**
 * @author zc �û���¼��activity
 */
public class UserLoginActivity extends Activity {
	// ��¼�����û�������˺ź�����
	private EditText accountLoginEditText = null;
	private EditText passwordLoginEditText = null;
	// �Ƿ񱣴��û��˺���Ϣ�ĵ�ѡ��ť
	private CheckBox isSaveDataCheckBox = null;
	//�������밴ť
	private Button ForgetPasswdButton = null;
	// �û���¼�İ�ť
	private Button loginButton = null;
	// ע�ᰴť
	private Button userRegister = null;
	private ImageButton back;
	private TextView titleText;// back�ұ���ʾactivity���ܵ�text
	// �����¼��Ϣ��SharedPreference
	private SharedPreferences sharedPreference = null;
	private SharedPreferences.Editor editor = null;

	private String SHARED_PREF_NAME = "USER_LOGIN_INFO";
	private String SHARED_PREF_USER_ACCOUNT = "account";
	private String SHARED_PREF_USER_PASSWORD = "password";
	// �������ͨ����ز�����
	private String USER_PHONE = "userPhone";
	private String USER_PASSWD = "userPasswd";
	private String LOGIN_ERROR = "noUser_or_passwdError";

	// ��ʼ��¼ʱ�Ľ�����
	private ProgressDialog loginDialog = null;
	// �����¼��Handler
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				// �û��������
				loginDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserLoginActivity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("�û������������")
						.setPositiveButton("����������",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// Ҫ���û�������������
										dialog.dismiss();
										accountLoginEditText.requestFocus();
									}
								});
				builder.create().show();
			} else if (msg.what == 0x345) {
				// ��¼�ɹ��������û���Ϣ�ĵ�������������ת����ʾ��¼�ɹ�״̬
				loginDialog.dismiss();
				String jsonString = msg.obj.toString();
				// �жϱ����û���Ϣ�Ƿ�ɹ�
				JSONUtils.setUserInfoAfterLogin(jsonString);
				// ��ǵ�¼�ɹ�״̬
				UserInfoBean.setLogin(true);
				Toast.makeText(UserLoginActivity.this, "��½�ɹ�",
						Toast.LENGTH_SHORT).show();
				finish();
			}// �������
			else if (msg.what == 0x456) {
				loginDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserLoginActivity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("�������")
						.setNegativeButton("����",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								})
						.setPositiveButton("ȥ��������״̬",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// ��ת��ϵͳ��activityȥ��������
										dialog.dismiss();
										Intent intent = new Intent();
										intent.setAction(android.provider.Settings.ACTION_SETTINGS);
										startActivity(intent);
									}

								});
				builder.create().show();
			}// ����������
			if (msg.what == 0x999) {
				loginDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserLoginActivity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("ϵͳ�����������Ժ�����")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder.create().show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// activity����
		overridePendingTransition(R.anim.anim_in_from_right,
				R.anim.anim_out_to_left);
		accountLoginEditText = (EditText) findViewById(R.id.EditTextUserLoginAccount);
		passwordLoginEditText = (EditText) findViewById(R.id.EditTextUserLoginPassword);
		isSaveDataCheckBox = (CheckBox) findViewById(R.id.CheckBoxIsSavePasswd);
		ForgetPasswdButton = (Button)findViewById(R.id.ButtonForgetPasswd);
		// ��ס�����Զ���ѡ
		isSaveDataCheckBox.setChecked(true);
		loginButton = (Button) findViewById(R.id.ButtonUserLoginConfirme);
		userRegister = (Button) findViewById(R.id.ButtonUserRegister);
		initActionbar();// �Զ��������
		// ��һ���ж��Ƿ���ע��֮����Intent
		Intent receiveIntent = getIntent();
		if (receiveIntent.getStringExtra(UserRegisterActivity.USER_ACCOUNT) != null) {
			if (receiveIntent
					.getStringExtra(UserRegisterActivity.USER_PASSWORD) != null) {
				accountLoginEditText.setText(receiveIntent
						.getStringExtra(UserRegisterActivity.USER_ACCOUNT));
				passwordLoginEditText.setText(receiveIntent
						.getStringExtra(UserRegisterActivity.USER_PASSWORD));
			}
		} else {
			// ��ʾ�������Ϣ
			getSavedData();
		}
		//�������밴ť����
		ForgetPasswdButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("http://121.42.8.50/CS/customer/Customer_resetpasswd_input.jsp");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		// ��¼��ť�¼�����
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String userAccount = accountLoginEditText.getText()
						.toString();
				final String userPassword = passwordLoginEditText.getText()
						.toString();
				if (userAccount.equals("")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserLoginActivity.this)
							.setTitle("��ܰ��ʾ!")
							.setMessage("�����������˺�!")
							.setPositiveButton("��������",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// �˺�������ȡ����
											dialog.dismiss();
											accountLoginEditText.requestFocus();
										}
									});
					builder.create().show();
					return;
				}
				if (userPassword.equals("")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserLoginActivity.this)
							.setTitle("��ܰ��ʾ!")
							.setMessage("��������������!")
							.setPositiveButton("��������",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// ����������ȡ����
											dialog.dismiss();
											passwordLoginEditText
													.requestFocus();
										}
									});
					builder.create().show();
					return;
				}
				// �ж�����״̬
				if (!HttpUtils.isNetWorkEnable(UserLoginActivity.this)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserLoginActivity.this)
							.setTitle("��ܰ��ʾ")
							.setMessage("�������")
							.setNegativeButton("����",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									})
							.setPositiveButton("ȥ��������״̬",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// ��ת��ϵͳ��activityȥ��������
											dialog.dismiss();
											Intent intent = new Intent();
											intent.setAction(android.provider.Settings.ACTION_SETTINGS);
											startActivity(intent);
										}

									});
					builder.create().show();
				}
				// ��ʼ��¼(����������)
				loginDialog = new ProgressDialog(UserLoginActivity.this);
				loginDialog.setTitle("��ܰ��ʾ");
				loginDialog.setMessage("���ڵ�¼");
				loginDialog.setCancelable(true);
				loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				loginDialog.show();
				new Thread() {
					public void run() {
						try {
							// ��������

							ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair(USER_PHONE,
									userAccount));
							params.add(new BasicNameValuePair(USER_PASSWD,
									userPassword));

							String result = HttpUtils.queryStringForPost(
									HttpUtils.USER_LOGIN_URL, params);
							// �û������������
							if (result.equals(LOGIN_ERROR)) {
								handler.sendEmptyMessage(0x123);
							}
							// �������
							else if (result.equals(HttpUtils.NETWORK_ERROR)) {
								handler.sendEmptyMessage(0x456);
							} else if (TextUtils.isEmpty(result)) {
								handler.sendEmptyMessage(0x999);
							}
							// ��¼�ɹ�����user����
							else {
								Message msg = new Message();
								msg.what = 0x345;
								// ʹ��obj����װ���JSONString
								msg.obj = result;
								handler.sendMessage(msg);
								saveUserLoginData();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();
			}

		});
		// ע�ᰴť�¼�����
		userRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(UserLoginActivity.this,
						UserRegisterActivity.class);
				finish();
				UserLoginActivity.this.startActivity(intent);
			}
		});
	}

	/**
	 * ��activity����ʱ�Զ���SharedPreference�ж�ȡ�û���Ϣ
	 */
	private void getSavedData() {
		sharedPreference = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
		// ���ֻ��л�ȡSharedPreference����ȡʧ��ʱ����""�ַ���,����ʾ�ڿؼ���
		String userAccount = sharedPreference.getString(
				SHARED_PREF_USER_ACCOUNT, "");
		String userPassword = sharedPreference.getString(
				SHARED_PREF_USER_PASSWORD, "");
		// ��ȡ������
		accountLoginEditText.setText(userAccount);
		passwordLoginEditText.setText(userPassword);
	}

	/**
	 * ����¼�ɹ�ʱʹ��SharedPreference��ȡ�û���Ϣ
	 * 
	 * @return
	 */
	private boolean saveUserLoginData() {
		// ���û�ѡ���Զ�����ʱ����
		if (!shouldAutoSaveData()) {
			sharedPreference = getSharedPreferences(SHARED_PREF_NAME,
					Context.MODE_PRIVATE);
			editor = sharedPreference.edit();
			editor.clear();
			editor.commit();
			return false;
		} else {
			String userAccount = accountLoginEditText.getText().toString();
			String password = passwordLoginEditText.getText().toString();
			sharedPreference = getSharedPreferences(SHARED_PREF_NAME,
					Context.MODE_PRIVATE);
			editor = sharedPreference.edit();
			editor.putString(SHARED_PREF_USER_ACCOUNT, userAccount);
			editor.putString(SHARED_PREF_USER_PASSWORD, password);
			editor.commit();
			return true;
		}
	}

	/**
	 * �����ж��û��Ƿ���Ҫ�����¼��Ϣ
	 * 
	 * @return
	 */
	private boolean shouldAutoSaveData() {
		if (isSaveDataCheckBox.isChecked()) {
			return true;
		}
		return false;
	}

	/**
	 * ����ӿ������Ժ��ٲ˵�������¹��ܣ��޸���Ϣ���һ������
	 * 
	 * @param menu
	 * @return
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	private void initActionbar() {
		// �Զ��������
		getActionBar().setDisplayShowHomeEnabled(false); // ʹ���Ͻ�ͼ���Ƿ���ʾ��������false����û�г���ͼ�꣬�����͸����⣬������ʾӦ�ó���ͼ��
		getActionBar().setDisplayShowTitleEnabled(false); // ��ӦActionBar.DISPLAY_SHOW_TITLE��
		getActionBar().setDisplayShowCustomEnabled(true);// ʹ�Զ������ͨView����title����ʾ
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mTitleView = mInflater.inflate(
				R.layout.custom_action_bar_onlyback, null);
		getActionBar().setCustomView(
				mTitleView,
				new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		titleText = (TextView) findViewById(R.id.title_name);
		titleText.setText("��½");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
