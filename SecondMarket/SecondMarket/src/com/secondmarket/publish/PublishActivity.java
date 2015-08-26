package com.secondmarket.publish;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.Request.HTTPRequest;
import com.publicInfo.Address;
import com.secondmarket.DB.DBCreateWord;
import com.secondmarket.DB.DBHelper;
import com.secondmarket.DB.DBPerform;
import com.secondmarket.bean.SortSearchDemo;
import com.secondmarket.load.LoadActivity;
import com.secondmarket.load.R;

public class PublishActivity extends Activity implements OnItemClickListener {

	private GridView gridview;
	private GridAdapter adapter;

	private Button activity_selectimg_send;
	// private Button back;
	private EditText comment_name, comment_price, comment_connect, comment_describe;
	private String temp;
	private Button selectimg_bt_content_type, selectimg_bt_search;
	private LinearLayout selectimg_relativeLayout_below;
	private ScrollView activity_selectimg_scrollView;
	private HorizontalScrollView selectimg_horizontalScrollView;
	private List<String> categoryList;
	private float dp;
	
	private String contentName ;
	private String price;
	private String connect;
	private String contentDescribe;
	private int goodsTypeId;
	private int userId = LoadActivity.list1.get(0).getUserId();
	private List<Map<String, String>> lists;
	private SortSearchDemo searchDemo = new SortSearchDemo();
	// Spinner设置
	private Spinner sp;
	ArrayList<String> list = new ArrayList<String>();
	ArrayAdapter<String> spadapter;
	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	private String typeString = "";//获得确定后的类别名称
	private List<String> urList = new ArrayList<String>();
	
	private final String FORM_NOT_COMPLETE = "form_not_complete";
	private final String UPLOADSUCCESS = "UpLoadSuccess";
	private final String UPLOADFAIL = "UpLoadFail";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);

		Init();
		// Spinner设置
		sp = (Spinner) findViewById(R.id.sp);
		// 获取相应对象
		String[] ls = getResources().getStringArray(R.array.action);
		// 获取XML中定义的数组
		for (int i = 0; i < ls.length; i++) {
			list.add(ls[i]);
		}
		// 把数组导入到ArrayList中
		spadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		spadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 设置下拉菜单的风格
		sp.setAdapter(spadapter);
		sp.setOnItemSelectedListener(new SPListener());
		// 绑定适配器
		sp.setPrompt("标题栏");
	}

	class SPListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			typeString = list.get(arg2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", userId+""));
			params.add(new BasicNameValuePair("goodsName", contentName));
			params.add(new BasicNameValuePair("goodsDescribe", contentDescribe));
			params.add(new BasicNameValuePair("goodsType", goodsTypeId+""));
			params.add(new BasicNameValuePair("goodsPrice", price));
			params.add(new BasicNameValuePair("goodsConnect", connect));
			String jsonString = HTTPRequest.getStringFromHttpOfLoad(Address.SENDGOODSINFO_ADDRESS, params);
			System.out.println("publishJsonString="+jsonString);
			Message msg = handler.obtainMessage(); 
			if(jsonString.equals(FORM_NOT_COMPLETE)){
				msg.what = 0x02;
			}else {
				msg.what = 0x01;
			}
			handler.sendMessage(msg);
		}
	};
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x01){
				System.out.println("sendgoodsInfo");
				new Thread(runnable2).start();
				store2Sqlite();
				Toast.makeText(PublishActivity.this, "上传物品信息成功！", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(PublishActivity.this,
						PublishsuccActivity.class);
				startActivity(intent);
			}else if(msg.what == 0x02){
				Toast.makeText(PublishActivity.this, "表单不完整，上传物品信息失败！", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	//上传数据
	public void sendData(){
		new Thread(runnable).start();
	}

	//将数据存放在sqlite
	private void store2Sqlite(){
		DBPerform.createPublishTable(PublishActivity.this);
		int flag = DBPerform.insertPublishTable(PublishActivity.this, searchDemo, DBCreateWord.TB_PUBNAME);
		if(flag == 1){
			Toast.makeText(PublishActivity.this, "恭喜你，发布成功！",
					Toast.LENGTH_SHORT).show();
			
		}else if(flag == 0){
			Toast.makeText(PublishActivity.this, "该商品已经发布，不能重新发布！",
					Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(PublishActivity.this, "对不起，发布失败！",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	Handler handler2 = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 0x03){
				Toast.makeText(PublishActivity.this, "上传物品图片成功!", Toast.LENGTH_SHORT).show();
			}else if(msg.what == 0x04){
				Toast.makeText(PublishActivity.this, "上传物品图片失败!", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	Runnable runnable2 = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("UserName", "fy"));
			params.add(new BasicNameValuePair("UserSex", "nv"));
			params.add(new BasicNameValuePair("UserLevel", "dasan"));
			File image = new File(lists.get(0).get("goodsPictureAD1"));
//			params.add(new BasicNameValuePair("image", image));
			String jsonString = HTTPRequest.getStringFromHttpOfLoad(Address.SENDGOODSPICTURE_ADDRESS, null);
			Message msg = handler2.obtainMessage(); 
			System.out.println("2222jsonString="+jsonString);
			if(jsonString.equals(UPLOADSUCCESS)){
				msg.what = 0x03;
			}else if(jsonString.equals(UPLOADFAIL)){
				msg.what = 0x04;
			}
			handler2.sendMessage(msg);
		}
	};
	
	//上传物品图片
	public void sendGoodsPicture(){
		
	}
	
	//判断用户是否是第一次登陆
	public Boolean judgeLoaded(){
		SharedPreferences setting;
		final String INITIALIZED = "initlized";
		Boolean userFirst;
		//打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
		setting = getSharedPreferences("setting", 0);
		//取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		userFirst = setting.getBoolean("FIRST", true);
		setting.edit().putBoolean("FIRST", false).commit();
		return userFirst;
	}
	
	public void Init() {
		dp = getResources().getDimension(R.dimen.dp);
		comment_name = (EditText) findViewById(R.id.comment_name);
		comment_name.setFocusable(true);
		comment_name.setFocusableInTouchMode(true);
		comment_price = (EditText) findViewById(R.id.comment_price);
		comment_price.setFocusable(true);
		comment_price.setFocusableInTouchMode(true);
		comment_connect = (EditText) findViewById(R.id.comment_connect);
		comment_connect.setFocusable(true);
		comment_connect.setFocusableInTouchMode(true);
		comment_describe = (EditText)findViewById(R.id.comment_describe);
		comment_describe.setFocusable(true);
		comment_describe.setFocusableInTouchMode(true);

		selectimg_horizontalScrollView = (HorizontalScrollView) findViewById(R.id.selectimg_horizontalScrollView);
		gridview = (GridView) findViewById(R.id.noScrollgridview);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridviewInit();
		comment_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				temp = s.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		activity_selectimg_send = (Button) findViewById(R.id.activity_selectimg_send);
		activity_selectimg_send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				System.out.println("onclick");
				if (bmp.size() < 1) {
					Toast.makeText(getApplicationContext(), "至少需要一张图片",
							Toast.LENGTH_SHORT).show();
					return;
				}
				System.out.println("u8u8u8");
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(PublishActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				System.out.println("u8u8u81111");
				contentName = comment_name.getText().toString().trim();
				price = comment_price.getText().toString().trim();
				connect = comment_connect.getText().toString().trim();
				contentDescribe = comment_describe.getText().toString().trim();
				System.out.println("u8u8u822222");
				if (contentName.equals("") || price.equals("")
						|| connect.equals("")) {
					Toast.makeText(getApplicationContext(), "发布的内容不能为空",
							Toast.LENGTH_SHORT).show();
					System.out.println("u8u8u83333333");
					return;
				}
				for (int i = 0; i < drr.size(); i++) {
					urList.add(drr.get(i));
					System.out.println("u8u8u844444444");
				}
				activity_selectimg_send.setEnabled(false);
				if (bmp.size() == 3) {
					System.out.println("u8u8u845555555");
					//SortSearchDemo [isSale=0, goodsId=1, goodsConnect=1, userId=1, goodsDescribe=高数书, 
					//goodsTypeId=1, goodsPrice=23.0, goodsWanted=1, goodsPublishTime=2014-1-1, 
					//goodsName=高数, list=[{goodsPictureAD1=android.jpg}]]
					/*if(judgeLoaded()){//第一次登陆
						if(DBPerform.insertCountTable(PublishActivity.this, 1, 1, DBHelper.COUNT) == 1){//成功
							count = 1;
						}
					}else{
						count = DBPerform.selectCountTable(PublishActivity.this, DBHelper.COUNT, 1);
					}
					searchDemo.setGoodsId(count);*/
					searchDemo.setGoodsId(0);
					searchDemo.setIsSale(0);
					searchDemo.setGoodsConnect(connect);
					searchDemo.setUserId((LoadActivity.list1.get(0).getUserId()));
					searchDemo.setGoodsDescribe(contentDescribe);
					if(typeString.equals("书籍报刊")){
						goodsTypeId = 1;
						searchDemo.setGoodsTypeId("1");
					}else if(typeString.equals("数码电子")){
						goodsTypeId = 2;
						searchDemo.setGoodsTypeId("2");
					}else if(typeString.equals("交通工具")){
						goodsTypeId = 3;
						searchDemo.setGoodsTypeId("3");
					}else if(typeString.equals("生活用品")){
						goodsTypeId = 4;
						searchDemo.setGoodsTypeId("4");
					}else if(typeString.equals("体育用品")){
						goodsTypeId = 5;
						searchDemo.setGoodsTypeId("5");
					}else if(typeString.equals("衣物服饰")){
						goodsTypeId = 6;
						searchDemo.setGoodsTypeId("6");
					}else if(typeString.equals("免费赠品")){
						goodsTypeId = 7;
						searchDemo.setGoodsTypeId("7");
					}else if(typeString.equals("其他")){
						goodsTypeId = 8;
						searchDemo.setGoodsTypeId("8");
					}
					searchDemo.setGoodsPrice(price);
					searchDemo.setGoodsWanted(1);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String time = df.format(new Date());
					searchDemo.setGoodsPublishTime(time);
					searchDemo.setGoodsName(contentName);
					lists = new ArrayList<Map<String,String>>();
					Map<String, String> map = new HashMap<String, String>();
					if(urList.size() == 0){
						for(int i = 0; i < 3; i++)
							map.put("goodsPictureAD"+(i+1), null);
					}else if(urList.size() == 1){
						map.put("goodsPictureAD1", urList.get(0));
						for(int i = 1; i < 3; i++){
							map.put("goodsPictureAD"+(i+1), null);
						}
					}else if(urList.size() == 2){
						map.put("goodsPictureAD1", urList.get(0));
						map.put("goodsPictureAD2", urList.get(1));
						map.put("goodsPictureAD3", null);
					}else if(urList.size() >= 3){
						map.put("goodsPictureAD1", urList.get(0));
						map.put("goodsPictureAD2", urList.get(1));
						map.put("goodsPictureAD3", urList.get(2));
					}
					lists.add(map);
					searchDemo.setList(lists);
					
					//上传数据
					sendData();
				}else{
					Toast.makeText(PublishActivity.this, "至少需要三张图片", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		selectimg_relativeLayout_below = (LinearLayout) findViewById(R.id.selectimg_relativeLayout_below);
		activity_selectimg_scrollView = (ScrollView) findViewById(R.id.activity_selectimg_scrollView);
		activity_selectimg_scrollView.setVerticalScrollBarEnabled(false);

		final View decorView = getWindow().getDecorView();
		final WindowManager wm = this.getWindowManager();

		decorView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						@SuppressWarnings("deprecation")
						int displayheight = wm.getDefaultDisplay().getHeight();
						Rect rect = new Rect();
						decorView.getWindowVisibleDisplayFrame(rect);
						int dynamicHight = rect.bottom - rect.top;
						float ratio = (float) dynamicHight
								/ (float) displayheight;

						if (ratio > 0.2f && ratio < 0.6f) {
							selectimg_relativeLayout_below
									.setVisibility(View.VISIBLE);
							activity_selectimg_scrollView.scrollBy(0,
									activity_selectimg_scrollView.getHeight());
						} else {
							selectimg_relativeLayout_below
									.setVisibility(View.GONE);
						}
					}
				});

	}

	public void gridviewInit() {
		adapter = new GridAdapter(this);
		adapter.setSelectedPosition(0);
		int size = 0;
		if (bmp.size() < 3) {
			size = bmp.size() + 1;
		} else {
			size = bmp.size();
		}
		LayoutParams params = gridview.getLayoutParams();
		final int width = size * (int) (dp * 9.4f);
		params.width = width;
		gridview.setLayoutParams(params);
		gridview.setColumnWidth((int) (dp * 9.4f));
		gridview.setStretchMode(GridView.NO_STRETCH);
		gridview.setNumColumns(size);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);

		selectimg_horizontalScrollView.getViewTreeObserver()
				.addOnPreDrawListener(// 绘制完毕
						new OnPreDrawListener() {
							public boolean onPreDraw() {
								selectimg_horizontalScrollView.scrollTo(width,
										0);
								selectimg_horizontalScrollView
										.getViewTreeObserver()
										.removeOnPreDrawListener(this);
								return false;
							}
						});
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater listContainer;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public class ViewHolder {
			public ImageView image;
			public Button bt;
		}

		public GridAdapter(Context context) {
			listContainer = LayoutInflater.from(context);
		}

		public int getCount() {
			if (bmp.size() < 6) {
				return bmp.size() + 1;
			} else {
				return bmp.size();
			}
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int sign = position;
			// 自定义视图
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				// 获取list_item布局文件的视图

				convertView = listContainer.inflate(
						R.layout.item_published_grida, null);

				// 获取控件对象
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				holder.bt = (Button) convertView
						.findViewById(R.id.item_grida_bt);
				// 设置控件集到convertView
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				holder.bt.setVisibility(View.GONE);
				if (position == 3) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(bmp.get(position));
				holder.bt.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						PhotoActivity.bitmap.remove(sign);
						bmp.get(sign).recycle();
						bmp.remove(sign);
						drr.remove(sign);

						gridviewInit();
					}
				});
			}

			return convertView;
		}
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			super(mContext);
			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			// ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
			// R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					Intent i = new Intent(
							// 相册
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, RESULT_LOAD_IMAGE);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 0;
	private static final int RESULT_LOAD_IMAGE = 1;
	private static final int CUT_PHOTO_REQUEST_CODE = 2;
	private static final int SELECTIMG_SEARCH = 3;
	private String path = "";
	private Uri photoUri;

	public void photo() {
		try {
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = android.os.Environment
					.getExternalStorageDirectory().getPath() + "/tempImage/";
			File file = null;
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				// 有sd卡，是否有myImage文件夹
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// 是否有headImg文件
				file = new File(sdcardPathDir + System.currentTimeMillis()
						+ ".JPEG");
			}
			if (file != null) {
				path = file.getPath();
				photoUri = Uri.fromFile(file);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (drr.size() < 6 && resultCode == -1) {// 拍照
				startPhotoZoom(photoUri);
			}
			break;
		case RESULT_LOAD_IMAGE:
			if (drr.size() < 6 && resultCode == RESULT_OK && null != data) {// 相册返回
				Uri uri = data.getData();
				if (uri != null) {
					startPhotoZoom(uri);
				}
			}
			break;
		case CUT_PHOTO_REQUEST_CODE:
			if (resultCode == RESULT_OK && null != data) {// 裁剪返回
				Bitmap bitmap = Bimp.getLoacalBitmap(drr.get(drr.size() - 1));
				PhotoActivity.bitmap.add(bitmap);
				bitmap = Bimp.createFramedPhoto(480, 480, bitmap,
						(int) (dp * 1.6f));
				bmp.add(bitmap);
				gridviewInit();
			}
			break;
		case SELECTIMG_SEARCH:
			if (resultCode == RESULT_OK && null != data) {
				String text = "#" + data.getStringExtra("topic") + "#";
				text = comment_name.getText().toString() + text;
				comment_name.setText(text);

				comment_name.getViewTreeObserver().addOnPreDrawListener(
						new OnPreDrawListener() {
							public boolean onPreDraw() {
								comment_name.setEnabled(true);
								// 设置光标为末尾
								CharSequence cs = comment_name.getText();
								if (cs instanceof Spannable) {
									Spannable spanText = (Spannable) cs;
									Selection.setSelection(spanText,
											cs.length());
								}
								comment_name.getViewTreeObserver()
										.removeOnPreDrawListener(this);
								return false;
							}
						});

			}

			break;
		}
	}

	private void startPhotoZoom(Uri uri) {
		try {
			// 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyyMMddhhmmss");
			String address = sDateFormat.format(new java.util.Date());
			if (!FileUtils.isFileExist("")) {
				FileUtils.createSDDir("");

			}
			drr.add(FileUtils.SDPATH + address + ".JPEG");
			Uri imageUri = Uri.parse("file:///sdcard/formats/" + address
					+ ".JPEG");

			final Intent intent = new Intent("com.android.camera.action.CROP");

			// 照片URL地址
			intent.setDataAndType(uri, "image/*");

			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 480);
			intent.putExtra("outputY", 480);
			// 输出路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			// 输出格式
			intent.putExtra("outputFormat",
					Bitmap.CompressFormat.JPEG.toString());
			// 不启用人脸识别
			intent.putExtra("noFaceDetection", false);
			intent.putExtra("return-data", false);
			startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void onDestroy() {

		FileUtils.deleteDir(FileUtils.SDPATH);
		FileUtils.deleteDir(FileUtils.SDPATH1);
		// 清理图片缓存
		for (int i = 0; i < bmp.size(); i++) {
			bmp.get(i).recycle();
		}
		for (int i = 0; i < PhotoActivity.bitmap.size(); i++) {
			PhotoActivity.bitmap.get(i).recycle();
		}
		PhotoActivity.bitmap.clear();
		bmp.clear();
		drr.clear();
		super.onDestroy();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(PublishActivity.this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		if (arg2 == bmp.size()) {
			String sdcardState = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				new PopupWindows(PublishActivity.this, gridview);
			} else {
				Toast.makeText(getApplicationContext(), "sdcard已拔出，不能选择照片",
						Toast.LENGTH_SHORT).show();
			}
		} else {

			Intent intent = new Intent(PublishActivity.this,
					PhotoActivity.class);

			intent.putExtra("ID", arg2);
			startActivity(intent);
		}
	}

}
