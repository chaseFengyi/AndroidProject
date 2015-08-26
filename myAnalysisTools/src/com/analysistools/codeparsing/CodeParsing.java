package com.analysistools.codeparsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.analysistools.bean.KeyWord;

public class CodeParsing {

	/**
	 * 变量：0 数字：1
	 * */

	// 关键字
	public static List<Map<String, String>> keyWordList = new ArrayList<Map<String, String>>();
	// 标示符
	public static List<Map<String, String>> variableList = new ArrayList<Map<String, String>>();
	// 数字
	public static List<Map<String, String>> numberList = new ArrayList<Map<String, String>>();
	// 其他字符
	public static List<Map<String, String>> otherWordList = new ArrayList<Map<String, String>>();
	public static String KEYWORD = "关键字";
	public static String VARABLE = "标识符";
	public static String NUMBER = "数字";
	public static String OTHER = "其他字符";

	private static String store = "";

	
	/**
	 * @param codeString
	 * @param language
	 * @param context
	 * @return 1:正确 -1：出现错误 0:要识别的串为空串
	 */
	public static int javaCodeParsing(String codeString, String language, Context context) {
		int CORRECT = 1;
		int ERROR = -1;
		int EMPTYSTR = 0;
		keyWordList.clear();
		variableList.clear();
		numberList.clear();
		otherWordList.clear();

		Map<String, String> mapKey = new HashMap<String, String>();
		mapKey.put(KEYWORD, null);
		keyWordList.add(mapKey);
		mapKey = null;

		Map<String, String> mapVar = new HashMap<String, String>();
		mapVar.put(VARABLE, null);
		variableList.add(mapVar);
		mapVar = null;

		Map<String, String> mapNum = new HashMap<String, String>();
		mapNum.put(NUMBER, null);
		numberList.add(mapNum);
		mapNum = null;

		Map<String, String> mapOther = new HashMap<String, String>();
		mapOther.put(OTHER, null);
		otherWordList.add(mapOther);
		mapOther = null;

		Map<String, Integer> arithmeticOpe = KeyWord.arithmeticOperators();
		Map<String, Integer> relationOpe = KeyWord.relationalOperators();
		Map<String, Integer> anOpe = KeyWord.anOperators();
		Map<String, Integer> logicalOpe = KeyWord.logicalOperators();
		Map<String, Integer> assigmentOpe = KeyWord.assignmentOperators();
		Map<String, Integer> publicOtherOpe = KeyWord.otherOperators();
		Map<String, Integer> publicKeyOpe = KeyWord.keyWord();
		Map<String, Integer> keyOpe = null;
		Map<String, Integer> otherOpe = null;
		Map<String, Integer> escapeCharacterOpe = KeyWord.escapeCharcter();

		if (language.equals("c代码")) {
			keyOpe = KeyWord.cKeyWord();
			otherOpe = KeyWord.cOtherWord();
		} else if (language.equals("c++代码")) {

		} else if (language.equals("java代码")) {
			keyOpe = KeyWord.javaKeyWord();
			otherOpe = KeyWord.javaOtherWord();
		}

		String[] arithmetic = KeyWord.ArrayObject2String(KeyWord
				.sortMap(arithmeticOpe));
		String[] relation = KeyWord.ArrayObject2String(KeyWord
				.sortMap(relationOpe));
		String[] an = KeyWord.ArrayObject2String(KeyWord.sortMap(anOpe));
		String[] logical = KeyWord.ArrayObject2String(KeyWord
				.sortMap(logicalOpe));
		String[] assigment = KeyWord.ArrayObject2String(KeyWord
				.sortMap(assigmentOpe));
		String[] publicOther = KeyWord.ArrayObject2String(KeyWord
				.sortMap(publicOtherOpe));
		String[] publicKey = KeyWord.ArrayObject2String(KeyWord
				.sortMap(publicKeyOpe));
		String[] key = KeyWord.ArrayObject2String(KeyWord.sortMap(keyOpe));
		String[] other = KeyWord.ArrayObject2String(KeyWord.sortMap(otherOpe));
		String[] escapeCharacter = KeyWord.ArrayObject2String(KeyWord
				.sortMap(escapeCharacterOpe));

		int flag = -1;
		if (codeString == null) {
			return EMPTYSTR;
		}
		int i = 0;
		int sum = 0;
		/**
		 * 其它标示符： letter(letter|digit)* 整形常数: digit digit*
		 * */
		while (i < codeString.length()) {
			store = "";
			// 空格
			while ((i < codeString.length())
					&& (codeString.charAt(i) == ' ')) {
				i++;
			}
			Map<String, String> map = new HashMap<String, String>();
			// 标示符或者变量
			if ((i < codeString.length())
					&& (codeString.charAt(i) >= 'a' && codeString.charAt(i) <= 'z')
					|| (codeString.charAt(i) >= 'A' && codeString.charAt(i) <= 'Z')) {
				map.clear();
				System.out.println("33333333333");
				while ((i < codeString.length())
						&& ((codeString.charAt(i) >= 'a' && codeString
								.charAt(i) <= 'z')
								|| (codeString.charAt(i) >= 'A' && codeString
										.charAt(i) <= 'Z') || (codeString
								.charAt(i) >= '0' && codeString.charAt(i) <= '9'))) {
					System.out.println("44444444444");
					store += codeString.charAt(i);
					i++;
					// System.out.println("trus?=" + (i < codeString.length()));
				}
				// System.out.println("store9999=" + store);
				// 判断是否是公共关键字,flag经过排序后的下标
				flag = KeyWord.searchHalf(store, publicKey);
				if (flag != -1) {// 是公共关键字
					map.put(store, publicKeyOpe.get(publicKey[flag]) + "");
					keyWordList.add(map);
					// System.out.println("publicKey="+flag);
				} else {
					flag = KeyWord.searchHalf(store, key);
					if (flag != -1) {// 是关键字
						map.put(store, keyOpe.get(key[flag]) + "");
						keyWordList.add(map);
						// System.out.println("publicKey212222="+flag);
					} else {
						map.put(store, "(变量|常量名) 0");
						variableList.add(map);
						// System.out.println("publicKey444444="+flag);
					}
				}
			} else if ((i < codeString.length())
					&& (codeString.charAt(i) >= '0' && codeString.charAt(i) <= '9')) {
				System.out.println("5555555555555");
				map.clear();
				sum = 0;
				// System.out.println("numberString=" + codeString.charAt(i));
				while ((i < codeString.length())
						&& (codeString.charAt(i) >= '0' && codeString.charAt(i) <= '9')) {
					sum = sum * 10 + codeString.charAt(i) - '0';
					i++;
				}
				map.put(sum + "", "(常数) 1");
				numberList.add(map);
				// System.out.println("<" + sum + ",(常数) 1 >");
			} else if ((i < codeString.length())) {
				System.out.println("6666666666666");
				map.clear();
				// System.out.println("otherString=" + codeString.charAt(i));
				switch (codeString.charAt(i)) {
				case '(':
					flag = KeyWord.searchHalf(codeString.charAt(i) + "",
							arithmetic);
					if (flag != -1) {// 判断是否是算术运算符
						map.put(" ( ", arithmeticOpe.get(arithmetic[flag]) + "");
						otherWordList.add(map);
						// System.out.println("< ( ,"
						// + arithmeticOpe.get(arithmetic[flag]) + " >");
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" ( ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< ( ,"
							// + relationOpe.get(relation[flag]) + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" ( ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								// System.out.println("< ( ,"
								// + anOpe.get(an[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" ( ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< ( ,"
									// + logicalOpe.get(logical[flag])
									// + " >");
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" ( ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< ( ,"
										// + assigmentOpe
										// .get(assigment[flag])
										// + " >");
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" ( ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< ( ,"
											// + publicOtherOpe
											// .get(publicOther[flag])
											// + " >");
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" ( ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< ( ,"
												// + otherOpe
												// .get(other[flag])
												// + " >");
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}

					break;
				case ')':
					flag = KeyWord.searchHalf(codeString.charAt(i) + "",
							arithmetic);
					if (flag != -1) {// 判断是否是算术运算符
						map.put(" ) ", arithmeticOpe.get(arithmetic[flag]) + "");
						otherWordList.add(map);
						// System.out.println("< ) ,"
						// + arithmeticOpe.get(arithmetic[flag]) + " >");
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" ) ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< ) ,"
							// + relationOpe.get(relation[flag]) + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" ) ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								// System.out.println("< ) ,"
								// + anOpe.get(an[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" ) ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< ) ,"
									// + logicalOpe.get(logical[flag])
									// + " >");
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" ) ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< ) ,"
										// + assigmentOpe
										// .get(assigment[flag])
										// + " >");
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" ) ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< ) ,"
											// + publicOtherOpe
											// .get(publicOther[flag])
											// + " >");
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" ) ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< ) ,"
												// + otherOpe
												// .get(other[flag])
												// + " >");
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}
					break;
				case '[':
					flag = KeyWord.searchHalf(codeString.charAt(i) + "",
							arithmetic);
					if (flag != -1) {// 判断是否是算术运算符
						map.put(" [ ", arithmeticOpe.get(arithmetic[flag]) + "");
						otherWordList.add(map);
						// System.out.println("< [ ,"
						// + arithmeticOpe.get(arithmetic[flag]) + " >");
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" [ ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< [ ,"
							// + relationOpe.get(relation[flag]) + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" [ ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								// System.out.println("< [ ,"
								// + anOpe.get(an[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" [ ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< [ ,"
									// + logicalOpe.get(logical[flag])
									// + " >");
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" [ ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< [ ,"
										// + assigmentOpe
										// .get(assigment[flag])
										// + " >");
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" [ ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< [ ,"
											// + publicOtherOpe
											// .get(publicOther[flag])
											// + " >");
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" [ ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< [ ,"
												// + otherOpe
												// .get(other[flag])
												// + " >");
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}
					break;
				case ']':
					flag = KeyWord.searchHalf(codeString.charAt(i) + "",
							arithmetic);
					if (flag != -1) {// 判断是否是算术运算符
						map.put(" ] ", arithmeticOpe.get(arithmetic[flag]) + "");
						otherWordList.add(map);
						// System.out.println("< ] ,"
						// + arithmeticOpe.get(arithmetic[flag]) + " >");
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" ] ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< ] ,"
							// + relationOpe.get(relation[flag]) + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" ] ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								// System.out.println("< ] ,"
								// + anOpe.get(an[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" ] ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< ] ,"
									// + logicalOpe.get(logical[flag])
									// + " >");
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" ] ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< ] ,"
										// + assigmentOpe
										// .get(assigment[flag])
										// + " >");
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" ] ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< ] ,"
											// + publicOtherOpe
											// .get(publicOther[flag])
											// + " >");
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" ] ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< ] ,"
												// + otherOpe
												// .get(other[flag])
												// + " >");
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}
					break;
				case '.':
					flag = KeyWord.searchHalf(codeString.charAt(i) + "",
							arithmetic);
					if (flag != -1) {// 判断是否是算术运算符
						map.put(" . ", arithmeticOpe.get(arithmetic[flag]) + "");
						otherWordList.add(map);
						// System.out.println("< . ,"
						// + arithmeticOpe.get(arithmetic[flag]) + " >");
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" . ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< . ,"
							// + relationOpe.get(relation[flag]) + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" . ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								// System.out.println("< . ,"
								// + anOpe.get(an[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" . ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< . ,"
									// + logicalOpe.get(logical[flag])
									// + " >");
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" . ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< . ,"
										// + assigmentOpe
										// .get(assigment[flag])
										// + " >");
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" . ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< . ,"
											// + publicOtherOpe
											// .get(publicOther[flag])
											// + " >");
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" . ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< . ,"
												// + otherOpe
												// .get(other[flag])
												// + " >");
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}
					break;
				case '!':
					flag = KeyWord.searchHalf(codeString.charAt(i) + "",
							arithmetic);
					if (flag != -1) {// 判断是否是算术运算符
						map.put(" ! ", arithmeticOpe.get(arithmetic[flag]) + "");
						otherWordList.add(map);
						// System.out.println("< ! ,"
						// + arithmeticOpe.get(arithmetic[flag]) + " >");
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" ! ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< ! ,"
							// + relationOpe.get(relation[flag]) + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" ! ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								// System.out.println("< ! ,"
								// + anOpe.get(an[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" ! ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< ! ,"
									// + logicalOpe.get(logical[flag])
									// + " >");
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" ! ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< ! ,"
										// + assigmentOpe
										// .get(assigment[flag])
										// + " >");
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" ! ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< ! ,"
											// + publicOtherOpe
											// .get(publicOther[flag])
											// + " >");
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" ! ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< ! ,"
												// + otherOpe
												// .get(other[flag])
												// + " >");
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}
					break;
				case '~':
					flag = KeyWord.searchHalf(codeString.charAt(i) + "",
							arithmetic);
					if (flag != -1) {// 判断是否是算术运算符
						map.put(" ~ ", arithmeticOpe.get(arithmetic[flag]) + "");
						otherWordList.add(map);
						// System.out.println("< ~ ,"
						// + arithmeticOpe.get(arithmetic[flag]) + " >");
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" ~ ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< ~ ,"
							// + relationOpe.get(relation[flag]) + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" ~ ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								// System.out.println("< ~ ,"
								// + anOpe.get(an[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" ~ ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< ~ ,"
									// + logicalOpe.get(logical[flag])
									// + " >");
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" ~ ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< ~ ,"
										// + assigmentOpe
										// .get(assigment[flag])
										// + " >");
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" ~ ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< ~ ,"
											// + publicOtherOpe
											// .get(publicOther[flag])
											// + " >");
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" ~ ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< ~ ,"
												// + otherOpe
												// .get(other[flag])
												// + " >");
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}
					break;
				case '-':
					if (codeString.charAt(i + 1) == '>') {
						flag = KeyWord.searchHalf("->", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" -> ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							// System.out.println("< -> ,"
							// + arithmeticOpe.get(arithmetic[flag])
							// + " >");
							i++;
						} else {
							flag = KeyWord.searchHalf("->", relation);
							if (flag != -1) {// 关系运算符
								map.put(" -> ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								// System.out.println("< -> ,"
								// + relationOpe.get(relation[flag])
								// + " >");
								i++;
							} else {
								flag = KeyWord.searchHalf("->", an);
								if (flag != -1) {// 位运算符
									map.put(" -> ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< -> ,"
									// + anOpe.get(an[flag]) + " >");
									i++;
								} else {
									flag = KeyWord.searchHalf("->", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" -> ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< -> ,"
										// + logicalOpe.get(logical[flag])
										// + " >");
										i++;
									} else {
										flag = KeyWord.searchHalf("->",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" -> ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< -> ,"
											// + assigmentOpe
											// .get(assigment[flag])
											// + " >");
											i++;
										} else {
											flag = KeyWord.searchHalf("->",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" -> ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< -> ,"
												// + publicOtherOpe
												// .get(publicOther[flag])
												// + " >");
												i++;
											} else {
												flag = KeyWord.searchHalf("->",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" -> ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													// System.out
													// .println("< -> ,"
													// + otherOpe
													// .get(other[flag])
													// + " >");
													i++;
												} else {
													Log.i("notice",
															"没有找到\"->\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else if (codeString.charAt(i + 1) == '-') {
						flag = KeyWord.searchHalf("--", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" -- ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							// System.out.println("< -- ,"
							// + arithmeticOpe.get(arithmetic[flag])
							// + " >");
							i++;
						} else {
							flag = KeyWord.searchHalf("--", relation);
							if (flag != -1) {// 关系运算符
								map.put(" -- ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								// System.out.println("< -- ,"
								// + relationOpe.get(relation[flag])
								// + " >");
								i++;
							} else {
								flag = KeyWord.searchHalf("--", an);
								if (flag != -1) {// 位运算符
									map.put(" -- ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< -- ,"
									// + anOpe.get(an[flag]) + " >");
									// i++;
								} else {
									flag = KeyWord.searchHalf("--", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" -- ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< -- ,"
										// + logicalOpe.get(logical[flag])
										// + " >");
										i++;
									} else {
										flag = KeyWord.searchHalf("--",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" -- ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< -- ,"
											// + assigmentOpe
											// .get(assigment[flag])
											// + " >");
											i++;
										} else {
											flag = KeyWord.searchHalf("--",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" -- ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< -- ,"
												// + publicOtherOpe
												// .get(publicOther[flag])
												// + " >");
												i++;
											} else {
												flag = KeyWord.searchHalf("--",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" -- ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													// System.out
													// .println("< -- ,"
													// + otherOpe
													// .get(other[flag])
													// + " >");
													i++;
												} else {
													Log.i("notice",
															"没有找到\"--\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else if (codeString.charAt(i + 1) == '=') {
						flag = KeyWord.searchHalf("-=", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" -= ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							// System.out.println("< -= ,"
							// + arithmeticOpe.get(arithmetic[flag])
							// + " >");
							i++;
						} else {
							flag = KeyWord.searchHalf("-=", relation);
							if (flag != -1) {// 关系运算符
								map.put(" -= ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								// System.out.println("< -= ,"
								// + relationOpe.get(relation[flag])
								// + " >");
								i++;
							} else {
								flag = KeyWord.searchHalf("-=", an);
								if (flag != -1) {// 位运算符
									map.put(" -= ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< -= ,"
									// + anOpe.get(an[flag]) + " >");
									i++;
								} else {
									flag = KeyWord.searchHalf("-=", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" -= ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< -= ,"
										// + logicalOpe.get(logical[flag])
										// + " >");
										i++;
									} else {
										flag = KeyWord.searchHalf("-=",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" -= ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< -= ,"
											// + assigmentOpe
											// .get(assigment[flag])
											// + " >");
											i++;
										} else {
											flag = KeyWord.searchHalf("-=",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" -= ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< -= ,"
												// + publicOtherOpe
												// .get(publicOther[flag])
												// + " >");
												i++;
											} else {
												flag = KeyWord.searchHalf("-=",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" -= ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													// System.out
													// .println("< -= ,"
													// + otherOpe
													// .get(other[flag])
													// + " >");
													i++;
												} else {
													Log.i("notice",
															"没有找到\"-=\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" - ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							// System.out.println("< - ,"
							// + arithmeticOpe.get(arithmetic[flag])
							// + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", relation);
							if (flag != -1) {// 关系运算符
								map.put(" - ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								// System.out.println("< - ,"
								// + relationOpe.get(relation[flag])
								// + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", an);
								if (flag != -1) {// 位运算符
									map.put(" - ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< - ,"
									// + anOpe.get(an[flag]) + " >");
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" - ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< - ,"
										// + logicalOpe.get(logical[flag])
										// + " >");
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" - ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< - ,"
											// + assigmentOpe
											// .get(assigment[flag])
											// + " >");
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" - ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< - ,"
												// + publicOtherOpe
												// .get(publicOther[flag])
												// + " >");
											} else {
												flag = KeyWord.searchHalf(
														codeString.charAt(i)
																+ "", other);
												if (flag != -1) {// 其他运算符
													map.put(" - ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													// System.out
													// .println("< - ,"
													// + otherOpe
													// .get(other[flag])
													// + " >");
												} else {
													Log.i("notice",
															"没有找到\""
																	+ codeString
																			.charAt(i)
																	+ "\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					}
					break;
				case '+':
					if (codeString.charAt(i + 1) == '+') {
						flag = KeyWord.searchHalf("++", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" ++ ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							// System.out.println("< ++ ,"
							// + arithmeticOpe.get(arithmetic[flag])
							// + " >");
							i++;
						} else {
							flag = KeyWord.searchHalf("++", relation);
							if (flag != -1) {// 关系运算符
								map.put(" ++ ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								// System.out.println("< ++ ,"
								// + relationOpe.get(relation[flag])
								// + " >");
								i++;
							} else {
								flag = KeyWord.searchHalf("++", an);
								if (flag != -1) {// 位运算符
									map.put(" ++ ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< ++ ,"
									// + anOpe.get(an[flag]) + " >");
									i++;
								} else {
									flag = KeyWord.searchHalf("++", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" ++ ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< ++ ,"
										// + logicalOpe.get(logical[flag])
										// + " >");
										i++;
									} else {
										flag = KeyWord.searchHalf("++",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" ++ ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< ++ ,"
											// + assigmentOpe
											// .get(assigment[flag])
											// + " >");
											i++;
										} else {
											flag = KeyWord.searchHalf("++",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" ++ ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< ++ ,"
												// + publicOtherOpe
												// .get(publicOther[flag])
												// + " >");
												i++;
											} else {
												flag = KeyWord.searchHalf("++",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" ++ ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													// System.out
													// .println("< ++ ,"
													// + otherOpe
													// .get(other[flag])
													// + " >");
													i++;
												} else {
													Log.i("notice",
															"没有找到\"++\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else if (codeString.charAt(i + 1) == '=') {
						flag = KeyWord.searchHalf("+=", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" += ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							// System.out.println("< += ,"
							// + arithmeticOpe.get(arithmetic[flag])
							// + " >");
							i++;
						} else {
							flag = KeyWord.searchHalf("+=", relation);
							if (flag != -1) {// 关系运算符
								map.put(" += ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								// System.out.println("< += ,"
								// + relationOpe.get(relation[flag])
								// + " >");
								i++;
							} else {
								flag = KeyWord.searchHalf("+=", an);
								if (flag != -1) {// 位运算符
									map.put(" += ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< += ,"
									// + anOpe.get(an[flag]) + " >");
									i++;
								} else {
									flag = KeyWord.searchHalf("+=", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" += ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< += ,"
										// + logicalOpe.get(logical[flag])
										// + " >");
										i++;
									} else {
										flag = KeyWord.searchHalf("+=",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" += ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< += ,"
											// + assigmentOpe
											// .get(assigment[flag])
											// + " >");
											i++;
										} else {
											flag = KeyWord.searchHalf("+=",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" += ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< += ,"
												// + publicOtherOpe
												// .get(publicOther[flag])
												// + " >");
												i++;
											} else {
												flag = KeyWord.searchHalf("+=",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" += ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													// System.out
													// .println("< += ,"
													// + otherOpe
													// .get(other[flag])
													// + " >");
													i++;
												} else {
													Log.i("notice",
															"没有找到\"+=\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" + ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							// System.out.println("< + ,"
							// + arithmeticOpe.get(arithmetic[flag])
							// + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", relation);
							if (flag != -1) {// 关系运算符
								map.put(" + ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								// System.out.println("< + ,"
								// + relationOpe.get(relation[flag])
								// + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", an);
								if (flag != -1) {// 位运算符
									map.put(" + ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< + ,"
									// + anOpe.get(an[flag]) + " >");
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" + ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< + ,"
										// + logicalOpe.get(logical[flag])
										// + " >");
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" + ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< + ,"
											// + assigmentOpe
											// .get(assigment[flag])
											// + " >");
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" + ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< + ,"
												// + publicOtherOpe
												// .get(publicOther[flag])
												// + " >");
											} else {
												flag = KeyWord.searchHalf(
														codeString.charAt(i)
																+ "", other);
												if (flag != -1) {// 其他运算符
													map.put(" + ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													// System.out
													// .println("< + ,"
													// + otherOpe
													// .get(other[flag])
													// + " >");
												} else {
													Log.i("notice",
															"没有找到\""
																	+ codeString
																			.charAt(i)
																	+ "\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					}
					break;
				case '&':
					if (codeString.charAt(i + 1) == '&') {
						flag = KeyWord.searchHalf("&&", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" && ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							// System.out.println("< && ,"
							// + arithmeticOpe.get(arithmetic[flag])
							// + " >");
							i++;
						} else {
							flag = KeyWord.searchHalf("&&", relation);
							if (flag != -1) {// 关系运算符
								map.put(" && ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								// System.out.println("< && ,"
								// + relationOpe.get(relation[flag])
								// + " >");
								i++;
							} else {
								flag = KeyWord.searchHalf("&&", an);
								if (flag != -1) {// 位运算符
									map.put(" && ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< && ,"
									// + anOpe.get(an[flag]) + " >");
									i++;
								} else {
									flag = KeyWord.searchHalf("&&", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" && ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< && ,"
										// + logicalOpe.get(logical[flag])
										// + " >");
										i++;
									} else {
										flag = KeyWord.searchHalf("&&",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" && ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											// System.out
											// .println("< && ,"
											// + assigmentOpe
											// .get(assigment[flag])
											// + " >");
											i++;
										} else {
											flag = KeyWord.searchHalf("&&",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" && ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												// System.out
												// .println("< && ,"
												// + publicOtherOpe
												// .get(publicOther[flag])
												// + " >");
												i++;
											} else {
												flag = KeyWord.searchHalf("&&",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" && ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													// System.out
													// .println("< && ,"
													// + otherOpe
													// .get(other[flag])
													// + " >");
													i++;
												} else {
													Log.i("notice",
															"没有找到\"&&\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" & ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< & ,"
							// + relationOpe.get(relation[flag]) + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" & ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								// System.out.println("< & ,"
								// + anOpe.get(an[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" & ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< & ,"
									// + logicalOpe.get(logical[flag])
									// + " >");
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" & ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< & ,"
										// + assigmentOpe
										// .get(assigment[flag])
										// + " >");
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" & ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< & ," +
											 * publicOtherOpe
											 * .get(publicOther[flag]) + " >");
											 */
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" & ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< & ," +
												 * otherOpe .get(other[flag]) +
												 * " >");
												 */
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}
					break;
				case '*':
					if (codeString.charAt(i + 1) == '=') {
						flag = KeyWord.searchHalf("*=", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" *= ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							/*
							 * System.out.println("< *= ," +
							 * arithmeticOpe.get(arithmetic[flag]) + " >");
							 */
							i++;
						} else {
							flag = KeyWord.searchHalf("*=", relation);
							if (flag != -1) {// 关系运算符
								map.put(" *= ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								/*
								 * System.out.println("< *= ," +
								 * relationOpe.get(relation[flag]) + " >");
								 */
								i++;
							} else {
								flag = KeyWord.searchHalf("*=", an);
								if (flag != -1) {// 位运算符
									map.put(" *= ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									/*
									 * System.out.println("< *= ," +
									 * anOpe.get(an[flag]) + " >");
									 */
									i++;
								} else {
									flag = KeyWord.searchHalf("*=", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" *= ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< *= ," +
										 * logicalOpe.get(logical[flag]) +
										 * " >");
										 */
										i++;
									} else {
										flag = KeyWord.searchHalf("*=",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" *= ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< *= ," +
											 * assigmentOpe
											 * .get(assigment[flag]) + " >");
											 */
											i++;
										} else {
											flag = KeyWord.searchHalf("*=",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" *= ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< *= ,"
												 * + publicOtherOpe
												 * .get(publicOther[flag]) +
												 * " >");
												 */
												i++;
											} else {
												flag = KeyWord.searchHalf("*=",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" *= ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													/*
													 * System.out
													 * .println("< *= ," +
													 * otherOpe
													 * .get(other[flag]) +
													 * " >");
													 */
													i++;
												} else {
													Log.i("notice",
															"没有找到\"*=\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" * ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							/*
							 * System.out.println("< * ," +
							 * relationOpe.get(relation[flag]) + " >");
							 */
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" * ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								/*
								 * System.out.println("< * ," +
								 * anOpe.get(an[flag]) + " >");
								 */
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" * ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									/*
									 * System.out.println("< * ," +
									 * logicalOpe.get(logical[flag]) + " >");
									 */
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" * ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< * ," +
										 * assigmentOpe .get(assigment[flag]) +
										 * " >");
										 */
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" * ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< * ," +
											 * publicOtherOpe
											 * .get(publicOther[flag]) + " >");
											 */
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" * ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< * ," +
												 * otherOpe .get(other[flag]) +
												 * " >");
												 */
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}
					break;
				case '?':
					if (codeString.charAt(i + 1) == ':') {
						flag = KeyWord.searchHalf("?:", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" ?: ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							/*
							 * System.out.println("< ?: ," +
							 * arithmeticOpe.get(arithmetic[flag]) + " >");
							 */
							i++;
						} else {
							flag = KeyWord.searchHalf("?:", relation);
							if (flag != -1) {// 关系运算符
								map.put(" ?: ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								/*
								 * System.out.println("< ?: ," +
								 * relationOpe.get(relation[flag]) + " >");
								 */
								i++;
							} else {
								flag = KeyWord.searchHalf("?:", an);
								if (flag != -1) {// 位运算符
									map.put(" ?: ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									/*
									 * System.out.println("< ?: ," +
									 * anOpe.get(an[flag]) + " >");
									 */
									i++;
								} else {
									flag = KeyWord.searchHalf("?:", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" ?: ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< ?: ," +
										 * logicalOpe.get(logical[flag]) +
										 * " >");
										 */
										i++;
									} else {
										flag = KeyWord.searchHalf("?:",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" ?: ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< ?: ," +
											 * assigmentOpe
											 * .get(assigment[flag]) + " >");
											 */
											i++;
										} else {
											flag = KeyWord.searchHalf("?:",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" ?: ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< ?: ,"
												 * + publicOtherOpe
												 * .get(publicOther[flag]) +
												 * " >");
												 */
												i++;
											} else {
												flag = KeyWord.searchHalf("?:",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" ?: ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													/*
													 * System.out
													 * .println("< ?: ," +
													 * otherOpe
													 * .get(other[flag]) +
													 * " >");
													 */
													i++;
												} else {
													Log.i("notice",
															"没有找到\"?:\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else {
						map.put("error" + i, codeString.charAt(i) + "是一个错误的标示符");
						otherWordList.add(map);
						// System.out.println(codeString.charAt(i) +
						// "是一个错误的标示符");
					}
					break;
				case '/':
					if (codeString.charAt(i + 1) == '=') {
						flag = KeyWord.searchHalf("/=", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" /= ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							/*
							 * System.out.println("< /= ," +
							 * arithmeticOpe.get(arithmetic[flag]) + " >");
							 */
							i++;
						} else {
							flag = KeyWord.searchHalf("/=", relation);
							if (flag != -1) {// 关系运算符
								map.put(" /= ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								/*
								 * System.out.println("< /= ," +
								 * relationOpe.get(relation[flag]) + " >");
								 */
								i++;
							} else {
								flag = KeyWord.searchHalf("/=", an);
								if (flag != -1) {// 位运算符
									map.put(" /= ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									/*
									 * System.out.println("< /= ," +
									 * anOpe.get(an[flag]) + " >");
									 */
									i++;
								} else {
									flag = KeyWord.searchHalf("/=", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" /= ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< /= ," +
										 * logicalOpe.get(logical[flag]) +
										 * " >");
										 */
										i++;
									} else {
										flag = KeyWord.searchHalf("/=",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" /= ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< /= ," +
											 * assigmentOpe
											 * .get(assigment[flag]) + " >");
											 */
											i++;
										} else {
											flag = KeyWord.searchHalf("/=",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" /= ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< /= ,"
												 * + publicOtherOpe
												 * .get(publicOther[flag]) +
												 * " >");
												 */
												i++;
											} else {
												flag = KeyWord.searchHalf("/=",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" /= ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													/*
													 * System.out
													 * .println("< /= ," +
													 * otherOpe
													 * .get(other[flag]) +
													 * " >");
													 */
													i++;
												} else {
													Log.i("notice",
															"没有找到\"/=\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" / ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							/*
							 * System.out.println("< / ," +
							 * relationOpe.get(relation[flag]) + " >");
							 */
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" / ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								/*
								 * System.out.println("< / ," +
								 * anOpe.get(an[flag]) + " >");
								 */
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" / ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									/*
									 * System.out.println("< / ," +
									 * logicalOpe.get(logical[flag]) + " >");
									 */} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" / ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< / ," +
										 * assigmentOpe .get(assigment[flag]) +
										 * " >");
										 */
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" / ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< / ," +
											 * publicOtherOpe
											 * .get(publicOther[flag]) + " >");
											 */
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" / ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< / ," +
												 * otherOpe .get(other[flag]) +
												 * " >");
												 */
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}
					break;
				case '%':
					flag = KeyWord.searchHalf(codeString.charAt(i) + "",
							arithmetic);
					if (flag != -1) {// 关系运算符
						map.put(" % ", relationOpe.get(relation[flag]) + "");
						otherWordList.add(map);
						System.out.println("< % ,"
								+ relationOpe.get(relation[flag]) + " >");
					} else {
						flag = KeyWord
								.searchHalf(codeString.charAt(i) + "", an);
						if (flag != -1) {// 位运算符
							map.put(" % ", anOpe.get(an[flag]) + "");
							otherWordList.add(map);
							/*
							 * System.out.println("< % ," + anOpe.get(an[flag])
							 * + " >");
							 */
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", logical);
							if (flag != -1) {// 逻辑运算符
								map.put(" % ", logicalOpe.get(logical[flag])
										+ "");
								otherWordList.add(map);
								// System.out.println("< % ,"
								// + logicalOpe.get(logical[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", assigment);
								if (flag != -1) {// 赋值运算符
									map.put(" % ",
											assigmentOpe.get(assigment[flag])
													+ "");
									otherWordList.add(map);
									// System.out.println("< % ,"
									// + assigmentOpe.get(assigment[flag])
									// + " >");
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											publicOther);
									if (flag != -1) {// 其他运算符
										map.put(" % ",
												publicOtherOpe
														.get(publicOther[flag])
														+ "");
										otherWordList.add(map);
										// System.out.println("< % ,"
										// + publicOtherOpe
										// .get(publicOther[flag])
										// + " >");
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												other);
										if (flag != -1) {// 其他运算符
											map.put(" % ",
													otherOpe.get(other[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out.println("< % ," +
											 * otherOpe.get(other[flag]) +
											 * " >");
											 */
										} else {
											Log.i("notice", "没有找到\""
													+ codeString.charAt(i)
													+ "\"对应的值");
										}
									}
								}
							}
						}
					}
					break;
				case '>':
					if (codeString.charAt(i + 1) == '>') {
						flag = KeyWord.searchHalf(">>", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" >> ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							/*
							 * System.out.println("< >> ," +
							 * arithmeticOpe.get(arithmetic[flag]) + " >");
							 */
							i++;
						} else {
							flag = KeyWord.searchHalf(">>", relation);
							if (flag != -1) {// 关系运算符
								map.put(" >> ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								/*
								 * System.out.println("< >> ," +
								 * relationOpe.get(relation[flag]) + " >");
								 */
								i++;
							} else {
								flag = KeyWord.searchHalf(">>", an);
								if (flag != -1) {// 位运算符
									map.put(" >> ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									/*
									 * System.out.println("< >> ," +
									 * anOpe.get(an[flag]) + " >");
									 */
									i++;
								} else {
									flag = KeyWord.searchHalf(">>", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" >> ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< >> ," +
										 * logicalOpe.get(logical[flag]) +
										 * " >");
										 */
										i++;
									} else {
										flag = KeyWord.searchHalf(">>",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" >> ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< >> ," +
											 * assigmentOpe
											 * .get(assigment[flag]) + " >");
											 */
											i++;
										} else {
											flag = KeyWord.searchHalf(">>",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" >> ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< >> ,"
												 * + publicOtherOpe
												 * .get(publicOther[flag]) +
												 * " >");
												 */
												i++;
											} else {
												flag = KeyWord.searchHalf(">>",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" >> ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													/*
													 * System.out
													 * .println("< >> ," +
													 * otherOpe
													 * .get(other[flag]) +
													 * " >");
													 */
													i++;
												} else {
													Log.i("notice",
															"没有找到\">>\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else if (codeString.charAt(i + 1) == '=') {
						flag = KeyWord.searchHalf(">=", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" >= ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							/*
							 * System.out.println("< >= ," +
							 * arithmeticOpe.get(arithmetic[flag]) + " >");
							 */
							i++;
						} else {
							flag = KeyWord.searchHalf(">=", relation);
							if (flag != -1) {// 关系运算符
								map.put(" >= ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								/*
								 * System.out.println("< >= ," +
								 * relationOpe.get(relation[flag]) + " >");
								 */
								i++;
							} else {
								flag = KeyWord.searchHalf(">=", an);
								if (flag != -1) {// 位运算符
									map.put(" >= ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									/*
									 * System.out.println("< >= ," +
									 * anOpe.get(an[flag]) + " >");
									 */
									i++;
								} else {
									flag = KeyWord.searchHalf(">=", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" >= ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< >= ," +
										 * logicalOpe.get(logical[flag]) +
										 * " >");
										 */
										i++;
									} else {
										flag = KeyWord.searchHalf(">=",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" >= ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< >= ," +
											 * assigmentOpe
											 * .get(assigment[flag]) + " >");
											 */
											i++;
										} else {
											flag = KeyWord.searchHalf(">=",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" >= ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< >= ,"
												 * + publicOtherOpe
												 * .get(publicOther[flag]) +
												 * " >");
												 */
												i++;
											} else {
												flag = KeyWord.searchHalf(">=",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" >= ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													/*
													 * System.out
													 * .println("< >= ," +
													 * otherOpe
													 * .get(other[flag]) +
													 * " >");
													 */
													i++;
												} else {
													Log.i("notice",
															"没有找到\">=\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" > ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< > ,"
							// + relationOpe.get(relation[flag]) + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" > ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								// System.out.println("< > ,"
								// + anOpe.get(an[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" > ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									/*
									 * System.out.println("< > ," +
									 * logicalOpe.get(logical[flag]) + " >");
									 */
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" > ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< > ," +
										 * assigmentOpe .get(assigment[flag]) +
										 * " >");
										 */
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" > ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< > ," +
											 * publicOtherOpe
											 * .get(publicOther[flag]) + " >");
											 */
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" > ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< > ," +
												 * otherOpe .get(other[flag]) +
												 * " >");
												 */
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}

					break;
				case '<':
					if (codeString.charAt(i + 1) == '<') {
						flag = KeyWord.searchHalf("<<", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" << ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							/*
							 * System.out.println("< << ," +
							 * arithmeticOpe.get(arithmetic[flag]) + " >");
							 */
							i++;
						} else {
							flag = KeyWord.searchHalf("<<", relation);
							if (flag != -1) {// 关系运算符
								map.put(" << ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								/*
								 * System.out.println("< << ," +
								 * relationOpe.get(relation[flag]) + " >");
								 */
								i++;
							} else {
								flag = KeyWord.searchHalf("<<", an);
								if (flag != -1) {// 位运算符
									map.put(" << ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									/*
									 * System.out.println("< << ," +
									 * anOpe.get(an[flag]) + " >");
									 */
									i++;
								} else {
									flag = KeyWord.searchHalf("<<", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" << ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< << ," +
										 * logicalOpe.get(logical[flag]) +
										 * " >");
										 */
										i++;
									} else {
										flag = KeyWord.searchHalf("<<",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" << ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< << ," +
											 * assigmentOpe
											 * .get(assigment[flag]) + " >");
											 */
											i++;
										} else {
											flag = KeyWord.searchHalf("<<",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" << ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< << ,"
												 * + publicOtherOpe
												 * .get(publicOther[flag]) +
												 * " >");
												 */
												i++;
											} else {
												flag = KeyWord.searchHalf("<<",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" << ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													/*
													 * System.out
													 * .println("< << ," +
													 * otherOpe
													 * .get(other[flag]) +
													 * " >");
													 */
													i++;
												} else {
													Log.i("notice",
															"没有找到\"<<\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else if (codeString.charAt(i + 1) == '=') {
						flag = KeyWord.searchHalf("<=", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" <= ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							/*
							 * System.out.println("< <= ," +
							 * arithmeticOpe.get(arithmetic[flag]) + " >");
							 */
							i++;
						} else {
							flag = KeyWord.searchHalf("<=", relation);
							if (flag != -1) {// 关系运算符
								map.put(" <= ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								/*
								 * System.out.println("< <= ," +
								 * relationOpe.get(relation[flag]) + " >");
								 */
								i++;
							} else {
								flag = KeyWord.searchHalf("<=", an);
								if (flag != -1) {// 位运算符
									map.put(" <= ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									// System.out.println("< <= ,"
									// + anOpe.get(an[flag]) + " >");
									i++;
								} else {
									flag = KeyWord.searchHalf("<=", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" <= ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< <= ," +
										 * logicalOpe.get(logical[flag]) +
										 * " >");
										 */
										i++;
									} else {
										flag = KeyWord.searchHalf("<=",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" <= ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< <= ," +
											 * assigmentOpe
											 * .get(assigment[flag]) + " >");
											 */
											i++;
										} else {
											flag = KeyWord.searchHalf("<=",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" <= ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< <= ,"
												 * + publicOtherOpe
												 * .get(publicOther[flag]) +
												 * " >");
												 */
												i++;
											} else {
												flag = KeyWord.searchHalf("<=",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" <= ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													/*
													 * System.out
													 * .println("< <= ," +
													 * otherOpe
													 * .get(other[flag]) +
													 * " >");
													 */
													i++;
												} else {
													Log.i("notice",
															"没有找到\"<=\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" < ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< < ,"
							// + relationOpe.get(relation[flag]) + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" < ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								// System.out.println("< < ,"
								// + anOpe.get(an[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" < ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									/*
									 * System.out.println("< < ," +
									 * logicalOpe.get(logical[flag]) + " >");
									 */
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" < ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< < ," +
										 * assigmentOpe .get(assigment[flag]) +
										 * " >");
										 */
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" < ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< < ," +
											 * publicOtherOpe
											 * .get(publicOther[flag]) + " >");
											 */
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" < ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< < ," +
												 * otherOpe .get(other[flag]) +
												 * " >");
												 */
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}

					break;
				case '=':
					if (codeString.charAt(i + 1) == '=') {
						flag = KeyWord.searchHalf("==", arithmetic);
						if (flag != -1) {// 判断是否是算术运算符
							map.put(" == ", arithmeticOpe.get(arithmetic[flag])
									+ "");
							otherWordList.add(map);
							/*
							 * System.out.println("< == ," +
							 * arithmeticOpe.get(arithmetic[flag]) + " >");
							 */
							i++;
						} else {
							flag = KeyWord.searchHalf("==", relation);
							if (flag != -1) {// 关系运算符
								map.put(" == ", relationOpe.get(relation[flag])
										+ "");
								otherWordList.add(map);
								/*
								 * System.out.println("< == ," +
								 * relationOpe.get(relation[flag]) + " >");
								 */
								i++;
							} else {
								flag = KeyWord.searchHalf("==", an);
								if (flag != -1) {// 位运算符
									map.put(" == ", anOpe.get(an[flag]) + "");
									otherWordList.add(map);
									/*
									 * System.out.println("< == ," +
									 * anOpe.get(an[flag]) + " >");
									 */
									i++;
								} else {
									flag = KeyWord.searchHalf("==", logical);
									if (flag != -1) {// 逻辑运算符
										map.put(" == ",
												logicalOpe.get(logical[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< == ," +
										 * logicalOpe.get(logical[flag]) +
										 * " >");
										 */
										i++;
									} else {
										flag = KeyWord.searchHalf("==",
												assigment);
										if (flag != -1) {// 赋值运算符
											map.put(" == ",
													assigmentOpe
															.get(assigment[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< == ," +
											 * assigmentOpe
											 * .get(assigment[flag]) + " >");
											 */
											i++;
										} else {
											flag = KeyWord.searchHalf("==",
													publicOther);
											if (flag != -1) {// 其他运算符
												map.put(" == ",
														publicOtherOpe
																.get(publicOther[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< == ,"
												 * + publicOtherOpe
												 * .get(publicOther[flag]) +
												 * " >");
												 */
												i++;
											} else {
												flag = KeyWord.searchHalf("==",
														other);
												if (flag != -1) {// 其他运算符
													map.put(" == ",
															otherOpe.get(other[flag])
																	+ "");
													otherWordList.add(map);
													/*
													 * System.out
													 * .println("< == ," +
													 * otherOpe
													 * .get(other[flag]) +
													 * " >");
													 */
													i++;
												} else {
													Log.i("notice",
															"没有找到\"==\"对应的值");
												}
											}
										}
									}
								}
							}
						}
					} else {
						flag = KeyWord.searchHalf(codeString.charAt(i) + "",
								relation);
						if (flag != -1) {// 关系运算符
							map.put(" = ", relationOpe.get(relation[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< = ,"
							// + relationOpe.get(relation[flag]) + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", an);
							if (flag != -1) {// 位运算符
								map.put(" = ", anOpe.get(an[flag]) + "");
								otherWordList.add(map);
								// System.out.println("< = ,"
								// + anOpe.get(an[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", logical);
								if (flag != -1) {// 逻辑运算符
									map.put(" = ",
											logicalOpe.get(logical[flag]) + "");
									otherWordList.add(map);
									/*
									 * System.out.println("< = ," +
									 * logicalOpe.get(logical[flag]) + " >");
									 */
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											assigment);
									if (flag != -1) {// 赋值运算符
										map.put(" = ",
												assigmentOpe
														.get(assigment[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< = ," +
										 * assigmentOpe .get(assigment[flag]) +
										 * " >");
										 */
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												publicOther);
										if (flag != -1) {// 其他运算符
											map.put(" = ",
													publicOtherOpe
															.get(publicOther[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out .println("< = ," +
											 * publicOtherOpe
											 * .get(publicOther[flag]) + " >");
											 */
										} else {
											flag = KeyWord.searchHalf(
													codeString.charAt(i) + "",
													other);
											if (flag != -1) {// 其他运算符
												map.put(" = ",
														otherOpe.get(other[flag])
																+ "");
												otherWordList.add(map);
												/*
												 * System.out .println("< = ," +
												 * otherOpe .get(other[flag]) +
												 * " >");
												 */
											} else {
												Log.i("notice", "没有找到\""
														+ codeString.charAt(i)
														+ "\"对应的值");
											}
										}
									}
								}
							}
						}
					}
					break;
				case ',':
					flag = KeyWord.searchHalf(codeString.charAt(i) + "",
							relation);
					if (flag != -1) {// 关系运算符
						map.put(" ',' ", relationOpe.get(relation[flag]) + "");
						otherWordList.add(map);
						// System.out.println("< ',' ,"
						// + relationOpe.get(relation[flag]) + " >");
					} else {
						flag = KeyWord
								.searchHalf(codeString.charAt(i) + "", an);
						if (flag != -1) {// 位运算符
							map.put(" ',' ", anOpe.get(an[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< ',' ," +
							// anOpe.get(an[flag])
							// + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", logical);
							if (flag != -1) {// 逻辑运算符
								map.put(" ',' ", logicalOpe.get(logical[flag])
										+ "");
								otherWordList.add(map);
								// System.out.println("< ',' ,"
								// + logicalOpe.get(logical[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", assigment);
								if (flag != -1) {// 赋值运算符
									map.put(" ',' ",
											assigmentOpe.get(assigment[flag])
													+ "");
									otherWordList.add(map);
									/*
									 * System.out.println("< ',' ," +
									 * assigmentOpe.get(assigment[flag]) +
									 * " >");
									 */
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											publicOther);
									if (flag != -1) {// 其他运算符
										map.put(" ',' ",
												publicOtherOpe
														.get(publicOther[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< ',' ," +
										 * publicOtherOpe
										 * .get(publicOther[flag]) + " >");
										 */
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												other);
										if (flag != -1) {// 其他运算符
											map.put(" ',' ",
													otherOpe.get(other[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out.println("< ',' ," +
											 * otherOpe.get(other[flag]) +
											 * " >");
											 */
										} else {
											Log.i("notice", "没有找到\""
													+ codeString.charAt(i)
													+ "\"对应的值");
										}
									}
								}
							}
						}
					}
					break;
				case ';':
					flag = KeyWord.searchHalf(codeString.charAt(i) + "",
							relation);
					if (flag != -1) {// 关系运算符
						map.put(" ; ", relationOpe.get(relation[flag]) + "");
						otherWordList.add(map);
						// System.out.println("< ; ,"
						// + relationOpe.get(relation[flag]) + " >");
					} else {
						flag = KeyWord
								.searchHalf(codeString.charAt(i) + "", an);
						if (flag != -1) {// 位运算符
							map.put(" ; ", anOpe.get(an[flag]) + "");
							otherWordList.add(map);
							// System.out.println("< ; ," + anOpe.get(an[flag])
							// + " >");
						} else {
							flag = KeyWord.searchHalf(
									codeString.charAt(i) + "", logical);
							if (flag != -1) {// 逻辑运算符
								map.put(" ; ", logicalOpe.get(logical[flag])
										+ "");
								otherWordList.add(map);
								// System.out.println("< ; ,"
								// + logicalOpe.get(logical[flag]) + " >");
							} else {
								flag = KeyWord.searchHalf(codeString.charAt(i)
										+ "", assigment);
								if (flag != -1) {// 赋值运算符
									map.put(" ; ",
											assigmentOpe.get(assigment[flag])
													+ "");
									otherWordList.add(map);
									/*
									 * System.out.println("< ; ," +
									 * assigmentOpe.get(assigment[flag]) +
									 * " >");
									 */
								} else {
									flag = KeyWord.searchHalf(
											codeString.charAt(i) + "",
											publicOther);
									if (flag != -1) {// 其他运算符
										map.put(" ; ",
												publicOtherOpe
														.get(publicOther[flag])
														+ "");
										otherWordList.add(map);
										/*
										 * System.out.println("< ; ," +
										 * publicOtherOpe
										 * .get(publicOther[flag]) + " >");
										 */
									} else {
										flag = KeyWord.searchHalf(
												codeString.charAt(i) + "",
												other);
										if (flag != -1) {// 其他运算符
											map.put(" ; ",
													otherOpe.get(other[flag])
															+ "");
											otherWordList.add(map);
											/*
											 * System.out.println("< ; ," +
											 * otherOpe.get(other[flag]) +
											 * " >");
											 */
										} else {
											Log.i("notice", "没有找到\""
													+ codeString.charAt(i)
													+ "\"对应的值");
										}
									}
								}
							}
						}
					}
					break;
				case '\\':
					if (((i+1)<codeString.length())&&(codeString.charAt(i + 1) == 'n'
							|| codeString.charAt(i + 1) == 't'
							|| codeString.charAt(i + 1) == 'r')) {
						String string = codeString.charAt(i)+""
								+ codeString.charAt(i + 1) + "";
						flag = KeyWord.searchHalf(string, escapeCharacter);
						if (flag != -1) {
							map.put(string,
									escapeCharacterOpe.get(escapeCharacter[flag]) + "");
							otherWordList.add(map);
						} else {
							Log.i("notice", "没有找到\"" + codeString.charAt(i)
									+ codeString.charAt(i + 1) + "" + "\"对应的值");
						}
						i = i+1;
					} else {
						Toast.makeText(context, "第"+i+"个字符"+codeString.charAt(i)+"是个错误的字符，请改正", Toast.LENGTH_LONG).show();
						return -1;
					}
					break;
				}
				i++;
			}
			return CORRECT;
		}
		return ERROR;
	}

}
