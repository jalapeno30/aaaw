package com.ilts.anywhere.games.controller;

//import java.util.concurrent.atomic.AtomicLong;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.ilts.anywhere.authentication.AuthenticationService;
import com.ilts.anywhere.games.Game;
import com.ilts.anywhere.games.service.GameService;
import com.ilts.anywhere.response.Response;


@Controller
public class GamesController {
	
//	private GameService gameService = new GameService();
	@Autowired
	private GameService gameService;

	@Autowired
	private AuthenticationService authService;

	/**
	* Retrieve a list of all available games provided through this service provider. 
	* Assumption is that using a service provider they will be able to receive their winnings through a 
	* physical location or that using a service provider is somewhat beneficial to them.
	**/
	@RequestMapping("/games/availableGamesByServiceProvider")
	public @ResponseBody List<Game> games(
			HttpServletResponse response) {
		 System.out.println("****** /games/availableGamesByServiceProvider ***********");
		return gameService.retrieveGames();
	}

	/**
	* To retrieve the draw details for a particular game
	**/
	@RequestMapping("/games/retrieveDrawStatus")
	public @ResponseBody Object drawStatus(
			HttpServletResponse response) {
 System.out.println("********/games/retrieveDrawStatus*********");
		return new Object() {};
	}

	/**
	* When a user logs into the My Account/My games page, a list will be shown that will display
	* all available games for the user, a section for results (ended games) and the current games/bets 
	* that are in progress.
	**/
	@RequestMapping("/games/retrieveGameStatus")
	public @ResponseBody Object gameStatus(
			HttpServletResponse response) {
 System.out.println("**** /games/retrieveGameStatus *****");
		return new Object() {};
	}
	
	@RequestMapping("/games/getDraws")
	public @ResponseBody Response getDraws(
			@RequestParam(value="gameId", required=false) String gameId,
			HttpServletResponse response) {
		 System.out.println("****/games/getDraws ********** ");
		return this.gameService.getDraws();
	}
	
	/**
	 * return list of draws statuses
	 */
	@RequestMapping("/games/drawsStatusesList")
	public @ResponseBody ResponseEntity<List<Object>> getDrawStatuses(
			HttpServletResponse response) {
		 System.out.println("****** /games/drawsStatusesList ************");
		return new ResponseEntity<List<Object>>(this.gameService.getDrawStatuses(), HttpStatus.OK);
		
	}
	
	/**
	 * 
	 * @param userId
	 * @param statusId
	 * @param response
	 * @return ResponseEntity
	 */
	@RequestMapping("/games/changeDrawStatus")
	public @ResponseBody ResponseEntity<Response> changeDrawStatus(
			@RequestParam(value="drawId", required=true) String drawId,
			@RequestParam(value="statusId", required=true) String statusId,
			HttpServletResponse response) {
		 System.out.println("***** /games/changeDrawStatus*********");
		Response status = this.gameService.changeStatus(drawId, statusId);
		return new ResponseEntity<Response>(status, HttpStatus.OK);
	}
	
	@RequestMapping("/games/createDraw")
	public @ResponseBody ResponseEntity<Response> createDraw(
			@RequestParam(value="token", required = true) String token,
			@RequestBody String jsonRequest,
			HttpServletResponse response) {
             System.out.println("******/games/createDraw*************");
		
		if (!authService.validSession(token)) {
			return new ResponseEntity<Response>(HttpStatus.UNAUTHORIZED);
		} else {
			if (!authService.isAdmin(token)) {
				return new ResponseEntity<Response>(HttpStatus.FORBIDDEN);
			} else {
				int userId = authService.getUserId(token);
				Response resp = gameService.createDraw(jsonRequest);
                                System.out.println("************ json Request   __ : "+jsonRequest);
				return new ResponseEntity<Response>(resp, HttpStatus.OK);
			}
		}
		
	}
	
	@RequestMapping("/games")
	public @ResponseBody ResponseEntity<Object> availableGamesByServiceProvider(
			HttpServletResponse response) {
             System.out.println("************* /game **************");
		return gameService.availableGamesByServiceProvider();
	}
	
	@RequestMapping("/games/{gameId}/draws/available")
	public @ResponseBody ResponseEntity<Object> availableDrawsByGame(
			@PathVariable("gameId") Integer gameId,
			HttpServletResponse response) {
             System.out.println(" *** /games/{gameId}/draws/available*********");
		return gameService.availableDrawsByGame(gameId);
	}
	
	@RequestMapping("/games/systems")
	public @ResponseBody ResponseEntity<Object> availableDrawsByGame(
			HttpServletResponse response) {
             System.out.println("****/games/systems*********");
		return gameService.availableGameSystems();
	}
}