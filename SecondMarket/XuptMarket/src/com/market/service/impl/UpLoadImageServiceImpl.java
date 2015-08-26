package com.market.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.market.dao.impl.AddInfoDao;
import com.market.dao.impl.SelectInfoDao;
import com.market.dao.impl.UpdateInfoDao;

public class UpLoadImageServiceImpl {

	public static void upLoadUserImage(String userId, String userNike,
			String userSex, String userGrade, String imagePath) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		data = SelectInfoDao.selectUserInfo("userId", Integer.parseInt(userId));
		UpdateInfoDao.updateUserInfo(data.get(0).get("userId").toString(), data
				.get(0).get("userSchoolNum").toString(),
				data.get(0).get("userName").toString(),
				data.get(0).get("userPassword").toString(), userNike, userSex,
				userGrade, imagePath);
	}

	public static int upLoadGoodsInfo(String goodsName, String goodsTypeId,
			String goodsDescribe, String goodsPrice, int goodsWanted,
			int userId, int isSale, String goodsPublishTime, String goodsConnect) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		int goodsId;
		AddInfoDao.addGoodsInfo(goodsName, goodsTypeId, goodsDescribe,
				goodsPrice, goodsWanted, userId, isSale, goodsPublishTime,
				goodsConnect);
		data = SelectInfoDao.selectGoodsInfo("goodsDescribe", goodsDescribe);
		goodsId = Integer.parseInt(data.get(0).get("goodsId").toString());
		// AddInfoDao.addPictureInfo(goodsId, imagePath);
		// data = SelectInfoDao.selectPictureInfo("goodsPictureAD", imagePath);
		UpdateInfoDao.updateGoodsInfo(goodsId, goodsName, goodsTypeId,
				goodsDescribe, goodsPrice, goodsWanted, userId, isSale,
				goodsPublishTime, goodsConnect);
		return goodsId;
	}

	public static void upLoadGoodsImage(int goodsId, String imagePath) {

		AddInfoDao.addPictureInfo(goodsId, imagePath);
	}
}
