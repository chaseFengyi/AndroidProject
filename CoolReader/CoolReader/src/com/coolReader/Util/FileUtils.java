package com.coolReader.Util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

import com.coolReader.Bean.WebContentBean;

public class FileUtils {
	
	public static void write(Context context, List<WebContentBean> list){
		final SharedPreferences preferences = context.getSharedPreferences("CoolReader_WebContent_SaveFile", 0);
		
		HashSet<String> set = new HashSet<String>();
		if(list == null || list.size() <= 0)
			return;
		for(int i = 0; i < list.size(); i++){
			set.add(list.get(i).getWebUrl());
		}
		SharedPreferences.Editor editor = preferences.edit();
		editor.putStringSet("urls", set);
		editor.commit();
	}
	
	public static Set<String> read(Context context){
		final SharedPreferences preferences = context.getSharedPreferences("CoolReader_WebContent_SaveFile", 0);
		Set<String> set = preferences.getStringSet("urls", null);
		
		return set;
	}
}
