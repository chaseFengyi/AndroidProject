package com.allnotes.view;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 写Dialog的布局为两个输入框，两个文本框
 * @author FengYi~
 *2015年7月29日11:39:13
 */
public class AddName_DescView {
	public static View name_desc(String title, String desc, Context context){
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(1);// 垂直布局 默认为水平布局0

		TextView typeNameTxt = new TextView(context);
		typeNameTxt.setText(title);
		typeNameTxt.setTextSize(18);
		typeNameTxt.setId(1);
		layout.addView(typeNameTxt);

		EditText typeNameExt = new EditText(context);
		typeNameExt.setInputType(InputType.TYPE_CLASS_TEXT);
		typeNameExt.setId(2);
		layout.addView(typeNameExt);

		TextView typeDesTxt = new TextView(context);
		typeDesTxt.setText(desc);
		typeDesTxt.setTextSize(18);
		typeDesTxt.setId(3);
		layout.addView(typeDesTxt);

		EditText typeDesExt = new EditText(context);
		typeDesExt.setInputType(InputType.TYPE_CLASS_TEXT);
		typeDesExt.setId(4);
		layout.addView(typeDesExt);
		return layout;
	}
}
