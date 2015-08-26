package com.allnotes.bean;

public class NoteClassInfoBean {
	private int noteClassId;
	private String noteClassName;
	private String noteClassDesc;
	private int noteReadNum;
	private int noteFlagNum;
	private int noteArticleNum;
	private String password;
	private int noteIspw;
	private int userId;
	public int getNoteClassId() {
		return noteClassId;
	}
	public void setNoteClassId(int noteClassId) {
		this.noteClassId = noteClassId;
	}
	public String getNoteClassName() {
		return noteClassName;
	}
	public void setNoteClassName(String noteClassName) {
		this.noteClassName = noteClassName;
	}
	public String getNoteClassDesc() {
		return noteClassDesc;
	}
	public void setNoteClassDesc(String noteClassDesc) {
		this.noteClassDesc = noteClassDesc;
	}
	public int getNoteReadNum() {
		return noteReadNum;
	}
	public void setNoteReadNum(int noteReadNum) {
		this.noteReadNum = noteReadNum;
	}
	public int getNoteFlagNum() {
		return noteFlagNum;
	}
	public void setNoteFlagNum(int noteFlagNum) {
		this.noteFlagNum = noteFlagNum;
	}
	public int getNoteArticleNum() {
		return noteArticleNum;
	}
	public void setNoteArticleNum(int noteArticleNum) {
		this.noteArticleNum = noteArticleNum;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getNoteIspw() {
		return noteIspw;
	}
	public void setNoteIspw(int noteIspw) {
		this.noteIspw = noteIspw;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "NoteClassInfoBean [noteClassId=" + noteClassId
				+ ", noteClassName=" + noteClassName + ", noteClassDesc="
				+ noteClassDesc + ", noteReadNum=" + noteReadNum
				+ ", noteFlagNum=" + noteFlagNum + ", noteArticleNum="
				+ noteArticleNum + ", password=" + password + ", noteIspw="
				+ noteIspw + ", userId=" + userId + "]";
	}
	
}
