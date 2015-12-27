package com.jos.cart.service;

import java.util.HashMap;

import com.jos.cart.vo.CartBean;
import com.jos.common.baseclass.IBaseService;

public interface CartService extends IBaseService {
	HashMap<String,Object> add(CartBean cartBean);
	HashMap<String,Object> upd(CartBean cartBean);
	HashMap<String,Object> del(CartBean cartBean);
	HashMap<String,Object> que(CartBean cartBean);
}
