/**
 * 
 */
package com.yuyutechnology.exchange.cfg;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author silent.sun
 *
 */
public class JsonObjectMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3659875234079482330L;

	public JsonObjectMapper() {
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
        s.addSerializer(Double.class, new JsonSerializer<Double>(){
			@Override
			public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeNumber(value);
				gen.writeStringField(gen.getOutputContext().getCurrentName()+"4String",Double.toString(value));
			}
        });
        s.addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>(){
			@Override
			public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeNumber(value);
				gen.writeStringField(gen.getOutputContext().getCurrentName()+"4String",value.toString());
			}
        });
        this.setSerializerProvider(new CustomSerializerProvider());
        this.registerModule(s);
	}
}
