package com.mynutritionstreet.Bean;

import java.io.Serializable;

public class MaterialBean implements Serializable {
	private int materialId;
	private String materialName;
	private double materilaWeight;
	private String materialPicture;
	public int getMaterialId() {
		return materialId;
	}
	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public double getMaterilaWeight() {
		return materilaWeight;
	}
	public void setMaterilaWeight(double materilaWeight) {
		this.materilaWeight = materilaWeight;
	}
	public String getMaterialPicture() {
		return materialPicture;
	}
	public void setMaterialPicture(String materialPicture) {
		this.materialPicture = materialPicture;
	}
	@Override
	public String toString() {
		return "MaterialBean [materialId=" + materialId + ", materialName="
				+ materialName + ", materilaWeight=" + materilaWeight
				+ ", materialPicture=" + materialPicture + "]";
	}
	
}
