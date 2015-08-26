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
	// 用户注册界面按键
	private Button registerResetButton = null;
	private Button registerConfirmeButton = null;
	private ImageButton back;// 返回按钮
	private TextView titleText;// back右边显示activity功能的text
	// 用户注册界面表单控件
	private EditText registerPasswordEditText = null;
	private EditText registerPhoneEditText = null;
	private EditText registerMessageCheckEditText = null;
	private Button registerGetMessageButton = null;
	private CheckBox registerCheckPasswdCheckBox = null;
	// 用户注册对话框
	private ProgressDialog registerDialog = null;
	// 注册之后用于Intent传递的数据
	public static String USER_ACCOUNT = "account";
	public static String USER_PASSWORD = "password";
	// 验证信息的正则表达式
	public static String regExPhoneNumber = "^(((13[0-9])|(15([0-3]|[5-9]))|(18[0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
	public static String regExMail = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	// 与服务器通信参数
	private String USER_PHONE = "userPhone";
	private String USER_PASSWD = "userPasswd";
	private String ACTION = "action";
	private String RESPONSE_REGISTER_OK = "register_ok";// 表示用户成功注册
	private String RESPONSE_REGISTER_REPEAT = "register_repeat";// 手机号码已经被注册
	private String RESPONSE_DATABASE_ERROR = "database_error";// 数据库操作错误
	private String RESPONSE_ACTION_ERROR = "action_error";// 校验错误

	private TimeCount time;
	// 短信验证码
	private int code = 159357;
	private String reCheckPhoneNum;
	// 用户获取Bundle的数据
	final static String USER_INFO_BUNDLE = "user_info";
	// 用户注册成功的异步处理Handler
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			// 表示注册成功（Register_Ok）
			if (msg.what == 0x123) {
				registerDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserRegisterActivity.this)
						.setTitle("恭喜")
						.setMessage("注册成功")
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								})
						.setPositiveButton("现在去登录",
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
			// 表示注册失败（User_Exist）
			if (msg.what == 0x234) {
				registerDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserRegisterActivity.this)
						.setTitle("温馨提示")
						.setMessage("该手机号码已存在")
						.setPositiveButton("重新输入用户名",
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
			// 数据库操作错误
			if (msg.what == 0x235) {
				registerDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserRegisterActivity.this)
						.setTitle("温馨提示")
						.setMessage("数据库操作错误")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder.create().show();
			}
			// 校验错误
			if (msg.what == 0x236) {
				registerDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserRegisterActivity.this)
						.setTitle("温馨提示")
						.setMessage("校验错误")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder.create().show();
			}
			// 网络错误
			if (msg.what == 0x345) {
				registerDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserRegisterActivity.this)
						.setTitle("温馨提示")
						.setMessage("网络错误")
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								})
						.setPositiveButton("去更改网络状态",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 跳转到系统的activity去更改网络
										dialog.dismiss();
										Intent intent = new Intent();
										intent.setAction(android.provider.Settings.ACTION_SETTINGS);
										startActivity(intent);
									}

								});
				builder.create().show();
			}
			// 服务器错误
			if (msg.what == 0x999) {
				registerDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserRegisterActivity.this)
						.setTitle("溫馨提示")
						.setMessage("系统正在升级，稍后再试")
						.setPositiveButton("确认",
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
				Toast.makeText(getApplicationContext(), "获取短信验证成功",
						Toast.LENGTH_SHORT).show();
			}
			if (msg.what == 0x567) {
				registerGetMessageButton.setClickable(false);
				registerGetMessageButton.setText("已发送");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_register);
		// activity动画
		overridePendingTransition(R.anim.anim_in_from_right,
				R.anim.anim_out_to_left);
		initActionbar();// 自定义标题栏
		registerResetButton = (Button) findViewById(R.id.ButtonUserRegisterReset);
		registerConfirmeButton = (Button) findViewById(R.id.ButtonUserRegisterConfirme);
		registerPhoneEditText = (EditText) findViewById(R.id.EditTextUserRegisterPhone);
		registerGetMessageButton = (Button) findViewById(R.id.UserRegisterButtonGetMessage);
		registerMessageCheckEditText = (EditText) findViewById(R.id.EditTextUserRegisterMessageCheck);
		registerCheckPasswdCheckBox = (CheckBox) findViewById(R.id.CheckBoxUserCheckPasswd);
		registerPasswordEditText = (EditText) findViewById(R.id.EditTextUserRegisterPassword);
		// 恢复现场
		if (savedInstanceState != null) {
			Bundle map = savedInstanceState.getBundle(USER_INFO_BUNDLE);
			registerPasswordEditText.setText(map.getShort("pass"));
			registerPhoneEditText.setText(map.getShort("phone"));
		}
		// 显示密码
		registerCheckPasswdCheckBox
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (registerCheckPasswdCheckBox.isChecked()) {
							/* 设定EditText的内容为可见的 */
							registerPasswordEditText
									.setTransformationMethod(HideReturnsTransformationMethod
											.getInstance());
						} else {
							/* 设定EditText的内容为隐藏的 */
							registerPasswordEditText
									.setTransformationMethod(PasswordTransformationMethod
											.getInstance());
						}
					}
				});

		// 重置按键，重置注册界面所有信息
		registerResetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				registerPasswordEditText.setText("");
				registerPhoneEditText.setText("");
				registerMessageCheckEditText.setText("");
			}
		});

		// 获取短信验证码
		registerGetMessageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!isPhoneNumberIllegal()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserRegisterActivity.this)
							.setTitle("温馨提示")
							.setMessage("请输入正确的电话号码")
							.setPositiveButton("重新填写电话",
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
					time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
					time.start();
				}
			}
		});

		registerConfirmeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取注册表中的所有数据
				final String password = registerPasswordEditText.getText()
						.toString();
				final String phone = registerPhoneEditText.getText().toString();
				// 判断这八个数据是否为空
				if (password.equals("") || phone.equals("")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserRegisterActivity.this)
							.setTitle("温馨提示")
							.setMessage("表单信息不完整")
							.setPositiveButton("继续填写",
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

				// 判断验证码是否正确
				if (!isMessageCheck()) {
					Toast.makeText(getApplicationContext(), "验证码错误",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// 验证电话号码是否更改
				if (!reCheckPhoneNum.equals(phone)) {
					Toast.makeText(getApplicationContext(), "请使用接受验证码的手机号注册",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// 判断网络状态
				if (!HttpUtils.isNetWorkEnable(UserRegisterActivity.this)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserRegisterActivity.this)
							.setTitle("温馨提示")
							.setMessage("网络错误")
							.setNegativeButton("返回",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									})
							.setPositiveButton("去更改网络状态",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 跳转到系统的activity去更改网络
											dialog.dismiss();
											Intent intent = new Intent();
											intent.setAction(android.provider.Settings.ACTION_SETTINGS);
											startActivity(intent);
										}

									});
					builder.create().show();
				}
				// 显示进度条对话框
				registerDialog = new ProgressDialog(UserRegisterActivity.this);
				registerDialog.setTitle("温馨提示");
				registerDialog.setMessage("正在注册");
				registerDialog.setCancelable(true);
				registerDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				registerDialog.show();
				// 上传注册信息
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
	 * Activity生命周期结束前调用的方法
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Bundle b = new Bundle();
		b.putString("pass", registerPasswordEditText.getText().toString());
		b.putString("phone", registerPhoneEditText.getText().toString());
		outState.putBundle(USER_INFO_BUNDLE, b);

	}

	/**
	 * 判断电话号码是否合法
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
	 * 判断短信验证码正确性
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

	// 发送短信验证码
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
							+ java.net.URLEncoder.encode("您的验证码是：" + code
									+ "  。请不要把验证码泄露给其他人。", "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// 密码可以使用明文密码或使用32位MD5加密
				String ret = com.CollegeState.Util.Send.SMS(PostData,
						"http://106.ihuyi.cn/webservice/sms.php?method=Submit");

				Message msg = new Message();
				msg.what = 0x456;
				msg.obj = ret;
				handler.sendMessage(msg);
			}
		}.start();
	}

	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			registerGetMessageButton.setText("重新发送");
			registerGetMessageButton.setTextColor(Color.BLACK);
			registerGetMessageButton.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			registerGetMessageButton.setClickable(false);
			registerGetMessageButton.setText(millisUntilFinished / 1000 + "秒");
		}
	}

	private void initActionbar() {
		// 自定义标题栏
		getActionBar().setDisplayShowHomeEnabled(false); // 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
		getActionBar().setDisplayShowTitleEnabled(false); // 对应ActionBar.DISPLAY_SHOW_TITLE。
		getActionBar().setDisplayShowCustomEnabled(true);// 使自定义的普通View能在title栏显示
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
		titleText.setText("注册");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
