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

	// listView��adapter
	MyAdapter myAdapter = null;
	// ����˵���Ϣ���ӷ������ϻ�ȡ��ԭʼ��Ϣ��
	public static List<MyWealthInfoBean> menuList = new ArrayList<MyWealthInfoBean>();
	// �ӷ�������ȡ��ԭʼ��Ϣת��֮�������
	private List<Map<String, Object>> originalData;
	// Ҫ��ʾ��data
	private List<Map<String, Object>> mData;
	// ���ڼ��ؽ���
	private TextView loadingText;
	// ������
	private ImageButton back;
	private TextView titleText;

	// �ύ�Ľ������Ի���
	private ProgressDialog submitProgressDialog;

	// �������ͨ�ŵĲ���
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
				// ���������Ĳ˵�����
				menuList = (List<MyWealthInfoBean>) msg.obj;
				originalData = getData((List<MyWealthInfoBean>) msg.obj);
				mData = originalData;
				myAdapter = new MyAdapter(MyWealthActivity.this);
				listView.setAdapter(myAdapter);
			} else if (msg.what == 0x234) {
				Toast.makeText(MyWealthActivity.this, "û�вƸ�", 2000).show();
			} else if (msg.what == 0x345) {
				Toast.makeText(MyWealthActivity.this, "У����������µ�¼������",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0x100) {
				mData.remove(msg.arg1);
				submitProgressDialog.dismiss();
				myAdapter.refresh();
				Toast.makeText(MyWealthActivity.this, "���ѳɹ�", 2000).show();
			} else if (msg.what == 0x101) {
				Toast.makeText(MyWealthActivity.this, "����ʧ��", 2000).show();
			}
			// ����������
			else if (msg.what == 0x999) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MyWealthActivity.this)
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

	// ���ݼ������ʱ�����ڼ��ر���б�
	private void changeView() {
		listView.setVisibility(View.VISIBLE);
		loadingText.setVisibility(View.GONE);
	}

	// ɾ���ҵĲƸ�
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
	 * ͨ��Http�����ȡList���÷����Ὺ���µ��߳� ͨ��handler�������ݵ����̸߳���listview
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
	 * �����������ص�listת��Ϊ�������listview��list<map<obj,obj>>
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

		public TextView goodsName;// ��Ʒ����
		public TextView goodsPrivce;// ��Ʒ�۸�
		public TextView goodsCount;// ��Ʒ����
		public TextView shopName;// ��������
		public TextView closingDate;// ��ֹ����
		public Button confirmeBtn;
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
								.setTitle("��ܰ��ʾ")
								.setMessage("���Ѻ�˲Ƹ������б���ɾ��!")
								.setPositiveButton("ȷ������",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												delWealth(position);
												submitProgressDialog = new ProgressDialog(
														MyWealthActivity.this);
												submitProgressDialog
														.setTitle("�����ύ...");
												submitProgressDialog
														.setProgressStyle(ProgressDialog.STYLE_SPINNER);
												submitProgressDialog
														.setCancelable(true);
												submitProgressDialog.show();
												dialog.dismiss();
											}
										})
								.setNegativeButton("ȡ��",
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

				// ȥ��С�����Ϊ0
				String temp = mData.get(position).get("goodsPrivce").toString();
				if (temp.endsWith(".0")) {
					temp = temp.substring(0, temp.length() - 2);
				}
				holder.goodsPrivce.setText(temp + "Ԫ");

				holder.goodsCount.setText("���� "
						+ mData.get(position).get("goodsCount").toString());

				holder.shopName.setText(mData.get(position).get("shopName")
						.toString());
				holder.closingDate.setText("��ֹ "
						+ mData.get(position).get("closingDate").toString());

			}
			return convertView;
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
				finish();
			}
		});
		titleText = (TextView) findViewById(R.id.title_name);
		titleText.setText("�ҵĲƸ�");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
