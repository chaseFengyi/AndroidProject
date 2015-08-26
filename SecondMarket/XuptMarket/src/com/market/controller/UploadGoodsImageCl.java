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

		// update用户头像
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
		// 获得磁盘文件条目工厂。
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 获取文件上传需要保存的路径，upload文件夹需存在。
		// String path = request.getSession().getServletContext()
		// .getRealPath("/img");
		String path = "/img";

		// 设置暂时存放文件的存储室，这个存储室可以和最终存储文件的文件夹不同。因为当文件很大的话会占用过多内存所以设置存储室。
		factory.setRepository(new File(path));
		// 设置缓存的大小，当上传文件的容量超过缓存时，就放到暂时存储室。
		factory.setSizeThreshold(1024 * 1024);
		// 上传处理工具类（高水平API上传处理？）
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			// 调用 parseRequest（request）方法 获得上传文件 FileItem 的集合list 可实现多文件上传。
			List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
			for (FileItem item : list) {
				// 获取表单属性名字。
				String name = item.getFieldName();
				is = item.getInputStream();
				// 如果传入的是非简单字符串，而是图片，音频，视频等二进制文件。
				if (name.contains("image")) {
					// 获取路径名
					String filename = item.getName();
					String t[] = filename.split(".");
					String t1[] = t[0].split("-");
					goodsId = t1[0];
					// 重命名文件
					filename = ReName(t[0], t[1]);
					// 第三方提供的方法直接写到文件中。
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
