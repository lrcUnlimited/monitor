package com.monitor.service.devicerecord.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.monitor.dao.device.DeviceRepository;
import com.monitor.exception.CodeException;
import com.monitor.model.DeviceRecord;
import com.monitor.service.devicerecord.IDeviceRecordService;
@Service(value = "deviceRecordService")
public class DeviceRrecordServiceImpl implements IDeviceRecordService {
	@PersistenceContext
	private EntityManager manager;
	@Autowired
	DeviceRepository deviceRepository;
	private static Logger logger = Logger.getLogger(DeviceRrecordServiceImpl.class);
	@Override
	public DeviceRecord queryNewlyLocation(int deviceId) throws CodeException {
		// TODO Auto-generated method stub
		try {
			
			StringBuilder deviceNewLocation = new StringBuilder(
					"select * from devicerecord where realTime=(select MAX(realTime) from devicerecord WHERE devicerecord.deviceId =:deviceId)");
			
			Query query = manager.createNativeQuery(deviceNewLocation.toString(),DeviceRecord.class);
			if (!StringUtils.isEmpty(deviceId)) {
				query.setParameter("deviceId", deviceId);
				
			}
			DeviceRecord deviceRecord = (DeviceRecord) query.getSingleResult();
			deviceRecord.setDeviceName(deviceRepository.queryDeviceNameById(deviceRecord.getDeviceId()));
			
			return deviceRecord;
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取设备最新位置出错", e);
			throw new CodeException("内部错误");
		}
		
	}

	@Override
	public List<DeviceRecord> queryAllLocation(int deviceId)
			throws CodeException {
		// TODO Auto-generated method stub
		try {
			StringBuilder deviceAllLocation = new StringBuilder(
					"select * from devicerecord where devicerecord.deviceId=:deviceId and devicerecord.locationStatus=1");
			deviceAllLocation.append(" ORDER BY devicerecord.realTime");
			Query query = manager.createNativeQuery(deviceAllLocation.toString(),DeviceRecord.class);
			
			if (!StringUtils.isEmpty(deviceId)) {
				query.setParameter("deviceId", deviceId);
			}
			@SuppressWarnings("unchecked")
			List<DeviceRecord> list = query.getResultList();
			for(DeviceRecord deviceRecord : list){
				deviceRecord.setDeviceName(deviceRepository.queryDeviceNameById(deviceRecord.getDeviceId()));		
			}
			return list;
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取设备最历史地点出错", e);
			throw new CodeException("内部错误");
		}
	
	}
		

}
