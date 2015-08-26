package com.market.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.market.model.StringBean;
import com.market.service.impl.DelGoodsServiceImpl;
import com.market.service.impl.GoodsWantedServiceImpl;
import com.market.utils.StringUtils;

public class DelGoodsCl extends HttpServlet{

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
		int count = 1;
		try {
			count = Integer.parseInt(req.getParameter("goodsId"));
			if (DelGoodsServiceImpl.delGoods(count)) {
				out.print(StringUtils.base64EnCode(StringUtils
						.strToJson(StringBean.SUCCESS)));
			}else{
				out.print(StringUtils.base64EnCode(StringUtils
						.strToJson(StringBean.ALREADY_DEL)));
			}
		} catch (Exception e) {
			// TODO: handle exception
			out.print(StringUtils.base64EnCode(StringUtils
					.strToJson(StringBean.NOT_INT)));
		}

		out.flush();
		out.close();
		
	}

}
