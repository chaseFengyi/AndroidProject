package com.CollegeState.Data;

public class UserOrderFormInfoBean {
	private int orderItemId;// ������ID
	private int orderItemState;// ������״̬
	private String goodsPrice;// ��Ʒ�۸�
	private String shopName;// ��������
	private String userId;// �û�id
	private String goodsName;// ��Ʒ����
	private String goodsId;// ��Ʒid
	private int goodsCount;// ��Ʒ����

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
