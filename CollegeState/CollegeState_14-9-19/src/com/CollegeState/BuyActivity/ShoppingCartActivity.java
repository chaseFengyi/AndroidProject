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
 * ���ﳵactivity
 * 
 * @author zc 2014-5-25 - 0:14
 */
public class ShoppingCartActivity extends Activity {
	private Button home;// ��ҳ
	private Button shop;// �͵�
	private Button mine;// �ҵ�
	// ������
	private ImageButton back;
	private TextView titleText;
	// �ύ����
	private Button submitButton = null;
	// �������listview��data
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();;
	private SwipeListView orderListView = null;
	// ����Χ�ڵ���������
	private List<OrderBean> originalData;
	private MyAdapter adapter;
	//
	private int mRightWidth = 0;
	// �������Ի����Adapter
	private ArrayAdapter<String> inShopAdapter;
	private ArrayAdapter<String> peiSongAdapter;
	private String[] inShopArray;
	private String[] peiSongArray;

	private Intent intent = null;

	// �������ͨ�ŵĲ���
	private String CART_LIST_STR = "cartListStr"; // �ύ����Ʒ����
	private String USER_ID = "userId";// �û�ID
	private String AREA_ID = "areaId"; // ������ַ��Χ
	private String ORDER_ADDRESS = "orderAddress";// ������ַ��ȷ��Χ
	private String ORDER_PHONE = "orderPhone";// �����ߵ绰
	private String ORDER_JINDIAN_TIME = "orderJindianTime";// ����ʱ��
	private String ORDER_PEISONG_TIME = "orderPeisongTime";// ����ʱ��
	private String ORDER_WAY = "orderWay";// ������ʽ
	private String ACTION = "action";// У��

	private String SUBMIT_OK = "submit_ok";
	private String GOODS_IS_NOT_PEISONG = "goods_is_not_isPeisong";
	private String ORDER_BEFORE = "please_order_before_30minutes";
	private String ACTION_ERROR = "action_error";
	private String DATABASE_ERROR = "database_error";
	private String SUBMIT_ERROR = "submit_error";

	// �ύ�����Ľ������Ի���
	private ProgressDialog submitOrderProgressDialog;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			// create order filed!(���ɶ���ʧ��)
			case 0x123:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder0 = new AlertDialog.Builder(
						ShoppingCartActivity.this)
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
			// Submit Success! �ύ�ɹ�
			case 0x234:
				submitOrderProgressDialog.dismiss();
				boolean flag = OrderItem.clearAllData();
				System.out.println("ɾ�����ﳵ����--->" + flag);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ShoppingCartActivity.this)
						.setTitle("У��")
						.setMessage("�ύ�����ɹ���")
						.setPositiveButton("�ٴε��",
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
						.setNegativeButton("����",
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
				builder4.create().show();
				break;
			case 0x236:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder5 = new AlertDialog.Builder(
						ShoppingCartActivity.this)
						.setTitle("У��")
						.setMessage("���ݿ����")
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
			case 0x237:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder6 = new AlertDialog.Builder(
						ShoppingCartActivity.this)
						.setTitle("У��")
						.setMessage("�˷�����δ��ͨ��")
						.setPositiveButton("�Ժ�����",
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
						.setTitle("У��")
						.setMessage("������ѡ����Ͳ�ʱ�����ǰ��Сʱ���ͣ�")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder3.create().show();
				break;
			// �������
			case 0x345:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder2 = new AlertDialog.Builder(
						ShoppingCartActivity.this)
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
			case 0x456:
				submitOrderProgressDialog.dismiss();
				AlertDialog.Builder builder7 = new AlertDialog.Builder(
						ShoppingCartActivity.this)
						.setTitle("У��")
						.setMessage("ĳЩ��Ʒ��֧������")
						.setNegativeButton("����",
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
				// �����ﳵΪ��ʱ
				if (OrderItem.getOrderItems() == null
						|| OrderItem.getOrderItems().size() == 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ShoppingCartActivity.this)
							.setTitle("��ܰ��ʾ")
							.setMessage("���ﳵ����Ϊ��!")
							.setPositiveButton("ȥ���",
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
							.setNegativeButton("����",
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
				// ����û���¼״̬
				if (!UserInfoBean.isLogin()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ShoppingCartActivity.this)
							.setTitle("��ܰ��ʾ")
							.setMessage("����û�е�¼!")
							.setPositiveButton("��¼",
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
							.setNegativeButton("����",
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
				// �ж�����״̬
				isNetWorkEnable();
				// ����������Ϣ�Ի���
				LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(
						R.layout.shopping_cart_dialog, null);

				// �ܼ�
				TextView totleTv = (TextView) ll
						.findViewById(R.id.TextViewShoppingCartDiaTotlePrice);
				// �绰
				final EditText phoneEditText = (EditText) ll
						.findViewById(R.id.EditTextSubmitGoodsDialogPhone);

				// ��ַ
				final EditText ApartIDEditText = (EditText) ll
						.findViewById(R.id.EidtTextShoppingCartDialogApartID);
				final EditText addressEditText = (EditText) ll
						.findViewById(R.id.EditTextSubmitGoodsDialogAddress);
				// ʱ��
				final Spinner timeSpinner = (Spinner) ll
						.findViewById(R.id.SpinnerOrderTime);
				initSpinnerAdapter(ll);
				timeSpinner.setAdapter(inShopAdapter);
				// ���������
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
				// �˵��ܼ�
				double totlePrice = 0;
				for (OrderBean bean : OrderItem.getOrderItems()) {
					totlePrice += bean.getOrderItem().getGoodsPrice()
							* bean.getCount();
				}
				// �˷�
				double freight = 0;

				totleTv.setText(totlePrice + "");
				phoneEditText.setText(UserInfoBean.getUserPhone());
				// �ָ��ַ����ʾ
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
					peisong = "����";
				} else {
					peisong = "����";
				}
				// �ύ�����ĶԻ���
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ShoppingCartActivity.this)
						.setTitle("ȷ����Ϣ")
						.setView(ll)
						.setPositiveButton("����",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// ���ﳵ��Ϣ
										String cartListStr = createOrderItemString();
										// �û�ID
										String customerId = UserInfoBean
												.getUserId();
										// ������ַ
										String orderAddress = ApartIDEditText
												.getText().toString()
												+ "#"
												+ addressEditText.getText()
														.toString();
										// �����ߵ绰
										String orderPhone = phoneEditText
												.getText().toString();
										if (!isPhoneNumberIllegal(orderPhone)) {
											Toast.makeText(
													ShoppingCartActivity.this,
													"�绰���벻��ȷ",
													Toast.LENGTH_SHORT).show();
											return;
										}
										// ������ʽ
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
														"����д������ַ",
														Toast.LENGTH_SHORT)
														.show();
												return;
											}
										}
										// �����������ʱ��
										final String orderTime = timeSpinner
												.getSelectedItem().toString();
										// �ύhttp����

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
												.setTitle("�����ύ����...");
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
	 * ��ʼ�����Ի����spinner��adapter
	 * 
	 * @param ll�����view
	 *            �����ڻ�ȡcontext����
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
	 * �жϵ绰�����Ƿ�Ϸ�
	 * 
	 * @return
	 */
	private boolean isPhoneNumberIllegal(String phone) {
		Pattern regex = Pattern.compile(UserRegisterActivity.regExPhoneNumber);
		Matcher matcher = regex.matcher(phone);
		return matcher.matches();
	}

	/**
	 * �����ﳵ����������ת��Ϊ����������mdata
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
	 * ����http������ַ����� [{"goodsId":1,
	 * "goodsCount":10,"goodsMore":"haochi"},{"goodsId":1,
	 * "goodsCount":10,"goodsMore":"haochi"}] ������֮��Ҫ��Ӷ���
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
	 * �������״̬
	 * 
	 */
	public void isNetWorkEnable() {
		if (!HttpUtils.isNetWorkEnable(this)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ShoppingCartActivity.this)
					.setTitle("��ܰ��ʾ")
					.setMessage("�������")
					.setNegativeButton("����",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									finish();
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
	}

	// �ӱ��ػ�ȡ���ﳵ����
	public List<Map<String, Object>> getDataFromLocal() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		originalData = OrderItem.getOrderItems();
		if (originalData.size() == 0) {
			return null;
		} else {
			for (OrderBean bean : originalData) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("goodsName", bean.getOrderItem().getGoodsName());
				map.put("goodsPrice", "����:"
						+ bean.getOrderItem().getGoodsPrice());
				map.put("shopId", bean.getOrderItem().getShopId());
				map.put("itemCount", bean.getCount());
				// ����������ɾ��
				map.put("goodsId", bean.getOrderItem().getGoodsId());
				list.add(map);
			}
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
		titleText.setText("���ﳵ");
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
		public TextView shopName;// �̻�����
		public TextView orderCount;// ��������
		public TextView price;// ��Ʒ����
		public Button addButton;// ��������
		public Button subButton;// ����
		public Button del;// ɾ��
		public RelativeLayout item_right;// ��̬Ч����layout
		public Button remarkButton;// ��ע��ť
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
			final ViewHolder holder;
			if (mData != null) {// list��Ϊ�յ������
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
											.setTitle("�޸ı�ע��Ϣ")
											.setView(ll)
											.setPositiveButton(
													"��ӱ�ע",
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
																	"��ӳɹ�",
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
										"ɾ���ɹ�", 2000).show();
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

				// ������Ʒ����
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
						Toast.makeText(ShoppingCartActivity.this, "ɾ���ɹ�", 2000)
								.show();
						mData.remove(position);
						notifyDataSetChanged();

						// ɾ��������ɾ����ť
						orderListView.hiddenDel();
					}
				});
			}
			return convertView;
		}

	}
}
