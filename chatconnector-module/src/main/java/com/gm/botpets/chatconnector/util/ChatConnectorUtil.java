package com.gm.botpets.chatconnector.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class ChatConnectorUtil {

	public String base64EncodedSessionId(String channelInfo) {
		final byte[] authBytes = channelInfo.getBytes(StandardCharsets.UTF_8);
		final String encoded = Base64.getEncoder().encodeToString(authBytes);
		return encoded;
	}

}
