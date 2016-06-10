package com.ilts.anywhere.cart;

import java.util.List;

public interface OrderDAO {

	public void insert(NewOrderWrapper orderWrapper);
	
	public List<Order> getAll();
	
	public List<Order> getAll(int userId);
	
	public void get();
	
	public void delete(int id);
}
