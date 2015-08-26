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
 * ��˾�Ĺ�����
 * @author Xuptljw
 * 2015��7��22��15:19:32
 */
public class ToastUtils {
	public static void makeToast(Activity activity,String message) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		
		//����ָ���Ĳ����ļ�����һ�����в㼶��ϵ��View����
		//�ڶ�������ΪView����ĸ��ڵ㣬��LinerLayout��ID
		View layout = inflater.inflate(R.layout.toast_common,
				(ViewGroup)activity.findViewById(R.id.ll_toast_common));
		//����TextView�ؼ�
		TextView text = (TextView) layout.findViewById(R.id.tv_common_toast);
		text.setText(message);
		Toast toast = new Toast(activity);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(2000);
		toast.setView(layout);
		toast.show();
	}
}
