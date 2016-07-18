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
import com.monitor.util.MD5Util;

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
		String encryptPassWord = MD5Util.getMD5Str(passWord);
		Account account = accountRepository.loginAccount(userName,
				encryptPassWord);

		try {
			if (account != null) {
				CommandRecord commandRecord = new CommandRecord();
				commandRecord.setAccountId(account.getId());
				commandRecord.setRecordTime(new Date());
				StringBuffer content = new StringBuffer();
				content.append("用户:").append(userName).append("登录系统");
				commandRecord.setContent(content.toString());
				commandRecord.setType(0);
				commandRecordRepository.save(commandRecord);
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
			account.setPassWord(MD5Util.getMD5Str(account.getPassWord()));
			account.setRegisterDate(new Date());
			accountRepository.save(account);
			// 保存命令记录
			CommandRecord commandRecord = new CommandRecord();
			commandRecord.setAccountId(accountId);
			commandRecord.setRecordTime(new Date());
			commandRecord.setType(0);
			commandRecord.setContent("注册新用户: " + "("+account.getUserName()+")");
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
			String userName,String userPhone,long startTime,long endTime) throws CodeException {
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
					builder.append("  and account.userName like:userName ");
					countSql.append("  and account.userName like:userName ");
				}
				if(!StringUtils.isEmpty(userPhone)){
					builder.append("  and account.userPhone like:userPhone ");
					countSql.append("  and account.userPhone like:userPhone ");
				}
				if(startTime!=0){
					builder.append("  and account.registerDate>=:startDate ");
					countSql.append("  and account.registerDate>=:startDate ");
				}
				if(endTime!=0){
					builder.append("  and account.registerDate<=:endDate ");
					countSql.append("  and account.registerDate<=:endDate ");
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
					query.setParameter("userName", '%' + userName + '%');
					queryList.setParameter("userName", '%' + userName + '%');
				}
				if(!StringUtils.isEmpty(userPhone)){
					query.setParameter("userPhone", '%' + userPhone + '%');
					queryList.setParameter("userPhone", '%' + userPhone + '%');
				}
				if(startTime!=0){
					Date startDate=new Date(startTime);
					query.setParameter("startDate", startDate);
					queryList.setParameter("startDate", startDate);
				}
				if(endTime!=0){
					Date endDate=new Date(endTime);
					query.setParameter("endDate", endDate);
					queryList.setParameter("endDate", endDate);
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
				if (account.getPassWord() == null
						|| account.getPassWord().length() == 0) {
					accountRepository.updateUserInfoNoPassWord(
							account.getUserName(), account.getUserPhone(),
							account.getNote(), account.getType(),
							account.getId());

				} else {
					String md5PassWord = MD5Util.getMD5Str(account
							.getPassWord());
					accountRepository.updateUserInfo(account.getUserName(),
							account.getUserPhone(), account.getNote(),
							md5PassWord, account.getType(), account.getId());
				}

				// 保存命令记录
				CommandRecord commandRecord = new CommandRecord();
				commandRecord.setAccountId(accountId);
				commandRecord.setRecordTime(new Date());
				commandRecord.setType(0);
				commandRecord.setContent("修改用户: " +"("+ account.getUserName()+")"
						+ "个人信息");
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
			
			// 保存命令记录
			CommandRecord commandRecord = new CommandRecord();
			Account delAccount = accountRepository.findOne(delAccountId);
			commandRecord.setAccountId(operateAccountId);
			commandRecord.setRecordTime(new Date());
			commandRecord.setType(0);
			commandRecord.setContent("删除用户:"+"(" + delAccount.getUserName()+")"
					+ "个人信息");
			commandRecordRepository.save(commandRecord);
			//删除设备
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
				String md5PassWord = MD5Util.getMD5Str(account.getPassWord());

				accountRepository.updateUserInfo(account.getUserName(),
						account.getUserPhone(), account.getNote(), md5PassWord,
						personalAccount.getType(), account.getId());

				// 保存命令记录
				CommandRecord commandRecord = new CommandRecord();
				commandRecord.setAccountId(accountId);
				commandRecord.setAccountName(account.getUserName());
				commandRecord.setRecordTime(new Date());
				commandRecord.setType(0);
				commandRecord.setContent("修改用户:"+"(" + account.getUserName()+")"
						+ "个人信息");
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
	public void logout(int accountId) throws CodeException {
		try {
			Account account = accountRepository.findOne(accountId);
			if (account == null) {
				throw new CodeException("不存在该用户");
			}
			// 保存命令记录
			CommandRecord commandRecord = new CommandRecord();
			commandRecord.setAccountId(accountId);
			commandRecord.setRecordTime(new Date());
			commandRecord.setType(0);
			commandRecord.setContent("用户: " + account.getUserName() + "退出系统");
			commandRecordRepository.save(commandRecord);

		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("删除用户出错", e);
		}
	}

	@Override
	public List<Account> getAllAccount(int accountId,String userName,String userPhone,long startTime,long endTime) throws CodeException {
		try {
			Account account = accountRepository.findOne(accountId);
			if (account.getType() == 1 && account.getIsDelete() == 0) {

				StringBuilder builder = new StringBuilder(
						"select * from account account where 1=1");
				builder.append("  and account.isDelete=0 ");
				if (accountId != 0) {
					builder.append("  and account.id !=:id ");
				}
				if (!StringUtils.isEmpty(userName)) {
					builder.append("  and account.userName =:userName ");
				}
				if(!StringUtils.isEmpty(userPhone)){
					builder.append("  and account.userPhone=:userPhone ");
				}
				if(startTime!=0){
					builder.append("  and account.registerDate>=:startDate ");
				}
				if(endTime!=0){
					builder.append("  and account.registerDate<=:endDate ");
				}
				builder.append(" ORDER BY account.registerDate DESC ");
				Query queryList = manager.createNativeQuery(builder.toString(),
						Account.class);
				if (accountId != 0) {
					queryList.setParameter("id", accountId);
				}
				if (!StringUtils.isEmpty(userName)) {
					queryList.setParameter("userName", userName);
				}
				if(!StringUtils.isEmpty(userPhone)){
					queryList.setParameter("userPhone", userPhone);
				}
				if(startTime!=0){
					Date startDate=new Date(startTime);
					queryList.setParameter("startDate", startDate);
				}
				if(endTime!=0){
					Date endDate=new Date(endTime);
					queryList.setParameter("endDate", endDate);
				}
				@SuppressWarnings("unchecked")
				List<Account> list = queryList.getResultList();
				return list;

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
}
