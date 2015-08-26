package com.market.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.market.model.StringBean;
import com.market.service.impl.ClassifiedSearchServiceImpl;
import com.market.service.impl.IndistinctSearchServiceImpl;
import com.market.utils.StringUtils;

public class IndistinctSearchCl extends HttpServlet {
	private List<Map<String, Object>> data_json = new ArrayList<Map<String, Object>>();

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

		int page = 1;
		String str = "";
		try {

			page = Integer.parseInt(req.getParameter("page"));
			str = req.getParameter("keywords");
			data_json = IndistinctSearchServiceImpl.indistinctSearchByString(
					page, str);

			if (data_json != null)
				out.print(JSONArray.fromObject(data_json));
			else
				out.print(StringUtils.strToJson(StringBean.NO_MORE));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			out.print(StringUtils.base64EnCode(StringUtils
					.strToJson(StringBean.NOT_INT)));
		}

		out.flush();
		out.close();
	}

}
