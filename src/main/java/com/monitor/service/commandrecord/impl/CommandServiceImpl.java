package com.monitor.service.commandrecord.impl;

import com.monitor.dao.account.AccountRepository;
import com.monitor.dao.commandrecord.CommandRecordRepository;
import com.monitor.exception.CodeException;
import com.monitor.model.Account;
import com.monitor.model.CommandRecord;
import com.monitor.model.Pager;
import com.monitor.service.commandrecord.CommandService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
//				builder.append("  and cr.accountName =:accountName ");
//				countSql.append("  and cr.accountName =:accountName ");
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
//				query.setParameter("accountName", "%" + userName + "%");
//				queryList.setParameter("accountName", "%" + userName + "%");
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

	@Override
	public Pager queryDebug(Integer pageNo, Integer pageSize) {
		List<CommandRecord> resultList = new ArrayList<CommandRecord>();
		Pager pager = new Pager();
		int thisPage = (pageNo - 1) * pageSize;
		
		// 
		StringBuilder commandRecordSql = new StringBuilder("select * from commandrecord where addValidNote = '数据传输测试' limit " + thisPage + "," + pageSize);
		Query queryCommandRecordSql = manager.createNativeQuery(commandRecordSql.toString(), CommandRecord.class);
		resultList = queryCommandRecordSql.getResultList();
		
		pager.setTotalCount(resultList.size());
		pager.setItems(resultList);
		return pager;
	}
}
