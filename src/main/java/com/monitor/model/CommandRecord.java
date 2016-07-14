package com.monitor.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "commandrecord")
public class CommandRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer Id;//命令记录id
	private Integer accountId;//命令操作用户id
	private Integer deviceId;//操作设备Id
	private String lesseeName;//关闭设备租赁商
	private Integer deviceCloseType;//欠费设备关闭记录位,默认为0，被关闭记录位1
	private Date recordTime;//命令操作时间
	private String content;//命令内容
	private Integer type;//命令类型-0代表用户管理操作，1代表授权操作、暂时没用，2代表设备管理操作，3代表用户远程更新操作,4代表充值，即增加设备使用期限
	@Transient
	private String accountName;//命令操作用户名
	public String getLesseeName() {
		return lesseeName;
	}
	public void setLesseeName(String lesseeName) {
		this.lesseeName = lesseeName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}
	public Integer getDeviceCloseType() {
		return deviceCloseType;
	}
	public void setDeviceCloseType(Integer deviceCloseType) {
		this.deviceCloseType = deviceCloseType;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public Date getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	

}
