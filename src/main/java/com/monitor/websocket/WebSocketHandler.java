package com.monitor.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/websocket/{clientId}")
public class WebSocketHandler {
	private String userId;
	private Session sessionUser;

	public String getUserId() {
		return userId;
	}

	public String getSessionId() {
		return sessionUser.getId();
	}

	/**
	 * 
	 * @param message
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@OnMessage
	public void onMessage(String message) throws IOException,
			InterruptedException {
		System.out.println(sessionUser.getId());
		System.out.println("Received: " + message);

		sessionUser.getBasicRemote().sendText(
				"this is reply message from " + sessionUser.getId());
	}

	/**
	 * 打开连接
	 * 
	 * @param session
	 * @param clientId
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("clientId") String clientId) {
		userId = clientId;
		sessionUser = session;
		WebSocketMessagePoolUtil.addMessageWSH(this);
		System.out.println("Client connected:" + sessionUser.getId());
		System.out.println("Client connented:" + clientId);
	}

	/**
	 * 关闭连接
	 */
	@OnClose
	public void onClose() {
		// System.out.println("Connection closed");
		WebSocketMessagePoolUtil.removeMessageWSH(this);
	}

	/**
	 * 
	 * @param message
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void sendMessage(String message) throws IOException,
			InterruptedException {
		sessionUser.getBasicRemote().sendText(message);
	}

}
