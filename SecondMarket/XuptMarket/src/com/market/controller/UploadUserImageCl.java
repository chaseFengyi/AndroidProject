package com.market.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.market.service.impl.UpLoadImageServiceImpl;
import com.market.utils.StringUtils;

public class UploadUserImageCl extends HttpServlet {
	private InputStream is = null;
	private PrintWriter pw = null;

	private String userId;
	private String userNike;
	private String userSex;
	private String userGrade;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String imagePath;
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		imagePath = ImgHandle(request, response);

		// update�û�ͷ��
		try {
			UpLoadImageServiceImpl.upLoadUserImage(userId, userNike, userSex,
					userGrade, imagePath);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			pw.write(StringUtils.base64EnCode(StringUtils
					.strToJson("UpLoadFail")));
		}

		pw.flush();
		pw.close();
	}

	private String getTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd-HH:mm:ss");
		String time = dateFormat.format(now);
		return time;
	}

	private String ImgHandle(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		pw = response.getWriter();
		// ��ô����ļ���Ŀ������
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// ��ȡ�ļ��ϴ���Ҫ�����·����upload�ļ�������ڡ�
		String path = request.getSession().getServletContext()
				.getRealPath("/img");

		// ������ʱ����ļ��Ĵ洢�ң�����洢�ҿ��Ժ����մ洢�ļ����ļ��в�ͬ����Ϊ���ļ��ܴ�Ļ���ռ�ù����ڴ��������ô洢�ҡ�
		factory.setRepository(new File(path));
		// ���û���Ĵ�С�����ϴ��ļ���������������ʱ���ͷŵ���ʱ�洢�ҡ�
		factory.setSizeThreshold(1024 * 1024);
		// �ϴ��������ࣨ��ˮƽAPI�ϴ�������
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			// ���� parseRequest��request������ ����ϴ��ļ� FileItem �ļ���list ��ʵ�ֶ��ļ��ϴ���
			List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
			for (FileItem item : list) {
				// ��ȡ���������֡�
				String name = item.getFieldName();
				is = item.getInputStream();

				if (name.contains("userNike")) {
					userNike = item.getString();
					System.out.println(userNike);
				} else if (name.contains("userId")) {
					userId = item.getString();
				} else if (name.contains("userSex")) {
					userSex = item.getString();
				} else if (name.contains("userGrade")) {
					userGrade = item.getString();
				}
				// ���������ǷǼ��ַ���������ͼƬ����Ƶ����Ƶ�ȶ������ļ���
				else if (name.contains("image")) {
					// ��ȡ·����
					String filename = item.getName();

					String t = filename.substring(filename.lastIndexOf("."));

					// �������ļ�
					filename = ReName("user-", t);
					// �������ṩ�ķ���ֱ��д���ļ��С�
					item.write(new File(path, filename));

					pw.write(StringUtils.base64EnCode(StringUtils
							.strToJson(path + filename)));
					return path + "\\" + filename;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			pw.write(StringUtils.base64EnCode(StringUtils
					.strToJson("UpLoadFail")));
			return null;
		}

		return null;
	}

	private String ReName(String str, String t) {
		Date now = new Date();
		StringBuffer sb = new StringBuffer(str);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String ran = dateFormat.format(now) + "-"
				+ String.valueOf((int) (Math.random() * 1000));
		sb.append(ran + t);

		return sb.toString();
	}
}
