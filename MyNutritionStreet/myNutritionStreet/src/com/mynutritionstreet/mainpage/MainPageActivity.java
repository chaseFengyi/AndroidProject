package com.mynutritionstreet.mainpage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.example.mynutritionstreet.R;
import com.mynutritionstreet.dailyquestion.DailyQuestionActivity;
import com.mynutritionstreet.goodFoodCircle.GoodFoodCircleActivity;
import com.mynutritionstreet.login.LoginActivity;
import com.mynutritionstreet.mixinfo.SysApplication;
import com.mynutritionstreet.myinfo.MyInfoActivity;
import com.mynutritionstreet.pictureutil.ImageUtile;
import com.mynutritionstreet.register.RegisterActivity;
import com.mynutritionstreet.setting.SetActivity;
import com.mynutritionstreet.uploading.UploadingActivity;

public class MainPageActivity extends Activity implements OnClickListener {
	private ImageView drawer;

	private int[] images = null;// ͼƬ��ԴID
	private String[] titles = null;// ����

	private ArrayList<ImageView> imageSource = null;// ͼƬ��Դ
	private ArrayList<View> dots = null;// ��
	private TextView title = null;
	private ViewPager viewPager;// ������ʾͼƬ
	MyPagerAdapter adapter;// viewPager��������
	private int currPage = 0;// ��ǰ��ʾ��ҳ
	private int oldPage = 0;// ��һ����ʾ��ҳ

	private Button breakfastBtn;
	private Button chinese_foodBtn;
	private Button dinnerBtn;
	private Button idle_foodBtn;
	private ListView leftListView;
	private LinearLayout leftLayout;

	private DrawerLayout drawerLayout;
	
	private ImageView photo;
	private Button dailyQuestionBtn;
	private Button goodFoodCircleBtn;
	private Button mineBtn;
	private Button settingBtn;

	// �໬listView��list�б�
	private List<Map<String, Object>> lists;
	private TabHost tabHost;
	
	//���ϴ���Ʒ��Ϣ��ʱ��Ҫ���ݵĲ����Ӷ��жϵ������һ��
	public final static String BREAKFAST = "breakfast";
	public final static String CHINESEFOOD = "chinesefood";
	public final static String DINNERFOOD = "dinnerfood";
	public final static String IDLEFOOD = "idlefood";
	public final static String TAG = "tag";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mainpage);
		SysApplication.getInstance().addActivity(this); 

		init();
		findView();
		setAdapter();
		onclick();

//		tabHost = getTabHost();
		tabHost = (TabHost)findViewById(android.R.id.tabhost);
		
		LocalActivityManager mlLocalActivityManager = new LocalActivityManager(this, false);
		mlLocalActivityManager.dispatchCreate(savedInstanceState);
		//��ʼ����TabConnect��TabWidget
		tabHost.setup(mlLocalActivityManager);

		TabSpec daily = tabHost.newTabSpec("tab1").setIndicator("�����Ƽ�").setContent(new Intent(MainPageActivity.this,DailyRecommendActivity.class));
		tabHost.addTab(daily);

		TabSpec self = tabHost.newTabSpec("tab2").setIndicator("�Լ���")
				.setContent(R.id.tab02);
		tabHost.addTab(self);

		Bitmap bitmap = ImageUtile.drawableToBitmap(getResources().getDrawable(R.drawable.person_picture));
		bitmap = ImageUtile.zoomBitmap(bitmap, 120, 130);
	    bitmap = ImageUtile.getRoundedCornerBitmap(bitmap, 20);
	    photo.setImageBitmap(bitmap);
	    dailyQuestionBtn = (Button) setWidget(dailyQuestionBtn);
		goodFoodCircleBtn = (Button) setWidget(goodFoodCircleBtn);
		mineBtn = (Button) setWidget(mineBtn);
		settingBtn = (Button) setWidget(settingBtn);
	}

	private void findView() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		drawer = (ImageView) findViewById(R.id.dailyRecommendBack);
		breakfastBtn = (Button) findViewById(R.id.breakfast_mainpage);
		chinese_foodBtn = (Button) findViewById(R.id.chinese_food_mainpage);
		dinnerBtn = (Button) findViewById(R.id.dinner_mainpage);
		idle_foodBtn = (Button) findViewById(R.id.idle_hours_mainpage);
		leftLayout = (LinearLayout) findViewById(R.id.leftLinear_mainpage);
		photo = (ImageView)findViewById(R.id.photo_mainpage);
		dailyQuestionBtn = (Button)findViewById(R.id.dailyQuestion_mainpage);
		goodFoodCircleBtn = (Button)findViewById(R.id.goodFoodCircle_mainpage);
		mineBtn = (Button)findViewById(R.id.mine_mainpage);
		settingBtn = (Button)findViewById(R.id.setting_mainpage);
	}

	private void setAdapter() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int height = metrics.heightPixels;
		
		int w = View.MeasureSpec.makeMeasureSpec(0,  
                View.MeasureSpec.UNSPECIFIED);  
        int h = View.MeasureSpec.makeMeasureSpec(0,  
                View.MeasureSpec.UNSPECIFIED);  
        photo.measure(w, h);
        photo.setPadding(20, height/12, 0, 30);
		
	}

	private void onclick() {
		drawer.setOnClickListener(this);
		breakfastBtn.setOnClickListener(this);
		chinese_foodBtn.setOnClickListener(this);
		dinnerBtn.setOnClickListener(this);
		idle_foodBtn.setOnClickListener(this);
		dailyQuestionBtn.setOnClickListener(this);
		goodFoodCircleBtn.setOnClickListener(this);
		mineBtn.setOnClickListener(this);
		settingBtn.setOnClickListener(this);
	}

	// ���ؼ���������
	private TextView setWidget(TextView view) {
		view.setTextSize(24);
		view.setBackground(null);
		view.setTextColor(Color.WHITE);
		return view;
	}

	// ���û���ͼƬ
	public void init() {
		images = new int[] { R.drawable.food_one, R.drawable.food_two,
				R.drawable.food_three };
		titles = new String[] { "����", "���Ĳ�", "�ٹ���" };
		// ��Ҫ��ʾ��ͼƬ�ŵ�list������
		imageSource = new ArrayList<ImageView>();
		for (int i = 0; i < images.length; i++) {
			ImageView image = new ImageView(this);
			image.setBackgroundResource(images[i]);
			imageSource.add(image);
		}

		// ��ȡ��ʾ�ĵ㣨�������·��ĵ㣬��ʾ��ǰ�ǵڼ��ţ�
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.dot1));
		dots.add(findViewById(R.id.dot2));
		dots.add(findViewById(R.id.dot3));

		// ͼƬ�ı���
		title = (TextView) findViewById(R.id.title);
		title.setText(titles[0]);

		// ��ʾͼƬ��VIew
		viewPager = (ViewPager) findViewById(R.id.vp);
		// ΪviewPager����������
		adapter = new MyPagerAdapter();
		viewPager.setAdapter(adapter);
		// ΪviewPager��Ӽ��������ü��������ڵ�ͼƬ�任ʱ������͵�Ҳ���ű仯
		MyPageChangeListener listener = new MyPageChangeListener();
		viewPager.setOnPageChangeListener(listener);

		// ������ʱ����ÿ��2���Զ�������һ�ţ�ͨ�������߳�ʵ�֣�����Timer���ƣ���ʹ��Timer���棩
		ScheduledExecutorService scheduled = Executors
				.newSingleThreadScheduledExecutor();
		// ����һ���̣߳����߳�����֪ͨUI�̱߳任ͼƬ
		ViewPagerTask pagerTask = new ViewPagerTask();
		scheduled.scheduleAtFixedRate(pagerTask, 2, 2, TimeUnit.SECONDS);

		// http://www.cnblogs.com/gzggyy/archive/2012/04/26/2471063.html
	}

	// ViewPagerÿ�ν�����������ͼƬ�������ڷ�ֹ�����ڴ������
	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// �жϽ�Ҫ��ʾ��ͼƬ�Ƿ��������ʾ��ͼƬ��ͬһ��
			// arg0Ϊ��ǰ��ʾ��ͼƬ��arg1�ǽ�Ҫ��ʾ��ͼƬ
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// ���ٸ�ͼƬ
			container.removeView(imageSource.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// ��ʼ����Ҫ��ʾ��ͼƬ������ͼƬ���뵽container�У���viewPager��
			container.addView(imageSource.get(position));
			return imageSource.get(position);
		}
	}

	// ����ViewPager�ı仯
	private class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			// ����ʾ��ͼƬ�����仯֮��
			// ���ñ���
			title.setText(titles[position]);
			// �ı���״̬
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			dots.get(oldPage).setBackgroundResource(R.drawable.dot_normal);
			// ��¼��ҳ��
			oldPage = position;
			currPage = position;
		}
	}

	private class ViewPagerTask implements Runnable {
		@Override
		public void run() {
			// �ı䵱ǰҳ���ֵ
			currPage = (currPage + 1) % images.length;
			// ������Ϣ��UI�߳�
			handler.sendEmptyMessage(0);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// ���յ���Ϣ�󣬸���ҳ��
			viewPager.setCurrentItem(currPage);
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.dailyRecommendBack:
			drawerLayout.openDrawer(Gravity.LEFT);
			break;
		case R.id.breakfast_mainpage:
			intent.putExtra(TAG, BREAKFAST);
			intent.setClass(MainPageActivity.this, UploadingActivity.class);
			startActivity(intent);
			break;
		case R.id.chinese_food_mainpage:
			intent.putExtra(TAG, CHINESEFOOD);
			intent.setClass(MainPageActivity.this, UploadingActivity.class);
			startActivity(intent);
			break;
		case R.id.dinner_mainpage:
			intent.putExtra(TAG, DINNERFOOD);
			intent.setClass(MainPageActivity.this, UploadingActivity.class);
			startActivity(intent);
			break;
		case R.id.idle_hours_mainpage:
			intent.putExtra(TAG, IDLEFOOD);
			intent.setClass(MainPageActivity.this, UploadingActivity.class);
			startActivity(intent);
			break;
		case R.id.dailyQuestion_mainpage:
			startActivity(new Intent(MainPageActivity.this, DailyQuestionActivity.class));
			break;
		case R.id.goodFoodCircle_mainpage:
			startActivity(new Intent(MainPageActivity.this, GoodFoodCircleActivity.class));
			break;
		case R.id.mine_mainpage:
			startActivity(new Intent(MainPageActivity.this, MyInfoActivity.class));
			break;
		case R.id.setting_mainpage:
			startActivity(new Intent(MainPageActivity.this, SetActivity.class));
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {
			startActivity(new Intent(MainPageActivity.this, LoginActivity.class));
        }
        return super.onKeyDown(keyCode, event);
	}
}
