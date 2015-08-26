package com.myanalysistools; 

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.analysistools.lexicalanalysis.LexicalActivity;
import com.example.myanalysistools.R;

public class EnterActivity extends Activity {
	
	private ImageView picture;
	private Button clickEnter;
	private TextView welcomeText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter);
		findView();
		onclick();
		setPicture();
		setPosition();
	}

	private void findView(){
		picture = (ImageView)findViewById(R.id.picture);
		clickEnter = (Button)findViewById(R.id.clickEnter);
		welcomeText = (TextView)findViewById(R.id.welcomeText);
	}
	
	
	private void setPosition(){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;
		Log.v("width", width+"");
		Log.v("height", height+"");
		int w = View.MeasureSpec.makeMeasureSpec(0,
		        View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
		        View.MeasureSpec.UNSPECIFIED);
		picture.measure(w, h);
		Log.i("imageview2", "width: " + picture.getMeasuredWidth());
		Log.i("imageview2", "height: " + picture.getMeasuredHeight());
		int picX = width / 2 - picture.getMeasuredWidth() / 2;
		int picY = height / 2 - picture.getMeasuredHeight() / 2;
		Log.i("imageview2", "width: " + picX);
		Log.i("imageview2", "height: " + picY);
		picture.setX(picX);
		picture.setY(picY);
		
		clickEnter.setX(width * 1 / 5);
//		clickEnter.setY(height * 3 / 4);
	}
	
	private void setPicture(){
		picture.setBackgroundResource(R.drawable.anim_nv);
		AnimationDrawable animationDrawable = (AnimationDrawable)picture.getBackground();
		animationDrawable.start();
	}

	private void onclick() {
		clickEnter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(EnterActivity.this, LexicalActivity.class));
			}
		});
	}

}
