package com.coolReader.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 检查各种网络状况
 * @author FengYi~
 *2015年8月13日15:43:35
 */
public class CheckNetWork {
	/**
	 * 判断是否有网络连接
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
	 * 判断WIFI网络是否可用
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
	 * 判断Mobile网络是否可用
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
	 * 获取当前网络连接类型信息
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
