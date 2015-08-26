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

	// listview的adapter
	MyAdapter myAdapter = null;
	// 保存菜单信息（从服务器上获取的原始信息）
	public static List<GrabInfoBean> menuList = new ArrayList<GrabInfoBean>();
	// 从服务器获取的原始信息转换之后的数据
	private List<Map<String, Object>> originalData;
	// 要显示的data
	private List<Map<String, Object>> mData;
	// 正在加载界面
	private TextView loadingText;
	// 标题栏
	private ImageButton back;
	private TextView titleText;

	private Intent intent = null;
	// 与服务器通信的参数
	private String AREA_ID = "areaId";
	private String NO_PRO_GOODS = "no_proGooods";
	private String PROMOTION_ID = "promotionId";// 商品id
	private String USER_ID = "userId";
	private String ACTION = "action";
	private String DATABASE_ERROR = "";
	private String GRAB_OK = "grab_ok";
	private String ACTION_ERROR = "action_error";
	private String GRAB_ERROR = "grab_error";
	private String GRAB_REPEAT="grab_repeat";
	// 抢饭计数
	private int grabCount = 0;
	// 抢饭倒计时
	private int TimeCount = 0;

	private TimeCount timer;
	private TimeCount_returnZero timer_1;

	// 抢饭限制
	private String SHARED_PREF_NAME = "USER_LOGIN_INFO";
	private String SHARED_PREF_USER_ACCOUNT = "account";
	private String SHARED_PREF_GRAB_DATE = "lastdate";
	private String SHARED_PREF_TODAY_GRAB_COUNT = "today_count";
	private SharedPreferences sharedPreference = null;
	private SharedPreferences.Editor editor = null;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				// 返服务器的菜单数据
				menuList = (List<GrabInfoBean>) msg.obj;
				originalData = getData((List<GrabInfoBean>) msg.obj);
				mData = originalData;

				// 将正在加载的界面换掉
				changeView();

				myAdapter = new MyAdapter(GrabMealActivity.this);
				listView.setAdapter(myAdapter);
			} else if (msg.what == 0x234) {
				Toast.makeText(GrabMealActivity.this, "暂无活动商品",
						Toast.LENGTH_SHORT).show();
				changeView();
			} else if (msg.what == 0x345) {
				Toast.makeText(GrabMealActivity.this, "数据库错误",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0x456) {
				AlertDialog.Builder builder0 = new AlertDialog.Builder(
						GrabMealActivity.this)
						.setTitle("校帮")
						.setMessage("抢饭成功！可在'我的' -> '我的财富' 中查看 ")
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								})
						.setPositiveButton("我的财富",
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
				Toast.makeText(GrabMealActivity.this, "登陆失效，请重新登陆",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0x678) {
				Toast.makeText(GrabMealActivity.this, "该商品已被抢完",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0x789) {
				Toast.makeText(GrabMealActivity.this, "运气不好，抢饭失败",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0x890) {
				Toast.makeText(GrabMealActivity.this, "您今天已经抢过了，明天再来吧",
						Toast.LENGTH_SHORT).show();
			}
			// 服务器错误
			else if (msg.what == 0x999) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						GrabMealActivity.this)
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

	// 数据加载完成时，正在加载变成列表
	private void changeView() {
		listView.setVisibility(View.VISIBLE);
		loadingText.setVisibility(View.GONE);
	}

	/**
	 * 通过Http请求获取List，该方法会开启新的线程 通过handler发送数据到主线程更新listview
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
	 * 将服务器返回的list转化为可以填充listview的list<map<obj,obj>>
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
	 * 抢饭线程
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

		public TextView goodsName;// 商品名称
		public TextView goodsPrivce;// 商品价格
		public TextView shopName;// 商铺名称
		public TextView closingDate;// 截止日期
		public TextView startDate;// 截止日期
		public TextView goodsCount;// 商品数量
		public Button grabButton;// 抢饭按钮
	}

	final class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		// 刷新列表
		public void refresh() {
			notifyDataSetChanged();
		}

		@Override
		// 返回当前的adapter中包含item数量
		public int getCount() {
			return mData.size();
		}

		@Override
		// 根据位置得到相应的item对象
		public Object getItem(int arg0) {
			return mData.get(arg0);
		}

		@Override
		// 根据位置得到相应的item的id
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (mData != null) {// list不为空的情况下
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

				// 去除小数点后为0
				String temp = mData.get(position).get("goodsPrivce").toString();
				if (temp.endsWith(".0")) {
					temp = temp.substring(0, temp.length() - 2);
				}
				holder.goodsPrivce.setText("￥:" + temp);

				holder.shopName.setText(mData.get(position).get("shopName")
						.toString());
				holder.closingDate.setText("到"
						+ mData.get(position).get("closingDate").toString()
						+ "号");
				holder.startDate.setText("活动时间:"
						+ mData.get(position).get("proAddTime").toString()
						+ "号");
				holder.goodsCount.setText("剩余"
						+ mData.get(position).get("proGoodsCount").toString()
						+ "份");
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
							// 将CountDownTime放于下面也避免一开始显示0秒，线程启动有延时
							if (timer == null) {
								Toast.makeText(getApplicationContext(),
										"您抢得太频繁了，请10秒后再试", Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(getApplicationContext(),
										"您抢得太频繁了，请稍后再试", Toast.LENGTH_SHORT)
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

	// 当一定时间内没有抢，自动将计数归零
	private void grabBtnReturnZero() {
		if (timer_1 == null) {
			timer_1 = new TimeCount_returnZero(10000, 1000);
			timer_1.start();
		}
	}

	/* 定义一个倒计时的内部类，对于归零 */
	class TimeCount_returnZero extends CountDownTimer {

		public TimeCount_returnZero(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			grabCount = 0;
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程

		}
	}

	// 对抢饭按钮的点击次数的时间限制
	private void countDownTime() {
		if (TimeCount == 0) {
			if (timer_1 != null)
				timer_1 = null;
			timer = new TimeCount(10000, 1000);
			timer.start();
		}
	}

	/* 定义一个倒计时的内部类，对于抢饭点击次数 */
	class TimeCount extends CountDownTimer {
		// 参数依次为总时长,和计时的时间间隔
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			grabCount = 0;
			TimeCount = 0;
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程
			TimeCount = (int) millisUntilFinished / 1000;
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
		titleText.setText("抢饭");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void CheckWhenCreat() {

		// 检查网络状态
		if (!HttpUtils.isNetWorkEnable(this)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					GrabMealActivity.this)
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
		// 检查登录状态
		if (!UserInfoBean.isLogin()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					GrabMealActivity.this)
					.setTitle("温馨提示")
					.setMessage("您还没有登录!")
					.setPositiveButton("登录",
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
