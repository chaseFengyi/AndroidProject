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
 * ʹ��ViewPager��Ҫ�����android-support-v4.jar
 * 
 */
public class MenuActivity extends Activity {
	// ������
	private ImageButton back;
	private TextView titleText;

	private Button home;// ��ҳ
	private Button shop;// �͵�
	private Button shopping_cart;// ���ﳵ
	private Button mine;// �ҵ�

	// checkBox״̬
	private boolean[] checkBoxSta = null;
	// listview��adapter
	MyAdapter myAdapter = null;
	// ����˵���Ϣ���ӷ������ϻ�ȡ��ԭʼ��Ϣ��
	public static List<GoodsInfoBean> menuList = new ArrayList<GoodsInfoBean>();
	// �ӷ�������ȡ��ԭʼ��Ϣת��֮�������
	private List<Map<String, Object>> originalData;
	// Ҫ��ʾ��data
	private List<Map<String, Object>> mData;
	private Button order;// ����������ť
	private Intent intent;
	// ShopActivity���ص��̻�id
	private String shopId = "";
	// ViewPager�Ĳ���
	private ViewPager viewPager = null;
	// ������ÿһҳ�ı���
	private PagerTitleStrip pagerTitleStrip = null;
	// ��ʾװ�ػ����Ĳ���
	private List<View> list = null;
	// ��ʾ����ÿһҳ�ı���
	private List<String> title = null;
	// ���ֱַ��ʾ�˵��е�ȫ�����زˡ���ˡ�����
	private View view_all, view_sucai, view_huncai, view_qita;
	// list�ֱ��ʾ�˵��е�ȫ�����زˡ���ˡ�����
	private ListView menuListView_all;
	private ListView menuListView_sucai;
	private ListView menuListView_huncai;
	private ListView menuListView_qita;
	// ���������������ڼ��ؽ���
	private TextView TextView_all;
	private TextView TextView_sucai;
	private TextView TextView_huncai;
	private TextView TextView_qita;
	// ���浱ǰҳ���ֵ����һ������activityʱΪ0
	private int currentPage = 0;

	// �������ͨ�ŵĲ���
	private String SHOP_ID = "shopId";
	private String NO_GOODS = "no_goods";// û����Ʒ

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				// ���������Ĳ˵�����
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
								// ��ÿ�λ�ȡ�����itemʱ�ı�checkBox״̬����ˢ��litview
								checkBoxSta[arg2] = !checkBoxSta[arg2];
								myAdapter.refresh();
							}
						});
			} else if (msg.what == 0x234) {
				Toast.makeText(MenuActivity.this, "������Ʒ", Toast.LENGTH_SHORT)
						.show();
				changeView();
			}// ����������
			else if (msg.what == 0x999) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MenuActivity.this)
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
								// �����е��������Ʒ
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
				// ��ʾȫ����Ʒ
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
				// ��ʾ
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
									// ��ÿ�λ�ȡ�����itemʱ�ı�checkBox״̬����ˢ��litview
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
									// ��ÿ�λ�ȡ�����itemʱ�ı�checkBox״̬����ˢ��litview
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
		title.add("ȫ��");
		title.add("���");
		title.add("�ز�");
		title.add("����");
		pagerTitleStrip.setTextColor(Color.rgb(90, 135, 12));
		pagerTitleStrip.setTextSize(1, 20);
		viewPager.setAdapter(new MyViewPagerAdapter());
	}

	/**
	 * ͨ��Http�����ȡList���÷����Ὺ���µ��߳� ͨ��handler�������ݵ����̸߳���listview
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
	 * �����������ص�listת��Ϊ�������listview��list<map<obj,obj>>
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
	 * ����ҳ����ȡҪ��ʾ����Ʒ
	 * 
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getDataByPage(int page) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		//1��ʾ��ˣ�2��ʾ�زˣ�3��ʾ����
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

		public TextView goodsName;// ��Ʒ����
		public TextView canDistribution;// �Ƿ������
		public CheckBox check;// ��Ʒ�Ƿ�ѡ��
		public TextView price;// ��Ʒ�۸�
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

				// ������Ʒ����
				holder.goodsName.setText(mData.get(position).get("goodsName")
						.toString());
				// ������Ʒ�Ƿ������
				if (mData.get(position).get("canDistribution").toString()
						.equals("1"))
					holder.canDistribution.setText("������");
				else
					holder.canDistribution.setText("������");
				// ������Ʒ�۸�
				holder.price.setText("��"
						+ mData.get(position).get("price").toString());
				// ������Ʒ�Ƿ񱻹�ѡ
				holder.check.setChecked(checkBoxSta[position]);
			}
			return convertView;
		}

	}

	// ���ݼ������ʱ�����ڼ��ر���б�
	private void changeView() {
		TextView_all.setVisibility(View.GONE);
		TextView_sucai.setVisibility(View.GONE);
		TextView_huncai.setVisibility(View.GONE);
		menuListView_all.setVisibility(View.VISIBLE);
		menuListView_sucai.setVisibility(View.VISIBLE);
		menuListView_huncai.setVisibility(View.VISIBLE);
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
				finish();
			}
		});
		titleText = (TextView) findViewById(R.id.title_name);
		titleText.setText("�˵�");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

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
