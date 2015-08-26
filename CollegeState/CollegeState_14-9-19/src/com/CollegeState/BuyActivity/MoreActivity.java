package com.CollegeState.BuyActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.Data.UpdateInfo;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.UserActivity.MyWealthActivity;
import com.CollegeState.UserActivity.UserChangePasswdAcitvity;
import com.CollegeState.UserActivity.UserLoginActivity;
import com.CollegeState.UserActivity.UserOrderFormActivity;
import com.CollegeState.Util.DownloadUtil;
import com.CollegeState.Util.HttpUtils;

public class MoreActivity extends Activity {
	private Button myorder;// 我的订单
	private Button passwd;// 修改密码
	private Button myWealth;// 我的财富
	private Button checkForUpdates;// 检查更新
	private Button logOut;// 注销
	private ImageButton back;// 返回按钮
	private TextView titleText;// 标题名称
	private Intent intent;

	// 保存登录信息的SharedPreference
	private SharedPreferences sharedPreference = null;
	private SharedPreferences.Editor editor = null;
	public static String SHARED_PREF_NAME = "USER_LOGIN_INFO";
	public static String SHARED_PREF_USER_ACCOUNT = "account";
	public static String SHARED_PREF_USER_PASSWORD = "password";

	// 跟新有关
	private UpdateInfo info;
	private static final String TAG = "MoreActivity";
	private ProgressDialog pd;
	private ProgressDialog check4updatePd;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DownloadUtil.PARSER_XML_ERROR:
				Toast.makeText(getApplicationContext(), "解析xml失败",
						Toast.LENGTH_SHORT).show();
				break;
			case DownloadUtil.SERVER_ERROR:
				Toast.makeText(getApplicationContext(), "服务器错误",
						Toast.LENGTH_SHORT).show();
				break;
			case DownloadUtil.URL_ERROR:
				Toast.makeText(getApplicationContext(), "URL错误",
						Toast.LENGTH_SHORT).show();
				break;
			case DownloadUtil.NET_WORK_ERROR:
				Toast.makeText(getApplicationContext(), "网络错误",
						Toast.LENGTH_SHORT).show();
				break;
			case DownloadUtil.PARSER_XML_SUCCESS:
				if (DownloadUtil.getAppVersion(MoreActivity.this).equals(
						info.getVersion())) {
					Log.i(TAG, "版本相同");
					Toast.makeText(getApplicationContext(), "已经是最新版本",
							Toast.LENGTH_SHORT).show();
					check4updatePd.dismiss();
				} else {
					Log.i(TAG, "版本不同，更新界面");
					showUpdateDialog();
				}
				break;
			case DownloadUtil.DOWNLOAD_SUCCESS:
				File file = (File) msg.obj;
				Toast.makeText(getApplicationContext(), "下载成功",
						Toast.LENGTH_SHORT).show();
				Log.i(TAG, "安装apk:" + file.getAbsolutePath());
				// 安装apk
				DownloadUtil.installApk(file, MoreActivity.this);
				finish();
				break;
			case DownloadUtil.DOWNLOAD_FAILD:
				Toast.makeText(getApplicationContext(), "下载失败",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		initActionbar();
		setView();
		setListener();
		CheckWhenCreat();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		myorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.setClass(MoreActivity.this, UserOrderFormActivity.class);
				startActivity(intent);
			}
		});
		passwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.setClass(MoreActivity.this,
						UserChangePasswdAcitvity.class);
				startActivity(intent);
			}
		});
		
		myWealth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.setClass(MoreActivity.this, MyWealthActivity.class);
				startActivity(intent);
			}
		});

		checkForUpdates.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new Thread(new CheckVersionTask()).start();
				check4updatePd = new ProgressDialog(MoreActivity.this);
				check4updatePd.setTitle("正在检查新版本");
				check4updatePd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				check4updatePd.show();
			}
		});

		logOut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MoreActivity.this)
						.setTitle("掌中餐")
						.setMessage("确定注销？")
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (logOut())
											Toast.makeText(
													getApplicationContext(),
													"注销成功", Toast.LENGTH_SHORT)
													.show();
										intent = new Intent();
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										intent.setClass(MoreActivity.this,
												MainActivity.class);
										MoreActivity.this.startActivity(intent);
										finish();
									}
								})
						.setNegativeButton("否",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder.create().show();
			}
		});
	}

	private boolean logOut() {
		UserInfoBean.setOrderAddress("");
		UserInfoBean.setUserAuthority("");
		UserInfoBean.setUserCredit("");
		UserInfoBean.setUserId("");
		UserInfoBean.setUserPasswd("");
		UserInfoBean.setUserPhone("");
		//UserInfoBean.setUserChooseAreaId("");
		//UserInfoBean.setCityId("");
		UserInfoBean.setLogin(false);
		sharedPreference = getSharedPreferences(SHARED_PREF_NAME,
				Context.MODE_PRIVATE);
		editor = sharedPreference.edit();
		editor.clear();
		editor.commit();

		return true;
	}

	private void setView() {
		// TODO Auto-generated method stub
		myorder = (Button) findViewById(R.id.more_order);
		passwd = (Button) findViewById(R.id.more_change_passwd);
		myWealth = (Button) findViewById(R.id.more_my_wealth);
		checkForUpdates = (Button) findViewById(R.id.more_check_for_updates);
		logOut = (Button) findViewById(R.id.more_logout);
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
		titleText.setText("我的");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	/**
	 * 升级的提示对话框
	 */
	private void showUpdateDialog() {
		// 此处必须使用this的context(创建对话框必须挂载在activity上)
		// 如果牵扯到context的生命周期使用getApplicationContext
		check4updatePd.dismiss();
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("升级提醒")
				.setMessage(info.getDescription())
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						String apkUrl = info.getApkurl();
						pd = new ProgressDialog(MoreActivity.this);
						pd.setTitle("发现新版本:" + info.getVersion());
						pd.setMessage("正在下载");
						pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						pd.show();
						Log.i(TAG, "下载后安装：" + apkUrl);
						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
							final File file = new File(Environment
									.getExternalStorageDirectory(),
									DownloadUtil.getFileName(apkUrl));
							new Thread() {

								public void run() {
									File saveFile = DownloadUtil.download(
											info.getApkurl(),
											file.getAbsolutePath(), pd);
									Message msg = Message.obtain();
									if (saveFile != null) {
										// 下载成功
										msg.what = DownloadUtil.DOWNLOAD_SUCCESS;
										msg.obj = saveFile;
									} else {
										// 下载失败
										msg.what = DownloadUtil.DOWNLOAD_FAILD;
									}
									handler.sendMessage(msg);
									pd.dismiss();
								};
							}.start();
						} else {
							Toast.makeText(getApplicationContext(), "sd卡不可用",
									Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	};

	/**
	 * 下载任务
	 * 
	 * @author zc
	 * 
	 */
	private class CheckVersionTask implements Runnable {

		@Override
		public void run() {
			// 避免时间过长，影响用户体验
			long startTime = System.currentTimeMillis();
			// 获取旧的消息
			Message msg = Message.obtain();
			try {
				URL url = new URL(DownloadUtil.updateURL);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000);
				if (conn.getResponseCode() == 200) {
					InputStream is = conn.getInputStream();
					info = DownloadUtil.getUpdateInfo(is);
					if (info == null) {
						// 解析xml失败
						msg.what = DownloadUtil.PARSER_XML_ERROR;
					} else {
						// 解析成功
						msg.what = DownloadUtil.PARSER_XML_SUCCESS;
					}
				} else {
					// 服务器错误
					msg.what = DownloadUtil.SERVER_ERROR;
				}
			} catch (MalformedURLException e) {
				msg.what = DownloadUtil.URL_ERROR;
				e.printStackTrace();
			} catch (NotFoundException e) {
				msg.what = DownloadUtil.URL_ERROR;
				e.printStackTrace();
			} catch (IOException e) {
				msg.what = DownloadUtil.NET_WORK_ERROR;
				e.printStackTrace();
			} finally {
				long endTime = System.currentTimeMillis();
				long dTime = endTime - startTime;
				if (dTime < 2000) {
					try {
						Thread.sleep(2000 - dTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				handler.sendMessage(msg);
			}
		}

	}

	private void CheckWhenCreat() {

		// 检查网络状态
		if (!HttpUtils.isNetWorkEnable(this)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MoreActivity.this)
					.setTitle("溫馨提示")
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
									finish();
									startActivity(intent);
								}

							});
			builder.create().show();
		}
		// 检查登录状态
		if (!UserInfoBean.isLogin()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MoreActivity.this)
					.setTitle("溫馨提示")
					.setMessage("您还没有登录!")
					.setPositiveButton("登录",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent();
									intent.setClass(MoreActivity.this,
											UserLoginActivity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									finish();
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
	}

}
