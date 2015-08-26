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
 * @description 左边菜单
 */
public class LeftFragment extends Fragment implements OnClickListener {

	private View homeView;// 首页
	private View readView;// 阅读
	private View noteView;// 笔记
	private View groupView;// 群组
	private View findView;// 发现
	private View draftView;// 草稿
	private View settingView;// 设置
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
	 * 初始化用户信息
	 */
	private void initUserInfo() {
		mUser = mApp.getUser();
		if (mUser != null) {
			if (mUser.getUserName() == null || mUser.getUserName().equals("")) {
				mTextViewUserName.setText("用户" + mUser.getUserId());
			} else {
				mTextViewUserName.setText(mUser.getUserName());
			}
			if (mUser.getDesc().equals("")) {
				mTextViewDesc.setText("个人简介:主人有点懒咯~");
			} else {
				mTextViewDesc.setText("个人简介 : " + mUser.getDesc());
			}
		}
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		switch (v.getId()) {
		case R.id.menu_home: // 首页
			newContent = new HomeFragment();
			title = getString(R.string.home);
			break;
		case R.id.menu_read:// 阅读
			newContent = new ReadFragment();
			title = getString(R.string.read);
			break;
		case R.id.menu_note: // 笔记
			newContent = new NoteFragment();
			title = getString(R.string.note);
			break;
		case R.id.menu_group: // 群组
			newContent = new GroupFragment();
			title = getString(R.string.group);
			break;
		case R.id.menu_find: // 发现
			newContent = new FindFragment();
			title = getString(R.string.find);
			break;
		case R.id.menu_draft: // 草稿
			newContent = new DraftFragment();
			title = getString(R.string.draft);
			break;
		case R.id.menu_setting: // 设置
			newContent = new SettingFragment();
			title = getString(R.string.setting);
			break;
		case R.id.rl_layout_menu_user_info:// 修改个人信息
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
	 * 更换fragment
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
