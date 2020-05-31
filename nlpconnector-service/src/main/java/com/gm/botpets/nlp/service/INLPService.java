package com.gm.botpets.nlp.service;

import com.gm.botpets.nlp.exception.BPNLPConnectorException;

public interface INLPService {
	public Object invokeNLPEngine(String message, String sourceLanguage, String sessionId) throws BPNLPConnectorException;
}
