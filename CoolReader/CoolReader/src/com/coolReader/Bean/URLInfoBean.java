package com.coolReader.Bean;

import java.io.Serializable;

public class URLInfoBean implements Serializable {
	private int urlId;
	private String urlTitle;
	private String urlContent;
	//URLµÿ÷∑
	private String urlLink;
	private int urlTag;
	public int getUrlId() {
		return urlId;
	}
	public void setUrlId(int urlId) {
		this.urlId = urlId;
	}
	public String getUrlTitle() {
		return urlTitle;
	}
	public void setUrlTitle(String urlTitle) {
		this.urlTitle = urlTitle;
	}
	public String getUrlContent() {
		return urlContent;
	}
	public void setUrlContent(String urlContent) {
		this.urlContent = urlContent;
	}
	public String getUrlLink() {
		return urlLink;
	}
	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}
	public int getUrlTag() {
		return urlTag;
	}
	public void setUrlTag(int urlTag) {
		this.urlTag = urlTag;
	}
	@Override
	public String toString() {
		return "URLInfoBean [urlId=" + urlId + ", urlTitle=" + urlTitle
				+ ", urlContent=" + urlContent + ", urlLink=" + urlLink
				+ ", urlTag=" + urlTag + "]";
	}
	
	
}
