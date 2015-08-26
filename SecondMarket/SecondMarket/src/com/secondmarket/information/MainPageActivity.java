package com.secondmarket.information;

import java.util.ArrayList;
import java.util.List;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.secondmarket.publish.PublishActivity;

public class MainPageActivity extends Activity {
	private Button search;
	private Button bookAndNewspaper;
	private Button digital;
	private Button vehicle;
	private Button dailyUse;
	private Button peUser;
	private Button clothesUse;
	private Button freeGive;
	private Button other;
	private Button release;
	private Button mine2;
	private ListView listView;
	
	public static List<SortSearchDemo> list1 = new ArrayList<SortSearchDemo>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainpage);

		findView();
		onClick();
		setListView();
	}
	
	private void findView(){
		search = (Button)findViewById(R.id.ib_title_search);
		bookAndNewspaper = (Button)findViewById(R.id.bookAndNewspaper);
		digital = (Button)findViewById(R.id.digital);
		vehicle = (Button)findViewById(R.id.vehicle);
		dailyUse = (Button)findViewById(R.id.dailyUse);
		peUser = (Button)findViewById(R.id.peUser);
		clothesUse = (Button)findViewById(R.id.clothesUse);
		freeGive = (Button)findViewById(R.id.freeGive);
		other = (Button)findViewById(R.id.other);
		release = (Button)findViewById(R.id.release);
		mine2 = (Button)findViewById(R.id.mine2);
		listView = (ListView)findViewById(R.id.listView);
	}
	
	private void onClick(){
		bookAndNewspaper.setOnClickListener(listener);
		digital.setOnClickListener(listener);
		vehicle.setOnClickListener(listener);
		dailyUse.setOnClickListener(listener);
		peUser.setOnClickListener(listener);
		clothesUse.setOnClickListener(listener);
		freeGive.setOnClickListener(listener);
		other.setOnClickListener(listener);
		release.setOnClickListener(new PublishListener());
		mine2.setOnClickListener(new MineListener());
	}
	
android.view.View.OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			switch (arg0.getId()) {
			case R.id.bookAndNewspaper://如果点击的是书籍报刊
				intent.putExtra("name", getResources().getString(R.string.book));
				break;

			case R.id.digital://如果点击的是数码电子
				intent.putExtra("name", getResources().getString(R.string.digital));
				break;
				
			case R.id.vehicle://如果点击交通工具
				intent.putExtra("name", getResources().getString(R.string.transport));
				break;
				
			case R.id.dailyUse://如果点击生活用品
				intent.putExtra("name", getResources().getString(R.string.dailyuse));
				break;
				
			case R.id.peUser://如果点击体育用品
				intent.putExtra("name", getResources().getString(R.string.pe_use));
				break;
				
			case R.id.clothesUse://如果点击服装饰品
				intent.putExtra("name", getResources().getString(R.string.clothesuse));
				break;
				
			case R.id.freeGive://如果点击免费赠品
				intent.putExtra("name", getResources().getString(R.string.freegive));
				break;
				
			case R.id.other://如果点击其他
				intent.putExtra("name", getResources().getString(R.string.other));
				break;
			}
			
			intent.setClass(MainPageActivity.this, SortInformationActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	private void setListView(){
		if(HTTPRequest.isNetWorkEnable(MainPageActivity.this)){
			new Thread(new MyRunnable()).start();
		}else{
			Toast.makeText(MainPageActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
		}
	}
	
	//请求最新发布，获取最新发布信息
	public class MyRunnable extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("page", "1"));
			String string = HTTPRequest.getNewReleaseFromHttp(Address.NEWRELEASE, params);
  			System.out.println("mainString="+string);
			Message msg = handler.obtainMessage();
			msg.obj = JSONResolve.getSortInfoAfterJson(string);
			msg.what = 0x01;
			handler.sendMessage(msg);
		}

	}
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			
			if (msg.what == 0x01) {
				list1 = (List<SortSearchDemo>)msg.obj;

				int length = 0;
				if (list1.size() > 20) {
					length = 20;
				} else {
					length = list1.size();
				}
				MainPageAdapter adapter = new MainPageAdapter(MainPageActivity.this);
				listView.setAdapter(adapter);
			}
		}
	};
	
	private class MainPageAdapter extends BaseAdapter{
		private Context context;
		
		public MainPageAdapter(Context context) {
			this.context = context;
		}
		
		private void updateListView(List<SortSearchDemo> list){
			list1 = list;
			notifyDataSetChanged();
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
			final SortSearchDemo sortSearchDemo = (SortSearchDemo)list1.get(arg0);
			ViewHolder viewHolder = null;
				if(arg1 == null){
					viewHolder = new ViewHolder();
					arg1 = LayoutInflater.from(context).inflate(R.layout.simple_mainpage, null);
					viewHolder.imageView = (ImageView)arg1.findViewById(R.id.imageView);
					viewHolder.bookName = (TextView)arg1.findViewById(R.id.bookName);
					viewHolder.bookIntroduce = (TextView)arg1.findViewById(R.id.bookIntroduce);
					viewHolder.bookMoney = (TextView)arg1.findViewById(R.id.bookMoney);
					arg1.setTag(viewHolder);
				}else{
					viewHolder = (ViewHolder)arg1.getTag();
				}
				if(sortSearchDemo.getList().get(0).get("goodsPictureAD1") != null){
//					uri1 = list1.get(i).getList().get(0).get("goodsPictureAD1");
//http://b.hiphotos.baidu.com/movie/pic/item/50da81cb39dbb6fdbb7e3e020d24ab18972b37e9.jpg
//C:\\Users\\FengYi~\\Desktop\\mix\\photo\\bus.png		
//file:///C://Users//FengYi~//Desktop//mix//photo//bus.png
//path=   C:\Users\FengYi~\Desktop\mix\photo\bus.png	
					///http://www.cfanz.cn/?c=article&a=read&id=58189
					String path1 = sortSearchDemo.getList().get(0).get("goodsPictureAD1");
					System.out.println("path==="+path1);
					String path = changePictureLoad(path1);
					DisplayImageOptions options = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.ic_launcher)
						.cacheInMemory(true).cacheOnDisk(true)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
					ImageLoader.getInstance().displayImage("http://172.20.0.152:8080/XuptMarket/"+path1,viewHolder.imageView,options);
					
//					Bitmap bitmap = getLocalBitmap("file:///C://Users//FengYi~//Desktop//mix//photo//bus.png");
//					Bitmap bitmap = BitmapFactory.decodeFile("http://172.20.0.152:8080/XuptMarket/"+path1);
//					System.out.println("opopopath="+"http://172.20.0.152:8080/XuptMarket/"+path1);
//					System.out.println("bitmapbitmap="+bitmap);
//					viewHolder.imageView.setImageBitmap(bitmap);
				}else{
					viewHolder.imageView.setBackgroundResource(R.drawable.ic_launcher);
				}
				System.out.println("sortname==="+sortSearchDemo.getGoodsName());
				viewHolder.bookName.setText(sortSearchDemo.getGoodsName());
				viewHolder.bookIntroduce.setText(sortSearchDemo.getGoodsDescribe());
				viewHolder.bookMoney.setText("￥"+sortSearchDemo.getGoodsPrice());
			return arg1;
		}
		
	}
	
	//加载本地图片
	public static Bitmap getLocalBitmap(String url){
//		try {
			System.out.println(">>>>>");
//			FileInputStream fis = new FileInputStream(url);
			System.out.println("<<<<<<<");
//			System.out.println("fis==="+fis);
//			return BitmapFactory.decodeStream(fis);
			return BitmapFactory.decodeFile("C:\\Users\\FengYi~\\Desktop\\mix\\photo\\bus.png");
//		} catch (FileNotFoundException e) {S
			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
	}
	
	private String changePictureLoad(String oldPath){
		//file:///C://Users//FengYi~//Desktop//mix//photo//bus.png
		//path=   C:\Users\FengYi~\Desktop\mix\photo\bus.png
		System.out.println("sooooooo");
		String path="file:///";
		String newPath,subPath;
		System.out.println("90909090");
		subPath = oldPath.replace("\\", "//");
		newPath = path+subPath;
		System.out.println("newpathrrr="+newPath);
		return newPath;
	}
	
	private class ViewHolder{
		ImageView imageView;
		TextView bookName;
		TextView bookIntroduce;
		TextView bookMoney;
	}
	
	class MineListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainPageActivity.this, com.secondmarket.mine.MineActivity.class);
			startActivity(intent);
			finish();
		}
		
	}
	
	class PublishListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startActivity(new Intent(MainPageActivity.this, PublishActivity.class));
		}
		
	}
}
