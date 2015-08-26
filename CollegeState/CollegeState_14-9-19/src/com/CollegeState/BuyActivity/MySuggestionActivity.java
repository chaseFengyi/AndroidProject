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
	private TextView titleText;// back�ұ���ʾactivity���ܵ�text

	private Button submit;
	private EditText suggestion;
	// �������ͨ�ŵĲ���
	private String SUGGESTION_STR = "suggestionStr";
	private String SUGGESTION_OK = "suggestion_ok";
	private String DADABASE_ERROR = "database_error";

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				suggestion.setText("");
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MySuggestionActivity.this)
						.setTitle("У��")
						.setMessage("�ύ�ɹ���")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								})
						.setNegativeButton("����",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder.create().show();
			} else if (msg.what == 0x234) {
				Toast.makeText(getApplicationContext(), "�ύʧ��",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0x345) {
				Toast.makeText(getApplicationContext(), "���ݿ����",
						Toast.LENGTH_SHORT).show();
			}
			// ����������
			else if (msg.what == 0x999) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MySuggestionActivity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("ϵͳ�����������Ժ�����")
						.setPositiveButton("ȷ��",
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
		// �Զ��������
		getActionBar().setDisplayShowHomeEnabled(false); // ʹ���Ͻ�ͼ���Ƿ���ʾ��������false����û�г���ͼ�꣬�����͸����⣬������ʾӦ�ó���ͼ��
		getActionBar().setDisplayShowTitleEnabled(false); // ��ӦActionBar.DISPLAY_SHOW_TITLE��
		getActionBar().setDisplayShowCustomEnabled(true);// ʹ�Զ������ͨView����title����ʾ
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
		titleText.setText("��Ҫ����");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
