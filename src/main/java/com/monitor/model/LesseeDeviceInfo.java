package com.monitor.model;

public class LesseeDeviceInfo {
	private String lesseeName; //设备租用方名称
	private int totalDeviceNum; //设备总数
	private int normalDeviceNum; //正常设备数
	private int arrearageDeviceNum; //欠费设备数
	private int willArrearageDeviceNum; //即将欠费设备数
	
	public String getLesseeName() {
		return lesseeName;
	}
	public void setLesseeName(String lesseeName) {
		this.lesseeName = lesseeName;
	}
	public int getTotalDeviceNum() {
		return totalDeviceNum;
	}
	public void setTotalDeviceNum(int totalDeviceNum) {
		this.totalDeviceNum = totalDeviceNum;
	}
	public int getNormalDeviceNum() {
		return normalDeviceNum;
	}
	public void setNormalDeviceNum(int normalDeviceNum) {
		this.normalDeviceNum = normalDeviceNum;
	}
	public int getArrearageDeviceNum() {
		return arrearageDeviceNum;
	}
	public void setArrearageDeviceNum(int arrearageDeviceNum) {
		this.arrearageDeviceNum = arrearageDeviceNum;
	}
	public int getWillArrearageDeviceNum() {
		return willArrearageDeviceNum;
	}
	public void setWillArrearageDeviceNum(int willArrearageDeviceNum) {
		this.willArrearageDeviceNum = willArrearageDeviceNum;
	}
}
