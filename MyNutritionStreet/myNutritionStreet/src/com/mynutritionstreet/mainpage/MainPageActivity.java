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

	private int[] images = null;// 图片资源ID
	private String[] titles = null;// 标题

	private ArrayList<ImageView> imageSource = null;// 图片资源
	private ArrayList<View> dots = null;// 点
	private TextView title = null;
	private ViewPager viewPager;// 用于显示图片
	MyPagerAdapter adapter;// viewPager的适配器
	private int currPage = 0;// 当前显示的页
	private int oldPage = 0;// 上一次显示的页

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

	// 侧滑listView的list列表
	private List<Map<String, Object>> lists;
	private TabHost tabHost;
	
	//在上传商品信息的时候，要传递的参数从而判断点击的那一项
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
		//初始化了TabConnect和TabWidget
		tabHost.setup(mlLocalActivityManager);

		TabSpec daily = tabHost.newTabSpec("tab1").setIndicator("今日推荐").setContent(new Intent(MainPageActivity.this,DailyRecommendActivity.class));
		tabHost.addTab(daily);

		TabSpec self = tabHost.newTabSpec("tab2").setIndicator("自己配")
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

	// 给控件设置属性
	private TextView setWidget(TextView view) {
		view.setTextSize(24);
		view.setBackground(null);
		view.setTextColor(Color.WHITE);
		return view;
	}

	// 设置滑动图片
	public void init() {
		images = new int[] { R.drawable.food_one, R.drawable.food_two,
				R.drawable.food_three };
		titles = new String[] { "丸子", "开心菜", "百果鲜" };
		// 将要显示的图片放到list集合中
		imageSource = new ArrayList<ImageView>();
		for (int i = 0; i < images.length; i++) {
			ImageView image = new ImageView(this);
			image.setBackgroundResource(images[i]);
			imageSource.add(image);
		}

		// 获取显示的点（即文字下方的点，表示当前是第几张）
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.dot1));
		dots.add(findViewById(R.id.dot2));
		dots.add(findViewById(R.id.dot3));

		// 图片的标题
		title = (TextView) findViewById(R.id.title);
		title.setText(titles[0]);

		// 显示图片的VIew
		viewPager = (ViewPager) findViewById(R.id.vp);
		// 为viewPager设置适配器
		adapter = new MyPagerAdapter();
		viewPager.setAdapter(adapter);
		// 为viewPager添加监听器，该监听器用于当图片变换时，标题和点也跟着变化
		MyPageChangeListener listener = new MyPageChangeListener();
		viewPager.setOnPageChangeListener(listener);

		// 开启定时器，每隔2秒自动播放下一张（通过调用线程实现）（与Timer类似，可使用Timer代替）
		ScheduledExecutorService scheduled = Executors
				.newSingleThreadScheduledExecutor();
		// 设置一个线程，该线程用于通知UI线程变换图片
		ViewPagerTask pagerTask = new ViewPagerTask();
		scheduled.scheduleAtFixedRate(pagerTask, 2, 2, TimeUnit.SECONDS);

		// http://www.cnblogs.com/gzggyy/archive/2012/04/26/2471063.html
	}

	// ViewPager每次仅最多加载三张图片（有利于防止发送内存溢出）
	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// 判断将要显示的图片是否和现在显示的图片是同一个
			// arg0为当前显示的图片，arg1是将要显示的图片
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// 销毁该图片
			container.removeView(imageSource.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 初始化将要显示的图片，将该图片加入到container中，即viewPager中
			container.addView(imageSource.get(position));
			return imageSource.get(position);
		}
	}

	// 监听ViewPager的变化
	private class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			// 当显示的图片发生变化之后
			// 设置标题
			title.setText(titles[position]);
			// 改变点的状态
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			dots.get(oldPage).setBackgroundResource(R.drawable.dot_normal);
			// 记录的页面
			oldPage = position;
			currPage = position;
		}
	}

	private class ViewPagerTask implements Runnable {
		@Override
		public void run() {
			// 改变当前页面的值
			currPage = (currPage + 1) % images.length;
			// 发送消息给UI线程
			handler.sendEmptyMessage(0);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 接收到消息后，更新页面
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
