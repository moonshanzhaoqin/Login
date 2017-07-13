package com.yuyutechnology.exchange.goldpay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.util.HttpTookit;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.ResourceUtils;

@Service
public class GoldpayManager {
	public static Logger logger = LogManager.getLogger(GoldpayManager.class);

	public GoldpayUser getGoldpayInfo(String accessToken) {
		String result = HttpTookit.sendGet(ResourceUtils.getBundleValue4String("goldpayUas.url") + "unity/userInfo.do?access_token=" + accessToken, null);
		logger.info("result==={}", result);
		if (StringUtils.isNotEmpty(result)) {
			GoldpayInfo goldpayInfo = JsonBinder.getInstance().fromJson(result, GoldpayInfo.class);
			if (goldpayInfo!=null && goldpayInfo.getRetCode() == 1) {// 成功
				return goldpayInfo.getOauthGoldqUser();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public GoldpayUser checkGoldpay(String userName, String password) {
		StringBuilder param = new StringBuilder();
		param.append("client_id=").append(ResourceUtils.getBundleValue4String("goldpayUas.client.id", "exchange-client")).append("&");
		param.append("client_secret=").append(ResourceUtils.getBundleValue4String("goldpayUas.client.key", "exchange")).append("&");
		param.append("grant_type=").append("password").append("&");
		param.append("username=").append(userName.trim()).append("&");
		param.append("password=").append(password);
		logger.info("param to string : {}",param.toString());
		String result = sendPost(ResourceUtils.getBundleValue4String("goldpayUas.url") + "oauth/token", param.toString());
		logger.info("result==={}", result);
		if (StringUtils.isNotEmpty(result)) {
			Map<String, String> resultMap = JsonBinder.getInstance().fromJson(result, HashMap.class);
			if (StringUtils.isNotBlank(resultMap.get("access_token"))) {
				return getGoldpayInfo(resultMap.get("access_token"));
			}
		}
		return null;
	}
	
	private String sendPost(String link, String param){
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
			logger.warn("send to uas check failed {}",e.getMessage());
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
}
