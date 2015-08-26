package com.secondmarket.collect;


public class BookInfo {
	private boolean isChecked;
	private String bookPicture;
	private String bookName;
	private String bookIntroduce;
	private String bookMoney;
	private int goodsId;
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public boolean getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getBookPicture() {
		return bookPicture;
	}
	public void setBookPicture(String bookPicture) {
		this.bookPicture = bookPicture;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getBookIntroduce() {
		return bookIntroduce;
	}
	public void setBookIntroduce(String bookIntroduce) {
		this.bookIntroduce = bookIntroduce;
	}
	public String getBookMoney() {
		return bookMoney;
	}
	public void setBookMoney(String bookMoney) {
		this.bookMoney = bookMoney;
	}
	@Override
	public String toString() {
		return "BookInfo [isChecked=" + isChecked + ", bookPicture="
				+ bookPicture + ", bookName=" + bookName + ", bookIntroduce="
				+ bookIntroduce + ", bookMoney=" + bookMoney + ", goodsId="
				+ goodsId + "]";
	}
	
	
}
