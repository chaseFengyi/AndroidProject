package com.allnotes.view;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ListView;

/**
 * ����listView �ĸ߶�
 * @author FengYi~
 *2015��7��29��11:40:30
 */
public class ListViewHeightView {
	public static void setListViewHeight(Context context, ListView listView, int top, int bottom){
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		int height = metrics.heightPixels;

		android.view.ViewGroup.LayoutParams layoutParams = listView
				.getLayoutParams();
		layoutParams.height = top * height / bottom;
		listView.setLayoutParams(layoutParams);
	}
}
