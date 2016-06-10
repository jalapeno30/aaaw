package com.ilts.anywhere.cart;


public class OrderFactory {
	
	public static Order makeOrder(OrderType orderType) {
		Order order = null;
		switch (orderType) {
			case PCSO:
				order = new PCSOOrder();
				break;
                           case STANDARD:
				order = new StandardOrder();
				break; 
			default:
				break;
		}
		return order;
	}
}
