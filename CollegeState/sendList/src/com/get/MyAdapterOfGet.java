package com.get;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sendlist.R;

public class MyAdapterOfGet extends BaseAdapter {
	private List<ViewHolderOfGet> goods;// ListView显示的数据

	private int resource;// 显示列表项的Layout

	private LayoutInflater inflater;// 界面生成器

	private Context context;

	public static int sign = 0;

	public MyAdapterOfGet(List<ViewHolderOfGet> goods, int resource,
			Context context) {

		this.goods = goods;

		this.resource = resource;

		this.context = context;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return goods.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return goods.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = inflater.inflate(resource, null);
		}
		final ViewHolderOfGet good = goods.get(arg0);
		TextView shopPhone = (TextView) arg1.findViewById(R.id.shopPhone);
		shopPhone.setTag(1);
		sign = 0;
//		shopPhone.setText(good.getShopPhone());
		shopPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sign = 1;
				AlertDialog.Builder builder = 
						new AlertDialog.Builder(context)
						.setTitle("提示")
						.setMessage("确定拨打 "+good.getShopPhone()+" 这个电话吗？")
						.setPositiveButton("确认", 
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										System.out.println("calling---->");
										arg0.dismiss();
									}
								})
						.setNegativeButton("取消", 
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										arg0.dismiss();
									}
								});
				builder.create().show();
			}
		});
		TextView shopName = (TextView) arg1.findViewById(R.id.shopName);
//		shopName.setText(good.getShopName());
		TextView takemoney = (TextView) arg1.findViewById(R.id.takemony);
//		takemoney.setText(good.getTakeMoney());
		TextView shopAddress = (TextView) arg1.findViewById(R.id.shopAddress);
//		shopAddress.setText(good.getShopAddress());
		return arg1;
	}

}
