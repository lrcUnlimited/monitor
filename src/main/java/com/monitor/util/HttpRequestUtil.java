package com.monitor.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 发送http请求公共服务类
 * 
 * @author li
 * 
 */
public class HttpRequestUtil {
	private static final String BAIDU_URL = "http://api.map.baidu.com/geoconv/v1/?&ak=ErVT7KaBBsrD5q4lQqbMDisw4R2ssOFh&from=1&to=5&coords=";

	/**
	 * 百度坐标转换接口
	 * 
	 * @param lng
	 * @param lat
	 * @return
	 */
	public static JSONObject sendGet(double lng, double lat) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = BAIDU_URL + lng + "," + lat;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			JSONObject jsonObject = JSON.parseObject(result);
			System.out.println(jsonObject.toJSONString());
			JSONObject obj = ((JSONObject) ((JSONArray) jsonObject
					.get("result")).get(0));
			return obj;
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				// DO NOTHING
			}
		}
		return null;

	}

	public static void main(String[] args) {

		System.out.println(HttpRequestUtil.sendGet(104.0982566, 30.676415));

	}
}
