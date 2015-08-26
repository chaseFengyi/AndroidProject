package com.send;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sendlist.R;
import com.example.sendlist.UserInfo;
import com.publicInfo.BackInfo;
import com.request.HttpObtain;
import com.request.JsonResolve;

public class SeeDetailsOfSend extends Activity {
	public ArrayList<Map<String, Object>> list = null;
	private ListView listView;
	public static int ORDERID = 0;
	private ViewHolderOfSend map = null;
	private Button exit;
	private static final String url = "http://121.42.8.50/CS1/customer/OrderItem_getOrderItemPageByCus";
	private static List<AfterSendInfo> list1 = new ArrayList<AfterSendInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seedetails_get);

		Intent intent = getIntent();
		// map = (ViewHolderOfSend) intent.getSerializableExtra("map");
		// ORDERID = (Integer) map.getId();
		ORDERID = (Integer) intent.getSerializableExtra("map");

		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(new ExitListener());
		listView = (ListView) findViewById(R.id.listView);
		setListView();
	}

	public class MyRunnable extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			String jsonString = HttpObtain.afterSendOrderFromHttp(url, params);

			Message msg = Message.obtain();
			if (jsonString.equals(BackInfo.RESPONSE_ACTION_ERROR)) {
				msg.what = 0x02;
			} else if (jsonString.equals(BackInfo.RESPONSE_NO_ORDER)) {
				msg.what = 0x03;
			} else {
				msg.obj = JsonResolve.AfterSendOrderInfo(jsonString);
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
				list1 = (List<AfterSendInfo>) msg.obj;
				int i = 0;
				list = new ArrayList<Map<String, Object>>();
				while (i < list1.size()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("goodsName", "商品名：" + list1.get(i).getGoodsName());
					map.put("goodsPrice", "单价：" + list1.get(i).getGoodsPrice()
							+ "元");
					map.put("goodsCount", "数量" + list1.get(i).getGoodsCount());
					map.put("goodsMore", "备注：" + list1.get(i).getGoodsMore());
					list.add(map);
					i = i + 1;
				}
				SimpleAdapter adapter = new SimpleAdapter(
						SeeDetailsOfSend.this, list,
						R.layout.simple_seedetail_get, new String[] {
								"goodsName", "goodsPrice", "goodsCount",
								"goodsMore" }, new int[] { R.id.goodsName,
								R.id.goodsPrice, R.id.goodsCount,
								R.id.goodsMore });
				listView.setAdapter(adapter);
				break;

			case 0x02:
				Toast.makeText(SeeDetailsOfSend.this, "校验失败",
						Toast.LENGTH_SHORT).show();
				break;

			case 0x03:
				Toast.makeText(SeeDetailsOfSend.this, "没有订单",
						Toast.LENGTH_SHORT).show();
			}
		}

	};

	// 填充listView
	public void setListView() {
		new MyRunnable().start();
	}

	class ExitListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("map", "");
			intent.setClass(SeeDetailsOfSend.this, SendOrder.class);
			startActivity(intent);
			finish();
		}

	}

	// 捕获按下键盘返回按钮13
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(SeeDetailsOfSend.this, SendOrder.class);
			startActivity(intent);
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
