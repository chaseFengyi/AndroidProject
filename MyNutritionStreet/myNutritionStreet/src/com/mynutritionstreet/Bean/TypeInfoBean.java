package com.mynutritionstreet.Bean;

public class TypeInfoBean {
	private int id;
	private String typeName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@Override
	public String toString() {
		return "TypeInfoBean [id=" + id + ", typeName=" + typeName + "]";
	}
	
}
