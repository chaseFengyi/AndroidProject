package com.CollegeState.UserActivity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.BuyActivity.ShopActivity;
import com.CollegeState.Data.OrderItem;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.MD5StringUtil;

public class UserChangePasswdAcitvity extends Activity {
	private EditText passwd_oriange;// 原密码
	private EditText passwd_new;// 新密码
	private EditText passwd_confirm;// 确认新密码
	private Button confirm;// 确认按钮
	private ProgressDialog passwdProgressDialog;
	private ImageButton back;// 返回按钮
	private TextView titleText;// 标题名称

	private String USER_PHONE = "userPhone";
	private String USER_PASSWD = "userPasswd";
	private String NEW_PASSWD = "newPasswd";
	private String ACTION = "action";
	private String OLD_PASSWD_ERROR = "oldPassword_error";
	private String UPDATE_OK = "update_ok";
	private String ACTION_ERROR = "action_error";

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			// create order filed!(生成订单失败)
			case 0x123:
				passwdProgressDialog.dismiss();
				AlertDialog.Builder builder0 = new AlertDialog.Builder(
						UserChangePasswdAcitvity.this)
						.setTitle("温馨提示")
						.setMessage("服务器开小差了！")
						.setPositiveButton("稍后再试",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder0.create().show();
				break;
			case 0x124:
				passwdProgressDialog.dismiss();
				AlertDialog.Builder builder1 = new AlertDialog.Builder(
						UserChangePasswdAcitvity.this)
						.setTitle("温馨提示")
						.setMessage("原密码错误！")
						.setPositiveButton("请重试",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder1.create().show();
				break;
			// Submit Success! 提交成功
			case 0x234:
				passwdProgressDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserChangePasswdAcitvity.this)
						.setTitle("温馨提示")
						.setMessage("密码修改成功！")
						.setPositiveButton("去登陆",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Intent intent = new Intent();
										intent.setClass(
												UserChangePasswdAcitvity.this,
												UserLoginActivity.class);
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(intent);

									}
								})
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder.create().show();
				break;
			// 网络错误
			case 0x345:
				passwdProgressDialog.dismiss();
				AlertDialog.Builder builder2 = new AlertDialog.Builder(
						UserChangePasswdAcitvity.this)
						.setTitle("温馨提示")
						.setMessage("网络错误！")
						.setPositiveButton("去查看网络",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Intent intent = new Intent(
												android.provider.Settings.ACTION_SETTINGS);
										startActivity(intent);

									}
								})
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder2.create().show();
			case 0x456:
				passwdProgressDialog.dismiss();
				AlertDialog.Builder builder3 = new AlertDialog.Builder(
						UserChangePasswdAcitvity.this)
						.setTitle("温馨提示")
						.setMessage("登陆失效，请重新登陆")
						.setPositiveButton("请重试",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder3.create().show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_change_passwd);
		initActionbar();
		// 检测用户登录状态
		if (!UserInfoBean.isLogin()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					UserChangePasswdAcitvity.this)
					.setTitle("温馨提示")
					.setMessage("您还没有登录!")
					.setPositiveButton("登录",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent();
									intent.setClass(
											UserChangePasswdAcitvity.this,
											UserLoginActivity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);

								}
							})
					.setNegativeButton("返回",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									finish();
								}
							});
			builder.create().show();
			return;
		}
		// 判断网络状态
		isNetWorkEnable();
		findView();
		setListener();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (check()) {
					// 显示进度条对话框
					passwdProgressDialog = new ProgressDialog(
							UserChangePasswdAcitvity.this);
					passwdProgressDialog.setTitle("校帮");
					passwdProgressDialog.setMessage("正在修改");
					passwdProgressDialog.setCancelable(true);
					passwdProgressDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					passwdProgressDialog.show();
					// 上传注册信息
					new Thread() {
						public void run() {
							try {
								// 建立一个NameValuePair数组，用于存储欲传送的参数
								ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair(USER_PHONE,
										UserInfoBean.getUserPhone()));
								params.add(new BasicNameValuePair(USER_PASSWD,
										passwd_oriange.getText().toString()));
								params.add(new BasicNameValuePair(NEW_PASSWD,
										passwd_new.getText().toString()));
								params.add(new BasicNameValuePair(ACTION,
										UserInfoBean.getAction()));
								String result = HttpUtils.queryStringForPost(
										HttpUtils.USER_CHANGE_PASSWD, params);
								if (result.equals(OLD_PASSWD_ERROR)) {
									handler.sendEmptyMessage(0x124);
								} else if (result.equals(UPDATE_OK)) {
									handler.sendEmptyMessage(0x234);
								} else if (result
										.equals(HttpUtils.NETWORK_ERROR)) {
									handler.sendEmptyMessage(0x345);
								} else if (result.equals(ACTION_ERROR)) {
									handler.sendEmptyMessage(0x456);
								} else {
									handler.sendEmptyMessage(0x123);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						};

					}.start();
				}
			}
		});
	}

	private void findView() {
		// TODO Auto-generated method stub
		passwd_oriange = (EditText) findViewById(R.id.user_change_passwd_oriange);
		passwd_new = (EditText) findViewById(R.id.user_change_passwd_new);
		passwd_confirm = (EditText) findViewById(R.id.user_change_passwd_new_confrim);
		confirm = (Button) findViewById(R.id.user_change_passwd_confrim);
	}

	/**
	 * 判断表单密码
	 * 
	 * @return
	 */
	private boolean check() {
		if (!(passwd_new.getText().toString().equals(passwd_confirm.getText()
				.toString()))) {
			Toast.makeText(getApplicationContext(), "两次密码输入不一致",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 检测网络状态
	 * 
	 */
	private void isNetWorkEnable() {
		if (!HttpUtils.isNetWorkEnable(this)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					UserChangePasswdAcitvity.this)
					.setTitle("温馨提示")
					.setMessage("网络错误")
					.setNegativeButton("返回",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									finish();
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
				// TODO Auto-generated method stub
				finish();
			}
		});
		titleText = (TextView) findViewById(R.id.title_name);
		titleText.setText("修改密码");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
