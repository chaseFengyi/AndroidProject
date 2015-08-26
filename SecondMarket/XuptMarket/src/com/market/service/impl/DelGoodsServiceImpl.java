package com.market.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.market.dao.impl.SelectInfoDao;
import com.market.dao.impl.UpdateInfoDao;

public class DelGoodsServiceImpl {

	public static boolean delGoods(int goodsId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		data = SelectInfoDao.selectGoodsInfo("goodsId", goodsId);
		int state = Integer.parseInt(data.get(0).get("isSale").toString());
		if (state == 0) {
			UpdateInfoDao
					.updateGoodsInfo(
							goodsId,
							data.get(0).get("goodsName").toString(),
							data.get(0).get("goodsTypeId").toString(),
							data.get(0).get("goodsDescribe").toString(),
							data.get(0).get("goodsPrice").toString(),
							Integer.parseInt(data.get(0).get("goodsWanted")
									.toString()),
							Integer.parseInt(data.get(0).get("userId")
									.toString()), 1,
							data.get(0).get("goodsPublishTime").toString(),
							data.get(0).get("goodsConnect").toString());
			return true;
		} else {
			return false;
		}
	}
}
