package com.CollegeState.Data;

import java.util.Date;

/**
 * ������Ϣ��Bean ����sql�е�Search table
 * 
 * @author zc
 */
public class GoodsInfoBean {
	public int goodsId;// ��ƷID
	public int shopId;// �ο����̱�
	public String goodsName; // ��Ʒ����
	public String goodsCategory; // ��Ʒ����������Ȳˣ���
	public String goodsImgUrl; // ��ƷͼƬ��ַ
	public String goodsContent; // ��Ʒ�������
	public String goodsAddtime; // ��Ʒ���ʱ��
	public int isPeisong; // ��Ʒ�Ƿ�֧������ 0��ʾ��֧������ 1��ʾ֧������
	public double goodsPrice; // ��Ʒ�۸�
	public double freight; // �˷�
	//public int goodsScan; // ��Ʒ�������
	public int goodsColleted; // ��Ʒ�ղش���
	public double goodsAssess; // ��Ʒ������
	public String shopName;//��������

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
