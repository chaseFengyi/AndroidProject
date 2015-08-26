package com.allnotes.fragment;

import com.allnotes.R;
import com.allnotes.activity.main.MainActivity;
import com.allnotes.activity.userinfo.UserInfoActivity;
import com.allnotes.app.App;
import com.allnotes.bean.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * @description ��߲˵�
 */
public class LeftFragment extends Fragment implements OnClickListener {

	private View homeView;// ��ҳ
	private View readView;// �Ķ�
	private View noteView;// �ʼ�
	private View groupView;// Ⱥ��
	private View findView;// ����
	private View draftView;// �ݸ�
	private View settingView;// ����
	private User mUser;
	private TextView mTextViewUserName;
	private TextView mTextViewDesc;
	private RelativeLayout mRelativeLayoutUserInfo;
	private App mApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		mApp = (App) getActivity().getApplication();
		initUserInfo();
		return view;
	}

	public void findViews(View view) {
		homeView = view.findViewById(R.id.menu_home);
		readView = view.findViewById(R.id.menu_read);
		noteView = view.findViewById(R.id.menu_note);
		groupView = view.findViewById(R.id.menu_group);
		findView = view.findViewById(R.id.menu_find);
		draftView = view.findViewById(R.id.menu_draft);
		settingView = view.findViewById(R.id.menu_setting);
		mTextViewUserName = (TextView) view
				.findViewById(R.id.tv_layout_meun_name);
		mTextViewDesc = (TextView) view.findViewById(R.id.tv_layout_menu_desc);
		mRelativeLayoutUserInfo = (RelativeLayout) view
				.findViewById(R.id.rl_layout_menu_user_info);

		homeView.setOnClickListener(this);
		readView.setOnClickListener(this);
		noteView.setOnClickListener(this);
		groupView.setOnClickListener(this);
		findView.setOnClickListener(this);
		draftView.setOnClickListener(this);
		settingView.setOnClickListener(this);
		mRelativeLayoutUserInfo.setOnClickListener(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * ��ʼ���û���Ϣ
	 */
	private void initUserInfo() {
		mUser = mApp.getUser();
		if (mUser != null) {
			if (mUser.getUserName() == null || mUser.getUserName().equals("")) {
				mTextViewUserName.setText("�û�" + mUser.getUserId());
			} else {
				mTextViewUserName.setText(mUser.getUserName());
			}
			if (mUser.getDesc().equals("")) {
				mTextViewDesc.setText("���˼��:�����е�����~");
			} else {
				mTextViewDesc.setText("���˼�� : " + mUser.getDesc());
			}
		}
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		switch (v.getId()) {
		case R.id.menu_home: // ��ҳ
			newContent = new HomeFragment();
			title = getString(R.string.home);
			break;
		case R.id.menu_read:// �Ķ�
			newContent = new ReadFragment();
			title = getString(R.string.read);
			break;
		case R.id.menu_note: // �ʼ�
			newContent = new NoteFragment();
			title = getString(R.string.note);
			break;
		case R.id.menu_group: // Ⱥ��
			newContent = new GroupFragment();
			title = getString(R.string.group);
			break;
		case R.id.menu_find: // ����
			newContent = new FindFragment();
			title = getString(R.string.find);
			break;
		case R.id.menu_draft: // �ݸ�
			newContent = new DraftFragment();
			title = getString(R.string.draft);
			break;
		case R.id.menu_setting: // ����
			newContent = new SettingFragment();
			title = getString(R.string.setting);
			break;
		case R.id.rl_layout_menu_user_info:// �޸ĸ�����Ϣ
			MainActivity activity = (MainActivity) getActivity();
			activity.getSlidingMenu().showContent();
			startActivity(new Intent(getActivity(), UserInfoActivity.class));
			break;
		default:
			break;
		}
		if (newContent != null) {
			switchFragment(newContent, title);
		}
	}

	/**
	 * ����fragment
	 * 
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment, String title) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchConent(fragment, title);
		}
	}
	@Override
	public void onResume() {
		initUserInfo();
		super.onResume();
	}
}
