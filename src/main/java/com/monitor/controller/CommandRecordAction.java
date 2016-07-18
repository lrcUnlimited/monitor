package com.monitor.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.monitor.exception.CodeException;
import com.monitor.exception.ErrorMessage;
import com.monitor.model.CommandRecord;
import com.monitor.model.Pager;
import com.monitor.service.commandrecord.CommandService;

@Controller
@RequestMapping("/commandrecord")
public class CommandRecordAction {
	@Autowired
	CommandService commandService;
	
	@RequestMapping(value = "/e_query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Pager queryUser(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "searchUser", defaultValue = "") String userName,
			@RequestParam(value = "searchType", defaultValue = "-1") Integer type)
			throws CodeException {
		if (accountId == 0) {  
			throw new CodeException("请重新登录");
		}
		Pager pager = commandService.queryCommandRecord(pageNo, pageSize, accountId, userName,type);
		return pager;

	}
	
	@ExceptionHandler(CodeException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
	public ErrorMessage handleException(CodeException e) {
		return new ErrorMessage(e.getMessage());
	}
	
	
	@RequestMapping(value = "/e_debug", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Pager debugInfo(@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize
			)
			throws CodeException {
		if (accountId == 0) {  
			throw new CodeException("请重新登录");
		}
		Pager pager = commandService.queryDebug(pageNo, pageSize);
		return pager;
	}
}
