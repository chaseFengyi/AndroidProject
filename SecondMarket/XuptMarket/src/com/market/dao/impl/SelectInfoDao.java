package com.market.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectInfoDao {
	static Connection conn = null;
	static java.sql.PreparedStatement ps = null;
	static ResultSet rs = null;
	static String className = "com.market.dao.impl.PublicInfoDao";
	static PublicInfoDao info = null;
	static String sql = null;

	// 查找用户信息
	// name-->表示在数据库中的表中各个属性的属性名
	// des-->表示在name=des的条件下查找所有信息
	// 将查找到的信息内容存放在map中
	public static List<Map<String, Object>> selectUserInfo(
			String name, Object des) {
		Map<String, Object> data;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "select * from " + CreateWordDao.USER_TABLE_NAME + " where "
					+ name + "=?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, des);
			rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			String[] colName = new String[count];
			for (int i = 1; i <= count; i++) {
				colName[i - 1] = rsmd.getColumnLabel(i);
			}
			int j = 1;
			while (rs.next()) {
				data = new HashMap<String, Object>();
				for (int i = 0; i < colName.length; i++) {
					data.put(colName[i], rs.getObject(i + 1));
				}
				j = j + 1;
				list.add(data);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			info.free(conn, ps, rs);
		}
		return list;
	}

	// 查找商品信息
	public static java.util.List<Map<String, Object>> selectGoodsInfo(
			String name, Object des) {
		Map<String, Object> data;
		java.util.List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "select * from " + CreateWordDao.GOODS_TABLE_NAME + " where "
					+ name + "=?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, des);
			rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			String[] colName = new String[count];
			for (int i = 1; i <= count; i++) {
				colName[i - 1] = rsmd.getColumnLabel(i);
			}
			int j = 1;
			while (rs.next()) {
				data = new HashMap<String, Object>();
				for (int i = 0; i < colName.length; i++) {
					data.put(colName[i], rs.getObject(i + 1));
				}
				j = j + 1;
				list.add(data);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			info.free(conn, ps, rs);
		}
		return list;
	}

	// 模糊匹配查找商品信息
	public static java.util.List<Map<String, Object>> indistinctSelectGoodsInfo(
			String name, Object des) {
		Map<String, Object> data;
		java.util.List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "select * from " + CreateWordDao.GOODS_TABLE_NAME + " where "
					+ name + " LIKE '%" + des + "%'";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			String[] colName = new String[count];
			for (int i = 1; i <= count; i++) {
				colName[i - 1] = rsmd.getColumnLabel(i);
			}
			int j = 1;
			while (rs.next()) {
				data = new HashMap<String, Object>();
				for (int i = 0; i < colName.length; i++) {
					data.put(colName[i], rs.getObject(i + 1));
				}
				j = j + 1;
				list.add(data);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			info.free(conn, ps, rs);
		}
		return list;
	}

	// 按Id倒序查找商品信息
	public static java.util.List<Map<String, Object>> invertedSelectGoodsInfo(
			String name) {
		Map<String, Object> data;
		java.util.List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "select * from (select * from "
					+ CreateWordDao.GOODS_TABLE_NAME
					+ " where isSale = 0)T where " + name
					+ " order by goodsId desc limit 0,10;";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			String[] colName = new String[count];
			for (int i = 1; i <= count; i++) {
				colName[i - 1] = rsmd.getColumnLabel(i);
			}
			int j = 1;
			while (rs.next()) {
				data = new HashMap<String, Object>();
				for (int i = 0; i < colName.length; i++) {
					data.put(colName[i], rs.getObject(i + 1));
				}
				j = j + 1;
				list.add(data);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			info.free(conn, ps, rs);
		}
		return list;
	}

	// 查找类别信息
	/**
	 * @param name
	 *            属性名
	 * @param des
	 *            属性值
	 * @return
	 */
	public static java.util.List<Map<String, Object>> selectTypeInfo(
			String name, Object des) {
		Map<String, Object> data;
		java.util.List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "select * from " + CreateWordDao.TYPE_TABLE_NAME + " where "
					+ name + "=?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, des);
			rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			String[] colName = new String[count];
			for (int i = 1; i <= count; i++) {
				colName[i - 1] = rsmd.getColumnLabel(i);
			}
			int j = 1;
			while (rs.next()) {
				data = new HashMap<String, Object>();
				for (int i = 0; i < colName.length; i++) {
					data.put(colName[i], rs.getObject(i + 1));
				}
				j = j + 1;
				list.add(data);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			info.free(conn, ps, rs);
		}
		return list;
	}

	// 查找图形表信息
	public static java.util.List<Map<String, Object>> selectPictureInfo(
			String name, Object des) {
		Map<String, Object> data;
		java.util.List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "select * from " + CreateWordDao.PICTURE_TABLE_NAME
					+ " where " + name + "=?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, des);
			rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			String[] colName = new String[count];
			for (int i = 1; i <= count; i++) {
				colName[i - 1] = rsmd.getColumnLabel(i);
			}
			int j = 1;
			while (rs.next()) {
				data = new HashMap<String, Object>();
				for (int i = 0; i < colName.length; i++) {
					data.put(colName[i], rs.getObject(i + 1));
				}
				j = j + 1;
				list.add(data);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			info.free(conn, ps, rs);
		}
		return list;
	}
}
