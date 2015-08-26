package com.secondmarket.DB;

public class DBCreateWord {
	public static final String TB_NAME = "collectTable";
	public static final String TB_PUBNAME = "publishTable";
	public static final String TB_USER = "userTable";
	public static final String ID = "_id";
	public static final String GOODSNAME = "goodsName";
	public static final String GOODSTYPEID = "goodsTypeId";
	public static final String GOODSDESCRIBE = "goodsDescribe";
	public static final String GOODSPRICE = "goodsPrice";
	public static final String GOODSWANTED = "goodsWanted";
	public static final String GOODSPUBLISHTIME = "goodsPublishTime";
	public static final String GOODSCONNECT = "goodsConnect";
	public static final String ISSALE = "isSale";
	public static final String USERID = "userId";
	public static final String GOODSPICTUREAD1 = "goodsPictureAD1";
	public static final String GOODSPICTUREAD2 = "goodsPictureAD2";
	public static final String GOODSPICTUREAD3 = "goodsPictureAD3";
	
	
	public static final String USER_SCHOOLNUM = "userSchoolNum";
	public static final String USER_NAME = "userName";
	public static final String USER_PASS = "userPassword";
	public static final String USER_NIKE = "userNike";
	public static final String USER_SEX = "userSex";
	public static final String USER_GRADE = "userGrade";
	public static final String USER_PICTURE = "userPicture";
	

	//创建我的收藏表语句
	public final static String sqlCollect = "create table if not exists "
			+ TB_NAME + "(" + ID
			+ " Integer primary key," + GOODSNAME
			+ " char(20) not null," + GOODSTYPEID
			+ " char(12) not null," + GOODSDESCRIBE
			+ " char(200) not null," + GOODSPRICE
			+ " char(20) not null," + GOODSWANTED + " int not null,"
			+ USERID + " int(12)," + ISSALE
			+ " int check(isSale in ('0','1'))," + GOODSPUBLISHTIME
			+ " char(20) not null," + GOODSCONNECT
			+ " char(30) not null," + GOODSPICTUREAD1
			+ " char(30) not null," + GOODSPICTUREAD2 + " char(30),"
			+ GOODSPICTUREAD3 + " char(30)" + ")";
	//创建我的发布表语句
	public final static String sqlPublish = "create table if not exists "
			+ TB_PUBNAME + "(" + ID + " Integer primary key,"
			+ GOODSNAME + " char(20) not null," + GOODSTYPEID
			+ " char(12) not null," + GOODSDESCRIBE
			+ " char(200) not null," + GOODSPRICE
			+ " char(20) not null," + GOODSWANTED + " int not null,"
			+ USERID + " int(12)," + ISSALE
			+ " int check(isSale in ('0','1'))," + GOODSPUBLISHTIME
			+ " char(20) not null," + GOODSCONNECT
			+ " char(30) not null," + GOODSPICTUREAD1
			+ " char(30) not null," + GOODSPICTUREAD2 + " char(30),"
			+ GOODSPICTUREAD3 + " char(30)" + ")";
	
	//创建用户信息表
	public final static String sqlUser = "create table if not exists "
			+ TB_USER + "(" + ID + " Integer primary key,"
			+ USER_SCHOOLNUM + " char(20) not null," + USER_NAME
			+ " char(12) not null," + USER_PASS
			+ " char(200) not null," + USER_NIKE
			+ " char(20)," + USER_SEX + " CHAR(10),"
			+ USER_GRADE + " CHAR(20),"  + USER_PICTURE
			+ " char(50)" +  ")";
	
	public final static String dropCollect = "drop table if exists"
			+ TB_NAME;
	public final static String dropPublic = "drop table if exists"
			+ TB_PUBNAME;
	public final static String dropUser = "drop table if exists"
			+TB_USER;
}
