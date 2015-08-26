package com.mynutritionstreet.goodFoodCircle;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.example.mynutritionstreet.R;
import com.mynutritionstreet.Bean.FoodCircleBean;
import com.mynutritionstreet.json.JsonResolve;
import com.mynutritionstreet.mainpage.MainPageActivity;
import com.mynutritionstreet.mixinfo.SysApplication;
import com.mynutritionstreet.util.DialogUtils;
import com.mynutritionstreet.util.HttpUtil;
import com.mynutritionstreet.util.ToastUtil;
import com.mynutritionstreet.util.UrlUtils;
import com.mynutritionstreet.util.VolleyUtil;

public class GoodFoodCircleActivity extends Activity implements OnClickListener {
	private ImageView backImg;
	private ListView listView;
	private Button publishBtn;
	
	//存放listView的数据集合
	private List<FoodCircleBean> list = new ArrayList<FoodCircleBean>();
	private GoodFoodCircleAdapter adapter;

	public static final int NONE = 0;
	private static final int IMAGE_CAMERA = 1;// 拍照
	private static final int PHOTOZOOM = 2;// 缩放
	public static final int PHOTORESOULT = 3;// 结果
	
	private PopupWindow popupWindow = null; 

	// 存储图片的路径
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private String fileName = "";
	private Bitmap photo = null;
	
	public String GALLERY_GET = "从图库获取";
	public String CAMERA_GET = "拍照获取";
	
	static Dialog dialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_goodfoodcircle);
		SysApplication.getInstance().addActivity(this);
		
		findView();
		onclick();
		setListView();
	}
	
	private void findView(){
		backImg = (ImageView)findViewById(R.id.goodFoodBack);
		listView = (ListView)findViewById(R.id.goodFoodListView);
		publishBtn = (Button)findViewById(R.id.goodFoodPublish);
	}
	
	private void onclick(){
		backImg.setOnClickListener(this);
		publishBtn.setOnClickListener(this);
	}
	
	/**
	 * 从服务器获取发布信息
	 */
//	private void getData(){
//		for(int i = 0; i < 15; i++){
////			GoodInfoBean goodFoodBean = new GoodInfoBean();
////			goodFoodBean.setPublisherPicture("picture");
////			goodFoodBean.setPublisher("fengyi"+i);
////			Date date = new Date();
////			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
////			String time = format.format(date);
////			goodFoodBean.setPublishTime(time);
////			goodFoodBean.setFoodPicture("picture");
////			list.add(goodFoodBean);
//		}
//	}
	private void getData(){
		dialog = DialogUtils.createProgressDialog(GoodFoodCircleActivity.this, "美食圈", "数据获取中，请稍后.....");
		dialog.show();
		HttpUtil goodRequest = new HttpUtil(UrlUtils.QUERY_FOOD_CIRCLE, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				checkResult(response);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				ToastUtil.showToast(GoodFoodCircleActivity.this, "网络错误", 2000);
			}
		});
		VolleyUtil.getRequestQueue().add(goodRequest);
	}
	
	//验证数据
	public void checkResult(String response){
		dialog.dismiss();
		Log.i("goodFood", response);
		if(response.equals("\"False\"")){
			ToastUtil.showToast(GoodFoodCircleActivity.this, "查询失败", 2000);
		}else{
			list = JsonResolve.json_query_good_food(response);
			adapter = new GoodFoodCircleAdapter(GoodFoodCircleActivity.this, list);
			listView.setAdapter(adapter);
		}
	}

	private void setListView(){
		getData();
	}
	
	public void showPopuWindow(View view , Bitmap photo) {
		View convertView = null;
		// 一个自定义的布局，作为显示的内容
		if(popupWindow == null || convertView == null){
			LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(
					R.layout.sample_popuwindow, null);
			popupWindow = new PopupWindow(convertView,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		}
		popupWindow.setTouchable(true);
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		// 点击空白处时，隐藏掉pop窗口
		popupWindow.setOutsideTouchable(true);
		 // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		// 在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

		popupWindow.setFocusable(true);
		// backgroundAlpha(0.5f);

		Button camera = (Button) convertView.findViewById(R.id.popuCameraTxt);
		Button picture = (Button) convertView.findViewById(R.id.popuPhotoTxt);
		Button exit = (Button) convertView.findViewById(R.id.popuExitTxt);
		
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("camera_gallery", CAMERA_GET);
				intent.setClass(GoodFoodCircleActivity.this, Camera_GalleryActivity.class);
				startActivity(intent);
				popupWindow.dismiss();
			}
		});
		
		picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("camera_gallery", GALLERY_GET);
				intent.setClass(GoodFoodCircleActivity.this, Camera_GalleryActivity.class);
				/*
                 * 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
                 * 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
                 */
				startActivityForResult(intent, 1000);
				popupWindow.dismiss();
			}
		});
		
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		
		
	}
	
	/**
     * 所有的Activity对象的返回值都是由这个方法来接收
     * requestCode:    表示的是启动一个Activity时传过去的requestCode值
     * resultCode：表示的是启动后的Activity回传值时的resultCode值
     * data：表示的是启动后的Activity回传过来的Intent对象
     */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("list---------");
		if(requestCode == 1000 && resultCode == 1001){
//			String userPic = "userPic";
//			String publisher = "user";
//			Date date = new Date();
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
//			String time = format.format(date);
//			String foodPic = data.getStringExtra("foodPic");
//			String foodName = data.getStringExtra("foodName");
//			String foodIntro = data.getStringExtra("foodIntro");
//			GoodInfoBean goodFoodBean = new GoodInfoBean();
//			goodFoodBean.setPublisherPicture(userPic);
//			goodFoodBean.setPublisher(publisher);
//			goodFoodBean.setPublishTime(time);
//			goodFoodBean.setFoodPicture(foodPic);
//			
//			list.add(goodFoodBean);
//			list.add(0, goodFoodBean);
			adapter.notifyDataSetChanged();
//			setListView();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(popupWindow.isShowing()){
			popupWindow.dismiss();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.goodFoodBack:
			startActivity(new Intent(GoodFoodCircleActivity.this, MainPageActivity.class));
			break;
		case R.id.goodFoodPublish:
			showPopuWindow(v,photo);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {
			startActivity(new Intent(GoodFoodCircleActivity.this, MainPageActivity.class));
        }
        return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		VolleyUtil.getRequestQueue().cancelAll(this);
	}
	
}
