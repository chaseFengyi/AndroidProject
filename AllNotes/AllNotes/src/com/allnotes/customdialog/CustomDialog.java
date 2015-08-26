package com.allnotes.customdialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author FengYi~
 *2015��7��28��16:50:58
 */
public class CustomDialog {
	
	private static AlertDialog.Builder mbBuilder;
	
	public static void dialogListChoice(Context mContext, int resID, String[] mItems){
		dialogListChoice(mContext, mContext.getString(resID), mItems);
	}
	
	//�б�ѡ��
	/**
	 * @param mContext
	 * @param title
	 * @param mItems
	 * @return  ���ص���������±�
	 */
	public static void dialogListChoice(Context mContext, String title, String[] mItems){
		final DialogHolder dialogHolder = new DialogHolder();
		
		if(mbBuilder == null){
			mbBuilder = new AlertDialog.Builder(mContext);
		}
		mbBuilder.setTitle(title);
		mbBuilder.setItems(mItems, new DialogInterface.OnClickListener() {
			
			@SuppressWarnings("static-access")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialogHolder.value = which;
			}
		});
		mbBuilder.create().show();
	}
	
	public static class DialogHolder{
		public static int value = -1;

	}
	
	// ��ʼ����ʾ��
		public static ProgressDialog initDialog(ProgressDialog mProgressDialog, String msg, Context context) {
			// ��ʼ����ʾ��
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setTitle("��ʾ");
			mProgressDialog.setMessage(msg);
			
			return mProgressDialog;
		}

}
