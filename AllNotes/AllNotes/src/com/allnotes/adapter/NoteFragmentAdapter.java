package com.allnotes.adapter;

import java.util.List;

import com.allnotes.R;
import com.allnotes.bean.NoteInfoBean;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class NoteFragmentAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater = null;
	List<NoteInfoBean> list;
	
	public NoteFragmentAdapter(Context context, List<NoteInfoBean> list) {
		// TODO Auto-generated constructor stub
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
		
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.sample_fragment_note, null);
			holder.title = (TextView)convertView.findViewById(R.id.sample_note_title);
			holder.author = (TextView)convertView.findViewById(R.id.sample_note_author);
			holder.modified = (TextView)convertView.findViewById(R.id.sample_note_modified);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText("笔记名称：\t\t\t"+Html.fromHtml(list.get(position).getTitle()),BufferType.NORMAL);
		holder.author.setText("创建者   ：\t\t\t"+list.get(position).getAuthor());
		holder.modified.setText("修改时间：\t\t\t"+list.get(position).getModified());
		
		return convertView;
	}
	
	public final class ViewHolder{
		public TextView title;
		public TextView author;
		public TextView modified;
	}

}
