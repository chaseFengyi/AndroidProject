package com.CollegeState.Data;

import java.util.Date;

/**
 * 商铺信息的Bean 来自sql中的Search table
 * 
 * @author zc
 */
public class GoodsInfoBean {
	public int goodsId;// 商品ID
	public int shopId;// 参考商铺表
	public String goodsName; // 商品名字
	public String goodsCategory; // 商品所属类别，如热菜，汤
	public String goodsImgUrl; // 商品图片地址
	public String goodsContent; // 商品简介内容
	public String goodsAddtime; // 商品添加时间
	public int isPeisong; // 商品是否支持配送 0表示不支持配送 1表示支持配送
	public double goodsPrice; // 商品价格
	public double freight; // 运费
	//public int goodsScan; // 商品浏览次数
	public int goodsColleted; // 商品收藏次数
	public double goodsAssess; // 商品好评率
	public String shopName;//店铺名称

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsCategory() {
		return goodsCategory;
	}

	public void setGoodsCategory(String goodsCategory) {
		this.goodsCategory = goodsCategory;
	}

	public String getGoodsImgUrl() {
		return goodsImgUrl;
	}

	public void setGoodsImgUrl(String goodsImgUrl) {
		this.goodsImgUrl = goodsImgUrl;
	}

	public String getGoodsContent() {
		return goodsContent;
	}

	public void setGoodsContent(String goodsContent) {
		this.goodsContent = goodsContent;
	}

	public String getGoodsAddtime() {
		return goodsAddtime;
	}

	public void setGoodsAddtime(String goodsAddtime) {
		this.goodsAddtime = goodsAddtime;
	}

	public int getIsPeisong() {
		return isPeisong;
	}

	public void setIsPeisong(int isPeisong) {
		this.isPeisong = isPeisong;
	}

	public double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public double getFreight() {
		return freight;
	}

	public void setFreight(double freight) {
		this.freight = freight;
	}

//	public int getGoodsScan() {
//		return goodsScan;
//	}
//
//	public void setGoodsScan(int goodsScan) {
//		this.goodsScan = goodsScan;
//	}

	public int getGoodsColleted() {
		return goodsColleted;
	}

	public void setGoodsColleted(int goodsColleted) {
		this.goodsColleted = goodsColleted;
	}

	public double getGoodsAssess() {
		return goodsAssess;
	}

	public void setGoodsAssess(double goodsAssess) {
		this.goodsAssess = goodsAssess;
	}

	public void setShopName(String shopName) {
		// TODO Auto-generated method stub
		this.shopName = shopName;
	}

	public String getShopName() {
		return shopName;
	}

}
