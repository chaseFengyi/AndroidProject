package com.secondmarket.information;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.secondmarket.DB.DBCreateWord;
import com.secondmarket.DB.DBPerform;
import com.secondmarket.bean.SortSearchDemo;
import com.secondmarket.load.R;
import com.secondmarket.mine.MineActivity;
import com.secondmarket.publish.PublishActivity;

public class SortDetailsActivity extends Activity {
	private Button sortdetails;
	private TextView content;
	private TextView priceIs;
	private TextView phoneIs;
	private Button information;
	private Button release;
	private Button mine2;
	private ImageButton collectSort;

	private SortSearchDemo sortSearchDemo = null;
	public static int flag = 1;// 判断点击按钮

	int[] images = null;// 图片资源ID
	String[] titles = null;// 标题

	ArrayList<ImageView> imageSource = null;// 图片资源
	ArrayList<View> dots = null;// 点
	TextView title = null;
	ViewPager viewPager;// 用于显示图片
	MyPagerAdapter adapter;// viewPager的适配器
	private int currPage = 0;// 当前显示的页
	private int oldPage = 0;// 上一次显示的页

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sortdetails);

		init();

		findView();
		onClick();
		createWindow();
		fillContent();
	}

	public void init() {
		images = new int[] { R.drawable.mathone, R.drawable.mathtwo,
				R.drawable.maththree, };
		titles = new String[] { "图片1", "图片2", "图片3", };
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

		// 开启定时器，每隔2秒自动播放下一张（通过调用线程实现）
		ScheduledExecutorService scheduled = Executors
				.newSingleThreadScheduledExecutor();
		// 设置一个线程，该线程用于通知UI线程变换图片
		ViewPagerTask pagerTask = new ViewPagerTask();
		scheduled.scheduleAtFixedRate(pagerTask, 2, 2, TimeUnit.SECONDS);//按指定的间隔重复执行
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

	private void findView() {
		sortdetails = (Button) findViewById(R.id.sortdetails);
		content = (TextView) findViewById(R.id.content);
		priceIs = (TextView) findViewById(R.id.priceIs);
		phoneIs = (TextView) findViewById(R.id.phoneIs);
		information = (Button) findViewById(R.id.information);
		release = (Button) findViewById(R.id.release);
		mine2 = (Button) findViewById(R.id.mine2);
		collectSort = (ImageButton) findViewById(R.id.collectSort);
	}

	private void onClick() {
		sortdetails.setOnClickListener(new SortDetailsListener());
		mine2.setOnClickListener(new MineListener());
		information.setOnClickListener(new InformationListener());
		mine2.setOnClickListener(new Mine2Listener());
		collectSort.setOnClickListener(new CollectSortListener());
		release.setOnClickListener(new ReleaseListener());
	}

	// 布局
	private void createWindow() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;

		content.setHeight(height / 7);
		priceIs.setHeight(height / 11);
		phoneIs.setHeight(height / 11);

	}

	class MineListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(SortDetailsActivity.this, MineActivity.class);
			startActivity(intent);
			finish();
		}

	}

	class SortDetailsListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("name", "");
			intent.setClass(SortDetailsActivity.this,
					SortInformationActivity.class);
			startActivity(intent);
			finish();
		}

	}

	class InformationListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startActivity(new Intent(SortDetailsActivity.this,
					MainPageActivity.class));
		}

	}

	class Mine2Listener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startActivity(new Intent(SortDetailsActivity.this,
					MineActivity.class));
		}

	}

	class CollectSortListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int sign;
			if (flag == 1) {
				DBPerform.createCollectTable(SortDetailsActivity.this);
				sign = DBPerform.insertQuery(SortDetailsActivity.this, sortSearchDemo,DBCreateWord.TB_NAME);
				if(sign == 1){
					Toast.makeText(SortDetailsActivity.this, "恭喜你，收藏成功！",
							Toast.LENGTH_SHORT).show();
				}else if(sign == 0){
					Toast.makeText(SortDetailsActivity.this, "该商品已经收藏，不能重新收藏！",
							Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(SortDetailsActivity.this, "对不起，收藏失败！",
							Toast.LENGTH_SHORT).show();
				}
				flag = 0;
			} else if (flag == 0) {
				Toast.makeText(SortDetailsActivity.this, "取消收藏",
						Toast.LENGTH_SHORT).show();
				sign = DBPerform.deleteQuery(SortDetailsActivity.this, sortSearchDemo,DBCreateWord.TB_NAME);
				if(sign == -1){
					Toast toast = Toast.makeText(SortDetailsActivity.this,
							"该商品不存在，取消收藏失败!", Toast.LENGTH_SHORT);
					toast.show();
				}else{
					Toast toast = Toast.makeText(SortDetailsActivity.this, "恭喜你！取消收藏成功",
							Toast.LENGTH_SHORT);
					toast.show();
				}
				flag = 1;
			}

		}

	}

	private class ReleaseListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startActivity(new Intent(SortDetailsActivity.this, PublishActivity.class));
		}
		
	}
	
	private void fillContent() {
		Intent intent = getIntent();
		sortSearchDemo = (SortSearchDemo) intent
				.getSerializableExtra("sortSearchDemo");
		// SortSearchDemo [isSale=0, goodsId=1, goodsConnect=1, userId=1,
		// goodsDescribe=高数书,
		// goodsTypeId=1, goodsPrice=23.0, goodsWanted=1,
		// goodsPublishTime=2014-1-1,
		// goodsName=高数, list=[{goodsPictureAD1=android.jpg}]]
		System.out.println("sortsearchdemo=" + sortSearchDemo);
		content.setText(sortSearchDemo.getGoodsDescribe());
		priceIs.setText("￥" + sortSearchDemo.getGoodsPrice());
		phoneIs.setText("发布时间:" + sortSearchDemo.getGoodsPublishTime());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra("name", "");
			intent.setClass(SortDetailsActivity.this,
					SortInformationActivity.class);
			startActivity(intent);
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
}
