package com.analysistools.bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class KeyWord {
	
	//算术运算符
	public static Map<String, Integer> arithmeticOperators(){
		Map<String, Integer> ope = new HashMap<String, Integer>();
		
		ope.put("+", 100);
		ope.put("-", 101);
		ope.put("*", 102);
		ope.put("/", 103);
		ope.put("%", 104);
		ope.put("++", 105);
		ope.put("--", 106);
		
		return ope;
	}
	
	//关系运算符
	public static Map<String, Integer> relationalOperators(){
		Map<String, Integer> ope = new HashMap<String, Integer>();
		
		ope.put("==", 107);
		ope.put("!=", 108);
		ope.put(">", 109);
		ope.put("<", 110);
		ope.put(">=", 111);
		ope.put("<=", 112);  
		
		return ope;
	}
	
	//位运算符
	public static Map<String, Integer> anOperators(){
		Map<String, Integer> ope = new HashMap<String, Integer>();
		
		ope.put("&", 113);
		ope.put("|", 114);
		ope.put("^", 115);
		ope.put("~", 116);
		ope.put("<<", 117);
		ope.put(">>", 118);
		ope.put(">>>", 119);
		
		return ope;
	}
	
	//逻辑运算符
	public static Map<String, Integer> logicalOperators(){
		Map<String, Integer> ope = new HashMap<String, Integer>();
		
		ope.put("&&", 120);
		ope.put("||", 121);
		ope.put("!", 122);
		
		return ope;
	}
	
	//赋值运算符
	public static Map<String, Integer> assignmentOperators(){
		Map<String, Integer> ope = new HashMap<String, Integer>();
		
		ope.put("=", 123);
		ope.put("+=", 124);
		ope.put("-=", 125);
		ope.put("*=", 126);
		ope.put("/=", 127);
		ope.put("%=", 128);
		ope.put("<<=", 129);
		ope.put(">>=", 130);
		ope.put("&=", 131);
		ope.put("^=", 132);
		ope.put("|=", 133);
		
		return ope;
	}
	
	//转义字符
	public static Map<String, Integer> escapeCharcter(){
		Map<String, Integer> ope = new HashMap<String, Integer>();
		
		ope.put("\\n", 210);
		ope.put("\\t", 211);
		ope.put("\\r", 212);
		
		return ope;
	}
	
	//其他运算符
	public static Map<String, Integer> otherOperators(){
		Map<String, Integer> ope = new HashMap<String, Integer>();
		
		ope.put("?:", 134);
		ope.put("(", 135);
		ope.put(")", 136);
		ope.put("[", 137);
		ope.put("]", 138);
		ope.put(",", 139);
		ope.put(";", 140);
		ope.put("//", 141);
		ope.put("/*", 142);
		ope.put("*/", 143);
		ope.put("{", 144);
		ope.put("}", 145);
		
		return ope;
	}
	
	/*
	 * String[] javaKeyWord =
	 * {"abstract","interface","boolean","long","break","native",
	 * "byte","new","case","null","catch","package","char",
	 * "private","class","protected","continue","public",
	 * "default","return","do","short","double","static",
	 * "else","super","extends","switch","false","final",
	 * "this","finally","throw","float","throws","for",
	 * "if","try","implements","true","import","void",
	 * "instance","volatile","int","while"};
	 */
	public static Map<String, Integer> javaKeyWord() {
		Map<String, Integer> javaWord = new HashMap<String, Integer>();
		javaWord.put("interface", 2);
		javaWord.put("boolean", 3);
//		javaWord.put("long", 4);
//		javaWord.put("break", 5);
		javaWord.put("native", 6);
		javaWord.put("byte", 7);
		javaWord.put("new", 8);
//		javaWord.put("case", 9);
		javaWord.put("null", 10);
		javaWord.put("catch", 11);
		javaWord.put("package", 12);
//		javaWord.put("char", 13);
		javaWord.put("private", 14);
		javaWord.put("class", 15);
		javaWord.put("protected", 16);
//		javaWord.put("continue", 17);
		javaWord.put("public", 18);
//		javaWord.put("default", 19);
//		javaWord.put("return", 20);
//		javaWord.put("do", 21);
//		javaWord.put("short", 22);
//		javaWord.put("double", 23);
//		javaWord.put("static", 24);
//		javaWord.put("else", 25);
		javaWord.put("super", 26);
		javaWord.put("extends", 27);
//		javaWord.put("switch", 28);
		javaWord.put("false", 29);
		javaWord.put("final", 30);
		javaWord.put("finally", 31);
		javaWord.put("throw", 32);
//		javaWord.put("float", 33);
		javaWord.put("throws", 34);
//		javaWord.put("for", 35);
//		javaWord.put("if", 36);
		javaWord.put("try", 37);
		javaWord.put("implements", 38);
		javaWord.put("true", 39);
		javaWord.put("import", 40);
//		javaWord.put("void", 41);
		javaWord.put("instance", 42);
		javaWord.put("volatile", 43);
//		javaWord.put("int", 44);
//		javaWord.put("while", 45);
		javaWord.put("abstract", 46);

		return javaWord;
	}

	public static Map<String, Integer> javaOtherWord(){
		Map<String, Integer> javaWord = new HashMap<String, Integer>();
		
		javaWord.put("instanceof", 47);//只用于对象引用变量。操作检查对象是否为特定类型（类类型或接口类型）
		javaWord.put(".", 48);
		
		return javaWord;
	}
	
	//java封装器类
	public static Map<String, Integer> wrapperClass(){
		Map<String, Integer> ope = new HashMap<String, Integer>();
		
		ope.put("Boolean", 200);
		ope.put("Byte", 201);
		ope.put("Character", 202);
		ope.put("Short", 203);
		ope.put("Integer", 204);
		ope.put("Long", 205);
		ope.put("Float", 206);
		ope.put("Double", 207);
		ope.put("Void", 208);
		
		return ope;
	}
	
	
	
	//公共的关键字
	public static Map<String, Integer> keyWord(){
		Map<String, Integer> ope = new HashMap<String, Integer>();

		ope.put("double", 150);
		ope.put("enum", 151);
		ope.put("float", 152);
		ope.put("int", 153);
		ope.put("long", 154);
		ope.put("short", 155);
		ope.put("void", 156);
		ope.put("for", 157);
		ope.put("do", 158);
		ope.put("while", 159);
		ope.put("break", 160);
		ope.put("continue", 161);
		ope.put("if", 162);
		ope.put("else", 163);
		ope.put("goto", 164);
		ope.put("switch", 165);
		ope.put("case", 166);
		ope.put("default", 167);
		ope.put("return", 168);
		ope.put("static", 169);
		ope.put("const", 170);
		ope.put("volatile", 171);
		ope.put("char", 172);
		
		return ope;
	}
	
	public static Map<String, Integer> cKeyWord() {
		Map<String, Integer> cWord = new HashMap<String, Integer>();
		/*cWord.put("double", 2);
		cWord.put("enum", 3);
		cWord.put("float", 4);
		cWord.put("int", 5);
		cWord.put("long", 6);
		cWord.put("short", 7);*/
		cWord.put("signed", 8);
		cWord.put("struct", 9);
		cWord.put("union", 10);
		cWord.put("unsigned", 11);
		cWord.put("main", 12);
		/*cWord.put("void", 12);
		cWord.put("for", 13);
		cWord.put("do", 14);
		cWord.put("while", 15);
		cWord.put("break", 16);
		cWord.put("continue", 17);
		cWord.put("if", 18);
		cWord.put("else", 19);
		cWord.put("goto", 20);
		cWord.put("switch", 21);
		cWord.put("case", 22);
		cWord.put("default", 23);
		cWord.put("return", 24);*/
		cWord.put("auto", 25);
		cWord.put("extern", 26);
		cWord.put("register", 27);
		/*cWord.put("static", 28);
		cWord.put("const", 29);*/
		cWord.put("sizeof", 30);
		cWord.put("typedef", 31);
//		cWord.put("volatile", 32);
		cWord.put("char", 33);
		return cWord;
	}
	
	public static Map<String, Integer> cOtherWord() {
		Map<String, Integer> cWord = new HashMap<String, Integer>();
		/*cWord.put("(", 33);
		cWord.put(")", 34);
		cWord.put("[", 35);
		cWord.put("]", 36);*/
		cWord.put(".", 37);
		cWord.put("->", 38);
		/*cWord.put("!", 39);
		cWord.put("~", 40);
		cWord.put("-", 41);
		cWord.put("++", 42);
		cWord.put("--", 43);
		cWord.put("&", 44);
		cWord.put("*", 45);
		cWord.put("?:", 46);
		cWord.put("/", 47);
		cWord.put("%", 48);
		cWord.put("+", 49);
		cWord.put(">>", 50);
		cWord.put("<<", 51);
		cWord.put(">", 52);
		cWord.put(">=", 53);
		cWord.put("=", 54);
		cWord.put(",", 55);
		cWord.put("*=", 56);
		cWord.put("*%", 57);
		cWord.put("/=", 58);
		cWord.put("/%", 59);
		cWord.put("-=", 60);
		cWord.put("-%", 61);
		cWord.put("+=", 62);
		cWord.put("+%", 63);
		cWord.put("+%", 64);
		cWord.put("==", 65);
		cWord.put(";", 66);*/
		return cWord;
	}

	// 对map中的按key排序
	public static Object[] sortMap(Map<String, Integer> keyWord) {
		if(keyWord == null){
			return null;
		}
		Object[] key = keyWord.keySet().toArray();
		Arrays.sort(key);
		return key;
	}

	// 折半查找
	/**
	 * -1:没有找到 other:找到对应的下标
	 * */
	public static int searchHalf(String keyString, String[] index) {
		
		if(index == null || keyString == null)
			return -1;
		int flag = 0;
		int left = 0, right = index.length - 1;
		int middle = 0;
		System.out.println(keyString);
		while (left <= right) {
			middle = (left + right) / 2;
			flag = keyString.compareTo(index[middle]);
			if (flag == 0) {
				return middle;
			} else if (flag > 0) {
				left = middle + 1;
			} else if (flag < 0) {
				right = middle - 1;
			}
		}
		return -1;
	}
	
	/*
	 * Object数组转换为String数组
	 * */
	public static String[] ArrayObject2String(Object[] object){
		LinkedList<Object> list = new LinkedList<Object>();
		
		for(int i = 0; i < object.length; i++){
			list.add(object[i]);
		}
		
		String[] str = new String[list.size()];
		list.toArray(str);
		
		return str;
		
	}

}
