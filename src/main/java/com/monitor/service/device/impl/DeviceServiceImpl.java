package com.monitor.service.device.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private static String crtPath = null;
	private static String clientInterval = null;
	// 初始化证书脚本地址
	static {
		if (crtPath == null) {
			ResourceBundle bundle = ResourceBundle.getBundle("crtpath");
			if (bundle == null) {
				throw new IllegalArgumentException(
						"[crtpath.properties] is not found!");
			}
			crtPath = bundle.getString("crt.path");
			clientInterval = bundle.getString("client.interval");
		}
	}

	@Override
	public void addNewDevice(int accountId, Device device) throws CodeException {
		try {
			Account operateAccount = accountRepository.findOne(accountId);
			if (operateAccount.getIsDelete() == 1
					|| operateAccount.getType() == 0) {
				throw new CodeException("请重新登录");
			}
			// 添加新的设备
			device.setDeviceStatus(0);
			device.setCommunicationStatus(0);
			device.setManageDeviceStatus(1);
			device.setRegTime(new Date());
			Device newDevice = deviceRepository.save(device);
			// 生成设备信息文件
			StringBuffer filePath = new StringBuffer(crtPath);
			filePath.append("deviceInfo/").append(newDevice.getDeviceId());
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					filePath.toString())));
			writer.write("device_id=" + newDevice.getDeviceId());
			writer.newLine();
			writer.write("interval=" + clientInterval);
			writer.close();
			// 生成设备证书文件
			ProcessBuilder pb = new ProcessBuilder(crtPath
					+ "new_client_cert.sh", newDevice.getDeviceId() + "");

			Process p = pb.start();
			p.waitFor();// 同步执行
			// 保存到命令记录中
			CommandRecord commandRecord = new CommandRecord();
			commandRecord.setAccountId(accountId);
			commandRecord.setType(2);
			commandRecord.setRecordTime(new Date());
			commandRecord.setContent("添加了新的设备：" + device.getDeviceName());
			comRecordRepository.save(commandRecord);
		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("保存设备出错", e);
			throw new CodeException("新增设备出错");
		}
	}

	@Override
	public Pager queryDevice(Integer pageNo, Integer pageSize,
			Integer accountId, int type) throws CodeException {
		try {
			Account operateAccount = accountRepository.findOne(accountId);
			if (operateAccount.getIsDelete() == 1) {
				throw new CodeException("请重新登录");
			}

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

		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("获取设备列表出错", e);
			throw new CodeException("内部错误");
		}
	}

	@Override
	public void updateDeviceCRT(int accountId, int deviceId, int status)
			throws CodeException {
		try {
			Account operateAccount = accountRepository.findOne(accountId);
			if (operateAccount.getIsDelete() == 1
					|| operateAccount.getType() == 0) {
				throw new CodeException("请重新登录");
			}
			// 更新状态为
			deviceRepository.updateDeviceCRTStatus(status, deviceId);
			// 保存到命令记录中
			CommandRecord commandRecord = new CommandRecord();
			commandRecord.setAccountId(accountId);
			commandRecord.setType(3);
			commandRecord.setRecordTime(new Date());
			commandRecord.setContent("更新了设备Id为: " + deviceId + " 的证书");
			comRecordRepository.save(commandRecord);
		} catch (CodeException e) {
			throw e;
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
			if (account.getType() == 0 || account.getIsDelete() == 1) {
				throw new CodeException("请重新登录");
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
			if (account.getType() == 0 || account.getIsDelete()==1) {
				throw new CodeException("请重新登录");
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
			if (account.getIsDelete()==1 || account.getType() == 0) {
				throw new CodeException("请重新登录");
			}
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					byteArrayOutputStream);
			ZipOutputStream zipOutputStream = new ZipOutputStream(
					bufferedOutputStream);
			// 下载设备信息和证书文件
			ArrayList<File> files = new ArrayList<File>(4);
			files.add(new File(crtPath + "deviceInfo/" + deviceId));// 设备信息文件
			files.add(new File(crtPath + "ca/ca.crt"));// ca根证书
			files.add(new File(crtPath + "user/certificates/" + deviceId
					+ ".crt"));// 客户端证书
			files.add(new File(crtPath + "user/keys/" + deviceId + ".key"));// 客户端私钥

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
