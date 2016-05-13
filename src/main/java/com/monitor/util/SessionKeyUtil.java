package com.monitor.util;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;
import com.monitor.model.SessionKey;

public class SessionKeyUtil {

	/**
	 * 生成随机数
	 * 
	 * @return
	 */
	private static String generateRandomNum() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid.toUpperCase();
	}

	/**
	 * 生成会话密钥
	 * 
	 * @return
	 */

	public static String generateSessionKey() {
		SessionKey sessionKey = new SessionKey();
		sessionKey.setRandomNum(generateRandomNum());
		sessionKey.setCreateDate(new Date());
		return JSON.toJSONString(sessionKey);
	}

	/**
	 * 判断两个时间相差的小时数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDateDiff(Date date1, Date date2) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	/**
	 * 验证sessionKey是否合法
	 * 
	 * @param sessionKey
	 *            :用户发送过来的
	 * @param dbSessionKey
	 *            :数据库中保存的
	 * @return
	 */
	public static boolean isValidSessionKey(String sessionKey,
			String dbSessionKey) {
		if (StringUtil.isEmpty(sessionKey)) {
			return false;
		}
		SessionKey reciveKey = JSON.parseObject(sessionKey, SessionKey.class);
		SessionKey dbKey = JSON.parseObject(dbSessionKey, SessionKey.class);
		if (reciveKey.getRandomNum().equals(dbKey.getRandomNum())) {
			if (getDateDiff(reciveKey.getCreateDate(), dbKey.getCreateDate()) < 24) {
				return true;
			}
		}
		return false;

	}

	public static void main(String[] args) {
		

	}
}
