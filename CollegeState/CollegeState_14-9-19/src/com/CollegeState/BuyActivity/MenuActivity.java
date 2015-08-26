package com.CollegeState.BuyActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.Data.GoodsInfoBean;
import com.CollegeState.Data.OrderItem;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.JSONUtils;

/**
 * 使用ViewPager需要导入包android-support-v4.jar
 * 
 */
public class MenuActivity extends Activity {
	// 标题栏
	private ImageButton back;
	private TextView titleText;

	private Button home;// 首页
	private Button shop;// 餐店
	private Button shopping_cart;// 购物车
	private Button mine;// 我的

	// checkBox状态
	private boolean[] checkBoxSta = null;
	// listview的adapter
	MyAdapter myAdapter = null;
	// 保存菜单信息（从服务器上获取的原始信息）
	public static List<GoodsInfoBean> menuList = new ArrayList<GoodsInfoBean>();
	// 从服务器获取的原始信息转换之后的数据
	private List<Map<String, Object>> originalData;
	// 要显示的data
	private List<Map<String, Object>> mData;
	private Button order;// 立即订单按钮
	private Intent intent;
	// ShopActivity返回的商户id
	private String shopId = "";
	// ViewPager的操作
	private ViewPager viewPager = null;
	// 滑动的每一页的标题
	private PagerTitleStrip pagerTitleStrip = null;
	// 表示装载滑动的布局
	private List<View> list = null;
	// 表示滑动每一页的标题
	private List<String> title = null;
	// 布局分别表示菜单中的全部、素菜、荤菜、其他
	private View view_all, view_sucai, view_huncai, view_qita;
	// list分别表示菜单中的全部、素菜、荤菜、其他
	private ListView menuListView_all;
	private ListView menuListView_sucai;
	private ListView menuListView_huncai;
	private ListView menuListView_qita;
	// 销量，好评的正在加载界面
	private TextView TextView_all;
	private TextView TextView_sucai;
	private TextView TextView_huncai;
	private TextView TextView_qita;
	// 保存当前页面的值，第一次启动activity时为0
	private int currentPage = 0;

	// 与服务器通信的参数
	private String SHOP_ID = "shopId";
	private String NO_GOODS = "no_goods";// 没有商品

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				// 返服务器的菜单数据
				menuList = (List<GoodsInfoBean>) msg.obj;
				checkBoxSta = new boolean[menuList.size()];
				originalData = getData((List<GoodsInfoBean>) msg.obj);
				mData = originalData;
				changeView();
				myAdapter = new MyAdapter(MenuActivity.this);
				menuListView_all.setAdapter(myAdapter);
				myAdapter.refresh();
				menuListView_all
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// 在每次获取点击的item时改变checkBox状态并且刷新litview
								checkBoxSta[arg2] = !checkBoxSta[arg2];
								myAdapter.refresh();
							}
						});
			} else if (msg.what == 0x234) {
				Toast.makeText(MenuActivity.this, "暂无商品", Toast.LENGTH_SHORT)
						.show();
				changeView();
			}// 服务器错误
			else if (msg.what == 0x999) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MenuActivity.this)
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

	public void findView() {
		menuListView_all = (ListView) view_all.findViewById(R.id.menu_list_all);
		menuListView_sucai = (ListView) view_sucai
				.findViewById(R.id.menu_list_sucai);
		menuListView_huncai = (ListView) view_huncai
				.findViewById(R.id.menu_list_huncai);
		menuListView_qita = (ListView) view_qita
				.findViewById(R.id.menu_list_qita);
		TextView_all = (TextView) view_all.findViewById(R.id.menu_all_loading);
		TextView_sucai = (TextView) view_sucai
				.findViewById(R.id.menu_sucai_loading);
		TextView_huncai = (TextView) view_huncai
				.findViewById(R.id.menu_huncai_loading);
		TextView_qita = (TextView) view_qita
				.findViewById(R.id.menu_qita_loading);
		order = (Button) findViewById(R.id.menu_order);

		home = (Button) findViewById(R.id.menu_home);
		shop = (Button) findViewById(R.id.menu_candian);
		shopping_cart = (Button) findViewById(R.id.menu_shopping_cart);
		mine = (Button) findViewById(R.id.menu_wode);
	}

	public void setListener() {
		order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean isAdded = false;
				for (int i = 0; i < mData.size(); i++) {
					if (checkBoxSta[i] == true) {
						int id = (Integer) mData.get(i).get("goodsId");
						for (GoodsInfoBean info : menuList) {
							if (info.getGoodsId() == id) {
								// 程序中单例添加商品
								OrderItem.addItem(info);
								isAdded = true;
							}
						}
					}
				}
				if (isAdded == true) {
					Intent intent = new Intent();
					intent.setClass(MenuActivity.this,
							ShoppingCartActivity.class);
					startActivity(intent);
				}

			}
		});

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(MenuActivity.this, MainActivity.class);
				finish();
				MenuActivity.this.startActivity(intent);
			}
		});

		shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.putExtra("type", "shop");
				intent.setClass(MenuActivity.this, ShopActivity.class);
				MenuActivity.this.startActivity(intent);
			}
		});

		shopping_cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				intent = new Intent();
				intent.setClass(MenuActivity.this, ShoppingCartActivity.class);
				startActivity(intent);

			}
		});

		mine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.setClass(MenuActivity.this, MoreActivity.class);
				MenuActivity.this.startActivity(intent);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		initActionbar();
		initViewPager();
		findView();
		setListener();

		Intent mIntent = getIntent();
		Bundle bundle = mIntent.getExtras();
		shopId = bundle.getString("shopId");
		if (!HttpUtils.isNetWorkEnable(this)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MenuActivity.this)
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
		getItemsList();
	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.ViewPagerMenu);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (mData == null) {
					return;
				}
				switch (arg0) {
				// 显示全部商品
				case 0:
					currentPage = 0;
					mData = getDataByPage(0);
					if (mData.isEmpty()) {
						return;
					}
					changeView();
					checkBoxSta = new boolean[mData.size()];
					myAdapter.refresh();
					break;
				// 显示
				case 1:
					currentPage = 1;
					mData = getDataByPage(1);
					menuListView_sucai.setAdapter(myAdapter);
					if (mData.isEmpty()) {
						return;
					}
					changeView();
					checkBoxSta = new boolean[mData.size()];
					menuListView_sucai
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// 在每次获取点击的item时改变checkBox状态并且刷新litview
									checkBoxSta[arg2] = !checkBoxSta[arg2];
									myAdapter.refresh();
								}

							});
					myAdapter.refresh();
					break;
				case 2:
					currentPage = 2;
					mData = getDataByPage(2);
					if (mData.isEmpty()) {
						return;
					}
					changeView();
					checkBoxSta = new boolean[mData.size()];
					menuListView_huncai.setAdapter(myAdapter);
					menuListView_huncai
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// 在每次获取点击的item时改变checkBox状态并且刷新litview
									checkBoxSta[arg2] = !checkBoxSta[arg2];
									myAdapter.refresh();
								}
							});
					myAdapter.refresh();
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
		pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.PagerTitleMenu);
		view_all = LayoutInflater.from(MenuActivity.this).inflate(
				R.layout.menu_all, null);
		view_sucai = LayoutInflater.from(MenuActivity.this).inflate(
				R.layout.menu_su_cai, null);
		view_huncai = LayoutInflater.from(MenuActivity.this).inflate(
				R.layout.menu_hun_cai, null);
		view_qita = LayoutInflater.from(MenuActivity.this).inflate(
				R.layout.menu_qi_ta, null);
		list = new ArrayList<View>();
		list.add(view_all);
		list.add(view_sucai);
		list.add(view_huncai);
		title = new ArrayList<String>();
		title.add("全部");
		title.add("荤菜");
		title.add("素菜");
		title.add("其他");
		pagerTitleStrip.setTextColor(Color.rgb(90, 135, 12));
		pagerTitleStrip.setTextSize(1, 20);
		viewPager.setAdapter(new MyViewPagerAdapter());
	}

	/**
	 * 通过Http请求获取List，该方法会开启新的线程 通过handler发送数据到主线程更新listview
	 * 
	 */
	private void getItemsList() {
		new Thread() {
			public void run() {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(SHOP_ID, shopId));
				String result = HttpUtils.queryStringForPost(
						HttpUtils.MENU_ITEM_URL, params);
				List<GoodsInfoBean> list = JSONUtils.getSearchInfoList(result);
				Message msg = new Message();
				if (result.equals(NO_GOODS)) {
					msg.what = 0x234;
					handler.sendMessage(msg);
				} else if (TextUtils.isEmpty(result)) {
					handler.sendEmptyMessage(0x999);
				} else {
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
	public List<Map<String, Object>> getData(List<GoodsInfoBean> itemsList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (GoodsInfoBean item : itemsList) {
			map = new HashMap<String, Object>();
			map.put("goodsId", item.getGoodsId());
			map.put("goodsName", item.getGoodsName());
			map.put("canDistribution", item.getIsPeisong());
			map.put("price", item.getGoodsPrice() + "");
			map.put("goodCategory", item.getGoodsCategory());
			list.add(map);
		}
		return list;
	}

	/**
	 * 根据页数获取要显示的商品
	 * 
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getDataByPage(int page) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		//1表示荤菜，2表示素菜，3表示其他
		String types[] = new String[] { "1", "2", "3" };
		String compareStr = "";
		if (page == 0) {
			return originalData;
		}
		switch (page) {
		case 1:
			compareStr = types[0];
			break;
		case 2:
			compareStr = types[1];
			break;
		case 3:
			compareStr = types[2];
			break;

		default:
			break;
		}
		if (originalData == null) {
			return null;
		}
		for (Map map : originalData) {
			if (map.get("goodCategory").equals(compareStr)) {
				list.add(map);
			}
		}
		return list;
	}

	final class ViewHolder {

		public TextView goodsName;// 商品名称
		public TextView canDistribution;// 是否可配送
		public CheckBox check;// 商品是否选中
		public TextView price;// 商品价格
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
					convertView = mInflater.inflate(R.layout.menu_list_info,
							null);
					// findView
					holder.goodsName = (TextView) convertView
							.findViewById(R.id.menu_goods_name);
					holder.canDistribution = (TextView) convertView
							.findViewById(R.id.menu_distribution);
					holder.check = (CheckBox) convertView
							.findViewById(R.id.menu_list_checkBox);
					holder.price = (TextView) convertView
							.findViewById(R.id.menu_list_price);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				// 设置商品名称
				holder.goodsName.setText(mData.get(position).get("goodsName")
						.toString());
				// 设置商品是否可配送
				if (mData.get(position).get("canDistribution").toString()
						.equals("1"))
					holder.canDistribution.setText("可配送");
				else
					holder.canDistribution.setText("不配送");
				// 设置商品价格
				holder.price.setText("￥"
						+ mData.get(position).get("price").toString());
				// 设置商品是否被勾选
				holder.check.setChecked(checkBoxSta[position]);
			}
			return convertView;
		}

	}

	// 数据加载完成时，正在加载变成列表
	private void changeView() {
		TextView_all.setVisibility(View.GONE);
		TextView_sucai.setVisibility(View.GONE);
		TextView_huncai.setVisibility(View.GONE);
		menuListView_all.setVisibility(View.VISIBLE);
		menuListView_sucai.setVisibility(View.VISIBLE);
		menuListView_huncai.setVisibility(View.VISIBLE);
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
		titleText.setText("菜单");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

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
