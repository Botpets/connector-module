package com.gm.botpets.chatconnector.commons;

import static spark.Spark.stop;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;


public class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {
	private static final Logger logger = LoggerFactory.getLogger(GracefulShutdown.class);
	private volatile Connector connector;

	@Override
	public void customize(Connector connector) {
		this.connector = connector;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
			try {
				//Shutdown Spark's Embedded Jetty server  
				logger.info("Initiating graceful shutdown");
				stop();
				logger.info("Graceful shutdown success");
			} catch (Exception ex) {
				logger.info("Graceful shutdown failed");
				Thread.currentThread().interrupt();
		}
	}
}