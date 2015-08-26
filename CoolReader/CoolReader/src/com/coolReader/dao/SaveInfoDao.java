package com.coolReader.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolReader.Bean.SaveInfoBean;
import com.coolReader.Bean.SaveTableBean;

public class SaveInfoDao {
	// 存档的添加操作
	/**
	 * @param
	 * @return
	 */
	public static void insertSaveInfo(SQLiteDatabase db,
			SaveInfoBean saveInfoBean) {
		//用replace代替insert避免插入重复数据
		String sql = "insert or ignore into " + TablesName.SAVE_TABLE + "("
				+ SaveTableBean.U_ID + "," + SaveTableBean.URL_ID
				+ ") values(?,?)";
		db.execSQL(
				sql,
				new String[] { String.valueOf(saveInfoBean.getUserId()),
						String.valueOf(saveInfoBean.getUrlId()) });
	}

	// 存档的删除操作
	/**
	 * @param db
	 * @param id
	 *            根据email删除
	 * @return the number of rows affected by this SQL statement execution.
	 */
	public static int deleteSaveInfo(SQLiteDatabase db, int saveId) {
		return db.delete(TablesName.SAVE_TABLE, SaveTableBean.SAVE_ID + "=?",
				new String[] { String.valueOf(saveId) });
	}

	// 存档的修改操作
	/**
	 * @param db
	 * @param userInfoBean
	 * @return the number of rows affected
	 */
	public static int updateSaveInfo(SQLiteDatabase db,
			SaveInfoBean saveInfoBean) {
		ContentValues values = new ContentValues();
		values.put(SaveTableBean.U_ID, saveInfoBean.getUserId());
		values.put(SaveTableBean.URL_ID, saveInfoBean.getUrlId());
		return db.update(TablesName.SAVE_TABLE, values, SaveTableBean.SAVE_ID
				+ "=?",
				new String[] { String.valueOf(saveInfoBean.getSaveId()) });
	}

	// 查找表中所有数据
	public static List<SaveInfoBean> querySaveInfo(SQLiteDatabase db) {
		List<SaveInfoBean> saves = null;
		Cursor cursor = db.query(TablesName.SAVE_TABLE, null, null, null, null,
				null, null);
		if (cursor != null) {
			saves = new ArrayList<SaveInfoBean>();
			while (cursor.moveToNext()) {
				SaveInfoBean saveInfoBean = new SaveInfoBean();
				saveInfoBean.setSaveId(cursor.getInt(cursor
						.getColumnIndex(SaveTableBean.SAVE_ID)));
				saveInfoBean.setUrlId(cursor.getInt(cursor
						.getColumnIndex(SaveTableBean.URL_ID)));
				saveInfoBean.setUserId(cursor.getInt(cursor
						.getColumnIndex(SaveTableBean.U_ID)));
				saves.add(saveInfoBean);
			}
		}
		closeSursor(cursor);

		return saves;
	}

	// 查找指定id的数据
	public static SaveInfoBean querySaveInfoById(SQLiteDatabase db, long id) {
		Cursor cursor = db.query(TablesName.SAVE_TABLE, null,
				SaveTableBean.SAVE_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);

		SaveInfoBean saveInfoBean = null;
		if (cursor != null && cursor.moveToFirst()) {
			saveInfoBean = new SaveInfoBean();
			saveInfoBean.setSaveId(cursor.getInt(cursor
					.getColumnIndex(SaveTableBean.SAVE_ID)));
			saveInfoBean.setUrlId(cursor.getInt(cursor
					.getColumnIndex(SaveTableBean.URL_ID)));
			saveInfoBean.setUserId(cursor.getInt(cursor
					.getColumnIndex(SaveTableBean.U_ID)));
		}
		
		closeSursor(cursor);
		
		return saveInfoBean;
	}
	
	public static void closeSursor(Cursor cursor){
		if(cursor != null)
			cursor.close();
	}
	
}
