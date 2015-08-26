package com.send;

public class SendOrderInfo {
	private int orderId;
	private String orderPhone;//顾客电话
	private String orderConsumeTime;//顾客消费时间
	private String orderAddress;//地址
	private double handMoney;//收取金额
	private int pageNo;//页号
	
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getOrderPhone() {
		return orderPhone;
	}
	public void setOrderPhone(String orderPhone) {
		this.orderPhone = orderPhone;
	}
	public String getOrderConsumeTime() {
		return orderConsumeTime;
	}
	public void setOrderConsumeTime(String orderConsumeTime) {
		this.orderConsumeTime = orderConsumeTime;
	}
	public String getOrderAddress() {
		return orderAddress;
	}
	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}
	public double getHandMoney() {
		return handMoney;
	}
	public void setHandMoney(double handMoney) {
		this.handMoney = handMoney;
	}
	@Override
	public String toString() {
		return "SendOrderInfo [orderId=" + orderId + ", orderPhone="
				+ orderPhone + ", orderConsumeTime=" + orderConsumeTime
				+ ", orderAddress=" + orderAddress + ", handMoney=" + handMoney
				+ ", pageNo=" + pageNo + "]";
	}
	
	
	
	
}
