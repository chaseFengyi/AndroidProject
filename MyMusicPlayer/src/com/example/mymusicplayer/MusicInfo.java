package com.example.mymusicplayer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;


public class MusicInfo {
	private long id;
	private String title;//����
	private String singer;//�ݳ���
	private long time;
	private String url;
	private String album;
	
	public MusicInfo() {
	}
	
	public MusicInfo(long id,String title,String singer) {
		this.id = id;
		this.title = title;
		this.singer = singer;
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSinger() {
		return singer;
	}
	
	public void setSinger(String singer) {
		this.singer = singer;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	@Override
	public String toString() {
		return "MusicInfo [id="
				+id+",title="+title+
				",singer="+singer+
				",time="+time+",url="+url+
				",album="+album+"]";
	}
	
	//��ѯ������Ϣ,������list��
	public List<MusicInfo> getMusicInfos(Context context){
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				null,//�ֶΣ�û���ֶξ��ǲ�ѯ������Ϣ
				null,//��ѯ����
				null, //�����Ķ�Ӧ����
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER//����ʽ
				);
		System.out.println("cusor=="+cursor);
		List<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
		for(int i=0; i<cursor.getCount(); i++){
			MusicInfo musicInfo = new MusicInfo();
			cursor.moveToNext();
			//����id
			long id = cursor.getLong(
					cursor.getColumnIndex(MediaStore.Audio.Media._ID));
			//��������
			String title = cursor.getString(
					cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
			//����ʱ��
			long time = cursor.getLong(
					cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
			//ר��
			String album = cursor.getString(
					cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
			//������
			String singer = cursor.getString(
					cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			//�����ļ�·��
			String url = cursor.getString(
					cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
			int isMusic = cursor.getInt(
					cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)
					);
			if(isMusic != 0){
				musicInfo.setId(id);
				musicInfo.setTitle(title);
				musicInfo.setSinger(singer);
				musicInfo.setTime(time);
				musicInfo.setUrl(url);
				musicInfo.setAlbum(album);
				
				musicInfos.add(musicInfo);
			}
		}
		return musicInfos;
	}
}
