package com.yuyutechnology.exchange.utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yuyutechnology.exchange.ServerException;
/**
 * HTTP工具箱
 */
public final class HttpTookit
{
	public static Logger logger = LogManager.getLogger(HttpTookit.class);
	
	public static String sendGet4Oanda(String url, String param, String authorization){
		String result = "";
		BufferedReader in = null;
		try
		{
			String urlName = url;
			if (StringUtils.isNotBlank(param))
			{
				urlName = url + "?" + param;
			}
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			// 设置通用的请求属性
			conn.setRequestProperty("authorization", "Bearer " + authorization);
			// 建立实际的连接
			conn.connect();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result += "" + line;
			}
		}
		catch (Exception e){
//			result = e.getMessage();
			logger.error("sendGet error : {}", e.getMessage(), e);
		}
		// 使用finally块来关闭输入流
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	public static String sendGet(String url, String param){
		String result = "";
		BufferedReader in = null;
		try
		{
			String urlName = url;
			if (StringUtils.isNotBlank(param))
			{
				urlName = url + "?" + param;
			}
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("Content-Type",
					"text/html,application/xhtml+xml,application/xml");
			conn.setRequestProperty("Charset", "utf-8");
			// 建立实际的连接
			conn.connect();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result += "" + line;
			}
		}
		catch (Exception e){
//			result = e.getMessage();
			logger.error("sendGet error : {}", e.getMessage(), e);
		}
		// 使用finally块来关闭输入流
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		return result;
	}

	
	public static String sendPost4Form(String link, String param){
		BufferedWriter out = null;
		BufferedReader in = null;
		String body = "";
		try {
			URL url = new URL(link);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // 设置发送数据的格式
			connection.connect();
			out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
			out.write(param);
			out.flush();
			// 读取响应
			InputStream jnn = connection.getInputStream();
			in = new BufferedReader(new InputStreamReader(jnn, "UTF-8"));
			String line = "";
			while ((line = in.readLine()) != null) {
				body += "" + line;
			}
			if (connection.getResponseCode() == 200) {
				return body;
			}
		} catch (IOException e) {
			logger.error("send to request is failed: {}",e);
		} finally { // 关闭流
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				logger.error("close in or out io failed");
				ex.printStackTrace();
			}
		}
		return body;
	}
	
	public static String sendPost(String link, String param){
		logger.info("sendPost request is {}",param);
		BufferedWriter out = null;
		BufferedReader in = null;
		String body = "";
		try {
			URL url = new URL(link);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
			connection.connect();
			out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
			out.write(param);
			out.flush();
			// 读取响应
			if (connection.getResponseCode() != 200 && connection.getErrorStream() != null) {
				in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
			}else {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			}
			String line = "";
			while ((line = in.readLine()) != null) {
				body += "" + line;
			}
			if (connection.getResponseCode() == 200) {
				return body;
			}else{
				throw new ServerException(body);
			}
		} catch (Exception e) {
			logger.error("send to request is failed: {}",e);
		} finally { // 关闭流
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				logger.error("close in or out io failed");
			}
		}
		return body;
	}
	
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}
}
