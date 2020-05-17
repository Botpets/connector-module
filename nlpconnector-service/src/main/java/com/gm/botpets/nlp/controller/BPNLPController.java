package com.gm.botpets.nlp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gm.botpets.nlp.exception.BPNLPConnectorException;
import com.gm.botpets.nlp.model.BPNLPRequest;
import com.gm.botpets.nlp.service.IBPNLPService;
import com.gm.botpets.nlp.util.WSUtil;

@RestController
@RequestMapping(path = "/botpets")
public class BPNLPController {

	@Autowired
	private IBPNLPService bpNLPService;

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Object> getIntent(@RequestBody BPNLPRequest bpNLPRequest) {
		//NLPResponse response = WSUtil.getDefaultAPIResponse();
		Object result = null;
		try {
			result = bpNLPService.processBPNLPRequest(bpNLPRequest);
		} catch (BPNLPConnectorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return WSUtil.getResponseEntity(result);
	}

	@GetMapping(value = "/ping")
	public String checkHealth() {
		return "I am Ok";
	}
}
