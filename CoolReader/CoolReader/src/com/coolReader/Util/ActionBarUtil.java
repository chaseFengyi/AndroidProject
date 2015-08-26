package com.coolReader.Util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.coolReader.R;

/**
 * ����ҳ������
 * @author Xuptljw
 * 2015��7��22��21:01:50
 */
public class ActionBarUtil {
	public static void initMainActionBar(Context context,ActionBar actionBar,String title) {
		//ͼƬ��Դ
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bg_blue_single_line);
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		
		//�Զ��������
		actionBar.setBackgroundDrawable(bd);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(title);
	}
	
	public static void initActionBar(Context context,ActionBar actionBar,
			String title) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.bg_blue_single_line);
		@SuppressWarnings("deprecation")
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		
		//�Զ��������
		actionBar.setBackgroundDrawable(bd);
		actionBar.setDisplayHomeAsUpEnabled(true);
		//ʹ�����Ͻǵ�ͼ���Ƿ���ʾ���������Ϊfalse����û��ͼ�꣬������һ�����⣬������ʾӦ�ó���ͼ��
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setTitle(title);
	}
	
	public static void centerActionBar(Context context,final Activity activity,ActionBar actionBar,String title) {
		LayoutInflater mInflater = LayoutInflater.from(context);
		//ͼƬ��Դ
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.color.paleturquoise);
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT,
				Gravity.CENTER);
		View viewTitleBar = mInflater.inflate(R.layout.top_back_center_bar, null);
		
		actionBar.setCustomView(viewTitleBar,lp);
		
		//���ñ���
		TextView tx = (TextView) actionBar.getCustomView().findViewById(R.id.title);
		tx.setText(title);
		tx.setTextSize(20);
		ImageView ibvw = (ImageView) actionBar.getCustomView().findViewById(R.id.left_btn);;
		
		actionBar.setBackgroundDrawable(bd);
		actionBar.setDisplayShowHomeEnabled(false); //ȥ������
		actionBar.setDisplayShowTitleEnabled(false);//ȥ������
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		
		ibvw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
	}
	
	public static void centerActionBarAndRtn(Context context,final Activity activity,
			ActionBar actionBar,String titile,String btnName) {
		LayoutInflater mInflater = LayoutInflater.from(context);
		//ͼƬ��Դ
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.color.paleturquoise);
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT,
				Gravity.CENTER);
		View viewTitleBar = mInflater.inflate(R.layout.top_center_bar_rt, null);
		
		actionBar.setCustomView(viewTitleBar,lp);
		//�Զ�����еı���
		TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.tx_setting_edit_title);
		textView.setText(titile);
		textView.setTextSize(20);
		
		//���ò���������
		TextView textBtn = (TextView) actionBar.getCustomView().findViewById(R.id.tx_setting_edit_save);
		textBtn.setText(btnName);
		textBtn.setTextSize(20);
		
		ImageView ibvw = (ImageView) actionBar.getCustomView().findViewById(R.id.iv_setting_edit_left_btn);;
		
		actionBar.setBackgroundDrawable(bd);
		actionBar.setDisplayShowHomeEnabled(false); //ȥ������
		actionBar.setDisplayShowTitleEnabled(false);//ȥ������
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		
		ibvw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
	}
}
