package com.analysistools.filecontrol;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.analysistools.codeparsing.CodeParsing;
import com.example.myanalysistools.R;

public class SortAdapter extends BaseAdapter {
	 private List<Map<String, String>> list = null;  
	    private Context mContext;  
        String symbol;
        String value;
	      
	    public SortAdapter(Context mContext, List<Map<String, String>> list) {  
	        this.mContext = mContext;  
	        this.list = list;  
	    }  
	      
	    /** 
	     * 当ListView数据发生变化时,调用此方法来更新ListView 
	     * @param list 
	     */  
	    public void updateListView(List<Map<String, String>> list){  
	        this.list = list;  
	        notifyDataSetChanged();  
	    }  
	  
	    @Override
		public int getCount() {  
	        return list.size();  
	    }  
	  
	    @Override
		public Object getItem(int position) {  
	        return list.get(position);  
	    }  
	  
	    @Override
		public long getItemId(int position) {  
	        return position;  
	    }  
	  
	    @Override
		public View getView(final int position, View view, ViewGroup arg2) {  
	        ViewHolder viewHolder = null;  
	        final Map<String, String> mContent = (Map<String, String>)this.list.get(position);  
//	        if (view == null) {  
	            viewHolder = new ViewHolder();  
	            view = LayoutInflater.from(mContext).inflate(R.layout.item, null);  
	            viewHolder.symbol = (TextView)view.findViewById(R.id.symbol);
	            viewHolder.value = (TextView)view.findViewById(R.id.value);
	            view.setTag(viewHolder);  
//	        } else {  
//	            viewHolder = (ViewHolder) view.getTag();  
//	        }  
//	        System.out.println("cContent="+mContent);
//	        System.out.println("listlist----="+list);
	        Iterator<String> iterator = mContent.keySet().iterator();
	        while(iterator.hasNext()){
//	        	System.out.println(",,,,,,,,,,)))))))))))");
	        	symbol = iterator.next();
	        }
	        value = mContent.get(symbol);
//	        System.out.println("symbol="+symbol);
//	        System.out.println("value="+value);
	        
	        
	        if(symbol.equals(CodeParsing.KEYWORD) || symbol.equals(CodeParsing.VARABLE) 
	        		|| symbol.equals(CodeParsing.NUMBER) ||
	        		symbol.equals(CodeParsing.OTHER)){
	        	viewHolder.symbol.setText(symbol);
//	        	System.out.println("********symbol="+symbol);
	        	viewHolder.symbol.setTextColor(mContext.getResources().getColor(R.color.darkorange));
	        	viewHolder.symbol.setGravity(Gravity.LEFT|Gravity.CENTER_HORIZONTAL);
	        	viewHolder.value.setVisibility(View.GONE);
	        }else{
//	        	System.out.println("@@@@@@@@@@@@"+symbol);
	        	viewHolder.symbol.setTextColor(mContext.getResources().getColor(R.color.khaki));
	        	viewHolder.value.setTextColor(mContext.getResources().getColor(R.color.khaki));
	        	viewHolder.symbol.setText("    {    "+symbol);
	        	viewHolder.value.setText(",     "+value+"}");
	        }
	        
	        return view;  
	  
	    }  
	  
	    final static class ViewHolder {  
	        TextView symbol;  
	        TextView value;  
	    }
}
