package com.yuyutechnology.exchange.util;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
	
	
	public static String sendPost(String url,String params){
		
		CloseableHttpClient client4Post = null;
		CloseableHttpResponse response = null;
		String result = "";

		try {
			//创建一个httpclient对象
			client4Post = HttpClients.createDefault();
			//创建一个post对象
			HttpPost post = new HttpPost(url);
			//包装成一个Entity对象
//			StringEntity entity = new UrlEncodedFormEntity(params, "utf-8");
			StringEntity entity = new StringEntity(params,Charset.forName("UTF-8"));
			//设置请求的内容
	        post.setEntity(entity);
	        //设置请求的报文头部的编码
	        post.setHeader(
	            new BasicHeader("Content-Type", "application/json; charset=utf-8"));
	        //设置期望服务端返回的编码
	        post.setHeader(new BasicHeader("Accept", "application/json"));
	        //执行post请求
	        response = client4Post.execute(post);
	        //获取响应码
	        int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode == 200) {
	            //获取数据
	        	result = EntityUtils.toString(response.getEntity());
	            //输出
	            logger.warn("sendPost url : {},  result : {}",url, result);
	            
	        } else {
	            //输出
	        	logger.warn("sendPost url : {},  statusCode : {}",url, statusCode);
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//关闭response和client
	        try {
	        	response.close();
				client4Post.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
		
	}
	
	public static void main(String[] args){
		
//		String domain = "https://api-fxpractice.oanda.com/v1/prices";
//		String params = "instruments=USD_JPY";
//		BasicHeader basicHeader = new BasicHeader("Authorization", 
//				"Bearer " + "d413e2cd916ebc4613376c3a3ca826ae-ebdc8079ec4cca1b1d650ea030036226");
//		String result = sendGet(domain,params,basicHeader);
//		logger.info("result : {}",result);
		

		String result = sendPost("https://cloud888.yuyutechnology.com/notification/sendMail.do", "");
		
		System.out.println("result : "+result);
	}
	
	
	

}
