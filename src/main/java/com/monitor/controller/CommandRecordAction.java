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
			@RequestParam(value = "type", defaultValue = "") Integer type)
			throws CodeException {
		if (accountId == 0) {  
			throw new CodeException("请重新登录");
		}
		Pager pager = commandService.queryCommandRecord(pageNo, pageSize, accountId, type);
		return pager;

	}
	@RequestMapping(value = "/downcommandrecord")
	public void downcommandfile(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "path", defaultValue = "E:\\record.txt") String path) throws  IOException, CodeException{
			
		if (accountId == 0) {  
			throw new CodeException("请重新登录");
		}
		//取得CommandRecord数据
		List<CommandRecord> record = commandService.getCommandRecord();
		
		try{	
		FileWriter writer = new FileWriter(path,true);
        for(int i = 0;i<record.size();i++) {
            writer.write(record.get(i).toString());
        }       
        writer.close();
		}catch (IOException e1) {
            e1.printStackTrace();
        }
		
	}	
//	@RequestMapping(value = "/downcommandrecord")
//	public ResponseEntity<byte[]> downcommandfile(
//			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
//			@RequestParam(value = "path", defaultValue = "E:\\download\record.txt") String path) throws  IOException, CodeException{
//			
//		if (accountId == 0) {  
//			throw new CodeException("请重新登录");
//		}
//		//取得CommandRecord数据
//		List<CommandRecord> record = new ArrayList();
//		
//		
//		//取得文件名
//		 String fileName = path.substring(path.lastIndexOf("/") + 1);
//		 fileName = URLEncoder.encode(fileName, "UTF-8");
//		//取得文件的后缀名。
//		 String type = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
//		 HttpHeaders headers = new HttpHeaders();
//	        byte[] body = null;
//	        HttpStatus httpState = HttpStatus.NOT_FOUND;
//	        File file = new File(path);
//	        
//	        if (file.exists() && file.isFile()) {
//	 
//	            InputStream is = new FileInputStream(file);
//	            body = new byte[is.available()];
//	            is.read(body);
//	            is.close();
//	            headers.add("Content-Type", type);
//	            headers.add("Content-Length", "" + body.length);
//	            headers.add("Content-Disposition", "attachment;filename=" + fileName);
//	            httpState = HttpStatus.OK;
//	 
//	        }
//	 
//	        ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers,
//	                httpState);
//	 
//	        return entity;
//		
//       


		
		

	

	
	@ExceptionHandler(CodeException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
	public ErrorMessage handleException(CodeException e) {
		return new ErrorMessage(e.getMessage());
	}
}
