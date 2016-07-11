package com.monitor.model;

import java.util.Date;

public class DeviceStatus {
	private Integer onDeviceNum;//打开状态的设备数

	private Integer offDeviceNum;//关闭状态的设备数
	private String province;
	
	public Integer getOnDeviceNum() {
		return onDeviceNum;
	}
	public void setOnDeviceNum(Integer onDeviceNum) {
		this.onDeviceNum = onDeviceNum;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public Integer getOffDeviceNum() {
		return offDeviceNum;
	}
	public void setOffDeviceNum(Integer offDeviceNum) {
		this.offDeviceNum = offDeviceNum;
	}
}
