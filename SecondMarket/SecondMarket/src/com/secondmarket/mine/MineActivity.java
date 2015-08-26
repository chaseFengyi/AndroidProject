package com.secondmarket.mine;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.secondmarket.about.AboutActivity;
import com.secondmarket.check.CheckUpdateActivity;
import com.secondmarket.cleancache.CleanCacheActivity;
import com.secondmarket.collect.MyCollectionActivity;
import com.secondmarket.information.MainPageActivity;
import com.secondmarket.information.myInfo.MyInformationActivity;
import com.secondmarket.load.R;
import com.secondmarket.myRelease.MyReleaseActivity;
import com.secondmarket.publish.PublishActivity;
import com.secondmarket.set.SetActivity;

public class MineActivity<onClickListener> extends Activity {
	private RelativeLayout myInformation;
	private RelativeLayout myRelease;
	private RelativeLayout myCollection;
	private RelativeLayout set;
	private RelativeLayout cleanCache;
	private RelativeLayout checkUpdate;
	private RelativeLayout about;
	private Button information;//信息
	private Button release;//发布
	private Button mine;//我的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine);
		findView();
		click();
	}

	private void findView(){
		myInformation = (RelativeLayout)findViewById(R.id.myInfo);
		myRelease = (RelativeLayout)findViewById(R.id.myPublish);
		myCollection = (RelativeLayout)findViewById(R.id.myCollect);
		set = (RelativeLayout)findViewById(R.id.mySet);
		cleanCache = (RelativeLayout)findViewById(R.id.myClearCache);
		checkUpdate = (RelativeLayout)findViewById(R.id.myCheckUpdate);
		about = (RelativeLayout)findViewById(R.id.myAbout);
		information = (Button)findViewById(R.id.Myinformation);
		release = (Button)findViewById(R.id.Myrelease);
		mine = (Button)findViewById(R.id.Mymine2);
	}
	
	private void click(){
		myInformation.setOnClickListener(onClickListener);
		myRelease.setOnClickListener(onClickListener);
		myCollection.setOnClickListener(onClickListener);
		set.setOnClickListener(onClickListener);
		cleanCache.setOnClickListener(onClickListener);
		checkUpdate.setOnClickListener(onClickListener);
		about.setOnClickListener(onClickListener);
		information.setOnClickListener(new InformationListener());
		release.setOnClickListener(new PublishListener());
	}
	
	class PublishListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startActivity(new Intent(MineActivity.this, PublishActivity.class));
		}
		
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			switch (arg0.getId()) {
			case R.id.myInfo:
				intent.setClass(MineActivity.this, MyInformationActivity.class);
				break;

			case R.id.myPublish:
				intent.setClass(MineActivity.this, MyReleaseActivity.class);
				break; 
				
			case R.id.myCollect:
				intent.setClass(MineActivity.this, MyCollectionActivity.class);
				break;
	
			case R.id.mySet:
				intent.setClass(MineActivity.this, SetActivity.class);
				break;
	
			case R.id.myClearCache:
				intent.setClass(MineActivity.this, CleanCacheActivity.class);
				break;
	
			case R.id.myCheckUpdate:
				intent.setClass(MineActivity.this, CheckUpdateActivity.class);
				break;
	
			case R.id.myAbout:
				intent.setClass(MineActivity.this, AboutActivity.class);
				break;
			}
			
			startActivity(intent);
			finish();
		}
	};
	
	class InformationListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MineActivity.this, MainPageActivity.class);
			startActivity(intent);
			finish();
		}
		
	}
	
}
