package com.allnotes.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.allnotes.R;
import com.allnotes.bean.NoteClassInfoBean;

public class RightFragmentAdapter extends BaseAdapter {
	private LayoutInflater inflater = null;
	List<NoteClassInfoBean> list;

	public RightFragmentAdapter(Context context, List<NoteClassInfoBean> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
		inflater = LayoutInflater.from(context);
		System.out.println("list-adapter="+list);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
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
		ViewHolder holder;
		if(convertView == null){
			view = inflater.inflate(R.layout.sample_fragment_note_right, null);
			holder = new ViewHolder();
			//得到各个控件的对象
			holder.typeName = (TextView)view.findViewById(R.id.note_right_typeName);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		
		//设置TextView现实的内容
		holder.typeName.setText(list.get(position).getNoteClassName());
		
		return view;
	}

	public final class ViewHolder{
		public TextView typeName;
	}
	
}
