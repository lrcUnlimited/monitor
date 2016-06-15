package com.monitor.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "devicerecord")
public class DeviceRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer deviceRecordId;// 设备记录id,自增
	private Integer deviceId;// 设备Id
	private Double longitude;// 设备当前经度
	private Double latitude;// 设备当前纬度
	private Date realTime;// 设备当前时间
	private int status;// 设备当前状态
	private int locationStatus;// 设备位置变化状态位，默认为0（相邻的两个位置点距离超过500m，将该状态位设为1）
	private int operationType;// 设备异常位置操作，默认为0（0代表显示位置异常信息，1代表操作确认过）
	@Transient
	private String deviceName;
	@Transient
	private Date validTime;// 设备有效期

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	public int getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(int locationStatus) {
		this.locationStatus = locationStatus;
	}

	public Integer getDeviceRecordId() {
		return deviceRecordId;
	}

	public void setDeviceRecordId(Integer deviceRecordId) {
		this.deviceRecordId = deviceRecordId;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Date getRealTime() {
		return realTime;
	}

	public void setRealTime(Date realTime) {
		this.realTime = realTime;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
