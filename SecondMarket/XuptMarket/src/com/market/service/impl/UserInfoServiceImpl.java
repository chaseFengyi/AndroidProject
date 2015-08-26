package com.market.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.market.dao.impl.AddInfoDao;
import com.market.dao.impl.SelectInfoDao;
import com.market.model.StringBean;
import com.market.model.UserInfoBean;

public class UserInfoServiceImpl {

	/**
	 * 通过学号,判断该用户是否存在
	 * 
	 * @param account
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws DaoException
	 */
	public static UserInfoBean checkByUserSchoolNumberAnduserPassword(
			UserInfoBean userInfo) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		data = SelectInfoDao.selectUserInfo("userSchoolNum",
				userInfo.getUserSchoolNum());
		if (data.size() == 0) {
			userInfo.setUserSchoolNum(null);
			return userInfo;
		} else if (data.get(0).get("userPassword").toString()
				.equals(userInfo.getUserPassword())) {
			userInfo.setUserId(data.get(0).get("userId").toString());
			userInfo.setUserName(data.get(0).get("userName").toString());
			if (data.get(0).get("userPictureAD") != null)
				userInfo.setUserPictureAd(data.get(0).get("userPictureAD")
						.toString());
		} else {
			userInfo.setUserPassword(null);
		}
		return userInfo;
	}

	// 向数据库写入用户信息
	public static UserInfoBean regUser(UserInfoBean userInfo)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		boolean flag = AddInfoDao.addUserInfo(userInfo.getUserSchoolNum(),
				userInfo.getUserName(), userInfo.getUserPassword(), null, null, null, null);
		if (flag == true)
			userInfo = checkByUserSchoolNumberAnduserPassword(userInfo);
		return userInfo;
	}
}
