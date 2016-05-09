package com.monitor.service.account;

import com.monitor.exception.CodeException;
import com.monitor.model.Account;

public interface IAccountService {
	public Account getAccount(String userName, String passWord)
			throws CodeException;

	public boolean saveAccount(Account account) throws CodeException;
}
