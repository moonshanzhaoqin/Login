/**
 * 
 */
package com.yuyutechnology.exchange.cfg;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

/**
 * @author silent.sun
 *
 */
public class CustomSerializerProvider extends DefaultSerializerProvider{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5351085044443512403L;

	public CustomSerializerProvider(){
		super();
	}

	public CustomSerializerProvider(SerializerProvider src,SerializationConfig config,SerializerFactory f){
		super(src,config,f);
	}

	@Override
	public DefaultSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
		return new CustomSerializerProvider(this,config,jsf);
	}

	@Override
	public JsonSerializer<Object> findNullValueSerializer(BeanProperty property) throws JsonMappingException {
		if(property.getType().getRawClass().equals(String.class)){
			return new JsonSerializer<Object>() {
				@Override
				public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
						throws IOException, JsonProcessingException {
					gen.writeString("");
				}
			};
		}
		if(property.getType().isArrayType() || property.getType().isCollectionLikeType()){
			return new JsonSerializer<Object>() {
				@Override
				public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
						throws IOException, JsonProcessingException {
					gen.writeStartArray();
					gen.writeEndArray();
				}
			};
		}
		return super.findNullValueSerializer(property);
	}
}