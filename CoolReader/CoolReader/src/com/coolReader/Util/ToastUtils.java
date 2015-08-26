package com.coolReader.Util;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.coolReader.R;

/**
 * 吐司的工具类
 * @author Xuptljw
 * 2015年7月22日15:19:32
 */
public class ToastUtils {
	public static void makeToast(Activity activity,String message) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		
		//根据指定的布局文件创建一个具有层级关系的View对象
		//第二个参数为View对象的根节点，即LinerLayout的ID
		View layout = inflater.inflate(R.layout.toast_common,
				(ViewGroup)activity.findViewById(R.id.ll_toast_common));
		//查找TextView控件
		TextView text = (TextView) layout.findViewById(R.id.tv_common_toast);
		text.setText(message);
		Toast toast = new Toast(activity);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(2000);
		toast.setView(layout);
		toast.show();
	}
}
