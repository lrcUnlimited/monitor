package com.monitor.model;

import java.util.Date;


public class DeviceLocationError {
	private Integer deviceId;// 设备Id
	private String deviceName;//设备名字
	private Date startTime;// 设备异常开始时间
	private Date endTime;//设备异常截止时间
	private String lesseeName;//设备租户名称
	private String lesseePhone;//设备租户电话
	private Integer errorNumber;//设备异常次数
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
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
	public Integer getErrorNumber() {
		return errorNumber;
	}
	public void setErrorNumber(Integer errorNumber) {
		this.errorNumber = errorNumber;
	}
	

}
