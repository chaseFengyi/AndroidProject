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

	// ������
	private ImageButton back;
	private TextView titleText;

	// listview��adapter
	MyAdapter myAdapter0 = null;
	MyAdapter myAdapter1 = null;
	MyAdapter myAdapter2 = null;
	MyAdapter myAdapter3 = null;
	MyAdapter myAdapter11 = null;
	// Ҫ��ʾ��data
	private List<Map<String, Object>> mData0, mData1, mData2, mData3, mData11;
	// ViewPager�Ĳ���
	private ViewPager viewPager = null;
	// ������ÿһҳ�ı���
	private PagerTitleStrip pagerTitleStrip = null;
	// ��ʾװ�ػ����Ĳ���
	private List<View> list = null;
	// ��ʾ����ÿһҳ�ı���
	private List<String> title = null;
	//
	private View view_0, view_1, view_2, view_3, view_11;
	// ���ύ���ѽӵ��������ͣ����ջ��������ۣ���ȡ�����ֱ���0��1��2��3��4��-1��11��

	private ListView UserOrderForm_list_0;
	private ListView UserOrderForm_list_1;
	private ListView UserOrderForm_list_2;
	private ListView UserOrderForm_list_3;
	private ListView UserOrderForm_list_11;

	// �ύ�����Ľ������Ի���
	private ProgressDialog submitOrderProgressDialog = null;

	// ��¼�ύ�޸ĵ�״̬
	private int mark, mark_position;

	// �������ͨ�ŵĲ���
	private String USER_ID = "userId";
	private String ORDER_ITEM_STATE = "orderItemState";// ������״̬
	private String NO_ORDER = "no_order";// ��ʾ���û�û���¶���
	private String ORDER_ITEM_ID = "orderItemId";
	private String ACTION = "action";
	private String UPDATE_OK = "update_ok";// �޸ĳɹ�
	private String ACTION_ERROR = "action_error";// У��ʧ��
	private String UPDATE_ERROR = "update_error";// �޸�ʧ��
	private String ASSESS_STYLE = "assessStyle"; // ��������
	private String ASSESS_OK = "assess_ok";// ���۳ɹ�
	private String ASSESS_ERROR = "assess_error";// ����ʧ��
	private String DATABASE_ERROR = "database_error";// ����ʧ��

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			// ����������
			case 0x789:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder0 = new AlertDialog.Builder(
						UserOrderFormActivity.this)
						.setTitle("У��")
						.setMessage("��������С���ˣ�")
						.setPositiveButton("�Ժ�����",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder0.create().show();
				break;
			// ��δ�µ�
			case 0x890:
				Toast.makeText(UserOrderFormActivity.this, "����",
						Toast.LENGTH_SHORT).show();
				break;
			// Submit Success! �ύ�ɹ�
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
						.setTitle("У��")
						.setMessage("�ύ�ɹ���")
						.setPositiveButton("������ҳ",
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
						.setNegativeButton("����",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder.create().show();
				break;
			// Submit Success! �ύʧ��
			case 0x234:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder9 = new AlertDialog.Builder(
						UserOrderFormActivity.this)
						.setTitle("У��")
						.setMessage("�ύʧ�ܣ�")
						.setPositiveButton("������ҳ",
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
						.setNegativeButton("����",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder9.create().show();
				break;
			// �������
			case 0x345:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder2 = new AlertDialog.Builder(
						UserOrderFormActivity.this)
						.setTitle("У��")
						.setMessage("�������")
						.setPositiveButton("ȥ�鿴����",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Intent intent = new Intent(
												android.provider.Settings.ACTION_SETTINGS);
										startActivity(intent);

									}
								})
						.setNegativeButton("����",
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
						.setTitle("У��")
						.setMessage("��½ʧЧ�������µ�½")
						.setPositiveButton("�Ժ�����",
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
				// ���������Ĳ˵�����
				mData0 = getData((List<UserOrderFormInfoBean>) msg.obj);
				myAdapter0 = new MyAdapter(UserOrderFormActivity.this, 0);
				UserOrderForm_list_0.setAdapter(myAdapter0);
				if (mData0 == null || mData0.size() == 0)
					break;
				myAdapter0.refresh();
				break;
			case 0x001:
				// ���������Ĳ˵�����
				mData1 = getData((List<UserOrderFormInfoBean>) msg.obj);
				myAdapter1 = new MyAdapter(UserOrderFormActivity.this, 1);
				UserOrderForm_list_1.setAdapter(myAdapter1);
				if (mData0 == null || mData0.size() == 0)
					break;
				myAdapter1.refresh();
				break;
			case 0x002:
				// ���������Ĳ˵�����
				mData2 = getData((List<UserOrderFormInfoBean>) msg.obj);
				myAdapter2 = new MyAdapter(UserOrderFormActivity.this, 2);
				UserOrderForm_list_2.setAdapter(myAdapter2);
				if (mData2 == null || mData2.size() == 0)
					break;
				myAdapter2.refresh();
				break;
			case 0x003:
				// ���������Ĳ˵�����
				mData3 = getData((List<UserOrderFormInfoBean>) msg.obj);
				myAdapter3 = new MyAdapter(UserOrderFormActivity.this, 3);
				UserOrderForm_list_3.setAdapter(myAdapter3);
				if (mData3 == null || mData3.size() == 0)
					break;
				myAdapter3.refresh();
				break;
			case 0x0011:
				// ���������Ĳ˵�����
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
					.setTitle("��ܰ��ʾ")
					.setMessage("δ��¼")
					.setPositiveButton("ȥ��½",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// ��ת��ϵͳ��activityȥ��������
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
				// ��ʾ0
				case 0:
					getItemsList(0);
					if (mData0 == null)
						return;
					myAdapter0 = new MyAdapter(UserOrderFormActivity.this, 0);
					UserOrderForm_list_0.setAdapter(myAdapter0);
					break;
				// ��ʾ1
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
		title.add("���µ�");
		title.add("�ѽӵ�");
		title.add("������");
		title.add("���ջ�");
		title.add("��ȡ��");
		pagerTitleStrip.setTextSize(1, 25);
		pagerTitleStrip.setTextColor(Color.rgb(90, 135, 12));
		viewPager.setAdapter(new MyViewPagerAdapter());
	}

	/**
	 * ͨ��Http�����ȡList���÷����Ὺ���µ��߳� ͨ��handler�������ݵ����̸߳���listview
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
	 * ͨ��Http�����ȡList���÷����Ὺ���µ��߳� ͨ��handler�������ݵ����߳� ���ܣ�ȷ���ջ� orderItemId ����״̬ Not
	 * null orderItemState ����״̬ 0:���ύ��1:�ѽӵ�,2�ѷ���,3:��ȷ�ϣ�4������
	 */
	private void confirmOrder(final String orderItemId,
			final String orderItemState, final int position) {
		submitOrderProgressDialog = new ProgressDialog(
				UserOrderFormActivity.this);
		submitOrderProgressDialog.setTitle("У��");
		submitOrderProgressDialog.setMessage("�����ύ");
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
	 * ͨ��Http�����ȡList���÷����Ὺ���µ��߳� ͨ��handler�������ݵ����߳� ���ܣ����� goodsId ��Ʒid Not null
	 * customerId �û�id Not null assessStyle �������� 2������ 1������ 0������ assessContent
	 * �������� ����Ϊ��
	 */
	private void assessOrder(final String customerId, final String orderItemId,
			final String goodsId, final String assessStyle,
			final String assessContent, final int position) {
		submitOrderProgressDialog = new ProgressDialog(
				UserOrderFormActivity.this);
		submitOrderProgressDialog.setTitle("У��");
		submitOrderProgressDialog.setMessage("�����ύ");
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

	// ȥ�����������ص����߰���Ķ���
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
	 * �����������ص�listת��Ϊ�������listview��list<map<obj,obj>>
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
		titleText.setText("�ҵĶ���");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	final class ViewHolder {

		public TextView goodsName;// ��Ʒ����
		public TextView shopName;// ��������
		public TextView goodsCount;// ��Ʒ����
		public TextView goodsPrice;// ��Ʒ����
		public Button cancelOrder;// ��������
		public Button confirm;// ȷ���ջ�
		public Button assess;// ��Ʒ����
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

			// ������Ʒ����
			holder.goodsName.setText(mData.get(position).get("goodsName")
					.toString());
			// ���õ�������
			holder.shopName.setText(mData.get(position).get("shopName")
					.toString());
			// ������Ʒ����
			holder.goodsCount.setText("����:"
					+ mData.get(position).get("goodsCount").toString());
			// ������Ʒ�۸�
			holder.goodsPrice.setText("����:"
					+ mData.get(position).get("goodsPrice").toString());

			holder.cancelOrder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserOrderFormActivity.this)
							.setTitle("ȷ���˻���")
							.setPositiveButton("ȷ��",
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

			holder.confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserOrderFormActivity.this)
							.setTitle("ȷ���ջ���")
							.setPositiveButton("ȷ��",
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
							.setTitle("����")
							.setView(ll)
							.setPositiveButton("ȷ��",
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