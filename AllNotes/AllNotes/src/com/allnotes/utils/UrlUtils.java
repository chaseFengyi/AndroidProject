package com.allnotes.utils;

public class UrlUtils {

	private static final String URL = "http://114.215.118.160/";

	// 注册
	public static final String Register = URL + "auth/register";
	// 登录
	public static final String Login = URL + "auth/login";
	// 退出
	public static final String LoginOut = URL + "auth/logout";
	// 修改用户信息
	public static final String ChangeUserInfo = URL + "userinfo/";
	//笔记分类URL
		private static final String Class_URL = "http://114.215.118.160/noteclass";
		//笔记本分类创建
		public static final String CreateNoteClass = Class_URL;
		//笔记分类表
		public static final String NoteClass = Class_URL;
		//笔记分类删除
		public static final String NoteClass_Delete = Class_URL;
		//笔记分类修改
		public static final String NoteClass_Update = Class_URL;
		
		//笔记本URL
		private static final String Note_URL = "http://114.215.118.160/notearticle";
		//笔记创建
		public static final String Note_Create = Note_URL;
		//笔记列表获取
		public static final String Note_lists = Note_URL;
		//笔记详情显示
		public static final String Note_Details_Show = Note_URL;
		//笔记删除
		public static final String Note_Lists_delete = Note_URL;
		//笔记列表修改
		public static final String Note_Lists_Update = Note_URL;
		//笔记评论添加
		public static final String Note_Comment_Add = Note_URL +"/comment";
		//笔记评论修改
		public static final String Note_Comment_Update = Note_Comment_Add +"/";
		//笔记评论删除
		public static final String Note_Comment_Delete = Note_Comment_Add;
}
