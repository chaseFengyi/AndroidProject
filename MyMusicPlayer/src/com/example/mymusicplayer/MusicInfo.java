package com.example.mymusicplayer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;


public class MusicInfo {
	private long id;
	private String title;//名称
	private String singer;//演唱者
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
	
	//查询歌曲信息,保存在list中
	public List<MusicInfo> getMusicInfos(Context context){
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				null,//字段，没有字段就是查询所有信息
				null,//查询条件
				null, //条件的对应参数
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER//排序方式
				);
		System.out.println("cusor=="+cursor);
		List<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
		for(int i=0; i<cursor.getCount(); i++){
			MusicInfo musicInfo = new MusicInfo();
			cursor.moveToNext();
			//歌曲id
			long id = cursor.getLong(
					cursor.getColumnIndex(MediaStore.Audio.Media._ID));
			//歌曲名称
			String title = cursor.getString(
					cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
			//歌曲时长
			long time = cursor.getLong(
					cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
			//专辑
			String album = cursor.getString(
					cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
			//歌手名
			String singer = cursor.getString(
					cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			//歌曲文件路径
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
