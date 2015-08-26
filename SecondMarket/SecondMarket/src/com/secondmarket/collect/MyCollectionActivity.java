package com.secondmarket.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.secondmarket.DB.DBCreateWord;
import com.secondmarket.DB.DBHelper;
import com.secondmarket.DB.DBPerform;
import com.secondmarket.bean.SortSearchDemo;
import com.secondmarket.load.R;
import com.secondmarket.mine.MineActivity;

public class MyCollectionActivity extends Activity {
	private Button myCollection;
	public static Button edit = null;
	public static CheckBox allchoice = null;

	private CheckBox isDelete;
	private Button information;
	private Button release;
	private Button mine2;
	private ListView listView;

	private static MyAdapterOfCollection adapter = null;
	public List<BookInfo> list1 = new ArrayList<BookInfo>();
	public static int flag = 1;// �жϵ���༭��ť
	private static int allChoiced = 0;
	private static int isDeleted = 0;
	private static List<ViewHolderOfRelease> list = new ArrayList<ViewHolderOfRelease>();
	private static List<Integer> in = new ArrayList<Integer>();

	public int pageindex = 1;
	public int DataSizePerPage = 10;
	boolean isLoading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_mycollection);

		findView();
		setListView();
		onClick();
		createWindow(3, 4);
		setVisibility();
		listView.setOnItemClickListener(new ListViewListener());
	}

	private void findView() {
		myCollection = (Button) findViewById(R.id.myCollection);
		edit = (Button) findViewById(R.id.edit);
		allchoice = (CheckBox) findViewById(R.id.allchoice);
		isDelete = (CheckBox) findViewById(R.id.isDelete);
		information = (Button) findViewById(R.id.information);
		release = (Button) findViewById(R.id.release);
		mine2 = (Button) findViewById(R.id.mine2);
		listView = (ListView) findViewById(R.id.listView);
	}

	private void onClick() {
		myCollection.setOnClickListener(new MyCollectionListener());
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (flag == 1) {
					createWindow(7, 12);
					allchoice.setVisibility(View.VISIBLE);
					isDelete.setVisibility(View.VISIBLE);
					flag = 0;
				} else if (flag == 0) {
					createWindow(3, 4);
					allchoice.setVisibility(View.GONE);
					isDelete.setVisibility(View.GONE);
					flag = 1;
				}
			}
		});
		allchoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (allChoiced == 0) {
					allChoiced = 1;
				} else {
					allChoiced = 0;
				}
				listView.setAdapter(adapter);
			}
		});
		isDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (list1 != null) {
					isDeleted = 1;
					if (isDeleted == 1) {
						if (allChoiced == 1) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									MyCollectionActivity.this)
									.setMessage("�Ƿ�ɾ��������Ʒ��Ϣ")
									.setPositiveButton(
											"��",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													arg0.dismiss();
													for(int i = 0; i < list1.size(); i++){
														SortSearchDemo searchDemo = new SortSearchDemo();
														searchDemo.setGoodsId(list1.get(i).getGoodsId());
														if(DBPerform.deleteQuery(MyCollectionActivity.this, searchDemo, DBCreateWord.TB_NAME) == -1){
															Toast.makeText(MyCollectionActivity.this, "��"+(i+1)+"����Ʒɾ��ʧ��!", Toast.LENGTH_SHORT).show();
															return;
														}
													}
													list1.clear();
													list1 = null;
													adapter.notifyDataSetChanged();
												}
											})
									.setNegativeButton(
											"��",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													arg0.dismiss();
												}
											});
							builder.create().show();
						} else if (allChoiced == 0 && in != null) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									MyCollectionActivity.this)
									.setMessage("�Ƿ�ɾ������Ʒ��Ϣ")
									.setPositiveButton(
											"��",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													Collections.sort(in);
													for (int i = in.size() - 1; i >= 0; i--) {
														if (adapter.goods
																.get(in.get(i)).checkBox
																.isChecked() == true) {
															SortSearchDemo searchDemo = new SortSearchDemo();
															searchDemo.setGoodsId(list1.get((int)in.get(i)).getGoodsId());
															if(DBPerform.deleteQuery(MyCollectionActivity.this, searchDemo, DBCreateWord.TB_NAME) == -1){
																Toast.makeText(MyCollectionActivity.this, "ɾ����"+((int)in.get(i)+1)+"����Ʒʧ��!", Toast.LENGTH_SHORT).show();
															}else{
																Toast.makeText(MyCollectionActivity.this, "ɾ����"+((int)in.get(i)+1)+"����Ʒ�ɹ�!", Toast.LENGTH_SHORT).show();
															}
															list1.remove((int) in
																	.get(i));
														}
													}
													adapter.notifyDataSetChanged();
													listView.setAdapter(adapter);
													in.clear();
													in = new ArrayList<Integer>();
													arg0.dismiss();
												}
											})
									.setNegativeButton(
											"��",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													arg0.dismiss();
												}
											});
							builder.create().show();
						}
						isDeleted = 0;
					}
				} else {
					Toast.makeText(MyCollectionActivity.this, "�Բ���û����Ʒ����ɾ����!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	// ����
	private void createWindow(int top, int bottom) {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = height * top / bottom;
		listView.setLayoutParams(params);
	}

	private void setVisibility() {
		allchoice.setVisibility(View.GONE);
		isDelete.setVisibility(View.GONE);
	}

	class MyCollectionListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MyCollectionActivity.this, MineActivity.class);
			startActivity(intent);
			finish();
		}

	}

	private void setListView() {
		list = getListData();
		if (list1.size() > 0) {
			adapter = new MyAdapterOfCollection(list,R.layout.simple_release,
					MyCollectionActivity.this);
			listView.setAdapter(adapter);
		} else {
			Toast.makeText(MyCollectionActivity.this, "�����κ���Ʒ���ղ�!",
					Toast.LENGTH_SHORT).show();
		}

	}
	private List<ViewHolderOfRelease> getListData() {
		List<SortSearchDemo> lists = DBPerform.SelectQuery(MyCollectionActivity.this,DBCreateWord.TB_NAME,
												DBCreateWord.sqlCollect,DBCreateWord.dropCollect);
		// �����ݿ��л�ȡ����
		for (int i = 0; i < lists.size(); i++) {
			SortSearchDemo sortSearchDemo = lists.get(i);
			BookInfo book = new BookInfo();
			book.setIsChecked(false);
			book.setBookName(sortSearchDemo.getGoodsName());
			book.setBookIntroduce(sortSearchDemo.getGoodsDescribe());
			book.setBookMoney(sortSearchDemo.getGoodsPrice());
			book.setBookPicture(sortSearchDemo.getList().get(0).get("goodsPictureAD1"));
			book.setGoodsId(sortSearchDemo.getGoodsId());
			list1.add(book);
		}
		int i = 0;
		List<ViewHolderOfRelease> list = new ArrayList<ViewHolderOfRelease>();
		while (i < list1.size()) {
			ViewHolderOfRelease view = new ViewHolderOfRelease();
			list.add(view);
			i = i + 1;
		}
		return list;
	}
	public ArrayList<BookInfo> getCurrentPageItems(int pageindex) {
		ArrayList<BookInfo> nextPageItems = new ArrayList<BookInfo>();
		// ���һҳ����
		if (list1.size() - (pageindex - 1) * 10 <= 10
				&& list1.size() - (pageindex - 1) * 10 > 0) {
			for (int i = 10 * (pageindex - 1); i < list1.size(); i++) {
				nextPageItems.add(list1.get(i));
			}
		} else if (list1.size() - (pageindex - 1) * 10 <= 0) {
			return null;
		} else {
			for (int i = 10 * (pageindex - 1); i < pageindex * 10; i++) {
				nextPageItems.add(list1.get(i));
			}
		}
		return nextPageItems;
	}

	class ListViewListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(MyCollectionActivity.this, MineActivity.class);
			startActivity(intent);
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	private class MyAdapterOfCollection extends BaseAdapter {
		private LayoutInflater mInflater = null;
		private List<ViewHolderOfRelease> goods;
		private Context context;
		private int resource;

		public MyAdapterOfCollection(List<ViewHolderOfRelease> goods,
				int resource, Context context) {
			this.goods = goods;
			this.resource = resource;
			this.context = context;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void addItems(List<BookInfo> newItems) {
			if (newItems == null || newItems.size() <= 0)
				return;
			// testItems.add("----���ص�" + page + "ҳ����----");
			for (int i = 0; i < newItems.size(); i++) {
				list1.add(newItems.get(i));
			}
			notifyDataSetChanged();
			// page++;
		}

		public void clear() {
			list1.clear();
			// page = 1;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if ((list1 != null) && (list1.size() > 0)) {
				return list1.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list1.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			arg1 = mInflater.inflate(resource, null);
			goods.get(arg0).checkBox = ((CheckBox) arg1
					.findViewById(R.id.checkBox));
			goods.get(arg0).imageView = (ImageView) arg1
					.findViewById(R.id.imageView);
			goods.get(arg0).bookName = ((TextView) arg1
					.findViewById(R.id.bookName));
			goods.get(arg0).bookIntroduce = ((TextView) arg1
					.findViewById(R.id.bookIntroduce));
			goods.get(arg0).bookMoney = ((TextView) arg1
					.findViewById(R.id.bookMoney));
			
			if(list1.get(arg0).getBookPicture() != null){
//				uri1 = list1.get(i).getList().get(0).get("goodsPictureAD1");
//http://b.hiphotos.baidu.com/movie/pic/item/50da81cb39dbb6fdbb7e3e020d24ab18972b37e9.jpg
//C:\\Users\\FengYi~\\Desktop\\mix\\photo\\bus.png		
//file:///C://Users//FengYi~//Desktop//mix//photo//bus.png
//path=   C:\Users\FengYi~\Desktop\mix\photo\bus.png	
				///http://www.cfanz.cn/?c=article&a=read&id=58189
				String path1 = list1.get(arg0).getBookPicture();
				System.out.println("path==="+path1);
				DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.home_book)
					.cacheInMemory(true).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
				ImageLoader.getInstance().displayImage("http://172.20.0.152:8080/XuptMarket/"+path1,goods.get(arg0).imageView,options);
			}else{
				goods.get(arg0).imageView.setBackgroundResource(R.drawable.home_book);
			}
			goods.get(arg0).checkBox.setChecked(list1.get(arg0).getIsChecked());
			goods.get(arg0).bookName.setText(list1.get(arg0).getBookName());
			goods.get(arg0).bookIntroduce.setText(list1.get(arg0)
					.getBookIntroduce());
			goods.get(arg0).bookMoney.setText(list1.get(arg0).getBookMoney()
					+ "");
			if (flag == 0) {
				goods.get(arg0).checkBox.setVisibility(View.VISIBLE);
			} else if (flag == 1) {
				goods.get(arg0).checkBox.setVisibility(View.GONE);
			}
			if (allChoiced == 1) {
				goods.get(arg0).checkBox.setChecked(true);
				list1.get(arg0).setIsChecked(true);
			} else if (allChoiced == 0) {
				goods.get(arg0).checkBox.setChecked(false);
				list1.get(arg0).setIsChecked(false);
			}
			final BookInfo mChecked = list1.get(arg0);
			final int position = arg0;
			goods.get(arg0).checkBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mChecked.setIsChecked(true);
					in.add(position);
					System.out.println("checked!!!!!!");
				}
			});
			if ((list1.get(arg0).getIsChecked() == true)) {
				if (isDeleted == 1) {
					goods.remove(arg0);
					adapter.notifyDataSetChanged();
					isDeleted = 0;
				}
			}
			return arg1;
		}
	}

}