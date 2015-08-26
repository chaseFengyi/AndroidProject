package com.CollegeState.Util;

import android.content.Context;

/**
 * �жϽ����Ƿ�������
 * @author zc
 *
 */
public class Preferences {

	public static final String SHAREDPREFERENCES_NAME = "my_pref";
	// ��������KEY
	private static final String SHOP_ACTIVITY_GUIDE = "shop_activity";

	/**
	 * �ж��Ƿ�������
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
				.getString(SHOP_ACTIVITY_GUIDE, "").split("\\|");// ȡ���������� ��
		for (String string : classNames) {
			if (className.equalsIgnoreCase(string)) {
				return true;
			}
		}
		return false;
	}

	//�����Ѿ�����
	public static void setIsGuided(Context context, String className) {
		if (context == null || className == null
				|| "".equalsIgnoreCase(className))
			return;
		String classNames = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				SHOP_ACTIVITY_GUIDE, "");
		StringBuilder sb = new StringBuilder(classNames).append("|").append(
				className);// ���ֵ
		context.getSharedPreferences(SHAREDPREFERENCES_NAME,
				Context.MODE_WORLD_READABLE)// �����޸ĺ��ֵ
				.edit().putString(SHOP_ACTIVITY_GUIDE, sb.toString()).commit();
	}
}
