package com.market.dao.impl;

public class CreateWordDao {
	public static String USER_TABLE_NAME = "userTable"; /* "�û�������Ϣ��" */
	public static String GOODS_TABLE_NAME = "goodsTable";/* "��Ʒ��Ϣ��" */
	public static String TYPE_TABLE_NAME = "typeTable"; /* "����"; */
	public static String PICTURE_TABLE_NAME = "pictureTable"; /* "ͼ�α�"; */
	public static String SC_USER = "sc_user";
	public static String SC_TYPE = "sc_type";

	// �������û�������Ϣ���ַ���
	public static String createUserWord = "create table " + USER_TABLE_NAME
			+ "(" + "userId int(12) primary key AUTO_INCREMENT,"
			+ "userSchoolNum char(8) not null," + "userName char(20) not null,"
			+ "userPassword char(16) not null," + "userNike char(100),"
			+ "userSex char(5)," + "userGrade char(10),"
			+ "userPictureAD char(100))default charset=utf8;";

	// �����û�������Ϣ������
	// ��ɾ���ñ��ʱ����ɾ����֮�����ı��е�����
	public static String createUserTrigger = "create trigger " + SC_USER
			+ " after delete on " + USER_TABLE_NAME + " for each row "
			+ "begin" + "delete from " + GOODS_TABLE_NAME + ","
			+ PICTURE_TABLE_NAME + " where userId in("
			+ "select deleted.userId from deleted)" + "end;";

	// ��������Ʒ��Ϣ���ַ���
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

	// ������������Ϣ
	public static String createTypeWord = "create table " + TYPE_TABLE_NAME
			+ "(" + "goodsTypeId char(12) primary key,"
			+ "goodsTypeName char(50) not null);";

	// ������������
	// ��ɾ�������ʱ���ȼ���ɾ����֮�����ı��е�����
	public static String createTypeTrigger = "create trigger " + SC_TYPE
			+ " after delete on " + TYPE_TABLE_NAME + " for each row "
			+ "begin " + "delete from " + TYPE_TABLE_NAME
			+ " where goodsTypeId in("
			+ "select delected.goodsTypeId from deleted);" + "end";

	// ������ͼ�α���Ϣ
	public static String createPictureWord = "create table "
			+ PICTURE_TABLE_NAME + "("
			+ "goodsPictureId int(12) primary key AUTO_INCREMENT,"
			+ "goodsId int(12)," + "goodsPictureAD char(100) not null,"
			+
			// "goodsPictureId int(12) not null," +
			"foreign key (goodsId) references " + GOODS_TABLE_NAME
			+ "(goodsId) on update cascade)default charset=utf8;";

	// �����������ݱ�
	public static boolean createAllTable() {
		boolean flag = false;

		if (CreateTableDao.createTable(createUserWord, USER_TABLE_NAME)) {
			if (CreateTableDao.createTable(createGoodsWord, GOODS_TABLE_NAME)) {
				if (CreateTableDao.createTable(createTypeWord, TYPE_TABLE_NAME)) {
					if (CreateTableDao.createTable(createPictureWord,
							PICTURE_TABLE_NAME)) {
						flag = true;
					} else {
						System.out.println("����ͼ�α�ʧ�ܣ�");
					}
				} else {
					System.out.println("��������ʧ�ܣ�");
				}
			} else {
				System.out.println("������Ʒ��Ϣ��ʧ�ܣ�");
			}
		} else {
			System.out.println("�����û�������Ϣ��ʧ�ܣ�");
		}

		return flag;
	}
}
