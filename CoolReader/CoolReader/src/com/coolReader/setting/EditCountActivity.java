package com.coolReader.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.coolReader.R;
import com.coolReader.Bean.UserInfoBean;
import com.coolReader.Login.LoginActivity;
import com.coolReader.Util.ActionBarUtil;
import com.coolReader.dao.DBPerform;

/**
 * �༭�˺�
 * @author FengYi~
 *2015��8��12��08:39:31
 */
public class EditCountActivity extends Activity{
	//�˺�
	private EditText et_account;
	//����
	private EditText et_email;
	//�޸�����
	private Button btn_update_pass = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_setting_edit);
		ActionBarUtil.centerActionBarAndRtn(this, this, getActionBar(), "�༭�˺�", "����");
		findViews();
		getUserInfo();
		btn_update_pass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(EditCountActivity.this,EditCountChangePasswd.class));
			}
		});
	}
	
	//���ҳ�沼�ֿռ�
	private void findViews(){
		btn_update_pass = (Button)this.findViewById(R.id.changpass);
		et_account = (EditText)this.findViewById(R.id.et_username);
		et_email = (EditText)this.findViewById(R.id.et_userpass);
	}
	
	/**
	 * �����ݿ��ȡ�û���Ϣ
	 */
	public void getUserInfo(){
		UserInfoBean bean = DBPerform.queryUserInfoByEmail(EditCountActivity.this, LoginActivity.USER_EMAIL_STR);
		et_account.setText(bean.getUserName());
		et_email.setText(bean.getUserEmail());
	}
	
}
