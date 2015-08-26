package com.secondmarket.information;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.secondmarket.DB.DBCreateWord;
import com.secondmarket.DB.DBPerform;
import com.secondmarket.bean.SortSearchDemo;
import com.secondmarket.load.R;
import com.secondmarket.mine.MineActivity;
import com.secondmarket.publish.PublishActivity;

public class SortDetailsActivity extends Activity {
	private Button sortdetails;
	private TextView content;
	private TextView priceIs;
	private TextView phoneIs;
	private Button information;
	private Button release;
	private Button mine2;
	private ImageButton collectSort;

	private SortSearchDemo sortSearchDemo = null;
	public static int flag = 1;// �жϵ����ť

	int[] images = null;// ͼƬ��ԴID
	String[] titles = null;// ����

	ArrayList<ImageView> imageSource = null;// ͼƬ��Դ
	ArrayList<View> dots = null;// ��
	TextView title = null;
	ViewPager viewPager;// ������ʾͼƬ
	MyPagerAdapter adapter;// viewPager��������
	private int currPage = 0;// ��ǰ��ʾ��ҳ
	private int oldPage = 0;// ��һ����ʾ��ҳ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sortdetails);

		init();

		findView();
		onClick();
		createWindow();
		fillContent();
	}

	public void init() {
		images = new int[] { R.drawable.mathone, R.drawable.mathtwo,
				R.drawable.maththree, };
		titles = new String[] { "ͼƬ1", "ͼƬ2", "ͼƬ3", };
		// ��Ҫ��ʾ��ͼƬ�ŵ�list������
		imageSource = new ArrayList<ImageView>();
		for (int i = 0; i < images.length; i++) {
			ImageView image = new ImageView(this);
			image.setBackgroundResource(images[i]);
			imageSource.add(image);
		}

		// ��ȡ��ʾ�ĵ㣨�������·��ĵ㣬��ʾ��ǰ�ǵڼ��ţ�
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.dot1));
		dots.add(findViewById(R.id.dot2));
		dots.add(findViewById(R.id.dot3));

		// ͼƬ�ı���
		title = (TextView) findViewById(R.id.title);
		title.setText(titles[0]);

		// ��ʾͼƬ��VIew
		viewPager = (ViewPager) findViewById(R.id.vp);
		// ΪviewPager����������
		adapter = new MyPagerAdapter();
		viewPager.setAdapter(adapter);
		// ΪviewPager��Ӽ��������ü��������ڵ�ͼƬ�任ʱ������͵�Ҳ���ű仯
		MyPageChangeListener listener = new MyPageChangeListener();
		viewPager.setOnPageChangeListener(listener);

		// ������ʱ����ÿ��2���Զ�������һ�ţ�ͨ�������߳�ʵ�֣�
		ScheduledExecutorService scheduled = Executors
				.newSingleThreadScheduledExecutor();
		// ����һ���̣߳����߳�����֪ͨUI�̱߳任ͼƬ
		ViewPagerTask pagerTask = new ViewPagerTask();
		scheduled.scheduleAtFixedRate(pagerTask, 2, 2, TimeUnit.SECONDS);//��ָ���ļ���ظ�ִ��
	}

	// ViewPagerÿ�ν�����������ͼƬ�������ڷ�ֹ�����ڴ������
	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// �жϽ�Ҫ��ʾ��ͼƬ�Ƿ��������ʾ��ͼƬ��ͬһ��
			// arg0Ϊ��ǰ��ʾ��ͼƬ��arg1�ǽ�Ҫ��ʾ��ͼƬ
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// ���ٸ�ͼƬ
			container.removeView(imageSource.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// ��ʼ����Ҫ��ʾ��ͼƬ������ͼƬ���뵽container�У���viewPager��
			container.addView(imageSource.get(position));
			return imageSource.get(position);
		}
	}

	// ����ViewPager�ı仯
	private class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			// ����ʾ��ͼƬ�����仯֮��
			// ���ñ���
			title.setText(titles[position]);
			// �ı���״̬
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			dots.get(oldPage).setBackgroundResource(R.drawable.dot_normal);
			// ��¼��ҳ��
			oldPage = position;
			currPage = position;
		}
	}

	private class ViewPagerTask implements Runnable {
		@Override
		public void run() {
			// �ı䵱ǰҳ���ֵ
			currPage = (currPage + 1) % images.length;
			// ������Ϣ��UI�߳�
			handler.sendEmptyMessage(0);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// ���յ���Ϣ�󣬸���ҳ��
			viewPager.setCurrentItem(currPage);
		};
	};

	private void findView() {
		sortdetails = (Button) findViewById(R.id.sortdetails);
		content = (TextView) findViewById(R.id.content);
		priceIs = (TextView) findViewById(R.id.priceIs);
		phoneIs = (TextView) findViewById(R.id.phoneIs);
		information = (Button) findViewById(R.id.information);
		release = (Button) findViewById(R.id.release);
		mine2 = (Button) findViewById(R.id.mine2);
		collectSort = (ImageButton) findViewById(R.id.collectSort);
	}

	private void onClick() {
		sortdetails.setOnClickListener(new SortDetailsListener());
		mine2.setOnClickListener(new MineListener());
		information.setOnClickListener(new InformationListener());
		mine2.setOnClickListener(new Mine2Listener());
		collectSort.setOnClickListener(new CollectSortListener());
		release.setOnClickListener(new ReleaseListener());
	}

	// ����
	private void createWindow() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;

		content.setHeight(height / 7);
		priceIs.setHeight(height / 11);
		phoneIs.setHeight(height / 11);

	}

	class MineListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(SortDetailsActivity.this, MineActivity.class);
			startActivity(intent);
			finish();
		}

	}

	class SortDetailsListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("name", "");
			intent.setClass(SortDetailsActivity.this,
					SortInformationActivity.class);
			startActivity(intent);
			finish();
		}

	}

	class InformationListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startActivity(new Intent(SortDetailsActivity.this,
					MainPageActivity.class));
		}

	}

	class Mine2Listener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startActivity(new Intent(SortDetailsActivity.this,
					MineActivity.class));
		}

	}

	class CollectSortListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int sign;
			if (flag == 1) {
				DBPerform.createCollectTable(SortDetailsActivity.this);
				sign = DBPerform.insertQuery(SortDetailsActivity.this, sortSearchDemo,DBCreateWord.TB_NAME);
				if(sign == 1){
					Toast.makeText(SortDetailsActivity.this, "��ϲ�㣬�ղسɹ���",
							Toast.LENGTH_SHORT).show();
				}else if(sign == 0){
					Toast.makeText(SortDetailsActivity.this, "����Ʒ�Ѿ��ղأ����������ղأ�",
							Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(SortDetailsActivity.this, "�Բ����ղ�ʧ�ܣ�",
							Toast.LENGTH_SHORT).show();
				}
				flag = 0;
			} else if (flag == 0) {
				Toast.makeText(SortDetailsActivity.this, "ȡ���ղ�",
						Toast.LENGTH_SHORT).show();
				sign = DBPerform.deleteQuery(SortDetailsActivity.this, sortSearchDemo,DBCreateWord.TB_NAME);
				if(sign == -1){
					Toast toast = Toast.makeText(SortDetailsActivity.this,
							"����Ʒ�����ڣ�ȡ���ղ�ʧ��!", Toast.LENGTH_SHORT);
					toast.show();
				}else{
					Toast toast = Toast.makeText(SortDetailsActivity.this, "��ϲ�㣡ȡ���ղسɹ�",
							Toast.LENGTH_SHORT);
					toast.show();
				}
				flag = 1;
			}

		}

	}

	private class ReleaseListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startActivity(new Intent(SortDetailsActivity.this, PublishActivity.class));
		}
		
	}
	
	private void fillContent() {
		Intent intent = getIntent();
		sortSearchDemo = (SortSearchDemo) intent
				.getSerializableExtra("sortSearchDemo");
		// SortSearchDemo [isSale=0, goodsId=1, goodsConnect=1, userId=1,
		// goodsDescribe=������,
		// goodsTypeId=1, goodsPrice=23.0, goodsWanted=1,
		// goodsPublishTime=2014-1-1,
		// goodsName=����, list=[{goodsPictureAD1=android.jpg}]]
		System.out.println("sortsearchdemo=" + sortSearchDemo);
		content.setText(sortSearchDemo.getGoodsDescribe());
		priceIs.setText("��" + sortSearchDemo.getGoodsPrice());
		phoneIs.setText("����ʱ��:" + sortSearchDemo.getGoodsPublishTime());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra("name", "");
			intent.setClass(SortDetailsActivity.this,
					SortInformationActivity.class);
			startActivity(intent);
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
}
