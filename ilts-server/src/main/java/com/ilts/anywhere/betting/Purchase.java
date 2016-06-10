package com.ilts.anywhere.betting;

import java.util.ArrayList;

public class Purchase {

	private String date;
//	// private Order orders;
//	private int total;
//	private String token;
	private int userId;
	private ArrayList<Integer> orders;

	public Purchase(String date, ArrayList<Integer> orders) {
		this.orders = orders;
		this.date = date;
	}

	public Purchase() {}

	public ArrayList<Integer> getOrders() {
		return orders;
	}

	public void setOrders(ArrayList<Integer> orders) {
		this.orders = orders;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

//	public String getDate() {
//		return this.date;
//	}
//
//	public double getPrice() {
//		// LottoSystem.out.println(this.orders);
//		// return this.orders.getPrice();
//		return (double) this.total;
//	}
//
//	public String getToken() {
//		return this.token;
//	}
//	
//	public void setToken(String token) {
//		this.token = token;
//	}
	
}