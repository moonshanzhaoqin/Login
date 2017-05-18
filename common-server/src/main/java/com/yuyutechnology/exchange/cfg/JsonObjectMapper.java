/**
 * 
 */
package com.yuyutechnology.exchange.cfg;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author silent.sun
 *
 */
public class JsonObjectMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3659875234079482330L;

    public JsonObjectMapper(){
        super();
        this.setSerializationInclusion(Include.NON_NULL);
//        // 空值处理为空串  
//        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {  
//            @Override  
//            public void serialize(Object value, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {  
//            	if (value instanceof String) {
//            		jg.writeString("");  
//            	}
//            }  
//        });
    } 
}
