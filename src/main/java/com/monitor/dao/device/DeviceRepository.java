package com.monitor.dao.device;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monitor.model.Device;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
	

}
  