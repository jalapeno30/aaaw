package com.ilts.anywhere.games.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilts.anywhere.betting.OBet;
import com.ilts.anywhere.betting.BetFactory;
import com.ilts.anywhere.betting.BetType;
import com.ilts.anywhere.betting.Purchase;
import com.ilts.anywhere.games.Game;
import com.ilts.anywhere.games.JdbcGameDAO;
import com.ilts.anywhere.games.StandardDraw;
import com.ilts.anywhere.games.dao.GameDao;
import com.ilts.anywhere.response.DataResponse;
import com.ilts.anywhere.response.Response;
import com.ilts.anywhere.response.ResponseFactory;
import com.ilts.anywhere.response.ResponseType;
import com.ilts.anywhere.response.http.HttpResponse;
import com.ilts.anywhere.response.http.HttpResponseType;

@Service
@ComponentScan
public class GameService {

	@Autowired
	private JdbcGameDAO jdbcGameDAO;
	
	@Autowired
	private GameDao gameDao;
	
	public List<Game> retrieveGames() {
		return this.jdbcGameDAO.getAllGames();
	}
	
	public Response getDraws() {
		
		// validate token
		
		// get draws
		final List<StandardDraw> allDraws = this.jdbcGameDAO.getDraws();
		Object data = new Object(){
			public List<StandardDraw> draws = allDraws; 
		};
		//DataResponse response = (DataResponse)ResponseFactory.makeResponse(ResponseType.SUCCESSDATA);
		//response.setData(data);
		
		//return response;
            return null;
		
	}
	
	public List<Object> getDrawStatuses() {
		
		return this.jdbcGameDAO.getDrawStatuses();
		
	}
	
	public Response changeStatus(String drawId, String statusId) {
		
		this.jdbcGameDAO.changeStatus(drawId, statusId);
		
		//Response response = ResponseFactory.makeResponse(ResponseType.SUCCESSCHANGESTATUS);
		//return response;
            return null;
		
	}

	public Response createDraw(String jsonRequest) {

		StandardDraw draw = new StandardDraw();

		try {

			ObjectMapper mapper = new ObjectMapper();
			
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			draw = mapper.readValue(jsonRequest, StandardDraw.class);
			System.out.println("********* JSON PARSE **** "+draw.getGameName()+"  ---   "+draw.getStatus());
		} catch (JsonGenerationException e) {
 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}
		
		Integer drawId = this.jdbcGameDAO.createDraw(draw);
		this.changeStatus(drawId.toString(), "3");
		
		//Response response = ResponseFactory.makeResponse(ResponseType.SUCCESSDATA);
		
		//return response;
            return null;
	}

	@Transactional
	public ResponseEntity<Object> testList() {
		Object data = gameDao.list();
		HttpResponse httpResponse = ResponseFactory.makeHttpResponse(HttpResponseType.ACCEPTED, data);
		return httpResponse.getResponseEntity();
	}
	
	@Transactional
	public ResponseEntity<Object> availableGamesByServiceProvider() {
		Object data = gameDao.list();
		HttpResponse httpResponse = ResponseFactory.makeHttpResponse(HttpResponseType.ACCEPTED, data);
		return httpResponse.getResponseEntity();
	}

	@Transactional
	public ResponseEntity<Object> availableDrawsByGame(Integer gameId) {
		Object data = gameDao.availableDrawsByGame(gameId);
		HttpResponse httpResponse = ResponseFactory.makeHttpResponse(HttpResponseType.ACCEPTED, data);
		return httpResponse.getResponseEntity();
	}
	
	@Transactional
	public ResponseEntity<Object> availableGameSystems() {
		Object data = gameDao.listSystems();
		HttpResponse httpResponse = ResponseFactory.makeHttpResponse(HttpResponseType.ACCEPTED, data);
		return httpResponse.getResponseEntity();
	}

}
