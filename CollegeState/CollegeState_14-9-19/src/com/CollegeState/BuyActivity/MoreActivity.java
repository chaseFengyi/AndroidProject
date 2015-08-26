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
	private Button myorder;// �ҵĶ���
	private Button passwd;// �޸�����
	private Button myWealth;// �ҵĲƸ�
	private Button checkForUpdates;// ������
	private Button logOut;// ע��
	private ImageButton back;// ���ذ�ť
	private TextView titleText;// ��������
	private Intent intent;

	// �����¼��Ϣ��SharedPreference
	private SharedPreferences sharedPreference = null;
	private SharedPreferences.Editor editor = null;
	public static String SHARED_PREF_NAME = "USER_LOGIN_INFO";
	public static String SHARED_PREF_USER_ACCOUNT = "account";
	public static String SHARED_PREF_USER_PASSWORD = "password";

	// �����й�
	private UpdateInfo info;
	private static final String TAG = "MoreActivity";
	private ProgressDialog pd;
	private ProgressDialog check4updatePd;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DownloadUtil.PARSER_XML_ERROR:
				Toast.makeText(getApplicationContext(), "����xmlʧ��",
						Toast.LENGTH_SHORT).show();
				break;
			case DownloadUtil.SERVER_ERROR:
				Toast.makeText(getApplicationContext(), "����������",
						Toast.LENGTH_SHORT).show();
				break;
			case DownloadUtil.URL_ERROR:
				Toast.makeText(getApplicationContext(), "URL����",
						Toast.LENGTH_SHORT).show();
				break;
			case DownloadUtil.NET_WORK_ERROR:
				Toast.makeText(getApplicationContext(), "�������",
						Toast.LENGTH_SHORT).show();
				break;
			case DownloadUtil.PARSER_XML_SUCCESS:
				if (DownloadUtil.getAppVersion(MoreActivity.this).equals(
						info.getVersion())) {
					Log.i(TAG, "�汾��ͬ");
					Toast.makeText(getApplicationContext(), "�Ѿ������°汾",
							Toast.LENGTH_SHORT).show();
					check4updatePd.dismiss();
				} else {
					Log.i(TAG, "�汾��ͬ�����½���");
					showUpdateDialog();
				}
				break;
			case DownloadUtil.DOWNLOAD_SUCCESS:
				File file = (File) msg.obj;
				Toast.makeText(getApplicationContext(), "���سɹ�",
						Toast.LENGTH_SHORT).show();
				Log.i(TAG, "��װapk:" + file.getAbsolutePath());
				// ��װapk
				DownloadUtil.installApk(file, MoreActivity.this);
				finish();
				break;
			case DownloadUtil.DOWNLOAD_FAILD:
				Toast.makeText(getApplicationContext(), "����ʧ��",
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
				check4updatePd.setTitle("���ڼ���°汾");
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
						.setTitle("���в�")
						.setMessage("ȷ��ע����")
						.setPositiveButton("��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (logOut())
											Toast.makeText(
													getApplicationContext(),
													"ע���ɹ�", Toast.LENGTH_SHORT)
													.show();
										intent = new Intent();
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										intent.setClass(MoreActivity.this,
												MainActivity.class);
										MoreActivity.this.startActivity(intent);
										finish();
									}
								})
						.setNegativeButton("��",
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
		titleText.setText("�ҵ�");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	/**
	 * ��������ʾ�Ի���
	 */
	private void showUpdateDialog() {
		// �˴�����ʹ��this��context(�����Ի�����������activity��)
		// ���ǣ����context����������ʹ��getApplicationContext
		check4updatePd.dismiss();
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("��������")
				.setMessage(info.getDescription())
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						String apkUrl = info.getApkurl();
						pd = new ProgressDialog(MoreActivity.this);
						pd.setTitle("�����°汾:" + info.getVersion());
						pd.setMessage("��������");
						pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						pd.show();
						Log.i(TAG, "���غ�װ��" + apkUrl);
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
										// ���سɹ�
										msg.what = DownloadUtil.DOWNLOAD_SUCCESS;
										msg.obj = saveFile;
									} else {
										// ����ʧ��
										msg.what = DownloadUtil.DOWNLOAD_FAILD;
									}
									handler.sendMessage(msg);
									pd.dismiss();
								};
							}.start();
						} else {
							Toast.makeText(getApplicationContext(), "sd��������",
									Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	};

	/**
	 * ��������
	 * 
	 * @author zc
	 * 
	 */
	private class CheckVersionTask implements Runnable {

		@Override
		public void run() {
			// ����ʱ�������Ӱ���û�����
			long startTime = System.currentTimeMillis();
			// ��ȡ�ɵ���Ϣ
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
						// ����xmlʧ��
						msg.what = DownloadUtil.PARSER_XML_ERROR;
					} else {
						// �����ɹ�
						msg.what = DownloadUtil.PARSER_XML_SUCCESS;
					}
				} else {
					// ����������
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

		// �������״̬
		if (!HttpUtils.isNetWorkEnable(this)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MoreActivity.this)
					.setTitle("��ܰ��ʾ")
					.setMessage("�������")
					.setNegativeButton("����",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									finish();
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
									finish();
									startActivity(intent);
								}

							});
			builder.create().show();
		}
		// ����¼״̬
		if (!UserInfoBean.isLogin()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MoreActivity.this)
					.setTitle("��ܰ��ʾ")
					.setMessage("����û�е�¼!")
					.setPositiveButton("��¼",
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
					.setNegativeButton("����",
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
