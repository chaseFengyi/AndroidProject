package com.CollegeState.UserActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.CollegeState.R;
import com.CollegeState.BuyActivity.MainActivity;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.Data.UserOrderFormInfoBean;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.JSONUtils;

public class UserOrderFormActivity extends Activity {

	// 标题栏
	private ImageButton back;
	private TextView titleText;

	// listview的adapter
	MyAdapter myAdapter0 = null;
	MyAdapter myAdapter1 = null;
	MyAdapter myAdapter2 = null;
	MyAdapter myAdapter3 = null;
	MyAdapter myAdapter11 = null;
	// 要显示的data
	private List<Map<String, Object>> mData0, mData1, mData2, mData3, mData11;
	// ViewPager的操作
	private ViewPager viewPager = null;
	// 滑动的每一页的标题
	private PagerTitleStrip pagerTitleStrip = null;
	// 表示装载滑动的布局
	private List<View> list = null;
	// 表示滑动每一页的标题
	private List<String> title = null;
	//
	private View view_0, view_1, view_2, view_3, view_11;
	// 已提交，已接单，已配送，已收货，已评价，已取消，分别是0，1，2，3，4，-1（11）

	private ListView UserOrderForm_list_0;
	private ListView UserOrderForm_list_1;
	private ListView UserOrderForm_list_2;
	private ListView UserOrderForm_list_3;
	private ListView UserOrderForm_list_11;

	// 提交订单的进度条对话框
	private ProgressDialog submitOrderProgressDialog = null;

	// 记录提交修改的状态
	private int mark, mark_position;

	// 与服务器通信的参数
	private String USER_ID = "userId";
	private String ORDER_ITEM_STATE = "orderItemState";// 订单项状态
	private String NO_ORDER = "no_order";// 表示此用户没有下订单
	private String ORDER_ITEM_ID = "orderItemId";
	private String ACTION = "action";
	private String UPDATE_OK = "update_ok";// 修改成功
	private String ACTION_ERROR = "action_error";// 校验失败
	private String UPDATE_ERROR = "update_error";// 修改失败
	private String ASSESS_STYLE = "assessStyle"; // 评价类型
	private String ASSESS_OK = "assess_ok";// 评价成功
	private String ASSESS_ERROR = "assess_error";// 评价失败
	private String DATABASE_ERROR = "database_error";// 评价失败

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			// 服务器错误
			case 0x789:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder0 = new AlertDialog.Builder(
						UserOrderFormActivity.this)
						.setTitle("校帮")
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
			// 还未下单
			case 0x890:
				Toast.makeText(UserOrderFormActivity.this, "暂无",
						Toast.LENGTH_SHORT).show();
				break;
			// Submit Success! 提交成功
			case 0x123:
				submitOrderProgressDialog.dismiss();
				if (mark == 0)
					mData0.remove(mark_position);
				else if (mark == 2)
					mData2.remove(mark_position);
				else if (mark == 3)
					mData3.remove(mark_position);
				if (myAdapter0 != null)
					myAdapter0.refresh();
				if (myAdapter1 != null)
					myAdapter1.refresh();
				if (myAdapter2 != null)
					myAdapter2.refresh();
				if (myAdapter3 != null)
					myAdapter3.refresh();
				if (myAdapter11 != null)
					myAdapter11.refresh();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserOrderFormActivity.this)
						.setTitle("校帮")
						.setMessage("提交成功！")
						.setPositiveButton("返回首页",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Intent intent = new Intent();
										intent.setClass(
												UserOrderFormActivity.this,
												MainActivity.class);
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
			// Submit Success! 提交失败
			case 0x234:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder9 = new AlertDialog.Builder(
						UserOrderFormActivity.this)
						.setTitle("校帮")
						.setMessage("提交失败！")
						.setPositiveButton("返回首页",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Intent intent = new Intent();
										intent.setClass(
												UserOrderFormActivity.this,
												MainActivity.class);
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
				builder9.create().show();
				break;
			// 网络错误
			case 0x345:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder2 = new AlertDialog.Builder(
						UserOrderFormActivity.this)
						.setTitle("校帮")
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
				break;
			case 0x456:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder5 = new AlertDialog.Builder(
						UserOrderFormActivity.this)
						.setTitle("校帮")
						.setMessage("登陆失效，请重新登陆")
						.setPositiveButton("稍后再试",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder5.create().show();
				break;
			case 0x000:
				// 返服务器的菜单数据
				mData0 = getData((List<UserOrderFormInfoBean>) msg.obj);
				myAdapter0 = new MyAdapter(UserOrderFormActivity.this, 0);
				UserOrderForm_list_0.setAdapter(myAdapter0);
				if (mData0 == null || mData0.size() == 0)
					break;
				myAdapter0.refresh();
				break;
			case 0x001:
				// 返服务器的菜单数据
				mData1 = getData((List<UserOrderFormInfoBean>) msg.obj);
				myAdapter1 = new MyAdapter(UserOrderFormActivity.this, 1);
				UserOrderForm_list_1.setAdapter(myAdapter1);
				if (mData0 == null || mData0.size() == 0)
					break;
				myAdapter1.refresh();
				break;
			case 0x002:
				// 返服务器的菜单数据
				mData2 = getData((List<UserOrderFormInfoBean>) msg.obj);
				myAdapter2 = new MyAdapter(UserOrderFormActivity.this, 2);
				UserOrderForm_list_2.setAdapter(myAdapter2);
				if (mData2 == null || mData2.size() == 0)
					break;
				myAdapter2.refresh();
				break;
			case 0x003:
				// 返服务器的菜单数据
				mData3 = getData((List<UserOrderFormInfoBean>) msg.obj);
				myAdapter3 = new MyAdapter(UserOrderFormActivity.this, 3);
				UserOrderForm_list_3.setAdapter(myAdapter3);
				if (mData3 == null || mData3.size() == 0)
					break;
				myAdapter3.refresh();
				break;
			case 0x0011:
				// 返服务器的菜单数据
				mData11 = getData((List<UserOrderFormInfoBean>) msg.obj);
				myAdapter11 = new MyAdapter(UserOrderFormActivity.this, -1);
				UserOrderForm_list_11.setAdapter(myAdapter11);
				if (mData11 == null || mData11.size() == 0)
					break;
				myAdapter11.refresh();
				break;
			default:
				submitOrderProgressDialog.dismiss();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_form);
		if (UserInfoBean.isLogin() == false) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					UserOrderFormActivity.this)
					.setTitle("温馨提示")
					.setMessage("未登录")
					.setPositiveButton("去登陆",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 跳转到系统的activity去更改网络
									dialog.dismiss();
									Intent intent = new Intent();
									intent.setClass(UserOrderFormActivity.this,
											UserLoginActivity.class);
									startActivity(intent);
									finish();
								}

							});
			builder.create().show();
			return;
		}
		if (!HttpUtils.isNetWorkEnable(this)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					UserOrderFormActivity.this)
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
			return;
		}
		initViewPager();
		findView();
		setListener();
		initActionbar();

		getItemsList(0);
	}

	private void findView() {
		UserOrderForm_list_0 = (ListView) view_0
				.findViewById(R.id.user_form_list);
		UserOrderForm_list_1 = (ListView) view_1
				.findViewById(R.id.user_form_list);
		UserOrderForm_list_2 = (ListView) view_2
				.findViewById(R.id.user_form_list);
		UserOrderForm_list_3 = (ListView) view_3
				.findViewById(R.id.user_form_list);
		UserOrderForm_list_11 = (ListView) view_11
				.findViewById(R.id.user_form_list);
	}

	private void setListener() {

	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.userForm_ViewPagerForm);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				// 显示0
				case 0:
					getItemsList(0);
					if (mData0 == null)
						return;
					myAdapter0 = new MyAdapter(UserOrderFormActivity.this, 0);
					UserOrderForm_list_0.setAdapter(myAdapter0);
					break;
				// 显示1
				case 1:
					getItemsList(1);
					if (mData1 == null)
						return;
					myAdapter1 = new MyAdapter(UserOrderFormActivity.this, 1);
					UserOrderForm_list_1.setAdapter(myAdapter1);
					break;
				case 2:
					getItemsList(2);
					if (mData2 == null)
						return;
					myAdapter2 = new MyAdapter(UserOrderFormActivity.this, 2);
					UserOrderForm_list_2.setAdapter(myAdapter2);
					break;

				case 3:
					getItemsList(3);
					if (mData3 == null)
						return;
					myAdapter3 = new MyAdapter(UserOrderFormActivity.this, 3);
					UserOrderForm_list_3.setAdapter(myAdapter3);
					break;
				case 4:
					getItemsList(-1);
					if (mData11 == null)
						return;
					myAdapter11 = new MyAdapter(UserOrderFormActivity.this, -1);
					UserOrderForm_list_11.setAdapter(myAdapter11);
					break;
				default:
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.userForm_PagerTitleForm);
		view_0 = LayoutInflater.from(UserOrderFormActivity.this).inflate(
				R.layout.user_form_list, null);
		view_1 = LayoutInflater.from(UserOrderFormActivity.this).inflate(
				R.layout.user_form_list, null);
		view_2 = LayoutInflater.from(UserOrderFormActivity.this).inflate(
				R.layout.user_form_list, null);
		view_3 = LayoutInflater.from(UserOrderFormActivity.this).inflate(
				R.layout.user_form_list, null);
		view_11 = LayoutInflater.from(UserOrderFormActivity.this).inflate(
				R.layout.user_form_list, null);
		list = new ArrayList<View>();
		list.add(view_0);
		list.add(view_1);
		list.add(view_2);
		list.add(view_3);
		list.add(view_11);
		title = new ArrayList<String>();
		title.add("已下单");
		title.add("已接单");
		title.add("已配送");
		title.add("已收货");
		title.add("已取消");
		pagerTitleStrip.setTextSize(1, 25);
		pagerTitleStrip.setTextColor(Color.rgb(90, 135, 12));
		viewPager.setAdapter(new MyViewPagerAdapter());
	}

	/**
	 * 通过Http请求获取List，该方法会开启新的线程 通过handler发送数据到主线程更新listview
	 * 
	 */
	private void getItemsList(final int type) {
		new Thread() {
			public void run() {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(USER_ID, UserInfoBean
						.getUserId()));
				params.add(new BasicNameValuePair(ORDER_ITEM_STATE, type + ""));
				params.add(new BasicNameValuePair(ACTION, UserInfoBean
						.getAction()));
				String result = HttpUtils.queryStringForPost(
						HttpUtils.USER_ORDER_FORM, params);
				Message msg = new Message();
				if (result == null || result.equals(DATABASE_ERROR)) {
					msg.what = 0x789;
					handler.sendMessage(msg);
					return;
				}
				if (result.equals(NO_ORDER)) {
					msg.what = 0x890;
					handler.sendMessage(msg);
					return;
				}
				List<UserOrderFormInfoBean> list = JSONUtils
						.getUserOrderFormInfoList(result);

				if (type == 0) {
					msg.what = 0x000;
				} else if (type == 1) {
					msg.what = 0x001;
				} else if (type == 2) {
					msg.what = 0x002;
				} else if (type == 3) {
					msg.what = 0x003;
				} else if (type == -1) {
					msg.what = 0x0011;
				}
				msg.obj = list;
				handler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * 通过Http请求获取List，该方法会开启新的线程 通过handler发送数据到主线程 功能：确认收货 orderItemId 订单状态 Not
	 * null orderItemState 订单状态 0:已提交，1:已接单,2已发货,3:已确认，4已评价
	 */
	private void confirmOrder(final String orderItemId,
			final String orderItemState, final int position) {
		submitOrderProgressDialog = new ProgressDialog(
				UserOrderFormActivity.this);
		submitOrderProgressDialog.setTitle("校帮");
		submitOrderProgressDialog.setMessage("正在提交");
		submitOrderProgressDialog.setCancelable(true);
		submitOrderProgressDialog
				.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		submitOrderProgressDialog.show();
		new Thread() {
			public void run() {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(ORDER_ITEM_ID, orderItemId));
				params.add(new BasicNameValuePair(ORDER_ITEM_STATE,
						orderItemState));
				params.add(new BasicNameValuePair(ACTION, UserInfoBean
						.getAction()));
				String result = HttpUtils.queryStringForPost(
						HttpUtils.CHANGE_ORDER_ITEM_STATE, params);

				Message msg = new Message();
				if (result == null || result.equals(DATABASE_ERROR)) {
					msg.what = 0x789;
					handler.sendMessage(msg);
					return;
				} else if (result.equals(UPDATE_OK)) {

					msg.what = 0x123;
					if (orderItemState.equals("-1")) {
						mark = 0;
						mark_position = position;
					} else if (orderItemState.equals("3")) {
						mark = 2;
						mark_position = position;
					} else if (orderItemState.equals("4")) {
						mark = 3;
						mark_position = position;
					}

				} else if (result.equals(UPDATE_ERROR)) {
					msg.what = 0x234;
				} else if (result.equals(HttpUtils.NETWORK_ERROR)) {
					msg.what = 0x345;
				} else if (result.equals(ACTION_ERROR)) {
					msg.what = 0x456;
				} else {
					msg.what = 0x123;
				}
				handler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * 通过Http请求获取List，该方法会开启新的线程 通过handler发送数据到主线程 功能：评价 goodsId 商品id Not null
	 * customerId 用户id Not null assessStyle 评价类型 2：好评 1：中评 0：差评 assessContent
	 * 评价内容 可以为空
	 */
	private void assessOrder(final String customerId, final String orderItemId,
			final String goodsId, final String assessStyle,
			final String assessContent, final int position) {
		submitOrderProgressDialog = new ProgressDialog(
				UserOrderFormActivity.this);
		submitOrderProgressDialog.setTitle("校帮");
		submitOrderProgressDialog.setMessage("正在提交");
		submitOrderProgressDialog.setCancelable(true);
		submitOrderProgressDialog
				.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		submitOrderProgressDialog.show();
		new Thread() {
			public void run() {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(ORDER_ITEM_ID, orderItemId));
				params.add(new BasicNameValuePair(ASSESS_STYLE, assessStyle
						+ ""));
				params.add(new BasicNameValuePair(ACTION, UserInfoBean
						.getAction()));
				String result = HttpUtils.queryStringForPost(
						HttpUtils.USER_ASSESS, params);

				Message msg = new Message();
				if (result == null || result.equals(DATABASE_ERROR)) {
					msg.what = 0x789;
					handler.sendMessage(msg);
					return;
				} else if (result.equals(ASSESS_OK)) {
					msg.what = 0x123;

					mark = 3;
					mark_position = position;

				} else if (result.equals(ASSESS_ERROR)) {
					msg.what = 0x234;
				} else if (result.equals(HttpUtils.NETWORK_ERROR)) {
					msg.what = 0x345;
				} else if (result.equals(ACTION_ERROR)) {
					msg.what = 0x456;
				} else {
					msg.what = 0x123;
				}
				handler.sendMessage(msg);
			};
		}.start();
	}

	// 去除服务器返回的乱七八糟的东西
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 将服务器返回的list转化为可以填充listview的list<map<obj,obj>>
	 * 
	 * @param shopList
	 * @return
	 */
	public List<Map<String, Object>> getData(
			List<UserOrderFormInfoBean> itemsList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		if (itemsList == null) {
			return null;
		}
		for (UserOrderFormInfoBean item : itemsList) {
			map = new HashMap<String, Object>();
			map.put("goodsId", item.getGoodsId());
			map.put("orderItemId", item.getOrderItemId());
			map.put("orderItemState", item.getOrderItemState());
			map.put("goodsId", item.getGoodsId());
			map.put("goodsName", item.getGoodsName());
			map.put("shopName", item.getShopName());
			map.put("goodsCount", item.getGoodsCount() + "");
			map.put("goodsPrice", item.getGoodsPrice() + "");
			map.put("customerId", UserInfoBean.getUserId());
			list.add(map);
		}
		return list;
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
		titleText.setText("我的订单");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	final class ViewHolder {

		public TextView goodsName;// 商品名称
		public TextView shopName;// 店铺名称
		public TextView goodsCount;// 商品数量
		public TextView goodsPrice;// 商品单价
		public Button cancelOrder;// 撤销订单
		public Button confirm;// 确认收货
		public Button assess;// 商品评价
	}

	final class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private int type = 0;
		private List<Map<String, Object>> mData;

		public MyAdapter(Context context, int type) {
			this.mInflater = LayoutInflater.from(context);
			this.type = type;
			if (type == 0)
				mData = mData0;
			else if (type == 1)
				mData = mData1;
			else if (type == 2)
				mData = mData2;
			else if (type == 3)
				mData = mData3;
			else if (type == -1)
				mData = mData11;
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

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.user_form_list_item,
						null);
				// findView
				holder.goodsName = (TextView) convertView
						.findViewById(R.id.user_form_item_goodsName);
				holder.shopName = (TextView) convertView
						.findViewById(R.id.user_form_item_shopName);
				holder.goodsCount = (TextView) convertView
						.findViewById(R.id.user_form_item_goodsCount);
				holder.goodsPrice = (TextView) convertView
						.findViewById(R.id.user_form_item_goodsPrice);
				holder.cancelOrder = (Button) convertView
						.findViewById(R.id.user_form_item_cancel_order);
				holder.confirm = (Button) convertView
						.findViewById(R.id.user_form_item_confirm_receiving);
				holder.assess = (Button) convertView
						.findViewById(R.id.user_form_item_assess);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// 设置商品名称
			holder.goodsName.setText(mData.get(position).get("goodsName")
					.toString());
			// 设置店铺名称
			holder.shopName.setText(mData.get(position).get("shopName")
					.toString());
			// 设置商品数量
			holder.goodsCount.setText("数量:"
					+ mData.get(position).get("goodsCount").toString());
			// 设置商品价格
			holder.goodsPrice.setText("单价:"
					+ mData.get(position).get("goodsPrice").toString());

			holder.cancelOrder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserOrderFormActivity.this)
							.setTitle("确认退货？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											confirmOrder(mData.get(position)
													.get("orderItemId")
													.toString(), "-1", position);
											dialog.dismiss();
										}
									})
							.setNegativeButton("取消",
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
			});

			holder.confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserOrderFormActivity.this)
							.setTitle("确认收货？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											confirmOrder(mData.get(position)
													.get("orderItemId")
													.toString(), "3", position);
											dialog.dismiss();
										}
									})
							.setNegativeButton("取消",
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
			});
			holder.assess.setOnClickListener(new OnClickListener() {
				String assessStyle_str = "2";

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					LinearLayout ll = (LinearLayout) getLayoutInflater()
							.inflate(R.layout.user_order_form_assess_dialog,
									null);
					RadioGroup radioGroup = (RadioGroup) ll
							.findViewById(R.id.user_order_form_assess_dialog_radioGroup);
					radioGroup
							.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(RadioGroup arg0,
										int arg1) {
									// TODO Auto-generated method stub
									if (arg1 == R.id.user_order_form_assess_dialog_good) {
										assessStyle_str = "2";
									} else if (arg1 == R.id.user_order_form_assess_dialog_normal) {
										assessStyle_str = "1";
									} else {
										assessStyle_str = "0";
									}
								}
							});
					final EditText assessContentEditText = (EditText) ll
							.findViewById(R.id.user_order_form_assess_dialog_content);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserOrderFormActivity.this)
							.setTitle("评价")
							.setView(ll)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											assessOrder(
													mData.get(position)
															.get("customerId")
															.toString(),
													mData.get(position)
															.get("orderItemId")
															.toString(),
													mData.get(position)
															.get("goodsId")
															.toString(),
													assessStyle_str,
													assessContentEditText
															.getText()
															.toString(),
													position);

										}
									});
					;
					builder.create().show();
				}
			});
			if (type == 0) {
				holder.cancelOrder.setVisibility(View.VISIBLE);
				holder.confirm.setVisibility(View.GONE);
				holder.assess.setVisibility(View.GONE);
			} else if (type == 1) {
				holder.cancelOrder.setVisibility(View.GONE);
				holder.confirm.setVisibility(View.GONE);
				holder.assess.setVisibility(View.GONE);
			} else if (type == 2) {
				holder.cancelOrder.setVisibility(View.GONE);
				holder.confirm.setVisibility(View.VISIBLE);
				holder.assess.setVisibility(View.GONE);
			} else if (type == 3) {
				holder.cancelOrder.setVisibility(View.GONE);
				holder.confirm.setVisibility(View.GONE);
				holder.assess.setVisibility(View.VISIBLE);
			} else if (type == -1) {
				holder.cancelOrder.setVisibility(View.GONE);
				holder.confirm.setVisibility(View.GONE);
				holder.assess.setVisibility(View.GONE);
			}

			return convertView;
		}
	}

	/**
	 * ViewPager的适配器
	 * 
	 * @author zc
	 * 
	 */
	class MyViewPagerAdapter extends PagerAdapter {
		//
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(list.get(position));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return title.get(position);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(list.get(position));
			return list.get(position);
		}
	}
}