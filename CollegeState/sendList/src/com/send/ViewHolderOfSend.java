package com.send;

import android.widget.TextView;

public class ViewHolderOfSend {
	private int id;
	private TextView orderPhone;
	private TextView orderConsumeTime; 
	private TextView handMoney; 
	private TextView orderAddress;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public TextView getOrderPhone() {
		return orderPhone;
	}
	public void setOrderPhone(TextView orderPhone) {
		this.orderPhone = orderPhone;
	}
	public TextView getOrderConsumeTime() {
		return orderConsumeTime;
	}
	public void setOrderConsumeTime(TextView orderConsumeTime) {
		this.orderConsumeTime = orderConsumeTime;
	}
	public TextView getHandMoney() {
		return handMoney;
	}
	public void setHandMoney(TextView handMoney) {
		this.handMoney = handMoney;
	}
	public TextView getOrderAddress() {
		return orderAddress;
	}
	public void setOrderAddress(TextView orderAddress) {
		this.orderAddress = orderAddress;
	}
	
	
}
