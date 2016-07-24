package com.monitor.websocket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.persistence.Query;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.monitor.dao.commandrecord.CommandRecordRepository;
import com.monitor.dao.device.DeviceRepository;
import com.monitor.dao.devicerecord.DeviceRecordRepository;
import com.monitor.exception.CodeException;
import com.monitor.model.CommandRecord;
import com.monitor.model.Device;
import com.monitor.model.DeviceRecord;
import com.monitor.model.Message;
import com.monitor.model.MessageV2;
import com.monitor.model.SendMessage;
import com.monitor.model.SendMessageV2;
import com.monitor.model.SessionKey;
import com.monitor.service.devicerecord.IDeviceRecordService;
import com.monitor.util.HttpRequestUtil;
import com.monitor.util.SessionKeyUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.endpoint.SpringConfigurator;

@ServerEndpoint(value = "/websocket/{machineId}", configurator = SpringConfigurator.class)
public class WebSocketHandler {
	@Autowired
	DeviceRepository deviceRepository;
	@Autowired
	IDeviceRecordService deviceRecordService;
	@Autowired
	DeviceRecordRepository deviceRecordRepository;
	@Autowired
	CommandRecordRepository commandRecordRepository;
	private static String crtPath = null;

	@PersistenceContext
	private EntityManager manager;

	// 初始化证书脚本地址
	static {
		if (crtPath == null) {
			ResourceBundle bundle = ResourceBundle.getBundle("crtpath");
			if (bundle == null) {
				throw new IllegalArgumentException(
						"[crtpath.properties] is not found!");
			}
			crtPath = bundle.getString("crt.path");
		}
	}
	
//	private static String downloadPath = null;
//	// 初始化证书脚本地址
//	static {
//		if (downloadPath == null) {
//			ResourceBundle bundle = ResourceBundle.getBundle("crtpath");
//			if (bundle == null) {
//				throw new IllegalArgumentException(
//						"[crtpath.properties] is not found!");
//			}
//			downloadPath = bundle.getString("download.path");
//		}
//	}


	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private int deviceId;
	private String machineId;
	private Session sessionDevice;

	public int getDeviceId() {
		return deviceId;
	}

	public String getMachineId() {
		return machineId;
	}
//	public String getSessionId() {
//		return sessionDevice.getId();
//	}

	/**
	 * 
	 * @param msg
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws CodeException
	 * @throws ParseException
	 */
	@OnMessage
	public void onMessage(String msg) throws IOException, InterruptedException,
			CodeException, ParseException {
		System.out.println(msg);
//		
//		CommandRecord commandRecord = new CommandRecord();
//		commandRecord.setDeviceId(deviceId);
//		commandRecord.setDeviceCloseType(3);
//		commandRecord.setContent("数据传输测试");
//		commandRecord.setLesseeName("test");
//		commandRecord.setAddValidNote(msg);
//		commandRecordRepository.saveAndFlush(commandRecord);
		
		MessageV2 reciveMessage = JSON.parseObject(msg, MessageV2.class);// 接收消息
		SendMessageV2 sendMessage = new SendMessageV2();// 发送消息
		//TODO 暂时不适用dwtype 先设置默认值
		sendMessage.setDwtype(0);
		
		Date nowDate = new Date();// 当前日期
		int nowType = 0;// 设备当前的管理状态

		CommandRecord c = new CommandRecord();
		c.setDeviceId(deviceId);
		c.setDeviceCloseType(3);
		c.setContent("数据传输测试");
		c.setLesseeName("test");
		c.setAddValidNote(msg);
		commandRecordRepository.saveAndFlush(c);
		
		// 检查设备的状态
		//Device device = deviceRepository.findOne(deviceId);
		//Device device = deviceRepository.queryDeviceNameById();

		StringBuilder deviceInfo = new StringBuilder("select * from device where machineId = '" + this.getMachineId() + "'");
		Query queryDeviceInfo = manager.createNativeQuery(deviceInfo.toString(), Device.class);
		List<Device> deviceInfoList = queryDeviceInfo.getResultList();
		Device device = deviceInfoList.get(0);

		this.deviceId = device.getDeviceId();

		// 设备状态已经为off，直接关机
//		sendMessage.setKeyCreateDate("");
//		sendMessage.setRandomNum("");
		if (device.getManageDeviceStatus() == 0) {
//			sendMessage.setType(0);
			sendMessage.setTurnOnOff(0);
			nowType = 0;
		} else {
			if (nowDate.after(device.getValidTime())) {
				// 设备已经过期,更新数据库中的设备管理状态为off
				deviceRepository.updateManageDeviceStatus(0, deviceId);
				deviceRepository.updateArrearageCount(deviceId);
				//欠费关闭的设备被记录在日志表
				CommandRecord commandRecord = new CommandRecord();
				commandRecord.setDeviceId(deviceId);
				commandRecord.setDeviceCloseType(1);
				commandRecord.setContent("设备(" + device.getDeviceName() + ")欠费被关闭");
				commandRecord.setLesseeName(device.getLesseeName());
				commandRecordRepository.saveAndFlush(commandRecord);
//				sendMessage.setType(0);// 发送关机指令
				sendMessage.setTurnOnOff(0);
				nowType = 0;
			} else {
//				if (!SessionKeyUtil.isValidSessionKey(dateFormat,
//						reciveMessage.getKeyCreateDate(),
//						reciveMessage.getRandomNum(), device.getSessionKey())) {
//					// 如果sessionKey不合法,重新生成
//					SessionKey newSessionKey = SessionKeyUtil
//							.generateSessionKey();
//					// 数据库中更新sessionkey
//					deviceRepository.updateDeviceSessionKey(
//							JSON.toJSONString(newSessionKey), deviceId);
//					// 设置发送消息的sessionKey
//					sendMessage.setKeyCreateDate(dateFormat
//							.format(newSessionKey.getCreateDate()));
//					sendMessage.setRandomNum(newSessionKey.getRandomNum());
//				} else {
//					sendMessage.setKeyCreateDate("");
//					sendMessage.setRandomNum("");
//				}
//				sendMessage.setType(1);// 设置状态为允许开机
				sendMessage.setTurnOnOff(1);
				nowType = 1;
//				sendMessage.setUpdateCRTStatus(device.getUpdateCRT());
				sendMessage.setUpdateSystem(device.getUpdateCRT());
			}
		}
		if (reciveMessage.getCurrentStatus() != nowType) {
			deviceRepository.updateDeviceStatus(nowType, deviceId);// 更新设备运行状态
		}

		// if (sendMessage.getUpdateCRTStatus() == 1) {
		// sendMessage.setClientCRT(readFile("D://user1.crt"));
		// sendMessage.setClientKey(readFile("D://user1.key"));
		// deviceRepository.updateDeviceCRTStatus(0, deviceId);// 更新标志位
		// }

		// 需要更新证书,则发送文件
//		if (sendMessage.getUpdateCRTStatus() == 1) {
		if (sendMessage.getUpdateSystem() == 1) {
			// 创建新的证书文件
			// 生成设备证书文件
			ProcessBuilder pb = new ProcessBuilder(crtPath
					+ "new_client_cert.sh", deviceId + "");
			Process p = pb.start();
			try {
				p.waitFor();// 同步执行
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			sendMessage.setClientCRT(readFile(crtPath + "user/certificates/"
//					+ deviceId + ".crt"));// 读取证书文件
//			sendMessage.setClientKey(readFile(crtPath + "user/keys/" + deviceId
//					+ ".key"));// 读取私钥文件
			//读取更新文件
			//sendMessage.setUpdateFile(readFile(downloadPath));
			deviceRepository.updateDeviceCRTStatus(0, deviceId);// 更新标志位
		}

		sendMessage(JSON.toJSONString(sendMessage));// 发送消息
		// 将原始坐标转换为为百度坐标
		JSONObject jsonObject = HttpRequestUtil.sendGet(
				reciveMessage.getLongitude(), reciveMessage.getLatitude());
		if(jsonObject==null){
			return;
		}
		// 获取坐标所在省市信息
		JSONObject addressInfo = HttpRequestUtil.sendGet(
				jsonObject.getDouble("y"), jsonObject.getDouble("x"));

		// 处理gps信息
		DeviceRecord latestRecord = new DeviceRecord();
		latestRecord.setDeviceId(deviceId);
		latestRecord.setLongitude(jsonObject.getDouble("x"));
		latestRecord.setLatitude(jsonObject.getDouble("y"));
		latestRecord.setRealTime(new Date());
		latestRecord.setStatus(reciveMessage.getCurrentStatus());// 设置当前设备的状态
		if (addressInfo != null) {
			latestRecord.setProvince(addressInfo.getString("province"));
			latestRecord.setCity(addressInfo.getString("city"));
			latestRecord.setDistrict(addressInfo.getString("district"));
			deviceRepository.updateDeviceProvice(addressInfo.getString("province"), deviceId);
			deviceRepository.updateDeviceCity(addressInfo.getString("city"), deviceId);
			deviceRepository.updateDeviceDistrict(addressInfo.getString("district"), deviceId);
		}

		DeviceRecord deviceRecord = deviceRecordService
				.queryNewlyLocation(deviceId);

		if (deviceRecord == null) { // 设置为危险信息
			latestRecord.setLocationStatus(1);// 设置为危险信息
		} else {
			// 判断两个点之间的距离
			int distance = distFrom(deviceRecord.getLatitude(),
					deviceRecord.getLongitude(), latestRecord.getLatitude(),
					latestRecord.getLongitude());
			System.out.println(distance);
			if (distance >= 500) {
				latestRecord.setLocationStatus(1);// 设置为危险信息
			} else {
				latestRecord.setLocationStatus(0);// 正常信息
			}
		}
		deviceRecordRepository.saveAndFlush(latestRecord);// 保存gps信息

	}

	/**
	 * 打开连接
	 * 
	 * @param session request session
	 * @param clientId
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("machineId") String machineId) {
		this.machineId = machineId;
		//this.deviceId = deviceId;
		this.sessionDevice = session;
		System.out.println("Client connented:" + deviceId);
	}

	/**
	 * 关闭连接
	 */
	@OnClose
	public void onClose() {
		System.out.println("Connection closed");
	}

	/**
	 * 连接出错
	 * 
	 * @param session
	 * @param t
	 */
	@OnError
	public void error(Session session, Throwable t) {
		t.printStackTrace();

	}

	/**
	 * 
	 * @param message
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void sendMessage(String message) throws IOException,
			InterruptedException {
		sessionDevice.getBasicRemote().sendText(message);
	}

	/**
	 * 测量两个gps坐标之间的距离
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	private int distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371000; // 地球半径
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = (double) (earthRadius * c);
		return (int) dist;
	}

	/**
	 * 读取文件
	 * 
	 * @param file
	 * @return
	 */

	private String readFile(String file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(file)));
			StringBuffer buffer = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append("\n");
			}
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
