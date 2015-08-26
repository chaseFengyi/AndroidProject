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
 * ����༭�����ұ߻����˵�
 * 
 * @author FengYi~ 2015��7��28��16:49:38
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
	// �洢������Ϣ
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
		// ���������ݽ�����ʾ
		setData2ListView();

		addNewTypeTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String title = "�������ƣ����";
				String desc = "�����˵�������";
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
	 * ����listView��item���±�����Ӧ�ķ�����
	 * 
	 * @param position
	 * @return ���ط�����
	 */
	public String getClassNameById(int position) {
		String itemName = ((NoteClassInfoBean) typesListView
				.getItemAtPosition(position)).getNoteClassName();
		return itemName;
	}

	/**
	 * ���ڷ������Ʋ�����ͬ����ô���ݷ������Ʋ��ұ���װ��bean
	 * 
	 * @param className
	 * @return ����bean����
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
	 * �����Է�����Ϣ������Ӧ���޸Ĳ���
	 */
	private void operateClasss() {
		final int posi = position;
		// ���峤����Ҫ��ʾ�Ĳ���
		final String[] mItems = { "ɾ���÷���", "�޸ķ�����" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle("�б�ѡ���").setItems(mItems,
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
									// �޸ķ�����
									String title = "�������ƣ����";
									String desc = "�����˵�������";
									addNewTypes(1, title, desc);
									break;
								}
							}
						});
		builder.create().show();
	}

	/**
	 * ɾ���ʼǷ���
	 */
	public void deleteClassName() {
		// ��ʼ����������
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"ɾ���У����Ժ󡣡���", getActivity());
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
					CustomToast.showToast(getActivity(), "ɾ���ɹ�", 2000);
					// ������ӵıʼǷ�����ʾ�ڱʼǷ�����
					classs.remove(position);
					adapter.notifyDataSetChanged();
				} else {
					CustomToast.showToast(getActivity(), "ɾ��ʧ��", 2000);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				// ʧ�ܣ��������
				mProgressDialog.dismiss();
				CustomToast.showToast(getActivity(), "�������Ӵ���", 2000);
			}
		});
		mRequestQueue.add(classRequest);
	}

	/**
	 * �ԱʼǷ�����޸�
	 */
	public void updateClassName() {
		// ��ȡ��Ӧid������
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

		// ��ʼ����������
		mRequestQueue = Volley.newRequestQueue(getActivity());
		// initDialog("�޸���,���Ժ�...");
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"�޸��У����Ժ󡣡�����", getActivity());
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
					CustomToast.showToast(getActivity(), "�޸ĳɹ�", 2000);
					// ������ӵıʼǷ�����ʾ�ڱʼǷ�����
					classs.get(mIndex).setNoteClassName(mClassName);
					classs.get(mIndex).setNoteClassDesc(mClassDesc);
					adapter.notifyDataSetChanged();
				} else {
					CustomToast.showToast(getActivity(), "�޸�ʧ��", 2000);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				// ʧ�ܣ��������
				mProgressDialog.dismiss();
				CustomToast.showToast(getActivity(), "�������Ӵ���", 2000);
			}
		});
		mRequestQueue.add(classRequest);

	}

	// ��listView�����������
	private void setData2ListView() {
		getNoteClassInfo();

	}

	/**
	 * �ӷ�������ȡ�ʼǷ����
	 */
	private void getNoteClassInfo() {
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"��ȡ���з�����Ϣ�����Ժ�....", getActivity());
		// ��ʼ����������
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mProgressDialog.show();
		HttpUtils classRequest = new HttpUtils(Method.GET, UrlUtils.NoteClass
				+ "/?check=" + mApp.getUser().getCheck(),
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						mProgressDialog.dismiss();
						// ��֤�ϴ����
						String check = CheckResultRequest.checkResult(response);
						parseClasssInfo(check);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						// ʧ�ܣ��������
						mProgressDialog.dismiss();
						CustomToast.showToast(getActivity(), "�������Ӵ���", 2000);
					}
				});
		mRequestQueue.add(classRequest);
	}

	/**
	 * �ӷ�������ȡ�ķ�����Ϣ��װ��bean����
	 * 
	 * @param check
	 */
	public void parseClasssInfo(String check) {
		if (check != null) {
			try {
				JSONArray jsonArray = new JSONArray(check);
				// ������ӵıʼǷ�����ʾ�ڱʼǷ�����
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
			CustomToast.showToast(getActivity(), "��ȡ�ʼǷ����ʧ��", 2000);
		}
	}

	// ����½���������µķ�����Ϣ
	/**
	 * @param tag
	 *            =0:��ʾ��ӷ��� tag=1:��ʾ�޸ķ���
	 */
	public void addNewTypes(int tag, String title, String desc) {

		final LinearLayout layout = (LinearLayout) AddName_DescView.name_desc(
				title, desc, getActivity());

		final int tags = tag;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle(getString(R.string.note)).setView((View) layout)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

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
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		builder.show();

	}

	/**
	 * �ϴ��½�����
	 */
	public void uploadNoteType() {
		// ��ʼ����������
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"�½������ϴ���,���Ժ�...", getActivity());
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
						// ��֤�ϴ����
						if (CheckResultRequest.checkResult(response) != null) {
							CustomToast.showToast(getActivity(), "��ӱʼǷ���ɹ�",
									2000);
							// ������ӵıʼǷ�����ʾ�ڱʼǷ�����
							getData();
							adapter.notifyDataSetChanged();
						} else {
							CustomToast.showToast(getActivity(), "��ӱʼǷ���ʧ��",
									2000);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						// ʧ�ܣ��������
						mProgressDialog.dismiss();
						CustomToast.showToast(getActivity(), "�������Ӵ���", 2000);
					}
				});
		mRequestQueue.add(classRequest);
	}

	/**
	 * ��ȡ�½��ķ�����Ϣ
	 */
	private void getData() {
		NoteClassInfoBean bean = new NoteClassInfoBean();
		bean.setNoteClassName(className.getText().toString().trim());
		bean.setNoteClassDesc(classdesc.getText().toString().trim());
		bean.setUserId(Integer.parseInt(mApp.getUser().getUserId()));
		classs.add(0, bean);
	}

	/**
	 * ��֤�û�����ķ�����Ϣ
	 */
	private boolean checkClassName_ClassDesc() {
		String mclassName = className.getText().toString();
		String mclassDesc = classdesc.getText().toString();
		if (mclassName.equals("") || mclassName == null) {
			CustomToast.showToast(getActivity(), "�ʼǷ�������δ��д�����ʧ��", 2000);
			className.requestFocus();
			return false;
		} else if (mclassDesc.equals("") || mclassDesc == null) {
			CustomToast.showToast(getActivity(), "�ʼǷ������ϸ������Ϣδ��д�����ʧ��", 2000);
			classdesc.requestFocus();
			return false;
		} else {
			// ��֤�½��ʼ����Ƿ�֮ǰ��
			if (classs != null || classs.size() > 0) {
				for (int i = 0; i < classs.size(); i++) {
					if (className.getText().toString()
							.equals(classs.get(i).getNoteClassName())) {
						CustomToast.showToast(getActivity(),
								"�����ʼ����У������ظ���ӣ����ʧ��", 2000);
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
