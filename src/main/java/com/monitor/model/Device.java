package com.monitor.model;

import java.util.Date;

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
	private String deviceName;// 设备名字
	private String lesseeName;//设备租用方名称
	private String lesseePhone;//设备租用方电话
	private Date regTime;// 设备添加时间
	private Date enableTime;// 设备授权时间
	private Integer deviceStatus;// 被控端设备状态，0-开关断开状态，1-开关闭合状态
	private Integer communicationStatus;// 通信状态，0-通信断开，1.通信正常连接
	private Date validTime;// 设备有效时间
	private String sessionKey;// 会话秘钥
	private int updateCRT;// 是否需要更新证书文件 0-代表不需要更新 1-代表需要更新
	private Integer manageDeviceStatus;// 设备管理状态 0-代表关，1-代表开，默认为1
	private Integer arrearageCount;//客户每次被强制自动关机则不良行为记数+1，将所有用户按照不良行为记录数进行图表统计显示；默认为0；
	private String provice;//设备所在省份
	private String city;//设备所在城市
	private String district;//设备所在区域
	
	private String MachineID;
	
	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getLesseeName() {
		return lesseeName;
	}

	public Integer getArrearageCount() {
		return arrearageCount;
	}

	public void setArrearageCount(Integer arrearageCount) {
		this.arrearageCount = arrearageCount;
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

	public int getUpdateCRT() {
		return updateCRT;
	}

	public void setUpdateCRT(int updateCRT) {
		this.updateCRT = updateCRT;
	}

	public String getMachineID() {
		return MachineID;
	}

	public void setMachineID(String machineID) {
		MachineID = machineID;
	}

}
