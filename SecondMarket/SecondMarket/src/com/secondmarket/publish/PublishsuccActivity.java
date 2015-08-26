package com.secondmarket.publish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.secondmarket.load.R;
import com.secondmarket.myRelease.MyReleaseActivity;

public class PublishsuccActivity extends Activity {

	Button butsee ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publishsucc);
		//button 跳转信息页面
		butsee = (Button)findViewById(R.id.bunsee);
		butsee.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(PublishsuccActivity.this, MyReleaseActivity.class));
				finish();
			}
			
		});
		
	}
	
	

}