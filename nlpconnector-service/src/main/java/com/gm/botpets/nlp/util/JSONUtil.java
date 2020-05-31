package com.gm.botpets.nlp.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.botpets.nlp.model.BPNLPRequest;

@Component
public class JSONUtil {

	 	public static BPNLPRequest getBPNLPRequestObject(Object jsonObject)
	 			throws IOException, NoSuchMethodException, SecurityException, InstantiationException,
	 			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	 		ObjectMapper cortexObjectMapper = new ObjectMapper();
	 		Object convertToObjectFromJson = cortexObjectMapper.convertValue(jsonObject, BPNLPRequest.class);
	 		BPNLPRequest bpNLPRequest = (BPNLPRequest) convertToObjectFromJson;
	 		return bpNLPRequest;
	 	}
}
