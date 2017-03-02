package com.yuyutechnology.exchange.server.security;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * @author silent.sun
 *
 */
@Component
public class DecryptEncryptBodyProcessorImpl extends DecryptEncryptBodyProcessor {

    @Override
    protected String doDecryptRequestBody(String input, HttpHeaders httpHeaders) {
        log.info("==========doDecryptRequestBody, "+input);
        input = "{\"name\": \"silent\",\"age\": 30}";
        return super.doDecryptRequestBody(input, httpHeaders);
    }

    @Override
    protected String doEncryptResponseBody(String input, HttpHeaders httpHeaders) {
        log.info("==========doEncryptResponseBody");
        input = "123456789";
        return super.doEncryptResponseBody(input, httpHeaders);
    }

}
