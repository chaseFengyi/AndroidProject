package com.CollegeState.BuyActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import com.CollegeState.R;

public class StartActivity extends Activity {
	private SharedPreferences sharedPreference = null;
	private SharedPreferences.Editor editor = null;
	private final String SHARED_PREF_NAME = "USED_COUNT";
	private final String SHARED_PREF_USE_COUNT = "use_count";
	
	private int windowHeight;
	private int windowWidth;
	private ImageView iv;
	private Animation alphaAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_start);
		// 得到手机宽高
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		Point outSize = new Point();
		wm.getDefaultDisplay().getSize(outSize);
		windowHeight = outSize.y;
		windowWidth = outSize.x;
		iv = (ImageView) findViewById(R.id.ImageViewStart);
		load(iv);
		alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		alphaAnimation.setDuration(1800);
		alphaAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				int count = getUsedCount();
				// 第一次进入程序
				if (count == 0) {
					sharedPreference = getSharedPreferences(SHARED_PREF_NAME,
							MODE_PRIVATE);
					editor = sharedPreference.edit();
					// 第一次进入程序之后标记为使用次数为一次
					editor.putInt(SHARED_PREF_USE_COUNT, 1);
					editor.commit();
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.setClass(StartActivity.this, GuideViewActivity.class);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
					finish();
				} else {
					sharedPreference = getSharedPreferences(SHARED_PREF_NAME,
							MODE_PRIVATE);
					editor = sharedPreference.edit();
					// 每次进入程序使用次数加1
					editor.putInt(SHARED_PREF_USE_COUNT, count + 1);
					editor.commit();
					// 跳转到主菜单
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.setClass(StartActivity.this, MainActivity.class);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
					finish();
				}

			}
		});
		iv.startAnimation(alphaAnimation);
	}

	public void load(View view) {
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
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.welcome, option);
		iv.setImageBitmap(bitmap);
	}
	/**
	 * 当activity启动时自动从SharedPreference中程序启动次数
	 */
	private int getUsedCount() {
		sharedPreference = getSharedPreferences(SHARED_PREF_NAME,
				MODE_PRIVATE);
		// 从手机中获取SharedPreference，获取失败时返回0
		int count = sharedPreference.getInt(SHARED_PREF_USE_COUNT, 0);
		return count;
	}

	@Override
	protected void onDestroy() {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) iv
				.getDrawable();
		if (!bitmapDrawable.getBitmap().isRecycled()){
			bitmapDrawable.getBitmap().recycle();
		}
		bitmapDrawable = null;
		iv = null;
		super.onDestroy();
	}
}
