package com.CollegeState.Util;

import android.content.Context;

/**
 * 判断界面是否被引导过
 * @author zc
 *
 */
public class Preferences {

	public static final String SHAREDPREFERENCES_NAME = "my_pref";
	// 引导界面KEY
	private static final String SHOP_ACTIVITY_GUIDE = "shop_activity";

	/**
	 * 判断是否引导过
	 * @param context
	 * @param className
	 * @return
	 */
	public static boolean activityIsGuided(Context context, String className) {
		if (context == null || className == null
				|| "".equalsIgnoreCase(className))
			return false;
		String[] classNames = context
				.getSharedPreferences(SHAREDPREFERENCES_NAME,
						Context.MODE_WORLD_READABLE)
				.getString(SHOP_ACTIVITY_GUIDE, "").split("\\|");// 取得所有类名 如
		for (String string : classNames) {
			if (className.equalsIgnoreCase(string)) {
				return true;
			}
		}
		return false;
	}

	//设置已经引导
	public static void setIsGuided(Context context, String className) {
		if (context == null || className == null
				|| "".equalsIgnoreCase(className))
			return;
		String classNames = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				SHOP_ACTIVITY_GUIDE, "");
		StringBuilder sb = new StringBuilder(classNames).append("|").append(
				className);// 添加值
		context.getSharedPreferences(SHAREDPREFERENCES_NAME,
				Context.MODE_WORLD_READABLE)// 保存修改后的值
				.edit().putString(SHOP_ACTIVITY_GUIDE, sb.toString()).commit();
	}
}
