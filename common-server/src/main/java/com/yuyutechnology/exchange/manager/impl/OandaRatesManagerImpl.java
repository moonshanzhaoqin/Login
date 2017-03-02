package com.yuyutechnology.exchange.manager.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.oanda.OandaRespData;

@Service
public class OandaRatesManagerImpl implements OandaRatesManager {
	
	public static Logger logger = LoggerFactory.getLogger(OandaRatesManagerImpl.class);
	
	@Override
	public OandaRespData getCurrentPrices(String instruments){
		
		OandaRespData oandaRespData = null;
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
        try {
            // Set these variables to whatever personal ones are preferred
            String domain = "https://api-fxpractice.oanda.com";
            String access_token = "d413e2cd916ebc4613376c3a3ca826ae-ebdc8079ec4cca1b1d650ea030036226";

            HttpUriRequest httpGet = new HttpGet(domain + "/v1/prices?instruments=" + instruments);
            httpGet.setHeader(new BasicHeader("Authorization", "Bearer " + access_token));
            
            logger.info("Executing request: {}",httpGet.getRequestLine());

            HttpResponse resp = httpClient.execute(httpGet);
            HttpEntity entity = resp.getEntity();

            if (resp.getStatusLine().getStatusCode() == 200 && entity != null) {
                InputStream stream = entity.getContent();
                String line;
                StringBuffer lineSb = new StringBuffer();
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                while ((line = br.readLine()) != null) {
                    lineSb.append(line);
                }
                
                logger.info("Executing response: {}",lineSb.toString());
                
                if((lineSb.toString()).contains("#errors")){
                	return null;
                }else{
                	oandaRespData = JsonBinder.getInstance().fromJson(lineSb.toString(), OandaRespData.class);
                }
                
            } else {
                // print error message
                String responseString = EntityUtils.toString(entity, "UTF-8");
                logger.info("responseString : {}",responseString);
            }

        }catch(ClientProtocolException e1){
        	
        }catch(IOException e1){
        	
        }finally {
            httpClient.getConnectionManager().shutdown();
        }
		
        return oandaRespData;
	}

}
