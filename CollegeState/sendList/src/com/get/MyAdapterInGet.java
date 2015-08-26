package com.get;

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
import com.send.MyAdapterOfSend;

public class MyAdapterInGet extends BaseAdapter {
	private List<ViewHolderOfGet> goods;// ListView��ʾ������
	private List<GetOrderInfo> list;// ListView��ʾ������

	private int resource;// ��ʾ�б����Layout

	private LayoutInflater inflater;// ����������

	private Context context;

	private List<Map<String, Object>> mDate;

	public MyAdapterInGet(List<ViewHolderOfGet> goods, List<GetOrderInfo> list,
			int resource, Context context) {
		this.goods = goods;

		this.list = list;

		this.resource = resource;

		this.context = context;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	private void init() {
		mDate = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < goods.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("shopId", goods.get(i).getShopId());
			map.put("shopPhone", goods.get(i).getShopPhone());
			map.put("shopName", "�̼ң�  " + goods.get(i).getShopName());
			map.put("takeMoney", "֧����   " + goods.get(i).getTakeMoney() + "Ԫ");
			map.put("shopAddress", goods.get(i).getShopAddress());
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
		arg1 = inflater.inflate(resource, null);
		
		goods.get(arg0).setShopPhone((TextView)arg1.findViewById(R.id.shopPhone));
		goods.get(arg0).setShopName((TextView)arg1.findViewById(R.id.shopName));
		goods.get(arg0).setTakeMoney((TextView)arg1.findViewById(R.id.takemony));
		goods.get(arg0).setShopAddress((TextView)arg1.findViewById(R.id.shopAddress));
		
		goods.get(arg0).setShopId(list.get(arg0).getShopId());
		goods.get(arg0).getShopPhone().setText(list.get(arg0).getShopPhone().toString());
		goods.get(arg0).getShopName().setText("�̼ң�  " + list.get(arg0).getShopName().toString());
		goods.get(arg0).getTakeMoney().setText("֧����   " + list.get(arg0).getTakeMoney()+"Ԫ");
		goods.get(arg0).getShopAddress().setText(list.get(arg0).getShopAddress().toString());		
		
		final String phone = list.get(arg0).getShopPhone();
		goods.get(arg0).getShopPhone().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = 
						new AlertDialog.Builder(context)
						.setTitle("��ʾ")
						.setMessage("ȷ������ "+phone+" ����绰��")
						.setPositiveButton("ȷ��", 
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
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
						.setNegativeButton("ȡ��", 
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
		
		return arg1;
	}

}
