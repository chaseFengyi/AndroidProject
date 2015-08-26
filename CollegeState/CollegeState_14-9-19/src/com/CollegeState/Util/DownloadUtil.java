package com.CollegeState.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Xml;

import com.CollegeState.Data.UpdateInfo;

public class DownloadUtil {
	
	public static final int PARSER_XML_ERROR = 100;
	public static final int PARSER_XML_SUCCESS = 101;
	public static final int SERVER_ERROR = 102;
	public static final int URL_ERROR = 103;
	public static final int NET_WORK_ERROR = 104;
	public static final int DOWNLOAD_SUCCESS = 105;
	public static final int DOWNLOAD_FAILD = 106;
	
	public static final String updateURL = "http://121.42.8.50/CS/app/update.xml";
	/**
	 * 下载文件
	 * 
	 * @param serverPath
	 *            服务器文件的路径
	 * @param savePath
	 *            本地保存文件的路径
	 * @param pd
	 *            进度条对话框
	 * @return 下载成功返回文件对象，失败返回null
	 */
	public static File download(String serverPath, String savePath,
			ProgressDialog pd) {

		try {
			URL url = new URL(serverPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);

			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				// 设置进度条的最大值
				pd.setMax(conn.getContentLength());
				InputStream is = conn.getInputStream();
				File file = new File(savePath);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				int totle = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					totle += len;
					pd.setProgress(totle);
					Thread.sleep(20);
				}
				fos.flush();
				fos.close();
				is.close();
				return file;
			} else {
				return null;
			}
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 获取服务器文件的名称
	 * 
	 * @param serverPaht
	 * @return
	 */
	public static String getFileName(String serverPath) {
		return serverPath.substring(serverPath.lastIndexOf("/") + 1);
	}
	
	/**
	 * 获取app版本
	 * @return
	 */
	public static String getAppVersion(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo infos = packageManager.getPackageInfo(context.getPackageName(),
					0);
			return infos.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 解析服务器xml文件
	 * @param is
	 * @return
	 */
	public static UpdateInfo getUpdateInfo(InputStream is) {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "utf-8");
			int type = parser.getEventType();
			UpdateInfo info = null;
			while (type != XmlPullParser.END_DOCUMENT) {
				switch (type) {
				case XmlPullParser.START_TAG:
					if ("info".equals(parser.getName())) {
						info = new UpdateInfo();
					} else if ("version".equals(parser.getName())) {
						info.setVersion(parser.nextText());
					} else if ("description".equals(parser.getName())) {
						info.setDescription(parser.nextText());
					} else if ("apkurl".equals(parser.getName())) {
						info.setApkurl(parser.nextText());
					}
					break;
				default:
					break;
				}
				type = parser.next();
			}
			return info;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 安装apk
	 * 
	 * @param file
	 */
	public static void installApk(File file,Context context) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		/*
		 * //setData会清除setType方法 intent.setData(Uri.fromFile(file));
		 * //setType会清除setData方法
		 * intent.setType("application/vnd.android.package-archive");
		 */
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
