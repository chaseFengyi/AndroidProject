package com.coolReader.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class SaveDBHelper extends SQLiteOpenHelper {

	private SaveDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.beginTransaction();
		try{
			db.execSQL(CreateWordDAO.sql_savetable);
			db.execSQL(CreateWordDAO.sql_tri_save_insert);
//			db.execSQL(CreateWordDAO.sql_unique_index_save);
		}finally{
			db.endTransaction();
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(CreateWordDAO.drop_savetable);
		db.execSQL(CreateWordDAO.drop_tri_save_insert);
		onCreate(db);
	}
	
	private static SaveDBHelper mInstance = null;
	
	public synchronized static SaveDBHelper getInstance(Context context, String name, CursorFactory factory,
			int version){
		if(mInstance == null){
			mInstance = new SaveDBHelper(context, name, factory, version);
		}
		
		return mInstance;
	}

}
