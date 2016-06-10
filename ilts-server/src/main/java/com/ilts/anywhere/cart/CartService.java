package com.ilts.anywhere.cart;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.*;
import com.ilts.anywhere.authentication.JdbcSessionDAO;
import com.ilts.anywhere.authentication.JdbcUserDAO;
import com.ilts.anywhere.authentication.UserWrapper;
import com.ilts.anywhere.datastore.AnywhereDB;
import com.ilts.anywhere.response.DataResponse;
import com.ilts.anywhere.response.LoginResponse;
import com.ilts.anywhere.response.Response;
import com.ilts.anywhere.response.ResponseFactory;
import com.ilts.anywhere.response.ResponseType;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Service
public class CartService {
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private JdbcOrderDAO orderDAO;

	@Autowired
	private AnywhereDB anywhereDB;

	@Autowired
	private JdbcSessionDAO sessionDAO;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	CartService() {
	}
	
	public Response insertOrder(String orderJSON) {
		
		NewOrderWrapper orderWrapper = new NewOrderWrapper();
		
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			orderWrapper = mapper.readValue(orderJSON, NewOrderWrapper.class);
			

		} catch (JsonGenerationException e) {
 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}
		
//		if (this.sessionDAO.validSession(orderWrapper.getToken())) {
//		
//			// get user id
//			orderWrapper.setUserId(this.sessionDAO.getUserId(orderWrapper.getToken()));
//			
//			// get cost of order
//			int orderCost = this.calculateOrderCost(orderWrapper.getGameID(), orderWrapper.getSystem(), orderWrapper.getNumbers());
//			orderWrapper.setCost(orderCost);
//			
//			this.orderDAO.insert(orderWrapper);
//			
//			Response newOrderResponse = ResponseFactory.makeResponse(ResponseType.SUCCESSNEWORDER);
//			
//			return newOrderResponse;
//		} else {
//			Response unauthorizedResponse = ResponseFactory.makeResponse(ResponseType.ERRORINVALIDSESSION);
//			return unauthorizedResponse;
//		}
            return null;
	}
	
	public List<Order> getOrders(int userId) {

		List<Order> orders = this.orderDAO.getAll(userId);
		return orders;

	}
	
	public List<Order> getAllOrders() {
		List<Order> orders = this.orderDAO.getAll();
		return orders;
	}
	
	public void deleteOrder(int id) {
		this.orderDAO.delete(id);
	}
	
	private int calculateOrderCost(int gameId, String system, ArrayList<ArrayList<Integer>> numbers) {
		// retrieve game cost
		int gameCost = this.anywhereDB.getCost(gameId);
		
		// retrieve system
		int systemInt = this.anywhereDB.getSystem(system);
		
		// retrieve sets of numbers
		int numberSets = numbers.size();
		
		int totalCost = (this.factorial(systemInt).divide(this.factorial(6).multiply(this.factorial(systemInt - 6)))).intValue() * gameCost * numberSets;
		
		return totalCost;
	}
	
	public static BigInteger factorial(int number){
		BigInteger factValue = BigInteger.ONE;
		for ( int i = 2; i <= number; i++){
			factValue = factValue.multiply(BigInteger.valueOf(i));
		}
		return factValue;
	}


//	public Response registerUser(String registerJSON) {
//		
//		UserWrapper userWrap = new UserWrapper();
//		
//		try {
//
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			userWrap = mapper.readValue(registerJSON, UserWrapper.class);
//			
//
//		} catch (JsonGenerationException e) {
// 
//			e.printStackTrace();
//	 
//		} catch (IOException e) {
//	 
//			e.printStackTrace();
//	 
//		}
//		
//		User user = new User();
//		user.setUserName(userWrap.getUsername());
//		user.setPassword(userWrap.getPassword());
//		user.setFirstName(userWrap.getFirstName());
//		user.setLastName(userWrap.getLastName());
//		user.setBirthDate(userWrap.getBirthDate());
//		user.setGender(userWrap.getGender());
//		this.userDAO.register(user);
//        
//        Response registerResponse = ResponseFactory.makeResponse(ResponseType.SUCCESSREGISTER);
//        
//        return registerResponse;
//	}
//	
//	public Response loginUser(String username, String password) {
//		
//		User user = this.userDAO.login(username, password);
//		
//		if (user != null) {
//			Session session = new Session(user.getUserId());
//			this.sessionDAO.insert(session);
//			
//			LoginResponse loginResponse = (LoginResponse)ResponseFactory.makeResponse(ResponseType.SUCCESSLOGIN);
//			loginResponse.setSession(session);
//			
//			return loginResponse;
//			
//		} else {
//			Response loginResponse = ResponseFactory.makeResponse(ResponseType.ERROR);
//			loginResponse.setMessage("Error logging in");
//			
//			return loginResponse;
//		}
//	}
	
}
