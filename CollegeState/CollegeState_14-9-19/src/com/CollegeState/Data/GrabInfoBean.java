package com.CollegeState.Data;

/**
 * 
 * @author F.Crazy
 * 
 */
public class GrabInfoBean {
	private int goodsId;// 商品ID
	private String goodsName;// 商品名字
	private String proAddTime;// 上架时间
	private int proGoodsCount;// 商品数量
	private double proGoodsPrice;// 商品价格
	private String proTime;// 截止日期
	private int promotionId;// 促销id
	private int shopId;// 店铺id
	private String shopName;// 店铺名称

	public GrabInfoBean() {

	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getProAddTime() {
		return proAddTime;
	}

	public void setProAddTime(String proAddTime) {
		this.proAddTime = proAddTime;
	}

	public int getProGoodsCount() {
		return proGoodsCount;
	}

	public void setProGoodsCount(int proGoodsCount) {
		this.proGoodsCount = proGoodsCount;
	}

	public double getProGoodsPrice() {
		return proGoodsPrice;
	}

	public void setProGoodsPrice(double proGoodsPrice) {
		this.proGoodsPrice = proGoodsPrice;
	}

	public String getProTime() {
		return proTime;
	}

	public void setProTime(String proTime) {
		this.proTime = proTime;
	}

	public int getPromotionId() {
		return promotionId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

}
