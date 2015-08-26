package com.coolReader.dao;

public class TablesName {
	//数据库名
	public static String DATABASE_NAME = "Cool_Db";
	
	//版本号
	public static int VERSION = 1;
	
	//用户表
	public static String USER_TABLE = "user_info";
	
	//URL表\文章信息表
	public static String URL_TABLE = "url_content_info";
	
	//存档信息表
	public static String SAVE_TABLE = "save_info";
	
	//存档表的触发器插入表
	public static String TRIGGER_SAVE_INSERT = "trigger_save_insert";
	
	//用户表的索引
	public static String INDEX_USER = "index_user";
	
	//url表的索引
	public static String INDEX_URL = "index_url";
	
	//save表的唯一索引
	public static String UNIQUE_INDEX_SAVE = "unique_index_save";
}
