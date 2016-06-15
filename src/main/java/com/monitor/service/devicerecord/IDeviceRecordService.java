package com.monitor.service.devicerecord;

import java.util.Date;
import java.util.List;

import com.monitor.exception.CodeException;
import com.monitor.model.DeviceCommunicationError;
import com.monitor.model.DeviceLocationError;
import com.monitor.model.DeviceRecord;
import com.monitor.model.Pager;

public interface IDeviceRecordService {
	/**
	 * 查找设备最新的位置
	 * 
	 * @param accountID
	 * @param devceId
	 */
	public List<DeviceRecord> queryNewlyLocation(int accountId,
			List<Integer> deviceList) throws CodeException;

	public DeviceRecord queryNewlyLocation(int deviceId) throws CodeException;

	/**
	 * 查找设备状态为1的信息
	 * 
	 * @param deviceId
	 * @return
	 * @throws CodeException
	 */
	public List<DeviceRecord> queryAllLocation(int accountId, int deviceId,
			long startTime, long endTime,int type) throws CodeException;

	public Pager queryDeviceHisLocation(int accountId, int deviceId,
			int pageNo, int pageSize) throws CodeException;

	// 显示异常位置设备
	public Pager queryExceptionLocation(Integer pageNo, Integer pageSize,
			Integer accountId) throws CodeException;

	// 修改异常位置设备的操作位
	public void updateOperationType(int accountId, int deviceId,
			long startTime, long endTime) throws CodeException;

	// 获取设备位置危险信息的设备总数
	public int getErrorZLocatonDevice(int accountId);

	/**
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param accountId
	 * @return
	 * @throws CodeException
	 */
	public List<DeviceLocationError> queryAllExceptionLocation(Integer accountId)
			throws CodeException;

	/**
	 * 获取通信异常的设备
	 * 
	 * @param accountId
	 * @return
	 * @throws CodeException
	 */
	public Pager communicationExceptionDevice(int pageNo, int pageSize,
			int accountId) throws CodeException;

	/**
	 * 获取所有24小时内未获得通信的设备
	 * 
	 * @param accountId
	 * @return
	 * @throws CodeException
	 */
	public List<DeviceCommunicationError> queryAllCommunicationExceptionDevice(
			int accountId) throws CodeException;

	/**
	 * 获取通信异常总数
	 * 
	 * @param accountId
	 * @return
	 * @throws CodeException 
	 */

	public int getCommunicationExceptionCount(int accountId) throws CodeException;

}
