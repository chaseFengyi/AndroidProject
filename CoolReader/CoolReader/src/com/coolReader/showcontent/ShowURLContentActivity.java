package com.coolReader.showcontent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract.Constants;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.coolReader.R;
import com.coolReader.Bean.WebContentBean;
import com.coolReader.Util.ActionBarUtil;
import com.coolReader.bitmap_drawable.ChangeInBitmap_Drawable;
import com.coolReader.image.ImageDealByLruCache;
import com.coolReader.image.ImageDealBySoftReference;
import com.coolReader.mainPage.MainPageActivity;

/**
 * 显示URL的详细信息
 * 
 * @author FengYi~ 2015年8月12日11:34:07
 */
public class ShowURLContentActivity extends Activity {
	private TextView tv_title;
	private TextView tv_show;
	private WebContentBean bean;
	private Map<String, SoftReference<Drawable>> imageMap = new HashMap<String, SoftReference<Drawable>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_url_content);
		ActionBarUtil.centerActionBar(this, this, getActionBar(), "详细信息");

		tv_title = (TextView) findViewById(R.id.text_title);
		tv_show = (TextView) findViewById(R.id.text_content);
		// 设置内容过长可滑动显示
		tv_show.setMovementMethod(ScrollingMovementMethod.getInstance());
		getContent();
		showInUI();
	}

	/**
	 * 获取前一页的点击项的信息
	 */
	private void getContent() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String whichActivity = (String)bundle.getString("key");
		bean = (WebContentBean) bundle.getSerializable(MainPageActivity.TAG);
	}

	/**
	 * 在UI上进行展示信息
	 */
	private void showInUI() {
		if (bean != null) {
			String content = bean.getWebContent();
			String title = bean.getWebTitle();

			// 以富文本的形式显示
			tv_title.setText(Html.fromHtml(title) + "\n");
//			tv_show.setText(content);
//			 showStandardContent(content);
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					StringBuffer buffer = showStandardContent(content);
//					setTexts(buffer.toString());
//					Message message = handler.obtainMessage();
//					message.what = 0x01;
////					message.obj = buffer;
//					handler.sendMessage(message);
//				}
//			}).start();
			StringBuffer buffer = showStandardContent(content);
			final String html = buffer.toString();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ImageGetter getter = new ImageGetter() {
						
						@Override
						public Drawable getDrawable(String source) {
							// TODO Auto-generated method stub
							Drawable drawable = null;
							// 利用LruCache进行处理
							// 实例化对象
							ImageDealByLruCache byLruCache = ImageDealByLruCache
									.getInstance();
							// 从缓存中取图片
							Bitmap bitmap = byLruCache.getBitmapFromMemCache(source);
							if (bitmap == null) {// 缓存中没有图片，进行网络加载图片
								ImageDealBySoftReference imageDealBySoftReference = ImageDealBySoftReference
										.getInstance();
								bitmap = imageDealBySoftReference
										.downloadBitmap(source);
							}
							drawable = ChangeInBitmap_Drawable.bitmap2Drawable(bitmap);
							
							drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
							
							return drawable;
						}
					};
					
					CharSequence charSequence = Html.fromHtml(html, getter, null);
					Message message = handler.obtainMessage();
					message.what = 0x01;
					message.obj = charSequence;
					handler.sendMessage(message);
				}
			}).start();
		}
	}
	

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x01) {
				tv_show.setText((CharSequence)msg.obj);
			}

		};
	};

	
	/**
	 * 分别将里面的 文本图片等内容进行分割显示
	 * 
	 * @param content
	 */
	public StringBuffer showStandardContent(String content) {
		// 去掉里面的title内容之前
		String str_beforeQuitTitle[] = content.split("\\[content\\]:");
		System.out.println("content="+content);
		System.out.println("before===="+str_beforeQuitTitle.length);
		System.out.println("before====before"+str_beforeQuitTitle[0]);
		// 将title内容去掉之后
		String str_afterQuitTitle = str_beforeQuitTitle[1];
		// 将字符串转为输入流，一行一行进行读取
		Reader stringReader = new StringReader(str_afterQuitTitle);
		BufferedReader bufferedReader = new BufferedReader(stringReader);
		StringBuffer stringBuffer_text = new StringBuffer();// 存储文本
		// 通知此流是否已准备好被读取
		try {
			String str;
			while (true) {
				str = bufferedReader.readLine();
				if (str == null)
					break;
				if (str.contains("[img]:")) {// 如果是[img]开头，则显示的是图片流，进行截取
					StringBuffer stringBuffer_img = new StringBuffer();// 存储图片字符
					String string_format = null;// 存储文本格式
					stringBuffer_img.append(str.substring(6, str.length()));
					// 一次向后读，知道读到[format]字段
					while ((str = bufferedReader.readLine()) != null) {
						if (!str.contains("[format]:")) {
							stringBuffer_img.append(str);
						} else {// 遇到[format]字段
							if (str.length() == 8) {
								string_format = "jpg";
							} else {
								string_format = str.substring(9,
										str.length() - 1);
							}
							break;
						}
					}
					// 读取到图片信息了，将图片进行解码
					// String imag =
					// TCPRequest.decode(stringBuffer_img.toString());
					// 显示成final String sText2 =
					// "测试图片信息：<img src=\"/mnt/sdcard/temp/1.jpg\" />"的格式
					// String img_src =
					// "<img src=\"data:image/"+string_format+";base64,"+stringBuffer_img.toString()+"\"/>";
					String img_src = null;
					if (stringBuffer_img.toString()
							.contains(".jpg".toLowerCase())
							|| stringBuffer_img.toString()
									.contains(".png".toLowerCase())
							|| stringBuffer_img.toString()
									.contains(".gif".toLowerCase())
							|| stringBuffer_img.toString()
									.contains(".bmp".toLowerCase())
							|| stringBuffer_img.toString()
									.contains(".jpeg".toLowerCase())
							|| stringBuffer_img.toString()
									.contains(".jpe".toLowerCase())
							|| stringBuffer_img.toString()
									.contains(".pdf".toLowerCase())) {
						img_src = "<img src=\"" + stringBuffer_img.toString()
								+ "\"/>";
					} else {
						img_src = "<img src=\"" + stringBuffer_img.toString()
								+ ".jpg\"/>";
					}

					stringBuffer_text.append(img_src);
				} else {// 纯文本
					if(str != null || !str.equals(""))
						stringBuffer_text.append(str + "<br>");
				}
			}
			// setTexts(stringBuffer_text.toString());
			return stringBuffer_text;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (stringReader != null) {
					stringReader.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
		// tv_show.setText(str_afterQuitTitle);
	}

}
