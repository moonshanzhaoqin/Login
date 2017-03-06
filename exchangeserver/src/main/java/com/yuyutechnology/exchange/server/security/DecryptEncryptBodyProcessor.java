package com.yuyutechnology.exchange.server.security;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import com.yuyutechnology.exchange.server.controller.request.DecryptRequest;
import com.yuyutechnology.exchange.server.controller.response.EncryptResponse;
import com.yuyutechnology.exchange.utils.JsonBinder;

/**
 * @author silent.sun
 *
 */
public abstract class DecryptEncryptBodyProcessor {

	public static Logger logger = LogManager.getLogger(DecryptEncryptBodyProcessor.class);
    
    private Charset charset = Charset.forName("UTF-8");

    public final String decryptRequestBody(HttpInputMessage inputMessage) throws IOException {
        InputStream inputStream = inputMessage.getBody();
        String input = IOUtils.toString(inputStream, charset);
        HttpHeaders httpHeaders = inputMessage.getHeaders();
        DecryptRequest request = JsonBinder.getInstance().fromJson(input, DecryptRequest.class);
        return doDecryptRequestBody(request.getContent(), httpHeaders);
    }

    public final String encryptResponseBody(String input, HttpHeaders httpHeaders) {
        EncryptResponse response = new EncryptResponse();
        response.setContent(doEncryptResponseBody(input, httpHeaders));
        return JsonBinder.getInstance().toJson(response);
    }

    protected String doDecryptRequestBody(String input, HttpHeaders httpHeaders) {
        return input;
    }

    protected String doEncryptResponseBody(String input, HttpHeaders httpHeaders) {
        return input;
    }

}
