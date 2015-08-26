package com.allnotes.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.allnotes.R;
import com.allnotes.app.App;
import com.allnotes.bean.NoteCommentInfoBean;
import com.allnotes.customdialog.CustomDialog;
import com.allnotes.customtoast.CustomToast;
import com.allnotes.request.CheckResultRequest;
import com.allnotes.utils.HttpUtils;
import com.allnotes.utils.UrlUtils;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;

public class NodeDetailsFragmentAdapter extends BaseAdapter {
	// 对笔记评论时的
	private EditText et_noteComment = null;
	// 笔记评论内容
	private String commentContent;
	private App mApp;
	private ProgressDialog mProgressDialog = null;
	private RequestQueue mRequestQueue = null;
	private static Context context;
	private List<NoteCommentInfoBean> list;
	private LayoutInflater inflater;
	// 表示点击修改或完成修改 0->点击修改
	private int tag = 0;
	private int tag_del = 0;
	
	public NodeDetailsFragmentAdapter(Context context,
			List<NoteCommentInfoBean> list, App mApp) {
		this.mApp = mApp;
		this.context = context;
		this.list = list;

		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		final int posi = position;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.sample_notedetails, null);
			holder = new ViewHolder();
			holder.content = (TextView) convertView
					.findViewById(R.id.sample_details_content);
			holder.modified = (TextView) convertView
					.findViewById(R.id.sample_details_modified);
			holder.delete = (TextView) convertView
					.findViewById(R.id.sample_details_delete);
			holder.update = (TextView) convertView
					.findViewById(R.id.sample_details_update);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.content.setText(list.get(position).getContent());
		holder.modified.setText(list.get(position).getModified());

		holder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteComment(holder, posi);
			}
		});

		holder.update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateComments(holder, posi);
			}
		});

		return convertView;
	}

	/**
	 * 删除评论
	 */
	private void deleteComment(ViewHolder holder, int position) {
		uploadDelete(position);
	}

	/**
	 * 上传删除信息
	 */
	private void uploadDelete(int position) {
		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(context);
		// initDialog("上传中,请稍候...");
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"修改中，请稍后。。。。", context);
		mProgressDialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("_method", "delete");
		rawParams.put("check", mApp.getUser().getCheck());
		final int posi = position;
		HttpUtils classRequest = new HttpUtils(UrlUtils.Note_Comment_Delete
				+ "/" + list.get(position).getCommentId(), rawParams,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						mProgressDialog.dismiss();
						if (CheckResultRequest.checkResult(response) != null) {
							CustomToast.showToast(context, "删除成功", 2000);
							list.remove(posi);
							notifyDataSetChanged();
						} else {
							CustomToast.showToast(context, "删除失败", 2000);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						// 失败，网络错误
						mProgressDialog.dismiss();
						CustomToast.showToast(context, "网络连接错误", 2000);
					}
				});
		mRequestQueue.add(classRequest);

	}

	/**
	 * 修改评论
	 */
	private void updateComments(ViewHolder holder, int position) {
			final EditText editText = new EditText(context);
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			editText.setId(1);
			editText.setText(list.get(position).getContent());
			editText.setHint("请输入您的评论(不能为空)");
			editText.setSingleLine(false);
			final int posi = position;
			AlertDialog.Builder builder = new AlertDialog.Builder(context)
					.setTitle(
							"您正在对评论（" + list.get(position).getContent()
									+ "）进行修改")
					.setView(editText)
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									et_noteComment = (EditText) editText
											.findViewById(1);
									commentContent = et_noteComment.getText()
											.toString();
									if (commentContent.equals("")) {
										CustomToast.showToast(context,
												"评论内容为空。评论失败", 2000);
									} else {
										System.out.println("adapter-commentid="+list.get(posi).getCommentId());
										loadUpdate(list.get(posi)
												.getCommentId(), posi);
									}
								}
							});
			builder.create().show();
			tag = 1;
	
	}

	/**
	 * 上传修改内容
	 */
	private void loadUpdate(int commentId, int position) {
		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(context);
		// initDialog("上传中,请稍候...");
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"修改中，请稍后。。。。", context);
		mProgressDialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("_method", "put");
		rawParams.put("check", mApp.getUser().getCheck());
		rawParams.put("content", commentContent);
		final int posi = position;
		HttpUtils classRequest = new HttpUtils(UrlUtils.Note_Comment_Update
				+ list.get(position).getCommentId(), rawParams,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						mProgressDialog.dismiss();
						if (CheckResultRequest.checkResult(response) != null) {
							CustomToast.showToast(context, "修改成功", 2000);
							list.get(posi).setContent(commentContent);
							Date date = new Date();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
							String time = format.format(date);
							list.get(posi).setModified(time);
							notifyDataSetChanged();
						} else {
							CustomToast.showToast(context, "修改失败", 2000);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						// 失败，网络错误
						mProgressDialog.dismiss();
						CustomToast.showToast(context, "网络连接错误", 2000);
					}
				});
		mRequestQueue.add(classRequest);

	}

	
	static class ViewHolder {
		private TextView content;
		private TextView modified;
		private TextView delete;
		private TextView update;

	}

}
