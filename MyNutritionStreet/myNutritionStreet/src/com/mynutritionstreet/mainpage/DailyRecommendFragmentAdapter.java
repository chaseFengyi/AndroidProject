package com.mynutritionstreet.mainpage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mynutritionstreet.R;
import com.mynutritionstreet.Bean.GoodInfoBean;
import com.mynutritionstreet.image.ImageDealByLruCache;
import com.mynutritionstreet.image.ImageDealBySoftReference;
import com.mynutritionstreet.image.ScalePicture;
import com.mynutritionstreet.image.UploadDataAdnPhoto;
import com.mynutritionstreet.util.StrinAndDate;
import com.mynutritionstreet.util.UrlUtils;

public class DailyRecommendFragmentAdapter extends BaseAdapter {
	private Context context;
	private List<GoodInfoBean> list;
	private ImageView imageView;

	public DailyRecommendFragmentAdapter(Context context,
			List<GoodInfoBean> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// 给控件设置属性
	private TextView setWidget(TextView view) {
		view.setTextSize(22);
		view.setBackground(null);
		view.setTextColor(Color.BLACK);
		return view;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.subsample_daily_fragment, null);
		}
		imageView = (ImageView) view
				.findViewById(R.id.goodsPhoto_dailyfragment);
//		Bitmap bitmap = ImageUtile.drawableToBitmap((Drawable) (list
//				.get(position).get("icon")));
//		bitmap = ImageUtile.zoomBitmap(bitmap, 120, 130);
//		bitmap = ImageUtile.getRoundedCornerBitmap(bitmap, 20);
//		imageView.setImageBitmap(bitmap);

		final String source = list.get(position).getGoodsPhoto();
		Log.i("source", source);
		if(source == null || source.equals(null) || source.equals("null")){//如果没有图片信息，则显示默认图片
			Log.i("source---------", source);
//			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.foodpic);
//			bitmap = ScalePicture.decodeSampledBitmap(bitmap, 60, 40);
			Bitmap bitmap = ScalePicture.decodeSampledBitmapByResource(context.getResources(), R.drawable.foodpic, 60, 40);
			imageView.setImageBitmap(bitmap);
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// 利用LruCache进行处理
					// 实例化对象
					ImageDealByLruCache byLruCache = ImageDealByLruCache
							.getInstance();
					// 从缓存中取图片
					Bitmap bitmap = byLruCache.getBitmapFromMemCache(source);
					if (bitmap == null) {// 缓存中没有图片，进行网络加载图片
//						ImageDealBySoftReference imageDealBySoftReference = ImageDealBySoftReference
//								.getInstance();
//						bitmap = imageDealBySoftReference
//								.downloadBitmap(UrlUtils.PICTURE_DOWNLOAD+"/"+source);
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("inputPath", source));
						bitmap = UploadDataAdnPhoto.loadImage(UrlUtils.PICTURE_DOWNLOAD, params);
						bitmap = ScalePicture.decodeSampledBitmap(bitmap, 120, 90);
						//将图片添加到缓存
						byLruCache.addBitmapToMemoryCache(source, bitmap);
					}
					
					Message message = handler.obtainMessage();
					message.what = 0x01;
					message.obj = bitmap;
					handler.sendMessage(message);
				}
			}).start();
		}
		
		TextView goodsName = (TextView) view
				.findViewById(R.id.goodsName_dailyfragment);
		TextView publishTime = (TextView) view
				.findViewById(R.id.publishTime_dailyfragment);
		goodsName = setWidget(goodsName);
		goodsName.setText(list.get(position).getGoodsName());
		publishTime = setWidget(publishTime);
		publishTime.setText(list.get(position).getPublishTime());

		return view;
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x01){
				imageView.setImageBitmap((Bitmap)msg.obj);
			}
		};
	};

}
