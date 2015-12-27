package com.jos.order.vo;

import java.util.List;

import com.jos.order.model.Order;
import com.jos.order.model.OrderItem;

public class OrderBean {
	private Order order;
	private List<OrderItem> orderItemList;
	private String orderItem;
	
	public String getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(String orderItem) {
		this.orderItem = orderItem;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
}
