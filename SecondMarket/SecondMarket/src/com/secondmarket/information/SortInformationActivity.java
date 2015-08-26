package com.secondmarket.information;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.Request.HTTPRequest;
import com.Request.JSONResolve;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.publicInfo.Address;
import com.secondmarket.bean.SortSearchDemo;
import com.secondmarket.load.R;

public class SortInformationActivity extends Activity {
	private Button sortInformation;
	private TextView sortName;
	private ListView listView;

	private static String name = "";
	private static List<com.secondmarket.bean.SortSearchDemo> list1 = new ArrayList<SortSearchDemo>();
	private static String goodsTypeId;
	private static String page;
	ArrayList<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sortinformation);

		findView();
		click();
		setTextView();
		setListView();
		listView.setOnItemClickListener(new ListViewListener());
	}

	private void findView() {
		sortInformation = (Button) findViewById(R.id.sortInformation);
		sortName = (TextView) findViewById(R.id.sortName);
		listView = (ListView) findViewById(R.id.listView);
	}

	private void click() {
		sortInformation.setOnClickListener(new SortInformationListener());
	}

	class SortInformationListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(SortInformationActivity.this,
					MainPageActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void setTextView() {
		Intent intent = getIntent();
		String string = intent.getStringExtra("name");

		if (string.equals(getResources().getString(R.string.book))) {
			sortName.setText(getResources().getString(R.string.book));
			System.out.println("nameString:" + string);
			name = string;
		} else if (string.equals(getResources().getString(R.string.digital))) {
			sortName.setText(getResources().getString(R.string.digital));
			name = string;
		} else if (string.equals(getResources().getString(R.string.transport))) {
			sortName.setText(getResources().getString(R.string.transport));
			name = string;
		} else if (string.equals(getResources().getString(R.string.dailyuse))) {
			sortName.setText(getResources().getString(R.string.dailyuse));
			name = string;
		} else if (string.equals(getResources().getString(R.string.pe_use))) {
			sortName.setText(getResources().getString(R.string.pe_use));
			name = string;
		} else if (string.equals(getResources().getString(R.string.clothesuse))) {
			sortName.setText(getResources().getString(R.string.clothesuse));
			name = string;
		} else if (string.equals(getResources().getString(R.string.freegive))) {
			sortName.setText(getResources().getString(R.string.freegive));
			name = string;
		} else if (string.equals(getResources().getString(R.string.other))) {
			sortName.setText(getResources().getString(R.string.other));
			name = string;
		}
	}

	// 根据分类搜索，请求对应分类信息
	public class MyRunnable extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("goodsTypeId", goodsTypeId));
			params.add(new BasicNameValuePair("page", page));
			System.out.println("goodsTypeId=" + goodsTypeId);
			String string = HTTPRequest.getSortSearchFromHttp(
					Address.SORTSEARCH_ADDRESS, params);
			Message msg = handler.obtainMessage();
			System.out.println("stringstringing--->" + string);
			msg.obj = JSONResolve.getSortInfoAfterJson(string);
			System.out.println("OBJ:"
					+ JSONResolve.getSortInfoAfterJson(string));
			msg.what = 0x01;
			handler.sendMessage(msg);
		}

	}

	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			list = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map;

			if (msg.what == 0x01) {
				list1 = (List<SortSearchDemo>) msg.obj;
				if (list1.size() == 0) {
					Toast.makeText(SortInformationActivity.this, "暂无该商品信息",
							Toast.LENGTH_SHORT).show();
				} else {

					int length = list1.size();
					SortInfoAdapter adapter = new SortInfoAdapter(
							SortInformationActivity.this);
					listView.setAdapter(adapter);
				}
			}
		}
	};

	class ListViewListener implements OnItemClickListener {
		// 第position项被单击时激发该方法
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// Map<String, Object> map = getContent(arg2);
			SortSearchDemo sortSearchDemo = getContent(arg2);
			System.out.println("mapSort=" + sortSearchDemo);
			Intent intent = new Intent();
			intent.setClass(SortInformationActivity.this,
					SortDetailsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("sortSearchDemo",
					(Serializable) sortSearchDemo);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	private void setListView() {
		if (name.equals(getResources().getString(R.string.book))) {
			goodsTypeId = "1";
			page = "1";
			new MyRunnable().start();
		} else if (name.equals(getResources().getString(R.string.digital))) {
			goodsTypeId = "2";
			page = "1";
			new MyRunnable().start();
		} else if (name.equals(getResources().getString(R.string.transport))) {
			goodsTypeId = "3";
			page = "1";
			new MyRunnable().start();
		} else if (name.equals(getResources().getString(R.string.dailyuse))) {
			goodsTypeId = "4";
			page = "1";
			new MyRunnable().start();
		} else if (name.equals(getResources().getString(R.string.pe_use))) {
			goodsTypeId = "5";
			page = "1";
			new MyRunnable().start();
		} else if (name.equals(getResources().getString(R.string.clothesuse))) {
			goodsTypeId = "6";
			page = "1";
			new MyRunnable().start();
		} else if (name.equals(getResources().getString(R.string.freegive))) {
			goodsTypeId = "7";
			page = "1";
			new MyRunnable().start();
		} else if (name.equals(getResources().getString(R.string.other))) {
			goodsTypeId = "8";
			page = "1";
			new MyRunnable().start();
		}

	}

	// 获得第position位置的内容
	@SuppressWarnings("unchecked")
	private SortSearchDemo getContent(int number) {
		/*
		 * HashMap<String, Object> map = (HashMap<String, Object>) listView
		 * .getItemAtPosition(number); return map;
		 */
		SortSearchDemo searchDemo = (SortSearchDemo) listView
				.getItemAtPosition(number);
		return searchDemo;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(SortInformationActivity.this,
					MainPageActivity.class);
			startActivity(intent);
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	private class SortInfoAdapter extends BaseAdapter {
		private Context context;

		public SortInfoAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list1.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list1.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			final SortSearchDemo sortSearchDemo = (SortSearchDemo) list1
					.get(arg0);
			ViewHolder viewHolder = null;
			if (arg1 == null) {
				viewHolder = new ViewHolder();
				arg1 = LayoutInflater.from(context).inflate(
						R.layout.simple_sortinformation, null);
				viewHolder.imageView = (ImageView) arg1
						.findViewById(R.id.imageView);
				viewHolder.bookName = (TextView) arg1
						.findViewById(R.id.bookName);
				viewHolder.bookIntroduce = (TextView) arg1
						.findViewById(R.id.bookIntroduce);
				viewHolder.bookMoney = (TextView) arg1
						.findViewById(R.id.bookMoney);
				arg1.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) arg1.getTag();
			}
			if (sortSearchDemo.getList().get(0).get("goodsPictureAD1") != null) {
				// uri1 = list1.get(i).getList().get(0).get("goodsPictureAD1");
				// http://b.hiphotos.baidu.com/movie/pic/item/50da81cb39dbb6fdbb7e3e020d24ab18972b37e9.jpg
				// C:\\Users\\FengYi~\\Desktop\\mix\\photo\\bus.png
				// file:///C://Users//FengYi~//Desktop//mix//photo//bus.png
				// path= C:\Users\FengYi~\Desktop\mix\photo\bus.png
				// /http://www.cfanz.cn/?c=article&a=read&id=58189
				String path1 = sortSearchDemo.getList().get(0)
						.get("goodsPictureAD1");
				System.out.println("path===" + path1);
				// String path = changePictureLoad(path1);
				DisplayImageOptions options = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.ic_launcher)
						.cacheInMemory(true).cacheOnDisk(true)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
				ImageLoader.getInstance().displayImage(
						"http://192.168.1.112:8080/XuptMarket/" + path1,
						viewHolder.imageView, options);
				viewHolder.imageView
						.setBackgroundResource(R.drawable.ic_launcher);
			}
			System.out.println("sortname===" + sortSearchDemo.getGoodsName());
			viewHolder.bookName.setText(sortSearchDemo.getGoodsName());
			viewHolder.bookIntroduce.setText(sortSearchDemo.getGoodsDescribe());
			viewHolder.bookMoney.setText("￥" + sortSearchDemo.getGoodsPrice());
			return arg1;
		}

		private class ViewHolder {
			ImageView imageView;
			TextView bookName;
			TextView bookIntroduce;
			TextView bookMoney;
		}

	}

}
