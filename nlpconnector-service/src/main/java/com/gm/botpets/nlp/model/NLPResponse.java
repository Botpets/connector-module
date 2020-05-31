package com.gm.botpets.nlp.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NLPResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Object data;
	private String message;
	private boolean status;
}
