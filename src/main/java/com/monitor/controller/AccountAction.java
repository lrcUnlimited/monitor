package com.monitor.controller;

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
			@RequestParam(value = "userName", defaultValue = "") String userName)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		Pager pager = accountService.queryUser(pageNo, pageSize, accountId,
				userName);
		return pager;

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
			throw new CodeException("用户名不能为空");
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

	@ExceptionHandler(CodeException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
	public ErrorMessage handleException(CodeException e) {
		return new ErrorMessage(e.getMessage());
	}
}
