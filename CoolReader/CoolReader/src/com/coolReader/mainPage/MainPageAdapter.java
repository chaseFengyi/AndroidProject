package com.coolReader.mainPage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.coolReader.R;
import com.coolReader.Bean.WebContentBean;

public class MainPageAdapter extends BaseAdapter {
	List<WebContentBean> list;
	private Context context;

	public MainPageAdapter(Context context, List<WebContentBean> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(list == null || list.size() <= 0)
			return null;
		else
			return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		
		if(list == null || list.size() <= 0)
			return view;

		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.sample_mainpage, null);
		}

		CheckBox checkBox = (CheckBox) view.findViewById(R.id.sample_checkBox);
		TextView title = (TextView) view
				.findViewById(R.id.sample_mainpage_title);
		TextView url = (TextView) view.findViewById(R.id.sample_mainpage_url);

		title.setText(list.get(position).getWebTitle());
		url.setText(list.get(position).getWebUrl());

		checkBox.setChecked(list.get(position).isChecked());
		if (MainPageActivity.flag == 0) {// 如果点击编辑按钮
			checkBox.setVisibility(View.VISIBLE);
		} else if (MainPageActivity.flag == 1) {
			checkBox.setVisibility(View.GONE);
		}
		
		if(MainPageActivity.isSaveFile){
			for(int i = 0; i < MainPageActivity.in.size(); i++){
				list.get(i).setChecked(false);
			}
			MainPageActivity.isSaveFile = false;
		}

		final WebContentBean mChecked = list.get(position);
		final int posi = position;

		//选中
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					mChecked.setChecked(true);
					MainPageActivity.in.add(posi);
					Set<Integer> set = new HashSet<Integer>();
					set.addAll(MainPageActivity.in);
					MainPageActivity.in.clear();
					MainPageActivity.in.addAll(set);
				}
			}
		});
		
		//取消选中
		checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!((CheckBox)v).isChecked()){
					mChecked.setChecked(false);
					Iterator iterator = MainPageActivity.in.iterator();
					while (iterator.hasNext()) {
						int integer = (Integer) iterator.next();
						if (integer == posi) {
							iterator.remove();
						}
					}
				}
			}
		});

		return view;
	}

}
