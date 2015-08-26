package com.mynutritionstreet.mainpage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mynutritionstreet.R;
import com.mynutritionstreet.goodFoodCircle.GoodFoodCircleActivity;
import com.mynutritionstreet.login.LoginActivity;
import com.mynutritionstreet.mixinfo.SysApplication;

public class DailyRecommendActivity extends FragmentActivity implements
		OnClickListener {

	private DailyRecommendFragment breakfast;
	private ChineseFoodFragment chinesefood;
	private DinnerFragment dinner;
	private IdleHourFragment idlefood;

	// 帧布局对象,就是用来存放Fragment的容器
	private FrameLayout flayout;
	// 定义底部导航栏的布局
	private RelativeLayout breakfast_layout;
	private RelativeLayout chinesefood_layout;
	private RelativeLayout dinner_layout;
	private RelativeLayout idlehour_layout;

	// 定义底部导航栏中的TextView
	private TextView breakfast_text;
	private TextView chinesefood_text;
	private TextView dinner_text;
	private TextView idlehour_text;

	// 定义要用的颜色值
	private int whirt = 0xFFFFFFFF;
	private int gray = 0xFF7597B3;
	private int blue = 0xFF0AB2FB;
	
	static Dialog dialog = null;
	
	//参数类型
	public final static String TYPESTRING = "typeString";
	public final static String STR_BREAKFAST = "str_breakfast";
	public final static String STR_CHINESEFOOD = "str_chinesefood";
	public final static String STR_DINNERFOOD = "str_dinnerfood";
	public final static String STR_IDLEFOOD = "str_idlefood";

	// 定义FragmentManager对象
	private FragmentManager fManager;
	private FragmentTransaction transaction;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_daily);
		SysApplication.getInstance().addActivity(this); 
		

		initViews();
		clearChioce();
		
		fManager = getSupportFragmentManager();
		transaction = fManager.beginTransaction();

		if (breakfast != null && chinesefood != null && dinner != null
				&& idlefood != null) {
			transaction.hide(chinesefood).hide(dinner).hide(idlefood);
			transaction.show(breakfast).commitAllowingStateLoss();
		}

		Bundle bundle = new Bundle();
		breakfast_text.setTextColor(blue);
		breakfast_layout.setBackgroundResource(R.color.lightgreen);
		if (breakfast == null) {
			breakfast = new DailyRecommendFragment();
//			bundle.putSerializable(
//					DailyRecommendFragment.ARG_SECTION_NUMBER,
//					(Serializable) getData());
//			bundle.putString(TYPESTRING, STR_BREAKFAST);
			// 如果fg1为空，则创建一个并添加到界面上
//			breakfast.setArguments(bundle);
		}
		transaction.replace(R.id.content, breakfast);
		transaction.addToBackStack(null);
		transaction.show(breakfast);
		transaction.commitAllowingStateLoss();
	}

	// 完成组件的初始化
	public void initViews() {
		breakfast_text = (TextView) findViewById(R.id.breakfast_text);
		chinesefood_text = (TextView) findViewById(R.id.chinesefood_text);
		dinner_text = (TextView) findViewById(R.id.dinner_text);
		idlehour_text = (TextView) findViewById(R.id.idlehour_text);
		breakfast_layout = (RelativeLayout) findViewById(R.id.breakfast_layout);
		chinesefood_layout = (RelativeLayout) findViewById(R.id.chinesefood_layout);
		dinner_layout = (RelativeLayout) findViewById(R.id.dinner_layout);
		idlehour_layout = (RelativeLayout) findViewById(R.id.idlehour_layout);

		breakfast_layout.setOnClickListener(this);
		chinesefood_layout.setOnClickListener(this);
		dinner_layout.setOnClickListener(this);
		idlehour_layout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.breakfast_layout:
			setChioceItem(0);
			break;
		case R.id.chinesefood_layout:
			setChioceItem(1);
			break;
		case R.id.dinner_layout:
			setChioceItem(2);
			break;
		case R.id.idlehour_layout:
			setChioceItem(3);
			break;
		}
	}

	// 获取数据
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 15; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", getResources().getDrawable(R.drawable.food_one));
			map.put("goodsName", "goods:" + (i + 1));
			list.add(map);
		}

		return list;
	}

	// 定义一个选中一个item后的处理
	public void setChioceItem(int index) {
		// 重置选项+隐藏所有Fragment
		clearChioce();
		transaction = fManager.beginTransaction();
		if (breakfast != null && chinesefood != null && dinner != null
				&& idlefood != null) {
			transaction.hide(breakfast).hide(chinesefood).hide(dinner)
					.hide(idlefood);
		}
		Bundle bundle = new Bundle();
		switch (index) {
		case 0:
			breakfast_text.setTextColor(blue);
			breakfast_layout.setBackgroundResource(R.color.lightgreen);
			if (breakfast == null) {
				breakfast = new DailyRecommendFragment();
//				bundle.putSerializable(
//						DailyRecommendFragment.ARG_SECTION_NUMBER,
//						(Serializable) getData());
//				bundle.putString(TYPESTRING, STR_BREAKFAST);
				// 如果fg1为空，则创建一个并添加到界面上
//				breakfast.setArguments(bundle);
			}
			transaction.replace(R.id.content, breakfast);
			transaction.addToBackStack(null);
			transaction.show(breakfast);
			transaction.commitAllowingStateLoss();
			break;

		case 1:
			chinesefood_text.setTextColor(blue);
			chinesefood_layout.setBackgroundResource(R.color.lightgreen);
			if (chinesefood == null) {
				chinesefood = new ChineseFoodFragment();
//				bundle.putSerializable(ChineseFoodFragment.ARG_SECTION_NUMBER,
//						(Serializable) getData());

//				bundle.putString(TYPESTRING, STR_CHINESEFOOD);
				// 如果fg1为空，则创建一个并添加到界面上
//				chinesefood.setArguments(bundle);
			}
			transaction.replace(R.id.content, chinesefood);
			transaction.addToBackStack(null);
			transaction.show(chinesefood);
			transaction.commitAllowingStateLoss();
			break;

		case 2:
			dinner_text.setTextColor(blue);
			dinner_layout.setBackgroundResource(R.color.lightgreen);
			if (dinner == null) {
				dinner = new DinnerFragment();
//				bundle.putSerializable(
//						DailyRecommendFragment.ARG_SECTION_NUMBER,
//						(Serializable) getData());

//				bundle.putString(TYPESTRING, STR_DINNERFOOD);
				// 如果fg1为空，则创建一个并添加到界面上
//				dinner.setArguments(bundle);
			}
			transaction.replace(R.id.content, dinner);
			transaction.addToBackStack(null);
			transaction.show(dinner);
			transaction.commitAllowingStateLoss();
			break;
		case 3:
			idlehour_text.setTextColor(blue);
			idlehour_layout.setBackgroundResource(R.color.lightgreen);
			if (idlefood == null) {
				idlefood = new IdleHourFragment();
//				bundle.putSerializable(
//						DailyRecommendFragment.ARG_SECTION_NUMBER,
//						(Serializable) getData());

//				bundle.putString(TYPESTRING, STR_IDLEFOOD);
				// 如果fg1为空，则创建一个并添加到界面上
//				idlefood.setArguments(bundle);
			}
			transaction.replace(R.id.content, idlefood);
			transaction.addToBackStack(null);
			transaction.show(idlefood);
			transaction.commitAllowingStateLoss();
			break;
		}
	}

	// 定义一个重置所有选项的方法
	public void clearChioce() {
		breakfast_layout.setBackgroundColor(getResources().getColor(R.color.lightgreen));
		breakfast_text.setTextColor(gray);
		chinesefood_layout.setBackgroundColor(getResources().getColor(R.color.lightgreen));
		chinesefood_text.setTextColor(gray);
		dinner_layout.setBackgroundColor(getResources().getColor(R.color.lightgreen));
		dinner_text.setTextColor(gray);
		idlehour_layout.setBackgroundColor(getResources().getColor(R.color.lightgreen));
		idlehour_text.setTextColor(gray);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {
			startActivity(new Intent(DailyRecommendActivity.this, LoginActivity.class));
        }
        return super.onKeyDown(keyCode, event);
	}
}
