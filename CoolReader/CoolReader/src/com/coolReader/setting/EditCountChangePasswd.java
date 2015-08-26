package com.coolReader.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.coolReader.R;
import com.coolReader.Bean.UserInfoBean;
import com.coolReader.Login.LoginActivity;
import com.coolReader.Util.ActionBarUtil;
import com.coolReader.Util.ToastUtils;
import com.coolReader.dao.DBPerform;

/**
 * @author FengYi~
 *2015��8��12��08:44:48
 */
public class EditCountChangePasswd extends Activity{
	private EditText et_old_pass;
	private EditText et_new_pass;
	private EditText et_confirm_pass;
	private Button btn_save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setting_edit_change_passwd);
		ActionBarUtil.centerActionBar(this, this, getActionBar(), "�޸�����");
		
		findView();
	}
	
	private void findView(){
		et_old_pass = (EditText)findViewById(R.id.edit_change_old_pass);
		et_new_pass = (EditText)findViewById(R.id.edit_change_new_pass);
		et_confirm_pass = (EditText)findViewById(R.id.edit_change_confirm_new_pass);
		btn_save = (Button)findViewById(R.id.eidt_change_pass_bt);
		
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(check()){
					//���޸ĺ������ͬ�����������������ڱ������ݿ�
					ToastUtils.makeToast(EditCountChangePasswd.this, "�޸ĳɹ����뷵��");
				}
			}
		});
	}
	
	/**
	 * �������������Ƿ����Ҫ��
	 */
	public boolean check(){
		String str_oldPass = et_old_pass.getText().toString().trim();
		String str_newPass = et_new_pass.getText().toString().trim();
		String str_confirmPass = et_confirm_pass.getText().toString().trim();
		
		//�����ݿ��ȡԭ����
		UserInfoBean bean = DBPerform.queryUserInfoByEmail(EditCountChangePasswd.this, LoginActivity.USER_EMAIL_STR);
		String str_oldPass_db = bean.getUserPass();
		
		//�����ԭ�������Ѿ���Ŷ���ݿ�洢�����벻ƥ��
		if(!str_oldPass.equals(str_oldPass_db)){
			ToastUtils.makeToast(EditCountChangePasswd.this, "��������ԭ�����Ƿ�������ȷ");
			et_old_pass.requestFocus();
			return false;
		}
		//�������������ȷ��������бȽ�
		else if(!str_newPass.equals(str_confirmPass)){
			ToastUtils.makeToast(EditCountChangePasswd.this, "��������ȷ�����벻��ͬ������������ȷ������");
			et_confirm_pass.setText("");
			et_confirm_pass.requestFocus();
			return false;
		}
		//����ɹ�
		else{
			return true;
		}
	}
}
