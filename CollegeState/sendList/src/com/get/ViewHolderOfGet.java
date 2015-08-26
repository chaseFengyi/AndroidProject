package com.get;

import java.io.Serializable;

import android.widget.TextView;

@SuppressWarnings("serial")
public class ViewHolderOfGet implements Serializable {
	private TextView shopName;
	private TextView shopPhone;
	private TextView shopAddress;
	private TextView takeMoney;
	private int shopId;
	
	
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	
	public TextView getShopName() {
		return shopName;
	}
	public void setShopName(TextView shopName) {
		this.shopName = shopName;
	}
	public TextView getShopPhone() {
		return shopPhone;
	}
	public void setShopPhone(TextView shopPhone) {
		this.shopPhone = shopPhone;
	}
	public TextView getShopAddress() {
		return shopAddress;
	}
	public void setShopAddress(TextView shopAddress) {
		this.shopAddress = shopAddress;
	}
	public TextView getTakeMoney() {
		return takeMoney;
	}
	public void setTakeMoney(TextView takeMoney) {
		this.takeMoney = takeMoney;
	}
	@Override
	public String toString() {
		return "GetOrderInfo [shopName=" + shopName + ", shopPhone="
				+ shopPhone + ", shopAddress=" + shopAddress + ", takeMoney="
				+ takeMoney + ", shopId=" + shopId + "]";
	}
	
}
