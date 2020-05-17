package com.gm.botpets.nlp.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WatsonConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String url;
	private String version;
	private String workspaceId;
}
