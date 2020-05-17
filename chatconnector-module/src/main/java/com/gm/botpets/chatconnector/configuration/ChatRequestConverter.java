package com.gm.botpets.chatconnector.configuration;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.botpets.chatconnector.model.ChatRequest;


public class ChatRequestConverter extends AbstractHttpMessageConverter<ChatRequest> {

    private static final FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected boolean supports(Class<?> clazz) {
        return (ChatRequest.class == clazz);
    }

    @Override
    protected ChatRequest readInternal(Class<? extends ChatRequest> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Map<String, String> vals = formHttpMessageConverter.read(null, inputMessage).toSingleValueMap();
        return mapper.convertValue(vals, ChatRequest.class);
    }

    @Override
    protected void writeInternal(ChatRequest chatRequest, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }
}