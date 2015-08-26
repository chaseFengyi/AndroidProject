package com.market.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateInfoDao {
	static Connection conn = null;
	static java.sql.PreparedStatement ps = null;
	static ResultSet rs = null;
	static String className = "com.market.dao.impl.PublicInfoDao";
	static PublicInfoDao info = null;
	static String sql = null;

	// 添加用户信息
	public static boolean updateUserInfo(String userId, String userSchoolNum,
			String userName, String userPassword, String userNike,
			String userSex, String userGrade, String userPictureAD) {
		boolean flag = false;

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "update "
					+ CreateWordDao.USER_TABLE_NAME
					+ " set userSchoolNum=?,"
					+ "userName=?,userPassword=?,userNike=?,userSex=?,userGrade=?,userPictureAD=?"
					+ " where userId=" + Integer.parseInt(userId) + ";";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userSchoolNum);
			ps.setString(2, userName);
			ps.setString(3, userPassword);
			ps.setString(4, userNike);
			ps.setString(5, userSex);
			ps.setString(6, userGrade);
			ps.setString(7, userPictureAD);
			ps.executeUpdate();

			flag = true;
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
		return flag;
	}

	// 添加商品信息
	public static boolean updateGoodsInfo(int goodsId, String goodsName,
			String goodsTypeId, String goodsDescribe, String goodsPrice,
			int goodsWanted, int userId, int isSale, String goodsPublishTime,
			String goodsConnect) {
		boolean flag = false;

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "update "
					+ CreateWordDao.GOODS_TABLE_NAME
					+ " set goodsName=?,goodsTypeId=?,goodsDescribe=?,"
					+ "goodsPrice=?,goodsWanted=?,userId=?,isSale=?,goodsPublishTime=?,goodsConnect=? "
					+ "where goodsId=" + goodsId + ";";
			ps = conn.prepareStatement(sql);
			ps.setString(1, goodsName);
			ps.setString(2, goodsTypeId);
			ps.setString(3, goodsDescribe);
			ps.setString(4, goodsPrice);
			ps.setInt(5, goodsWanted);
			ps.setInt(6, userId);
			ps.setInt(7, isSale);
			ps.setString(8, goodsPublishTime);
			ps.setString(9, goodsConnect);
			ps.executeUpdate();

			flag = true;
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
		return flag;
	}

	// 添加类别信息
	public static boolean updateTypeInfo(String goodsTypeId,
			String goodsTypeName) {
		boolean flag = false;

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "update " + CreateWordDao.TYPE_TABLE_NAME
					+ " set goodsTypeName=?" + " where goodsTypeId="
					+ goodsTypeId + ";";
			ps = conn.prepareStatement(sql);
			ps.setString(1, goodsTypeName);
			ps.executeUpdate();

			flag = true;
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
		return flag;
	}

	// 添加图形表信息
	public static boolean updatePictureInfo(int goodsPictureId, int userId,
			String userPictureAD) {
		boolean flag = false;

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "update " + CreateWordDao.TYPE_TABLE_NAME + " set userId=?,"
					+ "userPictureAD=? where goodsPictureId=" + goodsPictureId
					+ ";";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setString(2, userPictureAD);
			ps.executeUpdate();

			flag = true;
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
		return flag;
	}
}
