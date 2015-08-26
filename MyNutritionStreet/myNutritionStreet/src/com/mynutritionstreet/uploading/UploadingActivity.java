package com.mynutritionstreet.uploading;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mynutritionstreet.R;
import com.mynutritionstreet.Bean.MaterialBean;
import com.mynutritionstreet.image.UploadDataAdnPhoto;
import com.mynutritionstreet.mainpage.MainPageActivity;
import com.mynutritionstreet.mixinfo.Address;
import com.mynutritionstreet.mixinfo.SysApplication;
import com.mynutritionstreet.myinfo.MyInfoActivity;
import com.mynutritionstreet.pictureutil.ImageUtile;
import com.mynutritionstreet.util.DialogUtils;
import com.mynutritionstreet.util.ToastUtil;
import com.mynutritionstreet.util.UrlUtils;

public class UploadingActivity extends Activity implements OnClickListener {
	private ImageView back;
	private Button upload;
	private ImageView image = null;
	private EditText foodNameEdit;
	private Spinner nutritionSpin, suitableRowsSpin;
	private GridView materialImg;
	private EditText stepEdit;

	public static final int NONE = 0;
	private static final int IMAGE_CAMERA = 1;// 拍照
	private static final int PHOTOZOOM = 2;// 从图库获取图片
	public static final int PHOTORESOULT = 3;// 结果

	private PopupWindow popupWindow = null;
	private SimpleAdapter adapter = null;
	
	static Dialog dialog = null;

	// 存储图片的路径
	public static final String IMAGE_UNSPECIFIED = "image/*";
	//选择图片路径
	private String fileName = "";
	private Bitmap photo = null;
	//功效
	private String nutritionEffects = "";
	//适宜人群\
	private String suitablePersion = "";
	//食材
	private String foodMaterial = "";

	private int height = 0;
	private int width = 0;
	private static int id = 1;

	// 存放食材
	private List<MaterialBean> list_food = new ArrayList<MaterialBean>();
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	@Override
	protected void onCreate(Bundle savedInsanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInsanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*
         * 防止键盘挡住输入框
         * 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
        		SOFT_INPUT_ADJUST_PAN);
        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_uploading);
		SysApplication.getInstance().addActivity(this);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		height = metrics.heightPixels;
		width = metrics.widthPixels;

		findView();
		onclick();
		//载入默认内容
		setGridView();
	}

	private void findView() {
		back = (ImageView) findViewById(R.id.uploadingBack);
		upload = (Button) findViewById(R.id.upload);
		image = (ImageView) findViewById(R.id.imageUploading);
		foodNameEdit = (EditText) findViewById(R.id.foodNameTxt);
		nutritionSpin = (Spinner) findViewById(R.id.nutriFunctionSpi);
		suitableRowsSpin = (Spinner) findViewById(R.id.suitableCrowsSpi);
		materialImg = (GridView) findViewById(R.id.materiaGridView);
		stepEdit = (EditText) findViewById(R.id.stepsEdit);
		
		nutritionSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//从string.xml获取所有内容
				String[] items = getResources().getStringArray(R.array.nutri_spinner);
				nutritionEffects = items[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		suitableRowsSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String[] items = getResources().getStringArray(R.array.suitablecrows_spinner);
				suitablePersion = items[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void setGridView() {
//		MaterialBean materialBean = new MaterialBean();
//		materialBean.setMaterialId(id);
//		materialBean.setMaterialName("");
//		materialBean.setMaterilaWeight(0);
//		materialBean.setMaterialPicture("picture");
		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("icon", R.drawable.icon_addpic);
		map.put("name", "食材名");
		map.put("weight", "重量");
		list.add(map);
		adapter = new SimpleAdapter(UploadingActivity.this, list,
				R.layout.sample_uploading_gridview, new String[] { 
						"name", "weight" }, new int[] {
						R.id.sample_gridview_name, R.id.sample_gridview_weight });
		/*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
		adapter.setViewBinder(new ViewBinder() {
			
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				// TODO Auto-generated method stub
				if(view instanceof ImageView && data instanceof Bitmap){
					ImageView imageView = (ImageView)view;
					imageView.setImageBitmap((Bitmap)data);
					return true;
				}
				return false;
			}
		});
		materialImg.setAdapter(adapter);
		materialImg.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//+放在尾部
				if(position == list.size()-1){
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					if(list != null || list.size() > 0){
						for(int i = 0; i < list.size()-1; i++){
							if(list.get(i).get("name").equals("食材名") && list.get(i).get("weight").equals("重量")){
								list.remove(i);
							}
						}
					}
//					Set<Map<String, Object>> set = new HashSet<Map<String,Object>>();
//					set.addAll(list);
//					list.clear();
//					list.addAll(set);
					bundle.putSerializable("material", (Serializable) list);
					intent.putExtras(bundle);
					intent.setClass(UploadingActivity.this, MaterialActivity.class);
					startActivityForResult(intent, 1000);
				}else{//点击删除
					dialog(position);
				}
				
			}
		});
	}

	private void onclick() {
		back.setOnClickListener(this);
		upload.setOnClickListener(this);
		image.setOnClickListener(this);
	}
	
	/**
	 * 验证输入的内容的合法性
	 * @return
	 */
	public boolean checkParams(){
		if(fileName.equals("")){
			ToastUtil.showToast(UploadingActivity.this, "请添加一张食物的图片", 2000);
			return false;
		}else if(foodNameEdit.getText().toString().trim().equals("")){
			ToastUtil.showToast(UploadingActivity.this, "请输入食物名称", 2000);
			foodNameEdit.requestFocus();
			return false;
		}else if(nutritionEffects == null || nutritionEffects.equals("")){
			ToastUtil.showToast(UploadingActivity.this, "请选择食物功效", 2000);
			return false;
		}else if(suitablePersion == null || suitablePersion.equals("")){
			ToastUtil.showToast(UploadingActivity.this, "请选择适宜人群", 2000);
			return false;
		}else if(stepEdit.getText().toString().trim().equals("")){
			ToastUtil.showToast(UploadingActivity.this, "请填入该食物的制作步骤", 2000);
			stepEdit.requestFocus();
			return false;
		}else if(list.size() <= 1){
			ToastUtil.showToast(UploadingActivity.this, "请输入要准备的食材内容", 2000);
			return false;
		}
		return true;
	}

	// 上传数据
	private void upload() {
		if(checkParams()){
			dialog = DialogUtils.createProgressDialog(UploadingActivity.this, "食物上传", "上传中，请稍后....");
			dialog.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//上传图片的名字
					String imageFile = Environment.getExternalStorageDirectory()
							+ Address.FOOD_PIC_PATH + fileName;
					Log.i("imageFile", imageFile);
					//商品名称
					String goodsName = foodNameEdit.getText().toString().trim();
					//功效
					String effectStr = nutritionEffects;
					//适宜人群
					String suitableStr = suitablePersion;
					//制作步骤
					String stepstr = stepEdit.getText().toString().trim();
					//获取要添加的食物对应的时间（早。中，晚，闲）
					Intent intent = getIntent();
					String type = intent.getStringExtra(MainPageActivity.TAG);
					//类型
					String typeStr;
					if(type.equals(MainPageActivity.BREAKFAST)){
						typeStr = "早";
					}else if(type.equals(MainPageActivity.CHINESEFOOD)){
						typeStr = "中"; 
					}else if(type.equals(MainPageActivity.DINNERFOOD)){
						typeStr = "晚";
					}else{
						typeStr = "闲";
					}
					Log.i("upload--material", list+"");
					//食材
					String material = "";
					for(int i = 0; i < list.size()-1; i++){
						material += list.get(i).get("name");
						material += ":";
						material += list.get(i).get("weight");
						material += ";";
					}
					//http://blog.csdn.net/jxxfzgy/article/details/44064481
					Map<String, String> rawParams = new HashMap<String, String>();
					rawParams.put("goodsName", goodsName);
					rawParams.put("nutritionalEffects", effectStr);
					rawParams.put("suitablPerson", suitableStr);
					rawParams.put("step", stepstr);
					rawParams.put("typeString", typeStr);
					rawParams.put("foodsString", material);
//					rawParams.put("imageFile", imageFile);
					Log.i("uploading-raw", rawParams+"");
//					HttpUtil uploadRequest = new HttpUtil(UrlUtils.ADD_GOODS, rawParams, new Listener<String>() {
		//
//						@Override
//						public void onResponse(String response) {
//							// TODO Auto-generated method stub
//							checkResult(response);
//						}
//					}, new ErrorListener() {
		//
//						@Override
//						public void onErrorResponse(VolleyError error) {
//							// TODO Auto-generated method stub
//							ToastUtil.showToast(UploadingActivity.this, "网络错误", 2000);
//						}
//					});
//					
//					VolleyUtil.getRequestQueue().add(uploadRequest);
					Map<String, File> files = new HashMap<String, File>();
					files.put("imageFile", new File(imageFile));
					Log.i("image", files+"");
					String receive = UploadDataAdnPhoto.post(UrlUtils.ADD_GOODS, rawParams, files);
					Message message = handler.obtainMessage();
					message.what = 0x01;
					message.obj = receive;
					handler.sendMessage(message);
				}
			}).start();
		}
		
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			dialog.dismiss();
			if(msg.what == 0x01){
				String response = (String)msg.obj;
				Log.i("upload0000", response);
				if(response.equals("\"Succsess\"")){
					System.out.println("execu");
					ToastUtil.showToast(UploadingActivity.this, "上传成功", 2000);
				}else{
					ToastUtil.showToast(UploadingActivity.this, "上传失败", 2000);
				}
			}
		};
	};
	
	//对图片进行二进制转换
    public byte[] getValue(Bitmap mBitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        mBitmap.compress(Bitmap.CompressFormat.JPEG,80,bos) ;
        return bos.toByteArray();
    }
	
	/**
	 * 检查上传数据结果
	 * @param response
	 */
	public void checkResult(String response){
		dialog.dismiss();
		Log.i("reesponse", response);
//		String receive = JsonResolve.get_result(response);
		if(response.equals("\"Succsess\"")){
			System.out.println("execu");
			ToastUtil.showToast(UploadingActivity.this, "上传成功", 2000);
		}else{
			ToastUtil.showToast(UploadingActivity.this, "上传失败", 2000);
		}
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// 设置文件保存路径这里放在跟目录下
		File picture = new File(Environment.getExternalStorageDirectory()
				+ Address.FOOD_PIC_PATH + fileName);

		// 拍照
		if (resultCode == IMAGE_CAMERA) {
			// 获取拍摄获得的uri
			Uri uri = null;
			try {
				uri = Uri.parse(android.provider.MediaStore.Images.Media
						.insertImage(getContentResolver(),
								picture.getAbsolutePath(), null, null));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (uri != null)
				startPhotoZoom(uri);
		}

		if (data == null) {
			return;
		}

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {//从相册取图片，有些手机有异常情况，请注意
			startPhotoZoom(data.getData());
		}
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			if (data == null || "".equals(data)) {
				return;
			}
			Bundle extras = data.getExtras();
			if (extras != null) {
				photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 -

				photo = ImageUtile.zoomBitmap(photo, width / 3, width / 3);
				photo = ImageUtile.getRoundedCornerBitmap(photo, 0.5f);
				image.setImageBitmap(photo);
				// 将图片保存到文件夹下
				new MyInfoActivity().savePic(photo, picture);
			}
		}
		// 从食材页面获取的信息
		if (requestCode == 1000 && resultCode == 1001) {
			Bundle bundle = data.getExtras();
			List<MaterialBean> cur = (List<MaterialBean>) bundle.getSerializable("material");
//			if(list != null || list.size() > 0){
//				for(int i = 0; i < list.size()-1; i++){
//					if(list.get(i).get("name").equals("食材名") && list.get(i).get("weight").equals("重量")){
//						list.remove(i);
//					}
//				}
//			}
			list.clear();
			Map<String, Object> map1 = new HashMap<String, Object>();
//			map.put("icon", R.drawable.icon_addpic);
			map1.put("name", "食材名");
			map1.put("weight", "重量");
			list.add(map1);
			for(int i = 0; i < cur.size(); i++){
				MaterialBean materialBean = cur.get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", materialBean.getMaterialName());
				map.put("weight", materialBean.getMaterilaWeight());
				list.add(0,map);
			}

			Log.i("activity", list+"");
			adapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 64);
		intent.putExtra("outputY", 64);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	public void showPopuWindow(View view, Bitmap photo) {
		// 一个自定义的布局，作为显示的内容
		View convertView = LayoutInflater.from(UploadingActivity.this).inflate(
				R.layout.sample_popuwindow, null);
		popupWindow = new PopupWindow(convertView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		// 点击空白处时，隐藏掉pop窗口
		popupWindow.setOutsideTouchable(true);
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		// 在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

		popupWindow.setFocusable(true);
		// backgroundAlpha(0.5f);

		Button camera = (Button) convertView.findViewById(R.id.popuCameraTxt);
		Button picture = (Button) convertView.findViewById(R.id.popuPhotoTxt);
		Button exit = (Button) convertView.findViewById(R.id.popuExitTxt);

		//照相
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				// 检查sdcard是否存在
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					return;
				}
				File fileDir = new File(Environment
						.getExternalStorageDirectory() + Address.FOOD_PIC_PATH);
				if (!fileDir.exists())
					fileDir.mkdirs();
				fileName = System.currentTimeMillis() + ".jpg";
				File path = new File(fileDir, fileName);
				Uri uri = Uri.fromFile(path);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, IMAGE_CAMERA);
				popupWindow.dismiss();
			}
		});

		//从图库获取
		picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 检查sdcard是否存在
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					return;
				}
				File fileDir = new File(Environment
						.getExternalStorageDirectory() + Address.FOOD_PIC_PATH);
				if (!fileDir.exists())
					fileDir.mkdirs();
				fileName = System.currentTimeMillis() + ".jpg";
				File path = new File(fileDir, fileName);

				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_UNSPECIFIED);
				startActivityForResult(intent, PHOTOZOOM);
				popupWindow.dismiss();
			}
		});

		//取消
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.uploadingBack:
			intent.setClass(UploadingActivity.this, MainPageActivity.class);
			startActivity(intent);
			break;
		case R.id.upload:
			upload();
			break;
		case R.id.imageUploading:
			showPopuWindow(v, photo);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(UploadingActivity.this, MainPageActivity.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//刷新图片
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
//		Log.i("onResume", "onResume:"+list);
		super.onResume();
//		setGridView();
//		adapter.notifyDataSetChanged();
//		adapter.notifyDataSetChanged();
//		if(!TextUtils.isEmpty(fileName)){
////			Bitmap bitmap = BitmapFactory.decodeFile(fileName);
////			HashMap<String, Object> map = new HashMap<String, Object>();
//////			map.put("icon", bitmap);
////			map.put("name", "食材名");
////			map.put("weight", "重量");
////			list.add(0,map);
////			adapter = new SimpleAdapter(UploadingActivity.this, list,
////					R.layout.sample_uploading_gridview, new String[] {
////							"name", "weight" }, new int[] {
////							R.id.sample_gridview_name, R.id.sample_gridview_weight });
////			/*
////	         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
////	         * map.put("itemImage", R.drawable.img);
////	         * 解决方法:
////	         *              1.自定义继承BaseAdapter实现
////	         *              2.ViewBinder()接口实现
////	         *  参考 http://blog.csdn.net/admin_/article/details/7257901
////	         */
////			adapter.setViewBinder(new ViewBinder() {
////				
////				@Override
////				public boolean setViewValue(View view, Object data,
////						String textRepresentation) {
////					// TODO Auto-generated method stub
////					if(view instanceof ImageView && data instanceof Bitmap){
////						ImageView imageView = (ImageView)view;
////						imageView.setImageBitmap((Bitmap)data);
////						return true;
////					}
////					return false;
////				}
////			});
////			materialImg.setAdapter(adapter);
//			adapter.notifyDataSetChanged();
			 //刷新后释放防止手机休眠后自动添加
//	          fileName = null;
//		}
	}
	
	/*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
	protected void dialog(final int position){
		AlertDialog.Builder builder = new Builder(UploadingActivity.this);
    	builder.setMessage("确认移除已添加图片吗？");
    	builder.setTitle("提示");
    	builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			list.remove(position);
    	        adapter.notifyDataSetChanged();
    		}
    	});
    	builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			}
    		});
    	builder.create().show();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		VolleyUtil.getRequestQueue().cancelAll(this);
	}

}
