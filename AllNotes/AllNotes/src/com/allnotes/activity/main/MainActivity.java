package com.allnotes.activity.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.allnotes.R;
import com.allnotes.fragment.HomeFragment;
import com.allnotes.fragment.LeftFragment;
import com.allnotes.fragment.RightFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * @description ������
 */
public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {

	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;
	private Button topRight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // ȥ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ��ʼ����߲˵�
		initSlidingMenu(savedInstanceState);
		// ��ʼ���û���Ϣ
		topButton = (ImageView) findViewById(R.id.topButton);
		topButton.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
		// �������ϽǱ༭���ɼ�
		topRight = (Button) findViewById(R.id.topEdit);
		topRight.setVisibility(View.INVISIBLE);
	}

	/**
	 * ��ʼ�������
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// ��������״̬��Ϊ����õ�֮ǰ�����Fragment������ʵ����MyFragment
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}

		if (mContent == null) {
			mContent = new HomeFragment();
		}

		// ������໬���˵�
		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commit();

		// ʵ���������˵�����
		SlidingMenu sm = getSlidingMenu();
		// ���ÿ������һ����Ĳ˵�
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		// ���û�����Ӱ�Ŀ��
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// ���û����˵���Ӱ��ͼ����Դ
		sm.setShadowDrawable(null);
		// ���û����˵���ͼ�Ŀ��
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// ���ý��뽥��Ч����ֵ
		sm.setFadeDegree(0.35f);
		// ���ô�����Ļ��ģʽ,��������Ϊȫ��
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// �����·���ͼ���ڹ���ʱ�����ű���
		sm.setBehindScrollScale(0.0f);

		// �����Ҳ�˵������ļ�
		sm.setSecondaryMenu(R.layout.menu_frame_right);
		sm.setSecondaryShadowDrawable(null);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_right, new RightFragment()).commit();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		try {
			getSupportFragmentManager().putFragment(outState, "mContent",
					mContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �л�Fragment
	 * 
	 * @param fragment
	 */
	public void switchConent(Fragment fragment, String title) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
		topTextView.setText(title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topButton:
			toggle();
			break;
		default:
			break;
		}
	}
}
