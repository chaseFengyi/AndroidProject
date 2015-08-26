package com.mynutritionstreet.uploading;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mynutritionstreet.R;
import com.mynutritionstreet.Bean.MaterialBean;
import com.mynutritionstreet.customertoast.CustomToast;
import com.mynutritionstreet.mixinfo.SysApplication;

public class MaterialActivity extends Activity {
	private Button finishBtn;
	private Button addMatBtn;
	private ListView listView;
	private LinearLayout layout;
//	private ImageView pictureImg;
	private EditText foodNameExt;
	private EditText foodWeightExt;
	private Button confirmBtn;
	//食材id
	private static int id = 1;
	//存放搜有添加的食材
	public List<MaterialBean> list = new ArrayList<MaterialBean>();
	private MaterialAdapter adapter;
	//表示第几次点击添加食材
	private static int count = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_material);
		
		findView();
		setAdapter();
		setListView();
	}
	
	private void findView(){
		finishBtn = (Button)findViewById(R.id.material_finish);
		addMatBtn = (Button)findViewById(R.id.material_add);
		listView = (ListView)findViewById(R.id.material_listView);
		layout = (LinearLayout)findViewById(R.id.material_linear);
		foodNameExt = (EditText)findViewById(R.id.material_name);
		foodWeightExt = (EditText)findViewById(R.id.material_weight);
		confirmBtn = (Button)findViewById(R.id.material_confirm);
		layout.setVisibility(View.GONE);
		
		finishBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				Log.i("material", list+"");
				bundle.putSerializable("material", (Serializable) list);
				intent.putExtras(bundle);
				setResult(1001, intent);
				finish();
			}
		});
		
		addMatBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layout.setVisibility(View.VISIBLE);
			}
		});
		
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!foodNameExt.getText().toString().equals("") && !foodWeightExt.getText().toString().equals("")){
//					setListView();
					if(haseIt()){
						MaterialBean materialBean = new MaterialBean();
						materialBean.setMaterialName(foodNameExt.getText().toString().trim());
						materialBean.setMaterilaWeight(Double.parseDouble(foodWeightExt.getText().toString().trim()));
						list.add(0,materialBean);
						adapter = new MaterialAdapter(MaterialActivity.this, list);
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						foodNameExt.setText("");
						foodWeightExt.setText("");
						layout.setVisibility(View.GONE);
					}
				}else{
					CustomToast.showToast(MaterialActivity.this, "请输入食材信息", 2000);
				}
			}
		});
	}	
	
	//遍历，查看是否该项已经存咋，如果是，不能重复添加
	public boolean haseIt(){
		String name = foodNameExt.getText().toString().trim();
		String weight = foodWeightExt.getText().toString().trim();
		if(list != null){
			for(int i = 0; i < list.size(); i++){
				if(list.get(i).getMaterialName().equals(name) && (list.get(i).getMaterilaWeight()+"").equals(weight)){
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void setAdapter(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		int height = metrics.heightPixels;
		
		ListAdapter adapter = listView.getAdapter();
		if(adapter == null)
			return;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.height = height * 4 / 5;
		listView.setLayoutParams(params);
	}
	
	//获取数据
	private MaterialBean getData(){
		String picture = "";
		String foodName = foodNameExt.getText().toString();
		String foodWeight = foodWeightExt.getText().toString();
		if(foodName.equals("")){
			CustomToast.showToast(this, "请输入食材名", 2000);
			foodNameExt.requestFocus();
			return null;
		}else if(foodWeight.equals("")){
			CustomToast.showToast(this, "请输入食材重量", 2000);
			foodWeightExt.requestFocus();
			return null;
		}else{
			MaterialBean materialBean = new MaterialBean();
			materialBean.setMaterialName(foodName);
			materialBean.setMaterilaWeight(Float.parseFloat(foodWeight));
			return materialBean;
		}
	}
	
	private void setListView(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		List<Map<String, Object>> lists = (List<Map<String, Object>>) bundle.getSerializable("material");
		Log.i("material-setlistview", lists+"");
		if(lists != null || lists.size() > 0){
			for(int i = 0; i < lists.size()-1; i++){
				if(lists.get(i).get("weight").equals("重量"))
					continue;
				MaterialBean materialBean = new MaterialBean();
				materialBean.setMaterialName((String) lists.get(i).get("name"));
				materialBean.setMaterilaWeight(Double.parseDouble((String.valueOf(lists.get(i).get("weight")))));
				list.add(0,materialBean);
			}
		}
		System.out.println("material-list="+list);
		if(getData() == null && (list == null || list.size() <= 0))
			return ;
		if(getData() != null)
			list.add(0,getData());
		adapter = new MaterialAdapter(MaterialActivity.this, list);
		listView.setAdapter(adapter);
	}
}
