package com.monitor.model;

import java.util.Date;

/**
 * 向客户端发送消息类
 * 
 * @author li
 * 
 */
public class SendMessage {
	private int type;// 0:关机 ，1：开机
	private int updateCRTStatus;// 0:需要更新 1：不需要更新
	private String keyCreateDate; // 会话秘钥创建时间
	private String randomNum; // 随机数
	private String clientCRT;// 客户端证书
	private String clientKey;// 客户端私钥

	public String getKeyCreateDate() {
		return keyCreateDate;
	}

	public void setKeyCreateDate(String keyCreateDate) {
		this.keyCreateDate = keyCreateDate;
	}

	public String getRandomNum() {
		return randomNum;
	}

	public void setRandomNum(String randomNum) {
		this.randomNum = randomNum;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUpdateCRTStatus() {
		return updateCRTStatus;
	}

	public void setUpdateCRTStatus(int updateCRTStatus) {
		this.updateCRTStatus = updateCRTStatus;
	}

	public String getClientCRT() {
		return clientCRT;
	}

	public void setClientCRT(String clientCRT) {
		this.clientCRT = clientCRT;
	}

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

}
