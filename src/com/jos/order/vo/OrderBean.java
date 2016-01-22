package com.jos.order.vo;

import java.util.List;

import com.jos.order.model.Order;
import com.jos.order.model.OrderItem;

public class OrderBean {
	private Order order;
	private List<OrderItem> orderItemList;
	private String orderItem;
	private String json;
	
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
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
		orderItemList = jsonToList(json);
		return orderItemList;
	}
	
	private List<OrderItem> jsonToList(String json) {
		return null;
	}
}
