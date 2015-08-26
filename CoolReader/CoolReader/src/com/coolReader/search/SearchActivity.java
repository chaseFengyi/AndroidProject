package com.coolReader.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.coolReader.R;
import com.coolReader.Application.SysApplication;
import com.coolReader.Bean.URLInfoBean;
import com.coolReader.Bean.WebContentBean;
import com.coolReader.Util.ToastUtils;
import com.coolReader.dao.DBPerform;
import com.coolReader.mainPage.MainPageActivity;
import com.coolReader.showcontent.ShowURLContentActivity;

/**
 * 搜索
 * 
 * @author FengYi~
 * 
 */
public class SearchActivity extends Activity implements OnQueryTextListener {
	private List<WebContentBean> list = new ArrayList<WebContentBean>();

	private ImageView backImg;
	private SearchView searchView;
	private TextView tipTxt;
	private ListView listView;

	private SimpleAdapter adapter = null;
	private String title_url;
	private List<Map<String, String>> list_show = null;

	// 判断是否是点击进入还是取消
	static boolean judgeEntry = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_search);

		findView();
		listView.setVisibility(View.GONE);

		// 设置该searchView默认自动缩小为图标
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);

		onclick();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i("search--url", getUrlOfItem(position));
				//从数据库获取指定url的对应内容
				URLInfoBean urlInfoBean = DBPerform.queryURLrInfoByUrl(SearchActivity.this, getUrlOfItem(position));
				WebContentBean webContentBean = new WebContentBean();
				webContentBean.setChecked(false);
				webContentBean.setWebContent(urlInfoBean.getUrlContent());
				webContentBean.setWebId(urlInfoBean.getUrlId());
				webContentBean.setWebTag(urlInfoBean.getUrlTag());
				webContentBean.setWebTitle(urlInfoBean.getUrlTitle());
				webContentBean.setWebUrl(urlInfoBean.getUrlLink());
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(MainPageActivity.TAG, webContentBean);
				bundle.putString("key", "SearchActivity");
				intent.putExtras(bundle);
				intent.setClass(SearchActivity.this, ShowURLContentActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 获取对应项上的url
	 * @param position
	 * @return
	 */
	public String getUrlOfItem(int position){
		Map<String, String> url_list = (Map<String, String>) listView.getItemAtPosition(position);
		String url = url_list.get("url");
		return url;
	}

	/**
	 * 根据地址栏输入的内容在底下listView下显示内容
	 */
	public void showListView() {
		// 如果listView为可见状态，向listView添加数据
		// adapter = new SimpleCursorAdapter(this, R.layout.sample_savefile, c,
		// from, to)
		if (listView.getVisibility() == View.VISIBLE) {// 当listView为可显示状态
			List<URLInfoBean> data = null;
			data = DBPerform.queryUrlInfoByTitleBlurry(SearchActivity.this,
					title_url);
			if (data == null) {// 用户可能输入的不是Title，而是url
				data = DBPerform.queryUrlInfoByURLBlurry(SearchActivity.this,
						title_url);
			}
			Log.i("data", data + "");
			if (data != null) {
				list_show = new ArrayList<Map<String, String>>();
				Map<String, String> map = null;
				for (int i = 0; i < data.size(); i++) {
					map = new HashMap<String, String>();
					map.put("title", data.get(i).getUrlTitle());
					map.put("url", data.get(i).getUrlLink());
					list_show.add(map);
				}

				adapter = new SimpleAdapter(SearchActivity.this, list_show,
						R.layout.sample_search_url,
						new String[] { "title", "url" }, new int[] {
								R.id.sample_search_title,
								R.id.sample_search_url });
				listView.setAdapter(adapter);
			} else {// 没有找到数据
				ToastUtils.makeToast(SearchActivity.this, "没有要找的内容");
			}

			listView.setTextFilterEnabled(true);
		}
	}

	private void findView() {
		backImg = (ImageView) findViewById(R.id.search_back);
		searchView = (SearchView) findViewById(R.id.search_search);
		tipTxt = (TextView) findViewById(R.id.search_tip);
		listView = (ListView) findViewById(R.id.search_listView);

	}

	private void onclick() {
		backImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(SearchActivity.this, MainPageActivity.class);
				intent.putExtra("listInfo", "");
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		// 隐藏提示字体
		tipTxt.setVisibility(View.GONE);
		// 显示listView
		listView.setVisibility(View.VISIBLE);
		if (TextUtils.isEmpty(newText)) {
			listView.clearTextFilter();
		} else {
			title_url = newText.toString().trim();
			showListView();
			adapter.notifyDataSetChanged();
//			listView.setFilterText(newText.toString());
		}

		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(SearchActivity.this, MainPageActivity.class);
			intent.putExtra("listInfo", "");
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}

}
