package com.monitor.service.account.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monitor.dao.account.AccountRepository;
import com.monitor.model.Account;
import com.monitor.service.account.IAccountService;

@Service(value = "accountService")
public class AccountServiceImpl implements IAccountService {
	private static Logger logger = Logger.getLogger(AccountServiceImpl.class);
	@Autowired
	private AccountRepository accountRepository;// 账户Repository

	@Override
	public Account getAccount(String userName, String passWord) {
		Account account = accountRepository.loginAccount(userName, passWord);
		logger.info("get account");
		return account;
	}

}
