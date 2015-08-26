package com.allnotes.bean;

public class NoteCommentInfoBean {
	private int commentId;
	private int noteId;
	private String modified;
	private String content;
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "NoteCommentInfoBean [commentId=" + commentId + ", noteId="
				+ noteId + ", modified=" + modified + ", content=" + content
				+ "]";
	}
	
}
