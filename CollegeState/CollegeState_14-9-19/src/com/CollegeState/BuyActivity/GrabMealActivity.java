package com.CollegeState.BuyActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.Data.GrabInfoBean;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.UserActivity.MyWealthActivity;
import com.CollegeState.UserActivity.UserLoginActivity;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.JSONUtils;

public class GrabMealActivity extends Activity {
	private ListView listView;

	// listview��adapter
	MyAdapter myAdapter = null;
	// ����˵���Ϣ���ӷ������ϻ�ȡ��ԭʼ��Ϣ��
	public static List<GrabInfoBean> menuList = new ArrayList<GrabInfoBean>();
	// �ӷ�������ȡ��ԭʼ��Ϣת��֮�������
	private List<Map<String, Object>> originalData;
	// Ҫ��ʾ��data
	private List<Map<String, Object>> mData;
	// ���ڼ��ؽ���
	private TextView loadingText;
	// ������
	private ImageButton back;
	private TextView titleText;

	private Intent intent = null;
	// �������ͨ�ŵĲ���
	private String AREA_ID = "areaId";
	private String NO_PRO_GOODS = "no_proGooods";
	private String PROMOTION_ID = "promotionId";// ��Ʒid
	private String USER_ID = "userId";
	private String ACTION = "action";
	private String DATABASE_ERROR = "";
	private String GRAB_OK = "grab_ok";
	private String ACTION_ERROR = "action_error";
	private String GRAB_ERROR = "grab_error";
	private String GRAB_REPEAT="grab_repeat";
	// ��������
	private int grabCount = 0;
	// ��������ʱ
	private int TimeCount = 0;

	private TimeCount timer;
	private TimeCount_returnZero timer_1;

	// ��������
	private String SHARED_PREF_NAME = "USER_LOGIN_INFO";
	private String SHARED_PREF_USER_ACCOUNT = "account";
	private String SHARED_PREF_GRAB_DATE = "lastdate";
	private String SHARED_PREF_TODAY_GRAB_COUNT = "today_count";
	private SharedPreferences sharedPreference = null;
	private SharedPreferences.Editor editor = null;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				// ���������Ĳ˵�����
				menuList = (List<GrabInfoBean>) msg.obj;
				originalData = getData((List<GrabInfoBean>) msg.obj);
				mData = originalData;

				// �����ڼ��صĽ��滻��
				changeView();

				myAdapter = new MyAdapter(GrabMealActivity.this);
				listView.setAdapter(myAdapter);
			} else if (msg.what == 0x234) {
				Toast.makeText(GrabMealActivity.this, "���޻��Ʒ",
						Toast.LENGTH_SHORT).show();
				changeView();
			} else if (msg.what == 0x345) {
				Toast.makeText(GrabMealActivity.this, "���ݿ����",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0x456) {
				AlertDialog.Builder builder0 = new AlertDialog.Builder(
						GrabMealActivity.this)
						.setTitle("У��")
						.setMessage("�����ɹ�������'�ҵ�' -> '�ҵĲƸ�' �в鿴 ")
						.setNegativeButton("����",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								})
						.setPositiveButton("�ҵĲƸ�",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										intent = new Intent();
										intent.setClass(GrabMealActivity.this,
												MyWealthActivity.class);
										startActivity(intent);
									}
								});
				builder0.create().show();
			} else if (msg.what == 0x567) {
				Toast.makeText(GrabMealActivity.this, "��½ʧЧ�������µ�½",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0x678) {
				Toast.makeText(GrabMealActivity.this, "����Ʒ�ѱ�����",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0x789) {
				Toast.makeText(GrabMealActivity.this, "�������ã�����ʧ��",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0x890) {
				Toast.makeText(GrabMealActivity.this, "�������Ѿ������ˣ�����������",
						Toast.LENGTH_SHORT).show();
			}
			// ����������
			else if (msg.what == 0x999) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						GrabMealActivity.this)
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grab_meal);
		initActionbar();
		CheckWhenCreat();
		findView();
		setListener();
		getItemsList();
	}

	private void setListener() {
		// TODO Auto-generated method stub

	}

	private void findView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.grab_meal_list);
		loadingText = (TextView) findViewById(R.id.grab_meal_textView_loading);
	}

	// ���ݼ������ʱ�����ڼ��ر���б�
	private void changeView() {
		listView.setVisibility(View.VISIBLE);
		loadingText.setVisibility(View.GONE);
	}

	/**
	 * ͨ��Http�����ȡList���÷����Ὺ���µ��߳� ͨ��handler�������ݵ����̸߳���listview
	 * 
	 */
	private void getItemsList() {
		new Thread() {
			public void run() {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(AREA_ID, UserInfoBean
						.getUserChooseAreaId()));
				String result = HttpUtils.queryStringForPost(
						HttpUtils.CHECk_PROMOTIONAL_PRODUCTS, params);
				if (result.equals(NO_PRO_GOODS)) {
					Message msg = new Message();
					msg.what = 0x234;
					handler.sendMessage(msg);
				} else if (result.equals(null)) {
					handler.sendEmptyMessage(0x999);
				} else {
					List<GrabInfoBean> list = JSONUtils.getGrabInfoList(result);
					Message msg = new Message();
					msg.what = 0x123;
					msg.obj = list;
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	/**
	 * �����������ص�listת��Ϊ�������listview��list<map<obj,obj>>
	 * 
	 * @param shopList
	 * @return
	 */
	public List<Map<String, Object>> getData(List<GrabInfoBean> itemsList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (GrabInfoBean item : itemsList) {
			map = new HashMap<String, Object>();
			map.put("goodsName", item.getGoodsName());
			map.put("goodsPrivce", item.getProGoodsPrice());
			map.put("shopName", item.getShopName());
			map.put("closingDate", item.getProTime());
			map.put("promotionId", item.getPromotionId());
			map.put("proAddTime", item.getProAddTime());
			map.put("proGoodsCount", item.getProGoodsCount());
			list.add(map);
		}
		return list;
	}

	/**
	 * �����߳�
	 * 
	 */
	private void grabMeal(final String promotionId) {
		new Thread() {
			public void run() {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(PROMOTION_ID, promotionId
						+ ""));
				params.add(new BasicNameValuePair(USER_ID, UserInfoBean
						.getUserId()));
				params.add(new BasicNameValuePair(ACTION, UserInfoBean
						.getAction()));
				String result = HttpUtils.queryStringForPost(
						HttpUtils.USER_GRAB_MEAL, params);
				Message msg = new Message();
				if (result.equals(NO_PRO_GOODS)) {
					msg.what = 0x678;
					handler.sendMessage(msg);
				} else if (result.equals(DATABASE_ERROR)) {
					msg.what = 0x345;
					handler.sendMessage(msg);
				} else if (result.equals(GRAB_OK)) {
					msg.what = 0x456;
					handler.sendMessage(msg);
				} else if (result.equals(ACTION_ERROR)) {
					msg.what = 0x567;
					handler.sendMessage(msg);
				} else if (result.equals(GRAB_ERROR)) {
					msg.what = 0x789;
					handler.sendMessage(msg);
				} else if (result.equals(GRAB_REPEAT)) {
					msg.what = 0x890;
					handler.sendMessage(msg);
				} else if (TextUtils.isEmpty(result)) {
					handler.sendEmptyMessage(0x999);
				}

			};
		}.start();
	}

	final class ViewHolder {

		public TextView goodsName;// ��Ʒ����
		public TextView goodsPrivce;// ��Ʒ�۸�
		public TextView shopName;// ��������
		public TextView closingDate;// ��ֹ����
		public TextView startDate;// ��ֹ����
		public TextView goodsCount;// ��Ʒ����
		public Button grabButton;// ������ť
	}

	final class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		// ˢ���б�
		public void refresh() {
			notifyDataSetChanged();
		}

		@Override
		// ���ص�ǰ��adapter�а���item����
		public int getCount() {
			return mData.size();
		}

		@Override
		// ����λ�õõ���Ӧ��item����
		public Object getItem(int arg0) {
			return mData.get(arg0);
		}

		@Override
		// ����λ�õõ���Ӧ��item��id
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (mData != null) {// list��Ϊ�յ������
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = mInflater.inflate(
							R.layout.grab_meal_list_item, null);
					// findView
					holder.goodsName = (TextView) convertView
							.findViewById(R.id.grab_item_goods_name);
					holder.goodsPrivce = (TextView) convertView
							.findViewById(R.id.grab_item_goods_price);
					holder.shopName = (TextView) convertView
							.findViewById(R.id.grab_item_shop_name);
					holder.closingDate = (TextView) convertView
							.findViewById(R.id.grab_item_closing_date);
					holder.grabButton = (Button) convertView
							.findViewById(R.id.grab_item_grab_btn);
					holder.startDate = (TextView) convertView
							.findViewById(R.id.grab_item_start_date);
					holder.goodsCount = (TextView) convertView
							.findViewById(R.id.grab_item_goods_count);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.goodsName.setText(mData.get(position).get("goodsName")
						.toString());

				// ȥ��С�����Ϊ0
				String temp = mData.get(position).get("goodsPrivce").toString();
				if (temp.endsWith(".0")) {
					temp = temp.substring(0, temp.length() - 2);
				}
				holder.goodsPrivce.setText("��:" + temp);

				holder.shopName.setText(mData.get(position).get("shopName")
						.toString());
				holder.closingDate.setText("��"
						+ mData.get(position).get("closingDate").toString()
						+ "��");
				holder.startDate.setText("�ʱ��:"
						+ mData.get(position).get("proAddTime").toString()
						+ "��");
				holder.goodsCount.setText("ʣ��"
						+ mData.get(position).get("proGoodsCount").toString()
						+ "��");
				holder.grabButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (grabCount < 1) {
							grabMeal(mData.get(position).get("promotionId")
									+ "");
							grabCount++;
							grabBtnReturnZero();
						} else {
							// ��CountDownTime��������Ҳ����һ��ʼ��ʾ0�룬�߳���������ʱ
							if (timer == null) {
								Toast.makeText(getApplicationContext(),
										"������̫Ƶ���ˣ���10�������", Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(getApplicationContext(),
										"������̫Ƶ���ˣ����Ժ�����", Toast.LENGTH_SHORT)
										.show();
							}
							countDownTime();
						}
					}
				});
			}
			return convertView;
		}
	}

	// ��һ��ʱ����û�������Զ�����������
	private void grabBtnReturnZero() {
		if (timer_1 == null) {
			timer_1 = new TimeCount_returnZero(10000, 1000);
			timer_1.start();
		}
	}

	/* ����һ������ʱ���ڲ��࣬���ڹ��� */
	class TimeCount_returnZero extends CountDownTimer {

		public TimeCount_returnZero(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// ��������Ϊ��ʱ��,�ͼ�ʱ��ʱ����
		}

		@Override
		public void onFinish() {// ��ʱ���ʱ����
			grabCount = 0;
		}

		@Override
		public void onTick(long millisUntilFinished) {// ��ʱ����

		}
	}

	// ��������ť�ĵ��������ʱ������
	private void countDownTime() {
		if (TimeCount == 0) {
			if (timer_1 != null)
				timer_1 = null;
			timer = new TimeCount(10000, 1000);
			timer.start();
		}
	}

	/* ����һ������ʱ���ڲ��࣬��������������� */
	class TimeCount extends CountDownTimer {
		// ��������Ϊ��ʱ��,�ͼ�ʱ��ʱ����
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {// ��ʱ���ʱ����
			grabCount = 0;
			TimeCount = 0;
		}

		@Override
		public void onTick(long millisUntilFinished) {// ��ʱ����
			TimeCount = (int) millisUntilFinished / 1000;
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
				// TODO Auto-generated method stub
				finish();
			}
		});
		titleText = (TextView) findViewById(R.id.title_name);
		titleText.setText("����");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void CheckWhenCreat() {

		// �������״̬
		if (!HttpUtils.isNetWorkEnable(this)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					GrabMealActivity.this)
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
		// ����¼״̬
		if (!UserInfoBean.isLogin()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					GrabMealActivity.this)
					.setTitle("��ܰ��ʾ")
					.setMessage("����û�е�¼!")
					.setPositiveButton("��¼",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent();
									intent.setClass(GrabMealActivity.this,
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
