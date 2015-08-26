package com.coolReader.bitmap_drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * bitmap��drawable֮����໥ת��
 * 
 * @author FengYi~ 2015��8��13��10:45:15
 */
public class ChangeInBitmap_Drawable {
	/**
	 * drawableͼƬת��ΪbitmapͼƬ
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if(drawable == null)
			return null;
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	/**
	 * bitmapתΪdrawable
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmap2Drawable(Bitmap bitmap){
		if(bitmap == null)
			return null;
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}
}
