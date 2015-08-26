package com.coolReader.mainPage;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.coolReader.R;
import com.coolReader.Application.SysApplication;
import com.coolReader.Bean.SaveInfoBean;
import com.coolReader.Bean.URLInfoBean;
import com.coolReader.Bean.WebContentBean;
import com.coolReader.Login.LoginActivity;
import com.coolReader.Util.AddressUtil;
import com.coolReader.Util.BackTagUtils;
import com.coolReader.Util.DialogUtils;
import com.coolReader.Util.FileUtils;
import com.coolReader.Util.ImageUtile;
import com.coolReader.Util.ToastUtils;
import com.coolReader.addnew.AddNewActivity;
import com.coolReader.dao.DBPerform;
import com.coolReader.net.CheckNetWork;
import com.coolReader.request.ConstructJsonFormat;
import com.coolReader.request.JsonResolve;
import com.coolReader.request.TCPRequest;
import com.coolReader.savefile.SaveFileActivity;
import com.coolReader.search.SearchActivity;
import com.coolReader.setting.SettingActivity;
import com.coolReader.showcontent.ShowURLContentActivity;

/**
 * @author FengYi~
 * 
 */

//http://blog.csdn.net/free4294/article/details/38420233
public class MainPageActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private ImageView drawerImg;
	private Button searchBtn;
	private ImageView menuImg;
	private ListView listView;
	private ImageView leftUserImage;
	private TextView leftUserName;
	private Button myFileBtn;
	private Button myListBtn;
	private Button delate;
	private Button saveDocument;

	private long mExitTime;

	private DrawerLayout drawerLayout;
	private PopupMenu popupMenu = null;
	public static int flag = 1;// �жϵ���༭��ť

	static ProgressDialog dialog;
	Dialog dialog2;

	private static final String MSG_LOGI_SUCCESS = "���³ɹ���";
	private static final int FLAG_LOGIN_SUCCESS = 1;
	public static final String TAG = "urlContent";
	public static final String KEY = "MainPageActivity";
	private final int OK = 1;
	private final int SERVER_ERROR = 2;
	private final int FINISH = 3;

	// �ж��Ƿ�ȫ����ɾ���ɹ�
	boolean state = true;
	// ��¼��Щѡ��û�б�ɾ��
	List<String> which_one = new ArrayList<String>();

	// �����û�ͼ��
	private Bitmap userPicture = null;

	// ���listView�����ݼ���
	private List<WebContentBean> list = new ArrayList<WebContentBean>();
	// ���û�������ѡ��ʱ���洢�����ѡ���position
	public static List<Integer> in = new ArrayList<Integer>();
	private MainPageAdapter adapter;
	public static boolean isSaveFile = false;
	public static boolean isDeleted = false;
//	public String urls_synchro = "";
	public List<String> urls_synchro = null;// ͬ�����ݵ�ʱ�򣬴���Ѿ����Ϊɾ����url

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_mainpage);

		findView();

		List<URLInfoBean> beans = DBPerform.queryURLInfo(MainPageActivity.this);
		Log.i("test-url-info", beans + "");

		userPicture = ImageUtile.drawableToBitmap(MainPageActivity.this
				.getResources().getDrawable(R.drawable.headpic));
		leftUserImage.setImageBitmap(userPicture);

		setAdapter();
		onclick();
		setVisibility();
		setListView();
		listView.setOnItemClickListener(this);

		// �鿴���ݿ�����
		// List<URLInfoBean> beans2 =
		// DBPerform.queryURLInfo(MainPageActivity.this);
		// if(beans2 != null){
		// for(int i = 0 ; i < beans2.size(); i++){
		// Log.i("title", beans2.get(i).getUrlTitle());
		// Log.i("tag", beans2.get(i).getUrlTag()+"");
		// }
		//
		// }

	}

	/**
	 * �������������ͬ������
	 */
	public void synchroData() {
		if (CheckNetWork.isNetWorkConnected(MainPageActivity.this)) {
			// �ڱ������ݿ��в����û�ɾ����url����url����tag=1������
			if (urls_synchro.size() > 0 || urls_synchro != null) {
//				if (urls_synchro.charAt(urls_synchro.length() - 1) == '&') {
//					urls_synchro = urls_synchro.substring(0,
//							urls_synchro.length() - 1);
//				}
				Log.i("urls_synchro", urls_synchro+"");
				String jsonString = ConstructJsonFormat.json_synchroData(
						LoginActivity.USER_EMAIL_STR, urls_synchro);
				// �������������ͬ������
				String receive = TCPRequest.tcpRequest(jsonString,
						AddressUtil.TCT_ADDRESS, AddressUtil.TCP_PORT);
				Log.i("syn-receive", receive);
				dealResult(receive);
			}
		}
	}

	/**
	 * �����ص�ͬ������
	 * @param receive
	 */
	public void dealResult(String receive){
		List<Map<String, Object>> add = JsonResolve.json_synchro(receive);
		deal_PC_delated((List<String>)add.get(0).get("return1"));
		deal_And_delated((String)add.get(0).get("return2"));
		deal_add_newinfo((List<URLInfoBean>)add.get(0).get("return3"));
	}
	
	/**
	 * ����ͬ����PC��ɾ���Ķ�Android��û��ɾ����
	 * @param pcDeleted
	 */
	public void deal_PC_delated(List<String> pcDeleted){
		//ɾ���������ݿ��е�����
		if(pcDeleted != null){
			int len = pcDeleted.size();
			//��¼û��ɾ���ɹ���url
			List<String> stringBuffer = new ArrayList<String>();
			//ɾ���ɹ��ı�ʶ
			int tag = -1;
			for(int i = 0; i < len; i++){
				String url = pcDeleted.get(i);
				tag = DBPerform.deleteURLInfoByUrl(MainPageActivity.this, url);
				if(tag < 0){
					stringBuffer.add(url);
				}
			}
			//����е�û��ɾ���ɹ������½���ɾ��
			int index = 0;
			while(stringBuffer.size() > 0){
				String url = stringBuffer.get(index);
				tag = DBPerform.deleteURLInfoByUrl(MainPageActivity.this, url);
				if(tag < 0){
					stringBuffer.add(url);
				}else{
					stringBuffer.remove(index);
				}
			}
			stringBuffer = null;
		}
	}
	
	/**
	 * ����AndroidҪɾ�����ϴ������������������OK��Android������ɾ�����ص�
	 * @param andDeleted
	 */
	public void deal_And_delated(String andDeleted){
		//ɾ��Android��Ҫɾ��������
		//��ȡ����url
		List<URLInfoBean> urls = DBPerform.queryURLInfo(MainPageActivity.this);
		//����Ҫɾ����url
		List<String> deleting = null;
		if(urls != null){
			deleting = new ArrayList<String>();
			int len = urls.size();
			for(int i = 0; i < len; i++){
				if(urls.get(i).getUrlTag() == 1){
					deleting.add(urls.get(i).getUrlLink());
				}
			}
			if(deleting != null){
				int lens = deleting.size();
				//��¼û��ɾ���ɹ���url
				List<String> stringBuffer = new ArrayList<String>();
				//ɾ���ɹ��ı�ʶ
				int tag = -1;
				for(int i = 0; i < lens; i++){
					String url = deleting.get(i);
					tag = DBPerform.deleteURLInfoByUrl(MainPageActivity.this, url);
					if(tag < 0){
						stringBuffer.add(url);
					}
				}
				//����е�û��ɾ���ɹ������½���ɾ��
				int index = 0;
				while(stringBuffer.size() > 0){
					String url = stringBuffer.get(index);
					tag = DBPerform.deleteURLInfoByUrl(MainPageActivity.this, url);
					if(tag < 0){
						stringBuffer.add(url);
					}else{
						stringBuffer.remove(index);
					}
				}
				stringBuffer = null;
			}
		}
	}
	
	/**
	 * ͬ��Android��δ�����ҳ��
	 * @param newInfo
	 */
	public void deal_add_newinfo(List<URLInfoBean> newInfo){
		//�ȷ��ڱ������ݿ⣬Ȼ����ʾ
		if(newInfo != null){
			int len = newInfo.size();
			for(int i = 0; i < len; i++){
				DBPerform.insertURLInfo(MainPageActivity.this, newInfo.get(i));
			}
			for (int i = 0; i < len; i++) {
				WebContentBean bean = new WebContentBean();
				bean.setChecked(false);
				bean.setWebId(newInfo.get(i).getUrlId());
				bean.setWebTitle(newInfo.get(i).getUrlTitle());
				bean.setWebUrl(newInfo.get(i).getUrlLink());
				bean.setWebContent(newInfo.get(i).getUrlContent());
				list.add(0, bean);
			}
		}
	}
	
	private void findView() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		drawerImg = (ImageView) findViewById(R.id.coolReaderDrawer);
		searchBtn = (Button) findViewById(R.id.coolReaderSearch);
		menuImg = (ImageView) findViewById(R.id.coolReaderMenu);
		listView = (ListView) findViewById(R.id.coolReaderListView);
		leftUserImage = (ImageView) findViewById(R.id.coolReaderPhoto);
		leftUserName = (TextView) findViewById(R.id.coolReaderUserName);
		myFileBtn = (Button) findViewById(R.id.coolReaderFile);
		myListBtn = (Button) findViewById(R.id.coolReaderListing);
		delate = (Button) findViewById(R.id.coolReader_delete);
		saveDocument = (Button) findViewById(R.id.coolReader_save_document);

		leftUserName.setText(LoginActivity.USER_EMAIL_STR);
	}

	private void setAdapter() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int height = metrics.heightPixels;
		int width = metrics.widthPixels;

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		leftUserImage.measure(w, h);
		int subWidth = leftUserImage.getMeasuredWidth();

		leftUserImage.setPadding((width - subWidth) / 8, 10, 0, height / 10);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = width / 2;
		params.height = width / 2;
		leftUserImage.setLayoutParams(params);

		myFileBtn.setPadding((width - subWidth) / 4, 0, 0, 10);
		myListBtn.setPadding((width - subWidth) / 4, 0, 0, 10);
	}

	private void onclick() {
		drawerImg.setOnClickListener(this);
		menuImg.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		myFileBtn.setOnClickListener(this);
		myListBtn.setOnClickListener(this);
		delate.setOnClickListener(this);
		saveDocument.setOnClickListener(this);
	}

	// ����
	private void createWindow(int top, int bottom) {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = height * top / bottom;
		listView.setLayoutParams(params);
	}

	private void setVisibility() {
		delate.setVisibility(View.GONE);
		saveDocument.setVisibility(View.GONE);
	}

	/*
	 * private void getData() { for (int i = 0; i < 15; i++) { WebContentBean
	 * webContentBean = new WebContentBean();
	 * 
	 * webContentBean.setChecked(false); webContentBean.setWebTitle("title" +
	 * i); webContentBean.setWebContent("content" + i);
	 * webContentBean.setWebUrl("Url" + i);
	 * 
	 * list.add(webContentBean); } }
	 */

	/**
	 * �ӱ������ݿ��ȡurl��Ϣ
	 */
	private void getData() {
		List<URLInfoBean> list_url = DBPerform
				.queryURLInfo(MainPageActivity.this);
		if (list != null) {
			urls_synchro = new ArrayList<String>();
			for (int i = 0; i < list_url.size(); i++) {
				if (list_url.get(i).getUrlTag() == 1) {// tag=1�����������Ѿ��ڱ���ɾ����������ʾ
					// �����StringBuffer�У�ʵ��������������ݵ�ͬ��
					urls_synchro.add(list_url.get(i).getUrlLink());
					continue;
				}
				WebContentBean bean = new WebContentBean();
				bean.setChecked(false);
				bean.setWebId(list_url.get(i).getUrlId());
				bean.setWebTitle(list_url.get(i).getUrlTitle());
				bean.setWebUrl(list_url.get(i).getUrlLink());
				bean.setWebContent(list_url.get(i).getUrlContent());
				list.add(0, bean);
			}
		}
		synchroData();
	}

	private void setListView() {
		getData();
		System.out.println("list=" + list.size());
		if (list != null && list.size() > 0) {
			adapter = new MainPageAdapter(MainPageActivity.this, list);
			listView.setAdapter(adapter);
		} else {
			Toast.makeText(MainPageActivity.this, "�����κ���Ϣ!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void showTip(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	private class MyHandler extends Handler {

		private final WeakReference<Activity> mActivity;

		public MyHandler(MainPageActivity activity) {
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			// ((LoginActivity)mActivity.get()).showTip();

			if (dialog != null) {
				dialog.dismiss();
			}

			int flag = msg.what;
			switch (flag) {
			case 0:
				String errorMsg = (String) msg.getData().getSerializable(
						"ErrorMsg");
				((MainPageActivity) mActivity.get()).showTip(errorMsg);
				break;
			case FLAG_LOGIN_SUCCESS:
				((MainPageActivity) mActivity.get()).showTip(MSG_LOGI_SUCCESS);
				break;
			}
		}
	}

	private MyHandler handler = new MyHandler(this);

	// ˢ��
	private void refresh() {
		/**
		 * loading.....
		 * */
		if (dialog == null) {
			dialog = new ProgressDialog(MainPageActivity.this);
		}
		dialog.setTitle("��ȴ�");
		dialog.setMessage("ˢ����...");
		dialog.setCancelable(false);
		dialog.show();

		/**
		 * ���߳�
		 */
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(FLAG_LOGIN_SUCCESS);
			}
		});
		thread.start();
	}

	private void popuMenuClicked(View click) {
		popupMenu = new PopupMenu(MainPageActivity.this, click);
		getMenuInflater().inflate(R.menu.popu_menu, popupMenu.getMenu());
		popupMenu
				.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						switch (item.getItemId()) {
						case R.id.menu_edit:
							// ���û���κ����ݣ����֮�󲻲����κη�Ӧ
							if (list == null || list.size() <= 0) {
								ToastUtils.makeToast(MainPageActivity.this,
										"�����κ�����");
							} else {
								if (flag == 1) {
									createWindow(11, 14);
									delate.setVisibility(View.VISIBLE);
									saveDocument.setVisibility(View.VISIBLE);
									flag = 0;
								}
							}
							break;
						case R.id.menu_refresh:
							refresh();
							break;
						case R.id.menu_setting:
							startActivity(new Intent(MainPageActivity.this,
									SettingActivity.class));
							break;
						case R.id.menu_add_new_content:
							startActivity(new Intent(MainPageActivity.this,
									AddNewActivity.class));
							break;
						}
						popupMenu.dismiss();
						return true;
					}
				});
		popupMenu.show();
	}

	// ����ɾ�����¼�
	Handler handler_delete = new Handler() {
		public void handleMessage(Message msg) {
			dialog2.dismiss();
			if (msg.what == OK) {
				// ɾ���ɹ��󣬴ӱ������ݿ����ɾ��
				List<Object> receive = (List<Object>) msg.obj;
				System.out.println("rece---list" + receive);
				URLInfoBean urlInfoBean = (URLInfoBean) receive.get(0);
				int index = Integer.parseInt((String) receive.get(1));
				Log.i("urlinfo-delete", urlInfoBean + "");

				if (!updateData(urlInfoBean)) {
					which_one.add(list.get(in.get(index)).getWebUrl());
					state = false;
				} else {
					state = true;
					list.remove((int) in.get(index));
					adapter.notifyDataSetChanged();

				}

				// if(!deleteData(url)){
				// which_one.add(url);
				// state = false;
				// }else{
				// state = true;
				// list.remove((int) in.get(index));
				// adapter.notifyDataSetChanged();
				// }

			} else if (msg.what == SERVER_ERROR) {
				String url = (String) msg.obj;
				ToastUtils.makeToast(MainPageActivity.this, "ɾ��ʧ��");
				which_one.add(url);
			}

			// if(state){
			// ToastUtils.makeToast(MainPageActivity.this, "ɾ���ɹ�");
			// }else{
			// if(which_one != null){
			// String urls = "";
			// for(int i = 0; i < which_one.size(); i++){
			// urls = urls + "," + which_one.get(i);
			// }
			// ToastUtils.makeToast(MainPageActivity.this, "��ַ��"+urls+"δɾ���ɹ�");
			// }
			// }
			//
			// if(list.size() <= 0){
			// list.clear();
			// list = null;
			// }
			// adapter.notifyDataSetChanged();
			// in.clear();
			// in = new ArrayList<Integer>();
			// isDeleted = false;
			// exitShowFace();
		};
	};

	Handler handler_delete_finish = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == FINISH) {
				Map<String, Object> map = (Map<String, Object>) msg.obj;
				List<WebContentBean> lists = (List<WebContentBean>) map
						.get("list");
				List<Integer> ins = (List<Integer>) map.get("in");

				if (state) {
					ToastUtils.makeToast(MainPageActivity.this, "ɾ���ɹ�");
				} else {
					if (which_one != null) {
						String urls = "";
						for (int i = 0; i < which_one.size(); i++) {
							urls = urls + "," + which_one.get(i);
						}
						ToastUtils.makeToast(MainPageActivity.this, "��ַ��"
								+ urls + "δɾ���ɹ�");
					}
				}
				if (lists.size() <= 0) {
					list.clear();
					list = null;
				}
				adapter.notifyDataSetChanged();

				in.clear();
				in = new ArrayList<Integer>();
				isDeleted = false;
				exitShowFace();
			}
		};
	};

	/**
	 * �ӱ������ݿ�ɾ����Ϣ
	 * 
	 * @param url
	 * @return
	 */
	public boolean deleteData(String url) {
		int isDelete = DBPerform.deleteURLInfoByUrl(MainPageActivity.this, url);
		if (isDelete < 0) {
			return false;
		}
		return true;

	}

	/**
	 * �޸ı������ݿ�url������
	 * 
	 * @param urlInfoBean
	 * @return
	 */
	public boolean updateData(URLInfoBean urlInfoBean) {
		int isDelete = DBPerform.updateUrlInfo(MainPageActivity.this,
				urlInfoBean);
		if (isDelete < 0) {
			return false;
		}
		return true;
	}

	private void delete() {
		if (in != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainPageActivity.this)
					.setMessage("�Ƿ�ɾ������Ʒ��Ϣ")
					.setPositiveButton("��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									Collections.sort(in);
									dialog2 = DialogUtils.createProgressDialog(
											MainPageActivity.this, "ɾ��",
											"ɾ����....���Ժ�");
									dialog2.show();
									new Thread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											for (int i = in.size() - 1; i >= 0; i--) {
												if (adapter.list.get(in.get(i))
														.isChecked() == true) {
													System.out
															.println("in-value----="
																	+ in);
													final String url = list
															.get(in.get(i))
															.getWebUrl();
													URLInfoBean urlInfoBean = new URLInfoBean();
													urlInfoBean.setUrlId(list
															.get(in.get(i))
															.getWebId());
													urlInfoBean.setUrlContent(list
															.get(in.get(i))
															.getWebContent());
													urlInfoBean.setUrlLink(list
															.get(in.get(i))
															.getWebUrl());
													urlInfoBean.setUrlTag(1);
													urlInfoBean.setUrlTitle(list
															.get(in.get(i))
															.getWebTitle());
													// ����Ƿ�������״��
													if (CheckNetWork
															.isNetWorkConnected(MainPageActivity.this)) {// ����
														final int index = i;
														// ͬ��������������ɾ��
														// TODO Auto-generated
														// method stub
														System.out.println("list.size="
																+ list.size());
														System.out
																.println("in-value="
																		+ in);
														System.out
																.println("delete-url="
																		+ list.get(
																				in.get(index))
																				.getWebUrl());
														Log.i("delete-url",
																list.get(
																		in.get(index))
																		.getWebUrl());
														// ����json��ʽ��
														String jsonString = ConstructJsonFormat
																.json_delete(
																		url,
																		LoginActivity.USER_EMAIL_STR);
														String receive = TCPRequest
																.tcpRequest(
																		jsonString,
																		AddressUtil.TCT_ADDRESS,
																		AddressUtil.TCP_PORT);
														System.out
																.println("receive="
																		+ receive);
														String describe = JsonResolve
																.fun_des_resolve(receive);
														Message message = handler_delete
																.obtainMessage();
														if (describe
																.equals(BackTagUtils.OK)) {
															message.what = OK;
															List<Object> send = new ArrayList<Object>();
															send.add(urlInfoBean);
															send.add(index + "");
															message.obj = send;
															// if(!deleteData(list.get(in.get(index)).getWebUrl())){
															// which_one.add(list.get(in.get(index)).getWebUrl());
															// state = false;
															// }else{
															// state = true;
															// list.remove((int)
															// in.get(index));
															// }
														} else {
															message.what = SERVER_ERROR;
															message.obj = url;
															// which_one.add(list.get(in.get(index)).getWebUrl());
														}
														handler_delete
																.sendMessage(message);
													} else {
														// �ڱ��ؿ��Ƚ����޸�url��ı�־λ������Ϊ1������Ҫɾ������ͬ�����ݵ�ʱ���ڽ���������ɾ��
														if (!updateData(urlInfoBean)) {
															which_one.add(list
																	.get(in.get(i))
																	.getWebUrl());
															state = false;
														} else {
															state = true;
															list.remove((int) in
																	.get(i));

														}
														// if(!deleteData(list.get(in.get(i)).getWebUrl())){
														// which_one.add(list.get(in.get(i)).getWebUrl());
														// state = false;
														// }else{
														// state = true;
														// list.remove((int)
														// in.get(i));
														// }
													}
													// list.remove((int)
													// in.get(i));
												}
											}

											Message messages = handler_delete_finish
													.obtainMessage();
											// ����ɾ���������UI�߳̽�����ʾ
											Map<String, Object> map = new HashMap<String, Object>();
											map.put("list", list);
											map.put("in", in);
											// if(list.size() < 0){
											// list.clear();
											// list = null;
											// }
											// adapter.notifyDataSetChanged();
											messages.what = FINISH;
											messages.obj = map;
											handler_delete_finish
													.sendMessage(messages);

										}
									}).start();

									// if(state){
									// ToastUtils.makeToast(MainPageActivity.this,
									// "ɾ���ɹ�");
									// }else{
									// if(which_one != null){
									// String urls = "";
									// for(int i = 0; i < which_one.size();
									// i++){
									// urls = urls + "," + which_one.get(i);
									// }
									// ToastUtils.makeToast(MainPageActivity.this,
									// "��ַ��"+urls+"δɾ���ɹ�");
									// }
									// }
									//
									// if(list.size() <= 0){
									// list.clear();
									// list = null;
									// }
									// adapter.notifyDataSetChanged();
									// in.clear();
									// in = new ArrayList<Integer>();
									// isDeleted = false;
									// exitShowFace();
									arg0.dismiss();
								}
							})
					.setNegativeButton("��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
								}
							});
			builder.create().show();
		} else {
			Toast.makeText(MainPageActivity.this, "�Բ���û����Ʒ����ɾ����!",
					Toast.LENGTH_SHORT).show();
		}
	}

	// ��listView�����ȡ�±�Position��Ӧ������
	private WebContentBean getContentFromListView(int position) {
		WebContentBean webContentBean = (WebContentBean) listView
				.getItemAtPosition(position);
		return webContentBean;
	}

	// ��ȡҪ�浵������
	private List<WebContentBean> getSaveContent() {
		List<WebContentBean> list_web = new ArrayList<WebContentBean>();

		if (in != null || in.size() > 0) {
			for (int i = 0; i < in.size(); i++) {
				list_web.add(getContentFromListView(in.get(i)));
			}
		}

		return list_web;
	}

	/**
	 * ����浵��ʵ�ִ浵
	 */
	private void save() {
		List<WebContentBean> list_content = getSaveContent();
		// FileUtils.write(MainPageActivity.this, list_content);
		// ��ȡUserId
		int userId = DBPerform.queryUserInfoByEmail(MainPageActivity.this,
				LoginActivity.USER_EMAIL_STR).getUserId();
		// ��Ҫ�浵�����ݷ���浵��Ϣ������
		if (list_content != null) {
			for (int i = 0; i < list_content.size(); i++) {
				SaveInfoBean bean = new SaveInfoBean();
				bean.setUrlId(list_content.get(i).getWebId());
				bean.setUserId(userId);
				DBPerform.insertSaveInfo(MainPageActivity.this, bean);
			}
		}
		// ����Ƿ�ɹ��浵
		List<SaveInfoBean> beans = DBPerform
				.querySaveInfo(MainPageActivity.this);
		Log.i("saveinfo", beans + "");
		showTip("�Ѵ浵");
		adapter.notifyDataSetChanged();
		exitShowFace();
	}

	// ɾ����֮��ȡ�����水ť����ʾ
	private void exitShowFace() {
		flag = 1;
		createWindow(1, 1);
		setVisibility();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.coolReaderDrawer:
			drawerLayout.openDrawer(Gravity.LEFT);
			break;
		case R.id.coolReaderMenu:
			popuMenuClicked(v);
			break;
		case R.id.coolReaderSearch:
			Intent intent = new Intent();
			intent.setClass(MainPageActivity.this, SearchActivity.class);
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("listInfo", (Serializable) list);
			// intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.coolReaderFile:
			startActivity(new Intent(MainPageActivity.this,
					SaveFileActivity.class));
			break;
		case R.id.coolReaderListing:
			drawerLayout.closeDrawers();
			break;
		case R.id.coolReader_delete:
			isDeleted = true;
			delete();
			break;
		case R.id.coolReader_save_document:
			isSaveFile = true;
			save();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				SysApplication.getInstance().exit();
			}
			// startActivity(new
			// Intent(MainPageActivity.this,LoginActivity.class));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// ��ȡ��Ӧitem��url
	public WebContentBean getUrl(int position) {
		WebContentBean bean = (WebContentBean) listView
				.getItemAtPosition(position);
		return bean;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		WebContentBean bean = getUrl(position);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable(TAG, bean);
		bundle.putString("key", KEY);
		intent.putExtras(bundle);
		intent.setClass(MainPageActivity.this, ShowURLContentActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// �ж�listview�����checkBox�Ƿ�Ϊ��ʾ״̬������ǣ������䲻��ʾ��������ʾ
		if (flag == 0) {// ����Ǳ༭��ť����ĳЩcheckBoxΪ��ʾ״̬������Ϊ����ʾ״̬
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setChecked(false);
				}
			}
			flag = 1;
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
		}
		
		//�жϵ��µ�ɾ����浵ѡ���Ƿ���״̬������ǣ�������ʾ
		if(delate.getVisibility() == View.VISIBLE || saveDocument.getVisibility() == View.VISIBLE){
			delate.setVisibility(View.GONE);
			saveDocument.setVisibility(View.GONE);
		}
		
		//�жϻ��������Ƿ�ر�
		if(drawerLayout.isActivated()){
			drawerLayout.closeDrawers();
		}
	}
}