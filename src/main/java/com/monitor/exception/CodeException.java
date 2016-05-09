package com.monitor.exception;

/**
 * 
 * @Description 自定义异常
 * @Author clsu
 * @Date 2014-2-26 上午10:26:50 
 * @CodeReviewer zwwang
 */
public class CodeException extends Exception {
	private static final long serialVersionUID = 8724252889850188629L;


	private String mess;

	public CodeException() {
	}

	public CodeException(String mess) {
		this.mess = mess;
	}

	public String getMessage() {
		return mess;
	}

}
