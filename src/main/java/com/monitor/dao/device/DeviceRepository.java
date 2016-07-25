package com.monitor.dao.device;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.monitor.model.Account;
import com.monitor.model.Device;

@Transactional
@Repository
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
	 * 修改设备的arrearageCount，使其+1
	 * @param deviceId
	 * @return
	 */
	@Modifying
	@Query("update Device device set device.arrearageCount=device.arrearageCount+1 where device.deviceId=?1")
	public int updateArrearageCount(int deviceId);


	/**
	 * 更新设备的管理状态
	 *
	 * @param status
	 * @param deviceId
	 * @return
	 */
	@Modifying
	@Query("update Device device set device.manageDeviceStatus=?1 where device.deviceId=?2")
	public int updateManageDeviceStatus(int status, int deviceId);

	/**
	 * 更新设备的管理状态
	 *
	 * @param status
	 * @param deviceId
	 * @return
	 */
	@Modifying
	@Query("update Device device set device.closeTime=?1 where device.deviceId=?2")
	public int updateDeviceShallCloseTime(String closeTime, int deviceId);


	/**
	 * 更新是否需要更新证书状态位
	 * 
	 * @param deviceId
	 * @param status
	 *            0:代表不需要更新 1：需要更新
	 * @return
	 */
	@Modifying
	@Query("update Device device set device.updateCRT=?1 where device.deviceId=?2")
	public int updateDeviceCRTStatus(int status, int deviceId);

	/**
	 * 更新设备有效时间
	 * 
	 * @param date
	 * @param deviceId
	 * @return
	 */
	@Modifying
	@Query("update Device device set device.validTime=?1 where device.deviceId=?2")
	public int updateDeviceValidTime(Date date, int deviceId);

	/**
	 * 同时更新设备状态和有效期
	 * 
	 * @param status
	 * @param validTime
	 * @param deviceId
	 * @return
	 */
	@Modifying
	@Query("update Device device set device.manageDeviceStatus=?1, device.validTime=?2 where device.deviceId=?3")
	public int updateDeviceMDStatusAndValidTime(int status, Date validTime,
			int deviceId);

	@Query("select deviceName from Device device where device.deviceId=?1")
	public String queryDeviceNameById(Integer deviceId);

	/**
	 * 更新数据库中设备的状态
	 * 
	 * @param status
	 * @param deviceId
	 * @return
	 */
	@Modifying
	@Query("update Device device set device.deviceStatus=?1 where device.deviceId=?2")
	public int updateDeviceStatus(int status, int deviceId);
	/**
	 * 更改设备名中的状态位
	 * @param status
	 * @param deviceId
	 */
	@Modifying
	@Query("update Device device set device.communicationStatus=?1 where device.deviceId=?2")
	public void updateCommunicationStatus0(int status, int deviceId);
	/**
	 * 更新设备的所在省份
	 * @param provice
	 * @param deviceId
	 */
	@Modifying
	@Query("update Device device set device.provice=?1 where device.deviceId=?2")
	public void updateDeviceProvice(String provice, int deviceId);
	@Modifying
	@Query("update Device device set device.city=?1 where device.deviceId=?2")
	public void updateDeviceCity(String city, int deviceId);
	@Modifying
	@Query("update Device device set device.district=?1 where device.deviceId=?2")
	public void updateDeviceDistrict(String district, int deviceId);
}
