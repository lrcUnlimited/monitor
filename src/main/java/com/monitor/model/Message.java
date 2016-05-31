package com.monitor.model;

import java.util.Date;

/**
 * socket接受客户端消息类
 * 
 * @author li
 * 
 */
public class Message {
	private int deviceId;// 被控端id
	private String keyCreateDate;// 会话秘钥生成时间
	private String randomNum; // 会话秘钥随机数
	private double longitude;// 经度
	private double latitude;// 纬度
	private int type;// 发送命令 0-代表开机 1-代表关机

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

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

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
