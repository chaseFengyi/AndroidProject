package com.market.dao.impl;

import java.util.List;
import java.util.Map;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(CreateWordDao.createAllTable()){
			System.out.println("�������ݿ��ɹ�!");
		}else{
			System.out.println("�������ݿ��ʧ��!");
			return;
		}
	}

}
