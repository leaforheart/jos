package com.jos.cart.service;

import java.util.HashMap;

import com.jos.cart.dao.CartDao;
import com.jos.cart.vo.CartBean;
import com.jos.common.baseclass.AbstractBaseService;

public class CartServiceImpl extends AbstractBaseService implements CartService {
	private CartDao cartDao;

	public CartDao getCartDao() {
		return cartDao;
	}

	public void setCartDao(CartDao cartDao) {
		this.cartDao = cartDao;
	}

	@Override
	public HashMap<String, Object> add(CartBean cartBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();

		return map;
	}

	@Override
	public HashMap<String, Object> upd(CartBean cartBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();

		return map;
	}

	@Override
	public HashMap<String, Object> del(CartBean cartBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();

		return map;
	}

	@Override
	public HashMap<String, Object> que(CartBean cartBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();

		return map;
	}
	
}
