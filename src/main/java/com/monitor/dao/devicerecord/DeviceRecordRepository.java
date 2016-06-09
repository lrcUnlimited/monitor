package com.monitor.dao.devicerecord;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.monitor.model.DeviceRecord;

@Transactional
@Repository
public interface DeviceRecordRepository extends
		JpaRepository<DeviceRecord, Integer> {
	/**
	 * 修改设备记录表的一场未知信息操作位
	 * @param deviceSessionKey
	 * @param deviceId
	 * @return
	 */
	@Modifying
	@Query("update DeviceRecord deviceRecord set deviceRecord.operationType=1 where deviceRecord.deviceId=?1 and deviceRecord.realTime>=?2 and deviceRecord.realTime<=?3")
	public int updateDeviceRecordOpeRationType(int deviceId,Date startTime,Date endTime);
	
}
