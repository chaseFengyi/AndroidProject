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
			Toast.makeText(context, "创建目录失败！", Toast.LENGTH_SHORT).show();
			return null;
		} else {
			File targetFile = null;
			try {
				targetFile = new File(file.getCanonicalPath() + "/" + fileName);
				if (!targetFile.exists()) {
					Toast.makeText(context, "文件不存在，不能读取！", Toast.LENGTH_SHORT)
							.show();
				} else {
					// 获取指定文件对应的输入流
					FileInputStream fileInputStream = new FileInputStream(
							targetFile.toString());
					// 将指定输入流包装成BufferedReader
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(fileInputStream));
					StringBuilder stringBuilder = new StringBuilder();
					String line = null;
					// 循环读取文件内容
					while ((line = bufferedReader.readLine()) != null) {
						stringBuilder.append(line);
					}
					bufferedReader.close();

					Toast.makeText(context, "从"+targetFile.toString()+"读取成功", Toast.LENGTH_SHORT).show();

					return stringBuilder.toString();
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "从"+targetFile.toString()+"读取失败", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "从"+targetFile.toString()+"读取失败", Toast.LENGTH_SHORT).show();
			}
		}

		return null;
	}

	// 写的内容直接覆盖
	public static void writeCover(String content, String fileName,
			Context context) {

		if (content.equals(null) || content.equals("")) {
			Toast.makeText(context, "要保存的内容为空", Toast.LENGTH_SHORT).show();
			return;
		}

		File file = createDir(context);
		if (file == null) {
			Toast.makeText(context, "创建"+file.toString()+"目录失败！", Toast.LENGTH_SHORT).show();
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
							.setMessage("该文件已存在，如果继续将会覆盖原来的文件内容")
							.setPositiveButton("继续",
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
														"保存成功！已保存至"
																+ targetFile,
														Toast.LENGTH_SHORT)
														.show();
											} catch (IOException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
												Toast.makeText(context2,
														targetFile+"保存失败！",
														Toast.LENGTH_SHORT)
														.show();
											}

											arg0.dismiss();
										}
									})
							.setNegativeButton("取消",
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
						Toast.makeText(context, "保存成功！已保存至："+targetFile, Toast.LENGTH_SHORT)
								.show();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						Toast.makeText(context2,
								"保存失败！",
								Toast.LENGTH_SHORT)
								.show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(context2,
								"保存失败！",
								Toast.LENGTH_SHORT)
								.show();
					}

				}
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				Toast.makeText(context,
						"保存失败！",
						Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

	// 写的内容会以追加的方式添加进去
	// 读取指定目录下的文件
	public static void writeAppend(String content, String fileName,
			Context context) {
		String path = getSDPath();

		if (path.equals(null)) {
			Toast.makeText(context, "检查是否插入sd卡", Toast.LENGTH_SHORT).show();
		} else {
			File file = new File(path);
			try {
				File targetFile = new File(file.getCanonicalPath()
						+ STOREPOSITION + "/" + fileName);
				// 以指定文件创建RandomAccessFile对象
				RandomAccessFile randomAccessFile = new RandomAccessFile(
						targetFile, "rw");
				// 将文件记录指针移动到最后
				randomAccessFile.seek(targetFile.length());
				// 输出文件内容
				randomAccessFile.write(content.getBytes());
				// 关闭
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
				android.os.Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}

		return sdDir.toString();
	}

	// 创建指定目录
	public static File createDir(Context context) {
		String path = getSDPath();

		if (path.equals(null)) {
			Toast.makeText(context, "检查是否插入sd卡", Toast.LENGTH_SHORT).show();
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
						// Toast.makeText(context, "创建"+files+"目录失败！",
						// Toast.LENGTH_SHORT).show();
						return null;
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "保存失败！", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "保存失败！", Toast.LENGTH_SHORT).show();
			}
		}

		return null;
	}

}
