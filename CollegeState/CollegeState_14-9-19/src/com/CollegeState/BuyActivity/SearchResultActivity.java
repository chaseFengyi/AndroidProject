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
	private ImageButton back;// ���ؼ�
	private TextView titleText;// ����������
	private Button order;// ����������ť
	private ListView searchListView;// ���������list
	// checkBox״̬
	private boolean[] checkBoxSta = null;
	private Intent intent;
	private List<Map<String, Object>> mData;// list�е�����

	// ����˵���Ϣ���ӷ������ϻ�ȡ��ԭʼ��Ϣ��
	public static List<GoodsInfoBean> menuList = new ArrayList<GoodsInfoBean>();

	// ��̬�����������̻���Ϣ������֮��ĳ����е���
	public static List<GoodsInfoBean> searchList = new ArrayList<GoodsInfoBean>();

	// �������ͨ�ŵĲ���
	private String SEARCH = "search";
	private String NO_GOODS = "no_goods";

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// ��ȡ���ݳɹ�
			if (msg.what == 0x123) {
				// ������Ϣ
				menuList = (List<GoodsInfoBean>) msg.obj;
				searchList = (List<GoodsInfoBean>) msg.obj;
				mData = getData((List<GoodsInfoBean>) msg.obj);
				Toast.makeText(getApplicationContext(), "������������Ʒ", 5000).show();
				if (mData.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SearchResultActivity.this)
							.setTitle("")
							.setMessage("������������Ʒ")
							.setNegativeButton("����",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
											finish();
										}
									})
							.setPositiveButton("ȷ��",
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
				searchListView.setItemsCanFocus(false);// ����item�еİ�ť���ɱ����
				searchListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);// �Ƿ�ɶ�ѡ
				searchListView
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								// ��ÿ�λ�ȡ�����itemʱ�ı�checkBox״̬����ˢ��litview
								checkBoxSta[arg2] = !checkBoxSta[arg2];
								myAdapter.refresh();
							}
						});
			} else if (msg.what == 0x234) {
				Toast.makeText(SearchResultActivity.this, "������Ʒ",
						Toast.LENGTH_SHORT).show();
			}
			// ����������
			else if (msg.what == 0x999) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SearchResultActivity.this)
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
								// �����е��������Ʒ
								OrderItem.addItem(info);
								isAdded = true;
							}
						}
					}
				}
				if (isAdded == true) {
					// ����
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SearchResultActivity.this)
							.setTitle("���ʵ��")
							.setMessage("��ӹ��ﳵ�ɹ�")
							.setNegativeButton("����",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									})
							.setPositiveButton("ȥ�ύ����",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											dialog.dismiss();
											// ��ת���ύ��activity
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
		getsearchList();
	}

	/**
	 * ͨ��Http�����ȡList���÷����Ὺ���µ��߳� ͨ��handler�������ݵ����̸߳���listview
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
	 * �����������ص�listת��Ϊ�������listview��list<map<obj,obj>>
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
			// ����������������
			map.put("goodsAssess", searchInfo.getGoodsAssess());
			list.add(map);
		}
		checkBoxSta = new boolean[list.size()];
		return list;
	}

	// �������������
	private String getSearchName() {
		String result = null;
		intent = getIntent();
		result = intent.getStringExtra("SearchName");
		return result;
	}

	// ViewHolder
	public final class ViewHolder {

		public TextView goodsName;// ��Ʒ����
		public TextView canDistribution;// �Ƿ������
		public CheckBox check;// ��Ʒ�Ƿ�ѡ��
		public TextView price;// ��Ʒ�۸�
		public TextView shopName;// ��������
	}

	// �Զ���ListAdapter
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
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		// ����λ�õõ���Ӧ��item����
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mData.get(arg0);
		}

		@Override
		// ����λ�õõ���Ӧ��item��id
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (!mData.isEmpty()) {// list��Ϊ�յ������
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

				// ������Ʒ����
				holder.goodsName.setText(mData.get(position).get("goodsName")
						.toString());
				// ������Ʒ�Ƿ������
				if (mData.get(position).get("canDistribution").toString()
						.equals("1")) {
					holder.canDistribution.setText("������");
				} else {
					holder.canDistribution.setText("��������");
				}
				// ������Ʒ�۸�
				holder.price.setText(mData.get(position).get("price")
						.toString()
						+ "Ԫ");
				// ������Ʒ�Ƿ񱻹�ѡ
				holder.check.setChecked(checkBoxSta[position]);
				holder.shopName.setText(mData.get(position).get("shopName")
						.toString());
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
