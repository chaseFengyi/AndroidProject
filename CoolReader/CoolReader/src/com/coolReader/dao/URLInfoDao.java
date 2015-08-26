package com.coolReader.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolReader.Bean.URLInfoBean;
import com.coolReader.Bean.URLTableBean;

public class URLInfoDao {
	// URL的添加操作
	/**
	 * @param 
	 * @return
	 */
	public static void insertURLInfo(SQLiteDatabase db, URLInfoBean urlInfoBean) {

		String sql = "replace into " + TablesName.URL_TABLE + "("
				+ URLTableBean.URL_TITLE + "," + URLTableBean.URL_CONTENT + ","
				+ URLTableBean.URL_LINK + ","+ URLTableBean.URL_TAG+") values(?,?,?,?)";
		db.execSQL(
				sql,
				new String[] { urlInfoBean.getUrlTitle(),
						urlInfoBean.getUrlContent(), urlInfoBean.getUrlLink(),String.valueOf(urlInfoBean.getUrlTag()) });

	}

	// URL的删除操作
	/**
	 * @param db
	 * @param id
	 *            根据email删除
	 * @return the number of rows affected by this SQL statement execution.
	 */
	public static int deleteURLInfoByUrl(SQLiteDatabase db, String url) {
		return db.delete(TablesName.URL_TABLE, URLTableBean.URL_LINK + "=?",
				new String[] { url });
	}

	// URL的修改操作
	/**
	 * @param db
	 * @param userInfoBean
	 * @return the number of rows affected
	 */
	public static int updateUserInfo(SQLiteDatabase db,
			URLInfoBean urlInfoBean) {
		ContentValues values = new ContentValues();
		values.put(URLTableBean.URL_TITLE, urlInfoBean.getUrlTitle());
		values.put(URLTableBean.URL_CONTENT, urlInfoBean.getUrlContent());
		values.put(URLTableBean.URL_LINK, urlInfoBean.getUrlLink());
		values.put(URLTableBean.URL_TAG, urlInfoBean.getUrlTag());
		return db.update(TablesName.URL_TABLE, values, URLTableBean.URL_ID
				+ "=?",
				new String[] { String.valueOf(urlInfoBean.getUrlId()) });
	}

	// 查找表中所有数据
	public static List<URLInfoBean> queryURLInfo(SQLiteDatabase db) {
		List<URLInfoBean> urls = null;
		Cursor cursor = db.query(TablesName.URL_TABLE, null, null, null, null,
				null, null);
		if (cursor != null) {
			urls = new ArrayList<URLInfoBean>();
			while (cursor.moveToNext()) {
				URLInfoBean urlInfoBean = new URLInfoBean();
				urlInfoBean.setUrlId(cursor.getInt(cursor.getColumnIndex(URLTableBean.URL_ID)));
				urlInfoBean.setUrlTitle(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_TITLE)));
				urlInfoBean.setUrlContent(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_CONTENT)));
				urlInfoBean.setUrlLink(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_LINK)));
				urlInfoBean.setUrlTag(cursor.getInt(cursor.getColumnIndex(URLTableBean.URL_TAG)));
				urls.add(urlInfoBean);
			}
		}
		closeSursor(cursor);

		return urls;
	}

	// 查找指定url的数据
	public static URLInfoBean queryURLrInfoByUrl(SQLiteDatabase db, String url) {
		Cursor cursor = db.query(TablesName.URL_TABLE, null, URLTableBean.URL_LINK
				+ "=?", new String[] { url }, null, null, null);

		URLInfoBean urlInfoBean = null;
		if (cursor != null && cursor.moveToFirst()) {
			urlInfoBean = new URLInfoBean();
			urlInfoBean.setUrlId(cursor.getInt(cursor.getColumnIndex(URLTableBean.URL_ID)));
			urlInfoBean.setUrlTitle(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_TITLE)));
			urlInfoBean.setUrlContent(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_CONTENT)));
			urlInfoBean.setUrlLink(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_LINK)));
			urlInfoBean.setUrlTag(cursor.getInt(cursor.getColumnIndex(URLTableBean.URL_TAG)));
		}
		
		closeSursor(cursor);

		return urlInfoBean;
	}
	
	// 查找指定id的数据
		public static URLInfoBean queryURLrInfoByID(SQLiteDatabase db, int id) {
			Cursor cursor = db.query(TablesName.URL_TABLE, null, URLTableBean.URL_ID
					+ "=?", new String[] { String.valueOf(id) }, null, null, null);

			URLInfoBean urlInfoBean = null;
			if (cursor != null && cursor.moveToFirst()) {
				urlInfoBean = new URLInfoBean();
				urlInfoBean.setUrlId(cursor.getInt(cursor.getColumnIndex(URLTableBean.URL_ID)));
				urlInfoBean.setUrlTitle(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_TITLE)));
				urlInfoBean.setUrlContent(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_CONTENT)));
				urlInfoBean.setUrlLink(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_LINK)));
				urlInfoBean.setUrlTag(cursor.getInt(cursor.getColumnIndex(URLTableBean.URL_TAG)));
			}

			closeSursor(cursor);
			
			return urlInfoBean;
		}
		
		
		//实现根据urlTitle的模糊查询
		public static List<URLInfoBean> queryUrlInfoByTitleBlurry(SQLiteDatabase db, String title){
//			String sql = "select * from "+TablesName.URL_TABLE+" where "+URLTableBean.URL_TITLE +" like '%"+title+"%'";
			List<URLInfoBean> list = new ArrayList<URLInfoBean>();
			Cursor cursor = db.query(TablesName.URL_TABLE, null, URLTableBean.URL_TITLE+" like ?", new String[]{"%"+title+"%"}, null, null,	null);
			URLInfoBean urlInfoBean = null;
			if(cursor != null && cursor.moveToFirst()){
				urlInfoBean = new URLInfoBean();
				urlInfoBean.setUrlId(cursor.getInt(cursor.getColumnIndex(URLTableBean.URL_ID)));
				urlInfoBean.setUrlTitle(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_TITLE)));
				urlInfoBean.setUrlContent(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_CONTENT)));
				urlInfoBean.setUrlLink(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_LINK)));
				urlInfoBean.setUrlTag(cursor.getInt(cursor.getColumnIndex(URLTableBean.URL_TAG)));
				list.add(urlInfoBean);
			}
			
			closeSursor(cursor);
			return list;
		}
		
		//根据URL实现模糊匹配
		public static List<URLInfoBean> queryUrlInfoByURLBlurry(SQLiteDatabase db, String url){
			List<URLInfoBean> list = new ArrayList<URLInfoBean>();
			Cursor cursor = db.query(TablesName.URL_TABLE, null, URLTableBean.URL_LINK+" like ?", new String[]{"%"+url+"%"}, null, null,	null);
			URLInfoBean urlInfoBean = null;
			if(cursor != null && cursor.moveToFirst()){
				urlInfoBean = new URLInfoBean();
				urlInfoBean.setUrlId(cursor.getInt(cursor.getColumnIndex(URLTableBean.URL_ID)));
				urlInfoBean.setUrlTitle(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_TITLE)));
				urlInfoBean.setUrlContent(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_CONTENT)));
				urlInfoBean.setUrlLink(cursor.getString(cursor.getColumnIndex(URLTableBean.URL_LINK)));
				urlInfoBean.setUrlTag(cursor.getInt(cursor.getColumnIndex(URLTableBean.URL_TAG)));
				list.add(urlInfoBean);
			}
			
			closeSursor(cursor);
			return list;
		}
		
		public static void closeSursor(Cursor cursor){
			if(cursor != null)
				cursor.close();
		}
		
}
