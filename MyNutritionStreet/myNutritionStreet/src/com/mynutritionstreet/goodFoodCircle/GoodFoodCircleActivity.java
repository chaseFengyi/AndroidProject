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
	
	//���listView�����ݼ���
	private List<FoodCircleBean> list = new ArrayList<FoodCircleBean>();
	private GoodFoodCircleAdapter adapter;

	public static final int NONE = 0;
	private static final int IMAGE_CAMERA = 1;// ����
	private static final int PHOTOZOOM = 2;// ����
	public static final int PHOTORESOULT = 3;// ���
	
	private PopupWindow popupWindow = null; 

	// �洢ͼƬ��·��
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private String fileName = "";
	private Bitmap photo = null;
	
	public String GALLERY_GET = "��ͼ���ȡ";
	public String CAMERA_GET = "���ջ�ȡ";
	
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
	 * �ӷ�������ȡ������Ϣ
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
		dialog = DialogUtils.createProgressDialog(GoodFoodCircleActivity.this, "��ʳȦ", "���ݻ�ȡ�У����Ժ�.....");
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
				ToastUtil.showToast(GoodFoodCircleActivity.this, "�������", 2000);
			}
		});
		VolleyUtil.getRequestQueue().add(goodRequest);
	}
	
	//��֤����
	public void checkResult(String response){
		dialog.dismiss();
		Log.i("goodFood", response);
		if(response.equals("\"False\"")){
			ToastUtil.showToast(GoodFoodCircleActivity.this, "��ѯʧ��", 2000);
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
		// һ���Զ���Ĳ��֣���Ϊ��ʾ������
		if(popupWindow == null || convertView == null){
			LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(
					R.layout.sample_popuwindow, null);
			popupWindow = new PopupWindow(convertView,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		}
		popupWindow.setTouchable(true);
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		// ����հ״�ʱ�����ص�pop����
		popupWindow.setOutsideTouchable(true);
		 // ���ñ����������Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		// ��PopupWindow����ͼ���������룬�ü��̵���ʱ�����ᵲסpop���ڡ�
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
                 * ���ϣ��������һ��Activity������ϣ���з���ֵ������Ҫʹ��startActivityForResult���������
                 * ��һ��������Intent���󣬵ڶ���������һ��requestCodeֵ������ж����ť��Ҫ����Activity����requestCode��־��ÿ����ť��������Activity
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
     * ���е�Activity����ķ���ֵ�������������������
     * requestCode:    ��ʾ��������һ��Activityʱ����ȥ��requestCodeֵ
     * resultCode����ʾ�����������Activity�ش�ֵʱ��resultCodeֵ
     * data����ʾ�����������Activity�ش�������Intent����
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
