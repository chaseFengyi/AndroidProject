package com.send;

public class AfterSendInfo {
	private int pageNo;
	private int goodsId;
	private String goodsName;
	private double goodsPrice;
	private int goodsCount;
	private String goodsMore;
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
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
	public double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public int getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}
	public String getGoodsMore() {
		return goodsMore;
	}
	public void setGoodsMore(String goodsMore) {
		this.goodsMore = goodsMore;
	}
	@Override
	public String toString() {
		return "AfterSendInfo [pageNo=" + pageNo + ", goodsId=" + goodsId
				+ ", goodsName=" + goodsName + ", goodsPrice=" + goodsPrice
				+ ", goodsCount=" + goodsCount + ", goodsMore=" + goodsMore
				+ "]";
	}
	
	
	
	
}
