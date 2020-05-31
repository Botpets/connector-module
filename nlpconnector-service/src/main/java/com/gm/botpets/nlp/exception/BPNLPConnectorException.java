package com.gm.botpets.nlp.exception;

public class BPNLPConnectorException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String message;

	public BPNLPConnectorException(String message) {
		super();
		this.message = message;
	}

	public BPNLPConnectorException(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
