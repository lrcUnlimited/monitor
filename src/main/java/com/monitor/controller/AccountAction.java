package com.monitor.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.monitor.exception.CodeException;
import com.monitor.model.Account;
import com.monitor.service.account.IAccountService;

@Controller
@RequestMapping("/user")
public class AccountAction {
	@Autowired
	IAccountService accountService;

	// 首页
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "login";
	}

	@RequestMapping(value = "/e_login", method = RequestMethod.POST)
	public String loginSubmit(HttpServletRequest request, HttpSession session,
			ModelMap map, Account input) throws CodeException {
		Account account = accountService.getAccount(input.getUserName(),
				input.getPassWord());
		if (account != null) {
			return "login";

		}

		return null;

	}
}
