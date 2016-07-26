package com.monitor.service.device.impl;

import com.monitor.dao.account.AccountRepository;
import com.monitor.dao.commandrecord.CommandRecordRepository;
import com.monitor.dao.device.DeviceRepository;
import com.monitor.dao.devicerecord.DeviceRecordRepository;
import com.monitor.exception.CodeException;
import com.monitor.model.*;
import com.monitor.service.device.IDeviceService;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service(value = "deviceService")
public class DeviceServiceImpl implements IDeviceService {
	private static Logger logger = Logger.getLogger(DeviceServiceImpl.class);
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private CommandRecordRepository comRecordRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private DeviceRecordRepository deviceRecordRepository;

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
			commandRecord.setContent("新增设备" + "("+device.getDeviceName()+")");
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
			Integer accountId, int type, String deviceName, String lesseeName,String provice1,
			long startTime, long endTime, long startValidTime, long endValidTime)
			throws CodeException {
		try {
			Account operateAccount = accountRepository.findOne(accountId);
			if (operateAccount.getIsDelete() == 1) {
				throw new CodeException("请重新登录");
			}
			String provice = java.net.URLDecoder.decode(provice1,"UTF-8"); 

			Pager pager = new Pager(pageNo, pageSize);
			int thisPage = (pageNo - 1) * pageSize;
			//查询通信正常设备的Id,小于24小时即为通信正常
			StringBuilder select_comCorSql = new StringBuilder("select deviceId,max(realTime) as maxTime from devicerecord devicerecord where 1=1 group by devicerecord.deviceId having maxTime>DATE_SUB(now(),INTERVAL 1  DAY)");
			//查询通信异常设备的Id,超过24小时即为通信异常
			StringBuilder select_comErrSql = new StringBuilder("select deviceId,max(realTime) as maxTime from devicerecord devicerecord where 1=1 group by devicerecord.deviceId having maxTime<DATE_SUB(now(),INTERVAL 1  DAY)");
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
			if (!StringUtils.isEmpty(provice)) {
				builder.append("  and device.provice like:provice ");
				countSql.append("  and device.provice like:provice ");
			}
			if (!StringUtils.isEmpty(deviceName)) {
				builder.append("  and device.deviceName like:deviceName ");
				countSql.append("  and device.deviceName like:deviceName ");
			}
			if (!StringUtils.isEmpty(lesseeName)) {
				builder.append("  and device.lesseeName like:lesseeName ");
				countSql.append("  and device.lesseeName like:lesseeName ");
			}
			if (startTime != 0) {
				builder.append("  and device.regTime >=:startTime ");
				countSql.append("  and device.regTime >=:startTime ");
			}
			if (endTime != 0) {
				builder.append("  and device.regTime <=:endTime ");
				countSql.append("  and device.regTime <=:endTime ");
			}
			if (startValidTime != 0) {
				builder.append("  and device.validTime >=:startValidTime ");
				countSql.append("  and device.validTime >=:startValidTime ");
			}
			if (endValidTime != 0) {
				builder.append("  and device.validTime <=:endValidTime ");
				countSql.append("  and device.validTime <=:endValidTime ");
			}

			builder.append(" ORDER BY device.deviceId DESC ");
			builder.append(" limit " + thisPage + "," + pageSize);

			Query query = manager.createNativeQuery(countSql.toString());
			Query queryList = manager.createNativeQuery(builder.toString(),
					Device.class);
			Query queryComErr = manager.createNativeQuery(select_comErrSql.toString());
			Query queryComCor = manager.createNativeQuery(select_comCorSql.toString());
			if (!StringUtils.isEmpty(deviceName)) {
				query.setParameter("deviceName", '%' + deviceName + '%');
				queryList.setParameter("deviceName", '%' + deviceName + '%');
			}
			if (!StringUtils.isEmpty(provice)) {
				query.setParameter("provice", '%' + provice + '%');
				queryList.setParameter("provice", '%' + provice + '%');
			}
			if (!StringUtils.isEmpty(lesseeName)) {
				query.setParameter("lesseeName", '%' + lesseeName + '%');
				queryList.setParameter("lesseeName", '%' + lesseeName + '%');
			}
			if (startTime != 0) {
				Date startDate = new Date(startTime);
				query.setParameter("startTime", startDate);
				queryList.setParameter("startTime", startDate);
			}
			if (endTime != 0) {
				Date endDate = new Date(endTime);
				query.setParameter("endTime", endDate);
				queryList.setParameter("endTime", endDate);
			}
			if (startValidTime != 0) {
				Date startValidDate = new Date(startValidTime);
				query.setParameter("startValidTime", startValidDate);
				queryList.setParameter("startValidTime", startValidDate);
			}
			if (endValidTime != 0) {
				Date endValidDate = new Date(endValidTime);
				query.setParameter("endValidTime", endValidDate);
				queryList.setParameter("endValidTime", endValidDate);
			}

			pager.setTotalCount(((BigInteger) query.getSingleResult())
					.intValue());
			@SuppressWarnings("unchecked")
			List<Object[]> listComErr = queryComErr.getResultList();
			if(listComErr.size()>0){
			for (int i = 0; i < listComErr.size(); i++) {
				int deviceId=(Integer) listComErr.get(i)[0];
				deviceRepository.updateCommunicationStatus0(0, deviceId);
				}
			}
			@SuppressWarnings("unchecked")
			List<Object[]> listComCor = queryComCor.getResultList();
			if(listComCor.size()>0){
			for (int i = 0; i < listComCor.size(); i++) {
				int deviceId=(Integer) listComCor.get(i)[0];
				deviceRepository.updateCommunicationStatus0(1, deviceId);
				}
			}
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
			Device device = deviceRepository.findOne(deviceId);
			CommandRecord commandRecord = new CommandRecord();
			commandRecord.setAccountId(accountId);
			commandRecord.setType(3);
			commandRecord.setRecordTime(new Date());
			commandRecord.setContent("更新设备" + "("+device.getDeviceName() +")"+ "状态");
			comRecordRepository.save(commandRecord);
		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("更新设备证书出错", e);
			throw new CodeException("更新设备证书出错");

		}
	}

	@Override
	public void updateValidTime(int accountId, int deviceId, long newValidTime,
			int addReason, String addNote) throws CodeException {
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
				System.out.println("addNote:" + addNote);
				CommandRecord commandRecord = new CommandRecord();
				commandRecord.setAccountId(accountId);
				commandRecord.setType(4);
				commandRecord.setRecordTime(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				StringBuffer buffer = new StringBuffer();
				buffer.append("设备(").append(device.getDeviceName()).append(")有效期")
						.append(sdf.format(device.getValidTime()))
						.append("修改为").append(sdf.format(validDate));
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
			if (account.getType() == 0 || account.getIsDelete() == 1) {
				throw new CodeException("请重新登录");
			} else {
				Device device = deviceRepository.findOne(deviceId);
				Date validTime = new Date(newValidTime);
				CommandRecord commandRecord = new CommandRecord();
				commandRecord.setAccountId(accountId);
				commandRecord.setType(2);
				commandRecord.setRecordTime(new Date());
				String manageType = null;
				if (status == 0) {
					manageType = "关闭";
					SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					deviceRepository.updateDeviceShallCloseTime(dateformat.format(new Date()), deviceId);
				} else {
					manageType = "开启";
				}
				if (changeType == 0) {
					// 只进行设备状态的更新
					deviceRepository.updateManageDeviceStatus(status, deviceId);
					// 保存到设备记录中
					StringBuffer buffer = new StringBuffer();
					buffer.append(manageType).append("设备(").append(device.getDeviceName()).append(")");
					commandRecord.setContent(buffer.toString());
				} else {
					// 对设备新的有效期进行检查
					if (device.getValidTime().after(validTime)) {
						throw new CodeException("请设置正确的有效期");
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					// 更新设备的状态，同时更新过期时间
					deviceRepository.updateDeviceMDStatusAndValidTime(status,
							validTime, deviceId);
					// 保存到设备记录
					StringBuffer buffer = new StringBuffer();
					buffer.append("设备(").append(device.getDeviceName()).append(")有效期")
							.append(sdf.format(device.getValidTime()))
							.append("修改为").append(sdf.format(validTime));
					commandRecord.setContent(buffer.toString());
					commandRecord.setType(4);
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
			if (account.getIsDelete() == 1 || account.getType() == 0) {
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

	@Override
	public int getTotalOutDateCount(int accountId) throws CodeException {
		try {
			Account operateAccount = accountRepository.findOne(accountId);
			if (operateAccount.getIsDelete() == 1
					|| operateAccount.getType() == 0) {
				throw new CodeException("请重新登录");
			}
			StringBuilder countSql = new StringBuilder(
					" select count(deviceId) from device device "
							+ " where 1=1 ");
			countSql.append(" and device.manageDeviceStatus =1 and device.validTime <=DATE_ADD(now(),INTERVAL 3 DAY) ");
			Query query = manager.createNativeQuery(countSql.toString());
			int totalCount = (((BigInteger) query.getSingleResult()).intValue());
			return totalCount;
		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("获取设备列表出错", e);
			throw new CodeException("内部错误");
		}
	}

	@Override
	public List<Device> getAllDevice(int accountId, int type, String deviceName, String lesseeName,
			long startTime, long endTime, long startValidTime, long endValidTime)
			throws CodeException {
		try {
			Account operateAccount = accountRepository.findOne(accountId);
			if (operateAccount.getIsDelete() == 1) {
				throw new CodeException("请重新登录");
			}

			StringBuilder builder = new StringBuilder(
					"select * from device device where 1=1");
			if (type == 0) {
				builder.append("  and device.manageDeviceStatus =1 and device.validTime >DATE_ADD(now(),INTERVAL 3 DAY) ");
			} else if (type == 1) {
				builder.append("  and device.manageDeviceStatus =1 and device.validTime <=DATE_ADD(now(),INTERVAL 3 DAY) ");
			} else if (type == 2) {
				builder.append("  and device.manageDeviceStatus =0 ");
			}
			if (!StringUtils.isEmpty(deviceName)) {
				builder.append("  and device.deviceName =:deviceName ");
			}
			if (!StringUtils.isEmpty(lesseeName)) {
				builder.append("  and device.lesseeName =:lesseeName ");
			}
			if (startTime != 0) {
				builder.append("  and device.regTime >=:startTime ");
			}
			if (endTime != 0) {
				builder.append("  and device.regTime <=:endTime ");
			}
			if (startValidTime != 0) {
				builder.append("  and device.validTime >=:startValidTime ");
			}
			if (endValidTime != 0) {
				builder.append("  and device.validTime <=:endValidTime ");
			}

			builder.append(" ORDER BY device.validTime ASC ");

			Query queryList = manager.createNativeQuery(builder.toString(),
					Device.class);
			if (!StringUtils.isEmpty(deviceName)) {
				queryList.setParameter("deviceName", deviceName);
			}
			if (!StringUtils.isEmpty(lesseeName)) {
				queryList.setParameter("lesseeName", lesseeName);
			}
			if (startTime != 0) {
				Date startDate = new Date(startTime);
				queryList.setParameter("startTime", startDate);
			}
			if (endTime != 0) {
				Date endDate = new Date(endTime);
				queryList.setParameter("endTime", endDate);
			}
			if (startValidTime != 0) {
				Date startValidDate = new Date(startValidTime);
				queryList.setParameter("startValidTime", startValidDate);
			}
			if (endValidTime != 0) {
				Date endValidDate = new Date(endValidTime);
				queryList.setParameter("endValidTime", endValidDate);
			}

			@SuppressWarnings("unchecked")
			List<Device> list = queryList.getResultList();
			return list;

		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("获取设备列表出错", e);
			throw new CodeException("内部错误");
		}

	}

	@Override
	public void updateDeviceManStatus(int accountId, int deviceId,
			long startTime, long endTime) throws CodeException {

		try {
			Account operateAccount = accountRepository.findOne(accountId);
			if (operateAccount.getIsDelete() == 1
					|| operateAccount.getType() == 0) {
				throw new CodeException("请重新登录");
			}
			deviceRecordRepository.updateDeviceRecordOpeRationType(deviceId,
					new Date(startTime), new Date(endTime));
			deviceRepository.updateManageDeviceStatus(0, deviceId);
			//命令记录日志
			Device device = deviceRepository.findOne(deviceId);
			CommandRecord commandRecord = new CommandRecord();
			commandRecord.setAccountId(accountId);
			commandRecord.setType(2);
			commandRecord.setRecordTime(new Date());
			commandRecord.setContent("关闭设备" + "("+device.getDeviceName()+")");
			comRecordRepository.save(commandRecord);
			
		} catch (CodeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("更新设备状态出错", e);
			throw new CodeException("更新设备状态出错");
		}
	}

	@SuppressWarnings("null")
	@Override
	public List<DeviceStatus> queryDeviceStatus() throws CodeException
	{
		List<DeviceStatus> resultList = new ArrayList<DeviceStatus>();
		try{
			StringBuilder deviceProvince = new StringBuilder("select DISTINCT provice from device");
			Query queryDeviceProvince = manager.createNativeQuery(deviceProvince.toString());
			List<String> provinceList = queryDeviceProvince.getResultList();
		
			//List<Device> deviceList = query.getResultList();
			for(int i = 0; i < provinceList.size(); i++){
				String province = provinceList.get(i);
				StringBuilder onDeviceInfo = new StringBuilder("select count(*) from device where provice = '" + province + "' and manageDeviceStatus = 1");
				Query queryOnDeviceInfo = manager.createNativeQuery(onDeviceInfo.toString());
				List<BigInteger> onDeviceInfoList = queryOnDeviceInfo.getResultList();
				int onDeviceNum = onDeviceInfoList.get(0).intValue();
				
				StringBuilder offDeviceInfo = new StringBuilder("select count(*) from device where provice = '" + province + "' and manageDeviceStatus = 0");
				Query queryOffDeviceInfo = manager.createNativeQuery(offDeviceInfo.toString());
				List<BigInteger> offDeviceInfoList = queryOffDeviceInfo.getResultList();
				int offDeviceNum = offDeviceInfoList.get(0).intValue();
				
				StringBuilder offAndArrearageDeviceInfo = new StringBuilder("select count(*) from device where provice = '" + province + "' and manageDeviceStatus = 0 and validTime <= now()");
				Query queryOffAndArrearageDeviceInfo = manager.createNativeQuery(offAndArrearageDeviceInfo.toString());
				List<BigInteger> offAndArrearageDeviceInfoList = queryOffAndArrearageDeviceInfo.getResultList();
				int offAndArrearageDeviceNum = offAndArrearageDeviceInfoList.get(0).intValue();
				
				DeviceStatus deviceStatus = new DeviceStatus();
				deviceStatus.setOnDeviceNum(onDeviceNum);
				deviceStatus.setOffDeviceNum(offDeviceNum);
				deviceStatus.setOffAndArrearageDeviceNum(offAndArrearageDeviceNum);
				deviceStatus.setProvince(province);
				
				resultList.add(deviceStatus);
			}
			return resultList;
		} catch (Exception e) {
			logger.error("内部错误", e);
			throw new CodeException("内部错误");
		}
	}
	
	@Override
	public List queryTotalNumOfDeviceStatus() throws CodeException {
		List resultList = new ArrayList();
		try{
			StringBuilder deviceOnStatusStatistic = new StringBuilder("select count(*) from device where manageDeviceStatus = 1");
			Query qOn = manager.createNativeQuery(deviceOnStatusStatistic.toString());
			
			StringBuilder deviceOffStatusStatistic = new StringBuilder("select count(*) from device where manageDeviceStatus = 0");
			Query qOff = manager.createNativeQuery(deviceOffStatusStatistic.toString());
			
			StringBuilder deviceOffAndArrearageStatusStatistic = new StringBuilder("select count(*) from device where manageDeviceStatus = 0 and validTime <= DATE_ADD(now(),INTERVAL 3 DAY)");
			Query qOffAndArrearage = manager.createNativeQuery(deviceOffAndArrearageStatusStatistic.toString());
			
			List onList = qOn.getResultList();
			List offList = qOff.getResultList();
			List offAndArrearageList = qOffAndArrearage.getResultList();
			
			resultList.add(onList);
			resultList.add(offList);
			resultList.add(offAndArrearageList);
			
			return resultList;
		} catch (Exception e) {
			logger.error("内部错误", e);
			throw new CodeException("内部错误");
		}
	}

	@Override
	public List<DeviceArrearagePercentage> queryArrearagePercentage(Integer month) throws CodeException {
		List<DeviceArrearagePercentage> resultList = new ArrayList<DeviceArrearagePercentage>();
		try{
            DecimalFormat decimalFormat = new DecimalFormat("#.00");

            StringBuilder deviceArrearageTotalStatistic = new StringBuilder("select sum(arrearageCount) from device");
			Query qTotal = manager.createNativeQuery(deviceArrearageTotalStatistic.toString());
			List<BigDecimal> totalResultList = qTotal.getResultList();
			
			float total = ((BigDecimal) totalResultList.get(0)).floatValue();
			//((BigInteger) query.getSingleResult()).intValue()
			StringBuilder deviceLesseeNameStatistic = new StringBuilder("select DISTINCT lesseeName from device");
			Query queryLesseeName = manager.createNativeQuery(deviceLesseeNameStatistic.toString());
			List<String> lesseeNameList = queryLesseeName.getResultList();
			for(int i = 0; i < lesseeNameList.size(); i++){
				DeviceArrearagePercentage deviceArrearagePercentage = new DeviceArrearagePercentage();
				
				String lesseeName = lesseeNameList.get(i);
//				StringBuilder deviceArrearageNum = new StringBuilder("select sum(arrearageCount) from device where lesseeName = '" + lesseeName + "'");
//				Query queryArrearageNum = manager.createNativeQuery(deviceArrearageNum.toString());
//				List<BigDecimal> arrearageNumberList = queryArrearageNum.getResultList();
//				int arrearageNumber = ((BigDecimal) arrearageNumberList.get(0)).intValue();
				//select count(*) from commandrecord where lesseeName = 'uestc' and recordTime >= DATE_SUB(NOW(),INTERVAL 1 MONTH)
				StringBuilder deviceArrearageNum = new StringBuilder("select count(*) from commandrecord where lesseeName = '" + lesseeName + "' and recordTime >= DATE_SUB(NOW(),INTERVAL " + month + " MONTH)");
				Query queryArrearageNum = manager.createNativeQuery(deviceArrearageNum.toString());
				List<BigInteger> arrearageNumberList = queryArrearageNum.getResultList();
				int arrearageNumber = arrearageNumberList.get(0).intValue();
				
				
				StringBuilder deviceTotalNum = new StringBuilder("select count(*) from device where lesseeName = '" + lesseeName + "'");
				Query queryDeviceTotalNum = manager.createNativeQuery(deviceTotalNum.toString());
				List<BigInteger> deviceTotalNumList = queryDeviceTotalNum.getResultList();
				int totalNum = deviceTotalNumList.get(0).intValue();
				
				StringBuilder deviceNormalNum = new StringBuilder("select count(*) from device where lesseeName = '" + lesseeName + "' and manageDeviceStatus = 1");
				Query queryDeviceNormalNum = manager.createNativeQuery(deviceNormalNum.toString());
				List<BigInteger> deviceNormalNumList = queryDeviceNormalNum.getResultList();
				int normalDeviceNum = deviceNormalNumList.get(0).intValue();

                //Integer.valueOf(delayValue).intValue()
				float arrearagePercentage = Float.valueOf(decimalFormat.format((float) arrearageNumber / (totalNum * month))).floatValue();
				
				deviceArrearagePercentage.setLessee(lesseeName);
				deviceArrearagePercentage.setPercentage(arrearagePercentage);
				deviceArrearagePercentage.setArrearageDeviceNum(totalNum - normalDeviceNum);
				deviceArrearagePercentage.setNormalDeviceNum(normalDeviceNum);
				

				if(arrearagePercentage > 0 && arrearagePercentage <= 0.05){
					deviceArrearagePercentage.setArrearagePercentageType(1);
				} else if(arrearagePercentage > 0.05 && arrearagePercentage <= 0.1){
					deviceArrearagePercentage.setArrearagePercentageType(2);
				} else if(arrearagePercentage > 0.1 && arrearagePercentage <= 0.15){
					deviceArrearagePercentage.setArrearagePercentageType(3);
				} else if(arrearagePercentage > 0.15 && arrearagePercentage <= 0.2){
					deviceArrearagePercentage.setArrearagePercentageType(4);
				} else if(arrearagePercentage > 0.2 && arrearagePercentage <= 0.25){
					deviceArrearagePercentage.setArrearagePercentageType(5);
				} else if(arrearagePercentage > 0.25 && arrearagePercentage <= 0.3){
					deviceArrearagePercentage.setArrearagePercentageType(6);
				} else if(arrearagePercentage > 0.3 && arrearagePercentage <= 1){
					deviceArrearagePercentage.setArrearagePercentageType(7);
				} else {
					deviceArrearagePercentage.setArrearagePercentageType(0);
				}
				
				resultList.add(deviceArrearagePercentage);
			}

			return resultList;
		} catch (Exception e) {
			logger.error("内部错误", e);
			throw new CodeException("内部错误");
		}
	}

	@Override
	public List<LesseeDeviceInfo> queryLesseeDeviceInformation()
			throws CodeException {
		List<LesseeDeviceInfo> resultList = new ArrayList<LesseeDeviceInfo>();
		try{
			StringBuilder deviceLesseeNameStatistic = new StringBuilder("select DISTINCT lesseeName from device");
			Query queryLesseeName = manager.createNativeQuery(deviceLesseeNameStatistic.toString());
			List<String> lesseeNameList = queryLesseeName.getResultList();
			
			//查询租赁商为空集
			if(lesseeNameList.get(0) == null){
				LesseeDeviceInfo nullResult = new LesseeDeviceInfo();
				nullResult.setArrearageDeviceNum(0);
				nullResult.setLesseeName("N/A");
				nullResult.setNormalDeviceNum(0);
				nullResult.setTotalDeviceNum(0);
				nullResult.setWillArrearageDeviceNum(0);
				resultList.add(nullResult);
				return resultList;
			}
			for(int i = 0; i < lesseeNameList.size(); i++){
				String lesseeName = lesseeNameList.get(i);
				
				//get total device number
				StringBuilder lesseeDeviceTotalNum = new StringBuilder("select count(*) from device where lesseeName = '" + lesseeName + "'");
				Query queryLesseeDeviceTotalNum = manager.createNativeQuery(lesseeDeviceTotalNum.toString());
				List<BigInteger> lesseeDeviceTotalNumList = queryLesseeDeviceTotalNum.getResultList();
				int totalDeviceNum = lesseeDeviceTotalNumList.get(0).intValue();
				// ((BigDecimal) totalResultList.get(0)).floatValue();
				
				//get arrearage device number
				StringBuilder lesseeArrearageDeviceNum = new StringBuilder("select count(*) from device where lesseeName = '" + lesseeName + "'and validTime <= DATE_ADD(now(),INTERVAL 3 DAY)");
				Query queryArrearageDeviceNum = manager.createNativeQuery(lesseeArrearageDeviceNum.toString());
				List<BigInteger> arrearageDeviceNumList = queryArrearageDeviceNum.getResultList();
				int arrearageDeviceNum = arrearageDeviceNumList.get(0).intValue();
			
				//get normal device number
				int normalDeviceNum = totalDeviceNum - arrearageDeviceNum;
				
				//get will arrearage device number
				//select count(*) from device where lesseeName = 'uestc' and validTime <= DATE_ADD(now(),INTERVAL 3 DAY);
				StringBuilder willArrearageLesseeDeviceNum = new StringBuilder("select count(*) from device where lesseeName = '" + lesseeName + "' and deviceStatus = 0");
				Query queryWillArrearageLesseeDeviceNum = manager.createNativeQuery(willArrearageLesseeDeviceNum.toString());
				List<BigInteger> willArrearageLesseeDeviceNumList = queryWillArrearageLesseeDeviceNum.getResultList();
				int willArrearageDeviceNum = willArrearageLesseeDeviceNumList.get(0).intValue();
				
				//get total arrearage number
				StringBuilder totalArrearageLesseeDeviceNum = new StringBuilder("select count(*) from device where lesseeName = '" + lesseeName + "' and deviceStatus = 0");
				Query queryTotalArrearageLesseeDeviceNum = manager.createNativeQuery(totalArrearageLesseeDeviceNum.toString());
				List<BigDecimal> totalArrearageLesseeDeviceNumList = queryTotalArrearageLesseeDeviceNum.getResultList();
				int totalArreatageDeviceNum = totalArrearageLesseeDeviceNumList.get(0).intValue();
				
				LesseeDeviceInfo lesseeDeviceInfo = new LesseeDeviceInfo();
				lesseeDeviceInfo.setArrearageDeviceNum(arrearageDeviceNum);
				lesseeDeviceInfo.setLesseeName(lesseeName);
				lesseeDeviceInfo.setNormalDeviceNum(normalDeviceNum);
				lesseeDeviceInfo.setTotalDeviceNum(totalDeviceNum);
				lesseeDeviceInfo.setWillArrearageDeviceNum(willArrearageDeviceNum);
				
				resultList.add(lesseeDeviceInfo);
			}

            return resultList;
		} catch (Exception e) {
			logger.error("内部错误", e);
			throw new CodeException("内部错误");
		}
	}

	@Override
	public Pager queryLesseeDeviceInformationPager(Integer pageNo,
			Integer pageSize, Integer accountId, int type, String lesseeName, int arrearagePercentageType, Integer month
			, Integer startYear, Integer startMonth, Integer endYear, Integer endMonth)
			throws CodeException {
		try {
			String startTime = startYear + "-" + startMonth + "-01";
			String endTime = endYear + "-" + endMonth + "-31";			
			
			//计算总欠费次数
			StringBuilder deviceArrearageTotalStatistic = new StringBuilder("select sum(arrearageCount) from device");
			Query qTotal = manager.createNativeQuery(deviceArrearageTotalStatistic.toString());
			List<BigDecimal> totalResultList = qTotal.getResultList();
			float total = ((BigDecimal) totalResultList.get(0)).floatValue();
			
			Pager pager = new Pager(pageNo, pageSize);
			pager.setTotalCount(0);
			int thisPage = (pageNo - 1) * pageSize;
			List<DeviceArrearagePercentage> resultList = new ArrayList();
			List<String> lesseeNameList = new ArrayList();
			if(!StringUtils.isEmpty(lesseeName)){//builder.append(" limit " + thisPage + "," + pageSize);
				StringBuilder deviceLesseeNameStatisticAll = new StringBuilder("select DISTINCT lesseeName from device where lesseeName like '%" + lesseeName + "%'");
				Query queryLesseeNameAll = manager.createNativeQuery(deviceLesseeNameStatisticAll.toString());
				lesseeNameList = queryLesseeNameAll.getResultList();
				pager.setTotalCount(lesseeNameList.size());

				StringBuilder deviceLesseeNameStatistic = new StringBuilder("select DISTINCT lesseeName from device where lesseeName like '%" + lesseeName + "%' limit " + thisPage + "," + pageSize);
				Query queryLesseeName = manager.createNativeQuery(deviceLesseeNameStatistic.toString());
				lesseeNameList = queryLesseeName.getResultList();
			} else {
				StringBuilder deviceLesseeNameStatisticAll = new StringBuilder("select DISTINCT lesseeName from device");
				Query queryLesseeNameAll = manager.createNativeQuery(deviceLesseeNameStatisticAll.toString());
				lesseeNameList = queryLesseeNameAll.getResultList();
				pager.setTotalCount(lesseeNameList.size());

				//获取租赁商列表
				StringBuilder deviceLesseeNameStatistic = new StringBuilder("select DISTINCT lesseeName from device limit " + thisPage + "," + pageSize);
				Query queryLesseeName = manager.createNativeQuery(deviceLesseeNameStatistic.toString());
				lesseeNameList = queryLesseeName.getResultList();
			}
			
			for(int i = 0; i < lesseeNameList.size(); i++){
				DeviceArrearagePercentage deviceArrearagePercentage = new DeviceArrearagePercentage();
				
				//查询该租赁商总欠费次数
				String thisLesseeName = lesseeNameList.get(i);
//				StringBuilder deviceArrearageNum = new StringBuilder("select sum(arrearageCount) from device where lesseeName = '" + thisLesseeName + "'");
//				Query queryArrearageNum = manager.createNativeQuery(deviceArrearageNum.toString());
//				List<BigDecimal> arrearageNumberList = queryArrearageNum.getResultList();
//				int arrearageNumber = ((BigDecimal) arrearageNumberList.get(0)).intValue();

				//通过日志查询欠费总次数
				StringBuilder deviceArrearageNum = new StringBuilder("select count(*) from commandrecord where lesseeName = '" + thisLesseeName + "' and recordTime > '" + startTime + "' and recordTime < '" + endTime + "'");
//				System.out.println("select count(*) from commandrecord where lesseeName = '" + thisLesseeName + "' and recordTime > " + startTime + " and recordTime < " + endTime);
				Query queryArrearageNum = manager.createNativeQuery(deviceArrearageNum.toString());
				List<BigInteger> arrearageNumberList = queryArrearageNum.getResultList();
				int arrearageNumber = arrearageNumberList.get(0).intValue();
				
				
				//查询总设备数
				StringBuilder deviceTotalNum = new StringBuilder("select count(*) from device where lesseeName = '" + thisLesseeName + "'");
				Query queryDeviceTotalNum = manager.createNativeQuery(deviceTotalNum.toString());
				List<BigInteger> deviceTotalNumList = queryDeviceTotalNum.getResultList();
				int totalNum = deviceTotalNumList.get(0).intValue();
				
				StringBuilder deviceNormalNum = new StringBuilder("select count(*) from device where lesseeName = '" + thisLesseeName + "' and manageDeviceStatus = 1");
				Query queryDeviceNormalNum = manager.createNativeQuery(deviceNormalNum.toString());
				List<BigInteger> deviceNormalNumList = queryDeviceNormalNum.getResultList();
				int normalDeviceNum = deviceNormalNumList.get(0).intValue();
				
				StringBuilder lesseePhone = new StringBuilder("select distinct lesseePhone from device where lesseeName = '" + thisLesseeName + "'");
				Query queryLesseePhone = manager.createNativeQuery(lesseePhone.toString());
				List<String> deviceLesseePhoneList = queryLesseePhone.getResultList();
				String lesseePhoneNumber = deviceLesseePhoneList.get(0);
				
				float arrearagePercentage = (float) arrearageNumber / (month * totalNum);
	
				if(arrearagePercentageType == 1 && (arrearagePercentage <= 0 || arrearagePercentage > 0.05)){
					continue;
				} else if(arrearagePercentageType == 2 && (arrearagePercentage <= 0.05 || arrearagePercentage > 0.1)){
					continue;
				} else if(arrearagePercentageType == 3 && (arrearagePercentage <= 0.1 || arrearagePercentage > 0.15)){
					continue;
				} else if(arrearagePercentageType == 4 && (arrearagePercentage <= 0.15 || arrearagePercentage > 0.2)){
					continue;
				} else if(arrearagePercentageType == 5 && (arrearagePercentage <= 0.2 || arrearagePercentage > 0.25)){
					continue;
				} else if(arrearagePercentageType == 6 && (arrearagePercentage <= 0.25 || arrearagePercentage > 0.3)){
					continue;
				} else if(arrearagePercentageType == 7 && (arrearagePercentage <= 0.3 || arrearagePercentage > 1)){
					continue;
				}else if(arrearagePercentageType == 0 && arrearagePercentage != 0){
					continue;
				}
				
				deviceArrearagePercentage.setLessee(thisLesseeName);
				deviceArrearagePercentage.setPercentage(arrearagePercentage);
				deviceArrearagePercentage.setArrearageDeviceNum(totalNum - normalDeviceNum);
				deviceArrearagePercentage.setNormalDeviceNum(normalDeviceNum);
				deviceArrearagePercentage.setLesseePhone(lesseePhoneNumber);
				deviceArrearagePercentage.setTotalDeviceNum(totalNum);
				deviceArrearagePercentage.setArrearageTimePerDevice((float) arrearageNumber / totalNum);
				
				resultList.add(deviceArrearagePercentage);
			}
			
			//pager.setTotalCount(resultList.size());
			pager.setItems(resultList);
			return pager;
		} catch (Exception e) {
			logger.error("获取设备列表出错", e);
			throw new CodeException("内部错误");
		}
	}
}
