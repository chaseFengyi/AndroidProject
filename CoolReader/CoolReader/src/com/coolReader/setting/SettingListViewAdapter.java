package com.coolReader.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coolReader.R;

public class SettingListViewAdapter extends BaseAdapter{
	
	private int[] icons = new int[]{R.drawable.icon_hotline,R.drawable.icon_fankui,R.drawable.icon_step_1,
			R.drawable.icon_about,R.drawable.icon_yaoqinghaoyou};
	private String[] infos = new String[]{"�༭�˺�","�������","���°汾","��������","ע��"};
	
	private Context context;
	private LayoutInflater mInflater;
	
	public SettingListViewAdapter(Context context) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return icons.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//�����Ŀ�Ĳ��֣���̬������Ŀ������
		RelativeLayout rl = (RelativeLayout) mInflater.inflate(R.layout.item_setting_listview, null);
		ImageView iv_item_setting_view = (ImageView)rl.findViewById(R.id.iv_item_setting_view);
		TextView tv_item_setting_content = (TextView) rl.findViewById(R.id.tv_item_setting_content);
		iv_item_setting_view.setBackgroundResource(icons[position]);
		tv_item_setting_content.setText(infos[position]);
		return rl;
	}
}
