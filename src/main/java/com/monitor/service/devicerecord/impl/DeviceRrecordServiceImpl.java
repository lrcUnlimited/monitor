package com.monitor.service.devicerecord.impl;

import java.util.ArrayList;
import java.util.Date;
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
	private static Logger logger = Logger
			.getLogger(DeviceRrecordServiceImpl.class);

	@Override
	public DeviceRecord queryNewlyLocation(int deviceId) throws CodeException {
		// TODO Auto-generated method stub
		try {

			StringBuilder deviceNewLocation = new StringBuilder(
					"select * from devicerecord where deviceRecordId=(select MAX(deviceRecordId) from devicerecord WHERE devicerecord.deviceId =:deviceId)");

			Query query = manager.createNativeQuery(
					deviceNewLocation.toString(), DeviceRecord.class);
			if (!StringUtils.isEmpty(deviceId)) {
				query.setParameter("deviceId", deviceId);
			}
			@SuppressWarnings("unchecked")
			List<DeviceRecord> list = (List<DeviceRecord>) query
					.getResultList();
			if (list == null || list.size() == 0) {
				return null;
			}
			DeviceRecord deviceRecord = list.get(0);
			deviceRecord.setDeviceName(deviceRepository
					.queryDeviceNameById(deviceRecord.getDeviceId()));

			return deviceRecord;

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取设备最新位置出错", e);
			throw new CodeException("内部错误");
		}

	}

	@Override
	public List<DeviceRecord> queryAllLocation(int deviceId, long startTime,
			long endTime) throws CodeException {
		// TODO Auto-generated method stub
		try {
			Date startDate;
			Date endDate;
			if (startTime == 0) {
				startDate = new Date(0);
			} else {
				startDate = new Date(startTime);
			}
			if (endTime == 0) {
				endDate = new Date();
			} else {
				endDate = new Date(endTime + 86400000);
			}

			StringBuilder deviceAllLocation = new StringBuilder(
					"select * from devicerecord where devicerecord.deviceId=:deviceId and devicerecord.locationStatus=1 and devicerecord.realTime>=:startTime and devicerecord.realTime<=:endTime ");
			deviceAllLocation.append(" ORDER BY devicerecord.realTime");
			Query query = manager.createNativeQuery(
					deviceAllLocation.toString(), DeviceRecord.class);

			if (!StringUtils.isEmpty(deviceId)) {
				query.setParameter("deviceId", deviceId);
				query.setParameter("startTime", startDate);
				query.setParameter("endTime", endDate);
			}
			@SuppressWarnings("unchecked")
			List<DeviceRecord> list = query.getResultList();
			for (DeviceRecord deviceRecord : list) {
				deviceRecord.setDeviceName(deviceRepository
						.queryDeviceNameById(deviceRecord.getDeviceId()));
			}
			return list;

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取设备最历史地点出错", e);
			throw new CodeException("内部错误");
		}

	}

	@Override
	public List<DeviceRecord> queryNewlyLocation(List<Integer> deviceList)
			throws CodeException {
		List<DeviceRecord> list = new ArrayList<DeviceRecord>();
		for (Integer i : deviceList) {
			DeviceRecord record = queryNewlyLocation(i);
			if (record != null) {
				list.add(record);
			}
		}
		return list;
	}
}
