package com.CollegeState.BuyActivity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.CollegeState.R;

/**
 * 用户导航activity
 * 
 * @author zc
 * 
 */
public class GuideViewActivity extends Activity {
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private View view1, view2, view3, view4;
	private int windowHeight;
	private int windowWidth;

	private ImageView iv1, iv2, iv3, iv4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_guide_view);

		// 得到手机宽高
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		Point outSize = new Point();
		wm.getDefaultDisplay().getSize(outSize);
		windowHeight = outSize.y;
		windowWidth = outSize.x;

		initViews();
		initViewPager();

	}

	public void load(ImageView iv, int id) {
		// 图片解析的配置
		BitmapFactory.Options option = new Options();
		BitmapFactory
				.decodeResource(getResources(), R.drawable.welcome, option);
		// 不去解析图片信息，只是去获取图片的头部信息
		option.inJustDecodeBounds = true;
		int imageWidth = option.outWidth;
		int imageHeight = option.outHeight;
		// 计算缩放比例
		double scaleX = imageWidth / windowWidth;
		double scaleY = imageHeight / windowHeight;
		double scale = 1;
		if (scaleX >= scaleY && scaleY >= 1) {
			scale = scaleX;
		} else if (scaleY >= scaleX && scaleX >= 1) {
			scale = scaleY;
		}
		// 去解析图片
		option.inJustDecodeBounds = false;
		// 进行缩放(采样率)
		option.inSampleSize = (int) scale;
		Bitmap bitmap = BitmapFactory
				.decodeResource(getResources(), id, option);
		iv.setImageBitmap(bitmap);
	}

	private void initViews() {
		view1 = LayoutInflater.from(GuideViewActivity.this).inflate(
				R.layout.guide_view_1, null);
		view2 = LayoutInflater.from(GuideViewActivity.this).inflate(
				R.layout.guide_view_2, null);
		view3 = LayoutInflater.from(GuideViewActivity.this).inflate(
				R.layout.guide_view_3, null);
		view4 = LayoutInflater.from(GuideViewActivity.this).inflate(
				R.layout.guide_view_4, null);

		iv1 = (ImageView) view1.findViewById(R.id.iv_guide_1);
		iv2 = (ImageView) view2.findViewById(R.id.iv_guide_2);
		iv3 = (ImageView) view3.findViewById(R.id.iv_guide_3);
		iv4 = (ImageView) view4.findViewById(R.id.iv_guide_4);

		load(iv2, R.drawable.guid_view_2);
		load(iv1, R.drawable.guid_view_1);
		load(iv3, R.drawable.guid_view_3);
		load(iv4, R.drawable.guid_view_4);
		view4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(GuideViewActivity.this, MainActivity.class);
				Toast.makeText(GuideViewActivity.this, "首次加载时间较长，请耐心等待", 1000)
						.show();
				onDestroy();
				finish();
				GuideViewActivity.this.startActivity(intent);
			}
		});
	}

	private void initViewPager() {
		pageViews = new ArrayList<View>();
		pageViews.add(view1);
		pageViews.add(view2);
		pageViews.add(view3);
		pageViews.add(view4);
		viewPager = (ViewPager) findViewById(R.id.ViewPagerGuideView);
		viewPager.setAdapter(new MyViewPagerAdapter());
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onDestroy() {
		setContentView(R.layout.view_null);
		destoryImage(iv1);
		destoryImage(iv2);
		destoryImage(iv3);
		destoryImage(iv4);
		viewPager = null;
		pageViews = null;
		view1 = null;
		view2 = null;
		view3 = null;
		view4 = null;
		super.onDestroy();
	}

	/**
	 * 回收内存
	 * 
	 * @param iv
	 */
	private void destoryImage(ImageView iv) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
		// 解除绑定
		iv.setImageBitmap(null);
		if (bitmapDrawable == null) {
			return;
		}
		Bitmap bitmap = bitmapDrawable.getBitmap();
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		bitmapDrawable = null;
	}

	/**
	 * ViewPager的适配器
	 * 
	 * @author zc
	 * 
	 */
	class MyViewPagerAdapter extends PagerAdapter {
		//
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(pageViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(pageViews.get(position));
			return pageViews.get(position);
		}

	}
}
