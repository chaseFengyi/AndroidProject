package com.secondmarket.information;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

public class CrashApplication extends Application {
	// ��һ�ν���app
	public boolean isFirstIn = true;

	@Override
	public void onCreate() {
		super.onCreate();

		// ײ��Ĭ�ϵ�ImageLoader���ò���
		/*ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);*/
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.
				Builder(getApplicationContext()).build();
		ImageLoader.getInstance().init(configuration);
	}
}