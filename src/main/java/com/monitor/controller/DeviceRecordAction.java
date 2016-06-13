package com.monitor.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.monitor.exception.CodeException;
import com.monitor.exception.ErrorMessage;
import com.monitor.model.DeviceLocationError;
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
	 * 
	 * @param accountId
	 * @param deviceId
	 * @return
	 * @throws CodeException
	 */
	@RequestMapping(value = "/e_querylocation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	List<DeviceRecord> devicerecord(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "deviceList", defaultValue = "") List<Integer> deviceList)

	throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		if (deviceList == null || deviceList.size() == 0) {
			throw new CodeException("请至少选择一个设备进行查看");
		}

		List<DeviceRecord> deviceRecord = deviceRecordService
				.queryNewlyLocation(accountId, deviceList);
		return deviceRecord;

	}

	@RequestMapping(value = "/e_queryhistory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	List<DeviceRecord> queryAllDevlceRecord(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "deviceId", defaultValue = "0") int deviceId,
			@RequestParam(value = "startTime", defaultValue = "0") long startTime,
			@RequestParam(value = "endTime", defaultValue = "0") long endTime)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		if (deviceId == 0) {
			throw new CodeException("设备名错误");
		}
		List<DeviceRecord> list = deviceRecordService.queryAllLocation(
				accountId, deviceId, startTime, endTime);
		return list;

	}

	@RequestMapping(value = "/e_queryallhistory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Pager queryAllDeviceRecordById(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "deviceId", defaultValue = "0") int deviceId,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		if (deviceId == 0) {
			throw new CodeException("设备名错误");
		}
		Pager pager = deviceRecordService.queryDeviceHisLocation(accountId,
				deviceId, pageNo, pageSize);
		return pager;

	}

	@RequestMapping(value = "/e_queryallErrorDevice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Pager queryallErrorDeviceRecordById(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		Pager pager = deviceRecordService.queryExceptionLocation(pageNo,
				pageSize, accountId);
		return pager;
	}

	@RequestMapping(value = "/e_queryallErPositionDevice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	List<DeviceLocationError> queryallErrorDeviceRecord(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		return deviceRecordService.queryAllExceptionLocation(accountId);
	}

	@RequestMapping(value = "/e_queryErrorDeviceCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	int queryErrorDeviceCountById(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		int errordeviceCount = deviceRecordService
				.getErrorZLocatonDevice(accountId);
		return errordeviceCount;
	}

	@RequestMapping(value = "/e_updateOperation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String updateOperation(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "deviceId", defaultValue = "0") int deviceId,
			@RequestParam(value = "startTime", defaultValue = "0") long startTime,
			@RequestParam(value = "endTime", defaultValue = "0") long endTime)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		deviceRecordService.updateOperationType(accountId, deviceId, startTime,
				endTime);

		return "success";

	}

	@ExceptionHandler(CodeException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
	public ErrorMessage handleException(CodeException e) {
		return new ErrorMessage(e.getMessage());
	}

}
