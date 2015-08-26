package com.send;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sendlist.Logined;
import com.example.sendlist.R;
import com.example.sendlist.UserInfo;
import com.publicInfo.BackInfo;
import com.request.HttpObtain;
import com.request.JsonResolve;

public class SendOrder extends Activity {
	private static List<SendOrderInfo> list1 = new ArrayList<SendOrderInfo>();
	private ListView listView;
	private Button send;
	private static MyAdapterInSend adapter;
	private static final String url = "http://121.42.8.50/CS1/customer/MyOrder_getOrderPageByCus";
	private static boolean sign = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendorder);

		findView();
		click();
		setListView();
		listView.setOnItemClickListener(new ListViewListener());
	}

	private void findView() {
		listView = (ListView) findViewById(R.id.listView);
		send = (Button) findViewById(R.id.send);
	}

	private void click() {
		send.setOnClickListener(new SendListener());
	}

	// 布局
	private void createWindow() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;

		// 设置listView
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = height * 3 / 5;
		listView.setLayoutParams(params);
	}

	class SendListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(SendOrder.this, Logined.class);
			startActivity(intent);
			finish();
		}

	}

	public class MyRunnable extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			String jsonString = HttpObtain.sendOrderFromHttp(url, params);
			System.out.println("send:" + jsonString);
			System.out.println("sending:"
					+ JsonResolve.sendOrderInfo(jsonString).size());
			Message msg = handler.obtainMessage();
			if (jsonString.equals(BackInfo.RESPONSE_NO_ORDER)) {
				msg.what = 0x02;
			} else {
				msg.obj = JsonResolve.sendOrderInfo(jsonString);
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
				sign = true;
				list1 = (List<SendOrderInfo>) msg.obj;
				System.out.println("sendinging:::" + list1.size());
				System.out.println("iiiiiii---->" + list1);
				/*
				 * Toast.makeText(SendOrder.this, "成功获得商家信息",
				 * Toast.LENGTH_SHORT) .show();
				 */
				adapter = new MyAdapterInSend(getListData(), list1,
						R.layout.simple_item_send, SendOrder.this);
				listView.setAdapter(adapter);
				break;

			case 0x02:
				sign = false;
				Toast.makeText(SendOrder.this, "没有商家", Toast.LENGTH_SHORT)
						.show();
			}
		}

	};

	public void setListView() {
		new MyRunnable().start();
		listView.setOnItemClickListener(new ListViewListener());
	}

	/* ListView数据 */

	private List<ViewHolderOfSend> getListData() {
		int i = 0;
		List<ViewHolderOfSend> list = new ArrayList<ViewHolderOfSend>();
		while (i < list1.size()) {
			ViewHolderOfSend view = new ViewHolderOfSend();
			list.add(view);
			i = i + 1;
		}

		return list;

	}

	// 获得第position位置的内容
	private ViewHolderOfSend getContent(int number) {
		ViewHolderOfSend map = (ViewHolderOfSend) adapter.getItem(number);
		return map;
	}

	// 为listView绑定事件监听
	class ListViewListener implements OnItemClickListener {

		// arg2代表点击位置
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			ViewHolderOfSend map = getContent(arg2);
			int id = map.getId();
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("map", id);
			Intent intent = new Intent();
			intent.putExtra("map", id);
			intent.setClass(SendOrder.this, SeeDetailsOfSend.class);
			startActivity(intent);
			finish();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(SendOrder.this, Logined.class);
			startActivity(intent);
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
