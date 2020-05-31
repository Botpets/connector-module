package com.gm.botpets.nlp.handler;

import org.springframework.stereotype.Component;

import com.gm.botpets.nlp.exception.BPNLPConnectorException;
import com.gm.botpets.nlp.service.INLPService;

@Component
public class NLPEngineManager {

	private INLPService nlpService;

	public void setNlpService(INLPService nlpService) {
		this.nlpService = nlpService;
	}

	public Object invokeNLPEngine(String message, String sourceLanguage, String sessionId)
			throws BPNLPConnectorException {
		return nlpService.invokeNLPEngine(message, sourceLanguage, sessionId);
	}

}
