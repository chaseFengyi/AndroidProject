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
 *2015年8月12日08:44:48
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
		ActionBarUtil.centerActionBar(this, this, getActionBar(), "修改密码");
		
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
					//将修改后的密码同步到服务器并保存在本地数据库
					ToastUtils.makeToast(EditCountChangePasswd.this, "修改成功，请返回");
				}
			}
		});
	}
	
	/**
	 * 检测输入的密码是否符合要求
	 */
	public boolean check(){
		String str_oldPass = et_old_pass.getText().toString().trim();
		String str_newPass = et_new_pass.getText().toString().trim();
		String str_confirmPass = et_confirm_pass.getText().toString().trim();
		
		//从数据库获取原密码
		UserInfoBean bean = DBPerform.queryUserInfoByEmail(EditCountChangePasswd.this, LoginActivity.USER_EMAIL_STR);
		String str_oldPass_db = bean.getUserPass();
		
		//输入的原密码与已经在哦数据库存储的密码不匹配
		if(!str_oldPass.equals(str_oldPass_db)){
			ToastUtils.makeToast(EditCountChangePasswd.this, "请检测您的原密码是否输入正确");
			et_old_pass.requestFocus();
			return false;
		}
		//输入的新密码与确认密码进行比较
		else if(!str_newPass.equals(str_confirmPass)){
			ToastUtils.makeToast(EditCountChangePasswd.this, "新密码与确认密码不相同，请重新输入确认密码");
			et_confirm_pass.setText("");
			et_confirm_pass.requestFocus();
			return false;
		}
		//输入成功
		else{
			return true;
		}
	}
}
