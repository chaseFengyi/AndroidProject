package com.market.dao.impl;

public class CreateWordDao {
	public static String USER_TABLE_NAME = "userTable"; /* "用户个人信息表" */
	public static String GOODS_TABLE_NAME = "goodsTable";/* "商品信息表" */
	public static String TYPE_TABLE_NAME = "typeTable"; /* "类别表"; */
	public static String PICTURE_TABLE_NAME = "pictureTable"; /* "图形表"; */
	public static String SC_USER = "sc_user";
	public static String SC_TYPE = "sc_type";

	// 创建“用户个人信息表”字符串
	public static String createUserWord = "create table " + USER_TABLE_NAME
			+ "(" + "userId int(12) primary key AUTO_INCREMENT,"
			+ "userSchoolNum char(8) not null," + "userName char(20) not null,"
			+ "userPassword char(16) not null," + "userNike char(100),"
			+ "userSex char(5)," + "userGrade char(10),"
			+ "userPictureAD char(100))default charset=utf8;";

	// 创建用户个人信息表触发器
	// 当删除该表的时候，先删除与之关联的表中的属性
	public static String createUserTrigger = "create trigger " + SC_USER
			+ " after delete on " + USER_TABLE_NAME + " for each row "
			+ "begin" + "delete from " + GOODS_TABLE_NAME + ","
			+ PICTURE_TABLE_NAME + " where userId in("
			+ "select deleted.userId from deleted)" + "end;";

	// 创建“商品信息”字符串
	public static String createGoodsWord = "create table "
			+ GOODS_TABLE_NAME
			+ "("
			+ "goodsId int(12) primary key AUTO_INCREMENT,"
			+ "goodsName char(20) not null,"
			+ "goodsTypeId char(12) not null,"
			+ "goodsDescribe char(200) not null,"
			+
			// "goodsPictureId int(12) not null," +
			"goodsPrice char(20) not null," + "goodsWanted int not null,"
			+ "userId int(12)," + "isSale int check(isSale in ('0','1')),"
			+ "goodsPublishTime char(20) not null,"
			+ "goodsConnect char(30) not null,"
			+ "foreign key (userId) references " + USER_TABLE_NAME
			+ "(userId) on update cascade" + ")default charset=utf8;";

	// 创建“类别表”信息
	public static String createTypeWord = "create table " + TYPE_TABLE_NAME
			+ "(" + "goodsTypeId char(12) primary key,"
			+ "goodsTypeName char(50) not null);";

	// 创建类别表触发器
	// 当删除类别表的时候，先级联删除与之关联的表中的属性
	public static String createTypeTrigger = "create trigger " + SC_TYPE
			+ " after delete on " + TYPE_TABLE_NAME + " for each row "
			+ "begin " + "delete from " + TYPE_TABLE_NAME
			+ " where goodsTypeId in("
			+ "select delected.goodsTypeId from deleted);" + "end";

	// 创建“图形表”信息
	public static String createPictureWord = "create table "
			+ PICTURE_TABLE_NAME + "("
			+ "goodsPictureId int(12) primary key AUTO_INCREMENT,"
			+ "goodsId int(12)," + "goodsPictureAD char(100) not null,"
			+
			// "goodsPictureId int(12) not null," +
			"foreign key (goodsId) references " + GOODS_TABLE_NAME
			+ "(goodsId) on update cascade)default charset=utf8;";

	// 创建所有数据表
	public static boolean createAllTable() {
		boolean flag = false;

		if (CreateTableDao.createTable(createUserWord, USER_TABLE_NAME)) {
			if (CreateTableDao.createTable(createGoodsWord, GOODS_TABLE_NAME)) {
				if (CreateTableDao.createTable(createTypeWord, TYPE_TABLE_NAME)) {
					if (CreateTableDao.createTable(createPictureWord,
							PICTURE_TABLE_NAME)) {
						flag = true;
					} else {
						System.out.println("创建图形表失败！");
					}
				} else {
					System.out.println("创建类别表失败！");
				}
			} else {
				System.out.println("创建商品信息表失败！");
			}
		} else {
			System.out.println("创建用户个人信息表失败！");
		}

		return flag;
	}
}
