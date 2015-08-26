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
 * ��ʾURL����ϸ��Ϣ
 * 
 * @author FengYi~ 2015��8��12��11:34:07
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
		ActionBarUtil.centerActionBar(this, this, getActionBar(), "��ϸ��Ϣ");

		tv_title = (TextView) findViewById(R.id.text_title);
		tv_show = (TextView) findViewById(R.id.text_content);
		// �������ݹ����ɻ�����ʾ
		tv_show.setMovementMethod(ScrollingMovementMethod.getInstance());
		getContent();
		showInUI();
	}

	/**
	 * ��ȡǰһҳ�ĵ�������Ϣ
	 */
	private void getContent() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String whichActivity = (String)bundle.getString("key");
		bean = (WebContentBean) bundle.getSerializable(MainPageActivity.TAG);
	}

	/**
	 * ��UI�Ͻ���չʾ��Ϣ
	 */
	private void showInUI() {
		if (bean != null) {
			String content = bean.getWebContent();
			String title = bean.getWebTitle();

			// �Ը��ı�����ʽ��ʾ
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
							// ����LruCache���д���
							// ʵ��������
							ImageDealByLruCache byLruCache = ImageDealByLruCache
									.getInstance();
							// �ӻ�����ȡͼƬ
							Bitmap bitmap = byLruCache.getBitmapFromMemCache(source);
							if (bitmap == null) {// ������û��ͼƬ�������������ͼƬ
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
	 * �ֱ������ �ı�ͼƬ�����ݽ��зָ���ʾ
	 * 
	 * @param content
	 */
	public StringBuffer showStandardContent(String content) {
		// ȥ�������title����֮ǰ
		String str_beforeQuitTitle[] = content.split("\\[content\\]:");
		System.out.println("content="+content);
		System.out.println("before===="+str_beforeQuitTitle.length);
		System.out.println("before====before"+str_beforeQuitTitle[0]);
		// ��title����ȥ��֮��
		String str_afterQuitTitle = str_beforeQuitTitle[1];
		// ���ַ���תΪ��������һ��һ�н��ж�ȡ
		Reader stringReader = new StringReader(str_afterQuitTitle);
		BufferedReader bufferedReader = new BufferedReader(stringReader);
		StringBuffer stringBuffer_text = new StringBuffer();// �洢�ı�
		// ֪ͨ�����Ƿ���׼���ñ���ȡ
		try {
			String str;
			while (true) {
				str = bufferedReader.readLine();
				if (str == null)
					break;
				if (str.contains("[img]:")) {// �����[img]��ͷ������ʾ����ͼƬ�������н�ȡ
					StringBuffer stringBuffer_img = new StringBuffer();// �洢ͼƬ�ַ�
					String string_format = null;// �洢�ı���ʽ
					stringBuffer_img.append(str.substring(6, str.length()));
					// һ��������֪������[format]�ֶ�
					while ((str = bufferedReader.readLine()) != null) {
						if (!str.contains("[format]:")) {
							stringBuffer_img.append(str);
						} else {// ����[format]�ֶ�
							if (str.length() == 8) {
								string_format = "jpg";
							} else {
								string_format = str.substring(9,
										str.length() - 1);
							}
							break;
						}
					}
					// ��ȡ��ͼƬ��Ϣ�ˣ���ͼƬ���н���
					// String imag =
					// TCPRequest.decode(stringBuffer_img.toString());
					// ��ʾ��final String sText2 =
					// "����ͼƬ��Ϣ��<img src=\"/mnt/sdcard/temp/1.jpg\" />"�ĸ�ʽ
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
				} else {// ���ı�
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
