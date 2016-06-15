package com.monitor.model;

import java.util.Date;

public class DeviceCommunicationError {
	private Integer deviceId;// 设备Id
	private String deviceName;// 设备名字
	private Date lastCommunicateTime;// 最后设备通信时间
	private String lesseeName;// 设备租户名称
	private String lesseePhone;// 设备租户电话
	private Date validTime; // 设备有效期
	private Date regTime; // 设备录入时间

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Date getLastCommunicateTime() {
		return lastCommunicateTime;
	}

	public void setLastCommunicateTime(Date lastCommunicateTime) {
		this.lastCommunicateTime = lastCommunicateTime;
	}

	public String getLesseeName() {
		return lesseeName;
	}

	public void setLesseeName(String lesseeName) {
		this.lesseeName = lesseeName;
	}

	public String getLesseePhone() {
		return lesseePhone;
	}

	public void setLesseePhone(String lesseePhone) {
		this.lesseePhone = lesseePhone;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

}
