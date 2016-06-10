package com.ilts.anywhere.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcSessionDAO {
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	JdbcSessionDAO() {
	}

	public void insert(Session session) {
		// TODO Auto-generated method stub
		try {

	        this.jdbcTemplate.update("INSERT INTO sessions (userId, token, "
	        		+ "expires) VALUES (?, ?, ?)", new Object[] {
	        				session.getUserId(), session.getToken(), session.getExpiration()});

		} catch (Exception e) {
 
			e.printStackTrace();
			throw new RuntimeException(e);
	 
		} 

	}
	
	public void disablePreviousSessions(String userId) {
		try {
			String sql = "UPDATE sessions SET deleted = 1 WHERE userId = ?";
			this.jdbcTemplate.update(sql, new Object[]{userId});
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void getSession(String token) {
		// TODO Auto-generated method stub
		

	}
	
	public boolean validSession(String token) {
		
		String sql = "SELECT COUNT(*) FROM sessions WHERE token = ? AND deleted = 0";
		int rowCount = this.jdbcTemplate.queryForObject(sql, new Object[]{token}, Integer.class);
		
		return (rowCount > 0);
	}
	
	public int getUserId(String token) {
		
		String sql = "SELECT userId FROM sessions WHERE token=? AND deleted = 0";
		int userId = this.jdbcTemplate.queryForObject(sql, new Object[]{token}, Integer.class);
		
		return userId;
	}
	
	public boolean isAdmin(String token) {
		String sql = "SELECT COUNT(*) FROM sessions "
				+ "JOIN user_roles ON user_roles.user_id = sessions.userID "
				+ "WHERE token = ? "
				+ "AND sessions.deleted = 0 "
				+ "AND role_id = 1";
		
		int rowCount = this.jdbcTemplate.queryForObject(sql, new Object[]{token}, Integer.class);
		return (rowCount > 0);
	}

}
