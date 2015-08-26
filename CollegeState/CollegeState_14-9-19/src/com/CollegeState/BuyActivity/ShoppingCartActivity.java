package com.CollegeState.BuyActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.Data.OrderItem;
import com.CollegeState.Data.OrderItem.OrderBean;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.UserActivity.UserLoginActivity;
import com.CollegeState.UserActivity.UserRegisterActivity;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Widget.SwipeListView;

/**
 * 购物车activity
 * 
 * @author zc 2014-5-25 - 0:14
 */
public class ShoppingCartActivity extends Activity {
	private Button home;// 首页
	private Button shop;// 餐店
	private Button mine;// 我的
	// 标题栏
	private ImageButton back;
	private TextView titleText;
	// 提交订单
	private Button submitButton = null;
	// 用于填充listview的data
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();;
	private SwipeListView orderListView = null;
	// 程序范围内单例的数据
	private List<OrderBean> originalData;
	private MyAdapter adapter;
	//
	private int mRightWidth = 0;
	// 用于填充对话框的Adapter
	private ArrayAdapter<String> inShopAdapter;
	private ArrayAdapter<String> peiSongAdapter;
	private String[] inShopArray;
	private String[] peiSongArray;

	private Intent intent = null;

	// 与服务器通信的参数
	private String CART_LIST_STR = "cartListStr"; // 提交的商品详情
	private String USER_ID = "userId";// 用户ID
	private String AREA_ID = "areaId"; // 订单地址大范围
	private String ORDER_ADDRESS = "orderAddress";// 订单地址精确范围
	private String ORDER_PHONE = "orderPhone";// 订购者电话
	private String ORDER_JINDIAN_TIME = "orderJindianTime";// 进店时间
	private String ORDER_PEISONG_TIME = "orderPeisongTime";// 配送时间
	private String ORDER_WAY = "orderWay";// 订购方式
	private String ACTION = "action";// 校验

	private String SUBMIT_OK = "submit_ok";
	private String GOODS_IS_NOT_PEISONG = "goods_is_not_isPeisong";
	private String ORDER_BEFORE = "please_order_before_30minutes";
	private String ACTION_ERROR = "action_error";
	private String DATABASE_ERROR = "database_error";
	private String SUBMIT_ERROR = "submit_error";

	// 提交订单的进度条对话框
	private ProgressDialog submitOrderProgressDialog;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			// create order filed!(生成订单失败)
			case 0x123:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder0 = new AlertDialog.Builder(
						ShoppingCartActivity.this)
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
			// Submit Success! 提交成功
			case 0x234:
				submitOrderProgressDialog.dismiss();
				boolean flag = OrderItem.clearAllData();
				System.out.println("删除购物车数据--->" + flag);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ShoppingCartActivity.this)
						.setTitle("校帮")
						.setMessage("提交订单成功！")
						.setPositiveButton("再次点餐",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Intent intent = new Intent();
										intent.putExtra("type", "shop");

										intent.setClass(
												ShoppingCartActivity.this,
												ShopActivity.class);
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
										adapter.refresh();
									}
								});
				builder.create().show();
				break;
			case 0x235:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder4 = new AlertDialog.Builder(
						ShoppingCartActivity.this)
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
				builder4.create().show();
				break;
			case 0x236:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder5 = new AlertDialog.Builder(
						ShoppingCartActivity.this)
						.setTitle("校帮")
						.setMessage("数据库错误！")
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
			case 0x237:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder6 = new AlertDialog.Builder(
						ShoppingCartActivity.this)
						.setTitle("校帮")
						.setMessage("此服务暂未开通！")
						.setPositiveButton("稍后再试",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder6.create().show();
				break;
			case 0x321:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder3 = new AlertDialog.Builder(
						ShoppingCartActivity.this)
						.setTitle("校帮")
						.setMessage("必须在选择的送餐时间段提前半小时订餐！")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder3.create().show();
				break;
			// 网络错误
			case 0x345:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder2 = new AlertDialog.Builder(
						ShoppingCartActivity.this)
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
			case 0x456:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder7 = new AlertDialog.Builder(
						ShoppingCartActivity.this)
						.setTitle("校帮")
						.setMessage("某些商品不支持配送")
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder7.create().show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping_cart);
		adapter = new MyAdapter(this);
		findView();
		initActionbar();
		inflateData(this);
		setListener();
	}

	private void findView() {
		home = (Button) findViewById(R.id.shopping_cart_meal_home);
		shop = (Button) findViewById(R.id.shopping_cart_meal_candian);
		mine = (Button) findViewById(R.id.shopping_cart_meal_wode);
		submitButton = (Button) findViewById(R.id.ButtonShoppingCartSubmitAll);
		orderListView = (SwipeListView) findViewById(R.id.ListViewShoppingCart);
		mRightWidth = orderListView.getRightViewWidth();
	}

	private void setListener() {
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(ShoppingCartActivity.this, MainActivity.class);
				finish();
				ShoppingCartActivity.this.startActivity(intent);
			}
		});

		shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.putExtra("type", "shop");
				intent.setClass(ShoppingCartActivity.this, ShopActivity.class);
				ShoppingCartActivity.this.startActivity(intent);
			}
		});

		mine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(ShoppingCartActivity.this, MoreActivity.class);
				ShoppingCartActivity.this.startActivity(intent);
			}
		});

		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 当购物车为空时
				if (OrderItem.getOrderItems() == null
						|| OrderItem.getOrderItems().size() == 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ShoppingCartActivity.this)
							.setTitle("温馨提示")
							.setMessage("购物车内容为空!")
							.setPositiveButton("去点餐",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent();
											intent.putExtra("type", "shop");
											intent.setClass(
													ShoppingCartActivity.this,
													ShopActivity.class);
											intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(intent);

										}
									})
							.setNegativeButton("返回",
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
				// 检测用户登录状态
				if (!UserInfoBean.isLogin()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ShoppingCartActivity.this)
							.setTitle("温馨提示")
							.setMessage("您还没有登录!")
							.setPositiveButton("登录",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent();
											intent.setClass(
													ShoppingCartActivity.this,
													UserLoginActivity.class);
											intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(intent);

										}
									})
							.setNegativeButton("返回",
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
				// 判断网络状态
				isNetWorkEnable();
				// 弹出订餐信息对话框
				LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(
						R.layout.shopping_cart_dialog, null);

				// 总价
				TextView totleTv = (TextView) ll
						.findViewById(R.id.TextViewShoppingCartDiaTotlePrice);
				// 电话
				final EditText phoneEditText = (EditText) ll
						.findViewById(R.id.EditTextSubmitGoodsDialogPhone);

				// 地址
				final EditText ApartIDEditText = (EditText) ll
						.findViewById(R.id.EidtTextShoppingCartDialogApartID);
				final EditText addressEditText = (EditText) ll
						.findViewById(R.id.EditTextSubmitGoodsDialogAddress);
				// 时间
				final Spinner timeSpinner = (Spinner) ll
						.findViewById(R.id.SpinnerOrderTime);
				initSpinnerAdapter(ll);
				timeSpinner.setAdapter(inShopAdapter);
				// 进店或配送
				String peisong = "";
				final RadioButton inShopRadioButton = (RadioButton) ll
						.findViewById(R.id.RadioButtonShoppingCartDialogInShop);
				final RadioButton peiSongRadioButton = (RadioButton) ll
						.findViewById(R.id.RadioButtonShoppingCartDialogPeiSong);
				inShopRadioButton.setVisibility(View.GONE);

				inShopRadioButton
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked == true) {
									timeSpinner.setAdapter(inShopAdapter);
								} else {
									timeSpinner.setAdapter(peiSongAdapter);
								}
							}
						});
				peiSongRadioButton.setChecked(true);
				// 菜单总价
				double totlePrice = 0;
				for (OrderBean bean : OrderItem.getOrderItems()) {
					totlePrice += bean.getOrderItem().getGoodsPrice()
							* bean.getCount();
				}
				// 运费
				double freight = 0;

				totleTv.setText(totlePrice + "");
				phoneEditText.setText(UserInfoBean.getUserPhone());
				// 分割地址并显示
				String address_a[] = UserInfoBean.getOrderAddress().split("#");
				if (address_a.length == 0) {
					ApartIDEditText.setText("");
					addressEditText.setText("");
				} else if (address_a.length == 1) {
					if (address_a[0].equals("null"))
						ApartIDEditText.setText("");
					else
						ApartIDEditText.setText(address_a[0]);
					addressEditText.setText("");
				} else {
					ApartIDEditText.setText(address_a[0]);
					if (address_a[1].equals("null"))
						addressEditText.setText("");
					else
						addressEditText.setText(address_a[1]);
				}

				if (inShopRadioButton.isChecked()) {
					peisong = "配送";
				} else {
					peisong = "进店";
				}
				// 提交订单的对话框
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ShoppingCartActivity.this)
						.setTitle("确认信息")
						.setView(ll)
						.setPositiveButton("订餐",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 购物车信息
										String cartListStr = createOrderItemString();
										// 用户ID
										String customerId = UserInfoBean
												.getUserId();
										// 订单地址
										String orderAddress = ApartIDEditText
												.getText().toString()
												+ "#"
												+ addressEditText.getText()
														.toString();
										// 订购者电话
										String orderPhone = phoneEditText
												.getText().toString();
										if (!isPhoneNumberIllegal(orderPhone)) {
											Toast.makeText(
													ShoppingCartActivity.this,
													"电话号码不正确",
													Toast.LENGTH_SHORT).show();
											return;
										}
										// 订购方式
										String orderWay = inShopRadioButton
												.isChecked() ? 1 + "" : 0 + "";
										if (orderWay.equals("0")) {
											if (ApartIDEditText.getText()
													.toString().equals("")
													|| addressEditText
															.getText()
															.toString()
															.equals("")) {
												Toast.makeText(
														ShoppingCartActivity.this,
														"请填写完整地址",
														Toast.LENGTH_SHORT)
														.show();
												return;
											}
										}
										// 进店或者配送时间
										final String orderTime = timeSpinner
												.getSelectedItem().toString();
										// 提交http请求

										final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
										params.add(new BasicNameValuePair(
												CART_LIST_STR, cartListStr));
										params.add(new BasicNameValuePair(
												USER_ID, customerId));
										params.add(new BasicNameValuePair(
												AREA_ID, UserInfoBean
														.getUserChooseAreaId()));
										params.add(new BasicNameValuePair(
												ORDER_ADDRESS, orderAddress));
										params.add(new BasicNameValuePair(
												ORDER_PHONE, orderPhone));
										params.add(new BasicNameValuePair(
												ACTION, UserInfoBean
														.getAction()));
										if (inShopRadioButton.isChecked()) {
											params.add(new BasicNameValuePair(
													ORDER_JINDIAN_TIME,
													orderTime));
										} else {
											params.add(new BasicNameValuePair(
													ORDER_PEISONG_TIME,
													orderTime));
										}
										params.add(new BasicNameValuePair(
												ORDER_WAY, orderWay));
										submitOrderProgressDialog = new ProgressDialog(
												ShoppingCartActivity.this);
										submitOrderProgressDialog
												.setTitle("正在提交订单...");
										submitOrderProgressDialog
												.setProgressStyle(ProgressDialog.STYLE_SPINNER);
										submitOrderProgressDialog
												.setCancelable(true);
										submitOrderProgressDialog.show();
										new Thread() {
											public void run() {
												String result = HttpUtils
														.queryStringForPost(
																HttpUtils.USER_SUBMIT_ORDER,
																params);
												System.out.println(inShopRadioButton
														.isChecked()
														+ "in"
														+ orderTime + "");
												if (result == null) {
													handler.sendEmptyMessage(0x123);
												} else if (result
														.equals(SUBMIT_OK)) {
													handler.sendEmptyMessage(0x234);
												} else if (result
														.equals(ACTION_ERROR)) {
													handler.sendEmptyMessage(0x235);
												} else if (result
														.equals(DATABASE_ERROR)) {
													handler.sendEmptyMessage(0x236);
												} else if (result
														.equals(ORDER_BEFORE)) {
													handler.sendEmptyMessage(0x321);
												} else if (result
														.equals(HttpUtils.NETWORK_ERROR)) {
													handler.sendEmptyMessage(0x345);
												} else if (result
														.equals(GOODS_IS_NOT_PEISONG)) {
													handler.sendEmptyMessage(0x456);
												} else if (result
														.equals(SUBMIT_ERROR)) {
													handler.sendEmptyMessage(0x237);
												} else {
													handler.sendEmptyMessage(0x123);
												}
											};
										}.start();
									}
								});
				builder.create().show();
			}
		});
	}

	/**
	 * 初始化填充对话框的spinner的adapter
	 * 
	 * @param ll传入的view
	 *            ，用于获取context对象
	 */
	public void initSpinnerAdapter(LinearLayout ll) {
		inShopArray = new String[] { "11:00-11:30", "11:30-12:00",
				"12:00-12:30", "12:30-13:00", "17:30-18:00", "18:00-18:30",
				"18:30-19:00" };
		peiSongArray = new String[] { "08:20-09:00", "12:00-12:40",
				"18:20-19:00" };
		inShopAdapter = new ArrayAdapter<String>(ll.getContext(),
				android.R.layout.simple_spinner_item, inShopArray);
		peiSongAdapter = new ArrayAdapter<String>(ll.getContext(),
				android.R.layout.simple_spinner_item, peiSongArray);

	}

	/**
	 * 判断电话号码是否合法
	 * 
	 * @return
	 */
	private boolean isPhoneNumberIllegal(String phone) {
		Pattern regex = Pattern.compile(UserRegisterActivity.regExPhoneNumber);
		Matcher matcher = regex.matcher(phone);
		return matcher.matches();
	}

	/**
	 * 将购物车单例的数据转化为可填充的数据mdata
	 * 
	 * @param context
	 */
	private void inflateData(Context context) {
		mData = getDataFromLocal();
		if (mData != null) {
			if (mData.size() != 0) {

				orderListView.setAdapter(adapter);
				adapter.refresh();
			}
		}

	}

	/**
	 * 创建http请求的字符串： [{"goodsId":1,
	 * "goodsCount":10,"goodsMore":"haochi"},{"goodsId":1,
	 * "goodsCount":10,"goodsMore":"haochi"}] 两个菜之间要添加逗号
	 * 
	 * @return
	 */
	public String createOrderItemString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		final String goodsId = "\"goodsId\":";
		final String goodsCount = "\"goodsCount\":";
		final String goodsMore = "\"goodsMore\":";
		final String quotation = "\"";
		for (OrderBean bean : OrderItem.getOrderItems()) {
			sb.append("{");
			sb.append(goodsId);
			sb.append(bean.getOrderItem().getGoodsId() + ",");
			sb.append(goodsCount);
			sb.append(bean.getCount() + ",");
			sb.append(goodsMore);
			if (bean.getGoodsMore() == null || bean.getGoodsMore().equals("")) {
				sb.append(quotation + quotation + "},");
			} else {
				sb.append(quotation + bean.getGoodsMore() + quotation + "},");
			}
		}
		sb = new StringBuilder(sb.toString().substring(0, sb.length() - 1));
		sb.append("]");
		System.out.println("cartListStr ==== " + sb.toString());
		return sb.toString();
	}

	/**
	 * 检测网络状态
	 * 
	 */
	public void isNetWorkEnable() {
		if (!HttpUtils.isNetWorkEnable(this)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ShoppingCartActivity.this)
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

	// 从本地获取购物车数据
	public List<Map<String, Object>> getDataFromLocal() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		originalData = OrderItem.getOrderItems();
		if (originalData.size() == 0) {
			return null;
		} else {
			for (OrderBean bean : originalData) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("goodsName", bean.getOrderItem().getGoodsName());
				map.put("goodsPrice", "单价:"
						+ bean.getOrderItem().getGoodsPrice());
				map.put("shopId", bean.getOrderItem().getShopId());
				map.put("itemCount", bean.getCount());
				// 添加这个用于删除
				map.put("goodsId", bean.getOrderItem().getGoodsId());
				list.add(map);
			}
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
		titleText.setText("购物车");
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
		public TextView shopName;// 商户名称
		public TextView orderCount;// 订货数量
		public TextView price;// 商品单价
		public Button addButton;// 增加数量
		public Button subButton;// 减少
		public Button del;// 删除
		public RelativeLayout item_right;// 动态效果的layout
		public Button remarkButton;// 备注按钮
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
			final ViewHolder holder;
			if (mData != null) {// list不为空的情况下
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = mInflater.inflate(
							R.layout.shopping_cart_list_item, parent, false);

					// findView
					holder.shopName = (TextView) convertView
							.findViewById(R.id.shopping_cart_shop_name);
					holder.goodsName = (TextView) convertView
							.findViewById(R.id.shopping_cart_item_name);
					holder.orderCount = (TextView) convertView
							.findViewById(R.id.shopping_cart_item_count);
					holder.price = (TextView) convertView
							.findViewById(R.id.shopping_cart_item_price);
					holder.addButton = (Button) convertView
							.findViewById(R.id.shopping_cart_add_count);
					holder.subButton = (Button) convertView
							.findViewById(R.id.shopping_cart_sub_count);
					holder.del = (Button) convertView
							.findViewById(R.id.shopping_cart_item_del);
					holder.item_right = (RelativeLayout) convertView
							.findViewById(R.id.shopping_cart_item_layouot_right);
					holder.remarkButton = (Button) convertView
							.findViewById(R.id.shopping_cart_remark);

					holder.remarkButton
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									LinearLayout ll = (LinearLayout) getLayoutInflater()
											.inflate(
													R.layout.shopping_cart_item_dialog,
													null);
									TextView nameTv = (TextView) ll
											.findViewById(R.id.TextViewShoppingCartItemName);
									TextView countTv = (TextView) ll
											.findViewById(R.id.TextViewShoppingCartItemCount);
									TextView totleTv = (TextView) ll
											.findViewById(R.id.TextViewShoppingCartItemTotlePrice);
									final EditText et = (EditText) ll
											.findViewById(R.id.EidtTextShoppingCartItemMore);

									nameTv.setText((CharSequence) mData.get(
											position).get("goodsName"));
									countTv.setText(mData.get(position).get(
											"itemCount")
											+ "");
									final int goodsID = (Integer) mData.get(
											position).get("goodsId");
									int count = (Integer) mData.get(position)
											.get("itemCount");
									String reg = "[\u4e00-\u9fa5]";
									Pattern pat = Pattern.compile(reg);
									Matcher mat = pat.matcher((String) mData
											.get(position).get("goodsPrice"));
									String repickStr = mat.replaceAll("");
									repickStr = repickStr.substring(1,
											repickStr.length());
									float price = Float
											.valueOf((String) repickStr);
									totleTv.setText(count * price + "");
									if (OrderItem.getOrderItems().get(position)
											.getGoodsMore() == null) {
										et.setText("");
									} else {
										et.setText(OrderItem.getOrderItems()
												.get(position).getGoodsMore()
												+ "");
									}

									AlertDialog.Builder builder = new AlertDialog.Builder(
											ShoppingCartActivity.this)
											.setTitle("修改备注信息")
											.setView(ll)
											.setPositiveButton(
													"添加备注",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															String goodsMore = et
																	.getText()
																	.toString();
															OrderItem
																	.modifyGoodsMore(
																			goodsID,
																			goodsMore);
															Toast.makeText(
																	ShoppingCartActivity.this,
																	"添加成功",
																	2000)
																	.show();
														}
													});
									builder.create().show();

								}
							});

					holder.addButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							int count = (Integer) mData.get(position).get(
									"itemCount");
							count = count + 1;
							OrderItem.getOrderItems().get(position)
									.setCount(count);
							inflateData(ShoppingCartActivity.this);
							holder.orderCount.setText(String.valueOf(count)
									+ "");
						}
					});
					holder.subButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							int count = (Integer) mData.get(position).get(
									"itemCount");
							if (count < 1) {
								return;
							} else if (count == 1) {
								System.out.println("count == 1");
								boolean flag = OrderItem
										.deleteItem((Integer) mData.get(
												position).get("goodsId"));
								Toast.makeText(ShoppingCartActivity.this,
										"删除成功", 2000).show();
								mData.remove(position);
								notifyDataSetChanged();
								return;
							} else {
								count = count - 1;
							}
							OrderItem.getOrderItems().get(position)
									.setCount(count);
							inflateData(ShoppingCartActivity.this);
							holder.orderCount.setText(String.valueOf(count)
									+ "");
						}
					});

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				// 设置商品名称
				holder.goodsName.setText(""
						+ mData.get(position).get("goodsName"));
				// holder.shopName.setText("" +
				// mData.get(position).get("shopId"));
				holder.price
						.setText("" + mData.get(position).get("goodsPrice"));
				holder.orderCount.setText(""
						+ mData.get(position).get("itemCount"));
				LayoutParams lp2 = new LayoutParams(mRightWidth,
						LayoutParams.MATCH_PARENT);
				holder.item_right.setLayoutParams(lp2);
				holder.del.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						System.out.println(mData.get(position).get("goodsId"));
						boolean flag = OrderItem.deleteItem((Integer) mData
								.get(position).get("goodsId"));
						Toast.makeText(ShoppingCartActivity.this, "删除成功", 2000)
								.show();
						mData.remove(position);
						notifyDataSetChanged();

						// 删除后隐藏删除按钮
						orderListView.hiddenDel();
					}
				});
			}
			return convertView;
		}

	}
}
