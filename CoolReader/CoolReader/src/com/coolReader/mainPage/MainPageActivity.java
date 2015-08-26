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
	public static int flag = 1;// 判断点击编辑按钮

	static ProgressDialog dialog;
	Dialog dialog2;

	private static final String MSG_LOGI_SUCCESS = "更新成功。";
	private static final int FLAG_LOGIN_SUCCESS = 1;
	public static final String TAG = "urlContent";
	public static final String KEY = "MainPageActivity";
	private final int OK = 1;
	private final int SERVER_ERROR = 2;
	private final int FINISH = 3;

	// 判断是否全部都删除成功
	boolean state = true;
	// 记录那些选项没有被删除
	List<String> which_one = new ArrayList<String>();

	// 保存用户图像
	private Bitmap userPicture = null;

	// 存放listView的数据集合
	private List<WebContentBean> list = new ArrayList<WebContentBean>();
	// 当用户点击多个选项时，存储点击的选项的position
	public static List<Integer> in = new ArrayList<Integer>();
	private MainPageAdapter adapter;
	public static boolean isSaveFile = false;
	public static boolean isDeleted = false;
//	public String urls_synchro = "";
	public List<String> urls_synchro = null;// 同步数据的时候，存放已经标记为删除的url

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

		// 查看数据库内容
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
	 * 在有网的情况下同步数据
	 */
	public void synchroData() {
		if (CheckNetWork.isNetWorkConnected(MainPageActivity.this)) {
			// 在本地数据库中查找用户删除的url，即url表中tag=1的内容
			if (urls_synchro.size() > 0 || urls_synchro != null) {
//				if (urls_synchro.charAt(urls_synchro.length() - 1) == '&') {
//					urls_synchro = urls_synchro.substring(0,
//							urls_synchro.length() - 1);
//				}
				Log.i("urls_synchro", urls_synchro+"");
				String jsonString = ConstructJsonFormat.json_synchroData(
						LoginActivity.USER_EMAIL_STR, urls_synchro);
				// 进行与服务器的同步操作
				String receive = TCPRequest.tcpRequest(jsonString,
						AddressUtil.TCT_ADDRESS, AddressUtil.TCP_PORT);
				Log.i("syn-receive", receive);
				dealResult(receive);
			}
		}
	}

	/**
	 * 处理返回的同步数据
	 * @param receive
	 */
	public void dealResult(String receive){
		List<Map<String, Object>> add = JsonResolve.json_synchro(receive);
		deal_PC_delated((List<String>)add.get(0).get("return1"));
		deal_And_delated((String)add.get(0).get("return2"));
		deal_add_newinfo((List<URLInfoBean>)add.get(0).get("return3"));
	}
	
	/**
	 * 处理同步中PC端删除的而Android端没有删除的
	 * @param pcDeleted
	 */
	public void deal_PC_delated(List<String> pcDeleted){
		//删除本地数据库中的内容
		if(pcDeleted != null){
			int len = pcDeleted.size();
			//记录没有删除成功的url
			List<String> stringBuffer = new ArrayList<String>();
			//删除成功的标识
			int tag = -1;
			for(int i = 0; i < len; i++){
				String url = pcDeleted.get(i);
				tag = DBPerform.deleteURLInfoByUrl(MainPageActivity.this, url);
				if(tag < 0){
					stringBuffer.add(url);
				}
			}
			//如果有的没有删除成功，重新进行删除
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
	 * 处理Android要删除的上传服务器后服务器返回OK后Android真正的删除本地的
	 * @param andDeleted
	 */
	public void deal_And_delated(String andDeleted){
		//删除Android端要删除的内容
		//获取所有url
		List<URLInfoBean> urls = DBPerform.queryURLInfo(MainPageActivity.this);
		//保存要删除的url
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
				//记录没有删除成功的url
				List<String> stringBuffer = new ArrayList<String>();
				//删除成功的标识
				int tag = -1;
				for(int i = 0; i < lens; i++){
					String url = deleting.get(i);
					tag = DBPerform.deleteURLInfoByUrl(MainPageActivity.this, url);
					if(tag < 0){
						stringBuffer.add(url);
					}
				}
				//如果有的没有删除成功，重新进行删除
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
	 * 同步Android端未缓存的页面
	 * @param newInfo
	 */
	public void deal_add_newinfo(List<URLInfoBean> newInfo){
		//先放在本地数据库，然后显示
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

	// 布局
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
	 * 从本地数据库获取url信息
	 */
	private void getData() {
		List<URLInfoBean> list_url = DBPerform
				.queryURLInfo(MainPageActivity.this);
		if (list != null) {
			urls_synchro = new ArrayList<String>();
			for (int i = 0; i < list_url.size(); i++) {
				if (list_url.get(i).getUrlTag() == 1) {// tag=1表明该数据已经在本地删除，则不能显示
					// 存放在StringBuffer中，实现有网情况下数据的同步
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
			Toast.makeText(MainPageActivity.this, "暂无任何信息!", Toast.LENGTH_SHORT)
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

	// 刷新
	private void refresh() {
		/**
		 * loading.....
		 * */
		if (dialog == null) {
			dialog = new ProgressDialog(MainPageActivity.this);
		}
		dialog.setTitle("请等待");
		dialog.setMessage("刷新中...");
		dialog.setCancelable(false);
		dialog.show();

		/**
		 * 副线程
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
							// 如果没有任何内容，点击之后不产生任何反应
							if (list == null || list.size() <= 0) {
								ToastUtils.makeToast(MainPageActivity.this,
										"暂无任何内容");
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

	// 处理删除的事件
	Handler handler_delete = new Handler() {
		public void handleMessage(Message msg) {
			dialog2.dismiss();
			if (msg.what == OK) {
				// 删除成功后，从本地数据库进行删除
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
				ToastUtils.makeToast(MainPageActivity.this, "删除失败");
				which_one.add(url);
			}

			// if(state){
			// ToastUtils.makeToast(MainPageActivity.this, "删除成功");
			// }else{
			// if(which_one != null){
			// String urls = "";
			// for(int i = 0; i < which_one.size(); i++){
			// urls = urls + "," + which_one.get(i);
			// }
			// ToastUtils.makeToast(MainPageActivity.this, "地址："+urls+"未删除成功");
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
					ToastUtils.makeToast(MainPageActivity.this, "删除成功");
				} else {
					if (which_one != null) {
						String urls = "";
						for (int i = 0; i < which_one.size(); i++) {
							urls = urls + "," + which_one.get(i);
						}
						ToastUtils.makeToast(MainPageActivity.this, "地址："
								+ urls + "未删除成功");
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
	 * 从本地数据库删除信息
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
	 * 修改本地数据库url的内容
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
					.setMessage("是否删除该商品信息")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									Collections.sort(in);
									dialog2 = DialogUtils.createProgressDialog(
											MainPageActivity.this, "删除",
											"删除中....请稍后");
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
													// 检查是否是网络状况
													if (CheckNetWork
															.isNetWorkConnected(MainPageActivity.this)) {// 有网
														final int index = i;
														// 同步到服务器进行删除
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
														// 构造json格式串
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
														// 在本地库先进行修改url表的标志位，让其为1，代表要删除。当同步数据的时候，在进行真正的删除
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
											// 发送删除完命令，让UI线程进行显示
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
									// "删除成功");
									// }else{
									// if(which_one != null){
									// String urls = "";
									// for(int i = 0; i < which_one.size();
									// i++){
									// urls = urls + "," + which_one.get(i);
									// }
									// ToastUtils.makeToast(MainPageActivity.this,
									// "地址："+urls+"未删除成功");
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
					.setNegativeButton("否",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
								}
							});
			builder.create().show();
		} else {
			Toast.makeText(MainPageActivity.this, "对不起，没有商品可以删除了!",
					Toast.LENGTH_SHORT).show();
		}
	}

	// 从listView里面获取下标Position对应的内容
	private WebContentBean getContentFromListView(int position) {
		WebContentBean webContentBean = (WebContentBean) listView
				.getItemAtPosition(position);
		return webContentBean;
	}

	// 获取要存档的内容
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
	 * 点击存档，实现存档
	 */
	private void save() {
		List<WebContentBean> list_content = getSaveContent();
		// FileUtils.write(MainPageActivity.this, list_content);
		// 获取UserId
		int userId = DBPerform.queryUserInfoByEmail(MainPageActivity.this,
				LoginActivity.USER_EMAIL_STR).getUserId();
		// 将要存档的内容放入存档信息表里面
		if (list_content != null) {
			for (int i = 0; i < list_content.size(); i++) {
				SaveInfoBean bean = new SaveInfoBean();
				bean.setUrlId(list_content.get(i).getWebId());
				bean.setUserId(userId);
				DBPerform.insertSaveInfo(MainPageActivity.this, bean);
			}
		}
		// 检查是否成功存档
		List<SaveInfoBean> beans = DBPerform
				.querySaveInfo(MainPageActivity.this);
		Log.i("saveinfo", beans + "");
		showTip("已存档");
		adapter.notifyDataSetChanged();
		exitShowFace();
	}

	// 删除完之后，取消下面按钮的显示
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
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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

	// 获取对应item的url
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
		// 判断listview里面的checkBox是否为显示状态，如果是，则让其不显示，否则显示
		if (flag == 0) {// 如果是编辑按钮，即某些checkBox为显示状态，让其为非显示状态
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
		
		//判断底下的删除与存档选项是否显状态，如果是，不让显示
		if(delate.getVisibility() == View.VISIBLE || saveDocument.getVisibility() == View.VISIBLE){
			delate.setVisibility(View.GONE);
			saveDocument.setVisibility(View.GONE);
		}
		
		//判断滑动窗口是否关闭
		if(drawerLayout.isActivated()){
			drawerLayout.closeDrawers();
		}
	}
}