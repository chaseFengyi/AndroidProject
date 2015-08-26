package com.market.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.market.dao.impl.SelectInfoDao;

public class IndistinctSearchServiceImpl {

	private static List<Map<String, Object>> data_goods = null;
	private static List<Map<String, Object>> data_picture = null;
	private static List<Map<String, Object>> data_json = null;
	private static Map<String, Object> map = null;

	public static List<Map<String, Object>> indistinctSearchByString(int page,
			String str) {
		data_goods = new ArrayList<Map<String, Object>>();
		data_picture = new ArrayList<Map<String, Object>>();
		data_json = new ArrayList<Map<String, Object>>();

		data_goods = SelectInfoDao.indistinctSelectGoodsInfo("goodsName", str);

		for (int i = 0; i < data_goods.size(); i++) {
			data_picture = findGoodsPictureByGoodsId(Integer
					.parseInt(data_goods.get(i).get("goodsId").toString()));
			map = new HashMap<String, Object>();
			map = data_goods.get(i);
			// 将商品图片地址存到list
			List<String> pictureList = new ArrayList<String>();
			for (int j = 0; j < data_picture.size(); j++) {
				pictureList.add(data_picture.get(j).get("goodsPictureAD")
						.toString());
			}
			JSONArray temp = JSONArray.fromObject(pictureList);
			map.put("goodsPictureAD", temp);
			data_json.add(map);
		}

		if (20 * (page - 1) <= data_json.size()) {
			for (int k = 20 * page; k < data_json.size();) {
				data_json.remove(k);
			}
			for (int k = 20 * (page - 1); k > 0; k--) {
				data_json.remove(0);
			}
			return data_json;
		} else {
			return null;
		}

	}

	private static List<Map<String, Object>> findGoodsPictureByGoodsId(
			int goodsId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		data = SelectInfoDao.selectPictureInfo("goodsId", goodsId);
		return data;
	}

}
