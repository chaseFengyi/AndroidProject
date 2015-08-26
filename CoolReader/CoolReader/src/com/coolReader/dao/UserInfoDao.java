package com.coolReader.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract.CommonDataKinds.Email;

import com.coolReader.Bean.UserInfoBean;
import com.coolReader.Bean.UserTableBean;

public class UserInfoDao {
	// 用户表的添加操作
	/**
	 * @param userInfoBean
	 * @return
	 */
	public static void insertUserInfo(SQLiteDatabase db,
			UserInfoBean userInfoBean) {

		String sql = "replace into " + TablesName.USER_TABLE + "("
				+ UserTableBean.UNAME + "," + UserTableBean.UPASS + ","
				+ UserTableBean.UEMAIL + "," + UserTableBean.USEX + ","
				+ UserTableBean.UREGISTERDATE + "," + UserTableBean.UPICTURE
				+ ") values(?,?,?,?,?,?)";
		db.execSQL(
				sql,
				new String[] { userInfoBean.getUserName(),
						userInfoBean.getUserPass(),
						userInfoBean.getUserEmail(), userInfoBean.getUserSex(),
						userInfoBean.getUserRegisterDate(),
						userInfoBean.getUserPicture() });
	}
	
	//用户表的删除操作
	/**
	 * @param db
	 * @param id 根据email删除
	 * @return  the number of rows affected by this SQL statement execution.
	 */
	public static int deleteUserInfo(SQLiteDatabase db,  String email){
		return db.delete(TablesName.USER_TABLE, UserTableBean.UEMAIL+"=?", new String[]{email});
	}
	
	//用户表的修改操作
	/**
	 * @param db
	 * @param userInfoBean
	 * @return the number of rows affected
	 */
	public static int updateUserInfo(SQLiteDatabase db, UserInfoBean userInfoBean){
		ContentValues values = new ContentValues();
		values.put(UserTableBean.UNAME, userInfoBean.getUserName());
		values.put(UserTableBean.UPASS, userInfoBean.getUserPass());
		values.put(UserTableBean.UEMAIL, userInfoBean.getUserEmail());
		values.put(UserTableBean.USEX, userInfoBean.getUserSex());
		values.put(UserTableBean.UREGISTERDATE, userInfoBean.getUserRegisterDate());
		values.put(UserTableBean.UPICTURE, userInfoBean.getUserPicture());
		return db.update(TablesName.USER_TABLE, values, UserTableBean.UID+"=?", new String[]{String.valueOf(userInfoBean.getUserId())});
	}
	
	//查找表中所有数据
	public static List<UserInfoBean> queryUserInfo(SQLiteDatabase db){
		List<UserInfoBean> users = null;
		Cursor cursor = db.query(TablesName.USER_TABLE, null, null, null, null, null, null);
		if(cursor != null){
			users = new ArrayList<UserInfoBean>();
			while(cursor.moveToNext()){
				UserInfoBean userInfoBean = new UserInfoBean();
				userInfoBean.setUserId(cursor.getInt(cursor.getColumnIndex(UserTableBean.UID)));
				userInfoBean.setUserName(cursor.getString(cursor.getColumnIndex(UserTableBean.UNAME)));
				userInfoBean.setUserPass(cursor.getString(cursor.getColumnIndex(UserTableBean.UPASS)));
				userInfoBean.setUserEmail(cursor.getString(cursor.getColumnIndex(UserTableBean.UEMAIL)));
				userInfoBean.setUserSex(cursor.getString(cursor.getColumnIndex(UserTableBean.USEX)));
				userInfoBean.setUserRegisterDate(cursor.getString(cursor.getColumnIndex(UserTableBean.UREGISTERDATE)));
				userInfoBean.setUserPicture(cursor.getString(cursor.getColumnIndex(UserTableBean.UPICTURE)));
				
				users.add(userInfoBean);
			}
		}
		

		closeSursor(cursor);
		
		return users;
	}
	
	//查找指定email的数据
	public static UserInfoBean queryUserInfoByEmail(SQLiteDatabase db, String email){
		Cursor cursor = db.query(TablesName.USER_TABLE, null, UserTableBean.UEMAIL+"=?", new String[]{email}, null, null, null);
		
		UserInfoBean userInfoBean = null;
		if(cursor != null && cursor.moveToFirst()){
			userInfoBean = new UserInfoBean();
			userInfoBean.setUserId(cursor.getInt(cursor.getColumnIndex(UserTableBean.UID)));
			userInfoBean.setUserName(cursor.getString(cursor.getColumnIndex(UserTableBean.UNAME)));
			userInfoBean.setUserPass(cursor.getString(cursor.getColumnIndex(UserTableBean.UPASS)));
			userInfoBean.setUserEmail(cursor.getString(cursor.getColumnIndex(UserTableBean.UEMAIL)));
			userInfoBean.setUserRegisterDate(cursor.getString(cursor.getColumnIndex(UserTableBean.UREGISTERDATE)));
			userInfoBean.setUserPicture(cursor.getString(cursor.getColumnIndex(UserTableBean.UPICTURE)));
		}
		closeSursor(cursor);
		
		return userInfoBean;
	}
	
	//查找指定email的数据
	public static UserInfoBean queryUserInfoByID(SQLiteDatabase db, int id){
		Cursor cursor = db.query(TablesName.USER_TABLE, null, UserTableBean.UID+"=?", new String[]{String.valueOf(id)}, null, null, null);
		
		UserInfoBean userInfoBean = null;
		if(cursor != null && cursor.moveToFirst()){
			userInfoBean = new UserInfoBean();
			userInfoBean.setUserId(cursor.getInt(cursor.getColumnIndex(UserTableBean.UID)));
			userInfoBean.setUserName(cursor.getString(cursor.getColumnIndex(UserTableBean.UNAME)));
			userInfoBean.setUserPass(cursor.getString(cursor.getColumnIndex(UserTableBean.UPASS)));
			userInfoBean.setUserEmail(cursor.getString(cursor.getColumnIndex(UserTableBean.UEMAIL)));
			userInfoBean.setUserRegisterDate(cursor.getString(cursor.getColumnIndex(UserTableBean.UREGISTERDATE)));
			userInfoBean.setUserPicture(cursor.getString(cursor.getColumnIndex(UserTableBean.UPICTURE)));
		}
		closeSursor(cursor);
		
		return userInfoBean;
	}
	

	public static void closeSursor(Cursor cursor){
		if(cursor != null)
			cursor.close();
	}
	
}
