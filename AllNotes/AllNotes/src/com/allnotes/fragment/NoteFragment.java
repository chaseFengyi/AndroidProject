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
 * @author FengYi~ �ʼ� 2015��7��28��16:50:29
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
	// �Աʼ�����ʱ��
	private EditText et_noteComment = null;
	// �ʼ���������
	private String commentContent;
	private int classId;
	private NoteFragmentAdapter adapter = null;
	public int noteId = -1;
	private int position = -1;
	private FragmentManager manager;
	private FragmentTransaction ft;

	private App mApp;

	// �洢�ʼ���Ϣ
	private List<NoteInfoBean> notes = new ArrayList<NoteInfoBean>();

	// ����Ƿ�Ҫ�½��ʼǱ� 1:���½� 0:�½�
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
		textView.setText("�ʼ�");
		editBtn = (Button) getActivity().findViewById(R.id.topEdit);
		editBtn.setText("���");
		editBtn.setVisibility(View.VISIBLE);
		addNewNoteTxt = (TextView) view.findViewById(R.id.note_add);
		addNewImg = (ImageView) view.findViewById(R.id.note_btn_replay);
		notesListView = (ListView) view.findViewById(R.id.note_listView);

		// �����бʼ��б������ʾ
		setNotes2ListView();

		addNewNoteTxt.setVisibility(View.INVISIBLE);
		editBtn.setOnClickListener(this);
		addNewNoteTxt.setOnClickListener(this);
		addNewImg.setOnClickListener(this);

		// ����ʵ�ֶ���Ӧ��Ŀ���޸ĵȲ���
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

		// ����鿴�ñʼǵ���ϸ��Ϣ
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
	 * ��������Ϣ������Ӧ���޸Ĳ���
	 */
	private void operateNotes() {
		final int posi = position;
		// ���峤����Ҫ��ʾ�Ĳ���
		final String[] mItems = { "ɾ���÷���", "�޸ķ�����", "�������" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle("�б�ѡ���").setItems(mItems,
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
									// �޸ķ�����
									String title = "�ʼ����ƣ����";
									String desc = "�ʼǵ�˵�������";
									addNewTypes(1, title, desc);
									break;
								case 2:
									// �������
									String noteName = getNotesNameById(posi);
									addComment(noteName);
									break;
								}
							}
						});
		builder.create().show();
	}

	/**
	 * �������
	 */
	public void addComment(String noteName) {
		final EditText editText = new EditText(getActivity());
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
		editText.setId(1);
		editText.setHint("��������������(����Ϊ��)");
		editText.setSingleLine(false);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle("�����ڶԱʼǣ�" + noteName + "����������").setView(editText)
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				})
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						et_noteComment = (EditText) editText.findViewById(1);
						commentContent = et_noteComment.getText().toString();
						if (commentContent.equals("")) {
							CustomToast.showToast(getActivity(), "��������Ϊ�ա�����ʧ��",
									2000);
						} else {
							uploadNoteComment(noteId);
						}
					}
				});
		builder.create().show();
	}

	/**
	 * �ϴ�����
	 */
	private void uploadNoteComment(int noteId) {
		// ��ʼ����������
		mRequestQueue = Volley.newRequestQueue(getActivity());
		// initDialog("�ϴ���,���Ժ�...");
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"�޸��У����Ժ󡣡�����", getActivity());
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
					CustomToast.showToast(getActivity(), "���۳ɹ�,�������鿴����", 2000);
				} else {
					CustomToast.showToast(getActivity(), "����ʧ��", 2000);
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
	 * �ԱʼǷ�����޸�
	 */
	public void updateClassName() {
		// ��ȡ��Ӧid������
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

		// ��ʼ����������
		mRequestQueue = Volley.newRequestQueue(getActivity());
		// initDialog("�޸���,���Ժ�...");
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"�޸��У����Ժ󡣡�����", getActivity());
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
					CustomToast.showToast(getActivity(), "�޸ĳɹ�", 2000);
					// ������ӵıʼ���ʾ�ڱʼ��б���
					notes.get(mIndex).setTitle(mNoteName);
					notes.get(mIndex).setContent(mNoteDesc);
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

	public void deleteNotesName() {
		// ��ʼ����������
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"ɾ���У����Ժ󡣡���", getActivity());
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
					CustomToast.showToast(getActivity(), "ɾ���ɹ�", 2000);
					// ������ӵıʼǷ�����ʾ�ڱʼǷ�����
					notes.remove(position);
					adapter.notifyDataSetChanged();
				} else {
					CustomToast.showToast(getActivity(), "ɾ��ʧ��", 2000);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				byte[] htmlBodyBytes = error.networkResponse.data;
				Log.e("LOGIN-ERROR", new String(htmlBodyBytes), error);
				// ʧ�ܣ��������
				mProgressDialog.dismiss();
				CustomToast.showToast(getActivity(), "�������Ӵ���", 2000);
			}
		});
		mRequestQueue.add(request);

	}

	/**
	 * ����listView��item���±�����Ӧ�ıʼ���
	 * 
	 * @param position
	 * @return ���ط�����
	 */
	public String getNotesNameById(int position) {
		String itemName = ((NoteInfoBean) notesListView
				.getItemAtPosition(position)).getTitle();
		return itemName;
	}

	/**
	 * ���ڷ������Ʋ�����ͬ����ô���ݷ������Ʋ��ұ���װ��bean
	 * 
	 * @param className
	 * @return ����bean����
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
	 * �ӷ�������ȡ�ʼ��б�
	 */
	private void getNotesInfo() {
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"��ȡ������Ϣ�����Ժ�....", getActivity());
		// ��ʼ����������
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
		mRequestQueue.add(noteRequest);
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
				// classId=-1�����]�е���ʼǷ����ֱ���ڷ��������ң���ô���Ҳ���
				if (RightFragment.classId == -1 || notes == null) {
					CustomToast.showToast(getActivity(), "��û���καʼ�", 2000);
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
			CustomToast.showToast(getActivity(), "��ȡ�ʼǷ����ʧ��", 2000);
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
				CustomToast.showToast(getActivity(), "�������Ͻǵİ�ť��ѡ��������������µ�",
						2000);
				return;
			}
			// ����µıʼǱ�
			String title = "�ʼ����ƣ����";
			String desc = "�ʼǵ����ݣ����";
//			addNewTypes(0, title, desc);
			AddNewNoteFragment addNewNoteFragment = new AddNewNoteFragment();
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.content_frame, addNewNoteFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commitAllowingStateLoss();
		
			break;
		case R.id.note_btn_replay:
			if (isAddNew == 0) {// �½�
				addNewImg.setImageResource(R.drawable.list_edit_selected);
				isAddNew = 1;
				addNewNoteTxt.setVisibility(View.VISIBLE);
			} else if (isAddNew == 1) {// ���½�
				addNewImg.setImageResource(R.drawable.list_edit);
				isAddNew = 0;
				addNewNoteTxt.setVisibility(View.INVISIBLE);
			}
			break;
		}
	}

	/**
	 * �ϴ��½��ʼ�
	 */
	public void uploadNoteType() {
		// ��ʼ����������
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mProgressDialog = CustomDialog.initDialog(mProgressDialog,
				"�½������ϴ���,���Ժ�...", getActivity());
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
						System.out.println("�ʼ�=" + response);
						// ��֤�ϴ����
						if (CheckResultRequest.checkResult(response) != null) {
							CustomToast.showToast(getActivity(), "��ӱʼǷ���ɹ�",
									2000);
							// ������ӵıʼ���ʾ�ڱʼ��б���
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
	 * ��ȡ�½��ıʼ���Ϣ
	 */
	private void getData() {
		NoteInfoBean bean = new NoteInfoBean();
		bean.setTitle(noteName.getText().toString().trim());
		bean.setContent(noteDesc.getText().toString().trim());
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		String time = format.format(date);
		bean.setModified(time);
		bean.setAuthor("XUPTCHASE");// ������ϸ������Ϣδд����������д����α����
		bean.setUserId(Integer.parseInt(mApp.getUser().getUserId()));
		notes.add(0, bean);
	}

	/**
	 * ��֤�û�����ķ�����Ϣ
	 */
	private boolean checkNoteName_NotsDesc() {
		String mclassName = noteName.getText().toString();
		String mclassDesc = noteDesc.getText().toString();
		if (mclassName.equals("") || mclassName == null) {
			CustomToast.showToast(getActivity(), "�ʼ�����δ��д�����ʧ��", 2000);
			return false;
		} else if (mclassDesc.equals("") || mclassDesc == null) {
			CustomToast.showToast(getActivity(), "�ʼǵ���ϸ������Ϣδ��д�����ʧ��", 2000);
			return false;
		} else {
			// ��֤�½��ʼ����Ƿ�֮ǰ��
			if (notes != null || notes.size() > 0) {
				for (int i = 0; i < notes.size(); i++) {
					if (noteName.getText().toString()
							.equals(notes.get(i).getTitle())) {
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
		if (mRequestQueue != null)
			mRequestQueue.cancelAll(getActivity());
		if(notes != null)
			notes.clear();
	}

}
