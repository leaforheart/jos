package com.jos.order.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.jos.address.model.Address;
import com.jos.common.baseclass.AbstractBaseService;
import com.jos.common.util.Constants;
import com.jos.order.dao.OrderDao;
import com.jos.order.model.Order;
import com.jos.order.model.OrderFllow;
import com.jos.order.model.OrderItem;
import com.jos.order.vo.OrderBean;

public class OrderServiceImpl extends AbstractBaseService implements OrderService {
	
	private OrderDao orderDao;
	

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	/**
	 * @author XYL
	 * 
	 * 提交订单
	 * 
	 * 客户端要传入的参数有：
	 * orderBean.order.userId
	 * orderBean.order.addressId
	 * orderBean.order.expressSupplier
	 * orderBean.order.expressCost
	 * orderBean.order.paySupplier
	 * 
	 * orderBean.orderItem(需要json解析)
	 */
	@Override
	public HashMap<String, Object> submiteOrder(OrderBean orderBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			//1.创建订单
			Order order = orderBean.getOrder();
			order.setStatus(String.valueOf(OrderStatus.Submited.getState()));
			order.setCreateTime(new Date());
			order.setLastUpdateTime(new Date());
			orderDao.save(order);
			//2.添加商品（订单-商品表）
			String orderId = order.getId();
			List<OrderItem> list = orderBean.getOrderItemList();
			double totalPrice = 0;
			for(OrderItem oi:list) {
				oi.setOrderId(orderId);
				orderDao.save(oi);
				String unitPrice = oi.getItemUnitPrice();
				String amount = oi.getItemAmount();
				totalPrice += Double.parseDouble(unitPrice)*Integer.parseInt(amount);
			}
			//3.生成订单号，计入总价
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMDD");
			String date = sdf.format(new Date());
			String orderCode = Constants.ORDER_CODE_PRE+date+getZeros(orderId)+orderId;
			order.setOrderCode(orderCode);
			order.setOrderCost(String.valueOf(totalPrice));
			order.setPayCost(String.valueOf(totalPrice+Double.parseDouble(orderBean.getOrder().getExpressCost())));
			
			//4.生成流水
			OrderFllow of = new OrderFllow();
			of.setOrderId(orderId);
			of.setPreStatus(String.valueOf(OrderStatus.init.getState()));
			of.setCurStatus(String.valueOf(OrderStatus.Submited.getState()));
			of.setCreateTime(new Date());
			orderDao.save(of);
			
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		
		return map;
	}
	
	private String getZeros(String orderId) {
		int i = 11 - orderId.length();
		String s = "";
		for(int j=0;j<i;j++) {
			s+="0";
		}
		return s;
	}

	@Override
	public HashMap<String, Object> payOrder(OrderBean orderBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		return map;
	}
	/**
	 * @author XYL
	 * 录入物流跟踪号，并标记为已发货
	 * 
	 */
	@Override
	public HashMap<String, Object> deliverOrder(OrderBean orderBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			//添加订单物流信息
			String id = orderBean.getOrder().getId();
			String pre_status = orderBean.getOrder().getStatus();
			if(!String.valueOf(OrderStatus.Payed.getState()).equals(pre_status)) {
				map.put(Constants.RETURN_CODE, "-1");//非已支付状态订单，不可以标记为已发货。
				return map;
			}
			Order order = orderDao.findById(id, Order.class);
			order.setExpressCode(orderBean.getOrder().getExpressCode());
			//标记已发货状态
			order.setStatus(String.valueOf(OrderStatus.Delivered.getState()));
			
			OrderFllow of = new OrderFllow();
			of.setOrderId(id);
			of.setPreStatus(String.valueOf(OrderStatus.Payed.getState()));
			of.setCurStatus(String.valueOf(OrderStatus.Delivered.getState()));
			of.setCreateTime(new Date());
			orderDao.save(of);
			
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		
		return map;
	}
	/**
	 * 让用户手工标记完成订单
	 * （系统还有一个定时完成订单的功能）
	 */
	@Override
	public HashMap<String, Object> completeOrder(OrderBean orderBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			//标记已完成状态
			String pre_status = orderBean.getOrder().getStatus();
			if(!String.valueOf(OrderStatus.Payed.getState()).equals(pre_status)) {
				map.put(Constants.RETURN_CODE, "-1");//非已发货状态订单，不可以标记为已完成。
				return map;
			}
			
			String id = orderBean.getOrder().getId();
			Order order = orderDao.findById(id, Order.class);
			order.setStatus(String.valueOf(OrderStatus.Completed.getState()));
			
			OrderFllow of = new OrderFllow();
			of.setOrderId(id);
			of.setPreStatus(String.valueOf(OrderStatus.Delivered.getState()));
			of.setCurStatus(String.valueOf(OrderStatus.Completed.getState()));
			of.setCreateTime(new Date());
			orderDao.save(of);
			
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		
		return map;
	}
	/**
	 * 在没有审核功能的情况下，先不做取消订单功能
	 */
	@Override
	public HashMap<String, Object> cancelOrder(OrderBean orderBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		//取消订单
		return map;
	}
	
	/**
	 * 查看订单详情
	 */
	@Override
	public HashMap<String, Object> getOrderDetail(OrderBean orderBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String id = orderBean.getOrder().getId();
			Order order = orderDao.findById(id, Order.class);
			String addressId = order.getAddressId();
			Address address = orderDao.findById(addressId,Address.class);
			order.setAddress(address);
			List<String> parameters = new ArrayList<String>();
			parameters.add(id);
			List<OrderItem> list = orderDao.findByHql("from OrderItem where orderId=?", parameters);
			order.setOrderItemList(list);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			map.put(Constants.RETURN_DATA, order);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		
		return map;
	}
	/**
	 * @author XYL
	 * 获取某用户的订单列表
	 */
	@Override
	public HashMap<String, Object> getOrderListForUser(OrderBean orderBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			List<String> param = new ArrayList<String>();
			param.add(orderBean.getOrder().getUserId());
			List<Order> list = orderDao.findByHql("from Order where userId=?", param);
			for(Order order:list) {
				String addressId = order.getAddressId();
				Address address = orderDao.findById(addressId,Address.class);
				order.setAddress(address);
				String orderId = order.getId();
				List<String> parameters = new ArrayList<String>();
				parameters.add(orderId);
				List<OrderItem> orderItemList = orderDao.findByHql("from OrderItem where orderId=?", parameters);
				order.setOrderItemList(orderItemList);
			}
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			map.put(Constants.RETURN_DATA, list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		return map;
	}
	
	/**
	 * @author XYL
	 * 获取系统中的所有订单列表
	 */
	@Override
	public HashMap<String, Object> getOrderList(OrderBean orderBean) {
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			List<Order> list = orderDao.findByHql("from Order", null);
			for(Order order:list) {
				String addressId = order.getAddressId();
				Address address = orderDao.findById(addressId,Address.class);
				order.setAddress(address);
				String orderId = order.getId();
				List<String> parameters = new ArrayList<String>();
				parameters.add(orderId);
				List<OrderItem> orderItemList = orderDao.findByHql("from OrderItem where orderId=?", parameters);
				order.setOrderItemList(orderItemList);
			}
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			map.put(Constants.RETURN_DATA, list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		
		return map;
	}

}
