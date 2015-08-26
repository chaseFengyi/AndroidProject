package com.get;

public class GetOrderInfo {
	private String shopName;
	private String shopPhone;
	private String shopAddress;
	private double takeMoney;
	private int shopId;
	
	
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
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
	public String getShopAddress() {
		return shopAddress;
	}
	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}
	public double getTakeMoney() {
		return takeMoney;
	}
	public void setTakeMoney(double takeMoney) {
		this.takeMoney = takeMoney;
	}
	@Override
	public String toString() {
		return "GetOrderInfo [shopName=" + shopName + ", shopPhone="
				+ shopPhone + ", shopAddress=" + shopAddress + ", takeMoney="
				+ takeMoney + ", shopId=" + shopId + "]";
	}
	
	
	
	
	
}
