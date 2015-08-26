package com.secondmarket.collect;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
	private CheckBox checkBox;
	private ImageView imageView;
	private TextView bookName;
	private TextView bookIntroduce;
	private TextView bookMoney;
	public CheckBox getCheckBox() {
		return checkBox;
	}
	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}
	public ImageView getImageView() {
		return imageView;
	}
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	public TextView getBookName() {
		return bookName;
	}
	public void setBookName(TextView bookName) {
		this.bookName = bookName;
	}
	public TextView getBookIntroduce() {
		return bookIntroduce;
	}
	public void setBookIntroduce(TextView bookIntroduce) {
		this.bookIntroduce = bookIntroduce;
	}
	public TextView getBookMoney() {
		return bookMoney;
	}
	public void setBookMoney(TextView bookMoney) {
		this.bookMoney = bookMoney;
	}
	@Override
	public String toString() {
		return "ViewHolder [checkBox=" + checkBox + ", imageView=" + imageView
				+ ", bookName=" + bookName + ", bookIntroduce=" + bookIntroduce
				+ ", bookMoney=" + bookMoney + "]";
	}
	
	
}
