package com.coolReader.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class URLDBHelper extends SQLiteOpenHelper {

	private URLDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.beginTransaction();
		try{
			db.execSQL(CreateWordDAO.sql_urltable);
		}finally{
			db.endTransaction();
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(CreateWordDAO.drop_urltable);
		onCreate(db);
	}
	
	private static URLDBHelper mInstance = null;
	
	public synchronized static URLDBHelper getInstance(Context context, String name, CursorFactory factory,
			int version){
		if(mInstance == null){
			mInstance = new URLDBHelper(context, name, factory, version);
		}
		return mInstance;
	}

}
