package com.monitor.model;

public class DeviceArrearagePercentage {
	private float percentage;//欠费率
	private String lessee;//租赁商名称
	private int arrearageDeviceNum;//欠费设备数
	private int normalDeviceNum;//正常设备数
	private String lesseePhone;//设备租用方电话
	private int arrearagePercentageType;//
	private int totalDeviceNum;//设备总数数
	private float arrearageTimePerDevice;//积累台欠费次
	
	public String getLessee() {
		return lessee;
	}
	public void setLessee(String lessee) {
		this.lessee = lessee;
	}
	public int getArrearageDeviceNum() {
		return arrearageDeviceNum;
	}
	public void setArrearageDeviceNum(int arrearageDeviceNum) {
		this.arrearageDeviceNum = arrearageDeviceNum;
	}
	public int getNormalDeviceNum() {
		return normalDeviceNum;
	}
	public void setNormalDeviceNum(int normalDeviceNum) {
		this.normalDeviceNum = normalDeviceNum;
	}
	public String getLesseePhone() {
		return lesseePhone;
	}
	public void setLesseePhone(String lesseePhone) {
		this.lesseePhone = lesseePhone;
	}
	public int getArrearagePercentageType() {
		return arrearagePercentageType;
	}
	public void setArrearagePercentageType(int arrearagePercentageType) {
		this.arrearagePercentageType = arrearagePercentageType;
	}
	public float getPercentage() {
		return percentage;
	}
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
	public int getTotalDeviceNum() {
		return totalDeviceNum;
	}
	public void setTotalDeviceNum(int totalDeviceNum) {
		this.totalDeviceNum = totalDeviceNum;
	}
	public float getArrearageTimePerDevice() {
		return arrearageTimePerDevice;
	}
	public void setArrearageTimePerDevice(float arrearageTimePerDevice) {
		this.arrearageTimePerDevice = arrearageTimePerDevice;
	}

}
