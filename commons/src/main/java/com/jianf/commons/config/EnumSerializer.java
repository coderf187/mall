package com.jianf.commons.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author fengjian
 */
public class EnumSerializer extends JsonSerializer<Enum> {

    @Override
    public void serialize(Enum value, JsonGenerator jgen, SerializerProvider arg2) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("key", value.name());
        jgen.writeStringField("value", value.toString());
        jgen.writeEndObject();
    }

    
}
