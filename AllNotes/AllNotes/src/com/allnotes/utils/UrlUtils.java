package com.allnotes.utils;

public class UrlUtils {

	private static final String URL = "http://114.215.118.160/";

	// ע��
	public static final String Register = URL + "auth/register";
	// ��¼
	public static final String Login = URL + "auth/login";
	// �˳�
	public static final String LoginOut = URL + "auth/logout";
	// �޸��û���Ϣ
	public static final String ChangeUserInfo = URL + "userinfo/";
	//�ʼǷ���URL
		private static final String Class_URL = "http://114.215.118.160/noteclass";
		//�ʼǱ����ഴ��
		public static final String CreateNoteClass = Class_URL;
		//�ʼǷ����
		public static final String NoteClass = Class_URL;
		//�ʼǷ���ɾ��
		public static final String NoteClass_Delete = Class_URL;
		//�ʼǷ����޸�
		public static final String NoteClass_Update = Class_URL;
		
		//�ʼǱ�URL
		private static final String Note_URL = "http://114.215.118.160/notearticle";
		//�ʼǴ���
		public static final String Note_Create = Note_URL;
		//�ʼ��б��ȡ
		public static final String Note_lists = Note_URL;
		//�ʼ�������ʾ
		public static final String Note_Details_Show = Note_URL;
		//�ʼ�ɾ��
		public static final String Note_Lists_delete = Note_URL;
		//�ʼ��б��޸�
		public static final String Note_Lists_Update = Note_URL;
		//�ʼ��������
		public static final String Note_Comment_Add = Note_URL +"/comment";
		//�ʼ������޸�
		public static final String Note_Comment_Update = Note_Comment_Add +"/";
		//�ʼ�����ɾ��
		public static final String Note_Comment_Delete = Note_Comment_Add;
}
