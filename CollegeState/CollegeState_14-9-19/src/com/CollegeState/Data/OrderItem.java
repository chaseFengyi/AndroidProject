package com.CollegeState.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * �������Ӧ�ó���Χ�ڵĵ��� �ڹ��ﳵactivity����ʾʱ��Ҫ���д���
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
	 * �ύ����֮��ɾ�����ﳵ����
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

	// �����Ʒ
	public static void addItem(GoodsInfoBean item) {
		// �Ƿ����
		boolean isAdded = false;
		for (OrderBean orderBean : orderItems) {
			// ���ﳵ���Ѿ����ڵ�ǰ��Ʒ
			if (orderBean.getOrderItem().getGoodsId() == item.getGoodsId()) {
				orderBean.setCount(orderBean.getCount() + 1);
				isAdded = true;
			}
		}
		// ��ԭ����list�в���������ӵ�item
		if (isAdded == false) {
			OrderBean orderBean = new OrderBean(item, 1);
			orderItems.add(orderBean);
		}
	}

	// ����IDɾ�����ﳵ�е���Ʒ
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
		private int count;// �������ƽ�Ĵ���
		private String goodsMore;// ��ע

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
