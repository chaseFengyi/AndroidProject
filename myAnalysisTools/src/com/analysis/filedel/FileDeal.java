package com.analysis.filedel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.Toast;

public class FileDeal {

	private static final String STOREPOSITION = "MyAnalysisTools";

	public static String read(String fileName, Context context) {
		File file = createDir(context);
		if (file == null) {
			Toast.makeText(context, "����Ŀ¼ʧ�ܣ�", Toast.LENGTH_SHORT).show();
			return null;
		} else {
			File targetFile = null;
			try {
				targetFile = new File(file.getCanonicalPath() + "/" + fileName);
				if (!targetFile.exists()) {
					Toast.makeText(context, "�ļ������ڣ����ܶ�ȡ��", Toast.LENGTH_SHORT)
							.show();
				} else {
					// ��ȡָ���ļ���Ӧ��������
					FileInputStream fileInputStream = new FileInputStream(
							targetFile.toString());
					// ��ָ����������װ��BufferedReader
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(fileInputStream));
					StringBuilder stringBuilder = new StringBuilder();
					String line = null;
					// ѭ����ȡ�ļ�����
					while ((line = bufferedReader.readLine()) != null) {
						stringBuilder.append(line);
					}
					bufferedReader.close();

					Toast.makeText(context, "��"+targetFile.toString()+"��ȡ�ɹ�", Toast.LENGTH_SHORT).show();

					return stringBuilder.toString();
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "��"+targetFile.toString()+"��ȡʧ��", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "��"+targetFile.toString()+"��ȡʧ��", Toast.LENGTH_SHORT).show();
			}
		}

		return null;
	}

	// д������ֱ�Ӹ���
	public static void writeCover(String content, String fileName,
			Context context) {

		if (content.equals(null) || content.equals("")) {
			Toast.makeText(context, "Ҫ���������Ϊ��", Toast.LENGTH_SHORT).show();
			return;
		}

		File file = createDir(context);
		if (file == null) {
			Toast.makeText(context, "����"+file.toString()+"Ŀ¼ʧ�ܣ�", Toast.LENGTH_SHORT).show();
			return;
		} else {
			final File targetFile;
			try {
				targetFile = new File(file.getCanonicalPath() + "/" + fileName);
				final String contentString = content;
				final Context context2 = context;
				if (targetFile.exists()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context)
							.setMessage("���ļ��Ѵ��ڣ�����������Ḳ��ԭ�����ļ�����")
							.setPositiveButton("����",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											try {
												FileOutputStream fileOutputStream = new FileOutputStream(
														targetFile);
												byte[] bytes = contentString
														.getBytes();
												fileOutputStream.write(bytes);
												fileOutputStream.close();
												Toast.makeText(
														context2,
														"����ɹ����ѱ�����"
																+ targetFile,
														Toast.LENGTH_SHORT)
														.show();
											} catch (IOException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
												Toast.makeText(context2,
														targetFile+"����ʧ�ܣ�",
														Toast.LENGTH_SHORT)
														.show();
											}

											arg0.dismiss();
										}
									})
							.setNegativeButton("ȡ��",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											arg0.dismiss();
										}
									});
					builder.show();
				} else {
					FileOutputStream fileOutputStream;
					try {
						fileOutputStream = new FileOutputStream(targetFile);
						byte[] bytes = contentString.getBytes();
						fileOutputStream.write(bytes);
						fileOutputStream.close();
						Toast.makeText(context, "����ɹ����ѱ�������"+targetFile, Toast.LENGTH_SHORT)
								.show();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						Toast.makeText(context2,
								"����ʧ�ܣ�",
								Toast.LENGTH_SHORT)
								.show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(context2,
								"����ʧ�ܣ�",
								Toast.LENGTH_SHORT)
								.show();
					}

				}
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				Toast.makeText(context,
						"����ʧ�ܣ�",
						Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

	// д�����ݻ���׷�ӵķ�ʽ��ӽ�ȥ
	// ��ȡָ��Ŀ¼�µ��ļ�
	public static void writeAppend(String content, String fileName,
			Context context) {
		String path = getSDPath();

		if (path.equals(null)) {
			Toast.makeText(context, "����Ƿ����sd��", Toast.LENGTH_SHORT).show();
		} else {
			File file = new File(path);
			try {
				File targetFile = new File(file.getCanonicalPath()
						+ STOREPOSITION + "/" + fileName);
				// ��ָ���ļ�����RandomAccessFile����
				RandomAccessFile randomAccessFile = new RandomAccessFile(
						targetFile, "rw");
				// ���ļ���¼ָ���ƶ������
				randomAccessFile.seek(targetFile.length());
				// ����ļ�����
				randomAccessFile.write(content.getBytes());
				// �ر�
				randomAccessFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String getSDPath() {

		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);// �ж�sd���Ƿ����
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}

		return sdDir.toString();
	}

	// ����ָ��Ŀ¼
	public static File createDir(Context context) {
		String path = getSDPath();

		if (path.equals(null)) {
			Toast.makeText(context, "����Ƿ����sd��", Toast.LENGTH_SHORT).show();
		} else {
			File file = new File(path);
			try {
				File files = new File(file.getCanonicalPath() + "/"
						+ STOREPOSITION);
				if (files.exists()) {
					return files;
				} else {
					if (files.mkdirs()) {
						return files;
					} else {
						// Toast.makeText(context, "����"+files+"Ŀ¼ʧ�ܣ�",
						// Toast.LENGTH_SHORT).show();
						return null;
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
			}
		}

		return null;
	}

}
