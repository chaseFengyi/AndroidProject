package com.allnotes.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.allnotes.R;
import com.allnotes.activity.main.MainActivity;
import com.allnotes.adapter.RightFragmentAdapter;
import com.allnotes.app.App;
import com.allnotes.bean.NoteClassInfoBean;
import com.allnotes.customdialog.CustomDialog;
import com.allnotes.customtoast.CustomToast;
import com.allnotes.request.CheckResultRequest;
import com.allnotes.utils.HttpUtils;
import com.allnotes.utils.UrlUtils;
import com.allnotes.view.AddName_DescView;
import com.allnotes.view.ListViewHeightView;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 * 点击编辑弹出右边滑动菜单
 * 
 * @author FengYi~ 2015年7月28日16:49:38
 */
public class RightFragment extends Fragment {
	private RequestQueue mRequestQueue = null;
	private TextView addNewTypeTxt = null;
	private ListView typesListView = null;
	private EditText className = null;
	private EditText classdesc = null;
	private ProgressDialog mProgressDialog = null;
	private RightFragmentAdapter adapter = null;
	public static int classId = -1;
	public static String classNameStr = "";
	private int position = -1;
	// 存储分类信息
	public List<NoteClassInfoBean> classs = new ArrayList<NoteClassInfoBean>();

	private App mApp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_menu_right_note, null);
		mApp = (App) getActivity().getApplication();
		findViews(view);
		return view;
	}

	public void findViews(View view) {
		addNewTypeTxt = (TextView) view
				.findViewById(R.id.right_note_addNewType);
		typesListView = (ListView) view.findViewById(R.id.right_note_types);

		ListViewHeightView.setListViewHeight(getActivity(), typesListView, 11,
				12);
		// 将已有数据进行显示
		setData2ListView();

		addNewTypeTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String title = "分类名称（必填）";
				String desc = "分类的说明（必填）";
				addNewTypes(0, title, desc);
			}
		});

		typesListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				RightFragment.this.position = position;
				operateClasss();
				return true;
			}
		});

		typesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				classId = getInfoByClassName(getClassNameById(position))
						.getNoteClassId();
				classNameStr = getClassNameById(position);
				NoteFragment fragment = new NoteFragment();
				if (fragment != null) {
					if (getActivity() == null)
						return;
					if (getActivity() instanceof MainActivity) {
						MainActivity fca = (MainActivity) getActivity();
						fca.switchConent(fragment, getString(R.string.note));
					}
				}
			}
		});
	}

	/**
	 * 根据listView的item的下标找相应的分类名
	 * 
	 * @param position
	 * @return 返回分类名
	 */
	public String getClassNameById(int position) {
		String itemName = ((NoteClassInfoBean) typesListView
				.getItemAtPosition(position)).getNoteClassName();
		return itemName;
	}

	/**
	 * 由于分类名称不能相同，那么根据分类名称查找被封装的bean
	 * 
	 * @param className
	 * @return 返回bean对象
	 */
	public NoteClassInfoBean getInfoByClassName(String className) {
		if (classs != null || classs.size() > 0) {
			for (int i = 0; i < classs.size(); i++) {
				if (classs.get(i).getNoteClassName().equals(className)) {
					return classs.get(i);
				}
			}
		}
		return null;
	}

	/**
	 * 长按对分类信息进行相应的修改操作
	 */
	private void operateClasss() {
		final int posi = position;
		// 定义长按后要显示的操作
		final String[] mItems = { "删除该分类", "修改分类名" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle("列表选择框").setItems(mItems,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								classId = getInfoByClassName(
										getClassNameById(posi))
										.getNoteClassId();
								switch (which) {
								case 0:
									deleteClassName();
									break;
								case 1:
									// 修改分类名
									String title = "分类名称（必填）";
									String desc = "分类的说明（必填）";
									addNewTypes(1, title, desc);
									break;
								}
							}
						});
		builder.create().show();
	}

	/**
	 * 删除笔记分类
	 */
	public void deleteClassName() {
		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"删除中，请稍后。。。", getActivity());
		mProgressDialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("check", mApp.getUser().getCheck());
		rawParams.put("_method", "delete");
		HttpUtils classRequest = new HttpUtils(UrlUtils.NoteClass_Delete + "/"
				+ classId, rawParams, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				mProgressDialog.dismiss();
				if (CheckResultRequest.checkResult(response) != null) {
					CustomToast.showToast(getActivity(), "删除成功", 2000);
					// 将新添加的笔记分类显示在笔记分类中
					classs.remove(position);
					adapter.notifyDataSetChanged();
				} else {
					CustomToast.showToast(getActivity(), "删除失败", 2000);
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
	 * 对笔记分类的修改
	 */
	public void updateClassName() {
		// 获取对应id的索引
		int index = -1;
		if (classs == null || classs.size() <= 0)
			return;
		for (int i = 0; i < classs.size(); i++) {
			if (classId == classs.get(i).getNoteClassId()) {
				index = i;
			}
		}
		final String mClassName = className.getText().toString();
		final String mClassDesc = classdesc.getText().toString();
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
		System.out.println("className=" + getClassNameById(position));
		rawParams.put("classname", mClassName);
		rawParams.put("classdesc", mClassDesc);
		HttpUtils classRequest = new HttpUtils(UrlUtils.NoteClass_Update + "/"
				+ classId, rawParams, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				mProgressDialog.dismiss();
				if (CheckResultRequest.checkResult(response) != null) {
					CustomToast.showToast(getActivity(), "修改成功", 2000);
					// 将新添加的笔记分类显示在笔记分类中
					classs.get(mIndex).setNoteClassName(mClassName);
					classs.get(mIndex).setNoteClassDesc(mClassDesc);
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

	// 给listView添加已有数据
	private void setData2ListView() {
		getNoteClassInfo();

	}

	/**
	 * 从服务器获取笔记分类表
	 */
	private void getNoteClassInfo() {
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"获取已有分类信息表，请稍后....", getActivity());
		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mProgressDialog.show();
		HttpUtils classRequest = new HttpUtils(Method.GET, UrlUtils.NoteClass
				+ "/?check=" + mApp.getUser().getCheck(),
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
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
		mRequestQueue.add(classRequest);
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
						if (jsonObject2.getString("classname").equals("")) {
							continue;
						} else {
							NoteClassInfoBean bean = new NoteClassInfoBean();
							bean.setNoteClassId(jsonObject2.getInt("id"));
							bean.setNoteClassName(jsonObject2
									.getString("classname"));
							bean.setNoteClassDesc(jsonObject2
									.getString("classdesc"));
							bean.setNoteReadNum(jsonObject2.getInt("readnum"));
							bean.setNoteFlagNum(jsonObject2.getInt("flagnum"));
							bean.setNoteArticleNum(jsonObject2
									.getInt("articlenum"));
							bean.setPassword(jsonObject2.getString("password"));
							bean.setNoteIspw(jsonObject2.getInt("ispw"));
							bean.setUserId(jsonObject2.getInt("userid"));
							classs.add(bean);
						}
					}

				}
				if (classs != null) {
					adapter = new RightFragmentAdapter(getActivity(), classs);
					typesListView.setAdapter(adapter);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			CustomToast.showToast(getActivity(), "获取笔记分类表失败", 2000);
		}
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
						className = (EditText) layout.findViewById(2);
						classdesc = (EditText) layout.findViewById(4);
						if (checkClassName_ClassDesc()) {
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
	 * 上传新建分类
	 */
	public void uploadNoteType() {
		// 初始化网络请求
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"新建分类上传中,请稍候...", getActivity());
		mProgressDialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("check", mApp.getUser().getCheck());
		rawParams.put("classname", className.getText().toString().trim());
		rawParams.put("classdesc", classdesc.getText().toString().trim());
		HttpUtils classRequest = new HttpUtils(UrlUtils.CreateNoteClass,
				rawParams, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						mProgressDialog.dismiss();
						// 验证上传结果
						if (CheckResultRequest.checkResult(response) != null) {
							CustomToast.showToast(getActivity(), "添加笔记分类成功",
									2000);
							// 将新添加的笔记分类显示在笔记分类中
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
	 * 获取新建的分类信息
	 */
	private void getData() {
		NoteClassInfoBean bean = new NoteClassInfoBean();
		bean.setNoteClassName(className.getText().toString().trim());
		bean.setNoteClassDesc(classdesc.getText().toString().trim());
		bean.setUserId(Integer.parseInt(mApp.getUser().getUserId()));
		classs.add(0, bean);
	}

	/**
	 * 验证用户输入的分类信息
	 */
	private boolean checkClassName_ClassDesc() {
		String mclassName = className.getText().toString();
		String mclassDesc = classdesc.getText().toString();
		if (mclassName.equals("") || mclassName == null) {
			CustomToast.showToast(getActivity(), "笔记分类名称未填写，添加失败", 2000);
			className.requestFocus();
			return false;
		} else if (mclassDesc.equals("") || mclassDesc == null) {
			CustomToast.showToast(getActivity(), "笔记分类的详细描述信息未填写，添加失败", 2000);
			classdesc.requestFocus();
			return false;
		} else {
			// 验证新建笔记名是否之前有
			if (classs != null || classs.size() > 0) {
				for (int i = 0; i < classs.size(); i++) {
					if (className.getText().toString()
							.equals(classs.get(i).getNoteClassName())) {
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
		mRequestQueue.cancelAll(getActivity());
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (classId != -1)
			classId = -1;
	}

}
