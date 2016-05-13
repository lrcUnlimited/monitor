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
	private Integer deviceId;// 设备Id
	private String devcieName;// 设备名字
	private Date regTime;// 设备添加时间
	private Date enableTime;// 设备授权时间
	private Integer deviceStatus;// 被控端设备状态，0-开关断开状态，1-开关闭合状态
	private Integer communicationStatus;//通信状态，0-通信断开，1.通信正常连接
	private Date validTime;// 设备有效时间
	private String sessionKey;// 会话秘钥
	private Integer manageDeviceStatus;// 设备管理状态 0-代表关，1-代表开，默认为1
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
	public Integer getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(Integer deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	public Integer getCommunicationStatus() {
		return communicationStatus;
	}
	public void setCommunicationStatus(Integer communicationStatus) {
		this.communicationStatus = communicationStatus;
	}
	public Date getValidTime() {
		return validTime;
	}
	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public Integer getManageDeviceStatus() {
		return manageDeviceStatus;
	}
	public void setManageDeviceStatus(Integer manageDeviceStatus) {
		this.manageDeviceStatus = manageDeviceStatus;
	}

	
}
