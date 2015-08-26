package com.analysistools.grammerAnalysis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.analysistools.filecontrol.EditFileActivity;
import com.analysistools.filecontrol.ExistedFileActivity;
import com.analysistools.lexicalanalysis.LexicalActivity;
import com.example.myanalysistools.R;

public class GrammerActivity extends Activity implements OnClickListener {

	private Button back;
	private Button openFile;
	private Button editBySelf;
	private Button lexicalAnalysisBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activit_grammer);

		findView();
		adapterView();
		onclick();
	}
	
	private void findView() {
		back = (Button) findViewById(R.id.backGrammer);
		openFile = (Button) findViewById(R.id.openFileGrammer);
		editBySelf = (Button) findViewById(R.id.editBySelfGrammer);
		lexicalAnalysisBtn = (Button) findViewById(R.id.lexicalAnalysisBtnGrammer);
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
		lexicalAnalysisBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();

		switch (arg0.getId()) {
		case R.id.backGrammer:
			intent.setClass(GrammerActivity.this, LexicalActivity.class);
			intent.putExtra("tag", "");
			break;

		case R.id.openFileGrammer:
			intent.setClass(GrammerActivity.this, ExistedFileActivity.class);
			intent.putExtra("tag", "GrammerActivity");
			break;

		case R.id.editBySelfGrammer:
			intent.setClass(GrammerActivity.this, EditFileActivity.class);
			intent.putExtra("tag", "GrammerActivity");
			break;
		case R.id.lexicalAnalysisBtnGrammer:
			intent.setClass(GrammerActivity.this, LexicalActivity.class);
			intent.putExtra("tag", "");
			break;
		}

		startActivity(intent);
	}
}
