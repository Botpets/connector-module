package com.gm.botpets.nlp.util;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gm.botpets.nlp.exception.BPNLPConnectorException;
import com.google.common.base.Optional;
import com.ibm.watson.developer_cloud.language_translator.v3.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.developer_cloud.language_translator.v3.model.TranslationResult;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

@Component
public class LanguagueUtil {
	private static Logger logger = LoggerFactory.getLogger(LanguagueUtil.class);

	public String translate(String inputText, String source, String target) {
		IamOptions options = new IamOptions.Builder().apiKey("NhiekaaCzxUr18L86uNwdsXUbk5uZDw0zDQBCPy8pgfV").build();
		LanguageTranslator service = new LanguageTranslator("2018-09-20", options);
		// API Key = EOR8lJyHC2ki3VaRrTaCKU-xxZ4XIHL0YyY9v_Lzi1wc
		service.setEndPoint("https://gateway-lon.watsonplatform.net/language-translator/api");
		TranslateOptions translateOptions = new TranslateOptions.Builder().addText(inputText).source(source)
				.target(target).build();
		TranslationResult translationResult = service.translate(translateOptions).execute();
		String result = translationResult.getTranslations().get(0).getTranslationOutput();
		logger.info("Actual message: " + inputText + " --> Translated message: " + result);
		return result;
	}

	public String detectLanguage(String message) throws BPNLPConnectorException {
		String detectedLanguage = "en"; // Default
		try {
			List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();
			// build language detector:
			LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
					.withProfiles(languageProfiles).build();

			// create a text object factory
			TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

			TextObject textObject = textObjectFactory.forText(message);
			Optional<LdLocale> lang = languageDetector.detect(textObject);
			if (lang.isPresent()) {
				detectedLanguage = lang.get().getLanguage();
				logger.info("*************** Detected language *********** : " + detectedLanguage);
			} else {
				// set default language
				detectedLanguage = "en";
				logger.info("*************** Couldn't detect the language *********** : " + detectedLanguage);
			}
		} catch (IOException e) {
			throw new BPNLPConnectorException("Failed to detect the language");
		}
		return detectedLanguage;
	}
}
