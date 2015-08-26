package com.mynutritionstreet.setting;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mynutritionstreet.R;
import com.mynutritionstreet.mainpage.MainPageActivity;
import com.mynutritionstreet.mixinfo.SysApplication;

public class SuggestFeedBackActivity extends Activity implements OnClickListener {
	private ImageView backImg;
	private static EditText inputSuggestExt;
	private Button sendBtn;

	private static ProgressDialog dialog;
	private static final int FLAG_LOGIN_SUCCESS = 1;
	private static final String MSG_LOGI_SUCCESS = "发送成功。";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_suggest_feedback);
		SysApplication.getInstance().addActivity(this); 
		
		findView();
		onclick();
	}
	
	private void findView(){
		backImg = (ImageView)findViewById(R.id.suggestBack);
		inputSuggestExt = (EditText)findViewById(R.id.suggest_inputSuggestEdt);
		sendBtn = (Button)findViewById(R.id.suggest_send);
	}

	private void onclick(){
		backImg.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
	}
	
	//处理反馈信息
	private void disposeInfo(){
		//发送数据到服务器，成功后
		/**
		 * loading.....
		 * */
		if(dialog == null){
			dialog = new ProgressDialog(SuggestFeedBackActivity.this);
		}
		dialog.setTitle("请等待");
		dialog.setMessage("登录中...");
		dialog.setCancelable(false);
		dialog.show();
		
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
	
	private void showTip(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
private static class MyHandler extends Handler{
		
		private final WeakReference<Activity> mActivity;
		
		public MyHandler(SuggestFeedBackActivity activity) {
			mActivity = new WeakReference<Activity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
//			((LoginActivity)mActivity.get()).showTip();
			
			if(dialog != null){
				dialog.dismiss();
			}
			
			int flag = msg.what;
			switch(flag){
			case 0:
				String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
				((SuggestFeedBackActivity)mActivity.get()).showTip(errorMsg);
				break;
			case FLAG_LOGIN_SUCCESS:
				inputSuggestExt.setText("");
				((SuggestFeedBackActivity)mActivity.get()).showTip(MSG_LOGI_SUCCESS);
				break;
			}
		}
	}
	
	private MyHandler handler = new MyHandler(this);
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.suggestBack:
			startActivity(new Intent(SuggestFeedBackActivity.this, SetActivity.class));
			break;
		case R.id.suggest_send:
			disposeInfo();
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {
			startActivity(new Intent(SuggestFeedBackActivity.this, SetActivity.class));
        }
        return super.onKeyDown(keyCode, event);
	}
}
