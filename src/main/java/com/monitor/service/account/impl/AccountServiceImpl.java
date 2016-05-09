package com.monitor.service.account.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monitor.dao.account.AccountRepository;
import com.monitor.exception.CodeException;
import com.monitor.model.Account;
import com.monitor.service.account.IAccountService;

@Service(value = "accountService")
public class AccountServiceImpl implements IAccountService {
	private static Logger logger = Logger.getLogger(AccountServiceImpl.class);
	@Autowired
	private AccountRepository accountRepository;// 账户Repository

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
			logger.error("内部错误" + e);
			throw new CodeException("内部错误");

		}
	}

	@Override
	public boolean saveAccount(Account account) throws CodeException {
		try {
			accountRepository.save(account);
			return true;
		} catch (Exception e) {
			logger.error("添加用户出错", e);
			throw new CodeException("内部错误");
		}
	}

}
