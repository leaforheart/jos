package com.jos.cart.action;

import com.inveno.util.JsonUtil;
import com.jos.cart.service.CartService;
import com.jos.cart.vo.CartBean;
import com.jos.common.baseclass.BaseAction;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("rawtypes")
public class CartAction extends BaseAction implements ModelDriven {
	
	private static final long serialVersionUID = -6192739444494402039L;
	
	private CartBean cartBean;
	private CartService cartService;
	

	public CartBean getCartBean() {
		return cartBean;
	}


	public void setCartBean(CartBean cartBean) {
		this.cartBean = cartBean;
	}


	public CartService getCartService() {
		return cartService;
	}


	public void setCartService(CartService cartService) {
		this.cartService = cartService;
	}


	@Override
	public Object getModel() {
		if(cartBean==null) {
			cartBean = new CartBean();
		}
		return cartBean;
	}
	
	public void add(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(cartService.add(cartBean)));
	}
	public void upd(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(cartService.upd(cartBean)));
	}
	public void del(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(cartService.del(cartBean)));
	}
	public void que(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(cartService.que()));
	}
	public void clear(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(cartService.clear()));
	}
}
