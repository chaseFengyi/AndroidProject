package com.mynutritionstreet.Bean;

import java.util.List;

public class GoodInfoBean {
	private int id;
	private String goodsName;
	private String nutritionEffects;
	private String suitablePerson;
	private String step;
	private String publishTime;
	private String goodsPhoto;
	private int userId;
	List<TypeInfoBean> types;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getNutritionEffects() {
		return nutritionEffects;
	}
	public void setNutritionEffects(String nutritionEffects) {
		this.nutritionEffects = nutritionEffects;
	}
	public String getSuitablePerson() {
		return suitablePerson;
	}
	public void setSuitablePerson(String suitablePerson) {
		this.suitablePerson = suitablePerson;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getGoodsPhoto() {
		return goodsPhoto;
	}
	public void setGoodsPhoto(String goodsPhoto) {
		this.goodsPhoto = goodsPhoto;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public List<TypeInfoBean> getTypes() {
		return types;
	}
	public void setTypes(List<TypeInfoBean> types) {
		this.types = types;
	}
	@Override
	public String toString() {
		return "GoodInfoBean [id=" + id + ", goodsName=" + goodsName
				+ ", nutritionEffects=" + nutritionEffects
				+ ", suitablePerson=" + suitablePerson + ", step=" + step
				+ ", publishTime=" + publishTime + ", goodsPhoto=" + goodsPhoto
				+ ", userId=" + userId + ", types=" + types + "]";
	}
	
}
