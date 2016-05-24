package com.monitor.service.devicerecord;

import java.util.Date;
import java.util.List;

import com.monitor.exception.CodeException;
import com.monitor.model.DeviceRecord;

public interface IDeviceRecordService {
	/**
	 * 查找设备最新的位置
	 * 
	 * @param accountID
	 * @param devceId
	 */
	public List<DeviceRecord> queryNewlyLocation(List<Integer> deviceList) throws CodeException;
	/**
	 * 查找设备状态为1的信息
	 * @param deviceId
	 * @return
	 * @throws CodeException
	 */
	public List<DeviceRecord> queryAllLocation(int deviceId,long startTime, long endTime) throws CodeException;
}
