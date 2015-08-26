package com.CollegeState.Data;

/**
 * ������Ϣ��Bean ����sql�е�shop table
 * 
 * @author zc
 * 
 */
public class ShopInfoBean {
	private int areaId;// ����id
	private int serviceCategoty;// ��Ӫ���
	private String shopAddress;// ���̵�ַ
	private double shopAssess;// ��������
	private int shopCategory;// �������
	private String shopContent;// ���̼��
	private int shopCredit;// ���̻���
	private int shopId;// ����id
	private String shopImg;// ����ͼƬ��ַ
	private String shopName;// ��������
	private String shopPhone;// ���̵绰
	private int shopSales;// ��������
	private String shopState;// ����״̬
	private int userId;// �̻�id
	private String workTime;// Ӫҵʱ��

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
