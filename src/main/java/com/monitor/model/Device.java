package com.monitor.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "device")
public class Device {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer deviceId;//设备Id
	private String devcieName;//设备名字
	private Date regTime;//设备添加时间
	private Date enableTime;//设备授权时间
	private Integer status;//设备状态，0-断开状态，1-正常运行状态，2-代表欠费
	private Date validTime;//设备有效时间
	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}
	public String getDevcieName() {
		return devcieName;
	}
	public void setDevcieName(String devcieName) {
		this.devcieName = devcieName;
	}
	public Date getRegTime() {
		return regTime;
	}
	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	public Date getEnableTime() {
		return enableTime;
	}
	public void setEnableTime(Date enableTime) {
		this.enableTime = enableTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getValidTime() {
		return validTime;
	}
	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}
	
	

}
