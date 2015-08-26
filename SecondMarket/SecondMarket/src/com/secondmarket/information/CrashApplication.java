package com.secondmarket.information;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

public class CrashApplication extends Application {
	// 第一次进入app
	public boolean isFirstIn = true;

	@Override
	public void onCreate() {
		super.onCreate();

		// 撞见默认的ImageLoader配置参数
		/*ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);*/
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.
				Builder(getApplicationContext()).build();
		ImageLoader.getInstance().init(configuration);
	}
}