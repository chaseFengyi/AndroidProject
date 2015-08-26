package com.secondmarket.DB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract.Helpers;
import android.widget.Toast;

import com.secondmarket.bean.SortSearchDemo;
import com.secondmarket.bean.UserInfoDemo;

public class DBPerform {
	private static SQLiteDatabase db = null;
	private static DBHelper dbHelper;
	
	private static void createDBHelper(Context context){
		dbHelper = new DBHelper(context);
	}
	
	public static void createCollectTable(Context context){
		createDBHelper(context);
		dbHelper.getWritableDatabase().execSQL(DBCreateWord.sqlCollect);
	}
	
	public static void createPublishTable(Context context){
		createDBHelper(context);
		dbHelper.getWritableDatabase().execSQL(DBCreateWord.sqlPublish);
	}
	
	public static void createUserTable(Context context){
		createDBHelper(context);
		dbHelper.getWritableDatabase().execSQL(DBCreateWord.sqlUser);
	}
	
	/*
	 * -1：存入数据库失败 1：成功存入数据库 0：商品已经存在
	 */
	// 将点击收藏后的商品加入数据库
	public static int insertQuery(Context context,
			SortSearchDemo sortSearchDemo, String tableName) {
		int flags = 0;
		if (sortSearchDemo != null) {
			// ContentValues存储的名值对当中的名是一个String类型,而值都是基本类型
			ContentValues values = new ContentValues();
			System.out.println("goodsid="+sortSearchDemo.getGoodsId());
			values.put(DBCreateWord.ID, sortSearchDemo.getGoodsId());
			values.put(DBCreateWord.GOODSNAME, sortSearchDemo.getGoodsName());
			values.put(DBCreateWord.GOODSTYPEID, sortSearchDemo.getGoodsTypeId());
			values.put(DBCreateWord.GOODSDESCRIBE,
					sortSearchDemo.getGoodsDescribe());
			values.put(DBCreateWord.GOODSPRICE, sortSearchDemo.getGoodsPrice());
			values.put(DBCreateWord.GOODSWANTED, sortSearchDemo.getGoodsWanted());
			values.put(DBCreateWord.USERID, sortSearchDemo.getUserId());
			values.put(DBCreateWord.ISSALE, sortSearchDemo.getIsSale());
			values.put(DBCreateWord.GOODSPUBLISHTIME,
					sortSearchDemo.getGoodsPublishTime());
			values.put(DBCreateWord.GOODSCONNECT, sortSearchDemo.getGoodsConnect());
			System.out.println("DBPerformPicture="
					+ sortSearchDemo.getList().get(0).get("goodsPictureAD1"));
			if (sortSearchDemo.getList().size() >= 1) {
				switch (sortSearchDemo.getList().get(0).size()) {
				case 0:
					values.put(DBCreateWord.GOODSPICTUREAD1, "");
					values.put(DBCreateWord.GOODSPICTUREAD2, "");
					values.put(DBCreateWord.GOODSPICTUREAD3, "");
					break;
				case 1:
					values.put(DBCreateWord.GOODSPICTUREAD1, sortSearchDemo
							.getList().get(0).get("goodsPictureAD1"));
					values.put(DBCreateWord.GOODSPICTUREAD2, "");
					values.put(DBCreateWord.GOODSPICTUREAD3, "");
					break;
				case 2:
					values.put(DBCreateWord.GOODSPICTUREAD1, sortSearchDemo
							.getList().get(0).get("goodsPictureAD1"));
					values.put(DBCreateWord.GOODSPICTUREAD2, sortSearchDemo
							.getList().get(0).get("goodsPictureAD2"));
					values.put(DBCreateWord.GOODSPICTUREAD3, "");
					break;
				case 3:
					values.put(DBCreateWord.GOODSPICTUREAD1, sortSearchDemo
							.getList().get(0).get("goodsPictureAD1"));
					values.put(DBCreateWord.GOODSPICTUREAD2, sortSearchDemo
							.getList().get(0).get("goodsPictureAD2"));
					values.put(DBCreateWord.GOODSPICTUREAD3, sortSearchDemo
							.getList().get(0).get("goodsPictureAD2"));
					break;
				}
			}
			
//			createDBHelper(context);
			// 创建数据库
			int flag = 1;
			db = dbHelper.getReadableDatabase();
			Cursor cursor = db
					.query(tableName, new String[] { DBCreateWord.ID }, DBCreateWord.ID
							+ "=?", new String[] { String
							.valueOf(sortSearchDemo.getGoodsId()) }, null,
							null, null);
			while (cursor.moveToNext()) {
				String id = cursor
						.getString(cursor.getColumnIndex(DBCreateWord.ID));
				if (id.equals(String.valueOf(sortSearchDemo.getGoodsId()))) {
					flag = 0;
					flags = 0;
				}
			}
			if (flag == 1) {
				db = dbHelper.getWritableDatabase();
				db.insert(tableName, null, values);

				flags = 1;
			}
		} else {

			flags = -1;
		}
		db.close();
		return flags;
	}

	/*
	 * -1:删除失败 1:删除成功
	 */
	// 点击取消收藏，删除数据库中已收藏的商品信息
	public static int deleteQuery(Context context,
			SortSearchDemo sortSearchDemo, String tableName) {
		int sign = 0;
		DBHelper helper = new DBHelper(context.getApplicationContext());
		// 创建数据库
		int flag = 1;
		db = helper.getReadableDatabase();
		Cursor cursor = db.query(tableName, new String[] { DBCreateWord.ID },
				DBCreateWord.ID + "=?",
				new String[] { String.valueOf(sortSearchDemo.getGoodsId()) },
				null, null, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(cursor.getColumnIndex(DBCreateWord.ID));
			if (id.equals(null) || id == null) {
				flag = 0;
				sign = -1;
			}
		}
		if (flag == 1) {
			db = helper.getWritableDatabase();
			String[] values = { String.valueOf(sortSearchDemo.getGoodsId()) };
			db.delete(tableName, DBCreateWord.ID + "=?", values);
			sign = 1;
		}

		db.close();
		return sign;
	}

	// http://blog.csdn.net/zlqqhs/article/details/8607849
	// 从数据库获取收藏的所有商品信息
	public static List<SortSearchDemo> SelectQuery(Context context,
			String tableName,String sql, String dropSql) {
		System.out.println("11111");
		DBHelper helper = new DBHelper(context.getApplicationContext());
		System.out.println("2222");
		// 创建数据库
		db = helper.getReadableDatabase();
		System.out.println("3333");
		Cursor cursor = db.rawQuery("select * from "+tableName, null);
		System.out.println("4444");
		List<SortSearchDemo> list = new ArrayList<SortSearchDemo>();
		System.out.println("%5555");
		System.out.println("cursorDBPerform=" + cursor);
		if (cursor != null) {
			System.out.println("66666");
			System.out.println("cursor1111+="+cursor.getCount());
			while (cursor.moveToNext()) {
				System.out.println("77777");
				System.out.println("cursor222");
				List<Map<String, String>> lis = new ArrayList<Map<String, String>>();
				Map<String, String> map = null;
				SortSearchDemo searchDemo = new SortSearchDemo();
				// SortSearchDemo [isSale=0, goodsId=1, goodsConnect=1,
				// userId=1,
				// goodsDescribe=高数书,
				// goodsTypeId=1, goodsPrice=23.0, goodsWanted=1,
				// goodsPublishTime=2014-1-1,
				// goodsName=高数, list=[{goodsPictureAD1=android.jpg}]]
				System.out.println("selectGoodsid="+cursor.getInt(cursor
						.getColumnIndex(DBCreateWord.ID)));
				searchDemo.setGoodsId(cursor.getInt(cursor
						.getColumnIndex(DBCreateWord.ID)));
				searchDemo.setGoodsName(cursor.getString(cursor
						.getColumnIndex(DBCreateWord.GOODSNAME)));
				searchDemo.setGoodsPublishTime(cursor.getString(cursor
						.getColumnIndex(DBCreateWord.GOODSPUBLISHTIME)));
				searchDemo.setGoodsWanted(cursor.getInt(cursor
						.getColumnIndex(DBCreateWord.GOODSWANTED)));
				searchDemo.setGoodsPrice(cursor.getString(cursor
						.getColumnIndex(DBCreateWord.GOODSPRICE)));
				searchDemo.setGoodsTypeId(cursor.getString(cursor
						.getColumnIndex(DBCreateWord.GOODSTYPEID)));
				searchDemo.setGoodsDescribe(cursor.getString(cursor
						.getColumnIndex(DBCreateWord.GOODSDESCRIBE)));
				searchDemo.setUserId(cursor.getInt(cursor
						.getColumnIndex(DBCreateWord.USERID)));
				searchDemo.setGoodsConnect(cursor.getString(cursor
						.getColumnIndex(DBCreateWord.GOODSCONNECT)));
				searchDemo.setIsSale(cursor.getInt(cursor
						.getColumnIndex(DBCreateWord.ISSALE)));
				String photo1 = cursor.getString(cursor
						.getColumnIndex(DBCreateWord.GOODSPICTUREAD1));
				String photo2 = cursor.getString(cursor
						.getColumnIndex(DBCreateWord.GOODSPICTUREAD2));
				String photo3 = cursor.getString(cursor
						.getColumnIndex(DBCreateWord.GOODSPICTUREAD3));
				map = new HashMap<String, String>();
				map.put("goodsPictureAD1", photo1);
				map.put("goodsPictureAD2", photo2);
				map.put("goodsPictureAD3", photo3);
				lis.add(map);
				searchDemo.setList(lis);
				System.out.println("DBPerform====+"
						+ cursor.getString(cursor
								.getColumnIndex(DBCreateWord.GOODSNAME)));
				System.out.println("DBPerform====+"
						+ cursor.getInt(cursor.getColumnIndex(DBCreateWord.ID)));
				System.out.println("DBPerform====+"
						+ cursor.getString(cursor
								.getColumnIndex(DBCreateWord.GOODSPUBLISHTIME)));
				System.out.println("DBPerform====+"
						+ cursor.getString(cursor
								.getColumnIndex(DBCreateWord.GOODSWANTED)));
				System.out.println("DBPerform====+"
						+ cursor.getString(cursor
								.getColumnIndex(DBCreateWord.GOODSPRICE)));
				System.out.println("DBPerform====+"
						+ cursor.getString(cursor
								.getColumnIndex(DBCreateWord.GOODSTYPEID)));
				System.out.println("DBPerform====+"
						+ cursor.getString(cursor
								.getColumnIndex(DBCreateWord.GOODSDESCRIBE)));
				System.out
						.println("DBPerform====+"
								+ cursor.getInt(cursor
										.getColumnIndex(DBCreateWord.USERID)));
				System.out.println("DBPerform====+"
						+ cursor.getString(cursor
								.getColumnIndex(DBCreateWord.GOODSCONNECT)));
				System.out
						.println("DBPerform====+"
								+ cursor.getInt(cursor
										.getColumnIndex(DBCreateWord.ISSALE)));
				System.out.println("DBPerform====+" + lis);
				list.add(searchDemo);
			}
		}
		cursor.close();
		db.close();
		return list;
	}

	/*
	 * -1：存入数据库失败 1：成功存入数据库 0：商品已经存在
	 */
	// 将点击收藏后的商品加入数据库
	public static int insertPublishTable(Context context,
			SortSearchDemo sortSearchDemo, String tableName) {
		int flags = 0;
		if (sortSearchDemo != null) {
			System.out.println("publish1111");
			// ContentValues存储的名值对当中的名是一个String类型,而值都是基本类型
			ContentValues values = new ContentValues();
			values.put(DBCreateWord.GOODSNAME, sortSearchDemo.getGoodsName());
			values.put(DBCreateWord.GOODSTYPEID, sortSearchDemo.getGoodsTypeId());
			values.put(DBCreateWord.GOODSDESCRIBE,
					sortSearchDemo.getGoodsDescribe());
			values.put(DBCreateWord.GOODSPRICE, sortSearchDemo.getGoodsPrice());
			values.put(DBCreateWord.GOODSWANTED, sortSearchDemo.getGoodsWanted());
			values.put(DBCreateWord.USERID, sortSearchDemo.getUserId());
			values.put(DBCreateWord.ISSALE, sortSearchDemo.getIsSale());
			values.put(DBCreateWord.GOODSPUBLISHTIME,
					sortSearchDemo.getGoodsPublishTime());
			values.put(DBCreateWord.GOODSCONNECT, sortSearchDemo.getGoodsConnect());
			System.out.println("DBPerformPicture="
					+ sortSearchDemo.getList().get(0).get("goodsPictureAD1"));
			if (sortSearchDemo.getList().size() >= 1) {
				switch (sortSearchDemo.getList().get(0).size()) {
				case 0:
					values.put(DBCreateWord.GOODSPICTUREAD1, "");
					values.put(DBCreateWord.GOODSPICTUREAD2, "");
					values.put(DBCreateWord.GOODSPICTUREAD3, "");
					break;
				case 1:
					values.put(DBCreateWord.GOODSPICTUREAD1, sortSearchDemo
							.getList().get(0).get("goodsPictureAD1"));
					values.put(DBCreateWord.GOODSPICTUREAD2, "");
					values.put(DBCreateWord.GOODSPICTUREAD3, "");
					break;
				case 2:
					values.put(DBCreateWord.GOODSPICTUREAD1, sortSearchDemo
							.getList().get(0).get("goodsPictureAD1"));
					values.put(DBCreateWord.GOODSPICTUREAD2, sortSearchDemo
							.getList().get(0).get("goodsPictureAD2"));
					values.put(DBCreateWord.GOODSPICTUREAD3, "");
					break;
				case 3:
					values.put(DBCreateWord.GOODSPICTUREAD1, sortSearchDemo
							.getList().get(0).get("goodsPictureAD1"));
					values.put(DBCreateWord.GOODSPICTUREAD2, sortSearchDemo
							.getList().get(0).get("goodsPictureAD2"));
					values.put(DBCreateWord.GOODSPICTUREAD3, sortSearchDemo
							.getList().get(0).get("goodsPictureAD3"));
					break;
				}
			}
			System.out.println("publish22222");
			DBHelper helper = new DBHelper(context.getApplicationContext());
			// 创建数据库

			db = helper.getWritableDatabase();
			System.out.println("publish333");
			db.insert(tableName, null, values);

			flags = 1;
		}
		db.close();
		return flags;
	}
	

	/*
	 * -1：存入数据库失败 1：成功存入数据库 0：商品已经存在
	 */
	// 将点击收藏后的商品加入数据库
	public static int insertUserQuery(Context context,
			UserInfoDemo userInfoDemo, String tableName) {
		int flags = 0;
		if (userInfoDemo != null) {
			// ContentValues存储的名值对当中的名是一个String类型,而值都是基本类型
			ContentValues values = new ContentValues();
			values.put(DBCreateWord.ID, userInfoDemo.getUserId());
			values.put(DBCreateWord.USER_SCHOOLNUM, userInfoDemo.getUserSchoolNum());
			values.put(DBCreateWord.USER_NAME, userInfoDemo.getUserName());
			values.put(DBCreateWord.USER_PASS, userInfoDemo.getUserPassword());
			values.put(DBCreateWord.USER_NIKE, userInfoDemo.getUserNike());
			values.put(DBCreateWord.USER_SEX, userInfoDemo.getUserSex());
			values.put(DBCreateWord.USER_GRADE, userInfoDemo.getUserGrade());
			values.put(DBCreateWord.USER_PICTURE, userInfoDemo.getUserPictureAd());
			createDBHelper(context);
			// 创建数据库
			DBHelper dbHelper = new DBHelper(context.getApplicationContext());
			int flag = 1;
			db = dbHelper.getWritableDatabase();
			Cursor cursor = db
					.query(tableName, new String[] { DBCreateWord.ID }, DBCreateWord.ID
							+ "=?", new String[] { String
							.valueOf(userInfoDemo.getUserId()) }, null,
							null, null);
			while (cursor.moveToNext()) {
				String id = cursor
						.getString(cursor.getColumnIndex(DBCreateWord.ID));
				if (id.equals(String.valueOf(userInfoDemo.getUserId()))) {
					flag = 0;
					flags = 0;
				}
			}
			if (flag == 1) {
				db = dbHelper.getWritableDatabase();
				db.insert(tableName, null, values);

				flags = 1;
			}
		} else {

			flags = -1;
		}
		db.close();
		return flags;
	}

	// 从数据库获取用户信息
		public static List<UserInfoDemo> SelectUserQuery(Context context,
				String tableName) {
			System.out.println("11111");
			DBHelper helper = new DBHelper(context.getApplicationContext());
			System.out.println("2222");
			// 创建数据库
			db = helper.getReadableDatabase();
			System.out.println("3333");
			Cursor cursor = db.rawQuery("select * from "+tableName, null);
			System.out.println("4444");
			List<UserInfoDemo> list = new ArrayList<UserInfoDemo>();
			System.out.println("%5555");
			System.out.println("cursorDBPerform=" + cursor);
			if (cursor != null) {
				System.out.println("66666");
				System.out.println("cursor1111+="+cursor.getCount());
				while (cursor.moveToNext()) {
					System.out.println("77777");
					System.out.println("cursor222");
					UserInfoDemo userInfoDemo = new UserInfoDemo();
					userInfoDemo.setUserId(cursor.getInt(cursor.getColumnIndex(DBCreateWord.ID)));
					userInfoDemo.setUserSchoolNum(cursor.getString(cursor.getColumnIndex(DBCreateWord.USER_SCHOOLNUM)));
					userInfoDemo.setUserName(cursor.getString(cursor.getColumnIndex(DBCreateWord.USER_NAME)));
					userInfoDemo.setUserPassword(cursor.getString(cursor.getColumnIndex(DBCreateWord.USER_PASS)));
					userInfoDemo.setUserNike(cursor.getString(cursor.getColumnIndex(DBCreateWord.USER_NIKE)));
					userInfoDemo.setUserSex(cursor.getString(cursor.getColumnIndex(DBCreateWord.USER_SEX)));
					userInfoDemo.setUserGrade(cursor.getString(cursor.getColumnIndex(DBCreateWord.USER_GRADE)));
					userInfoDemo.setUserPictureAd(cursor.getString(cursor.getColumnIndex(DBCreateWord.USER_PICTURE)));
					list.add(userInfoDemo);
				}
			}
			cursor.close();
			db.close();
			return list;
		}

		//修改用户信息
		/*
		 * 1:修改成功
		 * -1:修改失败
		 * */
		public static int updateUserTable(UserInfoDemo userInfoDemo, Context context){
			int flag = -1;
			if(userInfoDemo != null){
				DBHelper dbHelper = new DBHelper(context.getApplicationContext());
				db = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(DBCreateWord.USER_SCHOOLNUM, userInfoDemo.getUserSchoolNum());
				values.put(DBCreateWord.USER_NAME, userInfoDemo.getUserName());
				values.put(DBCreateWord.USER_PASS, userInfoDemo.getUserPassword());
				values.put(DBCreateWord.USER_NIKE, userInfoDemo.getUserNike());
				values.put(DBCreateWord.USER_SEX, userInfoDemo.getUserSex());
				values.put(DBCreateWord.USER_GRADE, userInfoDemo.getUserGrade());
				values.put(DBCreateWord.USER_PICTURE, userInfoDemo.getUserPictureAd());
				db.update(DBCreateWord.TB_USER, values, DBCreateWord.ID+"=?", new String[]{String.valueOf(userInfoDemo.getUserId())});
				db.close();
				flag = 1;
			}else{
//				Toast.makeText(context, "修改用户信息失败	", Toast.LENGTH_SHORT).show();
				flag = -1;
			}
			return flag;
		}
		
}
