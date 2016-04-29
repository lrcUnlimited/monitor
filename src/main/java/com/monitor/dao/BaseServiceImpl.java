package com.monitor.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;


public class BaseServiceImpl extends JdbcDaoSupport implements IBaseService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	private static Logger logger = Logger.getLogger(BaseServiceImpl.class);

	//	@Override
	//	public <T> void doPager(PageInfo<T> pi) {
	//		// TODO Auto-generated method stub
	//		
	//	}

	@Override
	public <T> void persist(T t) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void update(T t) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T findById(Class<T> classFrame, Serializable oid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void remove(T t) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void remove(T t, Boolean isCascade)
			throws IllegalArgumentException, IllegalAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> List<T> findByNamedQuery(String queryName, Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findByProperty(Class<T> entityClass,
			String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	//	@Override
	//	public int getCount(Class enClass, Condition... conditions) {
	//		// TODO Auto-generated method stub
	//		return 0;
	//	}

}
