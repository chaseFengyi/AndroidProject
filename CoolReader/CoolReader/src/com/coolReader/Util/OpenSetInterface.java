package com.coolReader.Util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * 打开系统设置界面
 * @author FengYi~
 *2015年8月15日10:51:23
 */
public class OpenSetInterface {
	public static void openSetInterface(Context context){
		Intent intent = new Intent("/");
		ComponentName componentName = new ComponentName(context, "com.android.settings.ApnSettings");
		intent.setComponent(componentName);
		intent.setAction("android.intent.action.VIEW");
		context.startActivity(intent);
	}
}
