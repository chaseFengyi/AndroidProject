package com.market.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.market.dao.impl.PublicInfoDao;

public class CreateTableDao {
	private static String username = "root";
	private static String password = "123456";
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/xuptmarket";

	static Connection conn = null;
	static java.sql.Statement st = null;
	static String className = "com.market.dao.impl.PublicInfoDao";
	static PublicInfoDao info = null;

	// �������û�������Ϣ��
	// �������ݿ�������true�������ɹ������򣬴���ʧ��
	public static boolean createTable(String createWord, String tableName) {
		boolean flag = false;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			st = conn.createStatement();
			// �жϸñ��Ƿ������
			String haveForeign = "show databases like \'" + tableName + "\'";
			st.executeUpdate(haveForeign);
			String set = "set foreign_key_checks=0;";
			st.executeUpdate(set);
			// ����ʱ�ȼ�����ݿ��и����Ƿ��Ѿ����ڣ������ڣ���ɾ����
			String checkTable = "drop table if exists " + tableName + ";";
			// System.out.println("tablename="+tableName);
			st.executeUpdate(checkTable);
			st.executeUpdate(createWord);
			String seted = "set foreign_key_checks=1;";
			st.executeUpdate(seted);
			flag = true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;
	}
}
