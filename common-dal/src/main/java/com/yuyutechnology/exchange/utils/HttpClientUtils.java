package com.yuyutechnology.exchange.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
	
	public static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	
	@SuppressWarnings("deprecation")
	public static String sendGet(String domain,String params){
		
		String result = "";
		
		HttpClient httpClient = HttpClientBuilder.create().build();
        try {
        	String urlName = domain;
        	if(StringUtils.isNotBlank(params)){
        		urlName = domain + "?" + params;
        	}
            HttpUriRequest httpGet = new HttpGet(urlName);
//            httpGet.setHeader(new BasicHeader("Authorization", "Bearer " + access_token));
            logger.info("Executing request: {}",httpGet.getRequestLine());
            HttpResponse resp = httpClient.execute(httpGet);
            HttpEntity entity = resp.getEntity();
            if (resp.getStatusLine().getStatusCode() == 200 && entity != null) {
                InputStream stream = entity.getContent();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                while ((line = br.readLine()) != null) {
                	result += "" + line;
                }
            } else {
                // print error message
                String responseString = EntityUtils.toString(entity, "UTF-8");
                logger.info("responseString : {}",responseString);
            }

        }catch(ClientProtocolException e1){
        	result = e1.getMessage();
        	e1.printStackTrace();
        }catch(IOException e2){
        	result = e2.getMessage();
        	e2.printStackTrace();
        }finally {
            httpClient.getConnectionManager().shutdown();
        }
        
        logger.info("HttpClientUtils result : {}",result);
        
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public static String sendGet(String domain,String params,BasicHeader basicHeader){
		String result = "";
		HttpClient httpClient = HttpClientBuilder.create().build();
        try {
        	String urlName = domain;
        	if(StringUtils.isNotBlank(params)){
        		urlName = domain + "?" + params;
        	}
            HttpUriRequest httpGet = new HttpGet(urlName);
            httpGet.setHeader(basicHeader);
            logger.info("Executing request: {}",httpGet.getRequestLine());
            HttpResponse resp = httpClient.execute(httpGet);
            HttpEntity entity = resp.getEntity();
            if (resp.getStatusLine().getStatusCode() == 200 && entity != null) {
                InputStream stream = entity.getContent();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                while ((line = br.readLine()) != null) {
                	result += "" + line;
                }
            } else {
                // print error message
                String responseString = EntityUtils.toString(entity, "UTF-8");
                logger.info("responseString : {}",responseString);
            }

        }catch(ClientProtocolException e1){
        	result = e1.getMessage();
        	e1.printStackTrace();
        }catch(IOException e2){
        	result = e2.getMessage();
        	e2.printStackTrace();
        }finally {
            httpClient.getConnectionManager().shutdown();
        }
        
        logger.info("HttpClientUtils result : {}",result);
        
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
