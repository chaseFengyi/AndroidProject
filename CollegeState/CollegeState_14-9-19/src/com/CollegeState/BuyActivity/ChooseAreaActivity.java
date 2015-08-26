package com.CollegeState.BuyActivity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.Data.SchoolBean;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.JSONUtils;

public class ChooseAreaActivity extends Activity {
	private ImageButton back;
	private TextView titleText;
	private ListView lv_schools;
	private final int NET_ERROR = 101;
	private final int GET_SCHOOL_SUCC = 102;
	private List<SchoolBean> mData = new ArrayList<SchoolBean>();

	// 保存学校信息的SharedPreference
	private SharedPreferences sharedPreferenceSchool = null;
	private SharedPreferences.Editor editor = null;
	private final String SHARED_PREF_SCHOOL = "CHOOSE_SCHOOL";
	private final String SHARED_PREF_SCHOOL_NAME = "school_name";
	private final String SHARED_PREF_SCHOOL_ID = "school_id";

	private String NO_AREA = "no_area";
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NET_ERROR:

				break;
			case GET_SCHOOL_SUCC:
				mData = (List<SchoolBean>) msg.obj;
				lv_schools.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;
			case 0x123:
				Toast.makeText(ChooseAreaActivity.this, "暂无此区域",
						Toast.LENGTH_SHORT).show();
				// 服务器错误
			case 0x999:
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ChooseAreaActivity.this)
						.setTitle("溫馨提示")
						.setMessage("系统正在升级，稍后再试")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder.create().show();

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_school);
		initActionbar();
		lv_schools = (ListView) findViewById(R.id.lv_schools);
		new GetSchoolTask().start();
		lv_schools.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 保存信息
				saveSchool(mData.get(arg2).getAreaName(), mData.get(arg2)
						.getAreaId() + "");
				// 由用户注册界面跳转而来
				if (isUserRegister()) {
					Intent intent = new Intent();
					intent.putExtra("areaId", mData.get(arg2).getAreaId() + "");
					intent.putExtra("area", mData.get(arg2).getAreaName());
					intent.setAction("com.ordering.school_user_reg");
					sendBroadcast(intent);
					ChooseAreaActivity.this.finish();
				} else {
					// 由主界面跳转
					Intent intent = new Intent();
					intent.putExtra("areaId", mData.get(arg2).getAreaId() + "");
					intent.putExtra("area", mData.get(arg2).getAreaName());
					intent.setAction("com.ordering.message_changed");
					sendBroadcast(intent);
					ChooseAreaActivity.this.finish();
				}
			}
		});
	}

	/**
	 * 判断是否是用户注册界面跳转而来
	 * 
	 * @return
	 */
	private boolean isUserRegister() {
		Intent intent = getIntent();
		return intent.hasExtra("a");
	}

	public class GetSchoolTask extends Thread {

		@Override
		public void run() {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("cityId", UserInfoBean
					.getCityId()));
			String result = HttpUtils.queryStringForPost(HttpUtils.AREA_LIST,
					params);
			if (result.equals(HttpUtils.NETWORK_ERROR)) {
				handler.sendEmptyMessage(NET_ERROR);
			} else if (result.equals(NO_AREA)) {
				handler.sendEmptyMessage(0x123);
			} else if (TextUtils.isEmpty(result)) {
				handler.sendEmptyMessage(0x999);
			} else {
				Message msg = Message.obtain();
				msg.what = GET_SCHOOL_SUCC;
				msg.obj = JSONUtils.getSchools(result);
				handler.sendMessage(msg);
			}
		}
	}

	private void initActionbar() {
		// 自定义标题栏
		getActionBar().setDisplayShowHomeEnabled(false); // 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
		getActionBar().setDisplayShowTitleEnabled(false); // 对应ActionBar.DISPLAY_SHOW_TITLE。
		getActionBar().setDisplayShowCustomEnabled(true);// 使自定义的普通View能在title栏显示
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mTitleView = mInflater.inflate(R.layout.custom_action_bar_shop,
				null);
		getActionBar().setCustomView(
				mTitleView,
				new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		titleText = (TextView) findViewById(R.id.shop_title_name);
		titleText.setText("选择学校");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// 保存选取学校信息
	public void saveSchool(String schoolName, String SchoolId) {
		sharedPreferenceSchool = getSharedPreferences(SHARED_PREF_SCHOOL,
				MODE_PRIVATE);
		editor = sharedPreferenceSchool.edit();
		// 第一次进入程序之后标记为使用次数为一次
		editor.putString(SHARED_PREF_SCHOOL_NAME, schoolName);
		editor.putString(SHARED_PREF_SCHOOL_ID, SchoolId);
		editor.commit();
	}

	public BaseAdapter adapter = new BaseAdapter() {

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			LinearLayout ll = (LinearLayout) LayoutInflater.from(
					ChooseAreaActivity.this).inflate(
					R.layout.list_adapter_schools, null);
			TextView tv_list_adapter_schools = (TextView) ll
					.findViewById(R.id.tv_list_adapter_schools);
			tv_list_adapter_schools.setText(mData.get(arg0).getAreaName());
			return ll;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCount() {
			return mData == null ? 0 : mData.size();
		}
	};

}
