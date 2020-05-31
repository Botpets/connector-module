package com.gm.botpets.nlp.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.gm.botpets.nlp.exception.BPNLPConnectorException;
import com.gm.botpets.nlp.service.INLPService;

@Component
@Qualifier("googleNLPService")
public class GoogleNLPServiceImpl implements INLPService {

	@Override
	public Object invokeNLPEngine(String message, String sourceLanguage, String sessionId) throws BPNLPConnectorException {
		// TODO Auto-generated method stub
		return null;
	}

}
