package com.CollegeState.BuyActivity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.Util.HttpUtils;

public class MySuggestionActivity extends Activity {
	private ImageButton back;
	private TextView titleText;// back右边显示activity功能的text

	private Button submit;
	private EditText suggestion;
	// 与服务器通信的参数
	private String SUGGESTION_STR = "suggestionStr";
	private String SUGGESTION_OK = "suggestion_ok";
	private String DADABASE_ERROR = "database_error";

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				suggestion.setText("");
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MySuggestionActivity.this)
						.setTitle("校帮")
						.setMessage("提交成功！")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								})
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder.create().show();
			} else if (msg.what == 0x234) {
				Toast.makeText(getApplicationContext(), "提交失败",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0x345) {
				Toast.makeText(getApplicationContext(), "数据库错误",
						Toast.LENGTH_SHORT).show();
			}
			// 服务器错误
			else if (msg.what == 0x999) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MySuggestionActivity.this)
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
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_suggestion);
		findView();
		setListener();
		initActionbar();
	}

	private void findView() {
		// TODO Auto-generated method stub
		suggestion = (EditText) findViewById(R.id.my_suggestion);
		submit = (Button) findViewById(R.id.my_suggestion_submit);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new Thread() {
					public void run() {
						// TODO Auto-generated method stub
						ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair(SUGGESTION_STR,
								suggestion.getText().toString()));
						String result = HttpUtils.queryStringForPost(
								HttpUtils.MY_SUGGESTION, params);

						Message msg = new Message();

						if (result.equals(SUGGESTION_OK)) {
							msg.what = 0x123;
						} else if (result.equals(DADABASE_ERROR)) {
							msg.what = 0x345;
						} else if (TextUtils.isEmpty(result)) {
							handler.sendEmptyMessage(0x999);
						} else {
							msg.what = 0x234;
						}
						handler.sendMessage(msg);
					};
				}.start();
			}
		});
	}

	private void initActionbar() {
		// 自定义标题栏
		getActionBar().setDisplayShowHomeEnabled(false); // 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
		getActionBar().setDisplayShowTitleEnabled(false); // 对应ActionBar.DISPLAY_SHOW_TITLE。
		getActionBar().setDisplayShowCustomEnabled(true);// 使自定义的普通View能在title栏显示
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mTitleView = mInflater.inflate(
				R.layout.custom_action_bar_onlyback, null);
		getActionBar().setCustomView(
				mTitleView,
				new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		titleText = (TextView) findViewById(R.id.title_name);
		titleText.setText("我要挑刺");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
