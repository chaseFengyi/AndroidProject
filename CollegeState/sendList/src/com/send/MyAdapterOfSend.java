package com.send;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sendlist.R;

public class MyAdapterOfSend extends BaseAdapter {
	private List<ViewHolderOfSend> goods;// ListView��ʾ������

	private int resource;// ��ʾ�б����Layout

	private LayoutInflater inflater;// ����������

	private Context context;

	public static int sign = 0;
	
	public static String phone = "";
	
	public MyAdapterOfSend(List<ViewHolderOfSend> goods, int resource, Context context) {

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

		final ViewHolderOfSend good = goods.get(arg0);

		TextView orderPhone = (TextView) arg1.findViewById(R.id.orderPhone);
		
		orderPhone.setTag(1);

		sign = 0;
		
		// ����ListView�е�Item�е�TextView

//		orderPhone.setText(good.getOrderPhone());
//		orderPhone.setText("13720422675");

		// ΪTextView���ü�����

		orderPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				sign = 1;

			}

		});

		TextView orderConsumeTime = (TextView) arg1.findViewById(R.id.orderConsumeTime);

//		orderConsumeTime.setText(good.getOrderConsumeTime());

  		TextView handMoney = (TextView) arg1.findViewById(R.id.handMoney);

//		handMoney.setText(good.getHandMoney());

		
		TextView orderAddress = (TextView) arg1.findViewById(R.id.orderAddress);

//		orderAddress.setText(good.getOrderAddress());

		return arg1;
	}

}
