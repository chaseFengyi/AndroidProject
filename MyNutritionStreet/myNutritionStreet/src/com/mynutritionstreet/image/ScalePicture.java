package com.mynutritionstreet.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * @author FengYi~
 *
 */
public class ScalePicture {

	/**
	 * ��������ʵ�inSampleSizeֵ
	 * @param options
	 * @param reqWidth  Ҫѹ������ͼƬ���
	 * @param reqHeight ��Ҫ��ͼƬ�߶�
	 * @return 
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,int reqHeight){
		//ԭͼƬ���
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if(height > reqHeight || width > reqWidth){
			// �����ʵ�ʿ�ߺ�Ŀ���ߵı���  
	        final int heightRatio = Math.round((float) height / (float) reqHeight);  
	        final int widthRatio = Math.round((float) width / (float) reqWidth);  
	        // ѡ���͸�����С�ı�����ΪinSampleSize��ֵ���������Ա�֤����ͼƬ�Ŀ�͸�  
	        // һ��������ڵ���Ŀ��Ŀ�͸ߡ�  
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;  
		}
		return inSampleSize;  
	}
	
	/**
	 * �õ�ѹ�����ͼƬ
	 * @param imageUrl 
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapByResource(Resources res, int resId, int reqWidth, int reqHeight){
		// ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С  
	    final BitmapFactory.Options options = new BitmapFactory.Options();  
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options); 
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    // ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ  
	    options.inJustDecodeBounds = false;  
	    return BitmapFactory.decodeResource(res, resId, options); 
	}
	
	public static Bitmap decodeSampledBitmap(Bitmap imageUrl, int reqWidth, int reqHeight){
		// ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С  
	    final BitmapFactory.Options options = new BitmapFactory.Options();  
	    options.inJustDecodeBounds = true;
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		Matrix matrix = new Matrix();
		matrix.postScale(1/options.inSampleSize, 1/options.inSampleSize);
		return Bitmap.createBitmap(imageUrl, 0, 0, imageUrl.getWidth(), imageUrl.getHeight(), matrix, true);
	}
}
