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
		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_start);
		// �õ��ֻ����
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
				// ��һ�ν������
				if (count == 0) {
					sharedPreference = getSharedPreferences(SHARED_PREF_NAME,
							MODE_PRIVATE);
					editor = sharedPreference.edit();
					// ��һ�ν������֮����Ϊʹ�ô���Ϊһ��
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
					// ÿ�ν������ʹ�ô�����1
					editor.putInt(SHARED_PREF_USE_COUNT, count + 1);
					editor.commit();
					// ��ת�����˵�
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
		// ͼƬ����������
		BitmapFactory.Options option = new Options();
		BitmapFactory
				.decodeResource(getResources(), R.drawable.welcome, option);
		// ��ȥ����ͼƬ��Ϣ��ֻ��ȥ��ȡͼƬ��ͷ����Ϣ
		option.inJustDecodeBounds = true;
		int imageWidth = option.outWidth;
		int imageHeight = option.outHeight;
		// �������ű���
		double scaleX = imageWidth / windowWidth;
		double scaleY = imageHeight / windowHeight;
		double scale = 1;
		if (scaleX >= scaleY && scaleY >= 1) {
			scale = scaleX;
		} else if (scaleY >= scaleX && scaleX >= 1) {
			scale = scaleY;
		}
		// ȥ����ͼƬ
		option.inJustDecodeBounds = false;
		// ��������(������)
		option.inSampleSize = (int) scale;
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.welcome, option);
		iv.setImageBitmap(bitmap);
	}
	/**
	 * ��activity����ʱ�Զ���SharedPreference�г�����������
	 */
	private int getUsedCount() {
		sharedPreference = getSharedPreferences(SHARED_PREF_NAME,
				MODE_PRIVATE);
		// ���ֻ��л�ȡSharedPreference����ȡʧ��ʱ����0
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
