package com.mynutritionstreet.setting;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mynutritionstreet.R;
import com.mynutritionstreet.mainpage.MainPageActivity;
import com.mynutritionstreet.mixinfo.SysApplication;

public class SetActivity extends Activity implements OnClickListener{
	private ImageView backImg;
	private RelativeLayout suggestRel;
	private RelativeLayout updateRel;
	private RelativeLayout aboutRel;
	private RelativeLayout quitRel;
	
	static ProgressDialog dialog;

	private static final String MSG_LOGI_SUCCESS = "更新成功。";
	private static final int FLAG_LOGIN_SUCCESS = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set);
		SysApplication.getInstance().addActivity(this); 
		
		findView();
		setAdapter();
		onclick();
	}
	
	private void findView(){
		backImg = (ImageView)findViewById(R.id.settingBack);
		suggestRel = (RelativeLayout)findViewById(R.id.rela_suggest_set);
		updateRel = (RelativeLayout)findViewById(R.id.rela_update_set);
		aboutRel = (RelativeLayout)findViewById(R.id.rela_about_set);
		quitRel = (RelativeLayout)findViewById(R.id.rela_quit_set);
	}
	
	private void setAdapter(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(width/16, height/10, width/16, 0);
		suggestRel.setLayoutParams(params);
		updateRel.setLayoutParams(params);
		aboutRel.setLayoutParams(params);
		quitRel.setLayoutParams(params);
		
		/*suggestRel.setPadding(20, height/7, 0, 0);
		updateRel.setPadding(20, height/10, 0, 0);
		aboutRel.setPadding(20, height/10, 0, 0);
		quitRel.setPadding(20, height/10, 0, 0);*/
	}

	private void onclick(){
		backImg.setOnClickListener(this);
		suggestRel.setOnClickListener(this);
		updateRel.setOnClickListener(this);
		aboutRel.setOnClickListener(this);
		quitRel.setOnClickListener(this);
	}
	
	private void showTip(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	private class MyHandler extends Handler {

		private final WeakReference<Activity> mActivity;

		public MyHandler(SetActivity activity) {
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
				((SetActivity) mActivity.get()).showTip(errorMsg);
				break;
			case FLAG_LOGIN_SUCCESS:
				((SetActivity) mActivity.get()).showTip(MSG_LOGI_SUCCESS);
				break;
			}
		}
	}

	private MyHandler handler = new MyHandler(this);

	
	//更新版本
	private void updateVersion(){
		/**
		 * loading.....
		 * */
		if (dialog == null) {
			dialog = new ProgressDialog(SetActivity.this);
		}
		dialog.setTitle("请等待");
		dialog.setMessage("更新中...");
		dialog.setCancelable(false);
		dialog.show();
		
		/**
		 * 副线程
		 */
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendEmptyMessage(FLAG_LOGIN_SUCCESS);
			}
		});
		thread.start();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.settingBack:
			startActivity(new Intent(SetActivity.this, MainPageActivity.class));
			break;
		case R.id.rela_suggest_set:
			startActivity(new Intent(SetActivity.this, SuggestFeedBackActivity.class));
			break;
		case R.id.rela_update_set:
			updateVersion();
			break;
		case R.id.rela_about_set:
			startActivity(new Intent(SetActivity.this, AboutUsActivity.class));
			break;
		case R.id.rela_quit_set:
			SysApplication.getInstance().exit();
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {
			startActivity(new Intent(SetActivity.this, MainPageActivity.class));
        }
        return super.onKeyDown(keyCode, event);
	}
}
