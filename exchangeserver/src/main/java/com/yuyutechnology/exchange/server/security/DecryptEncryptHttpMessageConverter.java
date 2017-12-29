package com.yuyutechnology.exchange.server.security;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.yuyutechnology.exchange.cfg.CustomSerializerProvider;

/**
 * @author silent.sun
 *
 */
@Component
public class DecryptEncryptHttpMessageConverter extends MappingJackson2HttpMessageConverter {

	@Autowired
    private DecryptEncryptBodyProcessor decryptEncryptBodyProcessor;
	
	private final String currentNameSuffix = "4String";
	
	@PostConstruct
	public void init () {
//		objectMapper.setSerializationInclusion(Include.ALWAYS);
        SimpleModule s = new SimpleModule();
        s.addSerializer(double.class, new JsonSerializer<Double>(){
			@Override
			public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeNumber(value);
				gen.writeStringField(gen.getOutputContext().getCurrentName()+currentNameSuffix,Double.toString(value));
			}
        });
        s.addSerializer(Double.class, new JsonSerializer<Double>(){
			@Override
			public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeNumber(value);
				gen.writeStringField(gen.getOutputContext().getCurrentName()+currentNameSuffix,Double.toString(value));
			}
        });
        s.addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>(){
			@Override
			public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeNumber(value);
				gen.writeStringField(gen.getOutputContext().getCurrentName()+currentNameSuffix,value.toString());
			}
        });
        objectMapper.setSerializerProvider(new CustomSerializerProvider());
        objectMapper.registerModule(s);
	}
	
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
