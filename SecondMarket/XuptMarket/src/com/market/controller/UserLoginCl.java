package com.market.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.market.model.StringBean;
import com.market.model.UserInfoBean;
import com.market.service.impl.UserInfoServiceImpl;
import com.market.utils.StringUtils;
import com.market.utils.getInfoFromServer;

public class UserLoginCl extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInfoBean userInfo;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String UserSNum = req.getParameter("userNum");
		String UserPasswd = req.getParameter("userPassword");
		System.out.println("!!!!!!!!!!!!!!!");
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		PrintWriter out = resp.getWriter();
		userInfo = new UserInfoBean();
		userInfo.setUserSchoolNum(UserSNum);
		userInfo.setUserPassword(UserPasswd);

		// 先从数据库中查询
		try {
			userInfo = UserInfoServiceImpl
					.checkByUserSchoolNumberAnduserPassword(userInfo);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 数据库中没有从服务器查询
		if (userInfo.getUserPassword() == null
				|| userInfo.getUserSchoolNum() == null) {
			userInfo.setUserSchoolNum(UserSNum);
			userInfo.setUserPassword(UserPasswd);
			userInfo = getInfoFromServer.getNameFromServer(userInfo);
			if (userInfo.getUserName().equals(StringBean.USER_NOT_EXIST)) {
				out.write(StringUtils.base64EnCode(StringUtils
						.strToJson(StringBean.USER_NOT_EXIST)));
			} else if (userInfo.getUserName().equals(
					StringBean.USER_PASSWD_ERROR)) {
				out.write(StringUtils.base64EnCode(StringUtils
						.strToJson(StringBean.USER_PASSWD_ERROR)));
			} else {
				try {
					userInfo = UserInfoServiceImpl.regUser(userInfo);
					JSONObject json = JSONObject.fromObject(userInfo);
					out.write(StringUtils.base64EnCode(json));
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			JSONObject json = JSONObject.fromObject(userInfo);
			out.write(StringUtils.base64EnCode(json));
		}

		out.flush();
		out.close();
	}

}
