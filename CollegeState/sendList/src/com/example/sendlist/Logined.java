package com.example.sendlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.get.GetOrder;
import com.send.SendOrder;

public class Logined extends Activity {
	private Button get;
	private Button send;
	private ImageButton imageButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logined);
		
		onClick();
		click();
	}
	
	private void onClick(){
		get = (Button)findViewById(R.id.getOrder);
		send = (Button)findViewById(R.id.sendOrder);
		imageButton = (ImageButton)findViewById(R.id.imageButton);
	}
	
	private void click(){
		get.setOnClickListener(new GetListener());
		send.setOnClickListener(new SendListener());
		imageButton.setOnClickListener(new ExitListener());
	}
	
	class GetListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(Logined.this, GetOrder.class);
			startActivity(intent);
			finish();
		}
		
	}
	
	class SendListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(Logined.this, SendOrder.class);
			startActivity(intent);
			finish();
		}
		
	}
	
	class ExitListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(Logined.this, Login.class);
			startActivity(intent);
		}
		
	}

	/*@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}*/

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent();
			intent.setClass(Logined.this, Login.class);
			startActivity(intent);
			finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
		
	}
	
	
}
