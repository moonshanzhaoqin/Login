package com.yuyutechnology.exchange.util;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
	
	public static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	
	private static Object lock = new Object();
	private static CloseableHttpClient client;
	
	private static CloseableHttpClient getClient() {
		if (client == null) {
			synchronized (lock)
			{
				if (client == null)
				{
					PoolingHttpClientConnectionManager cm =new PoolingHttpClientConnectionManager();
			        cm.setMaxTotal(50);
			        cm.setDefaultMaxPerRoute(20);
					RequestConfig config = RequestConfig.custom()
			                .setConnectTimeout(3000) 
			                .setSocketTimeout(3000).build();
					client = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(config).disableAutomaticRetries().build();
				}
			}
		}
		return client;
	}
	
	public static String sendGet(String domain,String params,BasicHeader basicHeader){
		String urlName = domain;
		if(StringUtils.isNotBlank(params)){
			urlName = domain + "?" + params;
		}
		String result = "";
		CloseableHttpResponse resp=null;
        try {
        	HttpGet httpGet = new HttpGet(urlName);
            httpGet.setHeader(basicHeader);
            resp = getClient().execute(httpGet);
            HttpEntity entity = resp.getEntity();
            if (resp.getStatusLine().getStatusCode() == 200 && entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");  
            } else {
                String responseString = EntityUtils.toString(entity, "UTF-8");
                logger.warn("sendGet url : {},  result : {}",urlName, responseString);
            }
        }catch(Exception e){
        	logger.warn("sendGet url : {},  result : {}",urlName, e.getMessage());
        }finally {
        	 if(resp!=null){
        		 try {
        			 resp.close();
        		 } catch (IOException e) {
        		 }
        	 }
        }
		return result;
	}
	
	public static void main(String[] args){
		
		String domain = "https://api-fxpractice.oanda.com/v1/prices";
		String params = "instruments=USD_JPY";
		BasicHeader basicHeader = new BasicHeader("Authorization", 
				"Bearer " + "d413e2cd916ebc4613376c3a3ca826ae-ebdc8079ec4cca1b1d650ea030036226");
		String result = sendGet(domain,params,basicHeader);
		logger.info("result : {}",result);
	}
	
	
	

}
