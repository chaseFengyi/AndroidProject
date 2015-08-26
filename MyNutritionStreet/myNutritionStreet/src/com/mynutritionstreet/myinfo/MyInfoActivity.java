package com.mynutritionstreet.myinfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mynutritionstreet.R;
import com.mynutritionstreet.Bean.UserInfoBean;
import com.mynutritionstreet.login.LoginActivity;
import com.mynutritionstreet.mainpage.DailyRecommendActivity;
import com.mynutritionstreet.mainpage.MainPageActivity;
import com.mynutritionstreet.mixinfo.Address;
import com.mynutritionstreet.mixinfo.SysApplication;
import com.mynutritionstreet.pictureutil.ImageUtile;

public class MyInfoActivity extends Activity implements OnClickListener {
	private ImageView backImg;
	private Button updateBtn;
	private ImageView userPhotoImg;
	private EditText userNameExt;
	private EditText sexExt;
	private EditText ageExt;
	private EditText jobExt;
	private EditText weightExt;

	public static int flag = 1;// 判断点击修改按钮

	private static List<UserInfoBean> list;

	private Bitmap photo = null;
	public static final int NONE = 0;
	private static final int IMAGE_CAMERA = 1;// 拍照
	private static final int PHOTOZOOM = 2;// 缩放
	public static final int PHOTORESOULT = 3;// 结果
	// 存储图片的路径
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private String fileName = "";
	private PopupWindow popupWindow = null;
	// 判断是否保存修改的图片
	private boolean isSave = false;
	// 判断是否点击修改按钮
	private boolean isUpdateClicked = false;

	// 屏幕宽度，长度
	private int width = 0;
	private int height = 0;
	
	private Bitmap oldPicture = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_myinfo);
		SysApplication.getInstance().addActivity(this);

		findView();
		
		oldPicture = ImageUtile.drawableToBitmap(MyInfoActivity.this.getResources().getDrawable(R.drawable.person_picture)); 
		photo = oldPicture;
		userPhotoImg.setImageBitmap(oldPicture);
		
		setAdapter();
		showUserInfo();
		// 设置为不可编辑状态
		setEdit_UnEdit(false);
		onclick();
	}

	private void findView() {
		backImg = (ImageView) findViewById(R.id.myInfoBack);
		updateBtn = (Button) findViewById(R.id.myInfoUpdate);
		userPhotoImg = (ImageView) findViewById(R.id.myInfoImage);
		userNameExt = (EditText) findViewById(R.id.myInfoUserName);
		sexExt = (EditText) findViewById(R.id.myInfoSex);
		ageExt = (EditText) findViewById(R.id.myInfoAge);
		jobExt = (EditText) findViewById(R.id.myInfoJob);
		weightExt = (EditText) findViewById(R.id.myInfoWeight);
	}

	// 从服务器获取用户信息进行显示
	private void showUserInfo() {
		userNameExt.setText("封毅");
		sexExt.setText("女");
		ageExt.setText("22");
		jobExt.setText("学生");
		weightExt.setText("110kg");
	}

	private void setAdapter() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		width = metrics.widthPixels;
		height = metrics.heightPixels;

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		userPhotoImg.measure(w, h);

		int subWidth = userPhotoImg.getMeasuredWidth();

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins((width - subWidth) / 2, height / 30,
				(width - subWidth) / 2, 0);
		userPhotoImg.setLayoutParams(params);
	}

	// 设置为编辑状态
	private void setEdit_UnEdit(boolean flag) {
		// flag=true,设置为可编辑状态
		if (flag) {
			userNameExt.setFocusable(true);
			userNameExt.setFocusableInTouchMode(true);
			sexExt.setFocusable(true);
			sexExt.setFocusableInTouchMode(true);
			ageExt.setFocusable(true);
			ageExt.setFocusableInTouchMode(true);
			jobExt.setFocusable(true);
			jobExt.setFocusableInTouchMode(true);
			weightExt.setFocusable(true);
			weightExt.setFocusableInTouchMode(true);
		} else {
			userNameExt.setFocusable(false);
			userNameExt.setFocusableInTouchMode(false);
			sexExt.setFocusable(false);
			sexExt.setFocusableInTouchMode(false);
			ageExt.setFocusable(false);
			ageExt.setFocusableInTouchMode(false);
			jobExt.setFocusable(false);
			jobExt.setFocusableInTouchMode(false);
			weightExt.setFocusable(false);
			weightExt.setFocusableInTouchMode(false);
		}
	}

	private void onclick() {
		backImg.setOnClickListener(this);
		updateBtn.setOnClickListener(this);
		userPhotoImg.setOnClickListener(this);
	}

	// 当用户点击修改
	private void update() {
		isUpdateClicked = true;
		isSave = true;
		if (flag == 1) {
			// 设置为可编辑状态
			setEdit_UnEdit(true);
			updateBtn.setText("确定");
			flag = 0;
//			userPhotoImg.setOnClickListener(this);
		} else if (flag == 0) {
			// 设置不可编辑状态
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MyInfoActivity.this)
					.setTitle("提示！")
					.setMessage("是否保存更改?")
					.setPositiveButton("保存",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									arg0.dismiss();
									setEdit_UnEdit(false);
									updateBtn.setText("修改");
									isUpdateClicked = false;
									/*
									 * UserInfoDemo userInfoDemo = list.get(0);
									 * userInfoDemo
									 * .setUserName(userNameExt.getText
									 * ().toString());
									 * userInfoDemo.setUserSex(sexExt
									 * .getText().toString());
									 * userInfoDemo.setUserAge
									 * (ageExt.getText().toString());
									 * userInfoDemo
									 * .setUserJob(jobExt.getText().toString());
									 * userInfoDemo
									 * .setUserWeight(weightExt.getText
									 * ().toString());
									 */
									Toast.makeText(MyInfoActivity.this,
											"修改用户信息成功!", Toast.LENGTH_SHORT)
											.show();
								}
							})
					.setNegativeButton("不保存",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									arg0.dismiss();
									updateBtn.setText("修改");
									isSave = false;
									userPhotoImg.setImageBitmap(oldPicture);
									System.out.println("---------not save");
								}
							});
			builder.create().show();
			flag = 1;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		oldPicture = photo;
		
		// 检查sdcard是否存在
		/*if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}
		File fileDir = new File(Environment
				.getExternalStorageDirectory() + Address.USER_PIC_PATH);
		if (!fileDir.exists())
			fileDir.mkdirs();
		fileName = System.currentTimeMillis() + ".jpg";
		File picture = new File(fileDir, fileName);
		userNameExt.setText(picture.toString());*/
		
		// 设置文件保存路径这里放在跟目录下
		File picture = new File(Environment.getExternalStorageDirectory()
				+ Address.USER_PIC_PATH + fileName);

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
			if (uri != null){
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
			Bundle extras = data.getExtras();
			if (extras != null) {
				photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 -
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				photo = ImageUtile.zoomBitmap(photo, width / 3, width / 3);
				photo = ImageUtile.getRoundedCornerBitmap(photo, 0.5f);
				if (isSave){
					userPhotoImg.setImageBitmap(photo);
				}
				/*else
				{
					Bitmap bitmap = ImageUtile.drawableToBitmap(oldPicture);
					userPhotoImg.setImageBitmap(bitmap);
//					bitmap.recycle();
				}*/
				// 将图片保存到文件夹下
				savePic(photo, picture);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void savePic(Bitmap bitmap, File file) {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fileOutputStream);

		try {
			fileOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void showPopuWindow(View view, Bitmap photo) {
		// 一个自定义的布局，作为显示的内容
		View convertView = LayoutInflater.from(MyInfoActivity.this).inflate(
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
						.getExternalStorageDirectory() + Address.USER_PIC_PATH);
				if (!fileDir.exists())
					fileDir.mkdirs();
				fileName = System.currentTimeMillis() + ".jpg";
				File path = new File(fileDir, fileName);
				Uri uri = Uri.fromFile(path);
				// intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, IMAGE_CAMERA);
			}
		});

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
						.getExternalStorageDirectory() + Address.USER_PIC_PATH);
				if (!fileDir.exists())
					fileDir.mkdirs();
				fileName = System.currentTimeMillis() + ".jpg";
				File path = new File(fileDir, fileName);
				
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_UNSPECIFIED);
				startActivityForResult(intent, PHOTOZOOM);
			}
		});

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
		switch (v.getId()) {
		case R.id.myInfoBack:
			startActivity(new Intent(MyInfoActivity.this,
					MainPageActivity.class));
			break;
		case R.id.myInfoUpdate:
			update();
			break;

		case R.id.myInfoImage:
			if (isUpdateClicked)
				showPopuWindow(v, photo);
			else
				Toast.makeText(MyInfoActivity.this, "请单击右上角的修改按钮",
						Toast.LENGTH_SHORT).show();

			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {
			startActivity(new Intent(MyInfoActivity.this, MainPageActivity.class));
        }
        return super.onKeyDown(keyCode, event);
	}
}
