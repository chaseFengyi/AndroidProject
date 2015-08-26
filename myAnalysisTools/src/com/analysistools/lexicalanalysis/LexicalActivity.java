package com.analysistools.lexicalanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.analysistools.filecontrol.EditFileActivity;
import com.analysistools.filecontrol.ExistedFileActivity;
import com.analysistools.grammerAnalysis.GrammerActivity;
import com.example.myanalysistools.R;
import com.myanalysistools.EnterActivity;

public class LexicalActivity extends FragmentActivity implements
		OnClickListener {

	private Button back;
	private Button openFile;
	private Button editBySelf;
	private Button grammerAnalysisBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lexical);

		findView();
		adapterView();
		onclick();
	}

	private void findView() {
		back = (Button) findViewById(R.id.back);
		openFile = (Button) findViewById(R.id.openFile);
		editBySelf = (Button) findViewById(R.id.editBySelf);
		grammerAnalysisBtn = (Button) findViewById(R.id.grammerAnalysisBtn);
	}

	private void adapterView(){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		
		openFile.setY(height / 5);
		editBySelf.setY((height / 4) + editBySelf.getHeight());
	}
	
	private void onclick(){
		back.setOnClickListener(this);
		openFile.setOnClickListener(this);
		editBySelf.setOnClickListener(this);
		grammerAnalysisBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		
		switch (arg0.getId()) {
		case R.id.back:
			intent.setClass(LexicalActivity.this, EnterActivity.class);
			intent.putExtra("tag", "");
			break;

		case R.id.openFile:
			intent.setClass(LexicalActivity.this , ExistedFileActivity.class);
			intent.putExtra("tag", "LexicalActivity");
			break;

		case R.id.editBySelf:
			intent.setClass(LexicalActivity.this, EditFileActivity.class);
			intent.putExtra("tag", "LexicalActivity");
			break;
		case R.id.grammerAnalysisBtn:
			intent.setClass(LexicalActivity.this, GrammerActivity.class);
			intent.putExtra("tag", "");
			break;
		}
		
		startActivity(intent);
	}

}
