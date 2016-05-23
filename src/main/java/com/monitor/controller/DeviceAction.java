package com.monitor.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.monitor.exception.CodeException;
import com.monitor.exception.ErrorMessage;
import com.monitor.model.Device;
import com.monitor.model.Pager;
import com.monitor.service.device.IDeviceService;

@Controller
@RequestMapping("/device")
public class DeviceAction {
	
	@Autowired
	private IDeviceService deviceService;

	/**
	 * 新增设备接口
	 * 
	 * @param accountId
	 * @param device
	 * @return
	 * @throws CodeException
	 */

	@RequestMapping(value = "/e_add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String addDevice(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestBody Device device) throws CodeException {
		if (StringUtils.isEmpty(device.getDeviceName())) {
			throw new CodeException("设备名不能为空");
		}
		if (device.getValidTime() == null) {
			throw new CodeException("设备名有效期不能为空");

		}
		deviceService.addNewDevice(accountId, device);
		return "success";
	}

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
	 * 更新被控端证书接口
	 * 
	 * @param accountId
	 * @param deviceId
	 * @param status
	 * @return
	 * @throws CodeException
	 */

	@RequestMapping(value = "/e_updateCRT", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String updateCRT(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "deviceId", defaultValue = "0") int deviceId,
			@RequestParam(value = "status", defaultValue = "1") int status)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		if (deviceId == 0) {
			throw new CodeException("请选择要更新的设备");
		}
		deviceService.updateDeviceCRT(accountId, deviceId, status);
		return "success";
	}

	/**
	 * 更新设备有效期
	 * 
	 * @param accountId
	 * @param deviceId
	 * @param modifyDeviceValidTime
	 * @return
	 * @throws CodeException
	 */
	@RequestMapping(value = "/e_updateValidTime", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String updateDeviceValidTime(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "deviceId", defaultValue = "0") int deviceId,
			@RequestParam(value = "modifyDeviceValidTime") Long modifyDeviceValidTime)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		if (deviceId == 0) {
			throw new CodeException("请选择要更新的设备");
		}
		if (modifyDeviceValidTime == null) {
			throw new CodeException("请设置设备新的有效期");
		}
		deviceService.updateValidTime(accountId, deviceId,
				modifyDeviceValidTime);

		return "success";
	}

	@RequestMapping(value = "/e_updateManageStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String updateDeviceManageStatus(
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			@RequestParam(value = "deviceId", defaultValue = "0") int deviceId,
			@RequestParam(value = "status", defaultValue = "1") int status,
			@RequestParam(value = "modifyDeviceValidTime", defaultValue = "0") Long modifyDeviceValidTime,
			@RequestParam(value = "changeType") int changeType)
			throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		if (deviceId == 0) {
			throw new CodeException("请选择要更新的设备");
		}
		if (changeType != 0 && changeType != 1) {
			throw new CodeException("传递参数非法");

		}
		deviceService.updateDeviceManageStatus(accountId, deviceId, changeType,
				status, modifyDeviceValidTime);
		return "success";
	}

	@RequestMapping(value = "/zip/{deviceId}", produces = "application/zip")
	public void getFile(
			@PathVariable("deviceId") int deviceId,
			@RequestParam(value = "accountId", defaultValue = "0") int accountId,
			HttpServletResponse response) throws CodeException {
		if (accountId == 0) {
			throw new CodeException("请重新登录");
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader("Content-Disposition",
				"attachment; filename=\"test.zip\"");
		ByteArrayOutputStream byteArrayOutputStream = deviceService
				.downloadDeviceZipFile(accountId, deviceId);
		try {
			response.getOutputStream().write(
					byteArrayOutputStream.toByteArray());
			response.getOutputStream().flush();
			byteArrayOutputStream.close();

		} catch (IOException e) {
			throw new CodeException("下载文件出错");
		}

	}

	@ExceptionHandler(CodeException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
	public ErrorMessage handleException(CodeException e) {
		return new ErrorMessage(e.getMessage());
	}

}
