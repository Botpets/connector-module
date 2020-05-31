package com.gm.botpets.nlp.service;

import com.gm.botpets.nlp.exception.BPNLPConnectorException;
import com.gm.botpets.nlp.model.BPNLPRequest;

public interface IBPNLPService {

	public Object processBPNLPRequest(BPNLPRequest bpNLPRequestJson) throws BPNLPConnectorException;
	
	public String translateMessage(String message, String sourceLanguage, String targetLanguage);
	
	public String detectLanguage(String message) throws BPNLPConnectorException;
}
