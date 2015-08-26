package com.coolReader.Bean;

public class SaveInfoBean {
	private int saveId;
	private int urlId;
	private int userId;
	public int getSaveId() {
		return saveId;
	}
	public void setSaveId(int saveId) {
		this.saveId = saveId;
	}
	public int getUrlId() {
		return urlId;
	}
	public void setUrlId(int urlId) {
		this.urlId = urlId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "SaveInfoBean [saveId=" + saveId + ", urlId=" + urlId
				+ ", userId=" + userId + "]";
	}
	
}
