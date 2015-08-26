package com.CollegeState.UserActivity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.CollegeState.R;
import com.CollegeState.BuyActivity.ShopActivity;
import com.CollegeState.Data.OrderItem;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.MD5StringUtil;

public class UserChangePasswdAcitvity extends Activity {
	private EditText passwd_oriange;// ԭ����
	private EditText passwd_new;// ������
	private EditText passwd_confirm;// ȷ��������
	private Button confirm;// ȷ�ϰ�ť
	private ProgressDialog passwdProgressDialog;
	private ImageButton back;// ���ذ�ť
	private TextView titleText;// ��������

	private String USER_PHONE = "userPhone";
	private String USER_PASSWD = "userPasswd";
	private String NEW_PASSWD = "newPasswd";
	private String ACTION = "action";
	private String OLD_PASSWD_ERROR = "oldPassword_error";
	private String UPDATE_OK = "update_ok";
	private String ACTION_ERROR = "action_error";

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			// create order filed!(���ɶ���ʧ��)
			case 0x123:
				passwdProgressDialog.dismiss();
				AlertDialog.Builder builder0 = new AlertDialog.Builder(
						UserChangePasswdAcitvity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("��������С���ˣ�")
						.setPositiveButton("�Ժ�����",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder0.create().show();
				break;
			case 0x124:
				passwdProgressDialog.dismiss();
				AlertDialog.Builder builder1 = new AlertDialog.Builder(
						UserChangePasswdAcitvity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("ԭ�������")
						.setPositiveButton("������",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder1.create().show();
				break;
			// Submit Success! �ύ�ɹ�
			case 0x234:
				passwdProgressDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserChangePasswdAcitvity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("�����޸ĳɹ���")
						.setPositiveButton("ȥ��½",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Intent intent = new Intent();
										intent.setClass(
												UserChangePasswdAcitvity.this,
												UserLoginActivity.class);
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(intent);

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
				break;
			// �������
			case 0x345:
				passwdProgressDialog.dismiss();
				AlertDialog.Builder builder2 = new AlertDialog.Builder(
						UserChangePasswdAcitvity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("�������")
						.setPositiveButton("ȥ�鿴����",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Intent intent = new Intent(
												android.provider.Settings.ACTION_SETTINGS);
										startActivity(intent);

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
				builder2.create().show();
			case 0x456:
				passwdProgressDialog.dismiss();
				AlertDialog.Builder builder3 = new AlertDialog.Builder(
						UserChangePasswdAcitvity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("��½ʧЧ�������µ�½")
						.setPositiveButton("������",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				builder3.create().show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_change_passwd);
		initActionbar();
		// ����û���¼״̬
		if (!UserInfoBean.isLogin()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					UserChangePasswdAcitvity.this)
					.setTitle("��ܰ��ʾ")
					.setMessage("����û�е�¼!")
					.setPositiveButton("��¼",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent();
									intent.setClass(
											UserChangePasswdAcitvity.this,
											UserLoginActivity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);

								}
							})
					.setNegativeButton("����",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									finish();
								}
							});
			builder.create().show();
			return;
		}
		// �ж�����״̬
		isNetWorkEnable();
		findView();
		setListener();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (check()) {
					// ��ʾ�������Ի���
					passwdProgressDialog = new ProgressDialog(
							UserChangePasswdAcitvity.this);
					passwdProgressDialog.setTitle("У��");
					passwdProgressDialog.setMessage("�����޸�");
					passwdProgressDialog.setCancelable(true);
					passwdProgressDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					passwdProgressDialog.show();
					// �ϴ�ע����Ϣ
					new Thread() {
						public void run() {
							try {
								// ����һ��NameValuePair���飬���ڴ洢�����͵Ĳ���
								ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair(USER_PHONE,
										UserInfoBean.getUserPhone()));
								params.add(new BasicNameValuePair(USER_PASSWD,
										passwd_oriange.getText().toString()));
								params.add(new BasicNameValuePair(NEW_PASSWD,
										passwd_new.getText().toString()));
								params.add(new BasicNameValuePair(ACTION,
										UserInfoBean.getAction()));
								String result = HttpUtils.queryStringForPost(
										HttpUtils.USER_CHANGE_PASSWD, params);
								if (result.equals(OLD_PASSWD_ERROR)) {
									handler.sendEmptyMessage(0x124);
								} else if (result.equals(UPDATE_OK)) {
									handler.sendEmptyMessage(0x234);
								} else if (result
										.equals(HttpUtils.NETWORK_ERROR)) {
									handler.sendEmptyMessage(0x345);
								} else if (result.equals(ACTION_ERROR)) {
									handler.sendEmptyMessage(0x456);
								} else {
									handler.sendEmptyMessage(0x123);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						};

					}.start();
				}
			}
		});
	}

	private void findView() {
		// TODO Auto-generated method stub
		passwd_oriange = (EditText) findViewById(R.id.user_change_passwd_oriange);
		passwd_new = (EditText) findViewById(R.id.user_change_passwd_new);
		passwd_confirm = (EditText) findViewById(R.id.user_change_passwd_new_confrim);
		confirm = (Button) findViewById(R.id.user_change_passwd_confrim);
	}

	/**
	 * �жϱ�����
	 * 
	 * @return
	 */
	private boolean check() {
		if (!(passwd_new.getText().toString().equals(passwd_confirm.getText()
				.toString()))) {
			Toast.makeText(getApplicationContext(), "�����������벻һ��",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * �������״̬
	 * 
	 */
	private void isNetWorkEnable() {
		if (!HttpUtils.isNetWorkEnable(this)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					UserChangePasswdAcitvity.this)
					.setTitle("��ܰ��ʾ")
					.setMessage("�������")
					.setNegativeButton("����",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									finish();
								}
							})
					.setPositiveButton("ȥ��������״̬",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// ��ת��ϵͳ��activityȥ��������
									dialog.dismiss();
									Intent intent = new Intent();
									intent.setAction(android.provider.Settings.ACTION_SETTINGS);
									startActivity(intent);
								}

							});
			builder.create().show();
		}
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
		titleText.setText("�޸�����");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
