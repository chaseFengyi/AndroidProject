package com.coolReader.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolReader.R;

/**
 * 等待进度条或对话框，提升用户体验
 * @author Xuptljw
 * 2015年7月22日16:00:00
 */
public class DialogUtils {
	
	public static Dialog createProgressDialog(Context context,String title,
			String message) {
		LayoutInflater mInflater = LayoutInflater.from(context);
		LinearLayout ll_dialog_view = (LinearLayout) mInflater.inflate(
				R.layout.dialog_progress_loading, null);
		ImageView iv_progress_dialog_progress = (ImageView) ll_dialog_view.
				findViewById(R.id.iv_progress_dialog_progress);
		
		TextView tv_progress_dialog_title = (TextView)ll_dialog_view.
				findViewById(R.id.tv_progress_dialog_title);
		
		TextView tv_progress_dialog_message = (TextView)ll_dialog_view.
				findViewById(R.id.tv_progress_dialog_message);
		
		Animation anim = AnimationUtils.loadAnimation(context, 
				R.anim.loading_progress_anim);
		iv_progress_dialog_progress.startAnimation(anim);
		tv_progress_dialog_title.setText(title);
		tv_progress_dialog_message.setText(message);
		
		return new AlertDialog.Builder(context).setView(ll_dialog_view).create();
	}
}
