package com.coolReader.image;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * ͨ��LruCacheʵ��ͼƬ����
 * @author FengYi~
 *2015��8��13��12:33:49
 */
public class ImageDealByLruCache {
	private LruCache<String, Bitmap> mMemoryCache;
	//��ȡ�ֻ��ڴ�
	private int MAXMEMORY = (int)(Runtime.getRuntime().maxMemory() / 1024);
	private static ImageDealByLruCache mInstance = null;
	 
	private ImageDealByLruCache() {
		// TODO Auto-generated constructor stub
		if(mMemoryCache == null){
			//����Ϊ�ֻ��ڴ��1/8
			mMemoryCache = new LruCache<String, Bitmap>(MAXMEMORY / 8){
				//����ͼƬ����
				@Override
				protected int sizeOf(String key, Bitmap value) {
					// TODO Auto-generated method stub
					return value.getRowBytes() * value.getHeight() / 1024;
				}
				
				@Override
				protected void entryRemoved(boolean evicted, String key,
						Bitmap oldValue, Bitmap newValue) {
					// TODO Auto-generated method stub
					super.entryRemoved(evicted, key, oldValue, newValue);
					Log.v("tag", "hard cache is full , push to soft cache");
				}
			};
		}
	}
	
	public static synchronized ImageDealByLruCache getInstance(){
		if(mInstance == null){
			mInstance = new ImageDealByLruCache();
		}
		return mInstance;
	}
	
	/**
	 * ��ջ���
	 */
	public void clearCache(){
		if(mMemoryCache != null){
			if(mMemoryCache.size() > 0){
				Log.d("CacheUtils",
                        "mMemoryCache.size() " + mMemoryCache.size());
				mMemoryCache.evictAll();
				Log.d("CacheUtils", "mMemoryCache.size()" + mMemoryCache.size());
			}
			mMemoryCache = null;
		}
	}
	
	/**
	 * ���ͼƬ������
	 * @param key
	 * @param bitmap
	 */
	public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap){
		if(mMemoryCache.get(key) == null){
			if(key != null && bitmap != null){
				mMemoryCache.put(key, bitmap);
			}
		}else{
			Log.w("tag", "the res is aready exits");
		}
	}
	
	/**
	 * �ӻ���ȡͼƬ
	 * @param key
	 * @return
	 */
	public synchronized Bitmap getBitmapFromMemCache(String key){
		Bitmap bitmap = mMemoryCache.get(key);
		if(key != null){
			return bitmap;
		}
		return null;
	}
	
	/**
	 * 
	 * �Ƴ�����
	 * @param key
	 */
	public synchronized void removeImageCache(String key){
		if(key != null){
			if(mMemoryCache != null){
				Bitmap bitmap = mMemoryCache.remove(key);
				if(bitmap != null)
					bitmap.recycle();
			}
		}
	}
}
