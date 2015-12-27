package com.jos.order.service;

import java.util.HashMap;

import com.jos.common.baseclass.IBaseService;
import com.jos.order.vo.OrderBean;

public interface OrderService extends IBaseService {
	HashMap<String,Object> submiteOrder(OrderBean orderBean);
	HashMap<String,Object> payOrder(OrderBean orderBean);
	HashMap<String,Object> deliverOrder(OrderBean orderBean);
	HashMap<String,Object> completeOrder(OrderBean orderBean);
	HashMap<String,Object> cancelOrder(OrderBean orderBean);
	
	HashMap<String,Object> getOrderDetail(OrderBean orderBean);
	HashMap<String,Object> getOrderListForUser(OrderBean orderBean);
	HashMap<String,Object> getOrderList(OrderBean orderBean);
}
