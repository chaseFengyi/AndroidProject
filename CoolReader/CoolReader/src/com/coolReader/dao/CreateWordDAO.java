package com.coolReader.dao;

import com.coolReader.Bean.SaveTableBean;
import com.coolReader.Bean.URLTableBean;
import com.coolReader.Bean.UserTableBean;

public class CreateWordDAO {
	// 创建用户表
	public final static String sql_usertable = "create table if not exists "
			+ TablesName.USER_TABLE + "(" + UserTableBean.UID
			+ " INTEGER primary key autoincrement," + UserTableBean.UNAME
			+ " varchar(32) not null," + UserTableBean.UPASS
			+ " varchar(32) not null," + UserTableBean.UEMAIL
			+ " varchar(32) not null," + UserTableBean.USEX + " varchar(8),"
			+ UserTableBean.UREGISTERDATE + " varchar(32),"
			+ UserTableBean.UPICTURE + " varchar(100)"
			+ ")";

	// 创建URL表
	public final static String sql_urltable = "create table if not exists "
			+ TablesName.URL_TABLE + "(" + URLTableBean.URL_ID
			+ " INTEGER primary key autoincrement," + URLTableBean.URL_TITLE
			+ " varchar(100) not null," + URLTableBean.URL_CONTENT
			+ " long text not null," + URLTableBean.URL_LINK
			+ " varchar(50) not null," + URLTableBean.URL_TAG
			+ " integer not null) ";

	// 创建存档表
	public final static String sql_savetable = "create table if not exists "
			+ TablesName.SAVE_TABLE + "(" + SaveTableBean.SAVE_ID
			+ " INTEGER primary key autoincrement,"
			+ SaveTableBean.URL_ID +" INTEGER,"
			+ SaveTableBean.U_ID +" INTEGER," 
			+"unique("+SaveTableBean.URL_ID+","+SaveTableBean.U_ID+"));";
	
	//在user表上创建userEmail的索引
	public final static String sql_index_user = "create index if not exists "
			+TablesName.INDEX_USER+" on "+TablesName.USER_TABLE 
			+ "("+UserTableBean.UEMAIL+");";
	
	//在url表傻瓜创建url的索引
	public final static String sql_index_url = "create index if not exists "
			+TablesName.INDEX_URL+" on "+TablesName.URL_TABLE
			+"("+URLTableBean.URL_LINK+");";
//	
//	//在savetable上创建urlid，userid的索引，从而在插入数据的时候，防止出现重复的
//	public final static String sql_unique_index_save = "create unique index "
//			+ TablesName.UNIQUE_INDEX_SAVE + " on "+TablesName.SAVE_TABLE +
//			"("+SaveTableBean.U_ID+","+SaveTableBean.URL_ID+");";
	
	/*public final static String sql_savetable = "create table if not exists "
			+ TablesName.SAVE_TABLE + "(" + SaveTableBean.SAVE_ID
			+ " INTEGER primary key autoincrement," + SaveTableBean.URL_ID
			+ URLTableBean.URL_ID +"INTEGER,"
			+ UserTableBean.UID +"INTEGER,"
			+ " foreign key (" + URLTableBean.URL_ID + ") references "
			+ TablesName.URL_TABLE + " (" + URLTableBean.URL_ID
			+ ") on update cascade," + " foreign key (" + UserTableBean.UID
			+ ") references " + TablesName.USER_TABLE + "(" + UserTableBean.UID
			+ ") on update cascade);";*/
	
	//创建触发器，当向存档表添加数据的时候，先判断usertable与urltable里面是否有内容，
	public final static String sql_tri_save_insert = "create trigger if not exists "+TablesName.TRIGGER_SAVE_INSERT
			+ " BEFORE INSERT "+" on " + TablesName.SAVE_TABLE
			+" FOR EACH ROW "
			+"BEGIN "
			+"SELECT RAISE (ROLLBACK,\'NO THIS UTL ITEM\') "
			+"WHERE (SELECT "+URLTableBean.URL_ID +" FROM "+TablesName.URL_TABLE
			+" WHERE "+URLTableBean.URL_ID+"=new."+SaveTableBean.URL_ID+")is null; "
			+"SELECT RAISE (ROLLBACK,\'NO THIS USER\')"
			+"WHERE (SELECT "+UserTableBean.UID +" FROM "+TablesName.USER_TABLE
			+" WHERE "+UserTableBean.UID+"=NEW."+SaveTableBean.U_ID+")IS NULL;"
			+"END;";
	
	
	// 删除用户表
	public final static String drop_usertable = "drop table if exists "
			+ TablesName.URL_TABLE;

	// 删除URL表
	public final static String drop_urltable = "drop table if exists "
			+ TablesName.URL_TABLE;

	// 删除存档表
	public final static String drop_savetable = "drop table if exists "
			+ TablesName.SAVE_TABLE;
	
	//删除user索引
	public static final String drop_index_user = "drop index "+TablesName.INDEX_USER;
	
	//删除url索引
	public static final String drop_index_url = "drop index "+TablesName.INDEX_URL;
	
	//删除触发器
	public final static String drop_tri_save_insert = "alter table "+TablesName.SAVE_TABLE+" disable trigger "+TablesName.TRIGGER_SAVE_INSERT+";";
}
