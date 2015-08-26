package com.mynutritionstreet.dailyquestion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mynutritionstreet.R;
import com.mynutritionstreet.mainpage.MainPageActivity;
import com.mynutritionstreet.mixinfo.SysApplication;

public class DailyQuestionActivity extends Activity implements OnClickListener {
	private ImageView back;
	private RelativeLayout relativeLayout;
	private View view1, view2;
	private TextView judgeAnswerTxt, questionTxt,answerTxt;
	private Button right, error;
	
	private int height = 0;
	private int width = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_daily_question);
		SysApplication.getInstance().addActivity(this); 
		
		findView();
		getQuestion();
		setAdapter();
		relativeLayout.setVisibility(View.GONE);
		onclick();
	}

	private void findView() {
		back = (ImageView) findViewById(R.id.dailyQuestionBack);
		relativeLayout = (RelativeLayout) findViewById(R.id.realtive_daily);
		view1 = (View) findViewById(R.id.view1DailyQuestion);
		view2 = (View) findViewById(R.id.view2DailyQuestion);
		judgeAnswerTxt = (TextView) findViewById(R.id.judgeAnswerDailyQuestion);
		questionTxt = (TextView) findViewById(R.id.questionDailyQuestion);
		answerTxt = (TextView)findViewById(R.id.answerDailyQuestion);
		right = (Button) findViewById(R.id.dailyRightBtn);
		error = (Button) findViewById(R.id.dailyErrorBtn);
	}

	// �ӷ�������ȡ����
	private void getQuestion() {
		questionTxt.setText("���̻�����״ٽ����򲡷�����");
	}

	private void setAdapter() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		height = metrics.heightPixels;
		width = metrics.widthPixels;

		System.out.println("width=" + judgeAnswerTxt.getWidth());

		questionTxt.setPadding(0, height / 4, 0, 30);
	}

	// ���ݷ������������ݣ���ʾ��������
	private void showDetailsInfo(String click) {
		relativeLayout.setVisibility(View.VISIBLE);
		right.setVisibility(View.GONE);
		error.setVisibility(View.GONE);
		
		questionTxt.setPadding(0, height / 8, 0, 10);
		judgeAnswerTxt.setText("�����");
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		params.width = (width - judgeAnswerTxt.getWidth()) / 2;
		view1.setLayoutParams(params);
		view2.setLayoutParams(params);
		
		answerTxt.setText("��ţ�̻��߳�����֮�ಢû�и�����ͬ����Ч��");
	}

	private void onclick() {
		back.setOnClickListener(this);
		right.setOnClickListener(this);
		error.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String right = "right";
		String error = "error"; 
		switch (v.getId()) {
		case R.id.dailyQuestionBack:
			startActivity(new Intent(DailyQuestionActivity.this, MainPageActivity.class));
			break;
		case R.id.dailyRightBtn:
			showDetailsInfo(right);
			break;
		case R.id.dailyErrorBtn:
			showDetailsInfo(error);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {
			startActivity(new Intent(DailyQuestionActivity.this, MainPageActivity.class));
        }
        return super.onKeyDown(keyCode, event);
	}
}
