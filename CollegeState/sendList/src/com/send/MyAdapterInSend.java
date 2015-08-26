package com.send;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sendlist.R;

public class MyAdapterInSend extends BaseAdapter {
	private List<ViewHolderOfSend> goods;// ListView显示的数据
	private List<SendOrderInfo> list;// ListView显示的数据

	private int resource;// 显示列表项的Layout

	private LayoutInflater inflater;// 界面生成器

	private Context context;
	
	private List<Map<String, Object>> mDate;
	
	public MyAdapterInSend(List<ViewHolderOfSend> goods,List<SendOrderInfo> list, int resource,
			Context context) {
		this.goods = goods;
		
		this.list = list;

		this.resource = resource;

		this.context = context;
		
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	

	private void init(){
		mDate = new ArrayList<Map<String,Object>>();
		for(int i=0; i<list.size(); i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderId", list.get(i).getOrderId());
			map.put("orderPhone", goods.get(i).getOrderPhone());
			map.put("orderConsumeTime", goods.get(i).getOrderConsumeTime());
			map.put("handMoney", goods.get(i).getHandMoney() + "元");
			map.put("orderAddress", goods.get(i).getOrderAddress());
			mDate.add(map);
		}
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
		if(arg1 == null){
			arg1 = inflater.inflate(resource, null);
		}
		
		goods.get(arg0).setOrderPhone((TextView)arg1.findViewById(R.id.orderPhone));
		goods.get(arg0).setOrderConsumeTime((TextView)arg1.findViewById(R.id.orderConsumeTime));
		goods.get(arg0).setHandMoney((TextView)arg1.findViewById(R.id.handMoney));
		goods.get(arg0).setOrderAddress((TextView)arg1.findViewById(R.id.orderAddress));
		
		goods.get(arg0).setId(list.get(arg0).getOrderId());
		goods.get(arg0).getOrderPhone().setText(list.get(arg0).getOrderPhone().toString());
		goods.get(arg0).getOrderConsumeTime().setText(list.get(arg0).getOrderConsumeTime().toString());
		goods.get(arg0).getHandMoney().setText(list.get(arg0).getHandMoney()+"");
		goods.get(arg0).getOrderAddress().setText(list.get(arg0).getOrderAddress().toString());		
		
		final String phone = list.get(arg0).getOrderPhone();
		goods.get(arg0).getOrderPhone().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = 
						new AlertDialog.Builder(context)
				.setTitle("提示")
				.setMessage("确定拨打 13720422675 这个电话吗？")
				.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0,
									int arg1) {
								// TODO Auto-generated method stub
								arg0.dismiss();
								if (!phone.equals("")) {
									System.out.println("phone"
											+ MyAdapterOfSend.phone);
									Intent intent = new Intent(
											"android.intent.action.CALL",
											Uri.parse("tel: "
													+ "13720422675"));
									context.startActivity(intent);
								}
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0,
									int arg1) {
								// TODO Auto-generated method stub
								arg0.dismiss();
							}
						});
		builder.create().show();
			}
		});
		
		return arg1;
	}

}
