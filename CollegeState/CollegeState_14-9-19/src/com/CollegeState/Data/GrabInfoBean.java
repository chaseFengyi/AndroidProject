package com.CollegeState.Data;

/**
 * 
 * @author F.Crazy
 * 
 */
public class GrabInfoBean {
	private int goodsId;// ��ƷID
	private String goodsName;// ��Ʒ����
	private String proAddTime;// �ϼ�ʱ��
	private int proGoodsCount;// ��Ʒ����
	private double proGoodsPrice;// ��Ʒ�۸�
	private String proTime;// ��ֹ����
	private int promotionId;// ����id
	private int shopId;// ����id
	private String shopName;// ��������

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
