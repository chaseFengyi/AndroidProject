package com.coolReader.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * ����������״��
 * @author FengYi~
 *2015��8��13��15:43:35
 */
public class CheckNetWork {
	/**
	 * �ж��Ƿ�����������
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context){
		if(context != null){
			ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			if(info != null){
				return info.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * �ж�WIFI�����Ƿ����
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context){
		if(context != null){
			ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(info != null){
				return info.isAvailable();
			}
		}
		
		return false;
	}
	
	/**
	 * �ж�Mobile�����Ƿ����
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context){
		if(context != null){
			ConnectivityManager connectivityManager  = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if(info != null){
				return info.isAvailable();
			}
		}
		
		return false;
	}
	
	/**
	 * ��ȡ��ǰ��������������Ϣ
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context){
		if(context != null){
			ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			if(info != null && info.isAvailable()){
				return info.getType();
			}
		}
		return -1;
	}
}
