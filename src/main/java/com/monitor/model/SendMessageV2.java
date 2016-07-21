package com.monitor.model;

public class SendMessageV2 {
	private int dwtype; //暂不使用 1:心跳, 2:注册
	private int TurnOnOff;// 0:关机 ，1：开机
	private String MachineID;
	private int UpdateSystem; //0：不更新受控设备程序 1:更新受控设备程序
	private String updateFile;

	public int getDwtype() {
		return dwtype;
	}
	public void setDwtype(int dwtype) {
		this.dwtype = dwtype;
	}
	public int getTurnOnOff() {
		return TurnOnOff;
	}
	public void setTurnOnOff(int turnOnOff) {
		TurnOnOff = turnOnOff;
	}
	public String getMachineID() {
		return MachineID;
	}
	public void setMachineID(String machineID) {
		MachineID = machineID;
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
}
