package com.analysis.listview.drag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myanalysistools.R;

public class RefreshAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, String>> list = new ArrayList<Map<String,String>>();
	private LayoutInflater inflater;
	
	public RefreshAdapter(Context context, List<Map<String, String>> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(arg1 == null){
			arg1 = inflater.inflate(R.layout.sample_listview, null);
		}
		TextView content = (TextView)arg1.findViewById(R.id.content);
		content.setText(list.get(arg0).get("content"));
		return arg1;
	}

}
