package com.monitor.service.account.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.monitor.dao.account.AccountRepository;
import com.monitor.dao.commandrecord.CommandRecordRepository;
import com.monitor.exception.CodeException;
import com.monitor.model.Account;
import com.monitor.model.CommandRecord;
import com.monitor.model.Pager;
import com.monitor.service.account.IAccountService;

@Service(value = "accountService")
public class AccountServiceImpl implements IAccountService {
	private static Logger logger = Logger.getLogger(AccountServiceImpl.class);
	@Autowired
	private AccountRepository accountRepository;// 账户Repository
	@Autowired
	private CommandRecordRepository commandRecordRepository;

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Account getAccount(String userName, String passWord)
			throws CodeException {
		Account account = accountRepository.loginAccount(userName, passWord);
		try {
			if (account != null) {
				return account;
			} else {
				throw new CodeException("用户名或密码错误");
			}
		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("内部错误", e);
			throw new CodeException("内部错误");

		}
	}

	@Override
	public boolean saveAccount(int accountId, Account account)
			throws CodeException {
		try {
			Account operateAccount = accountRepository.findOne(accountId);
			if (operateAccount.getIsDelete() == 1
					|| operateAccount.getType() == 0) {
				throw new CodeException("请重新登录");
			}
			account.setRegisterDate(new Date());
			accountRepository.save(account);
			// 保存命令记录
			CommandRecord commandRecord = new CommandRecord();
			commandRecord.setAccountId(accountId);
			commandRecord.setRecordTime(new Date());
			commandRecord.setType(0);
			commandRecord.setContent("添加了新的用户: " + account.getUserName());
			commandRecordRepository.save(commandRecord);
			return true;
		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("添加用户出错", e);
			throw new CodeException("内部错误");
		}
	}

	@Override
	public Pager queryUser(Integer pageNo, Integer pageSize, Integer accountId,
			String userName) throws CodeException {
		try {
			Account account = accountRepository.findOne(accountId);
			if (account.getType() == 1 && account.getIsDelete() == 0) {
				Pager pager = new Pager(pageNo, pageSize);
				int thisPage = (pageNo - 1) * pageSize;
				StringBuilder countSql = new StringBuilder(
						" select count(id) from account account "
								+ " where 1=1 ");
				StringBuilder builder = new StringBuilder(
						"select * from account account where 1=1");
				builder.append("  and account.isDelete=0 ");
				countSql.append("  and account.isDelete=0 ");
				if (accountId != 0) {
					builder.append("  and account.id !=:id ");
					countSql.append("  and account.id !=:id ");
				}

				if (!StringUtils.isEmpty(userName)) {
					builder.append("  and account.userName =:userName ");
					countSql.append("  and account.userName =:userName ");
				}

				builder.append(" ORDER BY account.registerDate DESC ");
				builder.append(" limit " + thisPage + "," + pageSize);

				Query query = manager.createNativeQuery(countSql.toString());
				Query queryList = manager.createNativeQuery(builder.toString(),
						Account.class);
				if (accountId != 0) {
					query.setParameter("id", accountId);
					queryList.setParameter("id", accountId);
				}
				if (!StringUtils.isEmpty(userName)) {
					query.setParameter("userName", userName);
					queryList.setParameter("userName", userName);
				}
				pager.setTotalCount(((BigInteger) query.getSingleResult())
						.intValue());
				@SuppressWarnings("unchecked")
				List<Account> list = queryList.getResultList();
				pager.setItems(list);
				return pager;

			} else {
				throw new CodeException("请重新登录");
			}

		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("获取用户列表出错", e);
			throw new CodeException("内部错误");

		}

	}

	@Override
	public void updateAccountInfo(int accountId, Account account)
			throws CodeException {
		Account adminAccount = accountRepository.findOne(accountId);
		try {
			if (adminAccount.getType() == 0 || adminAccount.getIsDelete() == 1) {
				throw new CodeException("请重新登录");
			} else {
				accountRepository.updateUserInfo(account.getUserName(),
						account.getUserPhone(), account.getNote(),
						account.getPassWord(), account.getType(),
						account.getId());

				// 保存命令记录
				CommandRecord commandRecord = new CommandRecord();
				commandRecord.setAccountId(accountId);
				commandRecord.setRecordTime(new Date());
				commandRecord.setType(0);
				commandRecord.setContent("修改了用户ID为: " + account.getId()
						+ "的个人信息");
				commandRecordRepository.save(commandRecord);
			}
		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("内部错误", e);
			throw new CodeException("内部错误");

		}
	}

	@Override
	public Account account(int accountId) throws CodeException {
		try {
			Account account = accountRepository.findOne(accountId);
			if (account.getIsDelete() == 1) {
				throw new CodeException("请重新登录");
			}
			return account;
		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("获取个人信息失败", e);
			throw new CodeException("获取用户个人信息失败");
		}
	}

	@Override
	public void deleteAccount(int operateAccountId, int delAccountId)
			throws CodeException {
		try {
			Account operateAccount = accountRepository
					.findOne(operateAccountId);
			if (operateAccountId == delAccountId) {
				throw new CodeException("不能删除自己");

			}
			if (operateAccount.getIsDelete() == 1
					|| operateAccount.getType() == 0) {
				throw new CodeException("请重新登录");
			}
			accountRepository.deleteAccount(delAccountId);
		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("删除用户出错", e);
		}
	}

	@Override
	public void updatePersonalInfo(int accountId, Account account)
			throws CodeException {
		Account personalAccount = accountRepository.findOne(accountId);
		try {
			if (personalAccount.getIsDelete() == 1
					|| accountId != account.getId()) {
				throw new CodeException("请重新登录");
			} else {
				accountRepository.updateUserInfo(account.getUserName(),
						account.getUserPhone(), account.getNote(),
						account.getPassWord(), personalAccount.getType(),
						account.getId());

				// 保存命令记录
				CommandRecord commandRecord = new CommandRecord();
				commandRecord.setAccountId(accountId);
				commandRecord.setRecordTime(new Date());
				commandRecord.setType(0);
				commandRecord.setContent("修改了用户ID为: " + account.getId()
						+ "的个人信息");
				commandRecordRepository.save(commandRecord);
			}
		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("内部错误", e);
			throw new CodeException("内部错误");

		}

	}
}
