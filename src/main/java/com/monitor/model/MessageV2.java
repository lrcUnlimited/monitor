package com.monitor.model;

/**
 * socket接受客户端消息类
 * 
 * @author AlanJager
 * 
 */
public class MessageV2 {
	private int deviceId;// 被控端id
	private String MachineID;// 会话秘钥生成时间
	private double longitude;// 经度
	private double latitude;// 纬度
	private int dwtype; //暂不适用1:心跳 2:注册
	private int currentStatus;// 受控端状态 0-代表关机 1-代表开机

	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public String getMachineID() {
		return MachineID;
	}
	public void setMachineID(String machineID) {
		MachineID = machineID;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public int getDwtype() {
		return dwtype;
	}
	public void setDwtype(int dwtype) {
		this.dwtype = dwtype;
	}
	public int getCurrentStatus(){return this.currentStatus; }
	public void setCurrentStatus(int currentStatus){ this.currentStatus = currentStatus; }
}
