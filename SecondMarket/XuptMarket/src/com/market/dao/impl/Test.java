package com.market.dao.impl;

import java.util.List;
import java.util.Map;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(CreateWordDao.createAllTable()){
			System.out.println("创建数据库表成功!");
		}else{
			System.out.println("创建数据库表失败!");
			return;
		}
	}

}
