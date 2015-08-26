package com.CollegeState.UserActivity;

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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.CollegeState.Data.MyWealthInfoBean;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.UserActivity.MyWealthActivity.MyAdapter;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.JSONUtils;

public class MyWealthActivity<MyAdapter> extends Activity {
	private ListView listView;

	// listView的adapter
	MyAdapter myAdapter = null;
	// 保存菜单信息（从服务器上获取的原始信息）
	public static List<MyWealthInfoBean> menuList = new ArrayList<MyWealthInfoBean>();
	// 从服务器获取的原始信息转换之后的数据
	private List<Map<String, Object>> originalData;
	// 要显示的data
	private List<Map<String, Object>> mData;
	// 正在加载界面
	private TextView loadingText;
	// 标题栏
	private ImageButton back;
	private TextView titleText;

	// 提交的进度条对话框
	private ProgressDialog submitProgressDialog;

	// 与服务器通信的参数
	private String USER_ID = "userId";
	private String NO_WEALTH = "no_wealth";
	private String ACTION = "action";
	private String ACTION_ERROR = "action_error";
	private String UPDATE_OK = "update_ok";
	private String UPDATE_ERROR = "update_error";

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			changeView();
			if (msg.what == 0x123) {
				// 返服务器的菜单数据
				menuList = (List<MyWealthInfoBean>) msg.obj;
				originalData = getData((List<MyWealthInfoBean>) msg.obj);
				mData = originalData;
				myAdapter = new MyAdapter(MyWealthActivity.this);
				listView.setAdapter(myAdapter);
			} else if (msg.what == 0x234) {
				Toast.makeText(MyWealthActivity.this, "没有财富", 2000).show();
			} else if (msg.what == 0x345) {
				Toast.makeText(MyWealthActivity.this, "校验错误！请重新登录后再试",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0x100) {
				mData.remove(msg.arg1);
				submitProgressDialog.dismiss();
				myAdapter.refresh();
				Toast.makeText(MyWealthActivity.this, "消费成功", 2000).show();
			} else if (msg.what == 0x101) {
				Toast.makeText(MyWealthActivity.this, "消费失败", 2000).show();
			}
			// 服务器错误
			else if (msg.what == 0x999) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MyWealthActivity.this)
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
		setContentView(R.layout.my_wealth);
		initActionbar();
		findView();
		setListener();
		getItemsList();

	}

	private void setListener() {
		// TODO Auto-generated method stub

	}

	private void findView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.my_wealth_list);
		loadingText = (TextView) findViewById(R.id.my_wealth_textView_loading);
	}

	// 数据加载完成时，正在加载变成列表
	private void changeView() {
		listView.setVisibility(View.VISIBLE);
		loadingText.setVisibility(View.GONE);
	}

	// 删除我的财富
	private void delWealth(final int position) {
		new Thread() {
			public void run() {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("wealthId", mData.get(
						position).get("wealthId")
						+ ""));
				params.add(new BasicNameValuePair(ACTION, UserInfoBean
						.getAction()));
				String result = HttpUtils.queryStringForPost(
						HttpUtils.DEL_WEALTH, params);
				Message msg = Message.obtain();
				if (result.equals(UPDATE_OK)) {
					msg.what = 0x100;
					msg.arg1 = position;
				} else if (result.equals(UPDATE_ERROR)) {
					msg.what = 0x101;
				} else if (TextUtils.isEmpty(result)) {
					handler.sendEmptyMessage(0x999);
				}

				handler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * 通过Http请求获取List，该方法会开启新的线程 通过handler发送数据到主线程更新listview
	 * 
	 */
	private void getItemsList() {
		new Thread() {
			public void run() {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(USER_ID, UserInfoBean
						.getUserId()));
				params.add(new BasicNameValuePair(ACTION, UserInfoBean
						.getAction()));
				String result = HttpUtils.queryStringForPost(
						HttpUtils.USER_CKECK_WEALTH, params);
				Message msg = new Message();
				if (result.equals(NO_WEALTH)) {
					msg.what = 0x234;
				} else if (result.equals(ACTION_ERROR)) {
					msg.what = 0x345;
				} else if (TextUtils.isEmpty(result)) {
					handler.sendEmptyMessage(0x999);
				} else {
					List<MyWealthInfoBean> list = JSONUtils
							.getMyWealthInfoList(result);
					msg.what = 0x123;
					msg.obj = list;
				}
				handler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * 将服务器返回的list转化为可以填充listview的list<map<obj,obj>>
	 * 
	 * @param shopList
	 * @return
	 */
	public List<Map<String, Object>> getData(List<MyWealthInfoBean> itemsList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (MyWealthInfoBean item : itemsList) {
			map = new HashMap<String, Object>();
			map.put("goodsName", item.getGoodsName());
			map.put("goodsPrivce", item.getProGoodsPrice());
			map.put("goodsCount", item.getMyGoodsCount());
			map.put("shopName", item.getShopName());
			map.put("closingDate", item.getProTime());
			map.put("wealthId", item.getWealthId());

			list.add(map);
		}
		return list;
	}

	final class ViewHolder {

		public TextView goodsName;// 商品名称
		public TextView goodsPrivce;// 商品价格
		public TextView goodsCount;// 商品数量
		public TextView shopName;// 商铺名称
		public TextView closingDate;// 截止日期
		public Button confirmeBtn;
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
							R.layout.my_wealth_list_item, null);
					// findView
					holder.goodsName = (TextView) convertView
							.findViewById(R.id.my_wealth_item_goods_name);
					holder.goodsPrivce = (TextView) convertView
							.findViewById(R.id.my_wealth_item_goods_price);
					holder.goodsCount = (TextView) convertView
							.findViewById(R.id.my_wealth_item_goods_count);
					holder.shopName = (TextView) convertView
							.findViewById(R.id.my_wealth_item_shop_name);
					holder.closingDate = (TextView) convertView
							.findViewById(R.id.my_wealth_item_closing_date);
					holder.confirmeBtn = (Button) convertView
							.findViewById(R.id.my_wealth_item_confirme_btn);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.confirmeBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								MyWealthActivity.this)
								.setTitle("温馨提示")
								.setMessage("消费后此财富将从列表中删除!")
								.setPositiveButton("确定消费",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												delWealth(position);
												submitProgressDialog = new ProgressDialog(
														MyWealthActivity.this);
												submitProgressDialog
														.setTitle("正在提交...");
												submitProgressDialog
														.setProgressStyle(ProgressDialog.STYLE_SPINNER);
												submitProgressDialog
														.setCancelable(true);
												submitProgressDialog.show();
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

				holder.goodsName.setText(mData.get(position).get("goodsName")
						.toString());

				// 去除小数点后为0
				String temp = mData.get(position).get("goodsPrivce").toString();
				if (temp.endsWith(".0")) {
					temp = temp.substring(0, temp.length() - 2);
				}
				holder.goodsPrivce.setText(temp + "元");

				holder.goodsCount.setText("数量 "
						+ mData.get(position).get("goodsCount").toString());

				holder.shopName.setText(mData.get(position).get("shopName")
						.toString());
				holder.closingDate.setText("截止 "
						+ mData.get(position).get("closingDate").toString());

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
				finish();
			}
		});
		titleText = (TextView) findViewById(R.id.title_name);
		titleText.setText("我的财富");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
