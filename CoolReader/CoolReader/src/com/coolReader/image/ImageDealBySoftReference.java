package com.coolReader.image;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

/**
 * 通过软引用实现图片缓存
 * @author FengYi~
 *2015年8月13日12:11:33
 */
public class ImageDealBySoftReference {
	private Map<String, SoftReference<Bitmap>> imageMap = new HashMap<String, SoftReference<Bitmap>>();
	private static ImageDealBySoftReference mInstance = null;
	
	private ImageDealBySoftReference(){
		
	}
	
	public static synchronized ImageDealBySoftReference getInstance(){
		if(mInstance == null)
			mInstance = new ImageDealBySoftReference();
		return mInstance;
	}
	
	/**
	 * 将图片放入缓存中
	 * @param imageUrl
	 * @param imageCallBack
	 * @return
	 */
	public Bitmap loadBitmap(final String imageUrl,final ImageCallBack imageCallBack){
		SoftReference<Bitmap> reference = imageMap.get(imageUrl);
		if(reference != null){
			if(reference.get() != null){
				return reference.get();
			}
		}
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				//加入到缓存中
				Bitmap bitmap = (Bitmap)msg.obj;
				imageMap.put(imageUrl, new SoftReference<Bitmap>(bitmap));
				if(imageCallBack != null){
					imageCallBack.getBitmap(bitmap);
				}
			}
			
		};
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = handler.obtainMessage();
				message.obj = downloadBitmap(imageUrl);
				handler.sendMessage(message);
				
			}
		}).start();
		
		return null;
	}
	
	/**
	 * 从网上下载图片
	 * @param imageUrl
	 * @return
	 */
	public Bitmap downloadBitmap(String imageUrl){
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public interface ImageCallBack{
		void getBitmap(Bitmap bitmap);
	}
}
