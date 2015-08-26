package com.mynutritionstreet.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyUtil {
	private static RequestQueue mRequestQueue;
	
	public static void initialize(Context context){
		if(mRequestQueue == null){
			synchronized (VolleyUtil.class) {
				if(mRequestQueue == null){
					mRequestQueue = Volley.newRequestQueue(context);
				}
			}
		}
		mRequestQueue.start();
	}
	
	public static RequestQueue getRequestQueue(){
		if(mRequestQueue == null){
			throw new RuntimeException("«Îœ»≥ı ºªØmRequestQueue");
		}
		
		return mRequestQueue;
	}
}
