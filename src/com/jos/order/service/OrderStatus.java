package com.jos.order.service;

public enum OrderStatus {
	
	init(0),Submited(1),Payed(2),Delivered(3),Completed(4),Canceled(5);
	
	private int state;
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	private OrderStatus(int state) {
		this.state = state;
	}
}
