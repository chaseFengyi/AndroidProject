package com.secondmarket.information.myInfo;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.secondmarket.DB.DBCreateWord;
import com.secondmarket.DB.DBPerform;
import com.secondmarket.bean.UserInfoDemo;
import com.secondmarket.load.LoadActivity;
import com.secondmarket.load.R;
import com.secondmarket.mine.MineActivity;

public class MyInformationActivity extends Activity {
	private Button myInformation;// 我的信息
	private Button change;// 更换
	private EditText userSchoolNumEdit;
	private EditText userNikeEdit;
	private EditText userNameEdit;
	private ImageView imageView;
	public static int flag = 1;// 判断点击更换按钮
	

	private static List<com.secondmarket.bean.UserInfoDemo> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_myinformation);

		findView();
		onClick();
		//设置为不可编辑状态
		setEdit_UnEdit(false);
		lookInfo();
	}

	private void setEdit_UnEdit(boolean flag){
		//flag=true,设置为可编辑状态
		if(flag){
			userNikeEdit.setFocusable(true);
			userNikeEdit.setFocusableInTouchMode(true);
			userNameEdit.setFocusable(true);
			userNameEdit.setFocusableInTouchMode(true);
		}else{
			userNikeEdit.setFocusable(false);
			userNikeEdit.setFocusableInTouchMode(false);
			userNameEdit.setFocusable(false);
			userNameEdit.setFocusableInTouchMode(false);
		}
	}
	
	private void findView() {
		myInformation = (Button) findViewById(R.id.myinformation);
		change = (Button)findViewById(R.id.change);
		userSchoolNumEdit = (EditText)findViewById(R.id.userSchoolNumEdit);
		userNikeEdit = (EditText)findViewById(R.id.userNikeEdit);
		userNameEdit = (EditText)findViewById(R.id.userNameEdit);
		imageView = (ImageView)findViewById(R.id.imageView);
	}

	private void onClick() {
		myInformation.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyInformationActivity.this, MineActivity.class));
			}
			
		});

		change.setOnClickListener(new ChangeListener());
		
	}
	
	private class ChangeListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (flag == 1) {
				//设置为可编辑状态
				setEdit_UnEdit(true);
				change.setText("确定");
				flag = 0;
			} else if (flag == 0) {
				//设置不可编辑状态
				AlertDialog.Builder builder = 
						new AlertDialog.Builder(MyInformationActivity.this)
						.setTitle("提示！")
						.setMessage("是否保存更改?")
						.setPositiveButton("保存", 
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										arg0.dismiss();
										setEdit_UnEdit(false);
										change.setText("更换");
										UserInfoDemo userInfoDemo = list.get(0);
										userInfoDemo.setUserSchoolNum(userSchoolNumEdit.getText().toString());
										userInfoDemo.setUserNike(userNikeEdit.getText().toString());
										userInfoDemo.setUserName(userNameEdit.getText().toString());
										if(DBPerform.updateUserTable(userInfoDemo, MyInformationActivity.this) ==1){
											Toast.makeText(MyInformationActivity.this, "修改用户信息成功!", Toast.LENGTH_SHORT).show();
										}else{
											Toast.makeText(MyInformationActivity.this, "修改用户信息失败!", Toast.LENGTH_SHORT).show();
										}
									}
								})
								.setNegativeButton("不保存", 
										new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										arg0.dismiss();
										System.out.println("---------not save");
									}
								});
				builder.create().show();
				flag = 1;
			}
			
		}
		
	}
	private void lookInfo() {
//OBJ:[UserInfoDemo [userId=1, userGrade=, userName=����<, userNike=, 
//userPassword=fy04123124, userPictureAd=, userSchoolNum=04123124, userSex=]]
		//从sqlite中查找用户信息
//		List<UserInfoDemo> list = LoadActivity.list1;
		list = DBPerform.SelectUserQuery(MyInformationActivity.this, DBCreateWord.TB_USER);
		if(list == null){
			Toast.makeText(MyInformationActivity.this, "没有获取到用户的信息", Toast.LENGTH_SHORT).show();
		}else{
			UserInfoDemo userInfoDemo;
			String path;
			System.out.println("myInfouserinfo="+list);
			for(int i = 0; i < list.size(); i++){
				userInfoDemo = list.get(i);
				if(userInfoDemo.getUserPictureAd() != null){
					path = userInfoDemo.getUserPictureAd();
					DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.showImageOnLoading(R.drawable.ic_launcher)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
					ImageLoader.getInstance().displayImage("http://172.20.0.152:8080/XuptMarket/"+path,
								imageView, options);
				}else{
					imageView.setBackgroundResource(R.drawable.ic_launcher);
				}
				userSchoolNumEdit.setText(userInfoDemo.getUserSchoolNum());
				System.out.println("myInfoUserNike="+userInfoDemo.getUserNike().length());//0
				System.out.println("myInfoUserNike="+userInfoDemo.getUserNike().isEmpty());//true
				System.out.println("myInfoUserNike="+userInfoDemo.getUserName().equals(""));//false
				System.out.println("myInfoUserNike="+userInfoDemo.getUserName()!= null );//true
				//string字符串=null说明不是对象
				if(userInfoDemo.getUserNike().length() > 0 || !(userInfoDemo.getUserNike().isEmpty())
						|| userInfoDemo.getUserNike() == null 
						|| userInfoDemo.getUserName().equals("") 
						){
					userNikeEdit.setText(userInfoDemo.getUserNike());
				}else {
					userNikeEdit.setHint("你还没有设置昵称！");
				}
				userNameEdit.setText(userInfoDemo.getUserName());
			}
		}
		
	}
}
