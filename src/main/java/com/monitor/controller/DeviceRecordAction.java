package com.monitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.monitor.exception.CodeException;
import com.monitor.model.DeviceRecord;
import com.monitor.model.Pager;
import com.monitor.service.device.IDeviceService;
import com.monitor.service.devicerecord.IDeviceRecordService;

@Controller
@RequestMapping("/devicerecord")
public class DeviceRecordAction {
	
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IDeviceRecordService deviceRecordService;
	
	/**
	 * 分页查询设备信息接口
	 * 
	 * @param accountId
	 * @param pageNo
	 * @param pageSize
	 * @param type
	 * @return
	 * @throws CodeException
	 */
	@RequestMapping(value = "/e_query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Pager queryUser(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "type", defaultValue = "0") int type)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		Pager pager = deviceService.queryDevice(pageNo, pageSize, accountId,
				type);
		return pager;

	}

	/**
	 * 设备当前最新位置
	 * @param accountId
	 * @param deviceId
	 * @return
	 * @throws CodeException
	 */
	@RequestMapping(value = "/e_querylocation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	DeviceRecord devicerecord(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "deviceId", defaultValue = "0") int deviceId)
			
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		if (deviceId == 0) {
			throw new CodeException("设备名错误");
		}
		DeviceRecord  deviceRecord=deviceRecordService.queryNewlyLocation(deviceId);
		return deviceRecord;
		
	}
	
	@RequestMapping(value = "/e_queryhistory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	List<DeviceRecord> queryAllDevlceRecord(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "deviceId", defaultValue = "0") int deviceId)
			
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		if (deviceId == 0) {
			throw new CodeException("设备名错误");
		}
		List<DeviceRecord>  list=deviceRecordService.queryAllLocation(deviceId);
		return list;
		
	}

}
