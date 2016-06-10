package com.ilts.anywhere.games.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ilts.anywhere.games.model.Draw;
import com.ilts.anywhere.games.model.Game;

@Component
public interface GameDao {
	public List<Game> list();
	
	public void save(Game game);
	
	public void delete(Integer id);
	
	public Game get(Integer id);
	
	public List<Draw> getGameDraws(Integer id);

	public Object availableDrawsByGame(Integer gameId);

	public Object listSystems();
}
