package com.analysistools.filecontrol;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.analysistools.bean.FileTypeBean;
import com.analysistools.grammerAnalysis.GrammerActivity;
import com.analysistools.lexicalanalysis.LexicalActivity;
import com.example.myanalysistools.R;

public class ExistedFileActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private Button back;
	private String TAG = "";
	private String TagLex = "LexicalActivity";
	private String TagGram = "GrammerActivity";
	private ListView listView;
	private TextView path;
	private TextView item_count;
	private FileListAdapter mFileListAdapter;
	private String LANGUAGE = "";
	public static final String ACTIVITY = "ExistedFileActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_existedfile);

		findView();

		Intent intent = getIntent();
		TAG = intent.getStringExtra("tag");

		path.setHint("请先点击菜单选择语言!");
	}

	private void findView() {
		back = (Button) findViewById(R.id.backExisted);
		listView = (ListView) findViewById(R.id.listView);
		path = (TextView) findViewById(R.id.path);
		item_count = (TextView) findViewById(R.id.item_count);
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
		path.setFocusable(true);
		path.setFocusableInTouchMode(true);
		
		switch(item.getItemId()){
		case 1:
			LANGUAGE = "c代码";
			System.out.println("c代码+++");
			initView();
			onclick();
			break;
		case 2:
			LANGUAGE = "c++代码";
			initView();
			onclick();
			break;
		case 3:
			LANGUAGE = "java代码";
			initView();
			onclick();
			break;
			default:
				//对没有处理的事件，交给父类来处理
				return super.onOptionsItemSelected(item);
		}
		
		return true;//返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
	}
	
	private void initView() {
		// initView之后添加apk的权限，777 表示可读可写可操作。
		listView.setOnItemClickListener(this);
		String apkRoot = "chmod 777 " + getPackageCodePath();
		RootCommand(apkRoot);
		File folder = new File("/");
		initData(folder);
	}

	// 修改root权限的方法
	public static boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private void initData(File folder) {
		boolean isRoot = folder.getParent() == null;
		path.setText(folder.getAbsolutePath());
		ArrayList<File> files = new ArrayList<File>();
		if (!isRoot) {
			files.add(folder.getParentFile());
		}
		File[] filterFiles = folder.listFiles();
		item_count.setText(filterFiles.length + "项");
		if (null != filterFiles && filterFiles.length > 0) {
			for (File file : filterFiles) {
				files.add(file);
			}
		}
		mFileListAdapter = new FileListAdapter(this, files, isRoot);
		listView.setAdapter(mFileListAdapter);
	}

	private class FileListAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<File> files;
		private boolean isRoot;
		private LayoutInflater miInflater;

		public FileListAdapter(Context context, ArrayList<File> files,
				boolean isRoot) {
			this.context = context;
			this.files = files;
			this.isRoot = isRoot;
			miInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return files.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return files.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder viewHolder;
			if (arg1 == null) {
				viewHolder = new ViewHolder();
				arg1 = miInflater.inflate(R.layout.file_list_item, null);
				arg1.setTag(viewHolder);
				viewHolder.title = (TextView) arg1
						.findViewById(R.id.file_title);
				viewHolder.type = (TextView) arg1.findViewById(R.id.file_type);
				viewHolder.data = (TextView) arg1.findViewById(R.id.file_date);
				viewHolder.size = (TextView) arg1.findViewById(R.id.file_size);
			} else {
				viewHolder = (ViewHolder) arg1.getTag();
			}

			File file = (File) getItem(arg0);
			if (arg0 == 0 && !isRoot) {
				viewHolder.title.setText("返回上一级");
				viewHolder.data.setVisibility(View.GONE);
				viewHolder.size.setVisibility(View.GONE);
				viewHolder.type.setVisibility(View.GONE);
			} else {
				String fileName = file.getName();
				viewHolder.title.setText(fileName);
				if (file.isDirectory()) {
					viewHolder.size.setText("文件夹");
					viewHolder.size.setTextColor(Color.RED);
					viewHolder.type.setVisibility(View.GONE);
					viewHolder.data.setVisibility(View.GONE);
				} else {
					long fileSize = file.length();
					if (fileSize > 1024 * 1024) {
						float size = fileSize / (1024f * 1024f);
						viewHolder.size.setText(new DecimalFormat("#.00")
								.format(size) + "MB");
					} else if (fileSize >= 1024) {
						float size = fileSize / 1024;
						viewHolder.size.setText(new DecimalFormat("#.00")
								.format(size) + "KB");
					} else {
						viewHolder.size.setText(fileSize + "B");
					}
					int dot = fileName.indexOf('.');
					if (dot > -1 && dot < (fileName.length() - 1)) {
						viewHolder.type.setText(fileName.substring(dot + 1)
								+ "文件");
					}
					viewHolder.data.setText(new SimpleDateFormat(
							"yyyy/MM/dd HH:mm").format(file.lastModified()));
				}
			}
			return arg1;
		}

	}

	class ViewHolder {
		private TextView title;
		private TextView type;
		private TextView data;
		private TextView size;
	}

	private void onclick() {
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();

		switch (arg0.getId()) {
		case R.id.backExisted:
			System.out.println("Tag=:" + TAG);
			Log.e("uuuuuu", TAG);
			if (TAG.equals(TagLex)) {
				intent.setClass(ExistedFileActivity.this, LexicalActivity.class);
				startActivity(intent);
			} else if (TAG.equals(TagGram)) {
				intent.setClass(ExistedFileActivity.this, GrammerActivity.class);
				startActivity(intent);
			} else {
				Log.e("TAG", "error input");
			}
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		File file = (File) mFileListAdapter.getItem(arg2);
		if (!file.canRead()) {
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("权限不足")
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							
						}
					}).show();
		} else if (file.isDirectory()) {
			initData(file);
		} else {
			openFile(file);
		}
	}

	/*private void openFile(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = getMIMEType(file);*/
		//打开设置打开文件的类型。
		//如果type是*/*会弹出所有的可供选择的应用程序。
	/*	intent.setDataAndType(Uri.fromFile(file), type);
		try {
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(this, "未知类型，不能打开", Toast.LENGTH_SHORT).show();
		}
	}*/
	/*private void openFile(File file){
		String str = null;
		StringBuffer string = new StringBuffer();
		List<String> content = new ArrayList<String>();
		Intent intent = new Intent();
		try{
			InputStream is = new FileInputStream(file);
			InputStreamReader input = new InputStreamReader(is, "UTF-8");
			BufferedReader reader = new BufferedReader(input);
			while((str = reader.readLine()) != null){
				System.out.println("str="+str+"\n");
				string.append(str);
			}
			if(TAG.equals(TagLex)){
				content.add("LexicalActivity");
				content.add(string.toString());
				content.add(LANGUAGE);
				intent.setClass(ExistedFileActivity.this, ShowResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("tag", (Serializable) content);
				intent.putExtras(bundle);
				startActivity(intent);
			}else if(TAG.equals(TagGram)){
				content.add("GrammerActivity");
				content.add(string.toString());
				content.add(LANGUAGE);
				intent.setClass(ExistedFileActivity.this, ShowResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("tag", (Serializable) content);
				intent.putExtras(bundle);
				startActivity(intent);
			}else{
				Log.e("TAG", "error input");
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
			Toast.makeText(this, "未知类型，不能打开", Toast.LENGTH_SHORT).show();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	private void openFile(File file){
		String str = null;
		StringBuffer string = new StringBuffer();
		List<String> content = new ArrayList<String>();
		Intent intent = new Intent();
		try{
			InputStream is = new FileInputStream(file);
			InputStreamReader input = new InputStreamReader(is, "UTF-8");
			BufferedReader reader = new BufferedReader(input);
			while((str = reader.readLine()) != null){
				System.out.println("str="+str+"\n");
				string.append(str);
			}
			if(TAG.equals(TagLex)){
				content.add("LexicalActivity");
				content.add(string.toString());
				content.add(LANGUAGE);
				content.add(ACTIVITY);
				intent.setClass(ExistedFileActivity.this, ShowResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("tag", (Serializable) content);
				intent.putExtras(bundle);
				startActivity(intent);
			}else if(TAG.equals(TagGram)){
				content.add("GrammerActivity");
				content.add(string.toString());
				content.add(LANGUAGE);
				content.add(ACTIVITY);
				intent.setClass(ExistedFileActivity.this, ShowResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("tag", (Serializable) content);
				intent.putExtras(bundle);
				startActivity(intent);
			}else{
				Log.e("TAG", "error input");
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
			Toast.makeText(this, "未知类型，不能打开", Toast.LENGTH_SHORT).show();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//http://www.android-study.com/pingtaikaifa/38.html
	//读取doc文件里面的内容
	/*private void openFile(File file){
		FileInputStream in;
		String text = null;
		List<String> content = new ArrayList<String>();
		Intent intent = new Intent();
		
		try {
			in = new FileInputStream(file);
			WordExtractor extrator = null;
			extrator = new WordExtractor();
			//对doc文件进行提取
			text = extrator.extractText(in);
			System.out.println("text11111="+text);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("texts2222="+text);
		
		if(TAG.equals(TagLex)){
			content.add("LexicalActivity");
			content.add(text.toString());
			content.add(LANGUAGE);
			intent.setClass(ExistedFileActivity.this, ShowResultActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("tag", (Serializable) content);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(TAG.equals(TagGram)){
			content.add("GrammerActivity");
			content.add(text.toString());
			content.add(LANGUAGE);
			intent.setClass(ExistedFileActivity.this, ShowResultActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("tag", (Serializable) content);
			intent.putExtras(bundle);
			startActivity(intent);
		}else{
			Log.e("TAG", "error input");
		}
		
	}*/
	
	private String getMIMEType(File file) {
		String type = "*/*";
		String fileName = file.getName();
		int dotIndex = fileName.indexOf('.');
		if (dotIndex < 0) {
			return type;
		}
		String end = fileName.substring(dotIndex, fileName.length())
				.toLowerCase();
		if (end == "") {
			return type;
		}
		for (int i = 0; i < FileTypeBean.MIME_MapTable.length; i++) {
			if (end == FileTypeBean.MIME_MapTable[i][0]) {
				type = FileTypeBean.MIME_MapTable[i][1];
			}
		}
		return type;
	}
}
