/**
 * 
 */
package com.yuyutechnology.exchange.cfg;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author silent.sun
 *
 */
public class ExJsonObjectMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3659875234079482330L;
	
	public static final String CURRENT_NAME_SUFFIX = "4String";

	public ExJsonObjectMapper() {
		super();
//		this.setSerializationInclusion(Include.NON_EMPTY);
		 // 空值处理为空串
//		this.getSerializerProvider().setNullValueSerializer(new
//				JsonSerializer<Object>() {
//			@Override
//			public void serialize(Object value, JsonGenerator jg, SerializerProvider sp)
//					throws IOException, JsonProcessingException {
//				if (value instanceof String) {
//					jg.writeString("");
//				}
//			}
//		});
        SimpleModule s = new SimpleModule();
        
        s.addDeserializer(String.class, new JsonDeserializer<String>(){
			@Override
			public String deserialize(JsonParser p, DeserializationContext ctxt)
					throws IOException, JsonProcessingException {
				 return p.getCurrentToken().asString().trim();
			}
        });
        s.addSerializer(double.class, new JsonSerializer<Double>(){
			@Override
			public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeNumber(value);
				gen.writeStringField(gen.getOutputContext().getCurrentName()+CURRENT_NAME_SUFFIX,Double.toString(value));
			}
        });
        s.addSerializer(Double.class, new JsonSerializer<Double>(){
			@Override
			public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeNumber(value);
				gen.writeStringField(gen.getOutputContext().getCurrentName()+CURRENT_NAME_SUFFIX,Double.toString(value));
			}
        });
        s.addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>(){
			@Override
			public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeNumber(value);
				gen.writeStringField(gen.getOutputContext().getCurrentName()+CURRENT_NAME_SUFFIX,value.toString());
			}
        });
        this.setSerializerProvider(new ExJsonSerializerProvider());
        this.registerModule(s);
	}
}
