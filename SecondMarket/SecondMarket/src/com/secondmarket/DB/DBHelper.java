package com.secondmarket.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
	public static final int version = 1;
	public static final String DATABASE_NAME = "secondmarket";//数据库名称
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL(DBCreateWord.sqlCollect);
		arg0.execSQL(DBCreateWord.sqlPublish);
		arg0.execSQL(DBCreateWord.sqlUser);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		// 删除数据库之前，做数据备份
		arg0.execSQL(DBCreateWord.dropCollect);
		arg0.execSQL(DBCreateWord.dropPublic);
		arg0.execSQL(DBCreateWord.dropUser);
		onCreate(arg0);
	}

}
