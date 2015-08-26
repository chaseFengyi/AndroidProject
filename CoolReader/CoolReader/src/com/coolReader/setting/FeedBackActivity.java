package com.coolReader.setting;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.Button;
import android.widget.EditText;

import com.coolReader.R;
import com.coolReader.Util.ActionBarUtil;
import com.coolReader.Util.DialogUtils;
import com.coolReader.Util.ToastUtils;

public class FeedBackActivity extends Activity {

	private EditText et_feedback_content = null;
	private Button btn_feedback_submit = null;
	Dialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_feed_back);
		findViews();
		ActionBarUtil.centerActionBar(this, this, getActionBar(), "意见反馈");
	}
	
	private void findViews(){
		et_feedback_content = (EditText) findViewById(R.id.et_feedback_content);
		btn_feedback_submit = (Button) findViewById(R.id.btn_feedback_submit);
		
		btn_feedback_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String inputStr = et_feedback_content.getText().toString().trim();
				if(inputStr.equals("") || inputStr == null){
					ToastUtils.makeToast(FeedBackActivity.this, "意见内容为空，请先输入内容");
					et_feedback_content.requestFocus();
				}else{
					dialog = DialogUtils.createProgressDialog(FeedBackActivity.this, "意见反馈", "正在反馈，请等待");
					dialog.show();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message message = handler.obtainMessage();
							message.what = 0x01;
							message.obj = "反馈成功";
							handler.sendMessage(message);
						}
					}).start();
				}
			}
		});
	}
	
	//接受反馈后的情况
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x01){
				dialog.dismiss();
				ToastUtils.makeToast(FeedBackActivity.this, "反馈成功");
				et_feedback_content.setText("");
			}
		};
	};
}
