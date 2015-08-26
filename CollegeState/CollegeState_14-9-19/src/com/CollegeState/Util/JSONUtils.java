package com.CollegeState.Util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.CollegeState.Data.AdvertiseImage;
import com.CollegeState.Data.GoodsInfoBean;
import com.CollegeState.Data.GrabInfoBean;
import com.CollegeState.Data.MyWealthInfoBean;
import com.CollegeState.Data.SchoolBean;
import com.CollegeState.Data.ShopInfoBean;
import com.CollegeState.Data.UserInfoBean;
import com.CollegeState.Data.UserOrderFormInfoBean;
import com.CollegeState.Data.XMPPBean;

/**
 * �������������ص�JSON����
 * 
 * @author zc
 * 
 */
public class JSONUtils {

	/**
	 * ��¼�ɹ���Ե���UserInfo��������
	 * 
	 * @param jsonString
	 *            :���������ص�JSON���ݸ�ʽ {"areaId":0,"orderAddress":null,
	 *            "userAuthority":"00001","userCredit":0,"userId":1,
	 *            "userIdCardNum":null,"userPasswd":"1234567890",
	 *            "userPhone":"18729354347",
	 *            "userRegiTimeTime":"Tue Aug 19 18:13:06 CST 2014",
	 *            "workerNum":null}
	 * @throws JSONException
	 */
	public static boolean setUserInfoAfterLogin(String jsonString) {
		try {
			JSONObject obj = new JSONObject(jsonString);
			String orderAddress = obj.getString("orderAddress") + "";
			String userAuthority = obj.getString("userAuthority") + "";
			String userCredit = obj.getInt("userCredit") + "";
			String userId = obj.getInt("userId") + "";
			String userPasswd = obj.getString("userPasswd") + "";
			String userPhone = obj.getString("userPhone") + "";
			String action = obj.getString("action") + "";
			
			UserInfoBean.setOrderAddress(orderAddress);
			UserInfoBean.setUserAuthority(userAuthority);
			UserInfoBean.setUserCredit(userCredit);
			UserInfoBean.setUserId(userId);
			UserInfoBean.setUserPasswd(userPasswd);
			UserInfoBean.setUserPhone(userPhone);
			UserInfoBean.setAction(action);
			UserInfoBean.setLogin(true);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * ����������ϢList��JSON������
	 * 
	 * @param jsonString
	 *            [{"areaId":1,"serviceCategoty":1,"shopAddress":"123456",
	 *            "shopAssess":0.0,"shopCategory":1,"shopContent":null,
	 *            "shopCredit":30,"shopId":3,"shopImg":null,"shopName":"222",
	 *            "shopPhone":"18729354347","shopSales":0,"shopState":"xx",
	 *            "userId":1,"workTime":"11:00-12:00"}]
	 * 
	 * @return
	 */
	public static List<ShopInfoBean> getShopInfoList(String jsonString) {
		List<ShopInfoBean> list = new ArrayList<ShopInfoBean>();
		try {
			JSONArray data = new JSONArray(jsonString);
			for (int i = 0; i < data.length(); i++) {
				ShopInfoBean shopInfo = new ShopInfoBean();
				JSONObject obj = data.getJSONObject(i);
				shopInfo.setAreaId(obj.getInt("areaId"));// ����id
				shopInfo.setServiceCategoty(obj.getInt("serviceCategoty"));// ��Ӫ���
				shopInfo.setShopAddress(obj.getString("shopAddress"));// ���̵�ַ
				shopInfo.setShopAssess(obj.getDouble("shopAssess"));// ��������
				shopInfo.setShopCategory(obj.getInt("shopCategory"));// �������
				shopInfo.setShopContent(obj.getString("shopContent"));// ���̼��
				shopInfo.setShopCredit(obj.getInt("shopCredit"));// ���̻���
				shopInfo.setShopId(obj.getInt("shopId"));// ����id
				shopInfo.setShopImg(obj.getString("shopImg"));// ����ͼƬ��ַ
				shopInfo.setShopName(obj.getString("shopName"));// ��������
				shopInfo.setShopPhone(obj.getString("shopPhone"));// ���̵绰
				shopInfo.setShopSales(obj.getInt("shopSales"));// ��������
				shopInfo.setShopState(obj.getString("shopState"));// ����״̬
				shopInfo.setUserId(obj.getInt("userId"));// �̻�id
				shopInfo.setWorkTime(obj.getString("workTime"));// Ӫҵʱ��
				list.add(shopInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ������Ʒ��Ϣ��List��JSON����
	 * 
	 * @param jsonString
	 *           [{"freight":0.0,"goodsAddtime":"2014-9-15 18:14:42",
	 *           "goodsAssess":0.0,"goodsCategory":1,"goodsColleted":0,
	 *           "goodsContent":null,"goodsId":2654208,"goodsImgUrl":null,
	 *           "goodsName":"������Ʒ������ר�ã�","goodsPrice":10.0,"isPeisong":1,
	 *           "search":null,"shopId":2621440,"shopName":"������̣�����ר�ã�"}]
	 * @return
	 */
	public static List<GoodsInfoBean> getSearchInfoList(String jsonString) {
		// TODO Auto-generated method stub
		List<GoodsInfoBean> list = new ArrayList<GoodsInfoBean>();
		try {
			JSONArray data = new JSONArray(jsonString);
			for (int i = 0; i < data.length(); i++) {
				GoodsInfoBean GoodsInfo = new GoodsInfoBean();
				JSONObject obj = data.getJSONObject(i);
				GoodsInfo.setGoodsId(obj.getInt("goodsId"));
				GoodsInfo.setShopId(obj.getInt("shopId"));
				GoodsInfo.setGoodsName(obj.getString("goodsName") + "");
				GoodsInfo.setGoodsCategory(obj.getString("goodsCategory") + "");
				GoodsInfo.setGoodsImgUrl(obj.getString("goodsImgUrl") + "");
				GoodsInfo.setGoodsContent(obj.getString("goodsContent") + "");
				GoodsInfo.setGoodsAddtime(obj.getString("goodsAddtime"));
				GoodsInfo.setIsPeisong(obj.getInt("isPeisong"));
				GoodsInfo.setGoodsPrice(obj.getDouble("goodsPrice"));
				GoodsInfo.setFreight(obj.getDouble("freight"));
				// SearchInfo.setGoodsScan(obj.getInt("goodsScan"));
				GoodsInfo.setGoodsColleted(obj.getInt("goodsColleted"));
				GoodsInfo.setGoodsAssess(obj.getDouble("goodsAssess"));
				GoodsInfo.setShopName(obj.getString("shopName"));
				list.add(GoodsInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ���ض������json�������� [{"consumeTime":"11:00-12:00","goodsCount":2,
	 * "goodsId":2,"goodsMore":"buhaochi","goodsName":"dadddaddada",
	 * "goodsPrice":1100.0,"orderAddress":"1","orderId":5,"orderItemId":10,
	 * "orderItemState":1,"orderPhone":"18729354347","orderWay":1,"shopId":3,
	 * "shopName":"222","shopPhone":"18729354347","userId":1}]
	 * @param jsonString
	 * @return
	 */
	public static List<UserOrderFormInfoBean> getUserOrderFormInfoList(
			String jsonString) {
		List<UserOrderFormInfoBean> list = new ArrayList<UserOrderFormInfoBean>();
		UserOrderFormInfoBean userOrderFormInfoBean = null;
		JSONArray jsonArr;
		try {
			jsonArr = new JSONArray(jsonString);
			for (int i = 0; i < jsonArr.length(); i++) {
				userOrderFormInfoBean = new UserOrderFormInfoBean();
				JSONObject obj = jsonArr.getJSONObject(i);
				userOrderFormInfoBean.setOrderItemId(obj.getInt("orderItemId"));
				userOrderFormInfoBean.setOrderItemState(obj
						.getInt("orderItemState"));
				userOrderFormInfoBean.setUserId("");
				userOrderFormInfoBean.setGoodsId(obj.getInt("goodsId") + "");
				userOrderFormInfoBean.setGoodsName(obj.getString("goodsName"));
				userOrderFormInfoBean.setGoodsPrice(obj.getInt("goodsPrice")
						+ "");
				userOrderFormInfoBean.setShopName(obj.getString("shopName"));
				userOrderFormInfoBean.setGoodsCount(obj.getInt("goodsCount"));
				list.add(userOrderFormInfoBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	// �����б��json
	/**
	 *[{"areaId":1,"goodsId":1,"goodsName":"dadadasda",
	 *"proAddTime":"Tue Aug 19 18:53:22 CST 2014","proGoodsCount":100,
	 *"proGoodsPrice":0.0,"proTime":"1","promotionId":1,"shopId":3,
	 *"shopName":"222"},{"areaId":1,"goodsId":2,"goodsName":"dadddaddada",
	 *"proAddTime":"Tue Aug 19 18:53:41 CST 2014","proGoodsCount":100,
	 *"proGoodsPrice":100.0,"proTime":"2014-08-07","promotionId":2,"shopId":3,
	 *"shopName":"222"}]
	 **/

	public static List<GrabInfoBean> getGrabInfoList(String jsonString) {
		// TODO Auto-generated method stub
		List<GrabInfoBean> list = new ArrayList<GrabInfoBean>();
		try {
			JSONArray data = new JSONArray(jsonString);
			for (int i = 0; i < data.length(); i++) {
				GrabInfoBean GrabInfo = new GrabInfoBean();
				JSONObject obj = data.getJSONObject(i);
				GrabInfo.setGoodsId(obj.getInt("goodsId"));
				GrabInfo.setGoodsName(obj.getString("goodsName"));
				GrabInfo.setProAddTime(obj.getString("proAddTime"));
				GrabInfo.setProGoodsCount(obj.getInt("proGoodsCount"));
				GrabInfo.setProGoodsPrice(obj.getDouble("proGoodsPrice"));
				GrabInfo.setProTime(obj.getString("proTime"));
				GrabInfo.setPromotionId(obj.getInt("promotionId"));
				GrabInfo.setShopId(obj.getInt("shopId"));
				GrabInfo.setShopName(obj.getString("shopName"));

				list.add(GrabInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	// �ҵĲƸ��б��json
	/**
	 * [{"goodsId":1,"goodsName":"dadadasda","myGoodsCount":1,
	 * "proGoodsPrice":0.0,"proTime":"1","promotionId":1,"shopId":3,
	 * "shopName":"222","userId":1,"wealthId":1},
	 * {"goodsId":1,"goodsName":"dadadasda","myGoodsCount":1,
	 * "proGoodsPrice":0.0,"proTime":"1","promotionId":1,"shopId":3,
	 * "shopName":"222","userId":1,"wealthId":2}]
	 **/

	public static List<MyWealthInfoBean> getMyWealthInfoList(String jsonString) {
		// TODO Auto-generated method stub
		List<MyWealthInfoBean> list = new ArrayList<MyWealthInfoBean>();
		try {
			JSONArray data = new JSONArray(jsonString);
			for (int i = 0; i < data.length(); i++) {
				MyWealthInfoBean myWealthInfo = new MyWealthInfoBean();
				JSONObject obj = data.getJSONObject(i);
				myWealthInfo.setGoodsId(obj.getInt("goodsId"));
				myWealthInfo.setUserId(obj.getInt("userId"));
				myWealthInfo.setMyGoodsCount(obj.getInt("myGoodsCount"));
				myWealthInfo.setProGoodsPrice(obj.getDouble("proGoodsPrice"));
				myWealthInfo.setProTime(obj.getString("proTime"));
				myWealthInfo.setWealthId(obj.getInt("wealthId"));
				myWealthInfo.setGoodsName(obj.getString("goodsName"));
				myWealthInfo.setShopName(obj.getString("shopName"));
				list.add(myWealthInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ��ȡѧУ�б�
	 * 
	 * @param jsonStr
	 *            [{"areaId":1,"areaName":"??????","cityId":1001},
	 *            {"areaId":2,"areaName":"???????","cityId":1001}]
	 * @return
	 */
	public static List<SchoolBean> getSchools(String jsonStr) {
		List<SchoolBean> list = new ArrayList<SchoolBean>();
		try {
			JSONArray arr = new JSONArray(jsonStr);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				SchoolBean s = new SchoolBean();
				s.setAreaId(obj.getInt("areaId"));
				s.setCityId(obj.getInt("cityId"));
				s.setAreaName(obj.getString("areaName"));
				list.add(s);
			}
			return list;
		} catch (JSONException e) {
			return null;
		}
	}
	
	/**
	 * ת��������Ϣ
	 * @param title(title)
	 * @param message(message)
	 * @return
	 */
	public static XMPPBean toXmppMessage(String title,String message){
		XMPPBean xb = new XMPPBean();
		xb.setShopName(title);
		try {
			JSONObject obj = new JSONObject(message);
			xb.setMessage(obj.getString("message"));
			xb.setShopId(obj.getString("shopId"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return xb;
	}
	
	public static List<AdvertiseImage> getAdImages(String jsonString){
		List<AdvertiseImage> list = new ArrayList<AdvertiseImage>();
		try {
			JSONArray arr = new JSONArray(jsonString);
			for(int i = 0;i<arr.length();i++){
				JSONObject obj = arr.getJSONObject(i);
				AdvertiseImage ad = new AdvertiseImage();
				ad.setId(obj.getInt("adId"));
				ad.setAdImg(obj.getString("adImg"));
				ad.setAdName(obj.getString("adName"));
				list.add(ad);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
