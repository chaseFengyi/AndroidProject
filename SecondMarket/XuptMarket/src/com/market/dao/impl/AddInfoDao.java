package com.market.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddInfoDao {
	static Connection conn = null;
	static java.sql.PreparedStatement ps = null;
	static ResultSet rs = null;
	static String className = "com.market.dao.impl.PublicInfoDao";
	static PublicInfoDao info = null;
	static String sql = null;

	// 添加用户信息
	// 如果添加成功，则返回true
	/**
	 * @param userId
	 *            用户id
	 * @param userSchoolNum
	 *            学号
	 * @param userName
	 *            姓名
	 * @param userPassword
	 *            密码
	 * @param userConnect
	 *            用户联系方式
	 * @param userPictureAD
	 *            用户头像
	 * @return
	 */
	public static boolean addUserInfo(String userSchoolNum, String userName,
			String userPassword, String userNike, String userSex,
			String userGrade, String userPictureAD) {
		boolean flag = false;

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "insert into "
					+ CreateWordDao.USER_TABLE_NAME
					+ "(userSchoolNum,"
					+ "userName,userPassword,userNike,userSex,userGrade,userPictureAD) values(?,?,?,?,?,?,?);";

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
	/**
	 * @param goodsName
	 *            商品名
	 * @param goodsTypeId
	 *            商品类型id
	 * @param goodsDescripe
	 *            商品描述信息
	 * @param goodsPrice
	 *            商品价格
	 * @param goodsWanted
	 *            是否有意向
	 * @param userId
	 * @param isSale
	 * @param goodsPublishTime
	 * @return
	 */
	public static boolean addGoodsInfo(String goodsName, String goodsTypeId,
			String goodsDescribe, String goodsPrice, int goodsWanted,
			int userId, int isSale, String goodsPublishTime, String goodsConnect) {
		boolean flag = false;

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "insert into "
					+ CreateWordDao.GOODS_TABLE_NAME
					+ "(goodsName,"
					+ "goodsTypeId,goodsDescribe,goodsPrice,goodsWanted,"
					+ "userId,isSale,goodsPublishTime,goodsConnect) values(?,?,?,?,?,?,?,?,?);";
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
	public static boolean addTypeInfo(String goodsTypeName) {
		boolean flag = false;

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "insert into " + CreateWordDao.TYPE_TABLE_NAME
					+ "(goodsTypeName" + ") values(?);";
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
	public static boolean addPictureInfo(int goodsId, String goodsPictureAD) {
		boolean flag = false;

		try {
			Class tc = Class.forName(className);
			info = (PublicInfoDao) tc.newInstance();
			conn = info.getConnection();
			sql = "insert into " + CreateWordDao.PICTURE_TABLE_NAME
					+ "(goodsId," + "goodsPictureAD) values(?,?);";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, goodsId);
			ps.setString(2, goodsPictureAD);
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
