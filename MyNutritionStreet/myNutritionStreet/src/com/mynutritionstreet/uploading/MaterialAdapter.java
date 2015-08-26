package com.mynutritionstreet.uploading;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mynutritionstreet.R;
import com.mynutritionstreet.Bean.MaterialBean;

public class MaterialAdapter extends BaseAdapter {
	Context context;
	List<MaterialBean> list;
	static int id = 1;

	public MaterialAdapter(Context context, List<MaterialBean> list) {
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

		if (convertView == null)
			view = LayoutInflater.from(context).inflate(
					R.layout.sample_material, null);

		final ImageView deleteImg = (ImageView) view
				.findViewById(R.id.sample_material_delete);
//		ImageView pictureImg = (ImageView) view
//				.findViewById(R.id.sample_picture);
		TextView foodNameExt = (TextView) view
				.findViewById(R.id.sample_material_name);
		TextView foodWeigthExt = (TextView) view
				.findViewById(R.id.sample_material_weight);
		System.out.println("adapter-list-name=" + list);
		foodNameExt.setText(list.get(position).getMaterialName());
		foodWeigthExt.setText(list.get(position).getMaterilaWeight() + "");

		final int posi = position;
		view.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deleteImg.setVisibility(View.VISIBLE);
				// 设置选中的背景颜色
				v.setBackgroundColor(context.getResources().getColor(
						R.color.cornsilk));
				final View myView = v;
				deleteImg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						list.remove(posi);
						notifyDataSetChanged();
					}
				});
				return true;
			}
		});

		// 按下不选中
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 设 置背景颜色为不选中
				v.setBackgroundColor(context.getResources().getColor(
						R.color.white));
				deleteImg.setVisibility(View.GONE);
			}
		});

		return view;
	}

}
