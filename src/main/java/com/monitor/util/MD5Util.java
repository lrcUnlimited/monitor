package com.monitor.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.monitor.exception.CodeException;

public class MD5Util {

	/**
	 * MD5加密算法
	 * 
	 * @param str
	 * @return
	 * @throws CodeException
	 */
	public static String getMD5Str(String str) throws CodeException {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new CodeException("没有该加密算法");
		} catch (UnsupportedEncodingException e) {
			throw new CodeException("不支持的编码");
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	public static void main(String[] args) throws CodeException {
		System.out.println(new MD5Util().getMD5Str("1"));
	}
}
