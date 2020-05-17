package com.gm.botpets.nlp.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gm.botpets.nlp.model.NLPResponse;

public class WSUtil {

	public static ResponseEntity<Object> getResponseEntity(Object object) {
		return new ResponseEntity<Object>(object, HttpStatus.OK);
	}

	public static NLPResponse getDefaultAPIResponse() {
		NLPResponse response = new NLPResponse();
		return response;
	}
}
