package com.ilts.anywhere.authentication;

import com.ilts.anywhere.authentication.model.Session;

public interface SessionDAO {

	public void insert(Session session);
	public void disablePreviousSessions(Session session);
	public void getSession(String token);
	public boolean validSession(String token);
	public int getUserId(String token);
	public boolean isAdmin(String token);

}
