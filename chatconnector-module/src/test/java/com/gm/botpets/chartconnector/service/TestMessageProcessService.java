package com.gm.botpets.chartconnector.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gm.botpets.chatconnector.model.NLPRequest;
import com.gm.botpets.chatconnector.service.IMessageProcessor;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class TestMessageProcessService {

	@Autowired
	IMessageProcessor messageProcessor;

	@Test
	public void test_message_processor() {
		NLPRequest nlpRequest = new NLPRequest();
		nlpRequest.setQuestion("check balance");

		try {
			messageProcessor.processMessageRequest(nlpRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
