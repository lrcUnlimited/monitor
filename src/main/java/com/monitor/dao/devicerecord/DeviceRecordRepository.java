package com.monitor.dao.devicerecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.monitor.model.DeviceRecord;

@Transactional
@Repository
public interface DeviceRecordRepository extends
		JpaRepository<DeviceRecord, Integer> {

}
