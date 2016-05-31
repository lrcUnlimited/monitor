package com.monitor.service.device.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monitor.dao.account.AccountRepository;
import com.monitor.dao.commandrecord.CommandRecordRepository;
import com.monitor.dao.device.DeviceRepository;
import com.monitor.exception.CodeException;
import com.monitor.model.Account;
import com.monitor.model.CommandRecord;
import com.monitor.model.Device;
import com.monitor.model.Pager;
import com.monitor.service.device.IDeviceService;

@Service(value = "deviceService")
public class DeviceServiceImpl implements IDeviceService {
	private static Logger logger = Logger.getLogger(DeviceServiceImpl.class);
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private CommandRecordRepository comRecordRepository;
	@Autowired
	private AccountRepository accountRepository;

	@PersistenceContext
	private EntityManager manager;

	@Override
	public void addNewDevice(int accountId, Device device) throws CodeException {
		try {
			// 添加新的设备
			device.setDeviceStatus(0);
			device.setCommunicationStatus(0);
			device.setManageDeviceStatus(1);
			device.setRegTime(new Date());
			Device newDevice = deviceRepository.save(device);
			

			// 保存到命令记录中
			CommandRecord commandRecord = new CommandRecord();
			commandRecord.setAccountId(accountId);
			commandRecord.setType(2);
			commandRecord.setRecordTime(new Date());
			commandRecord.setContent("添加了新的设备：" + device.getDeviceName());
			comRecordRepository.save(commandRecord);
		} catch (Exception e) {
			logger.error("保存设备出错", e);
			throw new CodeException("新增设备出错");

		}
	}

	@Override
	public Pager queryDevice(Integer pageNo, Integer pageSize,
			Integer accountId, int type) throws CodeException {
		try {

			Pager pager = new Pager(pageNo, pageSize);
			int thisPage = (pageNo - 1) * pageSize;
			StringBuilder countSql = new StringBuilder(
					" select count(deviceId) from device device "
							+ " where 1=1 ");
			StringBuilder builder = new StringBuilder(
					"select * from device device where 1=1");
			if (type == 0) {
				builder.append("  and device.manageDeviceStatus =1 and device.validTime >DATE_ADD(now(),INTERVAL 3 DAY) ");
				countSql.append(" and device.manageDeviceStatus =1 and device.validTime >DATE_ADD(now(),INTERVAL 3 DAY) ");
			} else if (type == 1) {
				builder.append("  and device.manageDeviceStatus =1 and device.validTime <=DATE_ADD(now(),INTERVAL 3 DAY) ");
				countSql.append(" and device.manageDeviceStatus =1 and device.validTime <=DATE_ADD(now(),INTERVAL 3 DAY) ");
			} else if (type == 2) {
				builder.append("  and device.manageDeviceStatus =0 ");
				countSql.append("  and device.manageDeviceStatus =0 ");
			}

			builder.append(" ORDER BY device.validTime ASC ");
			builder.append(" limit " + thisPage + "," + pageSize);

			Query query = manager.createNativeQuery(countSql.toString());
			Query queryList = manager.createNativeQuery(builder.toString(),
					Device.class);

			pager.setTotalCount(((BigInteger) query.getSingleResult())
					.intValue());
			@SuppressWarnings("unchecked")
			List<Device> list = queryList.getResultList();
			pager.setItems(list);
			return pager;

		} catch (Exception e) {
			logger.error("获取设备列表出错", e);
			throw new CodeException("内部错误");

		}
	}

	@Override
	public void updateDeviceCRT(int accountId, int deviceId, int status)
			throws CodeException {
		try {
			// 更新状态为
			deviceRepository.updateDeviceCRTStatus(status, deviceId);
			// 保存到命令记录中
			CommandRecord commandRecord = new CommandRecord();
			commandRecord.setAccountId(accountId);
			commandRecord.setType(3);
			commandRecord.setRecordTime(new Date());
			commandRecord.setContent("更新了设备Id为: " + deviceId + " 的证书");
			comRecordRepository.save(commandRecord);
		} catch (Exception e) {
			logger.error("更新设备证书出错", e);
			throw new CodeException("更新设备证书出错");

		}
	}

	@Override
	public void updateValidTime(int accountId, int deviceId, long newValidTime)
			throws CodeException {
		try {
			Account account = accountRepository.findOne(accountId);
			if (account.getType() == 0) {
				throw new CodeException("您没有该权限");
			} else {
				// 更新设备有效期
				Date validDate = new Date(newValidTime);
				Device device = deviceRepository.findOne(deviceId);
				if (device.getValidTime().after(validDate)) {
					throw new CodeException("请设置正确的有效期");
				}
				deviceRepository.updateDeviceValidTime(validDate, deviceId);
				// 保存到命令记录中
				CommandRecord commandRecord = new CommandRecord();
				commandRecord.setAccountId(accountId);
				commandRecord.setType(4);
				commandRecord.setRecordTime(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				StringBuffer buffer = new StringBuffer();
				buffer.append("更改设备ID为:").append(deviceId).append("的有效期由 ")
						.append(sdf.format(device.getValidTime()))
						.append("更改为 ").append(sdf.format(validDate));
				commandRecord.setContent(buffer.toString());
				comRecordRepository.save(commandRecord);

			}

		} catch (CodeException e) {
			throw e;

		} catch (Exception e) {
			logger.error("更新设备有效期限出错", e);
			throw new CodeException("更新设备有效期出错");

		}

	}

	@Override
	public void updateDeviceManageStatus(int accountId, int deviceId,
			int changeType, int status, long newValidTime) throws CodeException {

		try {
			Account account = accountRepository.findOne(accountId);
			if (account.getType() == 0) {
				throw new CodeException("您没有该权限");
			} else {
				Date validTime = new Date(newValidTime);
				CommandRecord commandRecord = new CommandRecord();
				commandRecord.setAccountId(accountId);
				commandRecord.setRecordTime(new Date());
				String manageType = null;
				if (status == 0) {
					manageType = "关闭";
				} else {
					manageType = "开启";
				}
				if (changeType == 0) {
					// 只进行设备状态的更新
					deviceRepository.updateManageDeviceStatus(status, deviceId);
					// 保存到设备记录中
					StringBuffer buffer = new StringBuffer();
					buffer.append("更改设备ID为:").append(deviceId).append("的状态为")
							.append(manageType);
					commandRecord.setContent(buffer.toString());
				} else {
					// 对设备新的有效期进行检查
					Device device = deviceRepository.findOne(deviceId);
					if (device.getValidTime().after(validTime)) {
						throw new CodeException("请设置正确的有效期");
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					// 更新设备的状态，同时更新过期时间
					deviceRepository.updateDeviceMDStatusAndValidTime(status,
							validTime, deviceId);
					// 保存到设备记录
					StringBuffer buffer = new StringBuffer();
					buffer.append("更改设备ID为:").append(deviceId).append("的状态为")
							.append(manageType).append("同时将设备有效期从")
							.append(sdf.format(device.getValidTime()))
							.append("更改为").append(sdf.format(validTime));
					commandRecord.setContent(buffer.toString());
				}
				comRecordRepository.save(commandRecord);
			}

		} catch (CodeException e) {
			throw e;

		} catch (Exception e) {
			logger.error("更新设备有效期限出错", e);
			throw new CodeException("更新设备有效期出错");

		}
	}

	@Override
	public ByteArrayOutputStream downloadDeviceZipFile(int accountId,
			int deviceId) throws CodeException {
		try {
			Account account = accountRepository.findOne(accountId);
			if (account == null || account.getType() == 0) {
				throw new CodeException("没有权限");
			}
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					byteArrayOutputStream);
			ZipOutputStream zipOutputStream = new ZipOutputStream(
					bufferedOutputStream);
			// simple file list, just for tests
			ArrayList<File> files = new ArrayList<File>(2);
			files.add(new File("E:\\record.txt"));
			files.add(new File("E:\\search.bat"));

			// 打包文件
			for (File file : files) {
				zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
				FileInputStream fileInputStream = new FileInputStream(file);
				IOUtils.copy(fileInputStream, zipOutputStream);
				fileInputStream.close();
				zipOutputStream.closeEntry();
			}

			if (zipOutputStream != null) {
				zipOutputStream.finish();
				zipOutputStream.flush();
				IOUtils.closeQuietly(zipOutputStream);
			}
			IOUtils.closeQuietly(bufferedOutputStream);
			IOUtils.closeQuietly(byteArrayOutputStream);
			return byteArrayOutputStream;

		} catch (CodeException e) {
			throw e;

		} catch (Exception e) {
			logger.error("下载证书失败", e);
			throw new CodeException("下载证书失败");

		}
	}
}
