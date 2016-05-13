package com.monitor.model;

import java.util.Date;

/**
 * 会话密钥
 * 
 * @author li
 * 
 */
public class SessionKey {
	private Date createDate;// 会话秘钥生成时间
	private String randomNum;// 会话秘钥随机数

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRandomNum() {
		return randomNum;
	}

	public void setRandomNum(String randomNum) {
		this.randomNum = randomNum;
	}

}
