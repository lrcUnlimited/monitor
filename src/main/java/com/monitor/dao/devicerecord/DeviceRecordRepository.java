package com.monitor.dao.devicerecord;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monitor.model.DeviceRecord;

public interface DeviceRecordRepository extends JpaRepository<DeviceRecord, Integer> {

}
