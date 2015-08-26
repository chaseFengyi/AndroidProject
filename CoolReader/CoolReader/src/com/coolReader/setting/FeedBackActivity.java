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
		ActionBarUtil.centerActionBar(this, this, getActionBar(), "�������");
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
					ToastUtils.makeToast(FeedBackActivity.this, "�������Ϊ�գ�������������");
					et_feedback_content.requestFocus();
				}else{
					dialog = DialogUtils.createProgressDialog(FeedBackActivity.this, "�������", "���ڷ�������ȴ�");
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
							message.obj = "�����ɹ�";
							handler.sendMessage(message);
						}
					}).start();
				}
			}
		});
	}
	
	//���ܷ���������
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x01){
				dialog.dismiss();
				ToastUtils.makeToast(FeedBackActivity.this, "�����ɹ�");
				et_feedback_content.setText("");
			}
		};
	};
}
