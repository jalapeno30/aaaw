package com.ilts.anywhere.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.PreparedStatementCreator;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcOrderDAO implements OrderDAO {
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	JdbcOrderDAO() {
	}
	static{System.out.println("************ jdbc order DAO *****************");}
	public void insert(NewOrderWrapper orderWrapper) {
		
//		final String gamesInsertSQL = "INSERT INTO games(name, numbers, cost, logo) VALUES(?, ?, ?, ?)";
//    	final String drawsInsertSQL = "INSERT INTO draws(gameId, jackpot, date, day, vendorCode) VALUES(?, ?, ?, ?, ?)";
    	
    	final String orderInsertSQL = "INSERT INTO orders(game_id, draw_id, user_id, system_name, cost ) VALUES(?, ?, ?, ?, ?)";
    	final String numbersInsertSQL = "INSERT INTO orders_numbers(order_id, numbers) VALUES(?, ?)";
    	
    	KeyHolder keyHolder = new GeneratedKeyHolder();
    	int row;
    	
    	final NewOrderWrapper ow = orderWrapper; 
    	
    	row = this.jdbcTemplate.update(new PreparedStatementCreator(){
    		public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
    			PreparedStatement ps = connection.prepareStatement(orderInsertSQL, Statement.RETURN_GENERATED_KEYS);
    			ps.setInt(1, ow.getGameID());
    			ps.setInt(2,  ow.getDrawID());
    			ps.setString(3, ow.getSystem());
    			ps.setInt(4, ow.getCost());
    			ps.setInt(5, ow.getUserId());
    			return ps;
    		}
    	}, keyHolder);
    	
    	for (ArrayList<Integer> numbers : ow.getNumbers()) {
    		String numberList = "";
    		for (Integer n : numbers) {
    			numberList += n.toString() + ",";
    		}
    		jdbcTemplate.update(numbersInsertSQL, new Object[]{
	    		keyHolder.getKey(), numberList
	    	});
    	}

	}
	
	public List<Order> getAll() {
            System.out.println("************ getAll()************");
		String ordersSQL = "SELECT orders.order_id AS orderID,  orders.cost AS orderCost,  draw_id, games.game_name as gameName, "
				+ "games.game_cost as gameCost, system_name, users.user_name AS username, "
				+ "orders.active AS active, orders.deleted AS deleted "
				+ "FROM orders "
				+ "JOIN games ON orders.game_id = games.game_id "
				+ "LEFT JOIN users ON orders.user_id = users.user_id";
		
		String drawSQL = "SELECT * FROM draws WHERE draw_id=?";
		String numbersSQL = "SELECT * FROM orders_numbers WHERE order_id=?";
		
		List<Order> orders = new ArrayList<Order>();
		 System.out.println("************* orders size********** "+orders.size());
		List<Map<String,Object>> rows = this.jdbcTemplate.queryForList(ordersSQL);
		for (Map row: rows) {
                     System.out.println("***** row in getAll() "+row);
			StandardOrder order = (StandardOrder)OrderFactory.makeOrder(OrderType.STANDARD);
			order.setId(((BigInteger)(row.get("orderID"))).intValue());
			order.setGameName((String)(row.get("gameName")));
			order.setGameCost((BigDecimal)(row.get("gameCost")));
			order.setSystem((String)(row.get("system_name")));
			order.setOrderCost((Integer)(row.get("orderCost")));
			order.setUsername((String)(row.get("username")));
			order.setActive((Boolean)(row.get("active")));
			order.setDeleted((Boolean)(row.get("deleted")));
			Integer drawID = ((Long)(row.get("drawId"))).intValue();
 System.out.println("******** order.getname() *** "+order.getGameName());
			DrawWrapper draw = (DrawWrapper) this.jdbcTemplate.queryForObject(drawSQL, new Object[] {drawID},
			new RowMapper() {
				
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					DrawWrapper draw = new DrawWrapper();
					draw.setId(rs.getInt("id"));
					draw.setCode(rs.getString("vendorCode"));
					draw.setDate(rs.getString("date"));
					draw.setDay(rs.getString("day"));
					draw.setGameId(rs.getInt("gameId"));
					draw.setJackpot(rs.getBigDecimal("jackpot"));
					return draw;
				}
				
			});
			
			order.setDrawCode(draw.getCode());
			order.setDrawDate(draw.getDate());
			order.setDrawDay(draw.getDay());
			order.setDrawJackpot(draw.getJackpot());
			
			List<Map<String,Object>> dRows = this.jdbcTemplate.queryForList(numbersSQL,new Object[]{row.get("orderID")});
			ArrayList<List<String>> numbers = new ArrayList<List<String>>();
			for (Map dRow: dRows) {
                             System.out.println("********** dRow ************* "+dRow);
				String str = (String)(dRow.get("numbers"));
				List<String> nums = Arrays.asList(str.split("\\s*,\\s*"));
				numbers.add(nums);
			}
			
			order.setNumbers(numbers);
			orders.add(order);
		}
		return orders;
	}
	
	public List<Order> getAll(int userId) {
//		String gameSQL = "SELECT * FROM games";
//		String drawSQL = "SELECT * FROM draws WHERE gameId=?";
		System.out.println("**************getAll(userID :********** )"+userId);
		String ordersSQL = "SELECT orders.order_id AS orderID, games.name as gameName, "
				+ "games.cost as gameCost, draw_id, system_name, orders.cost AS orderCost FROM orders"
                        + " JOIN o_games AS games ON orders.game_id = games.o_game_id WHERE deleted=FALSE AND active=TRUE AND orders.user_id = ?";
		String drawSQL = "SELECT * FROM o_draws AS draws WHERE o_draws_id=?";
		String numbersSQL = "SELECT * FROM orders_numbers WHERE order_id=?";
		
		List<Order> orders = new ArrayList<Order>();
		System.out.println("*********** orders size *********"+orders.size());

		List<Map<String,Object>> rows = this.jdbcTemplate.queryForList(ordersSQL, new Object[]{userId});
		for (Map row: rows) {
                    System.out.println("*********** row *********"+row);
			StandardOrder order = (StandardOrder)OrderFactory.makeOrder(OrderType.STANDARD);
			order.setId(((BigInteger)(row.get("orderID"))).intValue());
			order.setGameName((String)(row.get("gameName")));
			order.setGameCost((BigDecimal)(row.get("gameCost")));
			order.setSystem((String)(row.get("system_name")));
			order.setOrderCost((Integer)(row.get("orderCost")));
			Integer drawID = ((Integer)(row.get("draw_id"))).intValue();

			DrawWrapper draw = (DrawWrapper) this.jdbcTemplate.queryForObject(drawSQL, new Object[] {drawID},
			new RowMapper() {
				
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					DrawWrapper draw = new DrawWrapper();
					draw.setId(rs.getInt("o_draws_id"));
					draw.setCode(rs.getString("vendorCode"));
					draw.setDate(rs.getString("date"));
					draw.setDay(rs.getString("day"));
					draw.setGameId(rs.getInt("o_game_id"));
					draw.setJackpot(rs.getBigDecimal("jackpot"));
					return draw;
				}
				
			});
			
			order.setDrawCode(draw.getCode());
			order.setDrawDate(draw.getDate());
			order.setDrawDay(draw.getDay());
			order.setDrawJackpot(draw.getJackpot());
			
			List<Map<String,Object>> dRows = this.jdbcTemplate.queryForList(numbersSQL,new Object[]{row.get("orderID")});
//			List<PCSODraw> draws = new ArrayList<PCSODraw>();
			ArrayList<List<String>> numbers = new ArrayList<List<String>>();
			for (Map dRow: dRows) {
				String str = (String)(dRow.get("numbers"));
				List<String> nums = Arrays.asList(str.split("\\s*,\\s*"));
//				PCSODraw draw = new PCSODraw();
//				draw.setId((Integer)(dRow.get("id")));
//				draw.setJackpot((BigDecimal)(dRow.get("jackpot")));
//				draw.setDate((String)(dRow.get("date")));
//				draw.setDay((String)(dRow.get("day")));
//				draw.setCode((String)(dRow.get("vendorCode")));
//				draws.add(draw);
				numbers.add(nums);
			}
//			game.setDraws(draws);
//			games.add(game);
			order.setNumbers(numbers);
			orders.add(order);
		}
		return orders;
//		return games;

	}

	public void get() {
		// TODO Auto-generated method stub

	}
	
	public void delete(int id) {
		try {

	        this.jdbcTemplate.update("UPDATE orders SET deleted=TRUE WHERE order_id=?", new Object[] {id});

		} catch (Exception e) {
 
			e.printStackTrace();
			throw new RuntimeException(e);
	 
		} 
	}

}
