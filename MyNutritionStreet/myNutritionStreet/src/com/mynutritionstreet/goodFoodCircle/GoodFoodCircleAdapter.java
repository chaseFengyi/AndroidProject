package com.mynutritionstreet.goodFoodCircle;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mynutritionstreet.R;
import com.mynutritionstreet.Bean.FoodCircleBean;
import com.mynutritionstreet.pictureutil.ImageUtile;

public class GoodFoodCircleAdapter extends BaseAdapter {

	private Context context;
	private List<FoodCircleBean> list;

	public GoodFoodCircleAdapter(Context context, List<FoodCircleBean> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list.size() <= 0) {
			return list.size() + 1;
		} else
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		
		if(convertView == null){
			view = LayoutInflater.from(context).inflate(R.layout.sample_goodfoodcircle, null);
		}
		
		ImageView publisherPicture = (ImageView)view.findViewById(R.id.sample_publisherPicture_goodfood);
		TextView publisher = (TextView)view.findViewById(R.id.sample_publisher_goodfood);
		TextView time = (TextView)view.findViewById(R.id.sample_publishTime_goodfood);
		ImageView foodPicture = (ImageView)view.findViewById(R.id.sample_foodPicture_goodfood);
		
		/*
		 * String path1 = sortSearchDemo.getList().get(0).get("goodsPictureAD1");
					System.out.println("path==="+path1);
					String path = changePictureLoad(path1);
					DisplayImageOptions options = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.ic_launcher)
						.cacheInMemory(true).cacheOnDisk(true)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
					ImageLoader.getInstance().displayImage("http://172.20.0.152:8080/XuptMarket/"+path1,viewHolder.imageView,options);
		 * */
		
		Bitmap publisherBmp = ImageUtile.drawableToBitmap(context.getResources().getDrawable(R.drawable.person_picture));
		publisherBmp = ImageUtile.zoomBitmap(publisherBmp, 80, 80);
		publisherBmp = ImageUtile.getRoundedCornerBitmap(publisherBmp, 0.5f);
		publisherPicture.setImageBitmap(publisherBmp);
		if(list.get(position).getUser() == null || list.get(position).getUser().equals(null)){
			publisher.setText("²âÊÔ");
		}else{
			publisher.setText(list.get(position).getUser().getUserName());
		}
		time.setText(list.get(position).getPublishTime());
		
		Bitmap foodBmp = ImageUtile.drawableToBitmap(context.getResources().getDrawable(R.drawable.food_two));
		foodBmp = ImageUtile.zoomBitmap(foodBmp, 80, 80);
		foodBmp = ImageUtile.getRoundedCornerBitmap(foodBmp, 0.5f);
		foodPicture.setImageBitmap(publisherBmp);
		
		return view;
	}

}
