package com.mynutritionstreet.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mynutritionstreet.R;
import com.mynutritionstreet.login.LoginActivity;
import com.mynutritionstreet.mixinfo.SysApplication;
import com.mynutritionstreet.register.RegisterActivity;

public class AboutUsActivity extends Activity {
	private ImageView backImg;
	private LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_aboutus);
		SysApplication.getInstance().addActivity(this); 
		
		backImg = (ImageView)findViewById(R.id.aboutBack);
		layout = (LinearLayout)findViewById(R.id.about_linear);
		backImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(AboutUsActivity.this, SetActivity.class));
			}
		});
		
		setAdapter();
	}
	
	private void setAdapter(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		int height = metrics.heightPixels;
		
		layout.setY(height / 6);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {
			startActivity(new Intent(AboutUsActivity.this, SetActivity.class));
        }
        return super.onKeyDown(keyCode, event);
	}
}
