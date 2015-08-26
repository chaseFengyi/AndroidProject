package com.mynutritionstreet.mainpage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.example.mynutritionstreet.R;
import com.mynutritionstreet.Bean.GoodInfoBean;
import com.mynutritionstreet.json.JsonResolve;
import com.mynutritionstreet.util.DialogUtils;
import com.mynutritionstreet.util.HttpUtil;
import com.mynutritionstreet.util.ToastUtil;
import com.mynutritionstreet.util.UrlUtils;
import com.mynutritionstreet.util.VolleyUtil;

public class DailyRecommendFragment extends Fragment {

	public static final String ARG_SECTION_NUMBER = "section_daily";

//	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private DailyRecommendFragmentAdapter adapter;
	private ListView listview;
	// ��ʾ��Ӧ���磬�У������е�ĳһ��
	private String type;

	static Dialog dialog = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.sample_daily_fragment, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
//		Bundle bundle = getArguments();
		// list = (List<Map<String, Object>>)
		// bundle.getSerializable(ARG_SECTION_NUMBER);
//		type = bundle.getString(DailyRecommendActivity.TYPESTRING);
		type = "��";
		Log.i("break-type", type);
		listview = (ListView) getActivity().findViewById(
				R.id.listView_sample_daily_fragment);
//		add2ListView();
		getDataByServer();
	}

	// http://blog.csdn.net/zglslyg/article/details/8480460
	// ��������ӵ�listView��
//	private void add2ListView() {
//		adapter = new DailyRecommendFragmentAdapter(getActivity(), list);
//		listview.setAdapter(adapter);
//	}

	/**
	 * �ӷ�������ȡ���ݣ���������ʾ
	 */
	public void getDataByServer() {
		dialog = DialogUtils.createProgressDialog(getActivity(), "���",
				"���ݻ�ȡ�У����Ժ�...");
		dialog.show();
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("typeString", type);
		HttpUtil breakResponse = new HttpUtil(UrlUtils.QUERY_BY_TYPE,rawParams, 
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
//						Log.i("response", response);
						showResult(response);
					}
				},new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						ToastUtil.showToast(getActivity(), "�������", 2000);
					}
				});
		VolleyUtil.getRequestQueue().add(breakResponse);
	}
	
	/**
	 * ��ʾ�ϴ����
	 * @param response
	 */
	public void showResult(String response){
		dialog.dismiss();
		List<GoodInfoBean> goods = JsonResolve.json_goods_query_by_type(response);
		Log.i("goods", goods+"");
		if(goods == null){
			ToastUtil.showToast(getActivity(), "�����κ�ʳ�﷢��", 2000);
		}else{
			adapter = new DailyRecommendFragmentAdapter(getActivity(), goods);
			listview.setAdapter(adapter);
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		VolleyUtil.getRequestQueue().cancelAll(this);
	}
	
}
