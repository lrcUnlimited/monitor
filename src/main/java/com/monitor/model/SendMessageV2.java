package com.monitor.model;

public class SendMessageV2 {
	public int getDwType() {
		return dwType;
	}

	public void setDwType(int dwType) {
		this.dwType = dwType;
	}

	public int getTurnOnOff() {
		return TurnOnOff;
	}

	public void setTurnOnOff(int turnOnOff) {
		TurnOnOff = turnOnOff;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public int getUpdateSystem() {
		return UpdateSystem;
	}

	public void setUpdateSystem(int updateSystem) {
		UpdateSystem = updateSystem;
	}

	public String getUpdateFile() {
		return updateFile;
	}

	public void setUpdateFile(String updateFile) {
		this.updateFile = updateFile;
	}

	public String getSetOnOffTime() {
		return SetOnOffTime;
	}

	public void setSetOnOffTime(String setOnOffTime) {
		SetOnOffTime = setOnOffTime;
	}

	public String getRecordTime() {
		return RecordTime;
	}

	public void setRecordTime(String recordTime) {
		RecordTime = recordTime;
	}

	private int dwType; //暂不使用 1:心跳, 2:注册
	private int TurnOnOff;// 0:关机 ，1：开机
	private String ID;
	private int UpdateSystem; //0：不更新受控设备程序 1:更新受控设备程序
	private String updateFile;
	private String SetOnOffTime;
	private String RecordTime;

}
