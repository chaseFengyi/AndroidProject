package com.CollegeState.BuyActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.androidpn.client.ServiceManager;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.CrashReport.CrashApplication;
import com.CollegeState.Data.UpdateInfo;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.UserActivity.UserLoginActivity;
import com.CollegeState.Util.DownloadUtil;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.JSONUtils;

/**
 * 
 * @author F.Crazy ������activity
 */
public class MainActivity extends Activity {
	private SearchView searchView;
	// �����¼��Ϣ��SharedPreference
	private SharedPreferences sharedPreference = null;
	// ����ѧУ��Ϣ��SharedPreference
	private SharedPreferences sharedPreferenceSchool = null;
	private SharedPreferences.Editor editor = null;
	private final String SHARED_PREF_SCHOOL = "CHOOSE_SCHOOL";
	private final String SHARED_PREF_SCHOOL_NAME = "school_name";
	private final String SHARED_PREF_SCHOOL_ID = "school_id";

	private ImageButton zhangshangcanting;// ���϶���
	private ImageButton woyaoqiangfan;// ��Ҫ����
	private ImageButton quweidingcan;// Ȥζ����
	private ImageButton woyaotiaoci;// ��Ҫ����
	private ImageButton dianhuajingcan;// �绰����
	private ImageButton gengduoqidai;// �����ڴ�
	private Button todayRecommend;// �����Ƽ�
	private Button todayRecommend_right;// �����Ƽ���ͷ
	private Button login;// ��½
	private Button shop;// �͵�
	private Button shopping_cart;// ���ﳵ
	private Button mine;// �ҵ�
	private Intent intent;
	private static Button chooseSchool;// ѡ��ѧУ

	// ���
	private ViewPager vp_ad_main = null;
	private List<View> adList = new ArrayList<View>();
	private ImageView iv1, iv2, iv3;
	private ImageView[] iv_circles;
	private ImageView iv_ad_circle_point;
	// ����
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	private boolean isContinue = true;
	// �Զ���¼
	private String SHARED_PREF_NAME = "USER_LOGIN_INFO";
	private String SHARED_PREF_USER_ACCOUNT = "account";
	private String SHARED_PREF_USER_PASSWORD = "password";
	private final static int AUTO_LOGIN_SUCC = 200;
	// �������ͨ����ز�����
	private String USER_PHONE = "userPhone";
	private String USER_PASSWD = "userPasswd";
	private String LOGIN_ERROR = "noUser_or_passwdError";

	private ProgressDialog pd;
	private UpdateInfo info;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// �Զ���¼�ɹ�
			case AUTO_LOGIN_SUCC:
				login.setText("����");
				break;
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
				if (DownloadUtil.getAppVersion(MainActivity.this).equals(
						info.getVersion())) {
					// Toast.makeText(getApplicationContext(), "�Ѿ������°汾",
					// Toast.LENGTH_SHORT).show();
				} else {
					showUpdateDialog();
				}
				break;
			case DownloadUtil.DOWNLOAD_SUCCESS:
				File file = (File) msg.obj;
				Toast.makeText(getApplicationContext(), "���سɹ�",
						Toast.LENGTH_SHORT).show();
				// ��װapk
				DownloadUtil.installApk(file, MainActivity.this);
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

	private final Handler viewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			vp_ad_main.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initActionbar();
		findView();
		setListener();
		getSavedData();
		initXMPPService();
		checkVersionAndUpdate();
		initAdViewPager();
		initCirclePoint();

	}

	@Override
	protected void onResume() {

		initAdViewPager();
		if (UserInfoBean.isLogin() == false) {
			login.setText("��¼");
		} else {
			login.setText("����");
			login.setClickable(false);
		}
		// ��ȡ�ϴ�ѡȡѧУ����Ϣ
		sharedPreferenceSchool = getSharedPreferences(SHARED_PREF_SCHOOL,
				MODE_PRIVATE);
		String school_name = sharedPreferenceSchool.getString(
				SHARED_PREF_SCHOOL_NAME, null);
		System.out.println(school_name+"!!!!");
		if (!TextUtils.isEmpty(school_name))
			chooseSchool.setText(school_name);

		String school_id = sharedPreferenceSchool.getString(
				SHARED_PREF_SCHOOL_ID, null);
		if (!TextUtils.isEmpty(school_id))
			UserInfoBean.setUserChooseAreaId(school_id);
		super.onResume();
	}

	private void initActionbar() {
		// �Զ��������
		getActionBar().setDisplayShowHomeEnabled(false); // ʹ���Ͻ�ͼ���Ƿ���ʾ��������false����û�г���ͼ�꣬�����͸����⣬������ʾӦ�ó���ͼ��
		getActionBar().setDisplayShowTitleEnabled(false); // ��ӦActionBar.DISPLAY_SHOW_TITLE��
		getActionBar().setDisplayShowCustomEnabled(true);// ʹ�Զ������ͨView����title����ʾ
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mTitleView = mInflater.inflate(R.layout.custom_action_bar_main,
				null);
		getActionBar().setCustomView(
				mTitleView,
				new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
		searchView = (SearchView) mTitleView
				.findViewById(R.id.search_view_main);
		chooseSchool = (Button) mTitleView
				.findViewById(R.id.main_choose_school);
		searchView.setQueryHint("������Ʒ");// û����������ʱ����ʾ
		searchView.setSubmitButtonEnabled(true);// ��ʾһ���ύ��ť
		searchView.setOnSearchClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				chooseSchool.setVisibility(View.GONE);
			}
		});

		searchView.setOnCloseListener(new OnCloseListener() {

			@Override
			public boolean onClose() {

				chooseSchool.setVisibility(View.VISIBLE);
				return false;
			}
		});

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				String str = searchView.getQuery().toString();
				intent = new Intent();
				intent.putExtra("SearchName", str);
				intent.setClass(MainActivity.this, SearchResultActivity.class);
				startActivity(intent);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				chooseSchool.setVisibility(View.GONE);
				return false;
			}
		});

		chooseSchool.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				intent = new Intent(MainActivity.this, ChooseAreaActivity.class);
				startActivity(intent);

			}
		});
	}

	/**
	 * ��ʼ�����
	 */
	private void initAdViewPager() {
		vp_ad_main = (ViewPager) findViewById(R.id.vp_ad_main);

		iv1 = new ImageView(this);
		iv1.setBackgroundResource(R.drawable.advertisement_1);
		adList.add(iv1);

		iv2 = new ImageView(this);
		iv2.setBackgroundResource(R.drawable.advertisement_2);
		adList.add(iv2);

		iv3 = new ImageView(this);
		iv3.setBackgroundResource(R.drawable.advertisement_3);
		adList.add(iv3);

		vp_ad_main.setAdapter(new ADPagerAdapter());
		vp_ad_main.setOnPageChangeListener(new AdPageChangeListener());

	}

	private void initCirclePoint() {
		ViewGroup viewGroup = (ViewGroup) findViewById(R.id.ll_ad_main_group);
		iv_circles = new ImageView[adList.size()];
		for (int i = 0; i < iv_circles.length; i++) {
			iv_ad_circle_point = new ImageView(this);
			iv_ad_circle_point.setLayoutParams(new LayoutParams(15, 15));
			iv_circles[i] = iv_ad_circle_point;
			if (i == 0) {
				iv_circles[i].setBackgroundResource(R.drawable.point_focused);
			} else {
				iv_circles[i].setBackgroundResource(R.drawable.point_unfocused);
			}
			viewGroup.addView(iv_circles[i]);
		}
		new Thread(new RefreshAdCircleTask()).start();
	}

	public class RefreshAdCircleTask implements Runnable {

		@Override
		public void run() {
			while (true) {
				if (isContinue) {
					viewHandler.sendEmptyMessage(atomicInteger.get());
					atomicOption();
				}
			}
		}
	}

	/**
	 * �����������µ�ǰλ��
	 */
	private void atomicOption() {
		atomicInteger.incrementAndGet();
		if (atomicInteger.get() > iv_circles.length - 1) {
			atomicInteger.getAndAdd(-3);
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * ���ͼƬ
	 * 
	 * @author zc
	 * 
	 */
	public final class ADPagerAdapter extends PagerAdapter {

		public int getCount() {
			return 3;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(adList.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(adList.get(position));
			return adList.get(position);
		}

	}

	/**
	 * Բ���������
	 * 
	 * @author zc
	 * 
	 */
	public final class AdPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int page) {
			atomicInteger.getAndSet(page);
			for (int i = 0; i < iv_circles.length; i++) {
				iv_circles[page]
						.setBackgroundResource(R.drawable.point_focused);
				if (page != i) {
					iv_circles[i]
							.setBackgroundResource(R.drawable.point_unfocused);
				}
			}
		}

	}

	// ����view
	private void findView() {
		zhangshangcanting = (ImageButton) findViewById(R.id.main_zhangshangcanting);		
		woyaoqiangfan = (ImageButton) findViewById(R.id.main_woyaoqiangfan);
		quweidingcan = (ImageButton) findViewById(R.id.main_quweidingcan);
		woyaotiaoci = (ImageButton) findViewById(R.id.main_woyaotiaoci);
		dianhuajingcan = (ImageButton) findViewById(R.id.main_dianhuadingcan);
		gengduoqidai = (ImageButton) findViewById(R.id.main_gengduoqidai);
		todayRecommend = (Button) findViewById(R.id.main_today_recommendButton);
		todayRecommend_right = (Button) findViewById(R.id.main_today_recommend_ButtonRight);
		login = (Button) findViewById(R.id.main_login);
		shop = (Button) findViewById(R.id.main_candian);
		shopping_cart = (Button) findViewById(R.id.main_shopping_cart);
		mine = (Button) findViewById(R.id.main_wode);
	}

	// ���ü���
	private void setListener() {
		zhangshangcanting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				intent = new Intent();
				intent.putExtra("type", "shop");
				intent.setClass(MainActivity.this, ShopActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		quweidingcan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				intent = new Intent();
				intent.setClass(MainActivity.this,
						FancySelectFoodActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		woyaotiaoci.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				intent = new Intent();
				intent.setClass(MainActivity.this, MySuggestionActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		woyaoqiangfan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				intent = new Intent();
				intent.setClass(MainActivity.this, GrabMealActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		dianhuajingcan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this)
						.setTitle("У��")
						.setMessage("�Ƿ���ϵ���Ͳ�����")
						.setPositiveButton("��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										intent = new Intent();
										intent.setAction("android.intent.action.DIAL");
										intent.setData(Uri
												.parse("tel:15802910391"));
										startActivity(intent);
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

		gengduoqidai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				intent = new Intent();
				intent.setClass(MainActivity.this, MoreExpectActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		todayRecommend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				intent = new Intent();
				intent.setClass(MainActivity.this, ShopActivity.class);
				intent.putExtra("type", "recommended");
				MainActivity.this.startActivity(intent);
			}
		});

		todayRecommend_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				intent = new Intent();
				intent.setClass(MainActivity.this, ShopActivity.class);
				intent.putExtra("type", "recommended");
				MainActivity.this.startActivity(intent);
			}
		});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				intent = new Intent();
				intent.setClass(MainActivity.this, UserLoginActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				intent = new Intent();
				intent.putExtra("type", "shop");
				intent.setClass(MainActivity.this, ShopActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		shopping_cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				intent = new Intent();
				intent.setClass(MainActivity.this, ShoppingCartActivity.class);
				startActivity(intent);

			}
		});

		mine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				intent = new Intent();
				intent.setClass(MainActivity.this, MoreActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
	}

	/**
	 * ��activity����ʱ�Զ���SharedPreference�ж�ȡ�û���Ϣ
	 */
	private void getSavedData() {
		sharedPreference = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
		// ���ֻ��л�ȡSharedPreference����ȡʧ��ʱ����""�ַ���,����ʾ�ڿؼ���
		final String userAccount = sharedPreference.getString(
				SHARED_PREF_USER_ACCOUNT, "");
		final String userPassword = sharedPreference.getString(
				SHARED_PREF_USER_PASSWORD, "");
		if (!(userAccount.equals(null) || userPassword.equals(null))) {
			// ��ȡ������
			new Thread() {
				public void run() {
					try {
						// ��������
						ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair(USER_PHONE,
								userAccount));
						// params.add(new BasicNameValuePair("passwd",
						// MD5StringUtil.MD5Encode(userPassword)));
						params.add(new BasicNameValuePair(USER_PASSWD,
								userPassword));
						String result = HttpUtils.queryStringForPost(
								HttpUtils.USER_LOGIN_URL, params);
						// �û������������
						if (result.equals(LOGIN_ERROR)) {

						}
						// �������
						else if (result.equals(HttpUtils.NETWORK_ERROR)) {

						}
						// ��¼�ɹ�����user����
						else {
							String jsonString = result;
							JSONUtils.setUserInfoAfterLogin(jsonString);
							// ��ǵ�¼�ɹ�״̬
							UserInfoBean.setLogin(true);
							handler.sendEmptyMessage(AUTO_LOGIN_SUCC);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}

	private void checkVersionAndUpdate() {
		CrashApplication app = (CrashApplication) getApplication();
		if (app.isFirstIn == true) {
			new Thread(new CheckVersionTask()).start();
			app.isFirstIn = false;
		}

	}

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

	/**
	 * ��������ʾ�Ի���
	 */
	private void showUpdateDialog() {
		// �˴�����ʹ��this��context(�����Ի�����������activity��)
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("��������").setMessage(info.getDescription())
				.setCancelable(false)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						String apkUrl = info.getApkurl();
						pd = new ProgressDialog(MainActivity.this);
						pd.setTitle("�����°汾:" + info.getVersion());
						pd.setMessage("��������");
						pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						pd.show();
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
				});

		builder.create().show();
	};

	/**
	 * ��ʼ��XMPP����
	 */
	public void initXMPPService() {
		ServiceManager serviceManager = new ServiceManager(this);
		serviceManager.setNotificationIcon(R.drawable.ic_launcher);
		serviceManager.startService();
	}

	public static class SchoolReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent1) {
			String area = intent1.getStringExtra("area");
			UserInfoBean.setUserChooseAreaId(intent1.getStringExtra("areaId"));
			chooseSchool.setText(area);
			chooseSchool.setTextSize(20);
		}

	}
}
