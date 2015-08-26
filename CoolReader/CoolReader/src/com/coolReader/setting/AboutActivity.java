package com.coolReader.setting;

import android.app.Activity;
import android.os.Bundle;

import com.coolReader.R;
import com.coolReader.Util.ActionBarUtil;

public class AboutActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setting_about_us);
		ActionBarUtil.centerActionBar(this, this, getActionBar(), "关于我们");
	}	
}
