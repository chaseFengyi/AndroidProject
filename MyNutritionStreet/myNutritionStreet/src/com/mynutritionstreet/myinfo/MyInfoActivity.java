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

	public static int flag = 1;// �жϵ���޸İ�ť

	private static List<UserInfoBean> list;

	private Bitmap photo = null;
	public static final int NONE = 0;
	private static final int IMAGE_CAMERA = 1;// ����
	private static final int PHOTOZOOM = 2;// ����
	public static final int PHOTORESOULT = 3;// ���
	// �洢ͼƬ��·��
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private String fileName = "";
	private PopupWindow popupWindow = null;
	// �ж��Ƿ񱣴��޸ĵ�ͼƬ
	private boolean isSave = false;
	// �ж��Ƿ����޸İ�ť
	private boolean isUpdateClicked = false;

	// ��Ļ��ȣ�����
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
		// ����Ϊ���ɱ༭״̬
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

	// �ӷ�������ȡ�û���Ϣ������ʾ
	private void showUserInfo() {
		userNameExt.setText("����");
		sexExt.setText("Ů");
		ageExt.setText("22");
		jobExt.setText("ѧ��");
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

	// ����Ϊ�༭״̬
	private void setEdit_UnEdit(boolean flag) {
		// flag=true,����Ϊ�ɱ༭״̬
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

	// ���û�����޸�
	private void update() {
		isUpdateClicked = true;
		isSave = true;
		if (flag == 1) {
			// ����Ϊ�ɱ༭״̬
			setEdit_UnEdit(true);
			updateBtn.setText("ȷ��");
			flag = 0;
//			userPhotoImg.setOnClickListener(this);
		} else if (flag == 0) {
			// ���ò��ɱ༭״̬
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MyInfoActivity.this)
					.setTitle("��ʾ��")
					.setMessage("�Ƿ񱣴����?")
					.setPositiveButton("����",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									arg0.dismiss();
									setEdit_UnEdit(false);
									updateBtn.setText("�޸�");
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
											"�޸��û���Ϣ�ɹ�!", Toast.LENGTH_SHORT)
											.show();
								}
							})
					.setNegativeButton("������",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									arg0.dismiss();
									updateBtn.setText("�޸�");
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
		
		// ���sdcard�Ƿ����
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
		
		// �����ļ�����·��������ڸ�Ŀ¼��
		File picture = new File(Environment.getExternalStorageDirectory()
				+ Address.USER_PIC_PATH + fileName);

		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		// ����
		if (requestCode == IMAGE_CAMERA) {

			// ��ȡ�����õ�uri
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

		// ��ȡ�������ͼƬ
		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}
		// ������
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
				// ��ͼƬ���浽�ļ�����
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
		// // �ü������
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 2);
		// // ���ͼƬ��С
		intent.putExtra("outputX", width / 3);
		intent.putExtra("outputY", width / 3);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	public void showPopuWindow(View view, Bitmap photo) {
		// һ���Զ���Ĳ��֣���Ϊ��ʾ������
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
				// �����������true�Ļ���touch�¼���������
				// ���غ� PopupWindow��onTouchEvent�������ã���������ⲿ�����޷�dismiss
			}
		});
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		// ����հ״�ʱ�����ص�pop����
		popupWindow.setOutsideTouchable(true);
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
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				// ���sdcard�Ƿ����
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
				
				// ���sdcard�Ƿ����
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
				Toast.makeText(MyInfoActivity.this, "�뵥�����Ͻǵ��޸İ�ť",
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
