package com.monitor.model;

public class DeviceArrearagePercentage {
	private float percantage;//欠费率
	private String lessee;//租赁商名称
	private int arrearageDeviceNum;//欠费设备数
	private int normalDeviceNum;//正常设备数
	private String lesseePhone;//设备租用方电话
	private int arrearagePercentageType;//
	
	public float getPercantage() {
		return percantage;
	}
	public void setPercantage(float percantage) {
		this.percantage = percantage;
	}
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
}
