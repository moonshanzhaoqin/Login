package com.yuyutechnology.exchange.server.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.server.controller.request.DecryptRequest;
import com.yuyutechnology.exchange.server.controller.response.EncryptResponse;
import com.yuyutechnology.exchange.util.AESCipher;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.ResourceUtils;

/**
 * @author silent.sun
 *
 */
@Component
public class DecryptEncryptBodyProcessorImpl extends DecryptEncryptBodyProcessor {

//	private static final String DEFAULT_KEY = "anytime_exchange";
	
    @Override
    protected String doDecryptRequestBody(String input, HttpHeaders httpHeaders) {
        logger.info("==========doDecryptRequestBody input, {}", input);
        String key = ResourceUtils.getBundleValue4String("aes.key");
        if (StringUtils.isNotBlank(key)) {
        	try {
                DecryptRequest request = JsonBinder.getInstance().fromJson(input, DecryptRequest.class);
				input = AESCipher.decryptAES(request == null ? "" : StringUtils.defaultString(request.getContent()),key);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
					| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
					| UnsupportedEncodingException e) {
				input = "";
			}
        }
        logger.info("==========doDecryptRequestBody input(decryptAES), {}", input);
        return input;
    }

    @Override
    protected String doEncryptResponseBody(String input, HttpHeaders httpHeaders) {
    	logger.info("==========doEncryptResponseBody input, {}", input);
        String key = ResourceUtils.getBundleValue4String("aes.key");
        if (StringUtils.isNotBlank(key)) {
        	try {
				input = AESCipher.encryptAES(input,key);
		        EncryptResponse response = new EncryptResponse();
		        response.setContent(input);
		        input = JsonBinder.getInstance().toJson(response);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
					| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
					| UnsupportedEncodingException e) {
				input = "";
			}
        }
        logger.info("==========doEncryptResponseBody input(encryptAES), {}", input);
        return input;
    }

}
