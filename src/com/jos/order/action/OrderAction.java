package com.jos.order.action;

import com.jos.common.baseclass.BaseAction;
import com.jos.order.service.OrderService;
import com.jos.order.vo.OrderBean;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("rawtypes")
public class OrderAction  extends BaseAction implements ModelDriven {
	private static final long serialVersionUID = 8018359012750073240L;
	
	private OrderService orderService;

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
	private OrderBean orderBean;

	public OrderBean getOrderBean() {
		return orderBean;
	}

	public void setOrderBean(OrderBean orderBean) {
		this.orderBean = orderBean;
	}

	@Override
	public Object getModel() {
		if(orderBean==null) {
			orderBean = new OrderBean();
		}
		return orderBean;
	}

}
