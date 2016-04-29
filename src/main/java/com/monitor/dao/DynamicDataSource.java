package com.monitor.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/**
 * 
 * @author dongbing
 * date: 8/11/2014
 */

/**动态切换mysql数据源*/
public class DynamicDataSource extends AbstractRoutingDataSource{
    private static ApplicationContext ac = new ClassPathXmlApplicationContext("spring/context-common-new.xml");
	private static DataSource ds=null;
	
	@Override
	protected Object determineCurrentLookupKey() {
		// TODO Auto-generated method stub
		try {
			ds = (DataSource)ac.getBean("dataSource1");
			ds.getConnection().close();
			return "ds1";
		} catch (SQLException e) {
			// TODO Auto-generated catch block 
			return "ds2";
		}
	} 
}