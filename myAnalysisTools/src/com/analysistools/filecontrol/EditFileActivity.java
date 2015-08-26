package com.analysistools.filecontrol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.analysis.filedel.FileDeal;
import com.analysistools.grammerAnalysis.GrammerActivity;
import com.analysistools.lexicalanalysis.LexicalActivity;
import com.example.myanalysistools.R;

public class EditFileActivity extends Activity implements OnClickListener {
	
	private Button back;
	private Button save;
	private Button determine;
	private Button reset;
	private EditText inputContent;
	
	private String TAG = "";
	private String TagLex = "LexicalActivity";
	private String TagGram = "GrammerActivity";
	private String LANGUAGE = "";
	public static final String ACTIVITY = "EditFileActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editfile);
		
		findView();
		adapterView();
		
		Intent intent = getIntent();
		TAG = intent.getStringExtra("tag");
		
		onclick();
	}
	
	private void findView(){
		back = (Button)findViewById(R.id.back);
		save = (Button)findViewById(R.id.save);
		determine = (Button)findViewById(R.id.determine);
		reset = (Button)findViewById(R.id.reset);
		inputContent = (EditText)findViewById(R.id.inputProject);
		inputContent.setFocusable(false);
		inputContent.setFocusableInTouchMode(false);
	}
	
	private void adapterView(){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		
		inputContent.setHeight(height * 2 / 3);
	}

	private void onclick(){
		back.setOnClickListener(this);
		save.setOnClickListener(this);
		determine.setOnClickListener(this);
		reset.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		String editContent = inputContent.getText().toString();
		List<String> content = new ArrayList<String>();
		Intent intent = new Intent();
		
		switch(arg0.getId()){
		case R.id.back:
			if(TAG.equals(TagLex)){
				intent.setClass(EditFileActivity.this, LexicalActivity.class);
			}else if(TAG.equals(TagGram)){
				intent.setClass(EditFileActivity.this, GrammerActivity.class);
			}else{
				Log.e("TAG", "error input");
			}
			startActivity(intent);
			break;
		case R.id.save:
			final EditText editText = new EditText(this);
			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					EditFileActivity.this)
					.setTitle("请输入文件名（后缀默认为.txt）")
					.setView(editText)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									String saveContent = inputContent.getText()
											.toString();
									String fileName = editText.getText()
											.toString();
									if (fileName.equals("")) {
										Toast.makeText(getApplicationContext(),
												"文件名不能为空，保存失败！" + fileName,
												Toast.LENGTH_LONG).show();
									} else {
										if (fileName.contains(".")) {
											String[] str = fileName
													.split("\\.");
											if (str[str.length - 1]
													.equals("txt"))
												FileDeal.writeCover(saveContent,
														fileName,EditFileActivity.this);
											else
												FileDeal.writeCover(saveContent,
														fileName + ".txt",EditFileActivity.this);
										} else {
											FileDeal.writeCover(saveContent, fileName
													+ ".txt",EditFileActivity.this);
										}
									}
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									arg0.dismiss();
								}
							});
			builder1.show();
			break;
		case R.id.determine:
			if(TAG.equals(TagLex)){
				content.add("LexicalActivity");
				content.add(editContent);
				content.add(LANGUAGE);
				content.add(ACTIVITY);
				intent.setClass(EditFileActivity.this, ShowResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("tag", (Serializable) content);
				intent.putExtras(bundle);
				startActivity(intent);
			}else if(TAG.equals(TagGram)){
				content.add("GrammerActivity");
				content.add(editContent);
				content.add(LANGUAGE);
				intent.setClass(EditFileActivity.this, ShowResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("tag", (Serializable) content);
				intent.putExtras(bundle);
				startActivity(intent);
			}else{
				Log.e("TAG", "error input");
			}
			break;
		case R.id.reset:
			inputContent.setText("");
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		//添加子菜单
		menu.add(1, 1, 2, "c代码");
		menu.add(2, 2, 2, "c++代码");
		menu.add(3, 3, 3, "java代码");

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		inputContent.setFocusable(true);
		inputContent.setFocusableInTouchMode(true);
		
		switch(item.getItemId()){
		case 1:
			LANGUAGE = "c代码";
			System.out.println("c代码+++");
			break;
		case 2:
			LANGUAGE = "c++代码";
			break;
		case 3:
			LANGUAGE = "java代码";
			break;
			default:
				//对没有处理的事件，交给父类来处理
				return super.onOptionsItemSelected(item);
		}
		
		return true;//返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
	}
	
}
