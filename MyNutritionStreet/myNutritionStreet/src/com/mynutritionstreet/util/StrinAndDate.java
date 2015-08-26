package com.mynutritionstreet.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StrinAndDate {
	/*
	* DateToString(Date date),时间转换成字符串
	*/
	public static String DateToString(Date date) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = formatDate.format(date);
		try {
			date = formatDate.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatDate.format(date);
	}

	/*
	 * Date StringToDate(String s),字符串转换成时间
	 */
	public static java.util.Date StringToUtilDate(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		try {
			Date date = sdf.parse(s);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static java.sql.Date StringToSqlDate(String s) {
		try {
			return java.sql.Date.valueOf(s);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
