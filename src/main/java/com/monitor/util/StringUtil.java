/*
 * Copyright Notice:
 *    Copyright (c) 2005-2009 China Financial Certification Authority(CFCA)
 *    A-1 You An Men Nei Xin An Nan Li, Xuanwu District, Beijing ,100054, China
 *    All rights reserved.
 *
 *    This software is the confidential and proprietary information of
 *    China Financial Certification Authority ("Confidential Information").
 *    You shall not disclose such Confidential Information and shall use 
 *    it only in accordance with the terms of the license agreement you 
 *    entered into with CFCA.
 */

package com.monitor.util;

import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static final String DEFAULT_CHARSET = "UTF-8";

	public static String escape(String src, HashMap<String, String> hashMap) {
		if (src == null || src.trim().length() == 0) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		StringCharacterIterator sci = new StringCharacterIterator(src);
		for (char c = sci.first(); c != StringCharacterIterator.DONE; c = sci
				.next()) {
			String ch = String.valueOf(c);
			if (hashMap.containsKey(ch)) {
				ch = (String) hashMap.get(ch);
			}
			sb.append(ch);
		}
		return sb.toString();
	}

	public static String escapeSQL(String input) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("'", "''");
		return escape(input, hashMap);
	}

	public static String escapeXML(String input) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("<", "&lt;");
		hashMap.put(">", "&gt;");
		hashMap.put("'", "&apos;");
		hashMap.put("\"", "&quot;");
		hashMap.put("&", "&amp;");
		return escape(input, hashMap);
	}

	public static String removeComma(String string) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			if (',' != string.charAt(i)) {
				sb.append(string.charAt(i));
			}
		}
		return sb.toString();
	}

	/**
	 * 将字符串中的非字符转换成字母X
	 * 
	 */
	public static String toLetterOrDigit(String string) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			if (Character.isLetterOrDigit(string.charAt(i))) {
				sb.append(string.charAt(i));
			} else {
				sb.append("X");
			}
		}
		return sb.toString();
	}

	public static String bytes2hex(byte bytes) {
		String result = "";

		result = Integer.toHexString(bytes & 0xFF);
		if (result.length() == 1) {
			result = "0" + result;
		}

		return result.toUpperCase();
	}

	public static String bytes2hex(byte[] bytes) {
		String result = "";
		String b = "";
		for (int i = 0; i < bytes.length; i++) {
			b = Integer.toHexString(bytes[i] & 0xFF);
			if (b.length() == 1) {
				b = "0" + b;
			}
			result += b;
		}
		return result.toUpperCase();
	}

	public static byte[] hex2bytes(String hexString) {

		hexString = hexString.toUpperCase();

		char[] chars = hexString.toCharArray();
		byte[] bytes = new byte[chars.length / 2];

		int index = 0;

		for (int i = 0; i < chars.length; i += 2) {
			byte newByte = 0x00;

			newByte |= char2byte(chars[i]);
			newByte <<= 4;

			newByte |= char2byte(chars[i + 1]);

			bytes[index] = newByte;

			index++;
		}
		return bytes;
	}

	public static byte char2byte(char ch) {
		switch (ch) {
		case '0':
			return 0x00;
		case '1':
			return 0x01;
		case '2':
			return 0x02;
		case '3':
			return 0x03;
		case '4':
			return 0x04;
		case '5':
			return 0x05;
		case '6':
			return 0x06;
		case '7':
			return 0x07;
		case '8':
			return 0x08;
		case '9':
			return 0x09;
		case 'A':
			return 0x0A;
		case 'B':
			return 0x0B;
		case 'C':
			return 0x0C;
		case 'D':
			return 0x0D;
		case 'E':
			return 0x0E;
		case 'F':
			return 0x0F;
		default:
			return 0x00;
		}
	}

	/*
	 * Converts a byte to hex digit and writes to the supplied buffer
	 */
	private static void byte2hex(byte b, StringBuffer sb) {
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		sb.append(hexChars[high]);
		sb.append(hexChars[low]);
	}

	/**
	 * Converts a byte array to hex string
	 * 
	 * @param bytes
	 * @param c
	 */
	public static String toHexString(byte[] bytes, char c) {
		StringBuffer sb = new StringBuffer();
		int len = bytes.length;
		for (int i = 0; i < len; i++) {
			byte2hex(bytes[i], sb);
			if (i < len - 1) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String toHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		int len = bytes.length;
		for (int i = 0; i < len; i++) {
			byte2hex(bytes[i], sb);
		}
		return sb.toString();
	}

	/**
	 * 判断字符串是否为�?
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否不为空
	 * 
	 */
	public static boolean isNotEmpty(String str) {
		if (str != null && !"".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	public static String trim(String string) {
		if (isEmpty(string)) {
			return "";
		} else {
			return string.trim();
		}
	}

	/**
	 * a-bA-B0-9 +-=?./: space chinese
	 * 
	 * @param string
	 * @return
	 */
	public static boolean validate(String string) {
		Pattern pattern = Pattern
				.compile("^[\\!\\w\\.\\?\\+\\-\\=\\/\\:\\,\\s\u4E00-\u9FA5]*$");
		Matcher m = pattern.matcher(string);
		return m.matches();
	}

	public static boolean containChinese(String string) {
		Pattern pattern = Pattern.compile("^[\u4E00-\u9FA5]{1}$");
		Matcher m = pattern.matcher(string);
		return m.matches();
	}

	public static boolean isChineseOnly(String string) {
		Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]{0,}$");
		Matcher m = pattern.matcher(string);
		System.out.println(m.matches());
		return m.matches();

	}

	public static boolean isEmail(String string) {
		Pattern pattern = Pattern.compile("^[\u4E00-\u9FA5]{1}$");
		Matcher m = pattern.matcher(string);
		return true;

	}

	public static boolean isNumeric(String string) {
		Pattern pattern = Pattern
				.compile("^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$");
		Matcher m = pattern.matcher(string);
		System.out.println(m.matches());
		return m.matches();
	}

	public static void main(String[] args) {
		System.out.println(containChinese(""));
	}

}
