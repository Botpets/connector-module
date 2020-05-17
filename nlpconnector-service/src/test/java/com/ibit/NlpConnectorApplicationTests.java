//package com.ibit;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.gm.botpets.nlp.exception.BPNLPConnectorException;
//import com.gm.botpets.nlp.model.Channel;
//import com.gm.botpets.nlp.model.ChannelType;
//import com.gm.botpets.nlp.model.BPNLPRequest;
//import com.gm.botpets.nlp.model.User;
//import com.gm.botpets.nlp.service.IBPNLPService;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class NlpConnectorApplicationTests {
//
//	@Autowired
//	IBPNLPService bpNLPService;
//
//	//@Test
//	public void test_nlp_service() {
//		//BPNLPRequest request = constructRequest();
//		
//		try {
//			bpNLPService.processBPNLPRequest(request);
//		} catch (BPNLPConnectorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	private BPNLPRequest constructRequest() {
//		BPNLPRequest request = new BPNLPRequest();
//		
//		User user = new User();
//		user.setPhoneNo("1213131113");
//		user.setEmail("test@gmail.com");
//		request.setUser(user);
//		
//		Channel channel = new Channel();
//		channel.setChannelType(ChannelType.SLACK);
//		channel.setChannelId("aa121212");
//		request.setChannel(channel);
//		
//		request.setQuestion("Awesome Thankyou!");
//		
//		return request;
//	}
//	
//}
