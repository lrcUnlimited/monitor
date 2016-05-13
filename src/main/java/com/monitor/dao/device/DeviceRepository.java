package com.monitor.dao.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.monitor.model.Device;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
	/**
	 * 更新设备的sessionKey
	 * 
	 * @param deviceSessionKey
	 * @param deviceId
	 * @return
	 */
	@Modifying
	@Query("update Device device set device.sessionKey=?1 where device.deviceId=?2")
	public int updateDeviceSessionKey(String deviceSessionKey, int deviceId);

	/**
	 * 更新设备的管理状态
	 * 
	 * @param status
	 * @param deviceId
	 * @return
	 */
	@Query("update Device device set device.manageDeviceStatus=?1 where device.deviceId=?2")
	public int updateManageDeviceStatus(int status, int deviceId);

}
  
