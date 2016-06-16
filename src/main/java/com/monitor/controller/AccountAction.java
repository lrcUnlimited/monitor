package com.monitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.monitor.dao.account.AccountRepository;
import com.monitor.exception.CodeException;
import com.monitor.exception.ErrorMessage;
import com.monitor.model.Account;
import com.monitor.model.Pager;
import com.monitor.service.account.IAccountService;

@Controller
@RequestMapping("/user")
public class AccountAction {
	@Autowired
	IAccountService accountService;
	@Autowired
	AccountRepository accountRepository;

	@RequestMapping(value = "/e_login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Account login(@RequestBody Account account) throws CodeException {

		if (StringUtils.isEmpty(account.getUserName())) {
			throw new CodeException("用户名不能为空");
		}
		if (StringUtils.isEmpty(account.getPassWord())) {
			throw new CodeException("密码不能为空");
		}
		Account loginAccount = accountService.getAccount(account.getUserName(),
				account.getPassWord());
		return loginAccount;
	}

	@RequestMapping(value = "/e_query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Pager queryUser(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "searchUserName", defaultValue = "") String userName,
			@RequestParam(value = "searchUserPhone", defaultValue = "") String userPhone,
			@RequestParam(value = "startSearchDate", defaultValue = "0") long startTime,
			@RequestParam(value = "endSearchDate", defaultValue = "0") long endTime)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		Pager pager = accountService.queryUser(pageNo, pageSize, accountId,
				userName, userPhone, startTime, endTime);
		return pager;

	}

	@RequestMapping(value = "/e_queryAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	List<Account> queryAllUser(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "searchUserName", defaultValue = "") String userName,
			@RequestParam(value = "searchUserPhone", defaultValue = "") String userPhone,
			@RequestParam(value = "startSearchDate", defaultValue = "0") long startTime,
			@RequestParam(value = "endSearchDate", defaultValue = "0") long endTime)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}

		return accountService.getAllAccount(accountId, userName, userPhone,
				startTime, endTime);

	}

	/**
	 * 用户名唯一性校验
	 * 
	 * @param accountId
	 * @param userName
	 * @return
	 * @throws CodeException
	 */
	@RequestMapping(value = "/e_queryUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Account queryUserExist(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "userName", defaultValue = "") String userName)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		Account account = accountRepository.queryAccountbyuserName(userName);

		return account;

	}

	@RequestMapping(value = "/e_getInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Account getUserInfo(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		Account account = accountService.account(accountId);

		return account;

	}

	@RequestMapping(value = "/e_add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String add(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestBody Account account) throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		if (StringUtils.isEmpty(account.getUserName())) {
			throw new CodeException("用户名不能为空或者用户名输入错误");
		}
		if (StringUtils.isEmpty(account.getPassWord())) {
			throw new CodeException("密码不能为空");
		}
		if (StringUtils.isEmpty(account.getUserPhone())) {
			throw new CodeException("手机号不能为空");

		}
		accountService.saveAccount(accountId, account);
		return "success";
	}

	/**
	 * 修改其他人信息
	 * 
	 * @param accountId
	 * @param account
	 * @return
	 * @throws CodeException
	 */
	@RequestMapping(value = "/e_update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String update(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestBody Account account) throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		if (StringUtils.isEmpty(account.getId())) {
			throw new CodeException("获取不到用户id");

		}
		if (StringUtils.isEmpty(account.getUserName())) {
			throw new CodeException("用户名不能为空");
		}

		if (StringUtils.isEmpty(account.getUserPhone())) {
			throw new CodeException("手机号不能为空");
		}
		accountService.updateAccountInfo(accountId, account);
		return "success";
	}

	/**
	 * 修改个人信息
	 * 
	 * @param accountId
	 * @param account
	 * @return
	 * @throws CodeException
	 */
	@RequestMapping(value = "/e_updatePersonal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String updatePersonalInfo(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestBody Account account) throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		if (StringUtils.isEmpty(account.getId())) {
			throw new CodeException("获取不到用户id");

		}
		if (StringUtils.isEmpty(account.getUserName())) {
			throw new CodeException("用户名不能为空");
		}

		if (StringUtils.isEmpty(account.getUserPhone())) {
			throw new CodeException("手机号不能为空");
		}
		accountService.updatePersonalInfo(accountId, account);
		return "success";
	}

	@ExceptionHandler(CodeException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
	public ErrorMessage handleException(CodeException e) {
		return new ErrorMessage(e.getMessage());
	}

	@RequestMapping(value = "/e_delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String deleteUser(
			@RequestParam(value = "accountId", defaultValue = "0") int operateAccountId,
			@RequestParam(value = "delAccountId", defaultValue = "0") int delAccountId)
			throws CodeException {
		if (operateAccountId == 0) {
			throw new CodeException("请重新登录");
		}
		if (delAccountId == 0) {
			throw new CodeException("请选择要删除的用户");

		}
		accountService.deleteAccount(operateAccountId, delAccountId);

		return "success";

	}
}
