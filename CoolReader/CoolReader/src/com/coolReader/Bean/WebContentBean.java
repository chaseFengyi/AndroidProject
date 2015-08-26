package com.coolReader.Bean;

import java.io.Serializable;

public class WebContentBean implements Serializable {
	private boolean isChecked;
	private int webId;
	private String webTitle;
	private String webUrl;
	private String webContent;
	private int webTag;
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public int getWebId() {
		return webId;
	}
	public void setWebId(int webId) {
		this.webId = webId;
	}
	public String getWebTitle() {
		return webTitle;
	}
	public void setWebTitle(String webTitle) {
		this.webTitle = webTitle;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getWebContent() {
		return webContent;
	}
	public void setWebContent(String webContent) {
		this.webContent = webContent;
	}
	public int getWebTag() {
		return webTag;
	}
	public void setWebTag(int webTag) {
		this.webTag = webTag;
	}
	@Override
	public String toString() {
		return "WebContentBean [isChecked=" + isChecked + ", webId=" + webId
				+ ", webTitle=" + webTitle + ", webUrl=" + webUrl
				+ ", webContent=" + webContent + ", webTag=" + webTag + "]";
	}
	
	
}
