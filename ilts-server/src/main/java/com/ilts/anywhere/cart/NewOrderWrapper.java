package com.ilts.anywhere.cart;

//import java.util.List;
import java.util.ArrayList;

public class NewOrderWrapper {
	
	private int gameID;
	private int drawID;
	private String system;
	private ArrayList<ArrayList<Integer>> numbers = new ArrayList<ArrayList<Integer>>();
	private int cost;
	private String token;
	private int userId;
	
	public int getGameID() {
		return gameID;
	}
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
	public int getDrawID() {
		return drawID;
	}
	public void setDrawID(int drawID) {
		this.drawID = drawID;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public ArrayList<ArrayList<Integer>> getNumbers() {
		return numbers;
	}
	public void setNumbers(ArrayList<ArrayList<Integer>> numbers) {
		this.numbers = numbers;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
