package com.coolReader.dao;

import java.util.ArrayList;
import java.util.List;

import com.coolReader.Bean.SaveInfoBean;
import com.coolReader.Bean.URLInfoBean;
import com.coolReader.Bean.URLTableBean;
import com.coolReader.Bean.UserInfoBean;
import com.coolReader.Bean.UserTableBean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBPerform {
	private static SQLiteDatabase db = null;
	private static UserDBHelper userDBHelper;
	private static URLDBHelper urldbHelper;
	private static SaveDBHelper saveDBHelper;

	// 创建表
	private static void createUserDBHelper(Context context) {
		/*
		 * userDBHelper = new UserDBHelper(context, TablesName.DATABASE_NAME,
		 * null, TablesName.VERSION);
		 */
		userDBHelper = UserDBHelper.getInstance(context,
				TablesName.DATABASE_NAME, null, TablesName.VERSION);
	}

	private static void createUrlDBHelper(Context context) {
		/*
		 * urldbHelper = new URLDBHelper(context, TablesName.DATABASE_NAME,
		 * null, TablesName.VERSION);
		 */
		urldbHelper = URLDBHelper.getInstance(context,
				TablesName.DATABASE_NAME, null, TablesName.VERSION);
	}

	private static void createSaveDBHelper(Context context) {
		/*
		 * saveDBHelper = new SaveDBHelper(context, TablesName.DATABASE_NAME,
		 * null, TablesName.VERSION);
		 */
		saveDBHelper = SaveDBHelper.getInstance(context,
				TablesName.DATABASE_NAME, null, TablesName.VERSION);
	}

	public static void createUserTable(Context context) {
		createUserDBHelper(context);
		db = userDBHelper.getWritableDatabase();
		db.execSQL(CreateWordDAO.sql_usertable);
	}

	public static void createURLTable(Context context) {
		createUrlDBHelper(context);
		db = urldbHelper.getWritableDatabase();
		db.execSQL(CreateWordDAO.sql_urltable);
	}

	public static void createSaveTable(Context context) {
		createSaveDBHelper(context);
		db = saveDBHelper.getWritableDatabase();
		db.execSQL(CreateWordDAO.sql_savetable);
	}

	// 用户表的添加操作
	/**
	 * @param userInfoBean
	 * @return
	 */
	public static void insertUserInfo(Context context, UserInfoBean userInfoBean) {

		createUserTable(context);
		UserInfoDao.insertUserInfo(db, userInfoBean);
		if(db != null){
			db.close();
		}

	}

	// 用户表的删除
	/**
	 * @param context
	 * @param db
	 * @param email
	 * @return <0 删除失败
	 */
	public static int deleteUserInfo(Context context, String email) {

		createUserTable(context);
		int ret = UserInfoDao.deleteUserInfo(db, email);
		if(db != null){
			db.close();
		}
		return ret;
	}

	// 用户表的更新
	/**
	 * @param db
	 * @param userInfoBean
	 * @return <0 更新失败
	 */
	public static int updateUserInfo(Context context, UserInfoBean userInfoBean) {

		createUserTable(context);
		int ret = UserInfoDao.updateUserInfo(db, userInfoBean);
		if(db != null){
			db.close();
		}
		return ret;
	}

	// 查找表中所有数据
	/**
	 * @param context
	 * @param db
	 * @return
	 */
	public static List<UserInfoBean> queryUserInfo(Context context) {

		createUserTable(context);
		 List<UserInfoBean> ret = UserInfoDao.queryUserInfo(db);
		if(db != null){
			db.close();
		}
		return ret;
	}

	// 查找指定email的数据
	public static UserInfoBean queryUserInfoByEmail(Context context,
			String email) {

		createUserTable(context);
		UserInfoBean ret = UserInfoDao.queryUserInfoByEmail(db, email);
		if(db != null){
			db.close();
		}
		return ret;

	}
	
	// 查找指定id的数据
		public static UserInfoBean queryUserInfoByID(Context context,
				int id) {

			createUserTable(context);
			UserInfoBean ret = UserInfoDao.queryUserInfoByID(db, id);
			if(db != null){
				db.close();
			}
			return ret;
		}

	// URL的添加操作
	/**
	 * @param db
	 * @param urlInfoBean
	 */
	public static void insertURLInfo(Context context, URLInfoBean urlInfoBean) {

		createURLTable(context);
		URLInfoDao.insertURLInfo(db, urlInfoBean);
		if(db != null){
			db.close();
		}
	}

	// URL的删除操作
	/**
	 * @param context
	 * @param db
	 * @param urlId
	 * @return <0删除失败
	 */
	public static int deleteURLInfoByUrl(Context context, String url) {

		createURLTable(context);
		int ret = URLInfoDao.deleteURLInfoByUrl(db, url);
		if(db != null){
			db.close();
		}
		return ret;

	}

	// URL的修改操作
	/**
	 * @param context
	 * @param db
	 * @param urlInfoBean
	 * @return <0 修改失败
	 */
	public static int updateUrlInfo(Context context, URLInfoBean urlInfoBean) {

		createURLTable(context);
		int ret = URLInfoDao.updateUserInfo(db, urlInfoBean);
		if(db != null){
			db.close();
		}
		return ret;
	}

	// 查找URL表中所有数据
	/**
	 * @param context
	 * @param db
	 * @return
	 */
	public static List<URLInfoBean> queryURLInfo(Context context) {

		createURLTable(context);
		List<URLInfoBean> ret=  URLInfoDao.queryURLInfo(db);
		if(db != null){
			db.close();
		}
		return ret;
	}

	// 查找指定url的数据(URL)
	public static URLInfoBean queryURLrInfoByUrl(Context context, String url) {

		createURLTable(context);
		return URLInfoDao.queryURLrInfoByUrl(db, url);
	}
	
	// 查找指定id的数据
		public static URLInfoBean queryURLrInfoByID(Context context, int id) {

			createURLTable(context);
			URLInfoBean ret = URLInfoDao.queryURLrInfoByID(db, id);
			if(db != null){
				db.close();
			}
			return ret;
		}
		
		//实现根据urlTitle的模糊查询
				public static List<URLInfoBean> queryUrlInfoByTitleBlurry(Context context, String title){
					createURLTable(context);
//					String sql = "select * from "+TablesName.URL_TABLE+" where "+URLTableBean.URL_TITLE +" like '%"+title+"%'";
					List<URLInfoBean> list = URLInfoDao.queryUrlInfoByTitleBlurry(db, title);
					
					if(db != null){
						db.close();
					}
					
					return list;
				}
				
				//根据URL实现模糊匹配
				public static List<URLInfoBean> queryUrlInfoByURLBlurry(Context context, String url){
					createURLTable(context);
					List<URLInfoBean> list = URLInfoDao.queryUrlInfoByURLBlurry(db, url);
					if(db != null){
						db.close();
					}
					
					return list;
				}
		
	// 存档的添加操作
	/**
	 * @param context
	 * @param db
	 * @param saveInfoBean
	 */
	public static void insertSaveInfo(Context context, SaveInfoBean saveInfoBean) {

		createSaveTable(context);
		SaveInfoDao.insertSaveInfo(db, saveInfoBean);
		if(db != null){
			db.close();
		}
	}

	// 存档的删除操作
	/**
	 * @param context
	 * @param db
	 * @param saveId
	 * @return <0 失败
	 */
	public static int deleteSaveInfo(Context context, int saveId) {

		createSaveTable(context);
		int ret = SaveInfoDao.deleteSaveInfo(db, saveId);
		if(db != null){
			db.close();
		}
		return ret;
	}

	// 存档的修改操作
	/**
	 * @param context
	 * @param db
	 * @param saveInfoBean
	 * @return 《0失败
	 */
	public static int updateSaveInfo(Context context, SaveInfoBean saveInfoBean) {

		createSaveTable(context);
		int ret = SaveInfoDao.updateSaveInfo(db, saveInfoBean);
		if(db != null){
			db.close();
		}
		return ret;
	}

	// 查找存档表中所有数据
	/**
	 * @param context
	 * @param db
	 * @return
	 */
	public static List<SaveInfoBean> querySaveInfo(Context context) {

		createSaveTable(context);
		List<SaveInfoBean> ret = SaveInfoDao.querySaveInfo(db);
		if(db != null){
			db.close();
		}
		return ret;
	}

	// 查找存档表中指定id的数据
	public static SaveInfoBean querySaveInfoById(Context context, long id) {

		createSaveTable(context);
		SaveInfoBean ret = SaveInfoDao.querySaveInfoById(db, id);
		if(db != null){
			db.close();
		}
		return ret;
	}
}
