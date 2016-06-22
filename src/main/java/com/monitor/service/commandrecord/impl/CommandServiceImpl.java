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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.monitor.dao.account.AccountRepository;
import com.monitor.dao.commandrecord.CommandRecordRepository;
import com.monitor.model.Account;
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
		return false;
	}

	@Override
	public Pager queryCommandRecord(Integer pageNo, Integer pageSize,
			Integer accountId1,String userName,Integer type) throws CodeException {
		try {
			Account operateAccount = accountRepository.findOne(accountId1);
			if (operateAccount.getIsDelete() == 1) {
				throw new CodeException("请重新登录");
			}

			Pager pager = new Pager(pageNo, pageSize);
			int thisPage = (pageNo - 1) * pageSize;
			StringBuilder countSql = new StringBuilder(
					" select count(id) from commandrecord cr where 1=1 ");
			StringBuilder builder = new StringBuilder(
					"select * from commandrecord cr where 1=1");
			if (type!=-1) {
				builder.append("  and cr.type =:type ");
				countSql.append("  and cr.type =:type ");
			}
			if (!StringUtils.isEmpty(userName)) {
				builder.append("  and cr.accountId =:accountId ");
				countSql.append("  and cr.accountId =:accountId ");
			}

			builder.append(" ORDER BY cr.recordTime DESC ");
			builder.append(" limit " + thisPage + "," + pageSize);

			Query query = manager.createNativeQuery(countSql.toString());
			Query queryList = manager.createNativeQuery(builder.toString(),
					CommandRecord.class);
			// String userName = accountRepository.queryUserNameById(accountId);

			if (type!=-1) {
				query.setParameter("type", type);
				queryList.setParameter("type", type);
			}
			if (!StringUtils.isEmpty(userName)) {
				Account account = accountRepository.queryAccountbyuserName(userName);
				Integer accountId = account.getId();
				query.setParameter("accountId", accountId);
				queryList.setParameter("accountId", accountId);
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

		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("获取用户列表出错", e);
			throw new CodeException("内部错误");

		}

	}

}
