package com.CollegeState.Data;

public class UserOrderFormInfoBean {
	private int orderItemId;// 订单项ID
	private int orderItemState;// 订单项状态
	private String goodsPrice;// 商品价格
	private String shopName;// 店铺名称
	private String userId;// 用户id
	private String goodsName;// 商品名称
	private String goodsId;// 商品id
	private int goodsCount;// 商品数量

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getOrderItemState() {
		return orderItemState;
	}

	public void setOrderItemState(int orderItemState) {
		this.orderItemState = orderItemState;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	@Override
	public String toString() {
		return "UserOrderFormInfoBean [orderItemId=" + orderItemId
				+ ", orderItemState=" + orderItemState + ", goodsPrice="
				+ goodsPrice + ", shopName=" + shopName + ", userId=" + userId
				+ ", goodsName=" + goodsName + ", goodsId=" + goodsId
				+ ", goodsCount=" + goodsCount + "]";
	}

}
