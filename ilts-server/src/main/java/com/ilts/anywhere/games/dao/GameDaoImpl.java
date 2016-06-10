package com.ilts.anywhere.games.dao;

import com.ilts.anywhere.AppConfig;

import java.util.List;

import com.ilts.anywhere.games.model.LottoSystem;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.ilts.anywhere.games.model.Draw;
import com.ilts.anywhere.games.model.Game;
import org.springframework.beans.factory.annotation.Autowired;

public class GameDaoImpl implements GameDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public GameDaoImpl() {
	 AppConfig appconfig = new AppConfig();
        sessionFactory = appconfig.getSessionFactory(appconfig.getDataSource());	
	}
	
	public GameDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Game> list() {
		List<Game> listGame = (List<Game>) sessionFactory.getCurrentSession()
				.createCriteria(Game.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
              for(Game g: listGame){
                System.out.println("----list----->  "+g);}
		return listGame;
	}

	@Override
	public List<Draw> availableDrawsByGame(Integer gid) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from Draw draw where draw.game.gameId = :game";
		Query q = session.createQuery(hql);
		q.setParameter("game", gid);
                List<Draw> g = (List<Draw>)q.list();
                for(Draw l: g){
                System.out.println("----availableDrawsByGame---->  "+l);}
		return q.list();
	}

	@Override
	public void save(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Game get(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Draw> getGameDraws(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LottoSystem> listSystems() {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from LottoSystem system order by system.systemNumber";
		Query q = session.createQuery(hql);
                
                for(Object g: q.list()){
                System.out.println("----listSystems----->  "+g);}
		return q.list();
	}

}
