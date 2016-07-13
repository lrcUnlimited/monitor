package com.monitor.model;

public class DeviceArrearagePercentage {
	private float percantage;
	private String lessee;
	private int arrearageDeviceNum;
	private int normalDeviceNum;
	
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
}
