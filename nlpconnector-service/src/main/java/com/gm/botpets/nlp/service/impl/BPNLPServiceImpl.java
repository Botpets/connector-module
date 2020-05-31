package com.gm.botpets.nlp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.gm.botpets.nlp.exception.BPNLPConnectorException;
import com.gm.botpets.nlp.handler.NLPEngineManager;
import com.gm.botpets.nlp.model.BPNLPRequest;
import com.gm.botpets.nlp.model.NLPEngine;
import com.gm.botpets.nlp.service.IBPNLPService;
import com.gm.botpets.nlp.service.INLPService;
import com.gm.botpets.nlp.util.LanguagueUtil;

@Component
public class BPNLPServiceImpl implements IBPNLPService {

	private static Logger logger = LoggerFactory.getLogger(BPNLPServiceImpl.class);

	@Autowired
	private Environment env;

	@Autowired
	private NLPEngineManager nlpManager;

	@Autowired
	@Qualifier("watsonNLPService")
	private INLPService watsonNLPService;

	@Autowired
	@Qualifier("googleNLPService")
	private INLPService googleNLPService;

	@Autowired
	private LanguagueUtil languagueUtil;

	@Override
	public Object processBPNLPRequest(BPNLPRequest nlpRequest) throws BPNLPConnectorException {
		logger.debug("processBPNLPRequest started()\n" + nlpRequest.getQuestion() + nlpRequest.getChannel());
		NLPEngine nlpEngine = nlpRequest.getNlpEngine();

		if (nlpEngine == null) {
			String defaultNlpEngine = env.getProperty("default-nlp-engine");
			nlpEngine = NLPEngine.valueOf(defaultNlpEngine);
		}
		String userPreferredLangauge = nlpRequest.getUser().getPreferredLanguage();

		Object response = null;
		switch (nlpEngine) {
		case WATSON:
			nlpManager.setNlpService(watsonNLPService);
			response = nlpManager.invokeNLPEngine(nlpRequest.getQuestion(), userPreferredLangauge,
					nlpRequest.getSessionId());
			break;
		case GOOGLE:
			nlpManager.setNlpService(googleNLPService);
			response = nlpManager.invokeNLPEngine(nlpRequest.getQuestion(), userPreferredLangauge,
					nlpRequest.getSessionId());
			break;
		}
		logger.debug("processBPNLPRequest ended()\n" + "Response Message From NLP:" + response);
		return response;

	}

	@Override
	public String translateMessage(String message, String sourceLanguage, String targetLanguage) {
		return languagueUtil.translate(message, sourceLanguage, targetLanguage);
	}

	@Override
	public String detectLanguage(String message) {
		String detectedLanguage = "en"; // Default language
		try {
			detectedLanguage = languagueUtil.detectLanguage(message);
		} catch (BPNLPConnectorException e) {
			logger.error("Error while detecting language");
		}

		return detectedLanguage;
	}

}
