package com.mynutritionstreet.util;

public class UrlUtils {
	private final static String IP = "192.168.1.102";
	public static final String IP_ADDRESS = "http://"+IP+":8080/NutritionServer/";
	// �û�ע��
	public static final String USER_REGISTER = "http://"+IP+":8080/NutritionServer/user_regist";
	// �û���½
	public static final String USER_LOGIN = "http://"+IP+":8080/NutritionServer/user_login";
	// ���goods
	public static final String ADD_GOODS = "http://"+IP+":8080/NutritionServer/goods_addGoods";
	// �������Ͳ�ѯgoods
	public static final String QUERY_BY_TYPE = "http://"+IP+":8080/NutritionServer/goods_queryByType";
	// ��ʳȦ����
	public static final String PUBLISH_PICTURE = "http://"+IP+":8080/NutritionServer/publish_publishPicture";
	// ��ѯ��ʳȦ
	public static final String QUERY_FOOD_CIRCLE = "http://"+IP+":8080/NutritionServer/publish_queryFoodCircle";
	// ����ղ�
	public static final String ADD_COLLECTION = "http://"+IP+":8080/NutritionServer/collection_addCollection";
	// �ҵ��ղ�
	public static final String QUERY_MY_COLLECTION = "http://"+IP+":8080/NutritionServer/collection_queryMyCollection";
	// �ҵķ���
	public static final String QUERY_MY_GOODS = "http://"+IP+":8080/NutritionServer/collection_goods_queryMyGoods";
	// ͼƬ����
	public static final String PICTURE_DOWNLOAD = "http://"+IP+":8080/NutritionServer/pictureDownload";

}
