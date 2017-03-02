package com.yuyutechnology.exchange.server.security;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.yuyutechnology.exchange.server.controller.ExchangeController;
import com.yuyutechnology.exchange.server.security.annotation.RequestDecryptBody;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;

/**
 * @author silent.sun
 *
 */
public class DecryptEncryptBodyMethodProcessor extends RequestResponseBodyMethodProcessor {

	public static Logger logger = LogManager.getLogger(DecryptEncryptBodyMethodProcessor.class);

    public DecryptEncryptBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestDecryptBody.class);
    }

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return (AnnotationUtils.findAnnotation(returnType.getContainingClass(), ResponseEncryptBody.class) != null ||
				returnType.getMethodAnnotation(ResponseEncryptBody.class) != null);
	}

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
    	logger.info("RequestURI:{}", webRequest.getNativeRequest(HttpServletRequest.class).getRequestURI());
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }

    @Override
    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter methodParam, Type paramType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
    	logger.info("RequestURI:{}", webRequest.getNativeRequest(HttpServletRequest.class).getRequestURI());
        return super.readWithMessageConverters(webRequest, methodParam, paramType);
    }

}
