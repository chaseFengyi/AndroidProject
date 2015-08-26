package com.analysis.listview.drag;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myanalysistools.R;

public class ReflashListView extends ListView implements OnScrollListener {

	View header;// 底部布局文件
	int headerHeight;// 顶部布局文件的高度
	int firstVisibleItem;// 当前第一个可见的item的位置
	int scrollState; // 当前滚动状态
	int lastItem;//最后一个能显示的item的位置

	boolean isRemark;// 当前是在listView的最顶端按下的
	int startY;// 按下时的Y值

	int state;// 当前的状态
	final int NONE = 0;// 正常状态
	final int PULL = 1;// 上拉刷新状态
	final int RELEASE = 2;// 松开释放状态
	final int REFLASH = 3;// 正常刷新
	
	IReflashListener iListener;//刷新数据接口

	public ReflashListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ReflashListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ReflashListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * 初始化界面，添加顶部布局文件到listView
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.footer_layout, null);
		measureView(header);
		headerHeight = header.getMeasuredHeight();
		Log.i("height", "headerHeitgh=" + headerHeight);
		topPadding(-headerHeight);
		this.addFooterView(header);
		this.setOnScrollListener(this);
	}

	/**
	 * 通知父布局占用的宽度，高度
	 * 
	 * @param view
	 */
	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		// 0:左右边距
		// 0：内边距
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}

	/**
	 * 设置header布局的下边距
	 * 
	 * @param topPadding
	 */
	private void topPadding(int topPadding) {
		header.setPadding(header.getPaddingLeft(), header.getPaddingTop(),
				header.getPaddingRight(), topPadding);
		header.invalidate();
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		this.firstVisibleItem = arg3-1;
		this.lastItem = arg3-1;
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		this.scrollState = arg1;
	}

	//手势控制
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstVisibleItem == lastItem) {//在界面最底端
				isRemark = true;
				startY = (int) ev.getY();
			}
			break;

		case MotionEvent.ACTION_MOVE:
			onMove(ev);
			break;
		case MotionEvent.ACTION_UP:
			if (state == RELEASE) {
				state = REFLASH;
				//加载最新数据
				reflashViewByState();
				iListener.onReflash();
			} else if (state == PULL) {
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;

		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 根据当前状态改变界面显示
	 */
	private void reflashViewByState() {
		TextView tip = (TextView) header.findViewById(R.id.tip);
		ImageView arrow = (ImageView) findViewById(R.id.arrow);
		ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
		RotateAnimation anim = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(500);
		anim.setFillAfter(true);
		RotateAnimation anim1= new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);
		switch (state) {
		case NONE:
			arrow.clearAnimation();
			topPadding(-headerHeight);
			break;

		case PULL:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("上拉刷新");
			arrow.clearAnimation();
			arrow.setAnimation(anim);
			break;
		case RELEASE://松开
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("松开刷新");
			arrow.clearAnimation();
			arrow.setAnimation(anim1);
			break;
		case REFLASH:
			topPadding(50);
			arrow.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			progress.setBackgroundResource(R.drawable.loading_progress_wait_icon);
			tip.setText("正在刷新...");
			arrow.clearAnimation();
			break;
		}
	}

	/**
	 * 获取完数据
	 */
	public void reflashComplete(){
		state = NONE;
		isRemark = false;
		reflashViewByState();
		TextView lastuploadTime = (TextView) header.findViewById(R.id.lastupdate_time);
		SimpleDateFormat format = new SimpleDateFormat("yyy年MM月dd日 hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String time = format.format(date);
		lastuploadTime.setText(time);
	}
	
	public void setInterface(IReflashListener iListener){
		this.iListener = iListener;
	}
	
	/**
	 * @author FengYi~
	 *刷新数据接口
	 */
	public interface IReflashListener{
		public void onReflash();
	}
	
	/**
	 * 判断移动过程中的操作
	 * 
	 * @param ev
	 */
	private void onMove(MotionEvent ev) {
		if (!isRemark) {
			return;
		}
		// 当前移动的位置
		int tempY = (int) ev.getY();
		// 移动的距离
		int space = startY - tempY;
		int topPadding =  headerHeight - space;
		switch (state) {
		case NONE:
			if (space > 0) {
				state = PULL;
				reflashViewByState();
			}
			break;

		case PULL:
			topPadding(topPadding);
			if (space > headerHeight - 30
					&& scrollState == SCROLL_STATE_TOUCH_SCROLL) {
				state = RELEASE;
				reflashViewByState();
			}
			break;
		case RELEASE:
			topPadding(topPadding);
			if (space < headerHeight - 30) {
				state = PULL;
				reflashViewByState();
			} else if (space <= 0) {
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;
		case REFLASH:

			break;
		}
	}
}
