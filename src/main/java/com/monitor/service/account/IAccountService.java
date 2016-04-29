package com.monitor.service.account;

import com.monitor.model.Account;

public interface IAccountService {
	public Account getAccount(String userName, String passWord);
}
