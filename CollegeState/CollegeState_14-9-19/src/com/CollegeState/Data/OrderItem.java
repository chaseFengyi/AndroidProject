package com.CollegeState.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类是应用程序范围内的单例 在购物车activity中显示时需要进行处理
 * 
 * @author zc
 * 
 */
public class OrderItem {
	public static List<OrderBean> orderItems = new ArrayList<OrderBean>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (OrderBean orderBean : orderItems) {
			sb.append("[goodsId=" + orderBean.getOrderItem().getGoodsId()
					+ " goodsCount=" + orderBean.getCount() + " goodsMore="
					+ orderBean.getGoodsMore() + "]");
		}
		return sb.toString();
	}

	public static List<OrderBean> getOrderItems() {
		return orderItems;
	}

	/**
	 * 提交订单之后删除购物车内容
	 * 
	 * @return
	 */
	public static boolean clearAllData() {
		boolean flag = false;
		orderItems.removeAll(orderItems);
		if (orderItems.size() == 0) {
			flag = true;
		}
		System.out.println("orderItems.size()" + orderItems.size());
		return flag;
	}

	// 添加商品
	public static void addItem(GoodsInfoBean item) {
		// 是否添加
		boolean isAdded = false;
		for (OrderBean orderBean : orderItems) {
			// 购物车中已经存在当前商品
			if (orderBean.getOrderItem().getGoodsId() == item.getGoodsId()) {
				orderBean.setCount(orderBean.getCount() + 1);
				isAdded = true;
			}
		}
		// 在原来的list中不存在新添加的item
		if (isAdded == false) {
			OrderBean orderBean = new OrderBean(item, 1);
			orderItems.add(orderBean);
		}
	}

	// 按照ID删除购物车中的物品
	public static boolean deleteItem(int itemID) {
		boolean flag = false;
		for (int i = 0; i < orderItems.size(); i++) {
			if (orderItems.get(i).getOrderItem().getGoodsId() == itemID) {
				orderItems.remove(i);
				flag = true;
			}
		}
		return flag;
	}

	public static boolean modifyGoodsMore(int itemID, String goodsMore) {
		boolean flag = false;
		for (int i = 0; i < orderItems.size(); i++) {
			if (orderItems.get(i).getOrderItem().getGoodsId() == itemID) {
				orderItems.get(i).setGoodsMore(goodsMore);
			}
		}
		return flag;
	}

	public static class OrderBean {
		private GoodsInfoBean orderItem;
		private int count;// 点这个上平的次数
		private String goodsMore;// 备注

		public OrderBean(GoodsInfoBean orderItem) {
			this.orderItem = orderItem;
		}

		public OrderBean(GoodsInfoBean orderItem, int count) {
			this.orderItem = orderItem;
			this.count = count;
		}

		public GoodsInfoBean getOrderItem() {
			return orderItem;
		}

		public void setOrderItem(GoodsInfoBean orderItem) {
			this.orderItem = orderItem;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getGoodsMore() {
			return goodsMore;
		}

		public void setGoodsMore(String goodsMore) {
			this.goodsMore = goodsMore;
		}

	}
}
