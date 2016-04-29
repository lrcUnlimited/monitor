package com.monitor.dao;

import java.io.Serializable;
import java.util.List;

public interface IBaseService extends Serializable {
	
	

	/**
	 * 分页
	 */
//	public <T> void doPager(final PageInfo<T> pi);

	/**
	 * 保存一个对象
	 */
	public <T> void persist(T t);

	/**
	 * 修改一个对象
	 */
	public <T> void update(T t);

	/**
	 * 根据OID查找对象
	 */
	public <T> T findById(Class<T> classFrame, Serializable oid);

	/**
	 * 删除一个对象
	 */
	public <T> void remove(T t);

	/**
	 * 删除一个对象
	 */
	public <T> void remove(T t, Boolean isCascade)
			throws IllegalArgumentException, IllegalAccessException;

	/**
	 * 命名hql查询
	 */
	public <T> List<T> findByNamedQuery(String queryName, Object... obj);

	/**
	 * 根据类的属性进行查询
	 * 
	 * @param <T>
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> List<T> findByProperty(Class<T> entityClass,
			String propertyName, Object value);

	/**
	 * 根据条件查询结果条数
	 * 
	 * @param enClass
	 * @param conditions
	 * @return
	 */
//	int getCount(Class enClass, Condition... conditions);
	
}
