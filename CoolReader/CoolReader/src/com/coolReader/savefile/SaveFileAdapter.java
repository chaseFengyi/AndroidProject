package com.coolReader.savefile;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coolReader.R;
import com.coolReader.Bean.SaveInfoBean;
import com.coolReader.Bean.URLInfoBean;
import com.coolReader.Bean.WebContentBean;
import com.coolReader.Login.LoginActivity;
import com.coolReader.Util.ToastUtils;
import com.coolReader.dao.DBPerform;
import com.coolReader.mainPage.MainPageActivity;
import com.coolReader.showcontent.ShowURLContentActivity;

public class SaveFileAdapter extends BaseAdapter {
	List<SaveInfoBean> list;
	Context context;
	static List<Integer> li = new ArrayList<Integer>();

	public SaveFileAdapter(Context context, List<SaveInfoBean> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;

		if (list == null || list.size() <= 0)
			return null;

		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.sample_savefile, null);
		}

		TextView title = (TextView) view
				.findViewById(R.id.sample_savefile_title);
		TextView url = (TextView) view.findViewById(R.id.sample_savefile_url);
		final TextView delete = (TextView) view
				.findViewById(R.id.sample_savefile_delete);

		final int posi = position;
		// ����ѡ��ɾ��
		view.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				delete.setVisibility(View.VISIBLE);
				// ����ѡ�еı�����ɫ
				v.setBackgroundColor(context.getResources().getColor(
						R.color.cadetblue));
				final View myView = v;
				delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// �ȴ����ݿ�ɾ���ü�¼
						int deleted = DBPerform.deleteSaveInfo(context,
								SaveFileActivity.list.get(posi).getSaveId());
						if (deleted > 0) {
							ToastUtils.makeToast((Activity) context, "ɾ���ɹ�");
							SaveFileActivity.list.remove(posi);
							delete.setVisibility(View.GONE);
							v.setBackgroundColor(0);
							notifyDataSetChanged();
						} else {
							ToastUtils.makeToast((Activity) context, "ɾ��ʧ��");
						}
					}
				});
				return true;
			}
		});

		// ���²�ѡ��
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//����ɾ����ť�Ƿ���ʾ�ж��ǵ������������ʾ�����ǵ��ȡ�������Ķ�����ɾ����ť����
				if(delete.getVisibility() == View.VISIBLE){//����
					// �� �ñ�����ɫΪ��ѡ��
					delete.setVisibility(View.GONE);
					v.setBackgroundColor(0);
				}else if(delete.getVisibility() == View.GONE){//���ǳ���������������ʾ����
					Intent intent = new Intent();
					WebContentBean webContentBean = new WebContentBean();
					//����urlid���Ҷ�Ӧurl����
					URLInfoBean urlInfoBean = DBPerform.queryURLrInfoByID(context, list.get(posi).getUrlId());
					webContentBean.setChecked(false);
					webContentBean.setWebContent(urlInfoBean.getUrlContent());
					webContentBean.setWebId(urlInfoBean.getUrlId());
					webContentBean.setWebTitle(urlInfoBean.getUrlTitle());
					webContentBean.setWebUrl(urlInfoBean.getUrlLink());
					Bundle bundle = new Bundle();
					bundle.putSerializable(MainPageActivity.TAG, webContentBean);
					bundle.putString("key", "SaveFileActivity");
					intent.putExtras(bundle);
					intent.setClass(context, ShowURLContentActivity.class);
					context.startActivity(intent);
				}
				
			}
		});

		// ���ݴ浵��Ϣ�����urlid��ȡ��Ӧurl����
		URLInfoBean urlInfoBean = DBPerform.queryURLrInfoByID(context, list
				.get(position).getUrlId());
		title.setText(urlInfoBean.getUrlTitle());
		url.setText(urlInfoBean.getUrlLink());

		return view;
	}

}
