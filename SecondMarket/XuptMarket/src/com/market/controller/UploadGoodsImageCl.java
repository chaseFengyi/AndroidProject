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

public class UploadGoodsImageCl extends HttpServlet {
	private PrintWriter pw = null;
	private InputStream is = null;

	private String goodsId;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");

		String imagePath;
		imagePath = ImgHandle(req, resp);

		// update�û�ͷ��
		try {
			UpLoadImageServiceImpl.upLoadGoodsImage(Integer.parseInt(goodsId),
					imagePath);
			pw.write(StringUtils.base64EnCode(StringUtils
					.strToJson("UpLoadSuccess")));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			pw.write(StringUtils.base64EnCode(StringUtils
					.strToJson("UpLoadFail")));
		}

		pw.flush();
		pw.close();
	}

	private String ImgHandle(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		pw = response.getWriter();
		// ��ô����ļ���Ŀ������
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// ��ȡ�ļ��ϴ���Ҫ�����·����upload�ļ�������ڡ�
		// String path = request.getSession().getServletContext()
		// .getRealPath("/img");
		String path = "/img";

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
				// ���������ǷǼ��ַ���������ͼƬ����Ƶ����Ƶ�ȶ������ļ���
				if (name.contains("image")) {
					// ��ȡ·����
					String filename = item.getName();
					String t[] = filename.split(".");
					String t1[] = t[0].split("-");
					goodsId = t1[0];
					// �������ļ�
					filename = ReName(t[0], t[1]);
					// �������ṩ�ķ���ֱ��д���ļ��С�
					item.write(new File(path, filename));

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
