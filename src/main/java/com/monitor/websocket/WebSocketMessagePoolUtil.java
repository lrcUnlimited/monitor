package com.monitor.websocket;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Description: WebSocket工具类
 * 
 * @date 2014-8-21下午1:18:05
 */
public class WebSocketMessagePoolUtil {
	// 保存连接的MAP容器
	private static Map<String, WebSocketHandler> connections = new HashMap<String, WebSocketHandler>(
			1024);
	// 保存session的容器
	private static Map<String, Set<String>> sessionMap = new HashMap<String, Set<String>>(
			1024);

	/**
	 * 添加连接
	 * 
	 * @author malin@tsinghuabigdata.com
	 * @param WebSocketHandler
	 *            wsh webSocket处理类
	 * @date 2014-8-21下午1:18:05
	 */
	public static void addMessageWSH(WebSocketHandler wsh) {
		// System.out.println("user : " + wsh.getUserId() + " join..");
		synchronized (WebSocketMessagePoolUtil.class) {
			Set<String> sessionIdList = sessionMap.get(wsh.getUserId()) == null ? new HashSet<String>()
					: sessionMap.get(wsh.getUserId());
			sessionIdList.add(wsh.getSessionId());
			sessionMap.put(wsh.getUserId(), sessionIdList);
			connections.put(wsh.getSessionId(), wsh);
			// System.out.println("connections:" + connections.size() +
			// ";users:" + sessionMap.size());
		}
	}

	/**
	 * 获取在线会话
	 * 
	 * @author malin@tsinghuabigdata.com
	 * @date 2014-8-21下午1:18:05
	 */
	public static Set<String> getOnlineSession() {
		return connections.keySet();
	}

	/**
	 * 获取在线用户
	 * 
	 * @author malin@tsinghuabigdata.com
	 * @date 2014-8-21下午1:18:05
	 */
	public static Set<String> getOnlineUser() {
		return sessionMap.keySet();
	}

	/**
	 * 移除连接
	 * 
	 * @author malin@tsinghuabigdata.com
	 * @param WebSocketHandler
	 *            wsh webSocket处理类
	 * @date 2014-8-21下午1:18:05
	 */
	public static void removeMessageWSH(WebSocketHandler wsh) {
		// System.out.println("user : " + wsh.getSessionId() + " exit..");
		synchronized (WebSocketMessagePoolUtil.class) {
			Set<String> sessionIdList = sessionMap.get(wsh.getUserId());
			if (sessionIdList != null && sessionIdList.size() > 0) {
				sessionIdList.remove(wsh.getSessionId());
			}
			if (sessionIdList.size() == 0) {
				sessionMap.remove(wsh.getUserId());
			}
			connections.remove(wsh.getSessionId());
			// System.out.println("connections:" + connections.size() +
			// ";users:" + sessionMap.size());
		}
	}

	/**
	 * 向指定会话发送消息
	 * 
	 * @author malin@tsinghuabigdata.com
	 * @param sessionId
	 *            会话ID
	 * @param message
	 *            消息
	 * @date 2014-8-21下午1:18:05
	 */
	public static void sendMessageToSession(String sessionId, String message) {
		try {
			// System.out.println("send message to session : " + sessionId +
			// " ,message content : " + message);
			WebSocketHandler wsh = connections.get(sessionId);
			if (wsh != null) {
				wsh.sendMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向指定用户发送消息
	 * 
	 * @author malin@tsinghuabigdata.com
	 * @param userId
	 *            需要发送消息的用户ID
	 * @param message
	 *            发送的消息内容
	 * @date 2014-8-21下午1:18:05
	 */
	public static void sendMessageToUser(String userId, String message) {
		Set<String> sessionIdList = sessionMap.get(userId);
		if (!sessionIdList.isEmpty()) {
			for (String sessionId : sessionIdList) {
				sendMessageToSession(sessionId, message);
			}
		}
	}

	/**
	 * 向所有用户发送消息
	 * 
	 * @author malin@tsinghuabigdata.com
	 * @param message
	 *            发送的消息内容
	 * @date 2014-8-21下午1:18:05
	 */
	public static void sendMessage(String message) {
		try {
			for (WebSocketHandler wsh : connections.values()) {
				if (wsh != null) {
					// System.out.println("send message to user : " +
					// wsh.getUserId() + " ,message content : " + message);
					wsh.sendMessage(message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
