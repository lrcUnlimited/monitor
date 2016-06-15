package com.monitor.service.account;

import com.monitor.exception.CodeException;
import com.monitor.model.Account;
import com.monitor.model.Pager;

public interface IAccountService {
	public Account getAccount(String userName, String passWord)
			throws CodeException;

	/**
	 * 增加用户
	 * 
	 * @param accountId
	 *            ：操作人id
	 * @param account
	 * @return
	 * @throws CodeException
	 */
	public boolean saveAccount(int accountId, Account account)
			throws CodeException;

	/**
	 * 获取用户列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param accountId
	 * @return
	 * @throws CodeException
	 */
	public Pager queryUser(Integer pageNo, Integer pageSize, Integer accountId,
			String userName) throws CodeException;

	/**
	 * 修改用户信息
	 * 
	 * @param accountId
	 * @param account
	 * @return
	 * @throws CodeException
	 */
	public void updateAccountInfo(int accountId, Account account)
			throws CodeException;

	public Account account(int accountId) throws CodeException;

	public void deleteAccount(int operateAccountId, int delAccountId)
			throws CodeException;

	/**
	 * 修改个人信息
	 * 
	 * @param accountId
	 * @param account
	 * @throws CodeException
	 */
	public void updatePersonalInfo(int accountId, Account account)
			throws CodeException;

	/**
	 * 退出系统
	 * 
	 * @param accountId
	 * @throws CodeException
	 */
	public void logout(int accountId) throws CodeException;

}
