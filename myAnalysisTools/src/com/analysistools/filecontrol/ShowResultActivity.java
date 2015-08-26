package com.analysistools.filecontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.analysis.filedel.FileDeal;
import com.analysis.listview.drag.ReflashListView;
import com.analysis.listview.drag.ReflashListView.IReflashListener;
import com.analysistools.codeparsing.CodeParsing;
import com.analysistools.grammerAnalysis.GrammerActivity;
import com.analysistools.lexicalanalysis.LexicalActivity;
import com.example.myanalysistools.R;

public class ShowResultActivity extends Activity implements OnClickListener,
		OnItemClickListener, IReflashListener {

	private Button back;
	private Button edit;
	private EditText code;
	private ReflashListView resultListView;
	private Button lexical;
	private Button grammer;

	private String TAG = "";
	private String TagLex = "LexicalActivity";
	private String TagGram = "GrammerActivity";
	private List<String> content = new ArrayList<String>();
	private String LANGUAGE = "";
	private String ACTIVITY = "";
	private int returnResult = 0;

	private SortAdapter sortAdapter = null;
	private List<Map<String, String>> listTotal = new ArrayList<Map<String, String>>();// �����������
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();// ÿ�δ��20������

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showresult);

		findView();
		adaptView();

		Intent intent = getIntent();
		content = (List<String>) intent.getSerializableExtra("tag");
		Log.i("content", content.toString());
		// System.out.println("content.length=" + content.size());
		// System.out.println("contnt=====" + content);
		TAG = content.get(0);
		LANGUAGE = content.get(2);
		ACTIVITY = content.get(3);

		if (TAG.equals(TagLex)) {
			lexical.setBackgroundResource(R.color.palecoral);
			grammer.setBackgroundResource(R.color.moderateseagreen);
		} else if (TAG.equals(TagGram)) {
			lexical.setBackgroundResource(R.color.moderateseagreen);
			grammer.setBackgroundResource(R.color.palecoral);
		} else {
			Log.e("TAG", "error input");
		}

		showCode();
		setListTotalContent();
		int flag = setListContent();
		if (flag == -1) {
			Toast.makeText(ShowResultActivity.this, "û���µ�������",
					Toast.LENGTH_SHORT).show();
		}
		setResultList();
		onclick();
		resultListView.setOnItemClickListener(this);
	}

	private void findView() {
		back = (Button) findViewById(R.id.backShow);
		edit = (Button) findViewById(R.id.editShow);
		code = (EditText) findViewById(R.id.codeShow);
		resultListView = (ReflashListView) findViewById(R.id.resultShow);
		lexical = (Button) findViewById(R.id.lexicalAnalysisBtnShow);
		grammer = (Button) findViewById(R.id.grammerAnalysisBtnShow);
		resultListView.setInterface(this);
	}

	private void adaptView() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;

		code.setHeight(height / 5);
		code.setX(20);

		int bottomHeight = lexical.getHeight();
		Log.i("bottomHeight", bottomHeight + "");

		ListAdapter listAdapter = resultListView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		ViewGroup.LayoutParams params = resultListView.getLayoutParams();
		params.height = height / 6 - bottomHeight;
		resultListView.setLayoutParams(params);
	}

	private void showCode() {
		code.setText(content.get(1));
	}

	// �жϸ��ķ��Ƿ���Խ���
	/**
	 * @return 1:���� -1:������
	 */
	private int isAnalysize() {
		listTotal.clear();
		String codeString = content.get(1);
		System.out.println("codeString=" + codeString);
		if (codeString.equals("") || codeString.equals(null)) {
			Intent intent = new Intent();
			if (LANGUAGE.equals("") || LANGUAGE.equals(null)) {
				Toast.makeText(ShowResultActivity.this, "û��ѡ�����ԣ�",
						Toast.LENGTH_SHORT).show();

				intent.setClass(ShowResultActivity.this, EditFileActivity.class);
				intent.putExtra("tag", "");
				startActivity(intent);
				return -1;
			}
			Toast.makeText(ShowResultActivity.this,
					"����Ĵ���Ϊ��!�������������������벢����޸�!", Toast.LENGTH_SHORT).show();
			return -1;
		}
		Log.i("codeString", codeString);

		returnResult = CodeParsing.javaCodeParsing(codeString, LANGUAGE,
				ShowResultActivity.this);

		if (returnResult == 0) {
			Toast.makeText(ShowResultActivity.this, "���ķ��ǿմ�������ʶ��!",
					Toast.LENGTH_SHORT).show();
			return -1;
		} else if (returnResult == -1) {
			return -1;
		} else {
			return 1;
		}
	}

	// �ӽ���������ݷŵ�list����
	private void setListTotalContent() {

		int isAnalysis = isAnalysize();
		if (isAnalysis == -1) {
			listTotal.clear();
			return;
		} else {

			if (CodeParsing.keyWordList.size() > 1) {
				for (int i = 0; i < CodeParsing.keyWordList.size(); i++) {
					listTotal.add(CodeParsing.keyWordList.get(i));
				}
			}
			if (CodeParsing.variableList.size() > 1) {
				for (int i = 0; i < CodeParsing.variableList.size(); i++) {
					listTotal.add(CodeParsing.variableList.get(i));
				}
			}
			if (CodeParsing.numberList.size() > 1) {
				for (int i = 0; i < CodeParsing.numberList.size(); i++) {
					listTotal.add(CodeParsing.numberList.get(i));
				}
			}
			if (CodeParsing.otherWordList.size() > 1) {
				for (int i = 0; i < CodeParsing.otherWordList.size(); i++) {
					listTotal.add(CodeParsing.otherWordList.get(i));
				}
			}
		}
	}

	/**
	 * @return 1:�������ݿ��Լ��� -1��û��������
	 */
	private int setListContent() {
		if (listTotal.size() > 0) {
			System.out.println("executed");
			if (listTotal.size() >= 20) {
				for (int i = 0; i < 20; i++) {
					list.add(listTotal.get(i));
				}
				for (int i = 0; i < 20; i++) {
					listTotal.remove(0);
				}
			} else {
				for (int i = 0; i < listTotal.size(); i++) {
					list.add(listTotal.get(i));
				}
				listTotal.clear();
			}
			return 1;
		}
		return -1;
	}

	private void setResultList() {
		sortAdapter = new SortAdapter(this, list);
		resultListView.setAdapter(sortAdapter);
	}

	private void onclick() {
		back.setOnClickListener(this);
		edit.setOnClickListener(this);
		lexical.setOnClickListener(this);
		grammer.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();

		switch (arg0.getId()) {
		case R.id.backShow:
			if (ACTIVITY.equals(ExistedFileActivity.ACTIVITY)) {
				intent.setClass(ShowResultActivity.this,
						ExistedFileActivity.class);
				intent.putExtra("tag", "");
				startActivity(intent);
			} else if (ACTIVITY.equals(EditFileActivity.ACTIVITY)) {
				intent.setClass(ShowResultActivity.this, EditFileActivity.class);
				intent.putExtra("tag", "");
				startActivity(intent);
			}

			break;
		case R.id.editShow:
			if (!code.getText().toString().equals(content.get(1))) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ShowResultActivity.this)
						.setMessage("�Ƿ񱣴��޸�")
						.setPositiveButton("��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										arg0.dismiss();
										String cur = content.get(2);
										String cod = content.get(3);
										content.remove(1);
										content.remove(1);
										content.remove(1);
										content.add(code.getText().toString());
										content.add(cur);
										content.add(cod);
										list.clear();
										setListTotalContent();
										int flag = setListContent();
										if (flag == -1) {
											Toast.makeText(
													ShowResultActivity.this,
													"û���µ�������",
													Toast.LENGTH_SHORT).show();
										}
										setResultList();
										new AlertDialog.Builder(
												ShowResultActivity.this)
												.setMessage("�Ƿ񱣴����ļ���")
												.setPositiveButton(
														"��",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface arg0,
																	int arg1) {
																int isAna = isAnalysize();
																if (isAna == -1) {
																	Toast.makeText(
																			ShowResultActivity.this,
																			"���ķ��д��󣬲��ܱ����棡",
																			Toast.LENGTH_SHORT)
																			.show();
																	return;
																}
																final EditText ed = new EditText(
																		ShowResultActivity.this);
																new AlertDialog.Builder(
																		ShowResultActivity.this)
																		.setTitle(
																				"�������ļ������洢��ʽΪtxt��")
																		.setIcon(
																				android.R.drawable.ic_dialog_info)
																		.setView(ed)
																		.setPositiveButton(
																				"ȷ��",
																				new DialogInterface.OnClickListener() {

																					@Override
																					public void onClick(
																							DialogInterface arg0,
																							int arg1) {
																						// TODO
																						// Auto-generated
																						// method
																						// stub
																						String saveContent = code
																								.getText()
																								.toString();
																						String fileName = ed
																								.getText()
																								.toString();
																						if (fileName
																								.equals("")) {
																							Toast.makeText(
																									getApplicationContext(),
																									"�ļ�������Ϊ�գ�����ʧ�ܣ�"
																											+ fileName,
																									Toast.LENGTH_LONG)
																									.show();
																						} else {
																							if (fileName
																									.contains(".")) {
																								String[] str = fileName
																										.split("\\.");
																								if (str[str.length - 1]
																										.equals("txt"))
																									FileDeal.writeCover(
																											saveContent,
																											fileName,
																											ShowResultActivity.this);
																								else
																									FileDeal.writeCover(
																											saveContent,
																											fileName
																													+ ".txt",
																											ShowResultActivity.this);
																							} else {
																								FileDeal.writeCover(
																										saveContent,
																										fileName
																												+ ".txt",
																										ShowResultActivity.this);
																							}
																						}
																					}
																				})
																		.setNegativeButton(
																				"ȡ��",
																				new DialogInterface.OnClickListener() {

																					@Override
																					public void onClick(
																							DialogInterface arg0,
																							int arg1) {
																						// TODO
																						// Auto-generated
																						// method
																						// stub
																						arg0.dismiss();
																					}
																				})
																		.show();

															}
														})
												.setNegativeButton(
														"��",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface arg0,
																	int arg1) {
																// TODO
																// Auto-generated
																// method stub
																arg0.dismiss();
															}
														}).show();
									}
								})
						.setNegativeButton("��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										arg0.dismiss();
										code.setText(content.get(1));
									}
								});
				builder.show();

			}
			break;
		case R.id.lexicalAnalysisBtnShow:
			if (TAG.equals(TagLex)) {

			} else if (TAG.equals(TagGram)) {
				intent.setClass(ShowResultActivity.this, LexicalActivity.class);
				startActivity(intent);
			} else {
				Log.i("tag", "error tag");
			}
			break;
		case R.id.grammerAnalysisBtnShow:
			if (TAG.equals(TagLex)) {
				intent.setClass(ShowResultActivity.this, GrammerActivity.class);
				startActivity(intent);
			} else if (TAG.equals(TagGram)) {

			} else {
				Log.i("tag", "error tag");
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReflash() {
		// TODO Auto-generated method stub
		System.out.println("ooooooooo");
		// ��ȡ��������
		int flag = setListContent();
		System.out.println("flag=====" + flag);
		Log.i("flag^^^^^^^^^^^^^^^", flag + "");
		if (flag == -1) {
			Toast.makeText(ShowResultActivity.this, "û���µ�������",
					Toast.LENGTH_SHORT).show();
			resultListView.reflashComplete();
		} else {
			// ֪ͨ������ʾ����
			setResultList();
			// ֪ͨlistViewˢ���������
			resultListView.reflashComplete();
		}
		/*
		 * Handler handler = new Handler(); handler.postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub //
		 * ��ȡ�������� int flag = setListContent();
		 * System.out.println("flag====="+flag); Log.i("flag^^^^^^^^^^^^^^^",
		 * flag+""); if (flag == -1) { Toast.makeText(ShowResultActivity.this,
		 * "û���µ�������", Toast.LENGTH_SHORT).show(); return; } else { // ֪ͨ������ʾ����
		 * setResultList(); // ֪ͨlistViewˢ���������
		 * resultListView.reflashComplete(); } } }, 2000);
		 */
	}
}
