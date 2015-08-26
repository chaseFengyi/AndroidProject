package com.coolReader.setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.coolReader.R;
import com.coolReader.Login.LoginActivity;
import com.coolReader.Util.ActionBarUtil;
import com.coolReader.Util.DialogUtils;
import com.coolReader.Util.ToastUtils;

public class SettingActivity extends Activity{
	
	private ListView lv_setting = null;
	Dialog dialog = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_setting);
		ActionBarUtil.centerActionBar(this,this, getActionBar(), "设置");
		findViews();
		initListView();
	}
	
	private void findViews() {
		lv_setting = (ListView)this.findViewById(R.id.lv_setting);
	}
	
	//初始化ListView页面
	private void initListView() {
		SettingListViewAdapter adapter = new SettingListViewAdapter(this);
		lv_setting.setAdapter(adapter);
		lv_setting.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adv, View v, int position,
					long id) {
				switch (position) {
				case 0:
					//编辑账号
					Log.i("test", "press");
					startActivity(new Intent(SettingActivity.this,EditCountActivity.class));
					break;
				case 1:
					//意见反馈
					startActivity(new Intent(SettingActivity.this,FeedBackActivity.class));
					break;
				case 2:
					//更新版本
					dialog = DialogUtils.createProgressDialog(SettingActivity.this, "版本更新", "更新中，请稍后");
					dialog.show();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message message = handler.obtainMessage();
							message.what = 0x01;
							handler.sendMessage(message);
						}
					}).start();
					break;
				case 3:
					//关于我们
					startActivity(new Intent(SettingActivity.this,AboutActivity.class));
					break;
				case 4:
					//注销
					Intent intent = new Intent();
					intent.setClass(SettingActivity.this, LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
					break;
				default:
					break;
				}
			}
		});
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 0x01){
				dialog.dismiss();
				ToastUtils.makeToast(SettingActivity.this, "已是最新版本，更新完成");
			}
		};
	};
}
