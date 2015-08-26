package com.allnotes.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.allnotes.R;
import com.allnotes.app.App;
import com.allnotes.customdialog.CustomDialog;
import com.allnotes.customtoast.CustomToast;
import com.allnotes.request.CheckResultRequest;
import com.allnotes.utils.HttpUtils;
import com.allnotes.utils.UrlUtils;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class AddNewNoteFragment extends Fragment {
	private EditText titleET; // 标题
	private EditText contentET; // 内容
	private Button saveBTN;//保存 
	
	private SimpleDateFormat simpledateformat;
	private ProgressDialog mProgressDialog = null;
	private RequestQueue mRequestQueue = null;
	private App mApp;
	
	private String content;
	private String title;
	private String time;
	
	public static final String TAGCONTENT = "content";
	public static final String TAGTITLE = "title";
	public static final String TAGTIME = "time";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_addnew, null);

		mApp = (App) getActivity().getApplication();
		findview(view);
		
		return view;
	}
	
	private void findview(View view){
		titleET = (EditText)view.findViewById(R.id.editTitle);
		contentET = (EditText)view.findViewById(R.id.editContent);
		saveBTN = (Button)getActivity().findViewById(R.id.topEdit);
		saveBTN.setText("保存");
		
		saveBTN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isEmpty())
					insert();	
			}
		});
	}
	
	/**
	 * 判断是否是空
	 * @return
	 */
	private boolean isEmpty(){
		if(titleET.getText().toString().trim().equals("")){
			CustomToast.showToast(getActivity(), "标题为空，请输入标题", 2000);
			titleET.requestFocus();
			return false;
		}else if(contentET.getText().toString().trim().equals("")){
			CustomToast.showToast(getActivity(), "内容为空，请输入内容", 2000);
			contentET.requestFocus();
			return false;
		}else
			return true;
	}
	
	/**
	 * 插入数据
	 */
	private void insert(){
		// 富文本转为html
		Editable editable = contentET.getEditableText();
		content = hideReturn(Html.toHtml(editable));   //toHtml默认标签是<p>  eg:<p dir=\"ltr\">ppp<\/p>
		content = replaceTag(content);
		Editable editable2 = titleET.getEditableText();
		title = hideReturn(Html.toHtml(editable2));
		title = replaceTag(title);
		time = getTime();
		uploadNoteType();
	}
	
	/**
	 * 将html中的p便签转换为b标签
	 * @param string
	 * @return
	 */
	public String replaceTag(String string){
		String dest = "";
		if(null != string){
			dest = string.replaceFirst(string.charAt(1)+"", "b");
			dest = dest.replaceFirst(dest.charAt(dest.length()-2)+"", "b");
		}
		return dest;
	}
	
	/**
	 * 消除字符串里面的\n
	 * @param string
	 * @return
	 */
	public String hideReturn(String string){
		String dest = "";
		if(string != null){
			Pattern pattern = Pattern.compile("\n");
			Matcher matcher = pattern.matcher(string);
			dest = matcher.replaceAll("");
		}
		return dest;
	}
	
	/**
	 * 上传新建笔记
	 */
	public void uploadNoteType() {
		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"新建分类上传中,请稍候...", getActivity());
		mProgressDialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("check", mApp.getUser().getCheck());
		rawParams.put("classid", RightFragment.classId + "");
		rawParams.put("content", content);
		rawParams.put("title", title);
		rawParams.put("author", "XUPTCHASE");
		HttpUtils classRequest = new HttpUtils(UrlUtils.Note_Create, rawParams,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						mProgressDialog.dismiss();
						System.out.println("笔记=" + response);
						// 验证上传结果
						if (CheckResultRequest.checkResult(response) != null) {
							CustomToast.showToast(getActivity(), "添加笔记分类成功",
									2000);
							// 添加成功后，跳转到notefragment页面显示数据
							FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
							NoteFragment noteFragment = new NoteFragment();
							Bundle bundle = new Bundle();
							bundle.putString(TAGCONTENT, content);
							bundle.putString(TAGTITLE, title);
							bundle.putString(TAGTIME, time);
							noteFragment.setArguments(bundle);
							fragmentTransaction.replace(R.id.content_frame, noteFragment);
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.commitAllowingStateLoss();
							titleET.setText("");
							contentET.setText("");
						} else {
							CustomToast.showToast(getActivity(), "添加笔记分类失败",
									2000);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						// 失败，网络错误
						mProgressDialog.dismiss();
						CustomToast.showToast(getActivity(), "网络连接错误", 2000);
					}
				});
		mRequestQueue.add(classRequest);
	}

	
	// 得到当前时间
	private String getTime() {
		simpledateformat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss ");
		return simpledateformat.format(new Date());
	}
	
}
