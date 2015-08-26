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
 * @author F.Crazy �̼��б�activity
 */
public class ShopActivity extends Activity {
	// ������
	private ImageButton back;
	private TextView titleText;

	private Button home;// ��ҳ
	private Button shopping_cart;// ���ﳵ
	private Button mine;// �ҵ�

	private RelativeLayout rl_shop_layout;
	private ListView shopListView_haoping;// list ������
	private ListView shopListView_xiaoliang;// list ������
	private List<Map<String, Object>> mData;// list�е�����
	private Intent intent;
	// ���������������ڼ��ؽ���
	private TextView TextView_haoping;
	private TextView TextView_xiaoliang;
	// ��ʾ��ǰҳ�棬1����������2��������
	private int currentPage = 1;
	// ��̬�����������̻���Ϣ������֮��ĳ����е���
	public static List<ShopInfoBean> shopList = new ArrayList<ShopInfoBean>();

	// ViewPager�Ĳ���
	private ViewPager viewPager = null;
	// ������ÿһҳ�ı���
	private PagerTitleStrip pagerTitleStrip = null;
	// ��ʾװ�ػ����Ĳ���
	private List<View> list = null;
	// ��ʾ����ÿһҳ�ı���
	private List<String> title = null;
	// �������ֱַ��ʾ�̼��б��еİ���������Ͱ���������
	private View view1, view2;
	// �Զ���adapter
	private MyAdapter myAdapter = null;
	// ��ʾ�б�����
	private String type = "";

	// �������ͨ�ŵĲ���
	private String AREA_ID = "areaId";
	private String CITY_ID = "cityId";
	private String SERVICE_CATEGOTY = "serviceCategoty";// ��Ʒ���
	private String NO_SHOP = "no_shop";// û������

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// ��ȡ���ݳɹ�
			if (msg.what == 0x123) {
				// ������Ϣ
				shopList = (List<ShopInfoBean>) msg.obj;
				mData = getData((List<ShopInfoBean>) msg.obj);

				shopList = sortListByType(1, shopList);
				mData = getData(shopList);
				// �������ʱ��ʾlist
				changeView();

				myAdapter = new MyAdapter(ShopActivity.this);
				shopListView_haoping.setAdapter(myAdapter);
				shopListView_haoping
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								if (mData.get(arg2).get("shopState")
										.equals("Ӫҵ")) {
									intent = new Intent();
									intent.putExtra("shopId", mData.get(arg2)
											.get("shopId") + "");
									intent.setClass(ShopActivity.this,
											MenuActivity.class);
									startActivity(intent);
								} else {
									Toast.makeText(getApplicationContext(),
											"��Ǹ���õ���δӪҵ", Toast.LENGTH_SHORT)
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
										.equals("Ӫҵ")) {
									intent = new Intent();
									intent.putExtra("shopId", mData.get(arg2)
											.get("shopId") + "");
									intent.setClass(ShopActivity.this,
											MenuActivity.class);
									ShopActivity.this.startActivity(intent);
								} else {
									Toast.makeText(getApplicationContext(),
											"��Ǹ���õ���δӪҵ", Toast.LENGTH_SHORT)
											.show();
								}
							}
						});
			} else if (msg.what == 0x234) {
				// �������ʱ��ʾlist
				changeView();
				Toast.makeText(ShopActivity.this, "��������", Toast.LENGTH_SHORT)
						.show();
			}
			// ����������
			else if (msg.what == 0x999) {
				changeView();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ShopActivity.this)
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop);
		initActionbar();
		initViewPager();
		findView();
		setListener();
		intent = getIntent();
		// ����Ҫ��ʾ�б������
		type = intent.getStringExtra("type");

		// ͨ��HTTP�����ȡList
		getShopList(type);
		addGuideImage();

	}

	public void addGuideImage() {
		if (Preferences.activityIsGuided(this, this.getClass().getName())) {
			return;
		}
		View view = getWindow().getDecorView()
				.findViewById(R.id.rl_shop_layout);// ����ͨ��setContentView�ϵĸ�����
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
			frameLayout.addView(guideImage);// �������ͼƬ

		}
	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.ViewPagerShop);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// ��������һҳ������������
				if (arg0 == 0) {
					currentPage = 1;
					if (shopList.isEmpty())
						return;
					// �������ʱ��ʾlist
					changeView();

					shopList = sortListByType(currentPage, shopList);
					mData = getData(shopList);
					if (myAdapter == null)
						return;
					myAdapter.refresh();
				}
				// �������ڶ�ҳ������������
				else if (arg0 == 1) {
					currentPage = 2;
					if (shopList.isEmpty())
						return;
					// �������ʱ��ʾlist
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
		title.add("������");
		title.add("������");
		pagerTitleStrip.setTextSize(1, 20);
		pagerTitleStrip.setTextColor(Color.rgb(90, 135, 12));
		pagerTitleStrip.setNonPrimaryAlpha(TRIM_MEMORY_BACKGROUND);
		viewPager.setAdapter(new MyViewPagerAdapter());
	}

	// ���ݼ������ʱ�����ڼ��ر���б�
	private void changeView() {
		TextView_haoping.setVisibility(View.GONE);
		TextView_xiaoliang.setVisibility(View.GONE);
		shopListView_haoping.setVisibility(View.VISIBLE);
		shopListView_xiaoliang.setVisibility(View.VISIBLE);
	}

	private void initActionbar() {
		// �Զ��������
		getActionBar().setDisplayShowHomeEnabled(false); // ʹ���Ͻ�ͼ���Ƿ���ʾ��������false����û�г���ͼ�꣬�����͸����⣬������ʾӦ�ó���ͼ��
		getActionBar().setDisplayShowTitleEnabled(false); // ��ӦActionBar.DISPLAY_SHOW_TITLE��
		getActionBar().setDisplayShowCustomEnabled(true);// ʹ�Զ������ͨView����title����ʾ
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
	 * List���պ���������������
	 * 
	 * @param type
	 *            1���������2��������
	 * @param list
	 *            �ӷ��������ص�list
	 */
	private List<ShopInfoBean> sortListByType(int type, List<ShopInfoBean> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = i; j < list.size(); j++) {
				ShopInfoBean infoI = list.get(i);
				ShopInfoBean infoJ = list.get(j);
				switch (type) {
				// ���պ�������
				case 1:
					if (infoI.getShopAssess() < infoJ.getShopAssess()) {
						list.set(i, infoJ);
						list.set(j, infoI);
					}
					break;
				// ������������
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

	// ����view
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

	// ���ü���
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
	 * ͨ��Http�����ȡList���÷����Ὺ���µ��߳� ͨ��handler�������ݵ����̸߳���listview
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
	 * �����������ص�listת��Ϊ�������listview��list<map<obj,obj>>
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
			// ����������������
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
	// public TextView shopName;// ��������
	// public TextView salesVolume;// ������
	// public TextView shopState;// ����״̬
	// public TextView shopAccess;// ���̺���
	// public TextView shopAddress;// ���̵�ַ
	// public ImageView shopImage;//�̻�ͼƬ
	// }

	// �Զ���listAdapter
	private class MyAdapter extends BaseAdapter {
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
				salesVolume.setText("����:"
						+ mData.get(position).get("salesVolume").toString());
				shopState.setText(mData.get(position).get("shopState")
						.toString());
				shopAccess.setText("����:"
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
				// ��ʾͼƬ������
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
	 * ViewPager��������
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
