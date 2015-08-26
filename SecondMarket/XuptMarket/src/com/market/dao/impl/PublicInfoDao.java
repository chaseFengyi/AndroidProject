package com.market.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PublicInfoDao {
	private static String username = "root";
//	private static String password = "f82210578";
	private static String password = "123456";
	//private static String password = "liqian11201213";
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/xuptmarket";

	private static Connection conn;

	// ע������
	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("ע������ʧ��");
		}
	}

	// ��������
	public Connection getConnection() {
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	// �ͷ���Դ
	public void free(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			System.out.println("�ͷ���Դʧ��!");
			System.exit(-1);
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("�ͷ���Դʧ��!");
				System.exit(-1);
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					System.out.println("�ͷ���Դʧ��!");
					System.exit(-1);
					e.printStackTrace();
				}
			}
		}
	}
}
