package com.monitor.service.commandrecord.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.monitor.exception.CodeException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.monitor.dao.account.AccountRepository;
import com.monitor.dao.commandrecord.CommandRecordRepository;
import com.monitor.model.CommandRecord;
import com.monitor.model.Pager;
import com.monitor.service.commandrecord.CommandService;

@Service(value = "commandService")
public class CommandServiceImpl implements CommandService {
	private static Logger logger = Logger.getLogger(CommandServiceImpl.class);
	@Autowired
	private CommandRecordRepository commandRecordRepository;// 命令记录Repository
	@Autowired
	private AccountRepository accountRepository;// 账户Repository/
	@PersistenceContext
	private EntityManager manager;

	@Override
	public boolean saveCommandRecord(int accountId, CommandRecord commandRecord)
			throws CodeException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Pager queryCommandRecord(Integer pageNo, Integer pageSize,
			Integer accountId, Integer type) throws CodeException {
		// TODO Auto-generated method stub
		try {

			Pager pager = new Pager(pageNo, pageSize);
			int thisPage = (pageNo - 1) * pageSize;
			StringBuilder countSql = new StringBuilder(
					" select count(id) from commandrecord cr " + " where 1=1 ");
			StringBuilder builder = new StringBuilder(
					"select * from commandrecord cr where 1=1");
			if (!StringUtils.isEmpty(type)) {
				builder.append("  and cr.type =:type ");
				countSql.append("  and cr.type =:type ");
			}

			builder.append(" ORDER BY cr.recordTime DESC ");
			builder.append(" limit " + thisPage + "," + pageSize);

			Query query = manager.createNativeQuery(countSql.toString());
			Query queryList = manager.createNativeQuery(builder.toString(),
					CommandRecord.class);
//			String userName = accountRepository.queryUserNameById(accountId);

			if (!StringUtils.isEmpty(type)) {
				query.setParameter("type", type);
				queryList.setParameter("type", type);
			}
			pager.setTotalCount(((BigInteger) query.getSingleResult())
					.intValue());
			@SuppressWarnings("unchecked")
			List<CommandRecord> list = queryList.getResultList();
			for (CommandRecord commandRecord : list) {
				commandRecord.setAccountName(accountRepository
						.queryUserNameById(commandRecord.getAccountId()));
			}

			pager.setItems(list);
			return pager;

		} catch (Exception e) {
			logger.error("获取用户列表出错", e);
			throw new CodeException("内部错误");

		}

	}

	@Override
	public List<CommandRecord> getCommandRecord() {
		// TODO Auto-generated method stub
		StringBuilder commandSql = new StringBuilder(
				" select * from commandrecord ");
		Query queryList = manager.createNativeQuery(commandSql.toString(),
				CommandRecord.class);
		@SuppressWarnings("unchecked")
		List<CommandRecord> list = queryList.getResultList();
		
		return list;
	}

}
