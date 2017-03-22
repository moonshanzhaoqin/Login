package com.yuyutechnology.exchange.server.security;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;

/**
 * @author silent.sun
 *
 */
@Component
public class DecryptEncryptHttpMessageConverter extends MappingJackson2HttpMessageConverter {

	@Autowired
    private DecryptEncryptBodyProcessor decryptEncryptBodyProcessor;
    
	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		JavaType javaType = getJavaType(type, contextClass);
		try {
	        if (decryptEncryptBodyProcessor != null) {
	            String input = decryptEncryptBodyProcessor.decryptRequestBody(inputMessage);
	            return this.objectMapper.readValue(input, javaType);
	        }
		}
		catch (IOException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
		return null;
	}

	@Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    	return read(clazz, inputMessage);
    }

	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		try {
			Class<?> serializationView = null;
			Object value = object;
			if (value instanceof MappingJacksonValue) {
				MappingJacksonValue container = (MappingJacksonValue) object;
				value = container.getValue();
				serializationView = container.getSerializationView();
			}
			String jsonString = "";
			if (serializationView != null) {
				jsonString = this.objectMapper.writerWithView(serializationView).writeValueAsString(value);
			}
			else {
				jsonString = this.objectMapper.writeValueAsString(value);
			}
	        if (decryptEncryptBodyProcessor != null) {
	        	jsonString = decryptEncryptBodyProcessor.encryptResponseBody(jsonString, outputMessage.getHeaders());
	        }
			JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
			outputMessage.getBody().write(jsonString.getBytes(encoding.getJavaName()));
			
		}
		catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException("Could not write content: " + ex.getMessage(), ex);
		}
	}
	
	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		writeInternal(object, outputMessage);
	}
}
