package com.monitor.model;

/**
 * socket接受客户端消息类
 * 
 * @author AlanJager
 * 
 */
public class MessageV2 {
	private int deviceId;// 被控端id
	private String ID;// 机器码
	private double longitude;// 经度

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public int getDwType() {
        return dwType;
    }

    public void setDwType(int dwType) {
        this.dwType = dwType;
    }

    public int getCurrentStatus() {
        return CurrentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        CurrentStatus = currentStatus;
    }

    private double latitude;// 纬度
	private int dwType; //暂不适用1:心跳 2:注册
	private int CurrentStatus;// 受控端状态 0-代表关机 1-代表开机


}
