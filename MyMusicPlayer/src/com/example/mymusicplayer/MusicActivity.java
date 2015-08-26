package com.example.mymusicplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MusicActivity extends Activity {
	private ListView listView;
	private TextView state;
	private ImageButton last;
	private ImageButton play;
	private ImageButton stop;
	private ImageButton next;
	private SeekBar progress;
	private TextView currentTime;

	private MediaPlayer mediaPlayer = new MediaPlayer();
	private boolean isPaused = false;
	private static int position = 0;
	private static int second = 0;
	private static int minute = 0;
	private static int cursecond = 0;
	private static int curminute = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);

		listView = (ListView) findViewById(R.id.list);
		state = (TextView) findViewById(R.id.state);
		last = (ImageButton) findViewById(R.id.last);
		play = (ImageButton) findViewById(R.id.music);
		stop = (ImageButton) findViewById(R.id.stop);
		next = (ImageButton) findViewById(R.id.next);
		progress = (SeekBar) findViewById(R.id.progress);
		currentTime = (TextView) findViewById(R.id.currentTime);

		MusicInfo music = new MusicInfo();
		List<MusicInfo> list = music.getMusicInfos(this);
		setListAdapter(list);
		last.setOnClickListener(listener);
		play.setOnClickListener(listener);
		stop.setOnClickListener(listener);
		next.setOnClickListener(listener);
		listView.setOnItemClickListener(new ListViewListener());

		createWindow();
		// ���е绰��ʱ��������ͣ
		TelephonyManager telephonyManager = (TelephonyManager) 
				getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new MyMobileListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
	}

	private final class MyMobileListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// ����
				if (mediaPlayer.isPlaying()) {
					position = mediaPlayer.getCurrentPosition();
					mediaPlayer.stop();
				}
				break;

			case TelephonyManager.CALL_STATE_IDLE:
//				 play();
//				 mediaPlayer.seekTo(position);
				break;
			}
		}

	}

	@Override
	protected void onDestroy() {
		mediaPlayer.release();
		super.onDestroy();
	}

	// ����
	private void createWindow() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;

		// ����listView
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = height * 3 / 5;
		listView.setLayoutParams(params);

		// ����textView
		state.setHeight(50);
	}

	// ���listView
	public void setListAdapter(List<MusicInfo> musicInfos) {
		String name = "";
		String[] nameStr;
		List<HashMap<String, String>> musicList = new ArrayList<HashMap<String, String>>();
		for (Iterator<MusicInfo> iterator = musicInfos.iterator(); iterator
				.hasNext();) {
			MusicInfo musicInfo = (MusicInfo) iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();

			if (musicInfo.getTitle().contains("-")) {
				nameStr = musicInfo.getTitle().split("-");
				name = nameStr[nameStr.length - 2];
			} else {
				nameStr = musicInfo.getTitle().split(".mp3");
				name = nameStr[0];
			}
			System.out.println("name="+name);
			System.out.println("singer="+musicInfo.getSinger());
			map.put("title", name);
			map.put("singer", musicInfo.getSinger());
			map.put("url", musicInfo.getUrl());
			map.put("album", musicInfo.getAlbum());

			musicList.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, musicList,
				R.layout.simple_item, new String[] { "title", "singer" },
				new int[] { R.id.title, R.id.singer });
		listView.setAdapter(adapter);
	}

	// �ж�sd���Ƿ���ڣ�������ڣ�ȡ�ø�·��������Ϊnull
	private File getPath() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = Environment.getExternalStorageDirectory();
			return file;
		} else {
			return null;
		}
	}

	// ��õ�position��λ�õĸ���url
	@SuppressWarnings("unchecked")
	public String getMusicUrl(int number) {
		// ���ѡ�����hashMap����
		HashMap<String, String> list = (HashMap<String, String>) listView
				.getItemAtPosition(number);
		String string = list.get("url");
		/*
		 * String[] str = string.split("/"); String url = ""; for(int i=0;
		 * i<str.length-1; i++){ url += str[i]+"/"; } return url;
		 */
		return string;
	}

	// ��õ�position��λ�õĸ���
	@SuppressWarnings({ "unchecked" })
	public String getMusicName(int number) {
		HashMap<String, String> list = (HashMap<String, String>) listView
				.getItemAtPosition(number);
		String name = list.get("title");
		return name;
	}

	// ΪMediaPlayer�Ĳ�������¼���ʱ�������
	class MediaPlayerListener implements OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer arg0) {
			position = position + 1;
			if (position >= listView.getCount()) {
				position = 0;
			}
			play();
		}

	}

	// ���϶�������λ��ʱ�������¼�
	class SeekListener implements OnSeekBarChangeListener {

		// �϶���
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			// �ж����û��ı们���ֵ
			if (arg2 == true) {
				mediaPlayer.pause();
				mediaPlayer.seekTo(arg1);
				mediaPlayer.start();
			}
		}

		// ��ʼ�϶�
		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		// �����϶�
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
		}
	}

	// ʹ��handler��threadʵ�ֹ����������ֽ��ȵ��ƶ�
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// �������Ϣ�Ǳ����������͵�
			progress.setProgress(msg.what);
			curminute = (msg.what / 1000) / 60;
			cursecond = (msg.what / 1000) % 60;
			currentTime.setText(curminute + ":" + cursecond + "/" + minute
					+ ":" + second);
			super.handleMessage(msg);
		}

	};

	// �����������ֽ����ƶ�
	Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				if (mediaPlayer == null) {
					Toast.makeText(MusicActivity.this, "û�����ֿ��Բ���",
							Toast.LENGTH_SHORT);
					return;
				}
				Message msg = new Message();
				if (mediaPlayer.isPlaying()) {
					progress.setOnSeekBarChangeListener(new SeekListener());
					msg.what = mediaPlayer.getCurrentPosition();
					handler.sendMessage(msg);
				}
			}
		}
	});

	private void play() {
		try {
			if (mediaPlayer.isPlaying() == true) {
				mediaPlayer.reset();
			}
			if (getPath() == null) {
				Toast.makeText(MusicActivity.this, getString(R.string.error),
						Toast.LENGTH_SHORT).show();
				return;
			}
			String path = "";
			path = getMusicUrl(position);
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer arg0) {
					mediaPlayer.start();
				}
			});
			progress.setMax(mediaPlayer.getDuration());
			minute = (mediaPlayer.getDuration() / 1000) / 60;
			second = (mediaPlayer.getDuration() / 1000) % 60;
			state.setText("���ڲ���:" + getMusicName(position));
			thread.start();
		} catch (Exception e) {
		}

	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.last:
				if (mediaPlayer.isPlaying()) {
					if (position == 0) {
						position = listView.getCount() - 1;
					} else {
						position = position - 1;
					}
					play();
				}
				break;
			case R.id.music:
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
					isPaused = true;
					state.setText(R.string.str_pause);
					play.setImageResource(R.drawable.play);
				} else {
					if (isPaused) {
						mediaPlayer.start();
						isPaused = false;
					} else {
						play();
					}
					mediaPlayer
							.setOnCompletionListener(new MediaPlayerListener());
					play.setImageResource(R.drawable.pause3);
				}
				break;
			case R.id.stop:
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
					state.setText(R.string.str_stop);
				}
				break;
			case R.id.next:
				if (mediaPlayer.isPlaying()) {
					int num = listView.getCount() - 1;
					if (position == num) {
						position = 0;
					} else {
						position = position + 1;
					}
					play();
				}
				break;
			}
		}
	};

	// ΪlistView���б���ĵ����¼���ʱ�������
	class ListViewListener implements OnItemClickListener {
		// ��position�����ʱ�����÷���
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			position = arg2;
			mediaPlayer.stop();
			play.setImageResource(R.drawable.play);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music, menu);
		return true;
	}

}
