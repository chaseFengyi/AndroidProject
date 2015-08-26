package com.secondmarket.about;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.secondmarket.load.R;
import com.secondmarket.mine.MineActivity;

public class AboutActivity extends Activity {
	private Button about;
	private EditText developerEdit;
	private EditText developdepartEdit;
	private EditText serviceTelEdit;
	private EditText addressEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		
		findView();
		onClick();
		setText();
	}
	
	private void findView(){
		about = (Button)findViewById(R.id.about);
		developerEdit = (EditText)findViewById(R.id.developerEdit);
		developdepartEdit = (EditText)findViewById(R.id.developdepartEdit);
		serviceTelEdit = (EditText)findViewById(R.id.serviceTelEdit);
		addressEdit = (EditText)findViewById(R.id.emailEdit);
	}
	
	private void setText(){
		developerEdit.setFocusable(false);
		developerEdit.setFocusableInTouchMode(false);
		developerEdit.setText("Android开发小组");
		developdepartEdit.setFocusable(false);
		developdepartEdit.setFocusableInTouchMode(false);
		developdepartEdit.setText("稀有移动应用开发俱乐部");
		serviceTelEdit.setFocusable(false);
		serviceTelEdit.setFocusableInTouchMode(false);
		serviceTelEdit.setText("029-84214966");
		addressEdit.setFocusable(false);
		addressEdit.setFocusableInTouchMode(false);
		addressEdit.setText("xiyoumarket@outlook.com");
		
	}
	
	private void onClick(){
		about.setOnClickListener(new AboutListener());
	}
	
	class AboutListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(AboutActivity.this, MineActivity.class);
			startActivity(intent);
			finish();
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent();
			intent.setClass(AboutActivity.this, MineActivity.class);
			startActivity(intent);
			finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
		
	}
}
