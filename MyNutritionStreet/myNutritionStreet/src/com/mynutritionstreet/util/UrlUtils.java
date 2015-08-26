package com.mynutritionstreet.util;

public class UrlUtils {
	private final static String IP = "192.168.1.102";
	public static final String IP_ADDRESS = "http://"+IP+":8080/NutritionServer/";
	// 用户注册
	public static final String USER_REGISTER = "http://"+IP+":8080/NutritionServer/user_regist";
	// 用户登陆
	public static final String USER_LOGIN = "http://"+IP+":8080/NutritionServer/user_login";
	// 添加goods
	public static final String ADD_GOODS = "http://"+IP+":8080/NutritionServer/goods_addGoods";
	// 根据类型查询goods
	public static final String QUERY_BY_TYPE = "http://"+IP+":8080/NutritionServer/goods_queryByType";
	// 美食圈发布
	public static final String PUBLISH_PICTURE = "http://"+IP+":8080/NutritionServer/publish_publishPicture";
	// 查询美食圈
	public static final String QUERY_FOOD_CIRCLE = "http://"+IP+":8080/NutritionServer/publish_queryFoodCircle";
	// 添加收藏
	public static final String ADD_COLLECTION = "http://"+IP+":8080/NutritionServer/collection_addCollection";
	// 我的收藏
	public static final String QUERY_MY_COLLECTION = "http://"+IP+":8080/NutritionServer/collection_queryMyCollection";
	// 我的发布
	public static final String QUERY_MY_GOODS = "http://"+IP+":8080/NutritionServer/collection_goods_queryMyGoods";
	// 图片下载
	public static final String PICTURE_DOWNLOAD = "http://"+IP+":8080/NutritionServer/pictureDownload";

}
