package com.get;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sendlist.Logined;
import com.example.sendlist.R;
import com.publicInfo.BackInfo;
import com.request.HttpObtain;
import com.request.JsonResolve;

public class GetOrder extends Activity {
	public ArrayList<Map<String, Object>> list = null;
	public TextView getFood;
	private ListView listView;
	public List<GetOrderInfo> list1 = new ArrayList<GetOrderInfo>();
	private static MyAdapterInGet adapter;
	private static final String url = "http://121.42.8.50/CS1/customer/Shop_getShopPageByCus";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getorder);

		findView();
		click();
		setListView();
		listView.setOnItemClickListener(new ListViewListener());

	}

	private void findView() {
		getFood = (TextView) findViewById(R.id.getFood);
		listView = (ListView) findViewById(R.id.listView);
	}

	private void click() {
		getFood.setOnClickListener(new GetFoodListener());
	}

	class GetFoodListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(GetOrder.this, Logined.class);
			startActivity(intent);
			finish();
		}

	}

	private class MyRunnable extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			String jsonString = HttpObtain.getOrderFromHttp(url, params);

			Message msg = handler.obtainMessage();
			System.out.println("getOrdering:"+jsonString);
			if (jsonString.equals(BackInfo.RESPONSE_NO_SHOP)) {
				msg.what = 0x02;
			} else {
				msg.obj = JsonResolve.getOrderInfo(jsonString);
				System.out.println("getGETGET:"+JsonResolve.getOrderInfo(jsonString));
				msg.what = 0x01;
			}
			handler.sendMessage(msg);
		}

	}

	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x01:
				Toast.makeText(GetOrder.this, "成功获得商铺信息", Toast.LENGTH_SHORT)
				.show();
				list1 = (List<GetOrderInfo>) msg.obj;
				System.out.println("get:::"+list1);
				adapter = new MyAdapterInGet(getListData(), list1,
						R.layout.simple_item_get, GetOrder.this);
				listView.setAdapter(adapter);
				break;

			case 0x02:
				Toast.makeText(GetOrder.this, "没有获得商铺信息", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}

	};

	public void setListView() {
		new MyRunnable().start();
		listView.setOnItemClickListener(new ListViewListener());
	}

	/* ListView数据 */

	private List<ViewHolderOfGet> getListData() {
		int i = 0;
		List<ViewHolderOfGet> list = new ArrayList<ViewHolderOfGet>();
		while (i < list1.size()) {
			ViewHolderOfGet view = new ViewHolderOfGet();
			list.add(view);
			i = i + 1;
		}

		return list;

	}

	// 获得第position位置的内容
	private ViewHolderOfGet getContent(int number) {
		ViewHolderOfGet map = (ViewHolderOfGet) adapter.getItem(number);
		return map;
	}

	/*
	 * private void setListeners(){
	 * 
	 * listView.setOnItemClickListener(new OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
	 * arg2, long arg3) { // TODO Auto-generated method stub index = arg2; } });
	 * LinearLayout ll = (LinearLayout)listView.getAdapter().getItem(index);
	 * ll.findViewById(R.id.shopPhone).setOnClickListener(new OnClickListener()
	 * {
	 * 
	 * @Override public void onClick(View arg0) { // TODO Auto-generated method
	 * stub Toast.makeText(getApplicationContext(), "fa hwi3orfj",
	 * Toast.LENGTH_LONG).show(); } }); }
	 */

	class ListViewListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			ViewHolderOfGet map = getContent(arg2);
			int id = map.getShopId();
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("map", id);
			Intent intent = new Intent();
			intent.putExtra("map",id);
			intent.setClass(GetOrder.this, SeeDetailsOfGet.class);
			startActivity(intent);
			finish();

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(GetOrder.this, Logined.class);
			startActivity(intent);
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
