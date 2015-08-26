package com.market.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.market.model.StringBean;
import com.market.service.impl.UpLoadImageServiceImpl;
import com.market.utils.StringUtils;

public class UploadGoodsInfoCl extends HttpServlet {

	// user的键值对
	private String USER_ID = "userId";
	private int UserId;

	// 物品名称
	private String GOODS_NAME = "goodsName";
	private String goodsName = "";

	// 发布物品时，物品描述
	private String GOODS_DESCRIBE = "goodsDescribe";
	private String goodsDescribe = "";
	// 物品类型
	private String GOODS_TYPE = "goodsType";
	private String goodsType = "";
	// 物品价格
	private String GOODS_PRICE = "goodsPrice";
	private String goodsPrice = "";
	// 联系方式
	private String GOODS_CONNECT = "goodsConnect";
	private String goodsConnect = "";

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
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		PrintWriter out = resp.getWriter();
		System.out.println(req.getParameter(USER_ID)
				+ req.getParameter(GOODS_NAME)
				+ req.getParameter(GOODS_DESCRIBE)
				+ req.getParameter(GOODS_TYPE) + req.getParameter(GOODS_PRICE)
				+ req.getParameter(GOODS_CONNECT));
		if (checkMessage(req)) {
			UserId = Integer.parseInt(req.getParameter(USER_ID));
			goodsName = req.getParameter(GOODS_NAME);
			goodsDescribe = req.getParameter(GOODS_DESCRIBE);
			goodsType = req.getParameter(GOODS_TYPE);
			goodsPrice = req.getParameter(GOODS_PRICE);
			goodsConnect = req.getParameter(GOODS_CONNECT);

			//goodsDescribe = URLDecoder.decode(goodsDescribe, "utf-8");

			int goodsId = UpLoadImageServiceImpl.upLoadGoodsInfo(goodsName,
					goodsType, goodsDescribe, goodsPrice, 0, UserId, 0,
					getTime(), goodsConnect);
			out.write(StringUtils.base64EnCode(StringUtils.strToJson(goodsId
					+ "")));
		} else {
			out.write(StringUtils.base64EnCode(StringUtils
					.strToJson(StringBean.FORM_NOT_COMPLETE)));
		}

	}

	private boolean checkMessage(HttpServletRequest request) {
		if (request.getParameter(USER_ID) != null)
			if (request.getParameter(GOODS_NAME) != null)
				if (request.getParameter(GOODS_DESCRIBE) != null)
					if (request.getParameter(GOODS_TYPE) != null)
						if (request.getParameter(GOODS_PRICE) != null)
							if (request.getParameter(GOODS_CONNECT) != null)
								return true;
		return false;

	}

	private String getTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");
		String time = dateFormat.format(now);
		return time;
	}
}
