package com.get;

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

public class SeeDetailsOfGet extends Activity {
	private ListView listView;
	public ArrayList<Map<String, Object>> list = null;
	public static int SHOPID = 0;

	private ViewHolderOfGet map = null;
	private Button exit;
	private static final String url = "http://121.42.8.50/CS1/customer/OrderItem_getOrderItemPageByCus";
	private static List<AfterGetInfo> list1 = new ArrayList<AfterGetInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seedetails_get);

		Intent intent = getIntent();
		// map = (ViewHolderOfGet) intent.getSerializableExtra("map");
		// SHOPID = (Integer) map.getShopId();
		SHOPID = (Integer) intent.getSerializableExtra("map");

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
			String jsonString = HttpObtain.afterGetOrderFromHttp(url, params);

			Message msg = Message.obtain();
			if (jsonString.equals(BackInfo.RESPONSE_NO_ORDER)) {
				msg.what = 0x02;
			} else {
				msg.obj = JsonResolve.afterGetOrderInfo(jsonString);
				msg.what = 0x01;
			}
			handler.sendMessage(msg);
		}

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x01:
				Toast.makeText(SeeDetailsOfGet.this, "成功获取商品信息",
						Toast.LENGTH_SHORT).show();
				list1 = (List<AfterGetInfo>) msg.obj;
				int i = 0;
				list = new ArrayList<Map<String, Object>>();
				while (i < list1.size()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("goodsName", "商品名:" + list1.get(i).getGoodsName());
					map.put("goodsPrice", "单价：" + list1.get(i).getGoodsPrice()
							+ "元" + "\t");
					map.put("goodsCount", "数量:" + list1.get(i).getGoodsCount());
					map.put("goodsMore", "备注：" + list1.get(i).getGoodsMore());
					list.add(map);
					i = i + 1;
					SimpleAdapter adapter = new SimpleAdapter(
							SeeDetailsOfGet.this, list,
							R.layout.simple_seedetail_get, new String[] {
									"goodsName", "goodsPrice", "goodsCount",
									"goodsMore" }, new int[] { R.id.goodsName,
									R.id.goodsPrice, R.id.goodsCount,
									R.id.goodsMore });
					listView.setAdapter(adapter);
				}
				break;
			case 0x02:
				Toast.makeText(SeeDetailsOfGet.this, "没有商品", Toast.LENGTH_SHORT)
						.show();
				break;
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
			intent.setClass(SeeDetailsOfGet.this, GetOrder.class);
			startActivity(intent);
			finish();
		}

	}

	// 捕获按下键盘返回按钮
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(SeeDetailsOfGet.this, GetOrder.class);
			startActivity(intent);
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
