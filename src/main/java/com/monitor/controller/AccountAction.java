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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.monitor.exception.CodeException;
import com.monitor.exception.ErrorMessage;
import com.monitor.model.Account;
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

	@RequestMapping(value = "/e_add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String add(@RequestBody Account account) throws CodeException {

		if (StringUtils.isEmpty(account.getUserName())) {
			throw new CodeException("用户名不能为空");
		}
		if (StringUtils.isEmpty(account.getPassWord())) {
			throw new CodeException("密码不能为空");
		}
		accountService.saveAccount(account);
		return "success";
	}

	@ExceptionHandler(CodeException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
	public ErrorMessage handleException(CodeException e) {
		return new ErrorMessage(e.getMessage());
	}
}
