package com.monitor.model;

/**
 * socket发送消息类
 * 
 * @author li
 * 
 */
public class Message {
	private String deviceId;// 被控端id
	private String sessionKey;// 会话密钥
	private double longitude;// 经度
	private double latitude;// 纬度
	private int type;// 发送命令 0-代表允许开机 1-代表关机
	private int updateSessionKeyStatus;// 是否需要更新sessionkey，0代表不需要更新，1代表需要更新
	private int updateCRTStatus;// 是否需要更新证书文件,0不需要更新，1代表需要更新

	public int getUpdateSessionKeyStatus() {
		return updateSessionKeyStatus;
	}

	public void setUpdateSessionKeyStatus(int updateSessionKeyStatus) {
		this.updateSessionKeyStatus = updateSessionKeyStatus;
	}

	public int getUpdateCRTStatus() {
		return updateCRTStatus;
	}

	public void setUpdateCRTStatus(int updateCRTStatus) {
		this.updateCRTStatus = updateCRTStatus;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
