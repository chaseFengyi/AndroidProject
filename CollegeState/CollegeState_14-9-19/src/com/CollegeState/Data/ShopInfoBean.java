package com.CollegeState.Data;

/**
 * 商铺信息的Bean 来自sql中的shop table
 * 
 * @author zc
 * 
 */
public class ShopInfoBean {
	private int areaId;// 区域id
	private int serviceCategoty;// 主营类别
	private String shopAddress;// 商铺地址
	private double shopAssess;// 商铺评价
	private int shopCategory;// 商铺类别
	private String shopContent;// 商铺简介
	private int shopCredit;// 商铺积分
	private int shopId;// 商铺id
	private String shopImg;// 商铺图片地址
	private String shopName;// 商铺名称
	private String shopPhone;// 商铺电话
	private int shopSales;// 商铺销量
	private String shopState;// 商铺状态
	private int userId;// 商户id
	private String workTime;// 营业时间

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public int getServiceCategoty() {
		return serviceCategoty;
	}

	public void setServiceCategoty(int serviceCategoty) {
		this.serviceCategoty = serviceCategoty;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public double getShopAssess() {
		return shopAssess;
	}

	public void setShopAssess(double shopAssess) {
		this.shopAssess = shopAssess;
	}

	public int getShopCategory() {
		return shopCategory;
	}

	public void setShopCategory(int shopCategory) {
		this.shopCategory = shopCategory;
	}

	public String getShopContent() {
		return shopContent;
	}

	public void setShopContent(String shopContent) {
		this.shopContent = shopContent;
	}

	public int getShopCredit() {
		return shopCredit;
	}

	public void setShopCredit(int shopCredit) {
		this.shopCredit = shopCredit;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getShopImg() {
		return shopImg;
	}

	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopPhone() {
		return shopPhone;
	}

	public void setShopPhone(String shopPhone) {
		this.shopPhone = shopPhone;
	}

	public int getShopSales() {
		return shopSales;
	}

	public void setShopSales(int shopSales) {
		this.shopSales = shopSales;
	}

	public String getShopState() {
		return shopState;
	}

	public void setShopState(String shopState) {
		this.shopState = shopState;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	@Override
	public String toString() {
		return "ShopInfoBean [areaId=" + areaId + ", serviceCategoty="
				+ serviceCategoty + ", shopAdress=" + shopAddress
				+ ", shopAssess=" + shopAssess + ", shopCategory="
				+ shopCategory + ", shopContent=" + shopContent
				+ ", shopCredit=" + shopCredit + ", shopId=" + shopId
				+ ", shopImg=" + shopImg + ", shopName=" + shopName
				+ ", shopPhone=" + shopPhone + ", shopSales=" + shopSales
				+ ", shopState=" + shopState + ", userId=" + userId
				+ ", workTime=" + workTime + "]";
	}

}
