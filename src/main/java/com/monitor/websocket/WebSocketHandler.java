package com.monitor.websocket;

import java.io.IOException;
import java.util.Date;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.monitor.dao.device.DeviceRepository;
import com.monitor.dao.devicerecord.DeviceRecordRepository;
import com.monitor.model.Device;
import com.monitor.model.Message;
import com.monitor.model.SessionKey;
import com.monitor.util.SessionKeyUtil;

@Service(value = "websocketService")
@ServerEndpoint(value = "/websocket/{deviceId}")
public class WebSocketHandler {
	@Autowired
	DeviceRepository deviceRepository;
	private int deviceId;
	private Session sessionDevice;

	public int getDeviceId() {
		return deviceId;
	}

	public String getSessionId() {
		return sessionDevice.getId();
	}

	/**
	 * 
	 * @param message
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@OnMessage
	public void onMessage(String msg) throws IOException, InterruptedException {
		/*
		Message reciveMessage = JSON.parseObject(msg, Message.class);// 接收消息
		Message sendMessage = new Message();// 发送消息
		Date nowDate = new Date();// 当前日期

		// 检查设备的状态
		Device device = deviceRepository.findOne(deviceId);
		// 设备状态已经为off，直接关机
		if (device.getManageDeviceStatus() == 0) {
			sendMessage.setType(0);
		} else {
			if (nowDate.after(device.getValidTime())) {
				// 设备已经过期,更新数据库中的设备管理状态为off
				deviceRepository.updateManageDeviceStatus(0, deviceId);
				sendMessage.setType(0);// 发送关机指令

			} else {
				if (SessionKeyUtil.isValidSessionKey(
						reciveMessage.getSessionKey(), device.getSessionKey())) {
					sendMessage.setSessionKey(reciveMessage.getSessionKey());
				} else {
					// 如果sessionKey不合法,重新生成
					String newSessionKey = SessionKeyUtil.generateSessionKey();
					// 数据库中更新sessionkey
					deviceRepository.updateDeviceSessionKey(newSessionKey,
							deviceId);
					// 设置发送消息的sessionKey
					sendMessage.setSessionKey(newSessionKey);
				}
				sendMessage.setType(1);// 设置状态为允许开机
			}
		}

		sessionDevice.getBasicRemote().sendText(JSON.toJSONString(sendMessage));// 发送消息
		*/

		/**
		 * 处理坐标信息，上传到百度，同时保存到数据库
		 */

	}

	/**
	 * 打开连接
	 * 
	 * @param session
	 * @param clientId
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("deviceId") int deviceId) {
		this.deviceId = deviceId;
		this.sessionDevice = session;
		System.out.println("Client connented:" + deviceId);
	}

	/**
	 * 关闭连接
	 */
	@OnClose
	public void onClose() {
		// System.out.println("Connection closed");
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

}
