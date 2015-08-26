package com.allnotes.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.allnotes.R;
import com.allnotes.adapter.NodeDetailsFragmentAdapter;
import com.allnotes.app.App;
import com.allnotes.bean.NoteCommentInfoBean;
import com.allnotes.bean.NoteInfoBean;
import com.allnotes.customdialog.CustomDialog;
import com.allnotes.customtoast.CustomToast;
import com.allnotes.request.CheckResultRequest;
import com.allnotes.utils.HttpUtils;
import com.allnotes.utils.UrlUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 * 笔记部分笔记的详细信息
 * 
 * @author FengYi~
 * 
 */
public class NoteDetailsFragment extends Fragment {
	private ProgressDialog mProgressDialog = null;
	private RequestQueue mRequestQueue = null;
	// 笔记详情
	private TextView tv_details = null;
	// 对笔记评论时的
	private EditText et_noteComment = null;
	// 笔记评论内容
	private String commentContent;
	// 评论内容
	private ListView tv_content = null;
	// 保存评论内容的之前的数据
	private String old_comment_str;
	private TextView tv_add = null;
	private NodeDetailsFragmentAdapter adapter;
	// 存放评论内容
	private List<NoteCommentInfoBean> comments = new ArrayList<NoteCommentInfoBean>();

	public static String ARG_POSITION = "classid";
	public static String ARG_NAEM = "name";
	private int noteId = -1;
	private String classNam = "";
	private NoteInfoBean bean = null;
	// 表示点击修改或完成修改 0->点击修改
	private int tag = 0;
	private int tag_del = 0;
	private App mApp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_notedetails, null);
		mApp = (App) getActivity().getApplication();
		findview(view);
		return view;
	}

	private void findview(View view) {
		TextView textView = (TextView) getActivity().findViewById(R.id.topTv);
		textView.setText("笔记详细信息");
		getActivity().findViewById(R.id.topEdit).setVisibility(View.GONE);
		tv_details = (TextView) view.findViewById(R.id.notedetails_lists);
		tv_content = (ListView) view.findViewById(R.id.notedetails_comtent);
		tv_add = (TextView) view.findViewById(R.id.notedetails_add);
		// tv_add.setVisibility(View.INVISIBLE);
		setAdapter();
		// 获取noteId
		noteId = getArguments().getInt(ARG_POSITION);
		classNam = getArguments().getString(ARG_NAEM);
		System.out.println("note-details-noteid=" + noteId);
		getNoteDetailsInfo();

		tv_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addComments(noteId);
			}
		});

	}

	/**
	 * 添加评论
	 */
	public void addComments(int noteId) {
		final EditText editText = new EditText(getActivity());
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
		editText.setId(1);
		editText.setHint("请输入您的评论(不能为空)");
		editText.setSingleLine(false);
		final int id = noteId;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle("您正在对笔记进行评论").setView(editText)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						et_noteComment = (EditText) editText.findViewById(1);
						commentContent = et_noteComment.getText().toString();
						if (commentContent.equals("")) {
							CustomToast.showToast(getActivity(), "评论内容为空。评论失败",
									2000);
						} else {
							uploadNoteComment(id);
						}
					}
				});
		builder.create().show();
	}

	/**
	 * 上传添加评论
	 */
	private void uploadNoteComment(int noteId) {
		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(getActivity());
		// initDialog("上传中,请稍候...");
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"修改中，请稍后。。。。", getActivity());
		mProgressDialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("check", mApp.getUser().getCheck());
		rawParams.put("content", commentContent);
		final int id = noteId;
		HttpUtils classRequest = new HttpUtils(UrlUtils.Note_Comment_Add + "/"
				+ noteId, rawParams, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				mProgressDialog.dismiss();
				if (CheckResultRequest.checkResult(response) != null) {
					CustomToast.showToast(getActivity(), "评论成功", 2000);
					/*
					 * NoteCommentInfoBean bean = new NoteCommentInfoBean();
					 * bean.setContent(commentContent); Date date = new Date();
					 * SimpleDateFormat format = new SimpleDateFormat(
					 * "yyyy-MM-dd-HH:mm:ss"); String time =
					 * format.format(date); bean.setModified(time);
					 * bean.setNoteId(id); comments.add(bean);
					 * adapter.notifyDataSetChanged();
					 */
					getNoteDetailsInfo();
				} else {
					CustomToast.showToast(getActivity(), "评论失败", 2000);
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

	/**
	 * 从notefragment传递noteid参数到该fragment
	 * 
	 * @param noteId
	 */
	public void getNoteIdFromNoteFragment(int noteId) {
		this.noteId = noteId;
	}

	/**
	 * 在UI界面上显示获得的信息
	 */
	private void showContentInUI() {
		if (bean == null) {
			CustomToast.showToast(getActivity(), "暂无笔记详细信息", 2000);
			return;
		}
		tv_details.setText("text");

		String str_details = "所属分类：\t\t" + classNam + "\n\n" + "笔记名称：\t\t"
				+ Html.fromHtml(bean.getTitle()) + "\n\n" + "修改时间：\t\t"
				+ bean.getModified() + "\n\n" + "笔记内容：\t\t"
				+ Html.fromHtml(bean.getContent()) + "\n";
		tv_details.setText(str_details);
		if (bean.getComments() != null && bean.getComments().size() > 0) {
			StringBuffer buffer = new StringBuffer();

			for (int i = 0; i < bean.getComments().size(); i++) {
				NoteCommentInfoBean noteCommentInfoBean = new NoteCommentInfoBean();
				noteCommentInfoBean.setCommentId((Integer) bean.getComments()
						.get(i).get("id"));
				noteCommentInfoBean.setNoteId(noteId);
				noteCommentInfoBean.setModified((String) bean.getComments()
						.get(i).get("modified"));
				noteCommentInfoBean.setContent((String) bean.getComments()
						.get(i).get("content"));
				comments.add(noteCommentInfoBean);
			}

		} else {
			tv_add.setVisibility(View.VISIBLE);
			CustomToast.showToast(getActivity(), "暂无评论", 2000);
		}
		if (comments != null) {
			adapter = new NodeDetailsFragmentAdapter(getActivity(), comments,
					mApp);
			tv_content.setAdapter(adapter);
		}

	}

	/**
	 * 获取笔记详细信息
	 */
	private void getNoteDetailsInfo() {
		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"获取笔记详情中,请稍候...", getActivity());
		mProgressDialog.show();
		HttpUtils request = new HttpUtils(Method.GET,
				UrlUtils.Note_Details_Show + "/" + noteId + "?check="
						+ mApp.getUser().getCheck(), new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						System.out.println("details====" + response);
						mProgressDialog.dismiss();
						String check = CheckResultRequest.checkResult(response);
						parseDetailsInfo(check);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.e("TAG", error.getMessage(), error);
						// 失败，网络错误
						mProgressDialog.dismiss();
						CustomToast.showToast(getActivity(), "网络连接错误", 2000);
					}
				});
		// 对Request设置重试策略时，更改默认超时时间
		request.setRetryPolicy(new DefaultRetryPolicy(500000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		mRequestQueue.add(request);

	}

	/**
	 * 从服务器获取的分类信息包装到bean里面
	 * 
	 * @param check
	 */
	public void parseDetailsInfo(String check) {
		try {
			JSONObject object = new JSONObject(check);
			JSONObject jsonObject2 = object.optJSONObject("0");
			if (jsonObject2 != null) {
				// 将新添加的笔记分类显示在笔记分类中
				bean = new NoteInfoBean();
				bean.setTitle(jsonObject2.optString("title"));
				bean.setAuthor(jsonObject2.optString("author"));
				bean.setUserId(jsonObject2.optInt("userid"));
				bean.setClassId(jsonObject2.optInt("classid"));
				bean.setNoteId(jsonObject2.optInt("id"));
				bean.setSrc(jsonObject2.optString("src"));
				bean.setDate(jsonObject2.optString("date"));
				bean.setPassword(jsonObject2.optString("password"));
				bean.setIspw(jsonObject2.optInt("ispw"));
				bean.setModified(jsonObject2.optString("modified"));
				bean.setDesc(jsonObject2.optString("desc"));
				bean.setContent(jsonObject2.optString("content"));
				bean.setCcount(jsonObject2.optInt("ccount"));
				JSONArray array = object.optJSONArray("comments");
				if (array.length() > 0) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonObject = array.getJSONObject(i);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", jsonObject.optInt("id"));
						map.put("noteid", jsonObject.optInt("noteid"));
						map.put("modified", jsonObject.optString("modified"));
						map.put("content", jsonObject.optString("content"));
						list.add(map);
					}
					bean.setComments(list);
				}
			}
			showContentInUI();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 设置布局的高度，宽度
	 */
	private void setAdapter() {
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);

		int height = metrics.heightPixels;
		tv_details.setHeight(height * 1 / 2);
	}

}
