package com.allnotes.bean;

import java.util.List;
import java.util.Map;

public class NoteInfoBean {
	private int noteId;
	private int userId;
	private int classId;
	private String title;
	private String author;
	private String src;
	private String date;
	private String password;
	private int ispw;
	private String modified;
	private String desc;
	private String content;
	private int ccount;
	private List<Map<String, Object>> comments;
	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getIspw() {
		return ispw;
	}
	public void setIspw(int ispw) {
		this.ispw = ispw;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCcount() {
		return ccount;
	}
	public void setCcount(int ccount) {
		this.ccount = ccount;
	}
	public List<Map<String, Object>> getComments() {
		return comments;
	}
	public void setComments(List<Map<String, Object>> comments) {
		this.comments = comments;
	}
	@Override
	public String toString() {
		return "NoteInfoBean [noteId=" + noteId + ", userId=" + userId
				+ ", classId=" + classId + ", title=" + title + ", author="
				+ author + ", src=" + src + ", date=" + date + ", password="
				+ password + ", ispw=" + ispw + ", modified=" + modified
				+ ", desc=" + desc + ", content=" + content + ", ccount="
				+ ccount + ", comments=" + comments + "]";
	}
	
}
