package com.gm.botpets.chatconnector.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String channelId;
	private ChannelType channelType;
}
