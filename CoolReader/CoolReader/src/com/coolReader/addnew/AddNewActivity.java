package com.coolReader.addnew;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.coolReader.R;
import com.coolReader.Bean.SaveInfoBean;
import com.coolReader.Bean.URLInfoBean;
import com.coolReader.Bean.UserInfoBean;
import com.coolReader.Login.LoginActivity;
import com.coolReader.Util.AddressUtil;
import com.coolReader.Util.BackTagUtils;
import com.coolReader.Util.DialogUtils;
import com.coolReader.Util.ToastUtils;
import com.coolReader.dao.DBPerform;
import com.coolReader.mainPage.MainPageActivity;
import com.coolReader.request.JsonResolve;
import com.coolReader.request.TCPRequest;

/**
 * 通过URL添加新内容
 * 
 * @author FY 2015年8月7日19:19:07
 */
public class AddNewActivity extends Activity {
	private ImageView backImg;
	private EditText editText;
	private TextView tv_entry;
	// 1：表示还没有输入网址，显示“取消”字体 0：表示已经输入网址，显示“进入”字体
	private int TAG = 0;

	private final int OK = 1;
	private final int URL_ALREADY_EXIST = 2;
	private final int SERVER_ERROR = 3;
	
	public static final String URL = "url";
	
	static Dialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		com.coolReader.Application.SysApplication.getInstance().addActivity(
				this);
		setContentView(R.layout.activity_addnew);

		findView();
		onclick();
	}

	private void findView() {
		backImg = (ImageView) findViewById(R.id.addnew_back);
		editText = (EditText) findViewById(R.id.addnew_add);
		tv_entry = (TextView) findViewById(R.id.addnew_entry);
	}

	private void onclick() {
		backImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(AddNewActivity.this, MainPageActivity.class);
				startActivity(intent);
			}
		});

		tv_entry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TAG == 0) {// 此时要在地址栏输入网址
					// 获取输入的地址
					String ip = editText.getText().toString().trim();

					getContentByIP(ip);
					TAG = 1;
				}
			}
		});
	}

	private String getJsonForm(String url) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("funType", 50).put(
					"funContent",
					new JSONObject().put("mailAddr", LoginActivity.USER_EMAIL_STR)
							.put("deviceType", "android").put("url", url)
							.put("tag", ""));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	/**
	 * 通过ip上传服务器获取内容
	 * 
	 * @param ip
	 */
	private void getContentByIP(String ip) {
		final String url = ip;
		dialog = DialogUtils.createProgressDialog(AddNewActivity.this, "添加", "上传中...请稍后.......");
		dialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String jsonString = getJsonForm(url);
				String receive = TCPRequest.tcpRequest(jsonString,
						AddressUtil.TCT_ADDRESS, AddressUtil.TCP_PORT);
				System.out.println("receive-addnew");
				int funType = JsonResolve.getFunType_resolve(receive);
				System.out.println("funType="+funType);
				Message message = new Message();
				if (funType == 205) {
					message.what = URL_ALREADY_EXIST;
					message.obj = BackTagUtils.URL_ALREADY_EXIST;
				} else if(funType == 202) {
					message.what = SERVER_ERROR;
					message.obj = BackTagUtils.SERVER_ERROR;
				}else{//成功
					URLInfoBean bean = JsonResolve.getUrlContent(receive);
					message.what = OK;
					message.obj = bean;
				}
				handler.sendMessage(message);
			}
		}).start();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			dialog.dismiss();
			switch (msg.what) {
			case OK:
				URLInfoBean bean = (URLInfoBean) msg.obj;
				//强新添加的数据写入本地数据库
				DBPerform.insertURLInfo(AddNewActivity.this, bean);
				//验证url表插入正确
				URLInfoBean urlInfoBean = DBPerform.queryURLrInfoByUrl(AddNewActivity.this,bean.getUrlLink());
//				//获取用户表
//				UserInfoBean userInfoBean = DBPerform.queryUserInfoByEmail(AddNewActivity.this, LoginActivity.USER_EMAIL_STR);
//				SaveInfoBean saveInfoBean = new SaveInfoBean();
//				saveInfoBean.setUrlId(bean.getUrlId());
//				saveInfoBean.setUserId(userInfoBean.getUserId());
//				DBPerform.insertSaveInfo(AddNewActivity.this, saveInfoBean);
//				//验证Save表是否插入成功
//				List<SaveInfoBean> saveInfoBean2 = DBPerform.querySaveInfo(AddNewActivity.this);
				
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(URL, bean);
				intent.putExtras(bundle);
				intent.setClass(AddNewActivity.this, MainPageActivity.class);
				startActivity(intent);
				ToastUtils.makeToast(AddNewActivity.this, "添加成功");
				finish();
				break;
			case URL_ALREADY_EXIST:
				ToastUtils.makeToast(AddNewActivity.this, "链接已经存在，添加失败");
				finish();
				break;
			case SERVER_ERROR:
				ToastUtils.makeToast(AddNewActivity.this, "网络错误，请检查");
				finish();
				break;
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(AddNewActivity.this, MainPageActivity.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}

}
