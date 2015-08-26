package com.mynutritionstreet.goodFoodCircle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.example.mynutritionstreet.R;
import com.mynutritionstreet.mixinfo.Address;
import com.mynutritionstreet.mixinfo.SysApplication;
import com.mynutritionstreet.myinfo.MyInfoActivity;
import com.mynutritionstreet.pictureutil.ImageUtile;
import com.mynutritionstreet.util.DialogUtils;
import com.mynutritionstreet.util.HttpUtil;
import com.mynutritionstreet.util.ToastUtil;
import com.mynutritionstreet.util.UrlUtils;
import com.mynutritionstreet.util.VolleyUtil;

public class Camera_GalleryActivity extends Activity implements OnClickListener {
	private ImageView backImg;
	private TextView titleTxt;
	private ImageView pictureImg;
	private EditText foodNameExt;
	private EditText foodIntroduceExt;
	private Button okBtn;

	private Bitmap photo = null;
	public static final int NONE = 0;
	private static final int IMAGE_CAMERA = 1;// 拍照
	private static final int PHOTOZOOM = 2;// 缩放
	public static final int PHOTORESOULT = 3;// 结果
	// 存储图片的路径
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private String fileName = "";

	private String titleContent = "";
	private Bitmap oldPicture = null;

	private int height = 0;
	private int width = 0;
	
	static Dialog dialog = null;
	//记录图片路径
	private String imageFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_camera_gallery);
		SysApplication.getInstance().addActivity(this);

		findView();

		oldPicture = ImageUtile.drawableToBitmap(Camera_GalleryActivity.this
				.getResources().getDrawable(R.drawable.icon_addpic));
		photo = oldPicture;
		pictureImg.setImageBitmap(oldPicture);

		setAdapt();
		setTilte();
		onclick();

	}

	private void findView() {
		backImg = (ImageView) findViewById(R.id.camearGalleryBack);
		titleTxt = (TextView) findViewById(R.id.camearGalleryTitle);
		pictureImg = (ImageView) findViewById(R.id.camearGalleryPicture);
		foodNameExt = (EditText) findViewById(R.id.camearGalleryNameExt);
		foodIntroduceExt = (EditText) findViewById(R.id.camearGalleryIntroduceExt);
		okBtn = (Button) findViewById(R.id.camearGalleryOk);
	}

	private void setAdapt() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		height = metrics.heightPixels;
		width = metrics.widthPixels;

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		pictureImg.measure(w, h);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = width / 3;
		params.height = width / 3;
		params.setMargins(0, height / 8, 0, height / 20);
		pictureImg.setLayoutParams(params);

	}

	private void setTilte() {
		Intent intent = getIntent();
		titleContent = intent.getStringExtra("camera_gallery");
		titleTxt.setText(titleContent);
	}

	private void onclick() {
		backImg.setOnClickListener(this);
		okBtn.setOnClickListener(this);
		pictureImg.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		oldPicture = photo;

		// 检查sdcard是否存在
		/*
		 * if (!Environment.getExternalStorageState().equals(
		 * Environment.MEDIA_MOUNTED)) { return; } File fileDir = new
		 * File(Environment .getExternalStorageDirectory() +
		 * Address.USER_PIC_PATH); if (!fileDir.exists()) fileDir.mkdirs();
		 * fileName = System.currentTimeMillis() + ".jpg"; File picture = new
		 * File(fileDir, fileName); userNameExt.setText(picture.toString());
		 */

		// 设置文件保存路径这里放在跟目录下
		File picture = new File(Environment.getExternalStorageDirectory()
				+ Address.FOOD_PIC_PATH + fileName);

		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		// 拍照
		if (requestCode == IMAGE_CAMERA) {

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
			if (uri != null) {
				startPhotoZoom(uri);
			}
		}

		if (data == null) {
			return;
		}

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
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
				pictureImg.setImageBitmap(photo);
				/*
				 * else { Bitmap bitmap =
				 * ImageUtile.drawableToBitmap(oldPicture);
				 * userPhotoImg.setImageBitmap(bitmap); // bitmap.recycle(); }
				 */
				// 将图片保存到文件夹下
				new MyInfoActivity().savePic(photo, picture);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// // 裁剪框比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 2);
		// // 输出图片大小
		intent.putExtra("outputX", width / 3);
		intent.putExtra("outputY", width / 3);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	private void getPicture() {

		if (titleContent.equals(new GoodFoodCircleActivity().CAMERA_GET)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			// 检查sdcard是否存在
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				return;
			}
			File fileDir = new File(Environment.getExternalStorageDirectory()
					+ Address.FOOD_PIC_PATH);
			if (!fileDir.exists())
				fileDir.mkdirs();
			fileName = System.currentTimeMillis() + ".jpg";
			File path = new File(fileDir, fileName);
			Uri uri = Uri.fromFile(path);
			// intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent, IMAGE_CAMERA);

		} else if (titleContent
				.equals(new GoodFoodCircleActivity().GALLERY_GET)) {
			// 检查sdcard是否存在
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				return;
			}
			File fileDir = new File(Environment.getExternalStorageDirectory()
					+ Address.FOOD_PIC_PATH);
			if (!fileDir.exists())
				fileDir.mkdirs();
			fileName = System.currentTimeMillis() + ".jpg";
			File path = new File(fileDir, fileName);

			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					IMAGE_UNSPECIFIED);
			startActivityForResult(intent, PHOTOZOOM);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.camearGalleryBack:
			Intent intent = new Intent();
			intent.putExtra("camera_gallery", "");
			intent.setClass(Camera_GalleryActivity.this,
					GoodFoodCircleActivity.class);
			startActivity(intent);
			break;
		case R.id.camearGalleryPicture:
			getPicture();
			break;
		case R.id.camearGalleryOk:
			// 检查sdcard是否存在
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				return;
			}
			File fileDir = new File(Environment.getExternalStorageDirectory()
					+ Address.FOOD_PIC_PATH);
			if (!fileDir.exists())
				fileDir.mkdirs();
			File path = new File(fileDir, fileName);
			// String foodPic = "foodPic";
			String foodPic = path.getAbsolutePath();
			String foodName = foodNameExt.getText().toString();
			String foodintro = foodIntroduceExt.getText().toString();
			imageFile = foodPic;
			if (foodName.equals("") || foodPic.equals("")) {
				Toast.makeText(Camera_GalleryActivity.this, "美食图片没有或者美食名没有填写",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				uploadData();
				Intent inten = new Intent();
				inten.putExtra("foodPic", foodPic);
				inten.putExtra("foodName", foodName);
				inten.putExtra("foodIntro", foodintro);
				/*
				 * 调用setResult方法表示我将Intent对象返回给之前的那个Activity，
				 * 这样就可以在onActivityResult方法中得到Intent对象，
				 */
				setResult(1001, inten);
				finish();
			}

			break;
		}
	}
	
	/**
	 * 上传发布信息
	 */
	public void uploadData(){
		dialog = DialogUtils.createProgressDialog(Camera_GalleryActivity.this, "美食圈发布", "发布中，请稍后...");
		dialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("publishContent", foodNameExt.getText().toString().trim());
		rawParams.put("imageFile", imageFile);
		HttpUtil goodFoodRequest = new HttpUtil(UrlUtils.PUBLISH_PICTURE, rawParams, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				checkResult(response);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				ToastUtil.showToast(Camera_GalleryActivity.this, "网络错误", 2000);
			}
		});
		
		VolleyUtil.getRequestQueue().add(goodFoodRequest);
	}
	
	/**
	 * 检查返回情况
	 * @param response
	 */
	public void checkResult(String response){
		dialog.dismiss();
		Log.i("goo-respo", response);
		if(response.equals("\"Success\"")){
			ToastUtil.showToast(Camera_GalleryActivity.this, "发布成功", 2000);
		}else{
			ToastUtil.showToast(Camera_GalleryActivity.this, "发布失败", 2000);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {
			Intent intent = new Intent();
			intent.putExtra("camera_gallery", "");
			intent.setClass(Camera_GalleryActivity.this,
					GoodFoodCircleActivity.class);
			startActivity(intent);
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
