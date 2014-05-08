package com.zjhcsoft.qin.inner.controller;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zjhcsoft.qin.inner.ConfigContainer;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;


public class JsonpHttpMessageConverter extends MappingJackson2HttpMessageConverter {
    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator jsonGenerator = this.getObjectMapper().getFactory().createJsonGenerator(outputMessage.getBody(), encoding);
        try {
            jsonGenerator.writeRaw(ConfigContainer.JSONP_CALLBACK);
            jsonGenerator.writeRaw('(');
            this.getObjectMapper().writeValue(jsonGenerator, object);
            jsonGenerator.writeRaw(");");
            jsonGenerator.flush();
        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }
}
