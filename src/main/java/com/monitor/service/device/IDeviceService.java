package com.monitor.service.device;

import java.util.Date;

import com.monitor.exception.CodeException;
import com.monitor.model.Device;
import com.monitor.model.Pager;

public interface IDeviceService {
	/**
	 * 增加新的设备
	 * 
	 * @param accountId
	 * @param device
	 * @throws CodeException
	 */
	public void addNewDevice(int accountId, Device device) throws CodeException;

	/**
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param accountId
	 * @param type
	 *            0:正常设备 1：即将过期设备 2：关闭设备
	 * @return
	 * @throws CodeException
	 */
	public Pager queryDevice(Integer pageNo, Integer pageSize,
			Integer accountId, int type) throws CodeException;

	/**
	 * 更新设备是否需要更新证书状态位
	 * 
	 * @param accountId
	 * @param deviceId
	 * @param status
	 * @throws CodeException
	 */
	public void updateDeviceCRT(int accountId, int deviceId, int status)
			throws CodeException;

	/**
	 * 更新设备有效时间
	 * 
	 * @param accountId
	 * @param deviceId
	 * @param newValidTime
	 * @throws CodeException
	 */
	public void updateValidTime(int accountId, int deviceId, long newValidTime)
			throws CodeException;

	/**
	 * 更新设备的状态
	 * 
	 * @param accountId
	 * @param deviceId
	 * @param changeType
	 * @param status
	 * @param newValidTime
	 * @throws CodeException 
	 */

	public void updateDeviceManageStatus(int accountId, int deviceId,
			int changeType, int status, long newValidTime) throws CodeException;

}
