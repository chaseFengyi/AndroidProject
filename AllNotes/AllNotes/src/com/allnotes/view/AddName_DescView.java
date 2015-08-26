package com.allnotes.view;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * дDialog�Ĳ���Ϊ��������������ı���
 * @author FengYi~
 *2015��7��29��11:39:13
 */
public class AddName_DescView {
	public static View name_desc(String title, String desc, Context context){
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(1);// ��ֱ���� Ĭ��Ϊˮƽ����0

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
