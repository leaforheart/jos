package com.jos.cart.vo;

import java.util.ArrayList;
import java.util.List;

import com.jos.cart.model.Cart;

public class CartBean {
	private Cart cart;
	private List<Cart> cartList = new ArrayList<Cart>();
	private double tPrice;
	private String[] ids;
	

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public double gettPrice() {
		return tPrice;
	}

	public void settPrice(double tPrice) {
		this.tPrice = tPrice;
	}

	public List<Cart> getCartList() {
		return cartList;
	}

	public void setCartList(List<Cart> cartList) {
		this.cartList = cartList;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
}
