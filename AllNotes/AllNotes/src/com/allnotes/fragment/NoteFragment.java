package com.allnotes.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.allnotes.R;
import com.allnotes.activity.login.LoginActivity;
import com.allnotes.adapter.NoteFragmentAdapter;
import com.allnotes.app.App;
import com.allnotes.bean.NoteInfoBean;
import com.allnotes.customdialog.CustomDialog;
import com.allnotes.customtoast.CustomToast;
import com.allnotes.request.CheckResultRequest;
import com.allnotes.utils.HttpUtils;
import com.allnotes.utils.UrlUtils;
import com.allnotes.view.AddName_DescView;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * @author FengYi~ 笔记 2015年7月28日16:50:29
 */
public class NoteFragment extends Fragment implements OnClickListener {
	private Button editBtn = null;
	private TextView addNewNoteTxt = null;
	private ImageView addNewImg = null;
	private ListView notesListView = null;
	private ProgressDialog mProgressDialog = null;
	private RequestQueue mRequestQueue = null;
	private EditText noteName = null;
	private EditText noteDesc = null;
	// 对笔记评论时的
	private EditText et_noteComment = null;
	// 笔记评论内容
	private String commentContent;
	private int classId;
	private NoteFragmentAdapter adapter = null;
	public int noteId = -1;
	private int position = -1;
	private FragmentManager manager;
	private FragmentTransaction ft;

	private App mApp;

	// 存储笔记信息
	private List<NoteInfoBean> notes = new ArrayList<NoteInfoBean>();

	// 标记是否要新建笔记本 1:不新建 0:新建
	private int isAddNew = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_note, null);
		manager = getFragmentManager();
		mApp = (App) getActivity().getApplication();
		findView(view);
		return view;
	}

	private void findView(View view) {
		TextView textView = (TextView) getActivity().findViewById(R.id.topTv);
		textView.setText("笔记");
		editBtn = (Button) getActivity().findViewById(R.id.topEdit);
		editBtn.setText("类别");
		editBtn.setVisibility(View.VISIBLE);
		addNewNoteTxt = (TextView) view.findViewById(R.id.note_add);
		addNewImg = (ImageView) view.findViewById(R.id.note_btn_replay);
		notesListView = (ListView) view.findViewById(R.id.note_listView);

		// 将已有笔记列表进行显示
		setNotes2ListView();

		addNewNoteTxt.setVisibility(View.INVISIBLE);
		editBtn.setOnClickListener(this);
		addNewNoteTxt.setOnClickListener(this);
		addNewImg.setOnClickListener(this);

		// 长按实现对相应条目的修改等操作
		notesListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				NoteFragment.this.position = position;
				operateNotes();
				return true;
			}
		});

		// 点击查看该笔记的详细信息
		notesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				NoteDetailsFragment detailsFragment = new NoteDetailsFragment();
				noteId = getInfoByNotesName(getNotesNameById(position))
						.getNoteId();
				ft = manager.beginTransaction();
				Bundle bundle = new Bundle();
				bundle.putInt(NoteDetailsFragment.ARG_POSITION, noteId);
				bundle.putString(NoteDetailsFragment.ARG_NAEM,
						RightFragment.classNameStr);
				detailsFragment.setArguments(bundle);
				ft.replace(R.id.content_frame, detailsFragment);
				ft.addToBackStack(null);
				ft.commitAllowingStateLoss();
			}
		});

	}

	/**
	 * 长按对信息进行相应的修改操作
	 */
	private void operateNotes() {
		final int posi = position;
		// 定义长按后要显示的操作
		final String[] mItems = { "删除该分类", "修改分类名", "添加评论" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle("列表选择框").setItems(mItems,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								noteId = getInfoByNotesName(
										getNotesNameById(posi)).getNoteId();
								switch (which) {
								case 0:
									deleteNotesName();
									break;
								case 1:
									// 修改分类名
									String title = "笔记名称（必填）";
									String desc = "笔记的说明（必填）";
									addNewTypes(1, title, desc);
									break;
								case 2:
									// 添加评论
									String noteName = getNotesNameById(posi);
									addComment(noteName);
									break;
								}
							}
						});
		builder.create().show();
	}

	/**
	 * 添加评论
	 */
	public void addComment(String noteName) {
		final EditText editText = new EditText(getActivity());
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
		editText.setId(1);
		editText.setHint("请输入您的评论(不能为空)");
		editText.setSingleLine(false);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle("您正在对笔记（" + noteName + "）进行评论").setView(editText)
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
							uploadNoteComment(noteId);
						}
					}
				});
		builder.create().show();
	}

	/**
	 * 上传评论
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
		HttpUtils classRequest = new HttpUtils(UrlUtils.Note_Comment_Add + "/"
				+ noteId, rawParams, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				mProgressDialog.dismiss();
				if (CheckResultRequest.checkResult(response) != null) {
					CustomToast.showToast(getActivity(), "评论成功,点击进入查看评论", 2000);
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

	// 点击新建分类添加新的分类信息
	/**
	 * @param tag
	 *            =0:表示添加分类 tag=1:表示修改分类
	 */
	public void addNewTypes(int tag, String title, String desc) {

		final LinearLayout layout = (LinearLayout) AddName_DescView.name_desc(
				title, desc, getActivity());

		final int tags = tag;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle(getString(R.string.note)).setView((View) layout)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						noteName = (EditText) layout.findViewById(2);
						noteDesc = (EditText) layout.findViewById(4);
						if (checkNoteName_NotsDesc()) {
							if (tags == 0)
								uploadNoteType();
							else if (tags == 1)
								updateClassName();
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		builder.show();

	}

	/**
	 * 对笔记分类的修改
	 */
	public void updateClassName() {
		// 获取对应id的索引
		int index = -1;
		if (notes == null || notes.size() <= 0)
			return;
		for (int i = 0; i < notes.size(); i++) {
			if (noteId == notes.get(i).getNoteId()) {
				index = i;
			}
		}
		final String mNoteName = noteName.getText().toString();
		final String mNoteDesc = noteDesc.getText().toString();
		final int mIndex = index;

		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(getActivity());
		// initDialog("修改中,请稍候...");
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"修改中，请稍后。。。。", getActivity());
		mProgressDialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("_method", "put");
		rawParams.put("check", mApp.getUser().getCheck());
		rawParams.put("classid", classId + "");
		rawParams.put("content", mNoteDesc);
		rawParams.put("title", mNoteName);
		rawParams.put("author", "XUPTCHASE");
		HttpUtils classRequest = new HttpUtils(UrlUtils.Note_Lists_Update + "/"
				+ noteId, rawParams, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				mProgressDialog.dismiss();
				if (CheckResultRequest.checkResult(response) != null) {
					CustomToast.showToast(getActivity(), "修改成功", 2000);
					// 将新添加的笔记显示在笔记列表中
					notes.get(mIndex).setTitle(mNoteName);
					notes.get(mIndex).setContent(mNoteDesc);
					adapter.notifyDataSetChanged();
				} else {
					CustomToast.showToast(getActivity(), "修改失败", 2000);
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

	public void deleteNotesName() {
		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"删除中，请稍后。。。", getActivity());
		mProgressDialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("check", mApp.getUser().getCheck());
		rawParams.put("_method", "delete");
		System.out.println("notesId=" + noteId);
		HttpUtils request = new HttpUtils(UrlUtils.Note_Lists_delete + "/"
				+ noteId, rawParams, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				mProgressDialog.dismiss();
				System.out.println("delete---response" + response);
				if (CheckResultRequest.checkResult(response) != null) {
					CustomToast.showToast(getActivity(), "删除成功", 2000);
					// 将新添加的笔记分类显示在笔记分类中
					notes.remove(position);
					adapter.notifyDataSetChanged();
				} else {
					CustomToast.showToast(getActivity(), "删除失败", 2000);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				byte[] htmlBodyBytes = error.networkResponse.data;
				Log.e("LOGIN-ERROR", new String(htmlBodyBytes), error);
				// 失败，网络错误
				mProgressDialog.dismiss();
				CustomToast.showToast(getActivity(), "网络连接错误", 2000);
			}
		});
		mRequestQueue.add(request);

	}

	/**
	 * 根据listView的item的下标找相应的笔记名
	 * 
	 * @param position
	 * @return 返回分类名
	 */
	public String getNotesNameById(int position) {
		String itemName = ((NoteInfoBean) notesListView
				.getItemAtPosition(position)).getTitle();
		return itemName;
	}

	/**
	 * 由于分类名称不能相同，那么根据分类名称查找被封装的bean
	 * 
	 * @param className
	 * @return 返回bean对象
	 */
	public NoteInfoBean getInfoByNotesName(String noteName) {
		if (notes != null || notes.size() > 0) {
			for (int i = 0; i < notes.size(); i++) {
				if (notes.get(i).getTitle().equals(noteName)) {
					return notes.get(i);
				}
			}
		}
		return null;
	}

	/**
	 * 从服务器获取笔记列表
	 */
	private void getNotesInfo() {
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"获取已有信息表，请稍后....", getActivity());
		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mProgressDialog.show();
		HttpUtils noteRequest = new HttpUtils(Method.GET, UrlUtils.Note_lists
				+ "/?check=" + mApp.getUser().getCheck() + "&classid="
				+ RightFragment.classId + "&page=0",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						System.out.println("response=" + response);
						mProgressDialog.dismiss();
						// 验证上传结果
						String check = CheckResultRequest.checkResult(response);
						parseClasssInfo(check);
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
		mRequestQueue.add(noteRequest);
	}

	/**
	 * 从服务器获取的分类信息包装到bean里面
	 * 
	 * @param check
	 */
	public void parseClasssInfo(String check) {
		if (check != null) {
			try {
				JSONArray jsonArray = new JSONArray(check);
				// 将新添加的笔记分类显示在笔记分类中
				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						if (jsonObject2.getString("title").equals("")) {
							continue;
						} else {
							NoteInfoBean bean = new NoteInfoBean();
							bean.setTitle(jsonObject2.getString("title"));
							bean.setNoteId(jsonObject2.getInt("id"));
							bean.setAuthor(jsonObject2.getString("author"));
							bean.setIspw(jsonObject2.getInt("ispw"));
							bean.setPassword(jsonObject2.getString("password"));
							bean.setModified(jsonObject2.getString("modified"));
							notes.add(bean);
						}
					}
					
					Set<NoteInfoBean> set = new HashSet<NoteInfoBean>();
					set.addAll(notes);
					notes.clear();
					notes.addAll(set);

				}
				// classId=-1表明沒有点击笔记分类而直接在服务器查找，那么会找不带
				if (RightFragment.classId == -1 || notes == null) {
					CustomToast.showToast(getActivity(), "暂没有任何笔记", 2000);
					return;
				} else {
					adapter = new NoteFragmentAdapter(getActivity(), notes);
					notesListView.setAdapter(adapter);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			CustomToast.showToast(getActivity(), "获取笔记分类表失败", 2000);
		}
	}

	private void dismissTheSame(){
		if(notes != null){
		}
	}
	
	private void setNotes2ListView() {

		getNotesInfo();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.topEdit:
			((SlidingFragmentActivity) getActivity()).showSecondaryMenu();
			break;
		case R.id.note_add:
			if (RightFragment.classId == -1) {
				CustomToast.showToast(getActivity(), "请点击右上角的按钮先选择分类类别再添加新的",
						2000);
				return;
			}
			// 添加新的笔记本
			String title = "笔记名称（必填）";
			String desc = "笔记的内容（必填）";
//			addNewTypes(0, title, desc);
			AddNewNoteFragment addNewNoteFragment = new AddNewNoteFragment();
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.content_frame, addNewNoteFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commitAllowingStateLoss();
		
			break;
		case R.id.note_btn_replay:
			if (isAddNew == 0) {// 新建
				addNewImg.setImageResource(R.drawable.list_edit_selected);
				isAddNew = 1;
				addNewNoteTxt.setVisibility(View.VISIBLE);
			} else if (isAddNew == 1) {// 不新建
				addNewImg.setImageResource(R.drawable.list_edit);
				isAddNew = 0;
				addNewNoteTxt.setVisibility(View.INVISIBLE);
			}
			break;
		}
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
		rawParams.put("content", noteDesc.getText().toString().trim());
		rawParams.put("title", noteName.getText().toString().trim());
		rawParams.put("author", "XUPTCHASE");
		System.out.println("rawParams=" + rawParams);
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
							// 将新添加的笔记显示在笔记列表中
							getData();
							adapter.notifyDataSetChanged();
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

	/**
	 * 获取新建的笔记信息
	 */
	private void getData() {
		NoteInfoBean bean = new NoteInfoBean();
		bean.setTitle(noteName.getText().toString().trim());
		bean.setContent(noteDesc.getText().toString().trim());
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		String time = format.format(date);
		bean.setModified(time);
		bean.setAuthor("XUPTCHASE");// 由于详细个人信息未写，所以这里写的是伪姓名
		bean.setUserId(Integer.parseInt(mApp.getUser().getUserId()));
		notes.add(0, bean);
	}

	/**
	 * 验证用户输入的分类信息
	 */
	private boolean checkNoteName_NotsDesc() {
		String mclassName = noteName.getText().toString();
		String mclassDesc = noteDesc.getText().toString();
		if (mclassName.equals("") || mclassName == null) {
			CustomToast.showToast(getActivity(), "笔记名称未填写，添加失败", 2000);
			return false;
		} else if (mclassDesc.equals("") || mclassDesc == null) {
			CustomToast.showToast(getActivity(), "笔记的详细描述信息未填写，添加失败", 2000);
			return false;
		} else {
			// 验证新建笔记名是否之前有
			if (notes != null || notes.size() > 0) {
				for (int i = 0; i < notes.size(); i++) {
					if (noteName.getText().toString()
							.equals(notes.get(i).getTitle())) {
						CustomToast.showToast(getActivity(),
								"该类别笔记已有，不能重复添加，添加失败", 2000);
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mRequestQueue != null)
			mRequestQueue.cancelAll(getActivity());
		if(notes != null)
			notes.clear();
	}

}
