package com.monitor.model;

import java.sql.Date;

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
	@Transient
	private String deviceName;

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
