package com.CollegeState.UserActivity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.MD5StringUtil;

/**
 * complete!!!
 * 
 * @author zc 2014 - 5 - 7 0:28
 */
public class UserRegisterActivity extends Activity {
	// �û�ע����水��
	private Button registerResetButton = null;
	private Button registerConfirmeButton = null;
	private ImageButton back;// ���ذ�ť
	private TextView titleText;// back�ұ���ʾactivity���ܵ�text
	// �û�ע�������ؼ�
	private EditText registerPasswordEditText = null;
	private EditText registerPhoneEditText = null;
	private EditText registerMessageCheckEditText = null;
	private Button registerGetMessageButton = null;
	private CheckBox registerCheckPasswdCheckBox = null;
	// �û�ע��Ի���
	private ProgressDialog registerDialog = null;
	// ע��֮������Intent���ݵ�����
	public static String USER_ACCOUNT = "account";
	public static String USER_PASSWORD = "password";
	// ��֤��Ϣ��������ʽ
	public static String regExPhoneNumber = "^(((13[0-9])|(15([0-3]|[5-9]))|(18[0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
	public static String regExMail = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	// �������ͨ�Ų���
	private String USER_PHONE = "userPhone";
	private String USER_PASSWD = "userPasswd";
	private String ACTION = "action";
	private String RESPONSE_REGISTER_OK = "register_ok";// ��ʾ�û��ɹ�ע��
	private String RESPONSE_REGISTER_REPEAT = "register_repeat";// �ֻ������Ѿ���ע��
	private String RESPONSE_DATABASE_ERROR = "database_error";// ���ݿ��������
	private String RESPONSE_ACTION_ERROR = "action_error";// У�����

	private TimeCount time;
	// ������֤��
	private int code = 159357;
	private String reCheckPhoneNum;
	// �û���ȡBundle������
	final static String USER_INFO_BUNDLE = "user_info";
	// �û�ע��ɹ����첽����Handler
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			// ��ʾע��ɹ���Register_Ok��
			if (msg.what == 0x123) {
				registerDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserRegisterActivity.this)
						.setTitle("��ϲ")
						.setMessage("ע��ɹ�")
						.setNegativeButton("����",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								})
						.setPositiveButton("����ȥ��¼",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent();
										intent.putExtra(USER_ACCOUNT,
												registerPhoneEditText.getText()
														.toString());
										intent.putExtra(USER_PASSWORD,
												registerPasswordEditText
														.getText().toString());
										intent.setClass(
												UserRegisterActivity.this,
												UserLoginActivity.class);
										startActivity(intent);
										finish();

									}
								});
				builder.create().show();
			}
			// ��ʾע��ʧ�ܣ�User_Exist��
			if (msg.what == 0x234) {
				registerDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserRegisterActivity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("���ֻ������Ѵ���")
						.setPositiveButton("���������û���",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										registerPhoneEditText.requestFocus();
										registerPasswordEditText.setText("");
									}
								});
				builder.create().show();
			}
			// ���ݿ��������
			if (msg.what == 0x235) {
				registerDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserRegisterActivity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("���ݿ��������")
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
			// У�����
			if (msg.what == 0x236) {
				registerDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserRegisterActivity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("У�����")
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
			// �������
			if (msg.what == 0x345) {
				registerDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserRegisterActivity.this)
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
			}
			// ����������
			if (msg.what == 0x999) {
				registerDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserRegisterActivity.this)
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
			if (msg.what == 0x456) {
				registerGetMessageButton.setTextColor(Color.GRAY);
				Toast.makeText(getApplicationContext(), "��ȡ������֤�ɹ�",
						Toast.LENGTH_SHORT).show();
			}
			if (msg.what == 0x567) {
				registerGetMessageButton.setClickable(false);
				registerGetMessageButton.setText("�ѷ���");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_register);
		// activity����
		overridePendingTransition(R.anim.anim_in_from_right,
				R.anim.anim_out_to_left);
		initActionbar();// �Զ��������
		registerResetButton = (Button) findViewById(R.id.ButtonUserRegisterReset);
		registerConfirmeButton = (Button) findViewById(R.id.ButtonUserRegisterConfirme);
		registerPhoneEditText = (EditText) findViewById(R.id.EditTextUserRegisterPhone);
		registerGetMessageButton = (Button) findViewById(R.id.UserRegisterButtonGetMessage);
		registerMessageCheckEditText = (EditText) findViewById(R.id.EditTextUserRegisterMessageCheck);
		registerCheckPasswdCheckBox = (CheckBox) findViewById(R.id.CheckBoxUserCheckPasswd);
		registerPasswordEditText = (EditText) findViewById(R.id.EditTextUserRegisterPassword);
		// �ָ��ֳ�
		if (savedInstanceState != null) {
			Bundle map = savedInstanceState.getBundle(USER_INFO_BUNDLE);
			registerPasswordEditText.setText(map.getShort("pass"));
			registerPhoneEditText.setText(map.getShort("phone"));
		}
		// ��ʾ����
		registerCheckPasswdCheckBox
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (registerCheckPasswdCheckBox.isChecked()) {
							/* �趨EditText������Ϊ�ɼ��� */
							registerPasswordEditText
									.setTransformationMethod(HideReturnsTransformationMethod
											.getInstance());
						} else {
							/* �趨EditText������Ϊ���ص� */
							registerPasswordEditText
									.setTransformationMethod(PasswordTransformationMethod
											.getInstance());
						}
					}
				});

		// ���ð���������ע�����������Ϣ
		registerResetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				registerPasswordEditText.setText("");
				registerPhoneEditText.setText("");
				registerMessageCheckEditText.setText("");
			}
		});

		// ��ȡ������֤��
		registerGetMessageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!isPhoneNumberIllegal()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserRegisterActivity.this)
							.setTitle("��ܰ��ʾ")
							.setMessage("��������ȷ�ĵ绰����")
							.setPositiveButton("������д�绰",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											registerPhoneEditText
													.requestFocus();
											dialog.dismiss();
										}
									});
					builder.create().show();
				} else {
					sendMessage();
					time = new TimeCount(60000, 1000);// ����CountDownTimer����
					time.start();
				}
			}
		});

		registerConfirmeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��ȡע����е���������
				final String password = registerPasswordEditText.getText()
						.toString();
				final String phone = registerPhoneEditText.getText().toString();
				// �ж���˸������Ƿ�Ϊ��
				if (password.equals("") || phone.equals("")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserRegisterActivity.this)
							.setTitle("��ܰ��ʾ")
							.setMessage("����Ϣ������")
							.setPositiveButton("������д",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									});
					builder.create().show();
					return;
				}

				// �ж���֤���Ƿ���ȷ
				if (!isMessageCheck()) {
					Toast.makeText(getApplicationContext(), "��֤�����",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// ��֤�绰�����Ƿ����
				if (!reCheckPhoneNum.equals(phone)) {
					Toast.makeText(getApplicationContext(), "��ʹ�ý�����֤����ֻ���ע��",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// �ж�����״̬
				if (!HttpUtils.isNetWorkEnable(UserRegisterActivity.this)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserRegisterActivity.this)
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
				// ��ʾ�������Ի���
				registerDialog = new ProgressDialog(UserRegisterActivity.this);
				registerDialog.setTitle("��ܰ��ʾ");
				registerDialog.setMessage("����ע��");
				registerDialog.setCancelable(true);
				registerDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				registerDialog.show();
				// �ϴ�ע����Ϣ
				new Thread() {
					public void run() {
						// http://222.24.63.101/Ordering/customerregcl?account=zzzzzz&passwd=123456&phone=13512347896&collegeId=1&apartId=1&dormNum=123
						try {
							SimpleDateFormat t = new SimpleDateFormat(
									"yyyy-MM-dd");

							String date = t.format(new Date());
							Log.i("aaa", date);
							ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair(USER_PHONE, phone
									+ ""));
							// params.add(new
							// BasicNameValuePair("passwd",MD5StringUtil.MD5Encode(password)));
							params.add(new BasicNameValuePair(USER_PASSWD,
									password));
							params.add(new BasicNameValuePair(ACTION,
									MD5StringUtil.MD5Encode(date)));
							String result = HttpUtils.queryStringForPost(
									HttpUtils.USER_REGISTER_URL, params);
							if (result.equals(RESPONSE_REGISTER_OK)) {
								handler.sendEmptyMessage(0x123);
							} else if (result.equals(RESPONSE_REGISTER_REPEAT)) {
								handler.sendEmptyMessage(0x234);
							} else if (result.equals(RESPONSE_DATABASE_ERROR)) {
								handler.sendEmptyMessage(0x235);
							} else if (result.equals(RESPONSE_ACTION_ERROR)) {
								handler.sendEmptyMessage(0x236);
							} else if (result.equals(HttpUtils.NETWORK_ERROR)) {
								handler.sendEmptyMessage(0x345);
							} else if (TextUtils.isEmpty(result)) {
								handler.sendEmptyMessage(0x999);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					};

				}.start();
			}
		});
	}

	/*
	 * Activity�������ڽ���ǰ���õķ���
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Bundle b = new Bundle();
		b.putString("pass", registerPasswordEditText.getText().toString());
		b.putString("phone", registerPhoneEditText.getText().toString());
		outState.putBundle(USER_INFO_BUNDLE, b);

	}

	/**
	 * �жϵ绰�����Ƿ�Ϸ�
	 * 
	 * @return
	 */
	private boolean isPhoneNumberIllegal() {
		String phone = registerPhoneEditText.getText().toString();
		Pattern regex = Pattern.compile(UserRegisterActivity.regExPhoneNumber);
		Matcher matcher = regex.matcher(phone);
		return matcher.matches();
	}

	/**
	 * �ж϶�����֤����ȷ��
	 */
	private boolean isMessageCheck() {
		if (TextUtils
				.isEmpty(registerMessageCheckEditText.getText().toString())) {
			return false;
		}
		if (code == Integer.parseInt(registerMessageCheckEditText.getText()
				.toString())) {
			return true;
		} else {
			return false;
		}
	}

	// ���Ͷ�����֤��
	private void sendMessage() {
		new Thread() {
			@Override
			public void run() {
				registerGetMessageButton.setClickable(false);
				reCheckPhoneNum = registerPhoneEditText.getText().toString();
				code = (int) ((Math.random() * 9 + 1) * 100000);
				handler.sendEmptyMessage(0x567);

				String PostData = null;
				String servicePhone = "18729354347";
				try {
					PostData = "account=cf_xrml&password=lijingenxrml&mobile="
							+ registerPhoneEditText.getText().toString()
							+ "&content="
							+ java.net.URLEncoder.encode("������֤���ǣ�" + code
									+ "  ���벻Ҫ����֤��й¶�������ˡ�", "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// �������ʹ�����������ʹ��32λMD5����
				String ret = com.CollegeState.Util.Send.SMS(PostData,
						"http://106.ihuyi.cn/webservice/sms.php?method=Submit");

				Message msg = new Message();
				msg.what = 0x456;
				msg.obj = ret;
				handler.sendMessage(msg);
			}
		}.start();
	}

	/* ����һ������ʱ���ڲ��� */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// ��������Ϊ��ʱ��,�ͼ�ʱ��ʱ����
		}

		@Override
		public void onFinish() {// ��ʱ���ʱ����
			registerGetMessageButton.setText("���·���");
			registerGetMessageButton.setTextColor(Color.BLACK);
			registerGetMessageButton.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// ��ʱ������ʾ
			registerGetMessageButton.setClickable(false);
			registerGetMessageButton.setText(millisUntilFinished / 1000 + "��");
		}
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
				finish();
			}
		});
		titleText = (TextView) findViewById(R.id.title_name);
		titleText.setText("ע��");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
