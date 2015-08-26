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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class SearchResultActivity extends Activity {
	private ImageButton back;// 返回键
	private TextView titleText;// 标题栏名称
	private Button order;// 立即订单按钮
	private ListView searchListView;// 搜索结果的list
	// checkBox状态
	private boolean[] checkBoxSta = null;
	private Intent intent;
	private List<Map<String, Object>> mData;// list中的数据

	// 保存菜单信息（从服务器上获取的原始信息）
	public static List<GoodsInfoBean> menuList = new ArrayList<GoodsInfoBean>();

	// 静态变量，保存商户信息，方便之后的程序中调用
	public static List<GoodsInfoBean> searchList = new ArrayList<GoodsInfoBean>();

	// 与服务器通信的参数
	private String SEARCH = "search";
	private String NO_GOODS = "no_goods";

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 获取数据成功
			if (msg.what == 0x123) {
				// 保存信息
				menuList = (List<GoodsInfoBean>) msg.obj;
				searchList = (List<GoodsInfoBean>) msg.obj;
				mData = getData((List<GoodsInfoBean>) msg.obj);
				Toast.makeText(getApplicationContext(), "暂无所搜索商品", 5000).show();
				if (mData.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SearchResultActivity.this)
							.setTitle("")
							.setMessage("暂无所搜索商品")
							.setNegativeButton("返回",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
											finish();
										}
									})
							.setPositiveButton("确定",
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
				final MyAdapter myAdapter = new MyAdapter(
						SearchResultActivity.this);
				searchListView = (ListView) findViewById(R.id.search_list);
				searchListView.setAdapter(myAdapter);
				searchListView.setItemsCanFocus(false);// 设置item中的按钮不可被点击
				searchListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);// 是否可多选
				searchListView
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								// 在每次获取点击的item时改变checkBox状态并且刷新litview
								checkBoxSta[arg2] = !checkBoxSta[arg2];
								myAdapter.refresh();
							}
						});
			} else if (msg.what == 0x234) {
				Toast.makeText(SearchResultActivity.this, "暂无商品",
						Toast.LENGTH_SHORT).show();
			}
			// 服务器错误
			else if (msg.what == 0x999) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SearchResultActivity.this)
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

	private void findView() {
		order = (Button) findViewById(R.id.search_order);
	}

	private void setListener() {
		order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean isAdded = false;
				if (mData == null)
					return;
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
					// 测试
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SearchResultActivity.this)
							.setTitle("西邮点餐")
							.setMessage("添加购物车成功")
							.setNegativeButton("返回",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									})
							.setPositiveButton("去提交订单",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											dialog.dismiss();
											// 跳转到提交订activity
											Intent intent = new Intent();
											intent.setClass(
													SearchResultActivity.this,
													ShoppingCartActivity.class);
											startActivity(intent);
										}
									});
					builder.create().show();
				}

			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		findView();
		setListener();
		initActionbar();

		if (!HttpUtils.isNetWorkEnable(this)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SearchResultActivity.this)
					.setTitle("溫馨提示")
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
		getsearchList();
	}

	/**
	 * 通过Http请求获取List，该方法会开启新的线程 通过handler发送数据到主线程更新listview
	 * 
	 */
	private void getsearchList() {
		new Thread() {
			public void run() {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(SEARCH, getSearchName()));
				String result = HttpUtils.queryStringForPost(
						HttpUtils.USER_SEARCH_URL, params);
				Message msg = Message.obtain();
				if (result.equals(NO_GOODS)) {
					msg.what = 0x234;
					handler.sendMessage(msg);
				} else {
					List<GoodsInfoBean> list = JSONUtils
							.getSearchInfoList(result);
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
	 * @param searchList
	 * @return
	 */
	public List<Map<String, Object>> getData(List<GoodsInfoBean> searchList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (GoodsInfoBean searchInfo : searchList) {
			map = new HashMap<String, Object>();
			map.put("goodsId", searchInfo.getGoodsId());
			map.put("goodsName", searchInfo.getGoodsName());
			map.put("canDistribution", searchInfo.getIsPeisong());
			map.put("price", searchInfo.getGoodsPrice() + "");
			map.put("goodCategory", searchInfo.getGoodsCategory());
			map.put("shopName", searchInfo.getShopName());
			// 以下数据用于排序
			map.put("goodsAssess", searchInfo.getGoodsAssess());
			list.add(map);
		}
		checkBoxSta = new boolean[list.size()];
		return list;
	}

	// 获得搜索的内容
	private String getSearchName() {
		String result = null;
		intent = getIntent();
		result = intent.getStringExtra("SearchName");
		return result;
	}

	// ViewHolder
	public final class ViewHolder {

		public TextView goodsName;// 商品名称
		public TextView canDistribution;// 是否可配送
		public CheckBox check;// 商品是否选中
		public TextView price;// 商品价格
		public TextView shopName;// 商铺名称
	}

	// 自定义ListAdapter
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
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		// 根据位置得到相应的item对象
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mData.get(arg0);
		}

		@Override
		// 根据位置得到相应的item的id
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (!mData.isEmpty()) {// list不为空的情况下
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = mInflater.inflate(
							R.layout.search_result_list_info, null);
					// findView
					holder.goodsName = (TextView) convertView
							.findViewById(R.id.search_goods_name);
					holder.canDistribution = (TextView) convertView
							.findViewById(R.id.search_distribution);
					holder.check = (CheckBox) convertView
							.findViewById(R.id.search_list_checkBox);
					holder.price = (TextView) convertView
							.findViewById(R.id.search_list_price);
					holder.shopName = (TextView) convertView
							.findViewById(R.id.search_shop_name);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				// 设置商品名称
				holder.goodsName.setText(mData.get(position).get("goodsName")
						.toString());
				// 设置商品是否可配送
				if (mData.get(position).get("canDistribution").toString()
						.equals("1")) {
					holder.canDistribution.setText("可配送");
				} else {
					holder.canDistribution.setText("不可配送");
				}
				// 设置商品价格
				holder.price.setText(mData.get(position).get("price")
						.toString()
						+ "元");
				// 设置商品是否被勾选
				holder.check.setChecked(checkBoxSta[position]);
				holder.shopName.setText(mData.get(position).get("shopName")
						.toString());
			}
			return convertView;
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
		titleText.setText(getSearchName());
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
