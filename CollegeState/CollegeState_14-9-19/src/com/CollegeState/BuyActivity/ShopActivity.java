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
import android.graphics.Bitmap;
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
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.Data.ShopInfoBean;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.JSONUtils;
import com.CollegeState.Util.Preferences;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 
 * @author F.Crazy 商家列表activity
 */
public class ShopActivity extends Activity {
	// 标题栏
	private ImageButton back;
	private TextView titleText;

	private Button home;// 首页
	private Button shopping_cart;// 购物车
	private Button mine;// 我的

	private RelativeLayout rl_shop_layout;
	private ListView shopListView_haoping;// list 按好评
	private ListView shopListView_xiaoliang;// list 按销量
	private List<Map<String, Object>> mData;// list中的数据
	private Intent intent;
	// 销量，好评的正在加载界面
	private TextView TextView_haoping;
	private TextView TextView_xiaoliang;
	// 表示当前页面，1代表按好评，2代表按销量
	private int currentPage = 1;
	// 静态变量，保存商户信息，方便之后的程序中调用
	public static List<ShopInfoBean> shopList = new ArrayList<ShopInfoBean>();

	// ViewPager的操作
	private ViewPager viewPager = null;
	// 滑动的每一页的标题
	private PagerTitleStrip pagerTitleStrip = null;
	// 表示装载滑动的布局
	private List<View> list = null;
	// 表示滑动每一页的标题
	private List<String> title = null;
	// 两个布局分别表示商家列表中的安好评排序和按销量排序
	private View view1, view2;
	// 自定义adapter
	private MyAdapter myAdapter = null;
	// 显示列表的类别
	private String type = "";

	// 与服务器通信的参数
	private String AREA_ID = "areaId";
	private String CITY_ID = "cityId";
	private String SERVICE_CATEGOTY = "serviceCategoty";// 商品类别
	private String NO_SHOP = "no_shop";// 没有商铺

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 获取数据成功
			if (msg.what == 0x123) {
				// 保存信息
				shopList = (List<ShopInfoBean>) msg.obj;
				mData = getData((List<ShopInfoBean>) msg.obj);

				shopList = sortListByType(1, shopList);
				mData = getData(shopList);
				// 加载完成时显示list
				changeView();

				myAdapter = new MyAdapter(ShopActivity.this);
				shopListView_haoping.setAdapter(myAdapter);
				shopListView_haoping
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								if (mData.get(arg2).get("shopState")
										.equals("营业")) {
									intent = new Intent();
									intent.putExtra("shopId", mData.get(arg2)
											.get("shopId") + "");
									intent.setClass(ShopActivity.this,
											MenuActivity.class);
									startActivity(intent);
								} else {
									Toast.makeText(getApplicationContext(),
											"抱歉，该店铺未营业", Toast.LENGTH_SHORT)
											.show();
								}
							}
						});

				shopList = sortListByType(2, shopList);
				mData = getData(shopList);
				myAdapter = new MyAdapter(ShopActivity.this);
				shopListView_xiaoliang.setAdapter(myAdapter);
				shopListView_xiaoliang
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								if (mData.get(arg2).get("shopState")
										.equals("营业")) {
									intent = new Intent();
									intent.putExtra("shopId", mData.get(arg2)
											.get("shopId") + "");
									intent.setClass(ShopActivity.this,
											MenuActivity.class);
									ShopActivity.this.startActivity(intent);
								} else {
									Toast.makeText(getApplicationContext(),
											"抱歉，该店铺未营业", Toast.LENGTH_SHORT)
											.show();
								}
							}
						});
			} else if (msg.what == 0x234) {
				// 加载完成时显示list
				changeView();
				Toast.makeText(ShopActivity.this, "暂无商铺", Toast.LENGTH_SHORT)
						.show();
			}
			// 服务器错误
			else if (msg.what == 0x999) {
				changeView();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ShopActivity.this)
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop);
		initActionbar();
		initViewPager();
		findView();
		setListener();
		intent = getIntent();
		// 接受要显示列表的种类
		type = intent.getStringExtra("type");

		// 通过HTTP请求获取List
		getShopList(type);
		addGuideImage();

	}

	public void addGuideImage() {
		if (Preferences.activityIsGuided(this, this.getClass().getName())) {
			return;
		}
		View view = getWindow().getDecorView()
				.findViewById(R.id.rl_shop_layout);// 查找通过setContentView上的根布局
		if (view == null)
			return;

		ViewParent viewParent = view.getParent();
		if (viewParent instanceof FrameLayout) {
			final FrameLayout frameLayout = (FrameLayout) viewParent;

			final ImageView guideImage = new ImageView(this);
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
			guideImage.setLayoutParams(params);
			guideImage.setScaleType(ScaleType.CENTER_CROP);
			guideImage.setImageResource(R.drawable.shop_guide);
			guideImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					frameLayout.removeView(guideImage);
					Preferences.setIsGuided(ShopActivity.this,
							ShopActivity.this.getClass().getName());
				}
			});
			frameLayout.addView(guideImage);// 添加引导图片

		}
	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.ViewPagerShop);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// 滑动到第一页，按好评排序
				if (arg0 == 0) {
					currentPage = 1;
					if (shopList.isEmpty())
						return;
					// 加载完成时显示list
					changeView();

					shopList = sortListByType(currentPage, shopList);
					mData = getData(shopList);
					if (myAdapter == null)
						return;
					myAdapter.refresh();
				}
				// 滑动到第二页，按销量排序
				else if (arg0 == 1) {
					currentPage = 2;
					if (shopList.isEmpty())
						return;
					// 加载完成时显示list
					changeView();

					shopList = sortListByType(currentPage, shopList);
					mData = getData(shopList);
					if (myAdapter == null)
						return;
					myAdapter.refresh();
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.PagerTitleShop);
		view1 = LayoutInflater.from(ShopActivity.this).inflate(
				R.layout.shop_haoping, null);
		view2 = LayoutInflater.from(ShopActivity.this).inflate(
				R.layout.shop_xiaoliang, null);
		list = new ArrayList<View>();
		list.add(view1);
		list.add(view2);
		title = new ArrayList<String>();
		title.add("按好评");
		title.add("按销量");
		pagerTitleStrip.setTextSize(1, 20);
		pagerTitleStrip.setTextColor(Color.rgb(90, 135, 12));
		pagerTitleStrip.setNonPrimaryAlpha(TRIM_MEMORY_BACKGROUND);
		viewPager.setAdapter(new MyViewPagerAdapter());
	}

	// 数据加载完成时，正在加载变成列表
	private void changeView() {
		TextView_haoping.setVisibility(View.GONE);
		TextView_xiaoliang.setVisibility(View.GONE);
		shopListView_haoping.setVisibility(View.VISIBLE);
		shopListView_xiaoliang.setVisibility(View.VISIBLE);
	}

	private void initActionbar() {
		// 自定义标题栏
		getActionBar().setDisplayShowHomeEnabled(false); // 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
		getActionBar().setDisplayShowTitleEnabled(false); // 对应ActionBar.DISPLAY_SHOW_TITLE。
		getActionBar().setDisplayShowCustomEnabled(true);// 使自定义的普通View能在title栏显示
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mTitleView = mInflater.inflate(R.layout.custom_action_bar_shop,
				null);
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
		titleText = (TextView) findViewById(R.id.shop_title_name);
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * List按照好评或者销量排序
	 * 
	 * @param type
	 *            1代表好评，2代表排序
	 * @param list
	 *            从服务器返回的list
	 */
	private List<ShopInfoBean> sortListByType(int type, List<ShopInfoBean> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = i; j < list.size(); j++) {
				ShopInfoBean infoI = list.get(i);
				ShopInfoBean infoJ = list.get(j);
				switch (type) {
				// 按照好评排序
				case 1:
					if (infoI.getShopAssess() < infoJ.getShopAssess()) {
						list.set(i, infoJ);
						list.set(j, infoI);
					}
					break;
				// 按照销量排序
				case 2:
					if (infoI.getShopSales() < infoJ.getShopSales()) {
						list.set(i, infoJ);
						list.set(j, infoI);
					}
					break;
				default:
					break;
				}
			}
		}
		return list;
	}

	// 设置view
	public void findView() {
		TextView_haoping = (TextView) view1
				.findViewById(R.id.shop_haoping_loading);
		TextView_xiaoliang = (TextView) view2
				.findViewById(R.id.shop_xiaoliang_loading);
		shopListView_haoping = (ListView) view1
				.findViewById(R.id.shop_haoping_list);
		shopListView_xiaoliang = (ListView) view2
				.findViewById(R.id.shop_xiaoliang_list);
		home = (Button) findViewById(R.id.shop_home);
		shopping_cart = (Button) findViewById(R.id.shop_shopping_cart);
		mine = (Button) findViewById(R.id.shop_wode);
	}

	// 设置监听
	public void setListener() {
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(ShopActivity.this, MainActivity.class);
				finish();
				ShopActivity.this.startActivity(intent);
			}
		});

		shopping_cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				intent = new Intent();
				intent.setClass(ShopActivity.this, ShoppingCartActivity.class);
				startActivity(intent);

			}
		});

		mine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.setClass(ShopActivity.this, MoreActivity.class);
				ShopActivity.this.startActivity(intent);
			}
		});
	}

	/**
	 * 通过Http请求获取List，该方法会开启新的线程 通过handler发送数据到主线程更新listview
	 * 
	 */
	private void getShopList(final String type) {
		new Thread() {
			public void run() {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				String result = null;
				if (type.equals("shop")) {
					params.add(new BasicNameValuePair(AREA_ID, UserInfoBean
							.getUserChooseAreaId()));
					result = HttpUtils.queryStringForPost(
							HttpUtils.SHOP_LIST_URL, params);
				} else if (type.equals("recommended")) {
					params.add(new BasicNameValuePair(CITY_ID, UserInfoBean
							.getCityId()));
					result = HttpUtils.queryStringForPost(
							HttpUtils.RECOMMENDED, params);

				} else {
					params.add(new BasicNameValuePair(SERVICE_CATEGOTY, type
							+ ""));
					params.add(new BasicNameValuePair(AREA_ID, UserInfoBean
							.getUserChooseAreaId()));
					result = HttpUtils.queryStringForPost(
							HttpUtils.USER_FANCY_URL, params);
				}
				Message msg = new Message();
				if (result.equals(NO_SHOP)) {
					msg.what = 0x234;
					handler.sendMessage(msg);
				} else if (TextUtils.isEmpty(result)) {
					handler.sendEmptyMessage(0x999);
				} else {
					List<ShopInfoBean> list = JSONUtils.getShopInfoList(result);
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
	public List<Map<String, Object>> getData(List<ShopInfoBean> shopList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (ShopInfoBean shopInfo : shopList) {
			map = new HashMap<String, Object>();
			map.put("shopName", shopInfo.getShopName());
			map.put("salesVolume", shopInfo.getShopSales());
			map.put("shopState", shopInfo.getShopState());
			// 以下数据用于排序
			map.put("shopAssess", shopInfo.getShopAssess());
			map.put("shopId", shopInfo.getShopId());
			map.put("businessId", shopInfo.getUserId());
			map.put("shopAddress", shopInfo.getShopAddress());
			map.put("shopImg", shopInfo.getShopImg());
			list.add(map);
		}
		return list;
	}

	// public final class ViewHolder {
	// public TextView shopName;// 店铺名称
	// public TextView salesVolume;// 月销量
	// public TextView shopState;// 店铺状态
	// public TextView shopAccess;// 店铺好评
	// public TextView shopAddress;// 店铺地址
	// public ImageView shopImage;//商户图片
	// }

	// 自定义listAdapter
	private class MyAdapter extends BaseAdapter {
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

		public View getView(int position, View convertView, ViewGroup parent) {
			if (mData != null) {
				LinearLayout ll;
				ll = (LinearLayout) mInflater.inflate(R.layout.shop_list_info,
						null);
				TextView shopName = (TextView) ll.findViewById(R.id.shop_name);
				TextView salesVolume = (TextView) ll
						.findViewById(R.id.shop_sales_volume);
				TextView shopState = (TextView) ll
						.findViewById(R.id.shop_state);
				TextView shopAccess = (TextView) ll
						.findViewById(R.id.shop_access);
				TextView shopAddress = (TextView) ll
						.findViewById(R.id.shop_address);
				TextView shopRecommend = (TextView) ll
						.findViewById(R.id.shop_recommend);
				final ImageView shopImage = (ImageView) ll
						.findViewById(R.id.shop_img);
				shopName.setText(mData.get(position).get("shopName").toString());
				salesVolume.setText("销量:"
						+ mData.get(position).get("salesVolume").toString());
				shopState.setText(mData.get(position).get("shopState")
						.toString());
				shopAccess.setText("好评:"
						+ mData.get(position).get("shopAssess").toString());
				shopAddress.setText(mData.get(position).get("shopAddress")
						.toString());

				if (type.equals("recommended")) {
					shopRecommend.setVisibility(View.VISIBLE);
				} else {
					shopRecommend.setVisibility(View.GONE);
				}

				String url = HttpUtils.IP_PREFIX
						+ mData.get(position).get("shopImg");
				// 显示图片的配置
				DisplayImageOptions options = new DisplayImageOptions.Builder()
						.cacheInMemory(true).cacheOnDisk(true)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
				ImageSize mImageSize = new ImageSize(60, 60);
				ImageLoader.getInstance().loadImage(url, mImageSize, options,
						new SimpleImageLoadingListener() {

							@Override
							public void onLoadingCancelled(String arg0,
									View arg1) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingComplete(String arg0,
									View arg1, Bitmap bitmap) {
								shopImage.setImageBitmap(bitmap);
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								// TODO Auto-generated method stub

							}

						});
				return ll;
			}
			return null;
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
