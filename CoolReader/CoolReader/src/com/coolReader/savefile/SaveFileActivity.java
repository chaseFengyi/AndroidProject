package com.coolReader.savefile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import com.coolReader.R;
import com.coolReader.Application.SysApplication;
import com.coolReader.Bean.SaveInfoBean;
import com.coolReader.Bean.URLInfoBean;
import com.coolReader.Bean.WebContentBean;
import com.coolReader.Util.ActionBarUtil;
import com.coolReader.Util.FileUtils;
import com.coolReader.dao.DBPerform;

public class SaveFileActivity extends Activity {
	private ListView listView;
	// �洢�༭�µĴ浵��Ϣ
	private Set<String> list_new = null;
	private SaveFileAdapter adapter = null;

	public static List<SaveInfoBean> list = new ArrayList<SaveInfoBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_savefile);
		ActionBarUtil.centerActionBar(this, this, getActionBar(), "�ҵĴ浵");

		list_new = FileUtils.read(SaveFileActivity.this);
		listView = (ListView) findViewById(R.id.savefile_listview);
		setListView();
	}

	// ����û���������ѡ��Ҫ�浵�����ݣ�����浵�󣬴浵�����ݴ�����Activity
	// private void fileContent(Set<String> list){
	// if(list == null)
	// return;
	// Iterator iterator = list.iterator();
	// while(iterator.hasNext()){
	// WebContentBean webContentBean = new WebContentBean();
	// webContentBean.setWebTitle("titlew");
	// webContentBean.setWebContent("content");
	// webContentBean.setChecked(false);
	// webContentBean.setWebUrl((String) iterator.next());
	// this.list.add(webContentBean);
	// }
	// }

	/**
	 * �����ݿ��ȡ�浵��Ϣ
	 */
	// public void getSaveContent(){
	// List<SaveInfoBean> saves =
	// DBPerform.querySaveInfo(SaveFileActivity.this);
	// Log.i("saves", saves+"");
	// //���ݴ浵��Ϣ��ȡ��Ӧ������
	// if(saves != null){
	// for(int i = 0; i < saves.size(); i++){
	// //ͨ���浵��Ϣ�������urlid��ȡ��Ӧ��url����
	// int urlId = saves.get(i).getUrlId();
	// Log.i("urlId=", urlId+"");
	// //����urlid��url���е�url����
	// URLInfoBean urlInfoBean =
	// DBPerform.queryURLrInfoByID(SaveFileActivity.this, urlId);
	// Log.i("urlinfo-", urlInfoBean+"");
	// WebContentBean bean = new WebContentBean();
	// bean.setChecked(false);
	// bean.setWebTitle(urlInfoBean.getUrlTitle());
	// bean.setWebContent(urlInfoBean.getUrlContent());
	// bean.setWebUrl(urlInfoBean.getUrlLink());
	// list.add(bean);
	// }
	// }
	// }
	public void getSaveContent() {
		list = DBPerform.querySaveInfo(SaveFileActivity.this);
		Log.i("saves", list + "");
	}

	private void setListView() {
		// fileContent(list_new);
		getSaveContent();
		if (list.size() <= 0) {
			Toast.makeText(SaveFileActivity.this, "���޴浵��Ϣ", Toast.LENGTH_SHORT)
					.show();
		} else {
			adapter = new SaveFileAdapter(SaveFileActivity.this, list);
			listView.setAdapter(adapter);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		System.out.println("restart");
		// ���list���ϣ���ֹ������ʾ�����ʱ��list���滹�����ݣ�������ʾ����������
		if (list != null) {
			list.clear();
			list = new ArrayList<SaveInfoBean>();
		}
	}
}
