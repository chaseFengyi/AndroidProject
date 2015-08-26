package com.CollegeState.Data;

/**
 * 
 * @author F.Crazy
 * 
 */
public class MyWealthInfoBean {
	private int goodsId;// 商品ID
	private int userId;// 用户id
	private int myGoodsCount;// 数量
	private int proGoodsCount;// 商品数量
	private double proGoodsPrice;// 商品价格
	private String proTime;// 截止日期
	private int wealthId;// 财富id
	private String goodsName;// 商品名称
	private String shopName;// 商铺名称

	public MyWealthInfoBean() {

	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getMyGoodsCount() {
		return myGoodsCount;
	}

	public void setMyGoodsCount(int myGoodsCount) {
		this.myGoodsCount = myGoodsCount;
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

	public int getWealthId() {
		return wealthId;
	}

	public void setWealthId(int wealthId) {
		this.wealthId = wealthId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

}
