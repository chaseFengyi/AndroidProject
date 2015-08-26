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
	private static final int IMAGE_CAMERA = 1;// ����
	private static final int PHOTOZOOM = 2;// ��ͼ���ȡͼƬ
	public static final int PHOTORESOULT = 3;// ���

	private PopupWindow popupWindow = null;
	private SimpleAdapter adapter = null;
	
	static Dialog dialog = null;

	// �洢ͼƬ��·��
	public static final String IMAGE_UNSPECIFIED = "image/*";
	//ѡ��ͼƬ·��
	private String fileName = "";
	private Bitmap photo = null;
	//��Ч
	private String nutritionEffects = "";
	//������Ⱥ\
	private String suitablePersion = "";
	//ʳ��
	private String foodMaterial = "";

	private int height = 0;
	private int width = 0;
	private static int id = 1;

	// ���ʳ��
	private List<MaterialBean> list_food = new ArrayList<MaterialBean>();
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	@Override
	protected void onCreate(Bundle savedInsanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInsanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*
         * ��ֹ���̵�ס�����
         * ��ϣ���ڵ�����activity���� android:windowSoftInputMode="adjustPan"
         * ϣ����̬�����߶� android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
        		SOFT_INPUT_ADJUST_PAN);
        //������Ļ
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_uploading);
		SysApplication.getInstance().addActivity(this);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		height = metrics.heightPixels;
		width = metrics.widthPixels;

		findView();
		onclick();
		//����Ĭ������
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
				//��string.xml��ȡ��������
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
		map.put("name", "ʳ����");
		map.put("weight", "����");
		list.add(map);
		adapter = new SimpleAdapter(UploadingActivity.this, list,
				R.layout.sample_uploading_gridview, new String[] { 
						"name", "weight" }, new int[] {
						R.id.sample_gridview_name, R.id.sample_gridview_weight });
		/*
         * HashMap����bmpͼƬ��GridView�в���ʾ,�������������ԴID����ʾ ��
         * map.put("itemImage", R.drawable.img);
         * �������:
         *              1.�Զ���̳�BaseAdapterʵ��
         *              2.ViewBinder()�ӿ�ʵ��
         *  �ο� http://blog.csdn.net/admin_/article/details/7257901
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
				//+����β��
				if(position == list.size()-1){
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					if(list != null || list.size() > 0){
						for(int i = 0; i < list.size()-1; i++){
							if(list.get(i).get("name").equals("ʳ����") && list.get(i).get("weight").equals("����")){
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
				}else{//���ɾ��
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
	 * ��֤��������ݵĺϷ���
	 * @return
	 */
	public boolean checkParams(){
		if(fileName.equals("")){
			ToastUtil.showToast(UploadingActivity.this, "�����һ��ʳ���ͼƬ", 2000);
			return false;
		}else if(foodNameEdit.getText().toString().trim().equals("")){
			ToastUtil.showToast(UploadingActivity.this, "������ʳ������", 2000);
			foodNameEdit.requestFocus();
			return false;
		}else if(nutritionEffects == null || nutritionEffects.equals("")){
			ToastUtil.showToast(UploadingActivity.this, "��ѡ��ʳ�﹦Ч", 2000);
			return false;
		}else if(suitablePersion == null || suitablePersion.equals("")){
			ToastUtil.showToast(UploadingActivity.this, "��ѡ��������Ⱥ", 2000);
			return false;
		}else if(stepEdit.getText().toString().trim().equals("")){
			ToastUtil.showToast(UploadingActivity.this, "�������ʳ�����������", 2000);
			stepEdit.requestFocus();
			return false;
		}else if(list.size() <= 1){
			ToastUtil.showToast(UploadingActivity.this, "������Ҫ׼����ʳ������", 2000);
			return false;
		}
		return true;
	}

	// �ϴ�����
	private void upload() {
		if(checkParams()){
			dialog = DialogUtils.createProgressDialog(UploadingActivity.this, "ʳ���ϴ�", "�ϴ��У����Ժ�....");
			dialog.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//�ϴ�ͼƬ������
					String imageFile = Environment.getExternalStorageDirectory()
							+ Address.FOOD_PIC_PATH + fileName;
					Log.i("imageFile", imageFile);
					//��Ʒ����
					String goodsName = foodNameEdit.getText().toString().trim();
					//��Ч
					String effectStr = nutritionEffects;
					//������Ⱥ
					String suitableStr = suitablePersion;
					//��������
					String stepstr = stepEdit.getText().toString().trim();
					//��ȡҪ��ӵ�ʳ���Ӧ��ʱ�䣨�硣�У����У�
					Intent intent = getIntent();
					String type = intent.getStringExtra(MainPageActivity.TAG);
					//����
					String typeStr;
					if(type.equals(MainPageActivity.BREAKFAST)){
						typeStr = "��";
					}else if(type.equals(MainPageActivity.CHINESEFOOD)){
						typeStr = "��"; 
					}else if(type.equals(MainPageActivity.DINNERFOOD)){
						typeStr = "��";
					}else{
						typeStr = "��";
					}
					Log.i("upload--material", list+"");
					//ʳ��
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
//							ToastUtil.showToast(UploadingActivity.this, "�������", 2000);
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
					ToastUtil.showToast(UploadingActivity.this, "�ϴ��ɹ�", 2000);
				}else{
					ToastUtil.showToast(UploadingActivity.this, "�ϴ�ʧ��", 2000);
				}
			}
		};
	};
	
	//��ͼƬ���ж�����ת��
    public byte[] getValue(Bitmap mBitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        mBitmap.compress(Bitmap.CompressFormat.JPEG,80,bos) ;
        return bos.toByteArray();
    }
	
	/**
	 * ����ϴ����ݽ��
	 * @param response
	 */
	public void checkResult(String response){
		dialog.dismiss();
		Log.i("reesponse", response);
//		String receive = JsonResolve.get_result(response);
		if(response.equals("\"Succsess\"")){
			System.out.println("execu");
			ToastUtil.showToast(UploadingActivity.this, "�ϴ��ɹ�", 2000);
		}else{
			ToastUtil.showToast(UploadingActivity.this, "�ϴ�ʧ��", 2000);
		}
	}

	/**
	 * ���������Ļ�ı���͸����
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
		// �����ļ�����·��������ڸ�Ŀ¼��
		File picture = new File(Environment.getExternalStorageDirectory()
				+ Address.FOOD_PIC_PATH + fileName);

		// ����
		if (resultCode == IMAGE_CAMERA) {
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
			if (uri != null)
				startPhotoZoom(uri);
		}

		if (data == null) {
			return;
		}

		// ��ȡ�������ͼƬ
		if (requestCode == PHOTOZOOM) {//�����ȡͼƬ����Щ�ֻ����쳣�������ע��
			startPhotoZoom(data.getData());
		}
		// ������
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
				// ��ͼƬ���浽�ļ�����
				new MyInfoActivity().savePic(photo, picture);
			}
		}
		// ��ʳ��ҳ���ȡ����Ϣ
		if (requestCode == 1000 && resultCode == 1001) {
			Bundle bundle = data.getExtras();
			List<MaterialBean> cur = (List<MaterialBean>) bundle.getSerializable("material");
//			if(list != null || list.size() > 0){
//				for(int i = 0; i < list.size()-1; i++){
//					if(list.get(i).get("name").equals("ʳ����") && list.get(i).get("weight").equals("����")){
//						list.remove(i);
//					}
//				}
//			}
			list.clear();
			Map<String, Object> map1 = new HashMap<String, Object>();
//			map.put("icon", R.drawable.icon_addpic);
			map1.put("name", "ʳ����");
			map1.put("weight", "����");
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
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 64);
		intent.putExtra("outputY", 64);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	public void showPopuWindow(View view, Bitmap photo) {
		// һ���Զ���Ĳ��֣���Ϊ��ʾ������
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

		//����
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

		//��ͼ���ȡ
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

		//ȡ��
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
	
	//ˢ��ͼƬ
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
////			map.put("name", "ʳ����");
////			map.put("weight", "����");
////			list.add(0,map);
////			adapter = new SimpleAdapter(UploadingActivity.this, list,
////					R.layout.sample_uploading_gridview, new String[] {
////							"name", "weight" }, new int[] {
////							R.id.sample_gridview_name, R.id.sample_gridview_weight });
////			/*
////	         * HashMap����bmpͼƬ��GridView�в���ʾ,�������������ԴID����ʾ ��
////	         * map.put("itemImage", R.drawable.img);
////	         * �������:
////	         *              1.�Զ���̳�BaseAdapterʵ��
////	         *              2.ViewBinder()�ӿ�ʵ��
////	         *  �ο� http://blog.csdn.net/admin_/article/details/7257901
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
			 //ˢ�º��ͷŷ�ֹ�ֻ����ߺ��Զ����
//	          fileName = null;
//		}
	}
	
	/*
     * Dialog�Ի�����ʾ�û�ɾ������
     * positionΪɾ��ͼƬλ��
     */
	protected void dialog(final int position){
		AlertDialog.Builder builder = new Builder(UploadingActivity.this);
    	builder.setMessage("ȷ���Ƴ������ͼƬ��");
    	builder.setTitle("��ʾ");
    	builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			list.remove(position);
    	        adapter.notifyDataSetChanged();
    		}
    	});
    	builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
